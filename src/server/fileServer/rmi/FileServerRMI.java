package server.fileServer.rmi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import fileSystem.FileInfo;
import fileSystem.InfoNotFoundException;
import server.rmi.ServerRMI;
import server.ws.ServerWS;

public interface FileServerRMI extends ServerRMI {

	/**
	 * Lista nome de ficheiros num dado directorio
	 */
	String[] dir(String path) throws RemoteException, InfoNotFoundException;

	/**
	 * Devolve informacao sobre ficheiro.
	 */
	FileInfo getFileInfo(String path) throws RemoteException,
			InfoNotFoundException;

	/**
	 * Devolve um booleano indicando se criou ou nao o directorio
	 * 
	 * @param name
	 * @return
	 */
	boolean makeDir(String name) throws RemoteException;

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
