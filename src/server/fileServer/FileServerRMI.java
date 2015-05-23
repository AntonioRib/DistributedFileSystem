package server.fileServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import server.ServerInfo;
import server.contactServer.ContactServer;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

public class FileServerRMI extends UnicastRemoteObject implements FileServer {

    private static final long serialVersionUID = 1L;

    private String name, contactServerURL;
    private boolean isPrimary;

    protected FileServerRMI(String name, final String contactServerURL)
	    throws NotBoundException, InfoNotFoundException, IOException, WriteNotAllowedException {
	super();
	Naming.rebind('/' + name, this);
	this.contactServerURL = contactServerURL;
	this.name = name;
	this.isPrimary = false;
	
	
	((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName(), true);
	
	List<ServerInfo> brothers = ((ContactServer) Naming.lookup(contactServerURL)).getAllFileServersByName(name);
	
    	for (ServerInfo si: brothers) {
    	    
    	    if (!si.getHost().equals(this.getHost())) {
    	    
        	    FileServer curr = ((FileServer) Naming.lookup(si.getAddress()));
        	    if (curr.isPrimary()) {
        		this.receiveFile(".sync", curr.getFile(".sync"), false);
        		
        		JSONParser parser = new JSONParser();
        		
        		try {
        		    Object obj = parser.parse(new FileReader(".sync"));
        		    JSONObject meta = (JSONObject) obj;
        		    
        		    // ....
        		    
        		    JSONArray files = (JSONArray) meta.get("files");
        		    for (int i = 0; i < files.size(); i++) {
        			JSONObject file = (JSONObject) files.get(i);
        			String fileName = (String) file.get("name");
        			this.receiveFile(fileName, curr.getFile(fileName), false);
        		    }
        		    
        		} catch (ParseException e) {
        		    e.printStackTrace();
        		}
        		
        	    }
    	    }
    	}
	
	this.genMetadata();

	new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }

    @SuppressWarnings("unchecked")
    private void genMetadata() throws InfoNotFoundException, IOException {

	JSONObject meta = new JSONObject();
	meta.put("name", name);
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

    private void heartbeat() {
	try {
	    for (;;) {
		((ContactServer) Naming.lookup(contactServerURL))
			.receiveAliveSignal(getHost(), getName());
		System.out.println("Mandei");
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
	    throws RemoteException, MalformedURLException, NotBoundException,
	    WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	boolean response = FileSystem.makeDir(name);

	try {
	    this.genMetadata();
	} catch (InfoNotFoundException | IOException e) {
	    e.printStackTrace();
	}

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    getFileServer(true, sf.getAddress()).makeDir(name, false);
		}
	    }
	}

	return response;
    }

    public boolean removeFile(String path, boolean isFile, boolean propagate)
	    throws RemoteException, MalformedURLException, NotBoundException,
	    WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	boolean response = FileSystem.removeFile(path, isFile);

	try {
	    this.genMetadata();
	} catch (InfoNotFoundException | IOException e) {
	    e.printStackTrace();
	}

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    getFileServer(true, sf.getAddress()).removeFile(name,
			    isFile, false);
		}
	    }
	}

	return response;
    }

    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
	    String toPath) throws IOException, NotBoundException,
	    WriteNotAllowedException {

	byte[] in = FileSystem.getData(fromPath);

	return getFileServer(toIsURL, toServer).receiveFile(toPath, in, false);
    }

    public byte[] getFile(String fromPath) throws IOException {
	return FileSystem.getData(fromPath);
    }

    public boolean receiveFile(String toPath, byte[] data, boolean propagate)
	    throws IOException, NotBoundException, WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	boolean response = FileSystem.createFile(toPath, data);

	try {
	    this.genMetadata();
	} catch (InfoNotFoundException e) {
	    e.printStackTrace();
	}

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    getFileServer(true, sf.getAddress()).receiveFile(toPath,
			    data, false);
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
