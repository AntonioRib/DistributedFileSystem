package server.contactServer.ws;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import server.ServerInfoClass;
import server.ws.ServerClassWS;
import fileSystem.FileSystem;

@WebService
public class ContactServerClassWS extends ServerClassWS implements ContactServerWS {

	public static final int HEARTBEATLIMIT = 1500;

	private static final long serialVersionUID = 1L;

	private ConcurrentMap<String, ConcurrentMap<String, ServerInfoClass>> servers;

	public ContactServerClassWS() {
		super("contactServer");
		servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfoClass>>();
	}


	public ContactServerClassWS(String name) {
		super(name);
		servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfoClass>>();
	}

	@Override
	@WebMethod
	public String[] listFileServers() {
		updateAndGetServers();
		if (servers.isEmpty())
			return null;
		return servers.keySet().toArray(new String[servers.size()]);
	}

	@Override
	@WebMethod
	public String[] getFileServersByName(String name) {

		ConcurrentMap<String, ServerInfoClass> tmp = updateAndGetServers(name);

		if (tmp == null) return null;

		return tmp.keySet().toArray(new String[tmp.size()]);
	}

	@Override
	@WebMethod
	public void addFileServer(String host, String name) {
		System.out.println("Adicionei o servidor "+host+" com o nome"+name);
		ServerInfoClass fs = new ServerInfoClass("//" + host + "/" + name);
		ConcurrentMap<String, ServerInfoClass> tmp = updateAndGetServers(name);
		if (tmp == null)
			servers.put(name, new ConcurrentHashMap<String, ServerInfoClass>());
		servers.get(name).put(host, fs);
	}

	@Override
	@WebMethod
	public ServerInfoClass getFileServerByURL(String URL) {
		String[] address = URL.substring(2).split("/");
		return servers.get(address[1]).get(address[0]);
	}

	@Override
	@WebMethod
	public ServerInfoClass getFileServerByName(String name) {
		ConcurrentMap<String, ServerInfoClass> tmp = updateAndGetServers(name);

		if (tmp == null)
			return null;

		ServerInfoClass[] fss = tmp.values().toArray(new ServerInfoClass[tmp.size()]);
		return fss[new Random().nextInt(fss.length)];
	}

	@Override
	@WebMethod
	public void receiveAliveSignal(String host, String name) {
		ConcurrentMap<String, ServerInfoClass> tmp = updateAndGetServers(name);

		if (tmp != null)
			tmp.get(host).setLastHeartbeat(System.currentTimeMillis());
		//	System.out.println("Recieving Heartbeat from: "+host+"/"+name);
	}

	@WebMethod
	private ConcurrentMap<String, ServerInfoClass> updateAndGetServers(String name) {
		ConcurrentMap<String, ServerInfoClass> serversName = servers.get(name);

		if (servers.isEmpty())
			return null;

		for(ServerInfoClass server : serversName.values()){
			System.out.println(server.getServerHost());
			System.out.println(server.getServerName());
			if(System.currentTimeMillis() - server.getLastHeartbeat() > HEARTBEATLIMIT){
				serversName.remove(server.getServerHost());
				System.out.println("Heartbeat time: "+(System.currentTimeMillis() - server.getLastHeartbeat()));
				System.out.println("Servidor não deu sinal. Servidor eliminado.");

			}
		}

		servers.put(name, serversName);

		if(serversName.size() == 0){
			servers.remove(name);
			System.out.println("Nome não possui servidores. Nome eliminado.");
		}

		return servers.get(name);
	}

	@WebMethod
	private void updateAndGetServers() {
		for(String serverName : servers.keySet()){
			updateAndGetServers(serverName);
		}
	}

	@WebMethod(exclude=true)
	public static void main(String args[]) throws Exception {
		try {
			final ContactServerWS cs = new ContactServerClassWS();
			Endpoint.publish(
					"http://"+cs.getHost()+"/"+cs.getName(),
					cs); 

			final InetAddress group = InetAddress.getByName("239.255.255.255");
			final MulticastSocket sock = new MulticastSocket(5000);
			sock.joinGroup(group);

			new Thread() {
				public void run() {
					try {

						String broadcast = "//" + cs.getHost() + '/' + "contactServer";

						for (;;) {
							sock.send( new DatagramPacket(broadcast.getBytes(), broadcast.length(), group, 5000) );
							//System.out.println("Broadcasting!");
							Thread.sleep(2500);
						}

					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();

			System.out.println("ContactServer bound in registry");
			System.out.println("//" + cs.getHost() + '/' + cs.getName());

		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

}
