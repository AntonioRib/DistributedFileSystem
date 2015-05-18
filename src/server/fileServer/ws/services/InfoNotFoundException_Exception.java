
package server.fileServer.ws.services;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InfoNotFoundException", targetNamespace = "http://ws.fileServer.server/")
public class InfoNotFoundException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InfoNotFoundException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InfoNotFoundException_Exception(String message, InfoNotFoundException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InfoNotFoundException_Exception(String message, InfoNotFoundException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: server.fileServer.ws.services.InfoNotFoundException
     */
    public InfoNotFoundException getFaultInfo() {
        return faultInfo;
    }

}
