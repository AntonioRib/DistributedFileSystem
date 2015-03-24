package server.contactServer;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import server.ServerClass;
import server.dataServer.DataServerClass;

public class ContactServerClass
		extends ServerClass
		implements ContactServer{

	protected ContactServerClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	public static void main( String args[]) throws Exception {
		try {
			String path = ".";
			if( args.length > 0)
				path = args[0];

			System.getProperties().put( "java.security.policy", "policy.all");

			if( System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			}

			try { // start rmiregistry
				LocateRegistry.createRegistry( 1099);
			} catch( RemoteException e) { 
				// if not start it
				// do nothing - already started with rmiregistry
			}

			 server = new ContactServerClass(path);
			Naming.rebind( "/myFileServer", server);
			System.out.println( "DirServer bound in registry");
		} catch( Throwable th) {
			th.printStackTrace();
		}
	}
	
	
}
