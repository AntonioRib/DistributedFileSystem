package server;

import java.io.Serializable;

<<<<<<< HEAD
import javax.jws.WebMethod;
import javax.jws.WebService;


=======
>>>>>>> feature/Multicast
public interface ServerInfo extends Serializable {

	String getAddress();
	
<<<<<<< HEAD
	
	String getServerHost();
	
	
	String getServerName();
	
	
	long getLastHeartbeat();
	
	
	void setLastHeartbeat(long time);
	
=======
	String getHost();
	
	String getName();
	
	long getLastHeartbeat();
	
	void setLastHeartbeat(long time);
	
	boolean isRMI();
>>>>>>> feature/Multicast
}
