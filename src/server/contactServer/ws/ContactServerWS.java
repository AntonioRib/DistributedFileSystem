package server.contactServer.ws;

import server.ServerInfo;
import server.ServerInfoClass;
import server.ws.ServerWS;

public interface ContactServerWS extends ServerWS {

	String[] listFileServers();

	String[] getFileServersByName(String name);

	ServerInfoClass getFileServerByURL(String URL);

	ServerInfoClass getFileServerByName(String name);

	void addFileServer(String host, String name);

	void receiveAliveSignal(String host, String name);

}
