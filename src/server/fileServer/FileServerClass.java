package server.fileServer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import server.ServerClass;
import client.FileInfo;

public class FileServerClass
		extends ServerClass
		implements FileServer {
	protected FileServerClass() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	private String basePathName;
	private File basePath;

	protected FileServerClass(String pathname) throws RemoteException {
		super();
		this.basePathName = pathname;
		basePath = new File(pathname);
	}

	@Override
	public String[] dir(String path) throws RemoteException, InfoNotFoundException {
		File f = new File( basePath, path);
		if( f.exists())
			return f.list();
		else
			throw new InfoNotFoundException( "Directory not found :" + path);
	}

	@Override
	public FileInfo getFileInfo(String path, String name) throws RemoteException, InfoNotFoundException {
		File dir = new File( basePath, path);
		if( dir.exists()) {
			File f = new File( dir, name);
			if( f.exists())
				return new FileInfo( path, f.length(), new Date(f.lastModified()), f.isFile());
			else
				throw new InfoNotFoundException( "File not found :" + name);
		} else
			throw new InfoNotFoundException( "Directory not found :" + path);
	}

	
	public byte[] getFile(String path, String name) throws IOException {
		
		RandomAccessFile f = new RandomAccessFile(basePath + "/" + path + "/" + name,"r");
		byte[] b = new byte[(int) f.length()];
		f.readFully(b);
		f.close();
		return b;
	}
	
	@SuppressWarnings("deprecation")
	public static void main( String args[]) throws Exception {
		try {
			String path = ".";
			if( args.length > 0)
				path = args[0];

			System.getProperties().put( "java.security.policy", "src/server/policy.all");

			if( System.getSecurityManager() == null) {
				System.setSecurityManager( new RMISecurityManager());
			}

			try { // start rmiregistry
				LocateRegistry.createRegistry( 1099);
			} catch( RemoteException e) { 
				// if not start it
				// do nothing - already started with rmiregistry
			}

			FileServerClass server = new FileServerClass( path);
			Naming.rebind( "/myFileServer", server);
			System.out.println( "FileServer bound in registry");
		} catch( Throwable th) {
			th.printStackTrace();
		}
	}


}
