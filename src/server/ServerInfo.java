package server;

import java.io.Serializable;

public interface ServerInfo extends Serializable {

	String getAddress();
	
	String getHost();
	
	String getName();
	
	long getLastHeartbeat();
	
	void setLastHeartbeat(long time);
	
}
