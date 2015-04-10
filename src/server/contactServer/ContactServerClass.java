package server.contactServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import server.ServerClass;
import server.ServerInfo;
import server.ServerInfoClass;

public class ContactServerClass extends ServerClass implements ContactServer {

    private static final long serialVersionUID = 1L;

    private ConcurrentMap<String, ConcurrentMap<String, ServerInfo>> servers;

    protected ContactServerClass(String name) throws RemoteException,
	    MalformedURLException {
	super(name);
	Naming.rebind('/' + name, this);
	servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfo>>();
    }

    public String[] listFileServers() throws RemoteException {
	updateAndGetServers();
	if (servers.isEmpty())
	    return null;
	return servers.keySet().toArray(new String[servers.size()]);
    }

    public String[] getFileServersByName(String name) throws RemoteException,
	    UnknownHostException {

	ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);
	
	if (tmp == null) return null;
	
	return tmp.keySet().toArray(new String[tmp.size()]);
    }

    public void addFileServer(String host, String name) throws RemoteException,
	    UnknownHostException {
	ServerInfo fs = new ServerInfoClass("//" + host + "/" + name);
	ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);
	
	if (tmp == null)
	    servers.put(name, new ConcurrentHashMap<String, ServerInfo>());
	
	servers.get(name).put(host, fs);
    }

    public ServerInfo getFileServerByURL(String URL) throws RemoteException,
	    UnknownHostException {
	String[] address = URL.substring(2).split("/");
	return servers.get(address[1]).get(address[0]);
    }

    public ServerInfo getFileServerByName(String name) throws RemoteException {
	ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);
	
	if (tmp == null)
	    return null;
	
	ServerInfo[] fss = tmp.values().toArray(new ServerInfo[tmp.size()]);
	return fss[new Random().nextInt(fss.length)];
    }

    public void receiveAliveSignal(String host, String name)
	    throws RemoteException {
	ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);
	
	if (tmp != null)
	    tmp.get(host).setLastHeartbeat(System.currentTimeMillis());
//	System.out.println("Recieving Heartbeat from: "+host+"/"+name);
    }
    
    private ConcurrentMap<String, ServerInfo> updateAndGetServers(String name) {
	ConcurrentMap<String, ServerInfo> serversName = servers.get(name);
	
	if (servers.isEmpty())
	    return null;
	
	for(ServerInfo server : serversName.values()){
	    if(System.currentTimeMillis() - server.getLastHeartbeat() > 5000){
		serversName.remove(server.getHost());
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
    
    private void updateAndGetServers() {
	for(String serverName : servers.keySet()){
	     	updateAndGetServers(serverName);
	}
    }

    @SuppressWarnings("deprecation")
    public static void main(String args[]) throws Exception {
	try {

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
	    
	    final ContactServer cs = new ContactServerClass("contactServer");
	    
	    final InetAddress group = InetAddress.getByName("239.255.255.255");
	    final MulticastSocket sock = new MulticastSocket(5000);
	    sock.joinGroup(group);
	    
	    new Thread() {
		public void run() {
        	    try {
        		
        		String broadcast = "//" + cs.getHost() + '/' + "contactServer";
        		
        		for (;;) {
        		    sock.send( new DatagramPacket(broadcast.getBytes(), broadcast.length(), group, 5000) );
        		    System.out.println("Broadcasting!");
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
