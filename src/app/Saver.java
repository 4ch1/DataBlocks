package app;

import backend.datapackage.DataTypeStorage;
import backend.idGenerator;

import java.io.*;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for saving and loading/recreating most vital data.
 */

public class Saver {

    public static void save() throws IllegalArgumentException {
        if (Main.schema.getName() == null) {
            Main.createName();
            if (Main.schema.getName() == null) throw new IllegalArgumentException("Scheme has no name!");
        }
        Main.schema.save();
        DataTypeStorage.getInstance().save();
        saveObject(idGenerator.getInstance(), Main.schema.getName() + "_IDS");
    }

    public static void open(String filename) {
        try {
            Main.schema.open(filename);
            Main.face.reCreate(Main.schema.getBlockController());
            DataTypeStorage.getInstance().open(filename + "_DATA");
            FileInputStream fis = new FileInputStream(filename + "_IDS");
            ObjectInputStream ois = new ObjectInputStream(fis);
            idGenerator generator = (idGenerator) ois.readObject();
            idGenerator.getInstance().merge(generator);
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param object Object to be saved.
     * @param fileName Name of the file the object will be saved to.
     * @param <T> Object type to be saved.
     */
    public static <T> void saveObject(T object, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
