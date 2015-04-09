
package server.fileServer.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getFileInfoResponse", namespace = "http://ws.fileServer.server/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFileInfoResponse", namespace = "http://ws.fileServer.server/")
public class GetFileInfoResponse {

    @XmlElement(name = "return", namespace = "")
    private fileSystem.FileInfo _return;

    /**
     * 
     * @return
     *     returns FileInfo
     */
    public fileSystem.FileInfo getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(fileSystem.FileInfo _return) {
        this._return = _return;
    }

}
