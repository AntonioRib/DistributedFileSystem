package server.fileServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import server.ServerInfo;
import server.ServerUtils;
import server.contactServer.ContactServer;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

public class FileServerRMI extends UnicastRemoteObject implements FileServer {

    private static final long serialVersionUID = 1L;

    private String name, contactServerURL;
    private boolean isPrimary;

    protected FileServerRMI(String name, final String contactServerURL)
            throws NotBoundException, InfoNotFoundException, IOException,
            WriteNotAllowedException {
        super();
        Naming.rebind('/' + name, this);
        this.contactServerURL = contactServerURL;
        this.name = name;
        this.isPrimary = false;

        this.genMetadata();
        this.sync();

        ((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
                this.getHost(), this.getName(), true);

        new Thread() {
            public void run() {
                heartbeat();
            }
        }.start();
    }

    private void sync() throws NotBoundException, IOException,
            WriteNotAllowedException, InfoNotFoundException {

        ServerInfo si = ((ContactServer) Naming.lookup(contactServerURL))
                .getPrimaryServer(name);

        if (si == null)
            return;

        if (!si.getHost().equals(this.getHost())) {

            FileServer curr = ((FileServer) Naming.lookup(si.getAddress()));
            this.receiveFile(".sync.tmp", curr.getFile(".sync"), false, false);

            JSONParser parser = new JSONParser();

            try {
                Object obj = parser.parse(new FileReader(".sync.tmp"));
                JSONObject meta = (JSONObject) obj;
                JSONArray files = (JSONArray) meta.get("files");

                for (int i = 0; i < files.size(); i++) {

                    // GETS ROOT CONTENTS
                    JSONObject file = (JSONObject) files.get(i);
                    String fileName = (String) file.get("name");
                    boolean fileIsDir = (boolean) file.get("is_dir");

                    if (fileIsDir)
                        this.syncDir(fileName, curr);
                    else
                        this.receiveFile(fileName, curr.getFile(fileName),
                                false, false);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.removeFile(".sync.tmp", true, false, false);
        }
    }

    private void syncDir(String path, FileServer primary)
            throws InfoNotFoundException, IOException, NotBoundException,
            WriteNotAllowedException {

        this.makeDir(path, false, false);

        List<String> contents = primary.dir(path);
        for (String name : contents) {

            String newPath = path + '/' + name;
            boolean fileIsDir = Boolean.parseBoolean(primary
                    .getFileInfo(newPath).get(3).substring(7));

            if (fileIsDir)
                this.syncDir(newPath, primary);
            else
                this.receiveFile(newPath, primary.getFile(newPath), false,
                        false);
        }

    }

    @SuppressWarnings("unchecked")
    private void genMetadata() throws InfoNotFoundException, IOException {

        JSONObject meta = new JSONObject();
        meta.put("name", name);
        meta.put("host", this.getHost());
        meta.put("is_primary", isPrimary);

        JSONArray files = new JSONArray();
        List<String> contents = this.dir(".");

        for (String fileName : contents) {
            File f = new File(fileName);
            JSONObject file = new JSONObject();
            file.put("date_modified", new Date(f.lastModified()).toString());
            file.put("is_dir", f.isDirectory());
            file.put("name", fileName);
            files.add(file);
        }

        meta.put("files", files);

        FileWriter toFile = new FileWriter(".sync");
        try {
            toFile.write(meta.toJSONString());
            System.out.println("Successfully generated metadata...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            toFile.flush();
            toFile.close();
        }
    }

    private void removeMetadata(File f) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(".sync"));
        JSONObject meta = (JSONObject) obj;
        JSONArray files = (JSONArray) meta.get("files");
        Iterator it = files.iterator();
        while (it.hasNext()) {
            JSONObject file = (JSONObject) it.next();
            System.out.println("FILE -> "+file.get("name"));
            System.out.println("F -> "+f.getName());
            if(((String)file.get("name")).equalsIgnoreCase(f.getName())){
            	it.remove();
            }
        }
        FileWriter fw = new FileWriter(".sync");
        fw.write(meta.toJSONString());
        fw.flush();
        fw.close();
    }

    private void addMetadata(File f) throws FileNotFoundException, IOException,
            ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(".sync"));
        JSONObject meta = (JSONObject) obj;
        JSONArray files = (JSONArray) meta.get("files");
        JSONObject file = new JSONObject();
        file.put("date_modified", new Date(f.lastModified()).toString());
        file.put("is_dir", f.isDirectory());
        file.put("name", f.getName());
        files.add(file);
        FileWriter fw = new FileWriter(".sync");
        fw.write(meta.toJSONString());
        fw.flush();
        fw.close();
    }

