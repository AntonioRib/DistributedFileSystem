package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerClass  
		extends UnicastRemoteObject
		implements Server{

	private static final long serialVersionUID = 1L;

	protected ServerClass() throws RemoteException {
		super();
	}
//	Classe que irá conter todos os métodos comuns a qualquer server que possa ser implementado.
}
