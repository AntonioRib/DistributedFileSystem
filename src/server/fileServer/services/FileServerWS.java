
package server.fileServer.services;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import server.fileServer.FileServer;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "FileServerWS", targetNamespace = "http://fileServer.server/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface FileServerWS extends FileServer {


    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getName", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetName")
    @ResponseWrapper(localName = "getNameResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetNameResponse")
    @Action(input = "http://fileServer.server/FileServerWS/getNameRequest", output = "http://fileServer.server/FileServerWS/getNameResponse")
    public String getName();

    /**
     * 
     * @param arg0
     * @return
     *     returns byte[]
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFile", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetFile")
    @ResponseWrapper(localName = "getFileResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetFileResponse")
    @Action(input = "http://fileServer.server/FileServerWS/getFileRequest", output = "http://fileServer.server/FileServerWS/getFileResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://fileServer.server/FileServerWS/getFile/Fault/IOException")
    })
    public byte[] getFile(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
    ;

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getHost", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetHost")
    @ResponseWrapper(localName = "getHostResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetHostResponse")
    @Action(input = "http://fileServer.server/FileServerWS/getHostRequest", output = "http://fileServer.server/FileServerWS/getHostResponse")
    public String getHost();

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "removeFile", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.RemoveFile")
    @ResponseWrapper(localName = "removeFileResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.RemoveFileResponse")
    @Action(input = "http://fileServer.server/FileServerWS/removeFileRequest", output = "http://fileServer.server/FileServerWS/removeFileResponse")
    public boolean removeFile(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        boolean arg1);

    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sendFile", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.SendFile")
    @ResponseWrapper(localName = "sendFileResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.SendFileResponse")
    @Action(input = "http://fileServer.server/FileServerWS/sendFileRequest", output = "http://fileServer.server/FileServerWS/sendFileResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://fileServer.server/FileServerWS/sendFile/Fault/IOException")
    })
    public boolean sendFile(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        boolean arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3)
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "makeDir", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.MakeDir")
    @ResponseWrapper(localName = "makeDirResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.MakeDirResponse")
    @Action(input = "http://fileServer.server/FileServerWS/makeDirRequest", output = "http://fileServer.server/FileServerWS/makeDirResponse")
    public boolean makeDir(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFileInfo", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetFileInfo")
    @ResponseWrapper(localName = "getFileInfoResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.GetFileInfoResponse")
    @Action(input = "http://fileServer.server/FileServerWS/getFileInfoRequest", output = "http://fileServer.server/FileServerWS/getFileInfoResponse", fault = {
        @FaultAction(className = InfoNotFoundException_Exception.class, value = "http://fileServer.server/FileServerWS/getFileInfo/Fault/InfoNotFoundException")
    })
    public List<String> getFileInfo(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "dir", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.Dir")
    @ResponseWrapper(localName = "dirResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.DirResponse")
    @Action(input = "http://fileServer.server/FileServerWS/dirRequest", output = "http://fileServer.server/FileServerWS/dirResponse", fault = {
        @FaultAction(className = InfoNotFoundException_Exception.class, value = "http://fileServer.server/FileServerWS/dir/Fault/InfoNotFoundException")
    })
    public List<String> dir(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0)
    ;

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "receiveFile", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.ReceiveFile")
    @ResponseWrapper(localName = "receiveFileResponse", targetNamespace = "http://fileServer.server/", className = "server.fileServer.services.ReceiveFileResponse")
    @Action(input = "http://fileServer.server/FileServerWS/receiveFileRequest", output = "http://fileServer.server/FileServerWS/receiveFileResponse", fault = {
        @FaultAction(className = IOException_Exception.class, value = "http://fileServer.server/FileServerWS/receiveFile/Fault/IOException")
    })
    public boolean receiveFile(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        byte[] arg1)
    ;

}
