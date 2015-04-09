package server;

import java.io.Serializable;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServerInfo extends Serializable {

	@WebMethod
	String getAddress();
	
	@WebMethod
	String getHost();
	
	@WebMethod
	String getName();
	
	@WebMethod
	long getLastHeartbeat();
	
	@WebMethod
	void setLastHeartbeat(long time);
	
}
