package server.fileServer;

import java.io.IOException;
import java.rmi.*;
import java.util.*;

import server.Server;
import client.FileInfo;

public interface FileServer extends Server
{
	/**
	 * Lista nome de ficheiros num dado directorio
	 */
	public String[] dir( String path) throws RemoteException, InfoNotFoundException;
	
	/**
	 * Devolve informacao sobre ficheiro.
	 */
	public FileInfo getFileInfo( String path, String name) throws RemoteException, InfoNotFoundException;
	
	/**
	 * Devolve os dados do ficheiro.
	 */
	public byte[] getFile(String path, String name) throws IOException;

}
