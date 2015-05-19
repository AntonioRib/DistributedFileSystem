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
    boolean makeDir(String name, boolean propagate) throws RemoteException, MalformedURLException,
            NotBoundException;

    /**
     * Devolve um booleano indicando se eliminou ou nao o ficheiro/directorio
     * 
     * @param name
     * @return
     * @throws RemoteException
     * @throws NotBoundException 
     * @throws MalformedURLException 
     */
    boolean removeFile(String name, boolean isFile, boolean propagate) throws RemoteException, MalformedURLException, NotBoundException;

    /**
     * 
     * @param fromPath
     * @param toServer
     * @param toIsURL
     * @param toPath
     * @return
     * @throws NotBoundException 
     * @throws FileNotFoundException
     */
    boolean sendFile(String fromPath, String toServer, boolean toIsURL,
            String toPath) throws IOException, NotBoundException;

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
     * @throws NotBoundException 
     */
    boolean receiveFile(String toPath, byte[] data, boolean propagate) throws IOException, NotBoundException;
}
