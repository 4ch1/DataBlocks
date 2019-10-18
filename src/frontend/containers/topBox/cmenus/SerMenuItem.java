package frontend.containers.topBox.cmenus;

import javafx.scene.control.MenuItem;

import java.io.Serializable;

class SerMenuItem extends MenuItem implements Serializable {
    public SerMenuItem(String add_new) {
        super(add_new);
    }
}
