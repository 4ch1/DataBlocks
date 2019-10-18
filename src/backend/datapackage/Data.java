package backend.datapackage;

import app.Main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing data.
 */

public class Data implements Serializable {
    final private String name;
    private HashMap<String, Double> data;

    public Data(String name, HashMap<String, Double> parsedData) {
        this.name = name;
        this.data = parsedData;
    }

    /**
     * Gives data type name.
     * @return Data type name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String retString = "";
        Set<String> keySet = this.data.keySet();
        for (String key : keySet) {
            retString = retString.concat(key + ": " + data.get(key) + "\n");
        }
        return retString;
    }

    /**
     * Gives a set of data fields in data type.
     * @return Set of fields names.
     */
    public Set<String> getKeys() {
        return this.data.keySet();
    }

    /**
     * Get value from data
     * @param key Data field name.
     * @return Value of data field.
     * @throws IllegalArgumentException If no data field with given name was found.
     */
    public Double getVal(String key) throws IllegalArgumentException {
        if (!this.data.containsKey(key)) throw new IllegalArgumentException("No " + key + " in " + this.name);

        return this.data.get(key);
    }

    /**
     * Set the data field value to given value.
     *
     * @param key   Data field name.
     * @param value New value.
     * @throws IllegalArgumentException If no data field with given name was found.
     */
    public void setVal(String key, double value) throws IllegalArgumentException {
        try {
            this.data.put(key, value);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("No " + key + " in " + this.name);
        }
    }

    /**
     * Nullifies all the values in data.
     */
    public void nullify() {
        Set<String> strings = this.data.keySet();
        for (String key : strings) {
            this.data.put(key, null);
        }
    }

    /**
     * Request manual fill of data.
     */
    public void fillMe() {
        this.data = Main.askForFillDialog(this.name).data;
    }
}
