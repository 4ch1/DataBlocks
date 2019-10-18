package frontend.containers.topBox.cmenus;

import frontend.DataShowcase;
import frontend.dialogues.AddDataDialogue;
import javafx.scene.control.Menu;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Data menu.
 */
public class DataMenu extends Menu implements Serializable {
    private SerMenuItem addDataTypeItem;
    private SerMenuItem showCurrTypes;

    public DataMenu() {
        this.setText("Data");
        initItems();
        this.getItems().addAll(addDataTypeItem, showCurrTypes);
    }

    private void initItems() {
        addDataTypeItem = new SerMenuItem("Add new");
        addDataTypeItem.setOnAction(event -> {
            AddDataDialogue addDataDialogue = new AddDataDialogue();
        });
        showCurrTypes = new SerMenuItem("Show existing");
        showCurrTypes.setOnAction(event -> {
            DataShowcase dataShowCase = new DataShowcase();
            dataShowCase.show();
        });
    }
}
