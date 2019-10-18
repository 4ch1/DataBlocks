package frontend;

import app.Main;
import frontend.dialogues.AddPortDialogue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for user interactions with Block.
 * @see backend.blockpackage.Block
 */

class BlockContextMenu extends ContextMenu implements Serializable {
    private final MenuItem delBlock = new MenuItem("Delete block");
    private final MenuItem addPort = new MenuItem("Add port");
    private String anchorId;

    public BlockContextMenu() {
        this.getItems().addAll(delBlock, addPort);
        this.delBlock.setOnAction(event -> {
            Main.deleteBlock(anchorId);
            anchorId = null;
        });
        this.addPort.setOnAction(event -> {
            AddPortDialogue addPortDialogue = new AddPortDialogue(anchorId);
        });
    }

    /**
     * Sets id of block the context Menu will be connected to.
     *
     * @param anchorId Block id
     */
    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

}
