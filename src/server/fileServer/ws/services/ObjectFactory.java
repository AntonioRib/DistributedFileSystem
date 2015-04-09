
package server.fileServer.ws.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the server.fileServer.ws.services package. 
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

    private final static QName _GetHost_QNAME = new QName("http://ws.fileServer.server/", "getHost");
    private final static QName _MakeDir_QNAME = new QName("http://ws.fileServer.server/", "makeDir");
    private final static QName _GetFileInfo_QNAME = new QName("http://ws.fileServer.server/", "getFileInfo");
    private final static QName _MakeDirResponse_QNAME = new QName("http://ws.fileServer.server/", "makeDirResponse");
    private final static QName _RemoveFile_QNAME = new QName("http://ws.fileServer.server/", "removeFile");
    private final static QName _RemoveFileResponse_QNAME = new QName("http://ws.fileServer.server/", "removeFileResponse");
    private final static QName _SendFile_QNAME = new QName("http://ws.fileServer.server/", "sendFile");
    private final static QName _DirResponse_QNAME = new QName("http://ws.fileServer.server/", "dirResponse");
    private final static QName _Dir_QNAME = new QName("http://ws.fileServer.server/", "dir");
    private final static QName _GetFile_QNAME = new QName("http://ws.fileServer.server/", "getFile");
    private final static QName _IOException_QNAME = new QName("http://ws.fileServer.server/", "IOException");
    private final static QName _ReceiveFile_QNAME = new QName("http://ws.fileServer.server/", "receiveFile");
    private final static QName _InfoNotFoundException_QNAME = new QName("http://ws.fileServer.server/", "InfoNotFoundException");
    private final static QName _GetFileResponse_QNAME = new QName("http://ws.fileServer.server/", "getFileResponse");
    private final static QName _GetHostResponse_QNAME = new QName("http://ws.fileServer.server/", "getHostResponse");
    private final static QName _GetNameResponse_QNAME = new QName("http://ws.fileServer.server/", "getNameResponse");
    private final static QName _GetFileInfoResponse_QNAME = new QName("http://ws.fileServer.server/", "getFileInfoResponse");
    private final static QName _SendFileResponse_QNAME = new QName("http://ws.fileServer.server/", "sendFileResponse");
    private final static QName _GetName_QNAME = new QName("http://ws.fileServer.server/", "getName");
    private final static QName _ReceiveFileResponse_QNAME = new QName("http://ws.fileServer.server/", "receiveFileResponse");
    private final static QName _ReceiveFileArg1_QNAME = new QName("", "arg1");
    private final static QName _GetFileResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: server.fileServer.ws.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetName }
     * 
     */
    public GetName createGetName() {
        return new GetName();
    }

    /**
     * Create an instance of {@link ReceiveFileResponse }
     * 
     */
    public ReceiveFileResponse createReceiveFileResponse() {
        return new ReceiveFileResponse();
    }

    /**
     * Create an instance of {@link GetFileInfoResponse }
     * 
     */
    public GetFileInfoResponse createGetFileInfoResponse() {
        return new GetFileInfoResponse();
    }

    /**
     * Create an instance of {@link SendFileResponse }
     * 
     */
    public SendFileResponse createSendFileResponse() {
        return new SendFileResponse();
    }

    /**
     * Create an instance of {@link ReceiveFile }
     * 
     */
    public ReceiveFile createReceiveFile() {
        return new ReceiveFile();
    }

    /**
     * Create an instance of {@link InfoNotFoundException }
     * 
     */
    public InfoNotFoundException createInfoNotFoundException() {
        return new InfoNotFoundException();
    }

    /**
     * Create an instance of {@link GetFileResponse }
     * 
     */
    public GetFileResponse createGetFileResponse() {
        return new GetFileResponse();
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
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link Dir }
     * 
     */
    public Dir createDir() {
        return new Dir();
    }

    /**
     * Create an instance of {@link GetFile }
     * 
     */
    public GetFile createGetFile() {
        return new GetFile();
    }

    /**
     * Create an instance of {@link DirResponse }
     * 
     */
    public DirResponse createDirResponse() {
        return new DirResponse();
    }

    /**
     * Create an instance of {@link SendFile }
     * 
     */
    public SendFile createSendFile() {
        return new SendFile();
    }

    /**
     * Create an instance of {@link RemoveFileResponse }
     * 
     */
    public RemoveFileResponse createRemoveFileResponse() {
        return new RemoveFileResponse();
    }

    /**
     * Create an instance of {@link RemoveFile }
     * 
     */
    public RemoveFile createRemoveFile() {
        return new RemoveFile();
    }

    /**
     * Create an instance of {@link MakeDirResponse }
     * 
     */
    public MakeDirResponse createMakeDirResponse() {
        return new MakeDirResponse();
    }

    /**
     * Create an instance of {@link GetFileInfo }
     * 
     */
    public GetFileInfo createGetFileInfo() {
        return new GetFileInfo();
    }

    /**
     * Create an instance of {@link GetHost }
     * 
     */
    public GetHost createGetHost() {
        return new GetHost();
    }

    /**
     * Create an instance of {@link MakeDir }
     * 
     */
    public MakeDir createMakeDir() {
        return new MakeDir();
    }

    /**
     * Create an instance of {@link FileInfo }
     * 
     */
    public FileInfo createFileInfo() {
        return new FileInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getHost")
    public JAXBElement<GetHost> createGetHost(GetHost value) {
        return new JAXBElement<GetHost>(_GetHost_QNAME, GetHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MakeDir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "makeDir")
    public JAXBElement<MakeDir> createMakeDir(MakeDir value) {
        return new JAXBElement<MakeDir>(_MakeDir_QNAME, MakeDir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getFileInfo")
    public JAXBElement<GetFileInfo> createGetFileInfo(GetFileInfo value) {
        return new JAXBElement<GetFileInfo>(_GetFileInfo_QNAME, GetFileInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MakeDirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "makeDirResponse")
    public JAXBElement<MakeDirResponse> createMakeDirResponse(MakeDirResponse value) {
        return new JAXBElement<MakeDirResponse>(_MakeDirResponse_QNAME, MakeDirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "removeFile")
    public JAXBElement<RemoveFile> createRemoveFile(RemoveFile value) {
        return new JAXBElement<RemoveFile>(_RemoveFile_QNAME, RemoveFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "removeFileResponse")
    public JAXBElement<RemoveFileResponse> createRemoveFileResponse(RemoveFileResponse value) {
        return new JAXBElement<RemoveFileResponse>(_RemoveFileResponse_QNAME, RemoveFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "sendFile")
    public JAXBElement<SendFile> createSendFile(SendFile value) {
        return new JAXBElement<SendFile>(_SendFile_QNAME, SendFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "dirResponse")
    public JAXBElement<DirResponse> createDirResponse(DirResponse value) {
        return new JAXBElement<DirResponse>(_DirResponse_QNAME, DirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "dir")
    public JAXBElement<Dir> createDir(Dir value) {
        return new JAXBElement<Dir>(_Dir_QNAME, Dir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getFile")
    public JAXBElement<GetFile> createGetFile(GetFile value) {
        return new JAXBElement<GetFile>(_GetFile_QNAME, GetFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "receiveFile")
    public JAXBElement<ReceiveFile> createReceiveFile(ReceiveFile value) {
        return new JAXBElement<ReceiveFile>(_ReceiveFile_QNAME, ReceiveFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfoNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "InfoNotFoundException")
    public JAXBElement<InfoNotFoundException> createInfoNotFoundException(InfoNotFoundException value) {
        return new JAXBElement<InfoNotFoundException>(_InfoNotFoundException_QNAME, InfoNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getFileResponse")
    public JAXBElement<GetFileResponse> createGetFileResponse(GetFileResponse value) {
        return new JAXBElement<GetFileResponse>(_GetFileResponse_QNAME, GetFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetHostResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getHostResponse")
    public JAXBElement<GetHostResponse> createGetHostResponse(GetHostResponse value) {
        return new JAXBElement<GetHostResponse>(_GetHostResponse_QNAME, GetHostResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getNameResponse")
    public JAXBElement<GetNameResponse> createGetNameResponse(GetNameResponse value) {
        return new JAXBElement<GetNameResponse>(_GetNameResponse_QNAME, GetNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getFileInfoResponse")
    public JAXBElement<GetFileInfoResponse> createGetFileInfoResponse(GetFileInfoResponse value) {
        return new JAXBElement<GetFileInfoResponse>(_GetFileInfoResponse_QNAME, GetFileInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "sendFileResponse")
    public JAXBElement<SendFileResponse> createSendFileResponse(SendFileResponse value) {
        return new JAXBElement<SendFileResponse>(_SendFileResponse_QNAME, SendFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "getName")
    public JAXBElement<GetName> createGetName(GetName value) {
        return new JAXBElement<GetName>(_GetName_QNAME, GetName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.fileServer.server/", name = "receiveFileResponse")
    public JAXBElement<ReceiveFileResponse> createReceiveFileResponse(ReceiveFileResponse value) {
        return new JAXBElement<ReceiveFileResponse>(_ReceiveFileResponse_QNAME, ReceiveFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = ReceiveFile.class)
    public JAXBElement<byte[]> createReceiveFileArg1(byte[] value) {
        return new JAXBElement<byte[]>(_ReceiveFileArg1_QNAME, byte[].class, ReceiveFile.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetFileResponse.class)
    public JAXBElement<byte[]> createGetFileResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetFileResponseReturn_QNAME, byte[].class, GetFileResponse.class, ((byte[]) value));
    }

}
