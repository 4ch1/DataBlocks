package frontend.objects;

import app.Main;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Port to user.
 * @see backend.blockpackage.Port
 */
public class PortRep extends Circle implements Serializable {
    final private String id;
    final private byte type;
    private double relX;
    private double relY;

    public PortRep(String id, byte type) {
        this.id = id;
        this.type = type;
        this.setRadius(7);
        this.setStroke(Color.BLACK);

        this.setOnMouseClicked(event -> Main.conModeDetected(this.id));
    }

    public byte getType() {
        return type;
    }

    public String getPortId() {
        return id;
    }

    /**
     * Set port representation coordinate relative to Block coordinate.
     */
    public void setRelX(double relX) {
        this.relX = relX;
    }

    /**
     * Set port representation coordinate relative to Block coordinate.
     */
    public void setRelY(double relY) {
        this.relY = relY;
    }

    /**
     * Updates coordinates according to block's.
     */
    public void relativeToBlock(double sceneX, double sceneY) {
        this.setCenterX(sceneX - relX);
        this.setCenterY(sceneY - relY);
        Main.face.updateConRep(this);
    }
}
