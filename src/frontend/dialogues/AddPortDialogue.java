package frontend.dialogues;

import app.Main;
import backend.Schema;
import backend.blockpackage.Port;
import backend.datapackage.Data;
import backend.datapackage.DataTypeStorage;
import backend.formpackage.AbstractFormula;
import backend.formpackage.FormulaOConst;
import backend.idGenerator;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for communication with user while adding new port to block.
 * @see Port
 * @see AbstractFormula
 * @see backend.blockpackage.Block
 */

public class AddPortDialogue extends Dialog<Port> {
    public AddPortDialogue(String blockId) {
        this.setTitle("Creating new block");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setMinHeight(Region.USE_PREF_SIZE);
        Node addButton = this.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        this.getDialogPane().setContent(grid);
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton inPortChoice = new RadioButton("In port");
        RadioButton outPortChoice = new RadioButton("Out port");

        inPortChoice.setToggleGroup(toggleGroup);
        outPortChoice.setToggleGroup(toggleGroup);

        toggleGroup.selectToggle(inPortChoice);
        inPortChoice.setSelected(true);
        inPortChoice.requestFocus();

        Label label = new Label("Select data for the port");
        Label labelForIn = new Label("Give port a variable name");

        TextField textField = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(DataTypeStorage.getInstance().getKeys());
        grid.add(inPortChoice, 1, 0);
        grid.add(outPortChoice, 1, 1);
        grid.add(label, 1, 2);
        grid.add(comboBox, 1, 3);
        grid.add(labelForIn, 1, 4);
        grid.add(textField, 1, 5);

        this.getDialogPane().lookupButton(addButtonType);
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("")) {
                addButton.setDisable(false);
            }
        });
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() == inPortChoice) {
                labelForIn.setVisible(true);
                textField.setVisible(true);
            } else if (toggleGroup.getSelectedToggle() == outPortChoice) {
                labelForIn.setVisible(false);
                textField.setVisible(false);
            }
        });

        this.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Data data = DataTypeStorage.getInstance().getDataType(comboBox.getSelectionModel().getSelectedItem());
                data.nullify();
                if (toggleGroup.getSelectedToggle() == inPortChoice) {
                    return new Port(idGenerator.getInstance().generatePortId(blockId), (byte) 1, data);
                } else {
                    return new Port(idGenerator.getInstance().generatePortId(blockId), (byte) 0, data);
                }
            }
            return null;
        });

        Optional<Port> result = this.showAndWait();

        result.ifPresent(port -> {
            if (port.getPortType() == 1) {
                try {
                    Schema.getInstance().getBlockController().getBlock(blockId).addPort(port, textField.getText());
                    Main.face.addPortToBlock(blockId, port.getId(), port.getPortType());
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                try {
                    FormulasDialogue formulasDialogue = new FormulasDialogue(comboBox.getSelectionModel().getSelectedItem());
                    HashMap<String, AbstractFormula> inFormulas = formulasDialogue.getResult();
                    if (inFormulas != null) {
                        Schema.getInstance().getBlockController().getBlock(blockId).addPort(port, inFormulas);
                        Set<String> keys = inFormulas.keySet();
                        for (String key : keys) {
                            if (inFormulas.get(key) instanceof FormulaOConst && !Schema.getInstance().getBlockController().getBlock(blockId).hasVarFormulas())
                                Main.face.highlightInitial(blockId);
                            else Main.face.stopHiglight(blockId);
                        }
                        Main.face.addPortToBlock(blockId, port.getId(), port.getPortType());
                    }
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Can't parse");
                    alert.setContentText("Your expression is unparsable!");
                    alert.showAndWait();
                }
            }
        });
    }

}
