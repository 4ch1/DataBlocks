package frontend;

import app.Main;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for user interactions with Center pane.
 * @see frontend.containers.CenterBox
 */

public class CenterContextMenu extends ContextMenu implements Serializable {
    private double ownSceneX;
    private double ownSceneY;

    private final MenuItem addBlock = new MenuItem("Add block");

    public CenterContextMenu() {
        addBlock.setOnAction(event -> Main.addBlock(ownSceneX, ownSceneY));
        this.getItems().add(addBlock);
    }

    public void setOwnSceneX(double ownSceneX) {
        this.ownSceneX = ownSceneX;
    }

    public void setOwnSceneY(double ownSceneY) {
        this.ownSceneY = ownSceneY;
    }
}
