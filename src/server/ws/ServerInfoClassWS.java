package server.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import server.ServerInfo;

@WebService
public class ServerInfoClassWS implements ServerInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String address;
	private long lastHeartbeat;
	
	public ServerInfoClassWS(String address) {
		this.address = address;
		this.lastHeartbeat = System.currentTimeMillis();
	}
	
	@WebMethod
	public String getAddress() {
		return address;
	}
	
	@WebMethod
	public String getHost() {
		return address.substring(2).split("/")[0];
	}
	
	@WebMethod
	public String getName() {
		return address.substring(2).split("/")[1];
	}
	
	@WebMethod
	public long getLastHeartbeat() {
		return lastHeartbeat;
	}
	
	@WebMethod
	public void setLastHeartbeat(long time) {
		this.lastHeartbeat = time;
	}

}
