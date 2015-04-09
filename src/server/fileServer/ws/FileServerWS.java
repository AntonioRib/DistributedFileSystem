package server.fileServer.ws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import server.ws.ServerWS;
import fileSystem.FileInfo;
import fileSystem.InfoNotFoundException;

public interface FileServerWS extends ServerWS {

	/**
	 * Lista nome de ficheiros num dado directorio
	 * @throws InfoNotFoundException 
	 */
	String[] dir(String path) throws InfoNotFoundException;

	/**
	 * Devolve informacao sobre ficheiro.
	 * @throws InfoNotFoundException 
	 */
	FileInfo getFileInfo(String path) throws InfoNotFoundException;

	/**
	 * Devolve um booleano indicando se criou ou nao o directorio
	 * 
	 * @param name
	 * @return
	 */
	boolean makeDir(String name);

	/**
	 * Devolve um booleano indicando se eliminou ou nao o ficheiro/directorio
	 * 
	 * @param name
	 * @return
	 * @throws RemoteException
	 */
	boolean removeFile(String name, boolean isFile);

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
