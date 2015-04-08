package server.fileServer.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import fileSystem.FileInfo;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;
import server.ServerInfo;
import server.contactServer.rmi.ContactServerRMI;
import server.rmi.ServerClassRMI;

public class FileServerClassRMI extends ServerClassRMI implements FileServerRMI {

    private static final long serialVersionUID = 1L;

    private String contactServerURL;

    protected FileServerClassRMI(String name, final String contactServerURL)
	    throws RemoteException, MalformedURLException, NotBoundException,
	    UnknownHostException {
	
	super(name);
	Naming.rebind('/' + name, this);
	this.contactServerURL = contactServerURL;
	
	((ContactServerRMI) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName());

	new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }

    private void heartbeat(){
	try {
	    for (;;) {
		((ContactServerRMI) Naming.lookup(contactServerURL))
			.receiveAliveSignal(getHost(), getName());
		Thread.sleep(1000);
	    }
	} catch (RemoteException | MalformedURLException
		| NotBoundException | InterruptedException e) {
	    e.printStackTrace();
	}
    }
    
    @Override
    public String[] dir(String path) throws RemoteException,
	    InfoNotFoundException {
	return FileSystem.dir(path);
    }

    @Override
    public FileInfo getFileInfo(String path) throws RemoteException,
	    InfoNotFoundException {
	return FileSystem.getFileInfo(path);
    }

    @Override
    public boolean makeDir(String name) throws RemoteException {
	return FileSystem.makeDir(name);
    }

    @Override
    public boolean removeFile(String path, boolean isFile)
	    throws RemoteException {
	return FileSystem.removeFile(path, isFile);
    }

    @Override
    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
	    String toPath) throws IOException {

	byte[] in = FileSystem.getData(fromPath);

	return getFileServer(toIsURL, toServer).receiveFile(toPath, in);
    }

    @Override
    public byte[] getFile(String fromPath) throws IOException {
	return FileSystem.getData(fromPath);
    }

    @Override
    public boolean receiveFile(String toPath, byte[] data) throws IOException {
	return FileSystem.createFile(toPath, data);
    }

    private FileServerRMI getFileServer(boolean isURL, String server)
	    throws RemoteException {
	try {
	    ContactServerRMI cs = ((ContactServerRMI) Naming.lookup(contactServerURL));
	    ServerInfo fs = isURL ? cs.getFileServerByURL(server) : cs
		    .getFileServerByName(server);
	    return (FileServerRMI) Naming.lookup(fs.getAddress());
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    e.printStackTrace();
	}

	return null;
    }

    @SuppressWarnings("deprecation")
    public static void main(String args[]) throws Exception {
	try {
	    if (args.length < 2) {
		System.out
			.println("Use: java server.fileServer.FileServerClass serverName contactServerURL");
		return;
	    }

	    String name = args[0];
	    String contactServerURL = args[1];

	    System.getProperties().put("java.security.policy", "policy.all");

	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new java.rmi.RMISecurityManager());
	    }

	    try { // start rmiregistry
		LocateRegistry.createRegistry(4000);
	    } catch (RemoteException e) {
		// if not start it
		// do nothing - already started with rmiregistry
	    }

	    FileServerRMI fs = new FileServerClassRMI(name, contactServerURL);
	    System.out.println("FileServer bound in registry");
	    System.out.println("//" + fs.getHost() + '/' + fs.getName());
	} catch (Throwable th) {
	    th.printStackTrace();
	}
    }

}
