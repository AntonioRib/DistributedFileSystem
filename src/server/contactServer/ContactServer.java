package server.contactServer;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.ServerInfo;

public interface ContactServer extends Remote {

    String getName() throws RemoteException;

    String getHost() throws RemoteException;

    String[] listFileServers() throws RemoteException, MalformedURLException, NotBoundException;

    String[] getFileServersByName(String name) throws RemoteException,
            UnknownHostException, MalformedURLException, NotBoundException;
    
    ServerInfo getPrimaryServer(String name) throws RemoteException;

    ServerInfo getFileServerByURL(String URL) throws RemoteException,
            UnknownHostException;

    ServerInfo getFileServerByName(String name) throws RemoteException, MalformedURLException, NotBoundException;

    List<ServerInfo> getAllFileServersByName(String name)
            throws RemoteException, MalformedURLException, NotBoundException;

    void addFileServer(String host, String name, boolean isRMI)
            throws RemoteException, UnknownHostException, MalformedURLException, NotBoundException;

    void receiveAliveSignal(String host, String name) throws RemoteException, MalformedURLException, NotBoundException;

}
