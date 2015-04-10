package server.contactServer;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

import server.Server;
import server.ServerInfo;

public interface ContactServer extends Server {

	String[] listFileServers() throws RemoteException;

	String[] getFileServersByName(String name) throws RemoteException,
			UnknownHostException;

	ServerInfo getFileServerByURL(String URL) throws RemoteException,
			UnknownHostException;

	ServerInfo getFileServerByName(String name) throws RemoteException;

	void addFileServer(String host, String name) throws RemoteException,
			UnknownHostException;

	void receiveAliveSignal(String host, String name) throws RemoteException;

}
