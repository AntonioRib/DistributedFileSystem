package server.contactServer;

import java.net.URL;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.ServerClass;
import server.ServerInfo;
import server.fileServer.FileServerClass;

public class ContactServerClass extends ServerClass implements ContactServer {
	
	
	//private Map<String, List<ServerInfo>> swarm;
	private List<ServerInfo> servers;

	
	protected ContactServerClass() throws RemoteException {
		super();
	//	this.swarm = new HashMap<String, List<ServerInfo>>();
		this.servers = new ArrayList<ServerInfo>();
		servers.add(new ServerInfo("potato", "catota"));
	}
	
	public List<ServerInfo> getServers() throws RemoteException {
		return servers; 
	}
	
	@SuppressWarnings("deprecation")
	public static void main( String args[]) throws Exception {
		try {
			String path = ".";
			if( args.length > 0)
				path = args[0];

			System.getProperties().put("java.security.policy", "src/server/policy.all");

			if( System.getSecurityManager() == null) {
				System.setSecurityManager( new RMISecurityManager());
			}

			try { // start rmiregistry
				LocateRegistry.createRegistry( 1099);
			} catch( RemoteException e) { 
				// if not start it
				// do nothing - already started with rmiregistry
			}

			ContactServerClass server = new ContactServerClass();
			Naming.rebind( "/contactServer", server);
			System.out.println( "ContactServer bound in registry");
		} catch( Throwable th) {
			th.printStackTrace();
		}
	}
	
	
}
