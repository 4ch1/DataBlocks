package backend.datapackage;

import app.Main;
import app.Saver;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for stroing data type examples ( Singleton Design Pattern, Prototype Design Pattern )
 */

public class DataTypeStorage implements Serializable {
    private static final DataTypeStorage ourInstance = new DataTypeStorage();
    private HashMap<String, Data> data = new HashMap<>();

    private DataTypeStorage() {
    }

    public static DataTypeStorage getInstance() {
        return ourInstance;
    }

    /**
     * Saves the storage to file;
     */
    public void save() {
        Saver.saveObject(data, Main.schema.getName() + "_DATA");
    }

    public void addNewDataType(Data data) {
        this.data.put(data.getName(), data);
    }

    /**
     * Clones a data type for Port private usage.
     *
     * @param name Name of data type to get cloned.
     * @return Cloned data.
     */
    public Data getDataType(String name) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(data.get(name));
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            return (Data) new ObjectInputStream(bais).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gives all data type names.
     * @return
     */
    public Set<String> getKeys() {
        return this.data.keySet();
    }

    /**
     * Loads storage from disk.
     * @param filename File containing storage.
     * @throws IOException
     * @throws ClassNotFoundException
     * @see FileInputStream For IOException
     * @see ObjectInputStream For ClassNotFoundException
     */
    public void open(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.data = (HashMap<String, Data>) ois.readObject();
        ois.close();
    }
}
