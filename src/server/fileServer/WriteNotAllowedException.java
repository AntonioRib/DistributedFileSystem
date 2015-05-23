package server.fileServer;

public class WriteNotAllowedException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public WriteNotAllowedException(String message) {
	super(message);
    }
    
    public WriteNotAllowedException() {
	super();
    }

}
