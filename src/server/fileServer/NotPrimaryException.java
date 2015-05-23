package server.fileServer;

public class NotPrimaryException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotPrimaryException(String message) {
	super(message);
    }
    
    public NotPrimaryException() {
	super();
    }

}
