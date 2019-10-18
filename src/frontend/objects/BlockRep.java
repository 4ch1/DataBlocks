package frontend.objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Block to user.
 * @see backend.blockpackage.Block
 */
public class BlockRep extends Rectangle implements Serializable {
    private final String id;
    private final ArrayList<PortRep> portReps = new ArrayList<>();
    private Paint prevColor = null;

    public BlockRep(String id, double x, double y) {
        this.setFill(Color.GRAY);
        this.setWidth(150);
        this.setHeight(50);
        this.setStroke(Color.BLACK);
        this.setX(x);
        this.setY(y);
        this.id = id;

        this.setOnMousePressed(event -> {
        });

        this.setOnMouseDragged(event -> {
            this.setX(event.getSceneX());
            this.setY(event.getSceneY());
            for (PortRep portRep : portReps) {
                portRep.relativeToBlock(event.getSceneX(), event.getSceneY());
            }
        });

        this.setOnMouseMoved(event -> {
        });

        this.setOnMouseExited(event -> {

        });
    }

    public String getBlockId() {
        return id;
    }

    public void addPort(String id, byte type) {
        PortRep portRep = new PortRep(id, type);

        if (type == 1) {
            portRep.setCenterX(this.getX());
            portRep.setFill(Color.LIGHTPINK);

            if (this.getLast((byte) 1) == null) {
                portRep.setCenterY(this.getY() + 25);
            } else {
                double lastCenter = this.getLast((byte) 1).getCenterY();
                portRep.setCenterY(lastCenter + 25);
                if (!this.contains(portRep.getCenterX(), portRep.getCenterY() + 10))
                    this.setHeight(this.getHeight() + 25);
            }
        } else {
            portRep.setCenterX(this.getX() + this.getWidth());
            portRep.setFill(Color.LIGHTGREEN);

            if (this.getLast((byte) 0) == null) {
                portRep.setCenterY(this.getY() + 25);
            } else {
                double lastCenter = this.getLast((byte) 0).getCenterY();
                portRep.setCenterY(lastCenter + 25);
                if (!this.contains(portRep.getCenterX(), portRep.getCenterY() + 10))
                    this.setHeight(this.getHeight() + 25);
            }
        }

        this.portReps.add(portRep);
        portRep.setRelX(this.getX() - portRep.getCenterX());
        portRep.setRelY(this.getY() - portRep.getCenterY());
    }

    public PortRep getPortRep(String id) {
        for (PortRep portRep : portReps) {
            if (portRep.getPortId().equals(id)) {
                return portRep;
            }
        }

        return null;
    }

    /**
     * Gets last port of given type
     *
     * @param type Type of port
     * @return Last PortRep
     */
    private PortRep getLast(byte type) {
        if (portReps.size() != 0) {
            for (int i = portReps.size() - 1; i >= 0; i--) {
                if (portReps.get(i).getType() == type) {
                    return portReps.get(i);
                }
            }
        }

        return null;
    }

    public ArrayList<PortRep> getPortReps() {
        return portReps;
    }

    /**
     * Highlights block currently being under calculations.
     */
    public void highlightCalc() {
        this.prevColor = this.getFill();
        this.setFill(Color.FORESTGREEN);
    }

    /**
     * Stops highlighting block under calculations. Reverts to previous block color.
     */
    public void stopCalcHighlight() {
        if (this.prevColor != null) {
            this.setFill(this.prevColor);
            this.prevColor = null;
        }
    }
}
