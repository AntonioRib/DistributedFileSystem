package server.contactServer.ws;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

import server.ServerInfo;
import server.rmi.ServerRMI;
import server.ws.ServerInfoClassWS;
import server.ws.ServerWS;

public interface ContactServerWS extends ServerWS {

	String[] listFileServers();

	String[] getFileServersByName(String name);

	ServerInfo getFileServerByURL(String URL);

	ServerInfo getFileServerByName(String name);

	void addFileServer(String host, String name);

	void receiveAliveSignal(String host, String name);

}
