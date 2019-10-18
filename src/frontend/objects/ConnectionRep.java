package frontend.objects;

import app.Main;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Connection to user.
 * @see backend.blockpackage.Connection
 */
public class ConnectionRep extends CubicCurve implements Serializable {
    final private int curveFactor = 25;
    private final PortRep inPort;
    private final PortRep outPort;
    private final Tooltip tooltip = new Tooltip();

    public ConnectionRep(PortRep inPort, PortRep outPort) {
        this.inPort = inPort;
        this.outPort = outPort;
        initCurve();
        initToolTip();

    }

    private void initToolTip() {
        Tooltip.install(this, tooltip);
        this.tooltip.setFont(Font.font("Consolas", 14));
        this.tooltip.setOnShown(event -> tooltip.setText(Main.getConInfo(this.inPort.getPortId())));
    }

    private void initCurve() {
        this.setStartX(inPort.getCenterX());
        this.setStartY(inPort.getCenterY());
        this.setControlX1(inPort.getCenterX() + curveFactor);
        this.setControlY1(inPort.getCenterY() - curveFactor);

        this.setEndX(outPort.getCenterX());
        this.setEndY(outPort.getCenterY());
        this.setControlX2(outPort.getCenterX() - curveFactor);
        this.setControlY2(outPort.getCenterY() + curveFactor);

        this.setStroke(Color.FORESTGREEN);
        this.setStrokeWidth(4);
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setFill(Color.TRANSPARENT);
    }

    /**
     * Updates the starting point of curve(In Port coordinates)
     */
    public void updateStart(double x, double y) {
        this.setStartX(x);
        this.setStartY(y);
        this.setControlX1(x + curveFactor);
        this.setControlY1(y - curveFactor);
    }

    public String getOutPortId() {
        return outPort.getPortId();
    }

    /**
     * Updates the end point of curve(Out Port coordinates)
     */
    public void updateEnd(double x, double y) {
        this.setEndX(x);
        this.setEndY(y);
        this.setControlX2(x - curveFactor);
        this.setControlY2(y + curveFactor);
    }

    public String getInPortId() {
        return inPort.getPortId();
    }
}
