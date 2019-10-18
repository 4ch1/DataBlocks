package frontend.dialogues;

import backend.datapackage.DataTypeStorage;
import backend.datapackage.dataparsing.DataParser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for communication with user while adding new data to data type storage.
 * @see DataTypeStorage
 */
public class AddDataDialogue extends Dialog<String> {

    public AddDataDialogue() {
        this.setTitle("Add new data type");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ButtonType okButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextArea dataArea = new TextArea();
        dataArea.setPromptText("Data here");

        grid.add(new Label("Give your data type a name"), 0, 0);
        grid.add(nameField, 0, 1);
        grid.add(new Label("Enter your data here:"), 0, 2);
        grid.add(dataArea, 0, 3);

        Node okButton = this.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> okButton.setDisable(newValue.trim().isEmpty()));

        this.getDialogPane().setContent(grid);

        Platform.runLater(dataArea::requestFocus);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return dataArea.getText();
            }
            return null;
        });

        Optional<String> result = this.showAndWait();

        result.ifPresent(data -> {
            try {
                DataTypeStorage.getInstance().addNewDataType(DataParser.getInstance().parseData(nameField.getText(), data));
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Can't parse data");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }
}
