package backend.blockpackage;

import backend.datapackage.Data;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for obtaining and owning data.
 */

public class Port implements Serializable {

    final private String id;//Consists of <Block id>:<Port part of id>
    final private byte type;// 0 for out, 1 for in.
    private Data data;
    private boolean gotData;

    public Port(String id, byte type, Data data) {
        this.type = type;
        this.id = id;
        this.data = data;
    }

    /**
     * Get's Type of Port's Data
     * @return Port's data type name.
     */
    public String getType() {
        return this.data.getName();
    }

    /**
     * Get's Type of Port(In or Out)
     * @return Return Port Type
     */
    public byte getPortType() {
        return this.type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    /**
     * Set the flag of port, indicating it got the data from the connection.
     */
    public void setGotData() {
        this.gotData = true;
    }

    public boolean gotData() {
        return gotData;
    }

    /**
     * A request to fill the non-connected in ports manually.
     */
    public void manuallyFill() {
        this.data.fillMe();
    }

    public void reset() {
        gotData = false;
        data.nullify();
    }
}
