package frontend.containers;

import frontend.CenterContextMenu;
import javafx.scene.control.ScrollPane;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Central pane.
 */
public class CenterBox extends ScrollPane {
    private final CenterContextMenu centerContextMenu = new CenterContextMenu();

    public CenterBox() {
        this.setOnContextMenuRequested(event -> {
            centerContextMenu.show(this, event.getScreenX(), event.getScreenY());
            centerContextMenu.setOwnSceneX(event.getSceneX());
            centerContextMenu.setOwnSceneY(event.getSceneY());
        });
    }

}
