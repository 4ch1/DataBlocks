package backend.blockpackage;


import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for describing main communication between blocks.
 */

public class Connection implements Serializable {

    final private Port connectedOut;
    final private Port connectedIn;

    public Connection(Port fPort, Port iPort) {
        Port outPort;
        Port inPort;
        if (fPort.getPortType() == iPort.getPortType()) throw new IllegalArgumentException("Can't connect same type of ports");
        else {
            outPort = fPort.getPortType() == 0 ? fPort : iPort;
            inPort = fPort.getPortType() == 1 ? fPort : iPort;
        }

        if (portsMatching(outPort, inPort)) {
            connectedIn = inPort;
            connectedOut = outPort;
        } else {
            throw new IllegalArgumentException("Types on ports don't match");
        }
    }

    /**
     * Parses block id from port id.
     * @param portId Port id to be parsed.
     * @return Block id.
     * @see Port
     */
    public static String parseBlockId(String portId) {
        String[] split = portId.split(":");
        return split[0];
    }

    /**
     * Indicates if ports are good to be connected.
     * @param outPort Out port.
     * @param inPort In port.
     * @return True if they're good.
     */
    private boolean portsMatching(Port outPort, Port inPort) {
        if (!parseBlockId(outPort.getId()).equals(parseBlockId(inPort.getId()))) return outPort.getType().equals(inPort.getType());

        return false;
    }

    public String getInPortId() {
        return this.connectedIn.getId();
    }

    public String getOutPortId() {
        return this.connectedOut.getId();
    }

    /**
     * Passes data from out port to in port.
     */
    public void pass() {
        this.connectedIn.setData(this.connectedOut.getData());
        this.connectedIn.setGotData();
    }

    /**
     * Gets information about connection.
     * @return Information.
     */
    public String getConStatus() {
        if (!connectedIn.gotData()) return "No calculations were processesed\n" + " for this connection just yet";
        return this.connectedOut.getData().toString();
    }
}
