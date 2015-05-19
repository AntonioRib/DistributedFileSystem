package server.fileServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.List;

import server.ServerInfo;
import server.contactServer.ContactServer;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

public class FileServerRMI extends UnicastRemoteObject implements FileServer {

    private static final long serialVersionUID = 1L;

    private String name, contactServerURL;

    protected FileServerRMI(String name, final String contactServerURL)
            throws RemoteException, MalformedURLException, NotBoundException,
            UnknownHostException {
        super();
        try {
        Naming.rebind('/' + name, this);
        this.contactServerURL = contactServerURL;
        this.name = name;

        ServerInfo principalServer = ((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
                this.getHost(), this.getName(), true);
        
        new Thread() {
            public void run() {
                heartbeat();
            }
        }.start();

        if(principalServer != null){
            List<String> dir = ((FileServer) Naming.lookup(principalServer.getAddress())).dir(".");
            
        }
        
        } catch (InfoNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void heartbeat() {
        try {
            for (;;) {
                ((ContactServer) Naming.lookup(contactServerURL))
                        .receiveAliveSignal(getHost(), getName());
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

    public boolean makeDir(String name, boolean propagate)
            throws RemoteException, MalformedURLException, NotBoundException {
        boolean response = FileSystem.makeDir(name);
        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this.getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())){
                    getFileServer(true, sf.getAddress()).makeDir(name, false);
                }
            }
        }

        return response;
    }

    public boolean removeFile(String path, boolean isFile, boolean propagate)
            throws RemoteException, MalformedURLException, NotBoundException {
        boolean response = FileSystem.removeFile(path, isFile);
        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this.getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())){
                    getFileServer(true, sf.getAddress()).removeFile(name, isFile, false);
                }
            }
        }

        return response;
    }

    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
            String toPath) throws IOException, NotBoundException {

        byte[] in = FileSystem.getData(fromPath);

        return getFileServer(toIsURL, toServer).receiveFile(toPath, in, true);
    }

    public byte[] getFile(String fromPath) throws IOException {
        return FileSystem.getData(fromPath);
    }

    public boolean receiveFile(String toPath, byte[] data, boolean propagate) throws IOException, NotBoundException {
        boolean response = FileSystem.createFile(toPath, data);
        if (propagate) {
            List<ServerInfo> serversList = ((ContactServer) Naming
                    .lookup(contactServerURL)).getAllFileServersByName(this.getName());
            for (ServerInfo sf : serversList) {
                if (!sf.getHost().equals(this.getHost())){
                    getFileServer(true, sf.getAddress()).receiveFile(toPath, data, false);
                }
            }
        }

        return response;
    }

    private FileServer getFileServer(boolean isURL, String server)
            throws RemoteException {
        try {
            ContactServer cs = ((ContactServer) Naming.lookup(contactServerURL));
            ServerInfo fs = isURL ? cs.getFileServerByURL(server) : cs
                    .getFileServerByName(server);
            if (fs.isRMI())
                return (FileServer) Naming.lookup(fs.getAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return getLocalhost().toString().substring(1);
    }

    private InetAddress getLocalhost() {
        try {
            try {
                Enumeration<NetworkInterface> e = NetworkInterface
                        .getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface n = e.nextElement();
                    Enumeration<InetAddress> ee = n.getInetAddresses();
                    while (ee.hasMoreElements()) {
                        InetAddress i = ee.nextElement();
                        if (i instanceof Inet4Address && !i.isLoopbackAddress())
                            return i;
                    }
                }
            } catch (SocketException e) {
                // do nothing
            }
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
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
