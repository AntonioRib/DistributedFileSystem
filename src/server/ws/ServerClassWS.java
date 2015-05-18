package server.ws;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class ServerClassWS implements ServerWS {

	private static final long serialVersionUID = 1L;
	protected String name;

	protected ServerClassWS(String name) {
		super();
		this.name = name;
	}

	@Override
	@WebMethod
	public String getName(){
		return name;
	}

	@Override
	@WebMethod
	public String getHost() {
		return getLocalhost().toString().substring(1);
	}

	@WebMethod
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
