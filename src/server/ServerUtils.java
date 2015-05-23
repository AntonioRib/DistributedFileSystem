package server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Enumeration;

import server.contactServer.ContactServer;
import server.fileServer.FileServer;

public class ServerUtils {

    public static InetAddress getLocalhost() {
	try {
	    try {
		Enumeration<NetworkInterface> e = NetworkInterface
			.getNetworkInterfaces();
		while (e.hasMoreElements()) {
		    NetworkInterface n = e.nextElement();
		    Enumeration<InetAddress> ee = n.getInetAddresses();
		    while (ee.hasMoreElements()) {
			InetAddress i = ee.nextElement();
			if (i instanceof Inet4Address && !i.isLoopbackAddress())
			    return i;
		    }
		}
	    } catch (SocketException e) {
		// do nothing
	    }
	    return InetAddress.getLocalHost();
	} catch (UnknownHostException e) {
	    return null;
	}
    }

    public static FileServer getFileServer(String contactServerURL,
	    boolean isURL, String server) throws RemoteException {
	try {
	    ContactServer cs = ((ContactServer) Naming.lookup(contactServerURL));
	    ServerInfo fs = isURL ? cs.getFileServerByURL(server) : cs
		    .getFileServerByName(server);
	    if (fs.isRMI())
		return (FileServer) Naming.lookup(fs.getAddress());
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    e.printStackTrace();
	}

	return null;
    }

    public static FileServer getPrimaryFileServer(String contactServerURL,
	    String serverName) throws RemoteException {
	try {
	    ContactServer cs = ((ContactServer) Naming.lookup(contactServerURL));
	    ServerInfo fs = cs.getPrimaryServer(serverName);
	    if (fs.isRMI())
		return (FileServer) Naming.lookup(fs.getAddress());
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    e.printStackTrace();
	}
	
	return null;
    }

}
