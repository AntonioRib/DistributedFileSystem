package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.rmi.* ;

import server.dataServer.DataServer;
import client.FileInfo;

public class GetFileInfo {
    
    public static void main(String[] args) {
        if( args.length != 3) {
        	System.out.println( "Use: java GetFileInfo server_host path name");
        	System.exit(0);
        }
        String serverHost = args[0];
        String path = args[1];
        String name = args[2];
    	
		try {
			DataServer server = (DataServer) Naming.lookup("//" + serverHost + "/myFileServer");

			FileInfo info = server.getFileInfo(path, name);
			byte[] buffer = server.getFile(path, name);
			
			File f = new File("ines");
			f.setWritable(true);
			FileOutputStream fis = new FileOutputStream(f);
			fis.write(buffer);
			fis.close();
	        System.out.println(info);
	        
		} catch( Exception e) {
			e.printStackTrace();
			System.err.println( "Erro: " + e.getMessage());
		}
    }
}