    private void heartbeat() {
        try {
            for (;;) {
                ((ContactServer) Naming.lookup(contactServerURL))
                        .receiveAliveSignal(getHost(), getName());
                System.out.println("Sent alive signal");
                Thread.sleep(1000);
            }
        } catch (RemoteException | MalformedURLException | NotBoundException
                | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> dir(String path) throws RemoteException,
            InfoNotFoundException {
        return FileSystem.dir(path);
    }

    public List<String> getFileInfo(String path) throws RemoteException,
            InfoNotFoundException {
        return FileSystem.getFileInfo(path);
    }

    public boolean makeDir(String name, boolean propagate, boolean writeMetadata)
            throws RemoteException, MalformedURLException, NotBoundException,
            WriteNotAllowedException {

        if (!isPrimary && propagate) {
            throw new WriteNotAllowedException();
        }

        boolean response = FileSystem.makeDir(name);
        try {
            if (response && writeMetadata)
                this.addMetadata(new File(name));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this
                    .getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())) {
                    ServerUtils.getFileServer(contactServerURL, true,
                            sf.getAddress()).makeDir(name, false, true);
                }
            }
        }

        return response;
    }

    public boolean removeFile(String path, boolean isFile, boolean propagate,
            boolean writeMetadata) throws RemoteException,
            MalformedURLException, NotBoundException, WriteNotAllowedException {

        if (!isPrimary && propagate) {
            throw new WriteNotAllowedException();
        }

        boolean response = FileSystem.removeFile(path, isFile);
        try {
            if (response && writeMetadata)
                this.removeMetadata(new File(path));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this
                    .getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())) {
                    ServerUtils.getFileServer(contactServerURL, true,
                            sf.getAddress()).removeFile(path, isFile, false,
                            true);
                }
            }
        }

        return response;
    }

    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
            String toPath) throws IOException, NotBoundException,
            WriteNotAllowedException {

        byte[] in = FileSystem.getData(fromPath);

        if (toIsURL)
            return ServerUtils.getFileServer(contactServerURL, toIsURL,
                    toServer).receiveFile(toPath, in, false, true);

        return ServerUtils.getPrimaryFileServer(contactServerURL, toServer)
                .receiveFile(toPath, in, false, true);
    }

    public byte[] getFile(String fromPath) throws IOException {
        return FileSystem.getData(fromPath);
    }

    public boolean receiveFile(String toPath, byte[] data, boolean propagate,
            boolean writeMetadata) throws IOException, NotBoundException,
            WriteNotAllowedException {

        if (!isPrimary && propagate) {
            throw new WriteNotAllowedException();
        }

        boolean response = FileSystem.createFile(toPath, data);

        try {
            if (response && writeMetadata)
                this.addMetadata(new File(name));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this
                    .getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())) {
                    ServerUtils.getFileServer(contactServerURL, true,
                            sf.getAddress()).receiveFile(toPath, data, false,
                            true);
                }
            }
        }

        return response;
    }

    public boolean isPrimary() throws RemoteException {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) throws RemoteException {
        this.isPrimary = isPrimary;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return ServerUtils.getLocalhost().toString().substring(1);
    }

    @SuppressWarnings("deprecation")
    public static void main(String args[]) throws Exception {
        try {
            if (args.length < 1) {
                System.out
                        .println("Use: java server.fileServer.FileServerClass serverName or java server.fileServer.FileServerClass serverName contactServerAdress");
                return;
            }

            String name = args[0];

            System.getProperties().put("java.security.policy", "policy.all");

            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new java.rmi.RMISecurityManager());
            }

            try { // start rmiregistry
                LocateRegistry.createRegistry(1099);
            } catch (RemoteException e) {
                // if not start it
                // do nothing - already started with rmiregistry
            }

            FileServer fs;
            if (args.length == 1) {
                InetAddress group = InetAddress.getByName("239.255.255.255");
                MulticastSocket sock = new MulticastSocket(5000);
                sock.joinGroup(group);

                byte buf[] = new byte[128];
                DatagramPacket contactServerBroadcast = new DatagramPacket(buf,
                        buf.length);
                sock.receive(contactServerBroadcast);
                sock.close();

                System.out.println("Got broadcast from contact server!");

                fs = new FileServerRMI(name, new String(
                        contactServerBroadcast.getData()).trim());
            } else {
                fs = new FileServerRMI(name, args[1]);
            }

            System.out.println("FileServer bound in registry");
            System.out.println("//" + fs.getHost() + '/' + fs.getName());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
