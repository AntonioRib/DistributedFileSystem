
package server.contactServer.ws.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the server.contactServer.ws.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetFileServerByName_QNAME = new QName("http://ws.contactServer.server/", "getFileServerByName");
    private final static QName _GetName_QNAME = new QName("http://ws.contactServer.server/", "getName");
    private final static QName _AddFileServer_QNAME = new QName("http://ws.contactServer.server/", "addFileServer");
    private final static QName _ListFileServersResponse_QNAME = new QName("http://ws.contactServer.server/", "listFileServersResponse");
    private final static QName _GetHostResponse_QNAME = new QName("http://ws.contactServer.server/", "getHostResponse");
    private final static QName _GetNameResponse_QNAME = new QName("http://ws.contactServer.server/", "getNameResponse");
    private final static QName _GetFileServersByName_QNAME = new QName("http://ws.contactServer.server/", "getFileServersByName");
    private final static QName _GetFileServersByNameResponse_QNAME = new QName("http://ws.contactServer.server/", "getFileServersByNameResponse");
    private final static QName _ListFileServers_QNAME = new QName("http://ws.contactServer.server/", "listFileServers");
    private final static QName _AddFileServerResponse_QNAME = new QName("http://ws.contactServer.server/", "addFileServerResponse");
    private final static QName _GetFileServerByNameResponse_QNAME = new QName("http://ws.contactServer.server/", "getFileServerByNameResponse");
    private final static QName _GetFileServerByURLResponse_QNAME = new QName("http://ws.contactServer.server/", "getFileServerByURLResponse");
    private final static QName _ReceiveAliveSignal_QNAME = new QName("http://ws.contactServer.server/", "receiveAliveSignal");
    private final static QName _ReceiveAliveSignalResponse_QNAME = new QName("http://ws.contactServer.server/", "receiveAliveSignalResponse");
    private final static QName _GetHost_QNAME = new QName("http://ws.contactServer.server/", "getHost");
    private final static QName _GetFileServerByURL_QNAME = new QName("http://ws.contactServer.server/", "getFileServerByURL");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: server.contactServer.ws.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddFileServer }
     * 
     */
    public AddFileServer createAddFileServer() {
        return new AddFileServer();
    }

    /**
     * Create an instance of {@link GetFileServerByName }
     * 
     */
    public GetFileServerByName createGetFileServerByName() {
        return new GetFileServerByName();
    }

    /**
     * Create an instance of {@link GetName }
     * 
     */
    public GetName createGetName() {
        return new GetName();
    }

    /**
     * Create an instance of {@link ListFileServersResponse }
     * 
     */
    public ListFileServersResponse createListFileServersResponse() {
        return new ListFileServersResponse();
    }

    /**
     * Create an instance of {@link GetHostResponse }
     * 
     */
    public GetHostResponse createGetHostResponse() {
        return new GetHostResponse();
    }

    /**
     * Create an instance of {@link GetNameResponse }
     * 
     */
    public GetNameResponse createGetNameResponse() {
        return new GetNameResponse();
    }

    /**
     * Create an instance of {@link AddFileServerResponse }
     * 
     */
    public AddFileServerResponse createAddFileServerResponse() {
        return new AddFileServerResponse();
    }

    /**
     * Create an instance of {@link GetFileServersByName }
     * 
     */
    public GetFileServersByName createGetFileServersByName() {
        return new GetFileServersByName();
    }

    /**
     * Create an instance of {@link GetFileServersByNameResponse }
     * 
     */
    public GetFileServersByNameResponse createGetFileServersByNameResponse() {
        return new GetFileServersByNameResponse();
    }

    /**
     * Create an instance of {@link ListFileServers }
     * 
     */
    public ListFileServers createListFileServers() {
        return new ListFileServers();
    }

    /**
     * Create an instance of {@link GetFileServerByNameResponse }
     * 
     */
    public GetFileServerByNameResponse createGetFileServerByNameResponse() {
        return new GetFileServerByNameResponse();
    }

    /**
     * Create an instance of {@link GetFileServerByURLResponse }
     * 
     */
    public GetFileServerByURLResponse createGetFileServerByURLResponse() {
        return new GetFileServerByURLResponse();
    }

    /**
     * Create an instance of {@link ReceiveAliveSignal }
     * 
     */
    public ReceiveAliveSignal createReceiveAliveSignal() {
        return new ReceiveAliveSignal();
    }

    /**
     * Create an instance of {@link ReceiveAliveSignalResponse }
     * 
     */
    public ReceiveAliveSignalResponse createReceiveAliveSignalResponse() {
        return new ReceiveAliveSignalResponse();
    }

    /**
     * Create an instance of {@link GetFileServerByURL }
     * 
     */
    public GetFileServerByURL createGetFileServerByURL() {
        return new GetFileServerByURL();
    }

    /**
     * Create an instance of {@link GetHost }
     * 
     */
    public GetHost createGetHost() {
        return new GetHost();
    }

    /**
     * Create an instance of {@link ServerInfoClass }
     * 
     */
    public ServerInfoClass createServerInfoClass() {
        return new ServerInfoClass();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServerByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServerByName")
    public JAXBElement<GetFileServerByName> createGetFileServerByName(GetFileServerByName value) {
        return new JAXBElement<GetFileServerByName>(_GetFileServerByName_QNAME, GetFileServerByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getName")
    public JAXBElement<GetName> createGetName(GetName value) {
        return new JAXBElement<GetName>(_GetName_QNAME, GetName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFileServer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "addFileServer")
    public JAXBElement<AddFileServer> createAddFileServer(AddFileServer value) {
        return new JAXBElement<AddFileServer>(_AddFileServer_QNAME, AddFileServer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListFileServersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "listFileServersResponse")
    public JAXBElement<ListFileServersResponse> createListFileServersResponse(ListFileServersResponse value) {
        return new JAXBElement<ListFileServersResponse>(_ListFileServersResponse_QNAME, ListFileServersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHostResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getHostResponse")
    public JAXBElement<GetHostResponse> createGetHostResponse(GetHostResponse value) {
        return new JAXBElement<GetHostResponse>(_GetHostResponse_QNAME, GetHostResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getNameResponse")
    public JAXBElement<GetNameResponse> createGetNameResponse(GetNameResponse value) {
        return new JAXBElement<GetNameResponse>(_GetNameResponse_QNAME, GetNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServersByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServersByName")
    public JAXBElement<GetFileServersByName> createGetFileServersByName(GetFileServersByName value) {
        return new JAXBElement<GetFileServersByName>(_GetFileServersByName_QNAME, GetFileServersByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServersByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServersByNameResponse")
    public JAXBElement<GetFileServersByNameResponse> createGetFileServersByNameResponse(GetFileServersByNameResponse value) {
        return new JAXBElement<GetFileServersByNameResponse>(_GetFileServersByNameResponse_QNAME, GetFileServersByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListFileServers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "listFileServers")
    public JAXBElement<ListFileServers> createListFileServers(ListFileServers value) {
        return new JAXBElement<ListFileServers>(_ListFileServers_QNAME, ListFileServers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddFileServerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "addFileServerResponse")
    public JAXBElement<AddFileServerResponse> createAddFileServerResponse(AddFileServerResponse value) {
        return new JAXBElement<AddFileServerResponse>(_AddFileServerResponse_QNAME, AddFileServerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServerByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServerByNameResponse")
    public JAXBElement<GetFileServerByNameResponse> createGetFileServerByNameResponse(GetFileServerByNameResponse value) {
        return new JAXBElement<GetFileServerByNameResponse>(_GetFileServerByNameResponse_QNAME, GetFileServerByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServerByURLResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServerByURLResponse")
    public JAXBElement<GetFileServerByURLResponse> createGetFileServerByURLResponse(GetFileServerByURLResponse value) {
        return new JAXBElement<GetFileServerByURLResponse>(_GetFileServerByURLResponse_QNAME, GetFileServerByURLResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveAliveSignal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "receiveAliveSignal")
    public JAXBElement<ReceiveAliveSignal> createReceiveAliveSignal(ReceiveAliveSignal value) {
        return new JAXBElement<ReceiveAliveSignal>(_ReceiveAliveSignal_QNAME, ReceiveAliveSignal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveAliveSignalResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "receiveAliveSignalResponse")
    public JAXBElement<ReceiveAliveSignalResponse> createReceiveAliveSignalResponse(ReceiveAliveSignalResponse value) {
        return new JAXBElement<ReceiveAliveSignalResponse>(_ReceiveAliveSignalResponse_QNAME, ReceiveAliveSignalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getHost")
    public JAXBElement<GetHost> createGetHost(GetHost value) {
        return new JAXBElement<GetHost>(_GetHost_QNAME, GetHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileServerByURL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.contactServer.server/", name = "getFileServerByURL")
    public JAXBElement<GetFileServerByURL> createGetFileServerByURL(GetFileServerByURL value) {
        return new JAXBElement<GetFileServerByURL>(_GetFileServerByURL_QNAME, GetFileServerByURL.class, null, value);
    }

}
