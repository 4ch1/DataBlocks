package frontend.dialogues;

import backend.datapackage.Data;
import backend.datapackage.DataTypeStorage;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for communication with user while manually filling data on ports.
 * @see Data
 */
public class DataFillDialogue extends Dialog<Data> {
    final private String dataTypeName;

    public DataFillDialogue(String dataTypeName) {
        this.dataTypeName = dataTypeName;
        this.setTitle("Fill data");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ArrayList<Pair<Label, TextField>> arrayList = FormulasDialogue.initArray(dataTypeName);
        int i = 0;
        for (Pair<Label, TextField> pair : arrayList) {
            grid.add(pair.getKey(), 1, i);
            grid.add(pair.getValue(), 2, i);
            i++;
        }

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return getData(arrayList);
            }
            return null;
        });

        Optional<Data> result = this.showAndWait();

        result.ifPresent(data -> {
        });
    }

    private Data getData(ArrayList<Pair<Label, TextField>> arrayList) {
        Data data = DataTypeStorage.getInstance().getDataType(this.dataTypeName);
        for (Pair<Label, TextField> pair : arrayList) {
            try {
                data.setVal(pair.getKey().getText(), Double.parseDouble(pair.getValue().getText()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Wrong data format");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("You must enter only double as values for data");
                alert.show();
            }
        }
        return data;
    }
}
