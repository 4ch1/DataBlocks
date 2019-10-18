package frontend.containers.topBox;

import frontend.containers.topBox.cmenus.CalcMenu;
import frontend.containers.topBox.cmenus.DataMenu;
import frontend.containers.topBox.cmenus.FileMenu;
import javafx.scene.control.MenuBar;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing Top Menu.
 */
public class TopBox extends MenuBar {
    private final FileMenu fileMenu;
    private final DataMenu dataMenu;
    private final CalcMenu calcMenu;

    public TopBox() {
        fileMenu = new FileMenu();

        dataMenu = new DataMenu();

        calcMenu = new CalcMenu();

        this.getMenus().addAll(fileMenu, dataMenu, calcMenu);
    }
}
