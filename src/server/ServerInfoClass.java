package server;


public class ServerInfoClass implements ServerInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String address;
	private long lastHeartbeat;
	
	public ServerInfoClass(String address) {
		this.address = address;
		this.lastHeartbeat = System.currentTimeMillis();
	}
	
	@Override
	public String getAddress() {
		return address;
	}
	
	@Override
	public String getServerHost() {
		return address.substring(2).split("/")[0];
	}
	
	@Override
	public String getServerName() {
		return address.substring(2).split("/")[1];
	}
	
	@Override
	public long getLastHeartbeat() {
		return lastHeartbeat;
	}
	
	@Override
	public void setLastHeartbeat(long time) {
		this.lastHeartbeat = time;
	}

}
