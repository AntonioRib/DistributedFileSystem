package server.contactServer.ws;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.jws.*;
import javax.xml.ws.Endpoint;

import server.rmi.ServerClassRMI;
import server.ws.ServerClassWS;
import server.ws.ServerInfoClassWS;

@WebService
public class ContactServerClassWS extends ServerClassWS implements ContactServerWS {

    public static final int HEARTBEATLIMIT = 1500;

    private static final long serialVersionUID = 1L;

    private ConcurrentMap<String, ConcurrentMap<String, ServerInfoClassWS>> servers;
    
    protected ContactServerClassWS(String name) {
	super(name);
	servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfoClassWS>>();
    }

    @WebMethod
    public String[] listFileServers() {
	updateAndGetServers();
	if (servers.isEmpty())
	    return null;
	return servers.keySet().toArray(new String[servers.size()]);
    }

    @WebMethod
    public String[] getFileServersByName(String name) {

	ConcurrentMap<String, ServerInfoClassWS> tmp = updateAndGetServers(name);
	
	if (tmp == null) return null;
	
	return tmp.keySet().toArray(new String[tmp.size()]);
    }

    @WebMethod
    public void addFileServer(String host, String name) {
	ServerInfoClassWS fs = new ServerInfoClassWS("//" + host + "/" + name);
	ConcurrentMap<String, ServerInfoClassWS> tmp = updateAndGetServers(name);
	if (tmp == null)
	    servers.put(name, new ConcurrentHashMap<String, ServerInfoClassWS>());
	servers.get(name).put(host, fs);
    }

    @WebMethod
    public ServerInfoClassWS getFileServerByURL(String URL) {
	String[] address = URL.substring(2).split("/");
	return servers.get(address[1]).get(address[0]);
    }

    @WebMethod
    public ServerInfoClassWS getFileServerByName(String name) {
	ConcurrentMap<String, ServerInfoClassWS> tmp = updateAndGetServers(name);
	
	if (tmp == null)
	    return null;
	
	ServerInfoClassWS[] fss = tmp.values().toArray(new ServerInfoClassWS[tmp.size()]);
	return fss[new Random().nextInt(fss.length)];
    }

    @WebMethod
    public void receiveAliveSignal(String host, String name) {
	ConcurrentMap<String, ServerInfoClassWS> tmp = updateAndGetServers(name);
	
	if (tmp != null)
	    tmp.get(host).setLastHeartbeat(System.currentTimeMillis());
//	System.out.println("Recieving Heartbeat from: "+host+"/"+name);
    }
    
    private ConcurrentMap<String, ServerInfoClassWS> updateAndGetServers(String name) {
	ConcurrentMap<String, ServerInfoClassWS> serversName = servers.get(name);
	
	if (servers.isEmpty())
	    return null;
	
	for(ServerInfoClassWS server : serversName.values()){
	    if(System.currentTimeMillis() - server.getLastHeartbeat() > HEARTBEATLIMIT){
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

    @WebMethod(exclude=true)
    public static void main(String args[]) throws Exception {
	try {
	    ContactServerWS cs = new ContactServerClassWS("contactServer");
	    Endpoint.publish(
		    "http://localhost:8080/ContactServer",
		    cs); 
	    
	    System.out.println("ContactServer bound in registry");
	    System.out.println("//" + cs.getHost() + '/' + cs.getName());

	} catch (Throwable th) {
	    th.printStackTrace();
	}
    }

}
