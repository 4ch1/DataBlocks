package frontend;

import app.Main;
import app.exceptions.CyclePresentException;
import backend.blockpackage.Block;
import backend.blockpackage.BlockController;
import backend.blockpackage.Connection;
import backend.datapackage.Data;
import frontend.containers.CenterBox;
import frontend.containers.topBox.TopBox;
import frontend.dialogues.DataFillDialogue;
import frontend.objects.BlockRep;
import frontend.objects.ConnectionRep;
import frontend.objects.PortRep;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.*;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * A facade responsible for communication of frontend package with outer package.
 * @see backend.blockpackage.Block
 */

public class Face extends BorderPane implements Serializable {
    private final CenterBox centerBox = new CenterBox();
    private final TopBox topBox = new TopBox();
    private final HashMap<String, BlockRep> blocks = new HashMap<>();

    private final ArrayList<ConnectionRep> connections = new ArrayList<>();
    private final BlockContextMenu blockContextMenu = new BlockContextMenu();
    private final ConnectionMode connectionMode = new ConnectionMode();

    public Face() {
        this.setCenter(centerBox);
        this.setTop(topBox);
        connectionMode.getText().setLayoutX(250);//TODO: REPLACE VOODOO MAGIC NUMBERS
        connectionMode.getText().setLayoutY(100);
        this.getChildren().add(connectionMode.getText());
    }

    /**
     * Adds the block on given coordinates.
     *
     * @param id Block id(Must be the same as with the backend block, which this is going to represent)
     */
    public void addBlock(String id, double x, double y) {
        BlockRep blockRep = new BlockRep(id, x, y);
        this.blocks.put(id, blockRep);
        this.getChildren().add(blockRep);
        blockRep.setOnContextMenuRequested(event -> {
            blockContextMenu.show(blockRep, event.getScreenX(), event.getScreenY());
            blockContextMenu.setAnchorId(blockRep.getBlockId());
        });
    }

    /**
     * Deletes the block by block Id.
     *
     * @param id Block id.
     */
    public void delBlock(String id) {
        deleteBlockCons(id);
        this.getChildren().removeAll(this.blocks.get(id).getPortReps());
        this.getChildren().remove(this.blocks.get(id));
        this.blocks.remove(id);
    }


    /**
     * Deletes all connections related to block
     * @param id Block id.
     */
    private void deleteBlockCons(String id) {
        for (int i = this.connections.size() - 1; i >= 0; i--) {
            if (this.blocks.get(id).getPortRep(this.connections.get(i).getInPortId()) != null || this.blocks.get(id).getPortRep(this.connections.get(i).getOutPortId()) != null) {
                this.getChildren().remove(this.connections.get(i));
                this.connections.remove(i);
            }
        }
    }

    /**
     * Adds port representation to block.
     *
     * @param blockId Block Id
     * @param id      Port Id
     * @param type    Port Type
     * @see PortRep
     */
    public void addPortToBlock(String blockId, String id, byte type) {
        this.blocks.get(blockId).addPort(id, type);
        this.getChildren().add(this.blocks.get(blockId).getPortRep(id));
    }

    /**
     * Higlights INITIAL block.
     *
     * @param blockId INITIAL BLock id.
     * @see Block
     */
    public void highlightInitial(String blockId) {
        this.blocks.get(blockId).setFill(Color.DARKCYAN);
    }

    /**
     * Stops initial block highlighting.
     *
     * @param blockId Block id.
     * @see Block
     */
    public void stopHiglight(String blockId) {
        this.blocks.get(blockId).setFill(Color.GRAY);
    }

    /**
     * Adds clicked ports to connection mode for further processing.
     *
     * @param id Port Id
     * @see ConnectionMode
     * @see PortRep
     */
    public void procConnectionMode(String id) {
        if (connectionMode.add(id)) {
            Main.establishCon(connectionMode.getPorts());
            connectionMode.clear();
        }
    }

    /**
     * Draws a curve between two ports
     *
     * @param key   In Port Id
     * @param value Out Port Id
     * @see ConnectionRep
     */
    public void drawConnection(String key, String value) {
        String keyBlockId = Connection.parseBlockId(key);
        String valBlockId = Connection.parseBlockId(value);

        ConnectionRep cubicCurve = new ConnectionRep(this.blocks.get(keyBlockId).getPortRep(key), this.blocks.get(valBlockId).getPortRep(value));
        this.connections.add(cubicCurve);
        this.getChildren().add(cubicCurve);
    }

