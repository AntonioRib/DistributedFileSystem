package server.proxyServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import server.contactServer.ContactServer;
import server.fileServer.FileServer;
import fileSystem.InfoNotFoundException;

public class GoogleDriveProxyServer extends UnicastRemoteObject implements
        FileServer {

    private static final long serialVersionUID = 1L;

    private static final String API_KEY = "1050234790101-e5mbrchk17pohuditaigvkl184mirune.apps.googleusercontent.com";
    private static final String API_SECRET = "c1XXYgMfXZltBFeV70e8_VK1";

    private static final String JSON_FILE_CONTENT = "items";
    private static final String JSON_FILE_ID = "id";

    private static final String URL_START = "https://www.googleapis.com/drive/v2/";
    private static final String URL_FILES = "files/";
    private static final String URL_CHILDREN = "children";
    private static final String URL_QUERY = "?q=";
    private static final String URL_TRASHED_FALSE = "trashed%3Dfalse";

    private static final String URL_CALLBACK = "urn:ietf:wg:oauth:2.0:oob";
    // private static final String URL_AUTHORIZE_ =
    // "https://accounts.google.com/o/oauth2/auth";

    private static final String SCOPE = "https://www.googleapis.com/auth/drive.readonly";

    private String name, contactServerURL;
    private static OAuthService service;
    private static Token requestToken, accessToken;

    public GoogleDriveProxyServer(String name, final String contactServerURL)
            throws RemoteException, MalformedURLException, NotBoundException,
            UnknownHostException {
        super();
        Naming.rebind('/' + name, this);
        this.contactServerURL = contactServerURL;
        this.name = name;

        service = new ServiceBuilder().provider(Google2Api.class)
                .apiKey(API_KEY).apiSecret(API_SECRET).callback(URL_CALLBACK)
                .scope(SCOPE).build();

        ((ContactServer) Naming.lookup(contactServerURL)).addFileServer(
                this.getHost(), this.getName(), true);

        new Thread() {
            public void run() {
                heartbeat();
            }
        }.start();
    }

    private void heartbeat() {
        try {
            for (;;) {
                ((ContactServer) Naming.lookup(contactServerURL))
                        .receiveAliveSignal(getHost(), getName());
                Thread.sleep(1000);
            }
        } catch (RemoteException | MalformedURLException | NotBoundException
                | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    // TODO really, pôr isto como deve de ser. Apesar de funcionar tá feio feio
    // feio.
    public List<String> dir(String path) throws RemoteException,
            InfoNotFoundException {
        String[] pathDecomposed = path.split("/");
        OAuthRequest request;
        String fileID = "root";

        if (!path.equals(".") && !path.equals("/")) {
            request = new OAuthRequest(Verb.GET, URL_START + URL_FILES
                    + URL_QUERY + URL_TRASHED_FALSE + "+and+" + "title%3D'"
                    + pathDecomposed[pathDecomposed.length - 1] + "'");

            System.out.println(request.getUrl());

            service.signRequest(accessToken, request);
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

            JSONArray items = (JSONArray) res.get(JSON_FILE_CONTENT);
            List<String> result = new LinkedList<String>();
            JSONObject file = (JSONObject) items.iterator().next();
            fileID = ((String) file.get(JSON_FILE_ID));
        }

        request = new OAuthRequest(Verb.GET, URL_START + URL_FILES + fileID
                + "/" + URL_CHILDREN + URL_QUERY + URL_TRASHED_FALSE);

        service.signRequest(accessToken, request);
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

        JSONArray items = (JSONArray) res.get(JSON_FILE_CONTENT);
        List<String> result = new LinkedList<String>();
        Iterator<?> it = items.iterator();
        while (it.hasNext()) {
            JSONObject file = (JSONObject) it.next();

            request = new OAuthRequest(Verb.GET, URL_START + URL_FILES
                    + file.get("id"));

            service.signRequest(accessToken, request);
            response = request.send();

            if (response.getCode() != 200)
                throw new RuntimeException("Metadata response code:"
                        + response.getCode());

            JSONParser childParser = new JSONParser();
            JSONObject childRes = null;
            try {
                childRes = (JSONObject) childParser.parse(response.getBody());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            result.add((String) childRes.get("title"));
        }

        return result;
    }

    @Override
    public List<String> getFileInfo(String path) throws RemoteException,
            InfoNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean makeDir(String name) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeFile(String name, boolean isFile)
            throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean sendFile(String fromPath, String toServer, boolean toIsURL,
            String toPath) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public byte[] getFile(String toPath) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean receiveFile(String toPath, byte[] data) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return getLocalhost().toString().substring(1);
    }

    private InetAddress getLocalhost() {
        try {
            try {
                Enumeration<NetworkInterface> e = NetworkInterface
                        .getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface n = e.nextElement();
                    Enumeration<InetAddress> ee = n.getInetAddresses();
                    while (ee.hasMoreElements()) {
                        InetAddress i = ee.nextElement();
                        if (i instanceof Inet4Address && !i.isLoopbackAddress())
                            return i;
                    }
                }
            } catch (SocketException e) {
                // do nothing
            }
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        try {

            if (args.length < 1) {
                System.out
                        .println("Use: java server.proxyServer.GoogleDriveProxyServer serverName or java server.proxyServer.GoogleDriveProxyServer serverName contactServerAdress");
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

                ds = new GoogleDriveProxyServer(name, new String(
                        contactServerBroadcast.getData()).trim());
            } else {
                ds = new GoogleDriveProxyServer(name, args[1]);
            }

            System.out.println("FileServer bound in registry");
            System.out.println("//" + ds.getHost() + '/' + ds.getName());

            Scanner in = new Scanner(System.in);

            String authorizationUrl = service.getAuthorizationUrl(requestToken);
            System.out.println("Got the Authorization URL!");
            System.out.println("Now go and authorize Scribe here:");
            System.out.println(authorizationUrl);
            System.out.println("And paste the authorization code here");
            System.out.print(">>");
            Verifier verifier = new Verifier(in.nextLine());
            System.out.println();

            accessToken = service.getAccessToken(requestToken, verifier);

            /*
             * // Now let's go and ask for a protected resource!
             * System.out.println
             * ("Now we're going to access a protected resource...");
             * OAuthRequest request = new OAuthRequest(Verb.GET,
             * PROTECTED_RESOURCE_URL); service.signRequest(accessToken,
             * request); Response response = request.send();
             * System.out.println("Got it! Lets see what we found...");
             * System.out.println(); System.out.println(response.getCode());
             * System.out.println(response.getBody());
             * 
             * System.out.println(); System.out.println(
             * "Thats it man! Go and build something awesome with Scribe! :)");
             */

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
