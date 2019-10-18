package frontend.containers.topBox.cmenus;

import app.Main;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing File menu.
 */
public class FileMenu extends Menu implements Serializable {
    private final SerMenuItem saveItem;
    private final SerMenuItem openItem;

    public FileMenu() {
        this.setText("Schema");
        saveItem = new SerMenuItem("Save");
        openItem = new SerMenuItem("Open");

        this.getItems().addAll(openItem, saveItem);

        saveItem.setOnAction(event -> Main.saveSchema());
        openItem.setOnAction(event -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) Main.openSchema(file.getAbsolutePath());
        });
    }
}
