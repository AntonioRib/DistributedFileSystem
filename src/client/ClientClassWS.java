package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.namespace.QName;

import server.ServerInfo;
import server.contactServer.ws.services.ContactServerClassWS;
import server.contactServer.ws.services.ContactServerClassWSService;
import server.contactServer.ws.services.ServerInfoClass;
import server.fileServer.ws.FileServerWS;
import server.fileServer.ws.services.FileServerClassWSService;
import fileSystem.FileInfo;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

/**
 * Classe base do cliente
 * 
 * @author nmp
 */
public class ClientClassWS implements Client {
	String contactServerURL;

	protected ClientClassWS(String url) {
		this.contactServerURL = url;

	}

	/**
	 * Returns a FileServer according to a name or an URL
	 * 
	 * @param isURL
	 *            - if server represents an URL
	 * @param server
	 *            - name or URL to the file server
	 * @return a FileServer
	 * @throws RemoteException
	 */
	private FileServerWS getFileServer(boolean isURL, String server) {
		try {
			System.out.println("http:"+contactServerURL+"?wsdl");
			ContactServerClassWSService css = new ContactServerClassWSService(
					new URL("http:"+contactServerURL+"?wsdl"),
					new QName("http://ws.contactServer.server/", "ContactServerClassWSService"));
			ContactServerClassWS cs = css.getContactServerClassWSPort();

			ServerInfoClass fs = isURL ? cs.getFileServerByURL(server) : cs
					.getFileServerByName(server);
			FileServerClassWSService fss = new FileServerClassWSService(
					new URL("http://"+fs.getHost()+":8080/"+fs.getName()+"?wsdl"),
					new QName("http://ws.fileServer.server/", "FileServerClassWSService"));
			return (FileServerWS) fss.getFileServerClassWSPort();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Devolve um array com os servidores a correr caso o name== null ou o URL
	 * dos servidores com nome name.
	 */
	protected String[] servers(String name) throws MalformedURLException {
		System.err.println("exec: servers");
		ContactServerClassWSService css = new ContactServerClassWSService(
					new URL("http:"+contactServerURL+"?wsdl"),
					new QName("http://ws.contactServer.server/", "ContactServerClassWSService"));
			ContactServerClassWS cs = css.getContactServerClassWSPort();
			List<String> list = cs.listFileServers();
			if(list.size() == 0)
				return null;
			if (name == null)
				return cs.listFileServers().toArray(new String[cs.listFileServers().size()]);
			return cs.getFileServersByName(name).toArray(new String[cs.listFileServers().size()]);
	}

	/**
	 * Devolve um array com os ficheiros/directoria na directoria dir no
	 * servidor server (ou no sistema de ficheiros do cliente caso server ==
	 * null). Se isURL for verdadeiro, server representa um URL para o servior
	 * (e.g. //127.0.0.1/myServer). Caso contrario e o nome do servidor. Nesse
	 * caso deve listar os ficheiro dum servidor com esse nome. Devolve null em
	 * caso de erro. NOTA: nao deve lancar excepcao.
	 */
	protected String[] dir(String server, boolean isURL, String dir) {
		System.err.println("exec: ls " + dir + " no servidor " + server
				+ " - e url : " + isURL);
		try {
			if (server == null)
				return FileSystem.dir(dir);
			return getFileServer(isURL, server).dir(dir);
		} catch (InfoNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Cria a directoria dir no servidor server@user (ou no sistema de ficheiros
	 * do cliente caso server == null). Se isURL for verdadeiro, server
	 * representa um URL para o servior (e.g. //127.0.0.1/myServer). Caso
	 * contrario e o nome do servidor. Nesse caso deve listar os ficheiro dum
	 * servidor com esse nome. Devolve false em caso de erro. NOTA: nao deve
	 * lancar excepcao.
	 */
	protected boolean mkdir(String server, boolean isURL, String dir) {
		System.err.println("exec: mkdir " + dir + " no servidor " + server
				+ " - e url : " + isURL);
			if (server == null)
				return FileSystem.makeDir(dir);
			return getFileServer(isURL, server).makeDir(dir);
	}

	/**
	 * Remove a directoria dir no servidor server@user (ou no sistema de
	 * ficheiros do cliente caso server == null). Se isURL for verdadeiro,
	 * server representa um URL para o servior (e.g. //127.0.0.1/myServer). Caso
	 * contrario e o nome do servidor. Nesse caso deve listar os ficheiro dum
	 * servidor com esse nome. Devolve false em caso de erro. NOTA: nao deve
	 * lancar excepcao.
	 */
	protected boolean rmdir(String server, boolean isURL, String dir) {
		System.err.println("exec: rmdir " + dir + " no servidor " + server
				+ " - e url : " + isURL);
			if (server == null)
				return FileSystem.removeFile(dir, false);
			return getFileServer(isURL, server).removeFile(dir, false);
	}

	/**
	 * Remove o ficheiro path no servidor server@user. (ou no sistema de
	 * ficheiros do cliente caso server == null). Se isURL for verdadeiro,
	 * server representa um URL para o servior (e.g. //127.0.0.1/myServer). Caso
	 * contrario e o nome do servidor. Nesse caso deve listar os ficheiro dum
	 * servidor com esse nome. Devolve false em caso de erro. NOTA: nao deve
	 * lancar excepcao.
	 */
	protected boolean rm(String server, boolean isURL, String path) {
		System.err.println("exec: rm " + path + " no servidor " + server
				+ " - e url : " + isURL);
			if (server == null)
				return FileSystem.removeFile(path, true);
			return getFileServer(isURL, server).removeFile(path, true);
	}

	/**
	 * Devolve informacao sobre o ficheiro/directoria path no servidor
	 * server@user. (ou no sistema de ficheiros do cliente caso server == null).
	 * Se isURL for verdadeiro, server representa um URL para o servior (e.g.
	 * //127.0.0.1/myServer). Caso contrario e o nome do servidor. Nesse caso
	 * deve listar os ficheiro dum servidor com esse nome. Devolve false em caso
	 * de erro. NOTA: nao deve lancar excepcao.
	 */
	protected FileInfo getAttr(String server, boolean isURL, String path) {
		System.err.println("exec: getattr " + path + " no servidor " + server
				+ " - e url : " + isURL);
		try {
			if (server == null)
				return FileSystem.getFileInfo(path);
			return getFileServer(isURL, server).getFileInfo(path);
		} catch (InfoNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Copia ficheiro de fromPath no servidor fromServer@fromUser para o
	 * ficheiro toPath no servidor toServer@toUser. (caso fromServer/toServer ==
	 * local, corresponde ao sistema de ficheiros do cliente). Devolve false em
	 * caso de erro. NOTA: nao deve lancar excepcao.
	 */
	protected boolean cp(String fromServer, boolean fromIsURL, String fromPath,
			String toServer, boolean toIsURL, String toPath) {
		System.err.println("exec: cp " + fromPath + " no servidor "
				+ fromServer + " - e url : " + fromIsURL + " para " + toPath
				+ " no servidor " + toServer + " - e url : " + toIsURL);

		try {

			if (fromServer == null && toServer == null) {
				byte[] localData = FileSystem.getData(fromPath);
				return FileSystem.createFile(toPath, localData);
			}

			if (fromServer == null) {
				byte[] localData = FileSystem.getData(fromPath);
				return getFileServer(toIsURL, toServer).receiveFile(toPath,
						localData);
			}

			if (toServer == null) {
				byte[] remoteData = getFileServer(fromIsURL, fromServer)
						.getFile(fromPath);
				return FileSystem.createFile(toPath, remoteData);
			}

			return getFileServer(fromIsURL, fromServer).sendFile(fromPath,
					toServer, toIsURL, toPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void doit() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		for (;;) {
			String line = reader.readLine();
			if (line == null)
				break;
			String[] cmd = line.split(" ");
			if (cmd[0].equalsIgnoreCase("servers")) {
				String[] s = servers(cmd.length == 1 ? null : cmd[1]);

				if (s == null)
					System.out.println("error");
				else {
					System.out.println(s.length);
					for (int i = 0; i < s.length; i++)
						System.out.println(s[i]);
				}
			} else if (cmd[0].equalsIgnoreCase("ls")) {
				String[] dirserver = cmd[1].split("@");
				String server = dirserver.length == 1 ? null : dirserver[0];
				boolean isURL = dirserver.length == 1 ? false : dirserver[0]
						.indexOf('/') >= 0;
						String dir = dirserver.length == 1 ? dirserver[0]
								: dirserver[1];

						String[] res = dir(server, isURL, dir);
						if (res != null) {
							System.out.println(res.length);
							for (int i = 0; i < res.length; i++)
								System.out.println(res[i]);
						} else
							System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("mkdir")) {
				String[] dirserver = cmd[1].split("@");
				String server = dirserver.length == 1 ? null : dirserver[0];
				boolean isURL = dirserver.length == 1 ? false : dirserver[0]
						.indexOf('/') >= 0;
						String dir = dirserver.length == 1 ? dirserver[0]
								: dirserver[1];

						boolean b = mkdir(server, isURL, dir);
						if (b)
							System.out.println("success");
						else
							System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("rmdir")) {
				String[] dirserver = cmd[1].split("@");
				String server = dirserver.length == 1 ? null : dirserver[0];
				boolean isURL = dirserver.length == 1 ? false : dirserver[0]
						.indexOf('/') >= 0;
						String dir = dirserver.length == 1 ? dirserver[0]
								: dirserver[1];

						boolean b = rmdir(server, isURL, dir);
						if (b)
							System.out.println("success");
						else
							System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("rm")) {
				String[] dirserver = cmd[1].split("@");
				String server = dirserver.length == 1 ? null : dirserver[0];
				boolean isURL = dirserver.length == 1 ? false : dirserver[0]
						.indexOf('/') >= 0;
						String path = dirserver.length == 1 ? dirserver[0]
								: dirserver[1];

						boolean b = rm(server, isURL, path);
						if (b)
							System.out.println("success");
						else
							System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("getattr")) {
				String[] dirserver = cmd[1].split("@");
				String server = dirserver.length == 1 ? null : dirserver[0];
				boolean isURL = dirserver.length == 1 ? false : dirserver[0]
						.indexOf('/') >= 0;
						String path = dirserver.length == 1 ? dirserver[0]
								: dirserver[1];

						FileInfo info = getAttr(server, isURL, path);
						if (info != null) {
							System.out.println(info);
							System.out.println("success");
						} else
							System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("cp")) {
				String[] dirserver1 = cmd[1].split("@");
				String server1 = dirserver1.length == 1 ? null : dirserver1[0];
				boolean isURL1 = dirserver1.length == 1 ? false : dirserver1[0]
						.indexOf('/') >= 0;
						String path1 = dirserver1.length == 1 ? dirserver1[0]
								: dirserver1[1];

						String[] dirserver2 = cmd[2].split("@");
						String server2 = dirserver2.length == 1 ? null : dirserver2[0];
						boolean isURL2 = dirserver2.length == 1 ? false : dirserver2[0]
								.indexOf('/') >= 0;
								String path2 = dirserver2.length == 1 ? dirserver2[0]
										: dirserver2[1];

								boolean b = cp(server1, isURL1, path1, server2, isURL2, path2);
								if (b)
									System.out.println("success");
								else
									System.out.println("error");
			} else if (cmd[0].equalsIgnoreCase("help")) {
				System.out
				.println("servers - lista nomes de servidores a executar");
				System.out
				.println("servers nome - lista URL dos servidores com nome nome");
				System.out
				.println("ls server@dir - lista ficheiros/directorias presentes na directoria dir (. e .. tem o significado habitual), caso existam ficheiros com o mesmo nome devem ser apresentados como nome@server;");
				System.out
				.println("mkdir server@dir - cria a directoria dir no servidor server");
				System.out
				.println("rmdir server@udir - remove a directoria dir no servidor server");
				System.out
				.println("cp path1 path2 - copia o ficheiro path1 para path2; quando path representa um ficheiro num servidor deve ter a forma server:path, quando representa um ficheiro local deve ter a forma path");
				System.out.println("rm path - remove o ficheiro path");
				System.out
				.println("getattr path - apresenta informacao sobre o ficheiro/directoria path, incluindo: nome, boolean indicando se e ficheiro, data da criacao, data da ultima modificacao");
			} else if (cmd[0].equalsIgnoreCase("exit"))
				break;

		}
	}

	public static void main(String[] args) {
		try {
		    
		    InetAddress group = InetAddress.getByName("239.255.255.255");
		    MulticastSocket sock = new MulticastSocket(5000);
		    sock.joinGroup(group);
		    
		    byte buf[] = new byte[128];
		    DatagramPacket contactServerResponse = new DatagramPacket(buf, buf.length);
		    sock.receive(contactServerResponse);
		    
		    System.out.println("Got response from contact server!");
		    
		    new ClientClassWS(new String(contactServerResponse.getData()).trim()).doit();
		    
		    
		} catch (IOException e) {
		    System.err.println("Error:" + e.getMessage());
		    e.printStackTrace();
		}
	}

}
