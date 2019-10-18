package backend.datapackage.dataparsing;

import backend.datapackage.Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for parsing data input to data types.
 */

public class DataParser {
    static private final DataParser instance = new DataParser();

    private DataParser() {
    }

    static public DataParser getInstance() {
        return instance;
    }

    /**
     * Parses data to Data class format
     * @param name Name of the data type.
     * @param userInput String to be parsed.
     * @return Formatted data.
     * @see JSONObject
     */
    public Data parseData(String name, String userInput) {
        JSONObject data = null;
        try {
            data = new JSONObject(userInput);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Data toData = convertToData(name, data);

        if (toData != null) {
            return toData;
        } else {
            throw new IllegalArgumentException("Only double allowed as data");
        }
    }

    /**
     * Converts data from JSON format to Data format.
     * @param name Data type name.
     * @param data Data in JSON format
     * @return Data in Data(backend) format.
     * @see JSONObject
     * @see Data
     */
    private Data convertToData(String name, JSONObject data) {
        Iterator<?> keys = data.keys();
        HashMap<String, Double> parsedData = new HashMap<>();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                parsedData.put(key, data.getDouble(key));
            } catch (JSONException e) {
                return null;
            }
        }
        return new Data(name, parsedData);
    }
}
