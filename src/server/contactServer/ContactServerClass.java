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
	
	
	private Map<String, List<ServerInfo>> swarm;

	
	protected ContactServerClass() throws RemoteException {
		super();
		this.swarm = new HashMap<String, List<ServerInfo>>();
	}
	
	public String[] getServers() throws RemoteException {
		if(swarm.size() == 0){
			//throw new NoServersConnectedException("Não existem servers"); 
			return null;
		}
		
		return swarm.keySet().toArray(new String[swarm.size()]);
	}
	
	public String[] getURL(String name) throws RemoteException {
		if(swarm.size() == 0){
			//throw new NoServersConnectedException("Não existem servers"); 
			return null;
		}
		
		List<ServerInfo> servers = swarm.get(name);
		String[] serverURLs = new String[servers.size()]; 
		for(int i = 0; i<servers.size(); i++){
			serverURLs[i] = servers.get(i).getURL();
		}
		
		return serverURLs;
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
