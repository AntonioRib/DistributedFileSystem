package server.fileServer.ws;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import server.ServerInfo;
import server.contactServer.rmi.ContactServerRMI;
import server.contactServer.ws.ContactServerWS;
import server.contactServer.ws.services.ContactServerClassWS;
import server.contactServer.ws.services.ContactServerClassWSService;
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

		ContactServerClassWSService css;
		try {
			css = new ContactServerClassWSService(
					new URL("http:"+contactServerURL+"?wsdl"),
					new QName("http://ws.contactServer.server/", "ContactServerClassWSService"));
			ContactServerClassWS cs = css.getContactServerClassWSPort();
			cs.addFileServer(this.getHost(), this.getName());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		new Thread() {
			public void run() {
				heartbeat();
			}
		}.start();
	}

	private void heartbeat(){
		try {
			for (;;) {
				try {
					ContactServerClassWSService css = new ContactServerClassWSService(
							new URL("http:"+contactServerURL+"?wsdl"),
							new QName("http://ws.contactServer.server/", "ContactServerClassWSService"));
					ContactServerClassWS cs = css.getContactServerClassWSPort();
					cs.receiveAliveSignal(this.getHost(), this.getName());;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

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
			if (args.length < 1) {
				System.out
				.println("Use: java server.fileServer.FileServerClass serverName");
				return;
			}

			String name = args[0];

			System.getProperties().put("java.security.policy", "policy.all");

			InetAddress group = InetAddress.getByName("239.255.255.255");
			MulticastSocket sock = new MulticastSocket(5000);
			sock.joinGroup(group);

			byte buf[] = new byte[128];
			DatagramPacket contactServerResponse = new DatagramPacket(buf, buf.length);
			sock.receive(contactServerResponse);

			FileServerWS fs = new FileServerClassWS(name, new String(contactServerResponse.getData()).trim());
			Endpoint.publish(
					"http://"+fs.getHost()+":8080/"+fs.getName(),
					fs); 

			System.out.println("FileServer bound in registry");
			System.out.println("//" + fs.getHost() + '/' + fs.getName());
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
