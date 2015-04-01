package server.contactServer;

public class NoServersConnectedException 
		extends Exception {
	
	private static final long serialVersionUID = 4063033701940593855L;

	public NoServersConnectedException(String message) {
		super( message);
	}

}
