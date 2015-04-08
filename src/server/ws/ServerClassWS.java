package server.ws;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

public class ServerClassWS implements ServerWS {

    private static final long serialVersionUID = 1L;
    protected String name;

    protected ServerClassWS(String name) {
	super();
	this.name = name;
    }

    public String getName(){
	return name;
    }

    public String getHost() {
	return getLocalhost().toString().substring(1);
    }

    protected InetAddress getLocalhost() {
	try {
	    try {
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
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

}
