package app;

import app.exceptions.CyclePresentException;
import backend.Schema;
import backend.datapackage.Data;
import frontend.Face;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Main class with all static Controller(Model-View-Controller) methods and main components of programm.
 */


public class Main extends Application {
    public static final Schema schema = Schema.getInstance();
    public static final Face face = new Face();

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * @param id Backend id of the block to be deleted.
     */
    public static void deleteBlock(String id) {
        face.delBlock(id);
        schema.delBlock(id);
    }

    public static void addBlock(double ownSceneX, double ownSceneY) {
        face.addBlock(schema.addBlock(), ownSceneX, ownSceneY);
    }

    /**
     * Steps calculations one step further(one step == one portion of blocks, with all the data ready)
     */
    public static void stepCalc() {
        try {
            schema.stepCalc();
        } catch (CyclePresentException e) {
            face.alertCycle(e);
        }
    }

    /**
     * Passes port id to Connection Mode class.
     * @param id Id of port that was clicked on to establish connection.
     * @see frontend.ConnectionMode
     */
    public static void conModeDetected(String id) {
        face.procConnectionMode(id);
    }

    /**
     * Establishes connection in both front and backends.
     * @param ports Port ids to establish the connection.
     */
    public static void establishCon(Pair<String, String> ports) {
        Pair<String, String> sortedCon = schema.establishConnection(ports.getKey(), ports.getValue());
        if (sortedCon != null) {
            face.drawConnection(sortedCon.getKey(), sortedCon.getValue());
        }
    }

    /**
     * Command to highlight block processing calculations.
     * @param readys Array of blocks ready to be calculated.
     */
    public static void highlightCurrenctCalc(ArrayList<String> readys) {
        face.highlightCurrentCalc(readys);
    }

    /**
     * Gets info about connection to highlight it after.
     * @param portId Port id of type In, indicating the connection.
     * @return Connection info.
     * @see backend.blockpackage.Connection
     * @see backend.blockpackage.BlockController
     */
    public static String getConInfo(String portId) {
        return schema.getConnectionInfo(portId);
    }

    /**
     * Creates a fill data dialog for non-connected In port.
     * @param name Data type name.
     * @return Returns filled data.
     */
    public static Data askForFillDialog(String name) {
        return face.createFillDataDialog(name);
    }

    /**
     * Resets schema for repeating calculations.
     */
    public static void resetSchema() {
        schema.reset();
    }

    public static void saveSchema() {
        try {
            Saver.save();
        } catch (IllegalArgumentException exception) {
            face.alert(exception.getMessage());
        }
    }

    public static void openSchema(String filename) {
        schema.clear();
        face.clear();
        Saver.open(filename);
    }

    /**
     * Creates a Choose a Name dialog, so the user can name their's scheme.
     */
    public static void createName() {
        Main.schema.setName(face.getSchemeName());
    }


    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(face, 800, 800);
        primaryStage.setTitle("EditBlocks");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
