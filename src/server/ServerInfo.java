package server;

public class ServerInfo {
	
	private String name, url;
	
	public ServerInfo(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public String getName() {
		return name;
	}
	
	public String getURL() {
		return url;
	}

}
