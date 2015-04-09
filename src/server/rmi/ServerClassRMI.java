package server.rmi;

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

public class ServerClassRMI extends UnicastRemoteObject implements ServerRMI, Remote {

	private static final long serialVersionUID = 1L;
	protected String name;

	protected ServerClassRMI(String name) throws RemoteException,
	MalformedURLException {
		super();
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
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
