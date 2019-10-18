package frontend.containers.topBox.cmenus;

import app.Main;
import javafx.scene.control.Menu;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Calculations menu.
 */
public class CalcMenu extends Menu implements Serializable {
    private SerMenuItem startCalcs;
    private SerMenuItem resetSchema;

    public CalcMenu() {
        this.setText("Calculus");
        initItems();
        this.getItems().addAll(startCalcs, resetSchema);
    }

    private void initItems() {
        startCalcs = new SerMenuItem("Step calculus");
        resetSchema = new SerMenuItem("Reset schema");
        startCalcs.setOnAction(event -> Main.stepCalc());
        resetSchema.setOnAction(event -> Main.resetSchema());
    }
}
