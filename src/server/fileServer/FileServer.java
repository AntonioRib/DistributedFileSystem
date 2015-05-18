package server.fileServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebService;

import fileSystem.InfoNotFoundException;

@WebService
public interface FileServer extends Remote {
    
    	String getName() throws RemoteException;
    	
    	String getHost() throws RemoteException;

	/**
	 * Lista nome de ficheiros num dado directorio
	 */
	List<String> dir(String path) throws RemoteException, InfoNotFoundException;

	/**
	 * Devolve informacao sobre ficheiro.
	 */
	List<String> getFileInfo(String path) throws RemoteException,
			InfoNotFoundException;

	/**
	 * Devolve um booleano indicando se criou ou nao o directorio
	 * 
	 * @param name
	 * @return
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	boolean makeDir(String name) throws RemoteException, MalformedURLException, NotBoundException;

	/**
	 * Devolve um booleano indicando se eliminou ou nao o ficheiro/directorio
	 * 
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	boolean removeFile(String name, boolean isFile) throws RemoteException;

	/**
	 * 
	 * @param fromPath
	 * @param toServer
	 * @param toIsURL
	 * @param toPath
	 * @return
	 * @throws FileNotFoundException
	 */
	boolean sendFile(String fromPath, String toServer, boolean toIsURL,
			String toPath) throws IOException;

	/**
	 * 
	 * @param toPath
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	byte[] getFile(String toPath) throws IOException;
	
	/**
	 * 
	 * @param toPath
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	boolean receiveFile(String toPath, byte[] data) throws IOException;
}
