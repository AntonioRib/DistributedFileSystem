package server;

public class ServerInfoClass implements ServerInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String address;
	private long lastHeartbeat;
	
	public ServerInfoClass(String address) {
		this.address = address;
		this.lastHeartbeat = System.currentTimeMillis();
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getHost() {
		return address.substring(2).split("/")[0];
	}
	
	public String getName() {
		return address.substring(2).split("/")[1];
	}
	
	public long getLastHeartbeat() {
		return lastHeartbeat;
	}
	
	public void setLastHeartbeat(long time) {
		this.lastHeartbeat = time;
	}

}
