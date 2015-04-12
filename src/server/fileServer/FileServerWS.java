package server.fileServer;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.List;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import fileSystem.FileInfo;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;
import server.ServerInfo;
import server.contactServer.ContactServer;
import server.fileServer.services.FileServerWSService;

@WebService
public class FileServerWS implements FileServer {

    private String name, contactServerURL;
    
    public FileServerWS() {
	this.contactServerURL = "http://127.0.0.1/contactServer";
	this.name = "fileServer";
    }

    public FileServerWS(String name, final String contactServerURL)
	    throws RemoteException, UnknownHostException,
	    MalformedURLException, NotBoundException {
	super();
	Endpoint.publish("http://" + this.getHost() + "/" + name, this);
	this.contactServerURL = contactServerURL;
	this.name = name;

	((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName(), false);

	new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }
    
    @WebMethod
    private void heartbeat() {
	try {
	    for (;;) {
		((ContactServer) Naming.lookup(contactServerURL))
			.receiveAliveSignal(getHost(), getName());
		Thread.sleep(1000);
	    }
	} catch (InterruptedException | RemoteException | MalformedURLException
		| NotBoundException e) {
	    e.printStackTrace();
	}
    }

    @WebMethod
    public List<String> dir(String path) throws InfoNotFoundException {
	return FileSystem.dir(path);
    }

    @WebMethod
    public List<String> getFileInfo(String path) throws InfoNotFoundException {
	return FileSystem.getFileInfo(path);
    }

    @WebMethod
    public boolean makeDir(String name) {
	return FileSystem.makeDir(name);
    }

    @WebMethod
    public boolean removeFile(String path, boolean isFile) {
	return FileSystem.removeFile(path, isFile);
    }

    @WebMethod
    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
	    String toPath) throws IOException {

	byte[] in = FileSystem.getData(fromPath);

	return getFileServer(toIsURL, toServer).receiveFile(toPath, in);
    }

    @WebMethod
    public byte[] getFile(String fromPath) throws IOException {
	return FileSystem.getData(fromPath);
    }

    @WebMethod
    public boolean receiveFile(String toPath, byte[] data) throws IOException {
	return FileSystem.createFile(toPath, data);
    }

    @WebMethod
    private FileServer getFileServer(boolean isURL, String server)
	    throws RemoteException {
	try {
	    ContactServer cs = ((ContactServer) Naming.lookup(contactServerURL));
	    ServerInfo fs = isURL ? cs.getFileServerByURL(server) : cs
		    .getFileServerByName(server);
	    if(fs.isRMI())
		return (FileServer) Naming.lookup(fs.getAddress());
	    FileServerWSService css = new FileServerWSService(new URL("http://"+fs.getHost()+"/"+fs.getName()), new QName("http://fileServer.server/", "FileServerWSService"));
	    return (FileServer) css.getFileServerWSPort();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    e.printStackTrace();
	}

	return null;
    }

    @WebMethod
    public String getName() {
	return name;
    }

    @WebMethod
    public String getHost() {
	return getLocalhost().toString().substring(1);
    }

    @WebMethod
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

    @WebMethod(exclude = true)
    @SuppressWarnings("deprecation")
    public static void main(String args[]) throws Exception {
	try {
	    if (args.length < 1) {
		System.out
			.println("Use: java server.fileServer.FileServerClass serverName");
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

	    InetAddress group = InetAddress.getByName("239.255.255.255");
	    MulticastSocket sock = new MulticastSocket(5000);
	    sock.joinGroup(group);

	    byte buf[] = new byte[128];
	    DatagramPacket contactServerResponse = new DatagramPacket(buf,
		    buf.length);
	    sock.receive(contactServerResponse);

	    FileServer fs = new FileServerWS(name, new String(
		    contactServerResponse.getData()).trim());
	    System.out.println("FileServer bound in registry");
	    System.out.println("//" + fs.getHost() + '/' + fs.getName());
	} catch (Throwable th) {
	    th.printStackTrace();
	}
    }

}
