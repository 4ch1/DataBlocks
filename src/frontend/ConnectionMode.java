package frontend;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for frontend part of connecting ports.
 * @see backend.blockpackage.Block
 */

class ConnectionMode {
    private String firstPort = null;
    private String lastPort = null;
    private Text conModeText;

    public ConnectionMode() {
        this.initConMText();

    }

    private void initConMText() {
        conModeText = new Text("You're in connection mode");
        conModeText.setFill(Color.GREEN);
        conModeText.setStroke(Color.BLACK);
        conModeText.setFont(Font.font("Verdana", 24));
        conModeText.setVisible(false);
    }

    public Node getText() {
        return conModeText;
    }

    /**
     * Adds block to the Mode on click
     *
     * @param id Block id
     * @return True if 2 blocks are collected and ready to be checked for compatibility.
     */
    public boolean add(String id) {
        conModeText.setVisible(true);
        if (firstPort != null) {
            establishCon(id);
            return true;
        } else
            firstPort = id;

        return false;
    }

    private void establishCon(String id) {
        conModeText.setVisible(false);
        lastPort = id;
    }

    public Pair<String, String> getPorts() {
        return new Pair<>(firstPort, lastPort);
    }

    public void clear() {
        this.conModeText.setVisible(false);
        this.lastPort = null;
        this.firstPort = null;
    }
}
