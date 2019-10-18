package frontend;

import backend.datapackage.DataTypeStorage;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing all the stored data types to user.
 * @see backend.blockpackage.Block
 */

public class DataShowcase extends Stage {
    private final BorderPane borderPane = new BorderPane();
    private final ListView<String> names = new ListView<>();
    private final TextArea textArea = new TextArea();

    public DataShowcase() {
        Stage stage = new Stage();
        stage.setTitle("Data Types");
        initStage();
        fillData();
    }

    /**
     * Fills the data type names. And set a listened to fill the data example when needed.
     */
    private void fillData() {
        names.getItems().addAll(DataTypeStorage.getInstance().getKeys());
        names.getSelectionModel().selectedItemProperty().addListener((ov, old_val, new_val) -> {
            textArea.clear();
            textArea.setText(DataTypeStorage.getInstance().getDataType(new_val).toString());
        });
    }

    private void initStage() {
        this.setScene(new Scene(borderPane, 800, 800));
        borderPane.setLeft(names);
        borderPane.setRight(textArea);
        textArea.setEditable(false);
        textArea.setMinHeight(800);
        textArea.setMinWidth(600);
    }
}
