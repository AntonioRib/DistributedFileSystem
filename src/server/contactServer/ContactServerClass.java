package server.contactServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import server.ServerInfo;
import server.ServerInfoClass;
import server.fileServer.FileServer;

public class ContactServerClass extends UnicastRemoteObject implements
        ContactServer {

    private static final long serialVersionUID = 1L;

    private ConcurrentMap<String, ConcurrentMap<String, ServerInfo>> servers;
    private String name;

    public ContactServerClass() throws RemoteException, MalformedURLException {
        this.name = "contactServer";
        Naming.rebind('/' + name, this);
        servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfo>>();
    }

    public ContactServerClass(String name) throws RemoteException,
            MalformedURLException {
        this.name = name;
        Naming.rebind('/' + name, this);
        servers = new ConcurrentHashMap<String, ConcurrentMap<String, ServerInfo>>();
    }

    public String[] listFileServers() throws RemoteException, MalformedURLException, NotBoundException {
        updateAndGetServers();
        if (servers.isEmpty())
            return null;
        return servers.keySet().toArray(new String[servers.size()]);
    }

    public String[] getFileServersByName(String name) throws RemoteException,
            UnknownHostException, MalformedURLException, NotBoundException {

        ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);

        if (tmp == null)
            return null;

        return tmp.keySet().toArray(new String[tmp.size()]);
    }

    public void addFileServer(String host, String name, boolean isRMI)
            throws RemoteException, UnknownHostException, MalformedURLException, NotBoundException {
        ServerInfo fs = new ServerInfoClass("//" + host + "/" + name, isRMI);
        ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);
        if (tmp == null)
            servers.put(name, new ConcurrentHashMap<String, ServerInfo>());

        if(servers.get(name).isEmpty()){
            promoteServer(host, name);
        }
        
        servers.get(name).put(host, fs);
        System.out.println("Added file server.");        
    }


    public List<ServerInfo> getAllFileServersByName(String name) throws RemoteException, MalformedURLException, NotBoundException {
        ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);

        if (tmp == null)
            return null;

        ServerInfo[] fss = tmp.values().toArray(new ServerInfo[tmp.size()]);
        List<ServerInfo> ret =  new ArrayList<ServerInfo>();
        ret.addAll(Arrays.asList(fss));
        return ret;
    }

    public ServerInfo getFileServerByURL(String URL) throws RemoteException,
            UnknownHostException {
        String[] address = URL.substring(2).split("/");
        return servers.get(address[1]).get(address[0]);
    }

    public ServerInfo getFileServerByName(String name) throws RemoteException, MalformedURLException, NotBoundException {
        ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);

        if (tmp == null)
            return null;

        ServerInfo[] fss = tmp.values().toArray(new ServerInfo[tmp.size()]);
        return fss[new Random().nextInt(fss.length)];
    }

    public void receiveAliveSignal(String host, String name)
            throws RemoteException, MalformedURLException, NotBoundException {
        ConcurrentMap<String, ServerInfo> tmp = updateAndGetServers(name);

        if (tmp != null)
            tmp.get(host).setLastHeartbeat(System.currentTimeMillis());
        // System.out.println("Recieving Heartbeat from: "+host+"/"+name);
    }

    private ConcurrentMap<String, ServerInfo> updateAndGetServers(String name) throws MalformedURLException, RemoteException, NotBoundException {
        ConcurrentMap<String, ServerInfo> serversName = servers.get(name);
        if (servers.isEmpty())
            return null;

        if (serversName == null) {
            return null;
        }
        Iterator<ServerInfo> serverInfoIt = serversName.values().iterator();
        int i = 0;
        while(serverInfoIt.hasNext()){
            ServerInfo server = serverInfoIt.next();
            if (System.currentTimeMillis() - server.getLastHeartbeat() > 5000) {
                serversName.remove(server.getHost());
                System.out.println("Server " + server.getAddress()
                        + " did not send alive signal, server removed.");
                if(i == 0 && !serversName.isEmpty()){
                    ServerInfo nextServer = serverInfoIt.next();
                    promoteServer(nextServer.getHost(), nextServer.getName());
                    i++;
                }
            }
            i++;
        }
        

        servers.put(name, serversName);

        if (serversName.size() == 0)
            servers.remove(name);

        return servers.get(name);
    }

    private void updateAndGetServers() throws MalformedURLException, RemoteException, NotBoundException {
        for (String serverName : servers.keySet()) {
            updateAndGetServers(serverName);
        }
    }
    
    private void promoteServer(String host, String name) throws MalformedURLException, RemoteException, NotBoundException{
       FileServer fs = ((FileServer) Naming.lookup("//" + host + '/' + name));
       fs.setPrimary(true);
       System.out.println("//" + host + '/' + name + " - " + "Promovido a primário");
    }

    public String getName() throws RemoteException {
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
    public static void main(String args[]) throws Exception {
        try {

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

            final ContactServer cs = new ContactServerClass("contactServer");

            new Thread() {
                public void run() {
                    try {

                        InetAddress group = InetAddress
                                .getByName("239.255.255.255");

                        // sock is never closed
                        @SuppressWarnings("resource")
                        MulticastSocket sock = new MulticastSocket(5000);
                        String broadcast = "//" + cs.getHost() + '/'
                                + "contactServer";

                        for (;;) {
                            sock.send(new DatagramPacket(broadcast.getBytes(),
                                    broadcast.length(), group, 5000));
                            System.out.println("Broadcasting!");
                            Thread.sleep(2500);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            System.out.println("ContactServer bound in registry");
            System.out.println("//" + cs.getHost() + '/' + cs.getName());

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
