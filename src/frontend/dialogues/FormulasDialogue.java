package frontend.dialogues;

import backend.datapackage.DataTypeStorage;
import backend.formpackage.AbstractFormula;
import backend.parsing.Parser;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for communication with user while adding formulas for each data field on Out Port.
 * @see backend.blockpackage.Port
 * @see AbstractFormula
 */
class FormulasDialogue extends Dialog<HashMap<String, AbstractFormula>> {

    public FormulasDialogue(String dataTypeName) {
        this.setTitle("Formulas");
        this.setHeaderText("Fill the formulas for variables for the " + dataTypeName);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ArrayList<Pair<Label, TextField>> arrayList = initArray(dataTypeName);
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

        Optional<HashMap<String, AbstractFormula>> result = this.showAndWait();

        result.ifPresent(usernamePassword -> {
        });
    }

    private HashMap<String, AbstractFormula> getData(ArrayList<Pair<Label, TextField>> arrayList) {
        HashMap<String, AbstractFormula> retArray = new HashMap<>();
        for (Pair<Label, TextField> pair : arrayList) {
            try {
                retArray.put(pair.getKey().getText(), Parser.getInstance().parse(pair.getValue().getText()));
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.show();
                return null;
            }
        }

        return retArray;
    }

    static public ArrayList<Pair<Label, TextField>> initArray(String dataTypeName) {
        ArrayList<Pair<Label, TextField>> pairArrayList = new ArrayList<>();
        Set<String> strings = DataTypeStorage.getInstance().getDataType(dataTypeName).getKeys();

        for (String name : strings) {
            TextField textField = new TextField();
            Label label = new Label(name);
            pairArrayList.add(new Pair<>(label, textField));
        }

        return pairArrayList;
    }
}

