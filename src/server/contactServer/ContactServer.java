package server.contactServer;

import java.rmi.RemoteException;
import java.util.List;

import server.Server;
import server.ServerInfo;

public interface ContactServer extends Server {
	//Interface que conter� todos os m�todos resp�ctivos ao servidor de contacto.
	String[] getServers() throws RemoteException;
	String[] getURL(String name) throws RemoteException;
}
