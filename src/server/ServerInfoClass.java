package server;

<<<<<<< HEAD

=======
>>>>>>> feature/Multicast
public class ServerInfoClass implements ServerInfo {
	
	private static final long serialVersionUID = 1L;
	
	private String address;
	private long lastHeartbeat;
<<<<<<< HEAD
	
	public ServerInfoClass(String address) {
		this.address = address;
		this.lastHeartbeat = System.currentTimeMillis();
	}
	
	@Override
=======
	private boolean isRMI;
	
	public ServerInfoClass(String address, boolean isRMI) {
		this.address = address;
		this.lastHeartbeat = System.currentTimeMillis();
		this.isRMI = isRMI;
	}
	
>>>>>>> feature/Multicast
	public String getAddress() {
		return address;
	}
	
<<<<<<< HEAD
	@Override
	public String getServerHost() {
		return address.substring(2).split("/")[0];
	}
	
	@Override
	public String getServerName() {
		return address.substring(2).split("/")[1];
	}
	
	@Override
=======
	public String getHost() {
		return address.substring(2).split("/")[0];
	}
	
	public String getName() {
		return address.substring(2).split("/")[1];
	}
	
>>>>>>> feature/Multicast
	public long getLastHeartbeat() {
		return lastHeartbeat;
	}
	
<<<<<<< HEAD
	@Override
	public void setLastHeartbeat(long time) {
		this.lastHeartbeat = time;
	}
=======
	public void setLastHeartbeat(long time) {
		this.lastHeartbeat = time;
	}
	
	public boolean isRMI() {
	    	return isRMI;
	}
>>>>>>> feature/Multicast

}
