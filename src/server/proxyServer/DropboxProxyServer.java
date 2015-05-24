package server.proxyServer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import server.ServerInfo;
import server.ServerUtils;
import server.contactServer.ContactServer;
import server.fileServer.FileServer;
import server.fileServer.WriteNotAllowedException;
import fileSystem.FileSystem;
import fileSystem.InfoNotFoundException;

public class DropboxProxyServer extends UnicastRemoteObject implements
	FileServer {

    private static final long serialVersionUID = 1L;

    private static final String ACCESS_TOKEN = "FYwRsrxo0ZoAAAAAAAAAlPbqRzn6Xy0OCr11Y2axLjeUzZljxafP3dNBHiZPc3zm";

    private String name, contactServerURL;
    private boolean isPrimary;

    protected DropboxProxyServer(String name, final String contactServerURL)
	    throws NotBoundException, InfoNotFoundException, IOException,
	    WriteNotAllowedException {
	super();
	Naming.rebind('/' + name, this);
	this.contactServerURL = contactServerURL;
	this.name = name;
	this.isPrimary = false;

	this.sync();
	this.genMetadata();

	((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
		this.getHost(), this.getName(), true);

	new Thread() {
	    public void run() {
		heartbeat();
	    }
	}.start();
    }

    private void sync() throws NotBoundException, IOException,
	    WriteNotAllowedException, InfoNotFoundException {

	ServerInfo si = ((ContactServer) Naming.lookup(contactServerURL))
		.getPrimaryServer(name);

	if (si != null && !si.getHost().equals(this.getHost())) {

	    FileServer curr = ((FileServer) Naming.lookup(si.getAddress()));
	    if (curr.isPrimary()) {
		this.receiveFile(".sync", curr.getFile(".sync"), false);

		JSONParser parser = new JSONParser();

		try {
		    Object obj = parser.parse(new FileReader(".sync"));
		    JSONObject meta = (JSONObject) obj;

		    JSONArray files = (JSONArray) meta.get("files");

		    for (int i = 0; i < files.size(); i++) {

			// GETS ROOT CONTENTS
			JSONObject file = (JSONObject) files.get(i);
			String fileName = (String) file.get("name");
			this.receiveFile(fileName, curr.getFile(fileName),
				false);

			// CHECK IF RECEIVED FILE IS A DIRECTORY
			File received = new File(fileName);
			if (received.isDirectory())
			    // IT IS, GET ITS CONTENTS AND SYNC THEM TOO
			    this.syncDir(fileName, curr);
		    }

		} catch (ParseException e) {
		    e.printStackTrace();
		}

	    }
	}
    }

    private void syncDir(String path, FileServer primary)
	    throws InfoNotFoundException, IOException, NotBoundException,
	    WriteNotAllowedException {

	List<String> contents = primary.dir(path);
	for (String name : contents) {

	    String newPath = path + '/' + name;
	    this.receiveFile(newPath, primary.getFile(newPath), false);

	    if (new File(newPath).isDirectory())
		this.syncDir(newPath, primary);
	}

    }

    @SuppressWarnings("unchecked")
    private void genMetadata() throws InfoNotFoundException, IOException {

	JSONObject meta = new JSONObject();
	meta.put("name", name);
	meta.put("host", this.getHost());
	meta.put("is_primary", isPrimary);

	JSONArray files = new JSONArray();
	List<String> contents = this.dir(".");

	for (String fileName : contents) {
	    File f = new File(fileName);
	    JSONObject file = new JSONObject();
	    file.put("date_modified", new Date(f.lastModified()).toString());
	    file.put("is_dir", f.isDirectory());
	    file.put("name", fileName);
	    files.add(file);
	}

	meta.put("files", files);

	FileWriter toFile = new FileWriter(".sync");
	try {
	    toFile.write(meta.toJSONString());
	    System.out.println("Successfully generated metadata...");
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    toFile.flush();
	    toFile.close();
	}
    }

    private void heartbeat() {
	try {
	    for (;;) {
		((ContactServer) Naming.lookup(contactServerURL))
			.receiveAliveSignal(getHost(), getName());
		System.out.println("Sent alive signal");
		Thread.sleep(1000);
	    }
	} catch (RemoteException | MalformedURLException | NotBoundException
		| InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @SuppressWarnings("rawtypes")
    public List<String> dir(String path) throws RemoteException {

	String dropboxPath = "";

	if (!path.equals(".") && !path.equals("/"))
	    dropboxPath = path;

	OAuthRequest request = new OAuthRequest(Verb.GET,
		"https://api.dropbox.com/1/metadata/dropbox/" + dropboxPath
			+ "?list=true");

	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	JSONParser parser = new JSONParser();
	JSONObject res = null;
	try {
	    res = (JSONObject) parser.parse(response.getBody());
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	JSONArray items = (JSONArray) res.get("contents");
	List<String> result = new LinkedList<String>();
	Iterator it = items.iterator();
	while (it.hasNext()) {
	    JSONObject file = (JSONObject) it.next();
	    result.add((String) file.get("path"));
	}

	return result;
    }

    public List<String> getFileInfo(String path) throws RemoteException {

	OAuthRequest request = new OAuthRequest(Verb.GET,
		"https://api.dropbox.com/1/metadata/dropbox/" + path);

	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	JSONParser parser = new JSONParser();
	JSONObject res = null;
	try {
	    res = (JSONObject) parser.parse(response.getBody());
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	List<String> result = new LinkedList<String>();
	result.add("Path: " + path);
	result.add("Size: " + res.get("size"));
	result.add("Last Modified: " + res.get("modified"));
	result.add("Is file:" + !((boolean) res.get("is_dir")));

	return result;
    }

    public boolean makeDir(String name, boolean propagate)
	    throws RemoteException, MalformedURLException, NotBoundException,
	    WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	OAuthRequest request = new OAuthRequest(Verb.POST,
		"https://api.dropbox.com/1/fileops/create_folder?root=dropbox&path="
			+ name);
	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    ServerUtils.getFileServer(contactServerURL, true,
			    sf.getAddress()).makeDir(name, false);
		}
	    }
	}

	return true;
    }

    public boolean removeFile(String path, boolean isFile, boolean propagate)
	    throws RemoteException, MalformedURLException, NotBoundException,
	    WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	OAuthRequest request = new OAuthRequest(Verb.GET,
		"https://api.dropbox.com/1/metadata/dropbox/" + path);
	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	JSONParser parser = new JSONParser();
	JSONObject res = null;
	try {
	    res = (JSONObject) parser.parse(response.getBody());
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	if (((boolean) res.get("is_dir") && isFile)
		|| !(boolean) res.get("is_dir") && !isFile)
	    return false;

	request = new OAuthRequest(Verb.POST,
		"https://api.dropbox.com/1/fileops/delete?root=dropbox&path="
			+ path);
	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    ServerUtils.getFileServer(contactServerURL, true,
			    sf.getAddress()).removeFile(path, isFile, false);
		}
	    }
	}

	return true;
    }

    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
	    String toPath) throws IOException, NotBoundException,
	    WriteNotAllowedException {

	byte[] in = FileSystem.getData(fromPath);

	if (toIsURL)
	    return ServerUtils.getFileServer(contactServerURL, toIsURL,
		    toServer).receiveFile(toPath, in, false);

	return ServerUtils.getPrimaryFileServer(contactServerURL, toServer)
		.receiveFile(toPath, in, false);
    }

    public byte[] getFile(String fromPath) throws IOException {
	OAuthRequest request = new OAuthRequest(Verb.GET,
		"https://api-content.dropbox.com/1/files/dropbox/" + fromPath);
	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	int length = Integer.parseInt(response.getHeader("Content-Length"));
	byte[] buf = new byte[length];
	new DataInputStream(response.getStream()).readFully(buf);

	return buf;
    }

    public boolean receiveFile(String toPath, byte[] data, boolean propagate)
	    throws IOException, NotBoundException, WriteNotAllowedException {

	if (!isPrimary && propagate) {
	    throw new WriteNotAllowedException();
	}

	OAuthRequest request = new OAuthRequest(Verb.PUT,
		"https://api-content.dropbox.com/1/files_put/dropbox/" + toPath);
	request.addHeader("Content-Type", "application/octet-stream");
	request.addPayload(data);
	request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN,
		ACCESS_TOKEN);
	Response response = request.send();

	if (response.getCode() != 200)
	    throw new RuntimeException("Metadata response code:"
		    + response.getCode());

	if (propagate) {
	    List<ServerInfo> serversList = ((ContactServer) Naming
		    .lookup(contactServerURL)).getAllFileServersByName(this
		    .getName());
	    for (ServerInfo sf : serversList) {
		if (!sf.getHost().equals(this.getHost())) {
		    ServerUtils.getFileServer(contactServerURL, true,
			    sf.getAddress()).receiveFile(toPath, data, false);
		}
	    }
	}

	return true;
    }

    public boolean isPrimary() {
	return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
	this.isPrimary = isPrimary;
    }

    public String getName() {
	return name;
    }

    public String getHost() {
	return ServerUtils.getLocalhost().toString().substring(1);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
	try {

	    if (args.length < 1) {
		System.out
			.println("Use: java server.proxyServer.DropboxProxyServer serverName or java server.proxyServer.DropboxProxyServer serverName contactServerAdress");
		return;
	    }

	    String name = args[0];

	    System.getProperties().put("java.security.policy", "policy.all");

	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new java.rmi.RMISecurityManager());
	    }

	    try { // start rmiregistry
		LocateRegistry.createRegistry(1099);
	    } catch (RemoteException e) {
		// if not start it
		// do nothing - already started with rmiregistry
	    }

	    FileServer ds;
	    if (args.length == 1) {
		InetAddress group = InetAddress.getByName("239.255.255.255");
		MulticastSocket sock = new MulticastSocket(5000);
		sock.joinGroup(group);

		byte buf[] = new byte[128];
		DatagramPacket contactServerBroadcast = new DatagramPacket(buf,
			buf.length);
		sock.receive(contactServerBroadcast);
		sock.close();

		System.out.println("Got broadcast from contact server!");

		ds = new DropboxProxyServer(name, new String(
			contactServerBroadcast.getData()).trim());
	    } else {
		ds = new DropboxProxyServer(name, args[1]);
	    }

	    System.out.println("FileServer bound in registry");
	    System.out.println("//" + ds.getHost() + '/' + ds.getName());

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
