package server.fileServer.ws;

import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import server.ServerInfo;
import server.contactServer.rmi.ContactServerRMI;
import server.fileServer.rmi.FileServerRMI;
import server.ws.ServerClassWS;
import fileSystem.FileInfo;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

@WebService
public class FileServerClassWS extends ServerClassWS implements FileServerWS {

	private static final long serialVersionUID = 1L;

	private String contactServerURL;

	public FileServerClassWS() {

		super("SD");
		this.contactServerURL = "//localhost/fileServer";

		/*((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName());*/

		/*new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }*/
	}
	
	public FileServerClassWS(String name, final String contactServerURL) {

		super(name);
		this.contactServerURL = contactServerURL;

		/*((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName());*/

		/*new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }*/
	}

	/* private void heartbeat(){
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
    }*/

	@Override
	@WebMethod
	public String getName(){
		return name;
	}

	@Override
	@WebMethod
	public String getHost() {
		return getLocalhost().toString().substring(1);
	}

	@WebMethod
	@Override
	public String[] dir(String path) throws InfoNotFoundException{
		return FileSystem.dir(path);
	}

	@WebMethod
	@Override
	public FileInfo getFileInfo(String path) throws InfoNotFoundException{
		return FileSystem.getFileInfo(path);
	}

	@WebMethod
	@Override
	public boolean makeDir(String name){
		return FileSystem.makeDir(name);
	}

	@WebMethod
	@Override
	public boolean removeFile(String path, boolean isFile) {
		return FileSystem.removeFile(path, isFile);
	}

	@WebMethod
	@Override
	public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
			String toPath) throws IOException {

		byte[] in = FileSystem.getData(fromPath);

		return getFileServer(toIsURL, toServer).receiveFile(toPath, in);
	}

	@WebMethod
	@Override
	public byte[] getFile(String fromPath) throws IOException {
		return FileSystem.getData(fromPath);
	}

	@WebMethod
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

	@WebMethod(exclude=true)
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

			FileServerWS fs = new FileServerClassWS(name, contactServerURL);
			Endpoint.publish(
					"http://localhost:8000/FileServer",
					fs);
			
			System.out.println("FileServer bound in registry");
			System.out.println("//" + fs.getHost() + '/' + fs.getName());
		}catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
