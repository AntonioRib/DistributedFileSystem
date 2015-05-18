
package server.contactServer.ws.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serverInfoClass complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serverInfoClass">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastHeartbeat" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serverInfoClass", propOrder = {
    "lastHeartbeat"
})
public class ServerInfoClass {

    protected long lastHeartbeat;

    /**
     * Gets the value of the lastHeartbeat property.
     * 
     */
    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    /**
     * Sets the value of the lastHeartbeat property.
     * 
     */
    public void setLastHeartbeat(long value) {
        this.lastHeartbeat = value;
    }

}
