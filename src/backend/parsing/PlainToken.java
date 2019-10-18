package backend.parsing;

import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing plain(no special use) token.
 */
public class PlainToken implements Serializable {

    private final String raw;

    private final tokenType type;

    public PlainToken(tokenType type, String raw) {
        this.raw = raw;
        this.type = type;
    }

    public String getRaw() {
        return raw;
    }

    public tokenType getType() {
        return type;
    }

    public enum tokenType {VAR_TOKEN, CONST_TOKEN, OPERATION, LEFT_PAR, RIGHT_PAR}
}
