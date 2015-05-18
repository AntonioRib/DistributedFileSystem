
package server.contactServer.ws.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getFileServerByNameResponse", namespace = "http://ws.contactServer.server/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFileServerByNameResponse", namespace = "http://ws.contactServer.server/")
public class GetFileServerByNameResponse {

    @XmlElement(name = "return", namespace = "")
    private server.ServerInfoClass _return;

    /**
     * 
     * @return
     *     returns ServerInfoClass
     */
    public server.ServerInfoClass getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(server.ServerInfoClass _return) {
        this._return = _return;
    }

}
