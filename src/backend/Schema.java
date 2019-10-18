package backend;

import app.Main;
import app.Saver;
import app.exceptions.CyclePresentException;
import backend.blockpackage.BlockController;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing schema(Facade Design Pattern).
 */
public class Schema implements Serializable {
    private static final Schema ourInstance = new Schema();
    private String name = null;
    private BlockController blockController = new BlockController();
    private CycleDetector cycleDetector = new CycleDetector(blockController);
    private boolean cyclesRechek = true;

    private Schema() {
    }

    public static Schema getInstance() {
        return ourInstance;
    }

    public String addBlock() {
        return this.blockController.addBlock();
    }

    public Pair<String, String> establishConnection(String fPort, String sPort) {
        cyclesRechek = true;
        return this.blockController.connect(fPort, sPort);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlockController getBlockController() {
        return blockController;
    }

    public void delBlock(String id) {
        this.blockController.delBlock(id);
    }

    /**
     * Steps calc a step further.
     *
     * @throws CyclePresentException If cycles are detected.
     * @see BlockController
     */
    public void stepCalc() throws CyclePresentException {
        String cyclingBlock = null;

        if (cyclesRechek) cyclingBlock = cycleDetector.cyclePresent();

        if (cyclingBlock == null) {
            ArrayList<String> readys = this.blockController.getReadys();
            Main.highlightCurrenctCalc(readys);
            blockController.calc(readys);
            blockController.passData(readys);
        } else {
            throw new CyclePresentException("Found cycle in your scheme", cyclingBlock);
        }
    }

    /**
     * Get's info about Connection.
     * @param portId In Port Id to identify connection.
     * @return Connection info.
     * @see backend.blockpackage.Connection
     */
    public String getConnectionInfo(String portId) {
        return this.blockController.getConnectionInfo(portId);
    }

    /**
     * Resets blockController for repeating calculations.
     */
    public void reset() {
        blockController.reset();
    }

    /**
     * Saves blockController to disk.
     */
    public void save() {
        Saver.saveObject(blockController, this.name);
    }

    /**
     * Loads schema from disk.
     * @param filename File where schema is stored.
     * @throws IOException
     * @throws ClassNotFoundException
     * @see FileInputStream for IOException
     * @see ObjectInputStream for ClassNotFoundException
     */
    public void open(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.blockController = (BlockController) ois.readObject();
        this.name = filename;
        cycleDetector = new CycleDetector(blockController);
        Main.resetSchema();
        ois.close();
    }

    public void clear() {
        this.blockController.clear();
    }
}