    /**
     * Updates connection curve coordinates according to the moved ports.
     *
     * @param port Port that changed it's coodrinates.
     * @see ConnectionRep
     */
    public void updateConRep(PortRep port) {
        if (port.getType() == 1) {
            for (ConnectionRep connectionRep : connections) {
                if (connectionRep.getInPortId().equals(port.getPortId()))
                    connectionRep.updateStart(port.getCenterX(), port.getCenterY());
            }
        } else {
            for (ConnectionRep connectionRep : connections) {
                if (connectionRep.getOutPortId().equals(port.getPortId()))
                    connectionRep.updateEnd(port.getCenterX(), port.getCenterY());
            }
        }
    }

    /**
     * Highlights blocks under calculations.
     * @param readys Array of block under calculations.
     * @see BlockRep
     */
    public void highlightCurrentCalc(ArrayList<String> readys) {
        stopCalcHighlight();
        for (String key : readys) {
            this.blocks.get(key).highlightCalc();
        }
    }

    /**
     * Stops calculation highlighting all the blocks.
     */
    private void stopCalcHighlight() {
        Set<String> strings = this.blocks.keySet();
        for (String key : strings) {
            this.blocks.get(key).stopCalcHighlight();
        }
    }

    /**
     * Creates fill data dialog , so the user can manually fill the data in.
     * @param name Name of the data type.
     * @return Initialized data
     * @see backend.datapackage.DataTypeStorage
     * @see Data
     */
    public Data createFillDataDialog(String name) {
        DataFillDialogue dataFillDialogue = new DataFillDialogue(name);
        return dataFillDialogue.getResult();
    }

    /**
     * Alert user, that his scheme contains a cycle
     * @param e Exception with information about cycle
     * @see backend.CycleDetector
     * @see CyclePresentException
     */
    public void alertCycle(CyclePresentException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Cycle detected");
        alert.setContentText(e.getMessage() + "\nBlock affected: " + e.getCausedByBlockId());
        alert.show();
    }


    /**
     * Recreates the frontend part of scheme(Example use: after loading scheme from file)
     * @param blockController Block controlling containing all the data needed for recreation
     * @see backend.Schema
     * @see app.Saver
     */
    public void reCreate(BlockController blockController) {
        HashSet<Block> blocks = blockController.getBlocks();
        double counterX = 100;
        double counterY = 100;

        for (Block block : blocks) {
            recreateBlock(block, counterX, counterY);
            if (counterX >= 900.0) {
                counterX = 0;
                counterY += 100;
            } else counterX += 200;
        }

        recreateConnections(blockController);
    }

    /**
     * Recreates all frontend connections from backend.
     * @param blockController Block Controller with connection data
     */
    private void recreateConnections(BlockController blockController) {
        Collection<Connection> connections = blockController.getConnections();
        for (Connection connection : connections)
            this.drawConnection(connection.getInPortId(), connection.getOutPortId());

    }

    /**
     *
     * Recreates all frontend blocks from backend.
     * @param block Block to acquire data for recreation from.
     */
    private void recreateBlock(Block block, double x, double y) {
        this.addBlock(block.getId(), x, y);
        HashSet<String> portsOutIf = block.getPortsIf(aByte -> aByte == 0);
        HashSet<String> portsInIf = block.getPortsIf(aByte -> aByte == 1);
        for (String key : portsOutIf) addPortToBlock(block.getId(), key, (byte) 0);
        for (String key : portsInIf) addPortToBlock(block.getId(), key, (byte) 1);
        if (block.isInitial() && (portsOutIf.size() != 0)) highlightInitial(block.getId());
    }

    /**
     * Shows a dialog allowing the user to choose a name for the scheme.
     * @return Scheme name
     * @see backend.Schema
     */
    public String getSchemeName() {
        TextInputDialog dialog = new TextInputDialog("Schema name");
        dialog.setTitle("Schema name");
        dialog.setHeaderText("Enter a name for your scheme");

        Optional<String> result = dialog.showAndWait();
        final String[] retString = new String[1];

        result.ifPresent(schemeName -> retString[0] = result.get());

        return retString[0];
    }

    /**
     * Clears all the frontend.
     */
    public void clear() {
        Collection<BlockRep> values = blocks.values();
        for (BlockRep blockRep : values)
            this.getChildren().removeAll(blockRep.getPortReps());

        this.getChildren().removeAll(values);
        this.getChildren().removeAll(this.connections);
        this.blocks.clear();
        this.connections.clear();
    }

    /**
     * Creates alert with given message.
     * @param message Message to display in Alert.
     */
    public void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.show();
    }
}
