/**
 * 
 */
package server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRMI extends Remote {

	String getName() throws RemoteException;

	String getHost() throws RemoteException;

}
