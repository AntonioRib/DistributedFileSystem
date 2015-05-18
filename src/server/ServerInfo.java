package server;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebService;


public interface ServerInfo extends Serializable {

	String getAddress();
	
	
	String getServerHost();
	
	
	String getServerName();
	
	
	long getLastHeartbeat();
	
	
	void setLastHeartbeat(long time);
	
}
