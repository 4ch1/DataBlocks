package backend.formpackage;

import backend.parsing.PlainToken;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing formulas needed to transform data from in Port of block to Out ports of block.
 */
public abstract class AbstractFormula implements Serializable {
    final ArrayList<PlainToken> tokens;

    AbstractFormula(ArrayList<PlainToken> parsedTokens) {
        this.tokens = parsedTokens;
    }

    public ArrayList<PlainToken> getTokens() {
        return tokens;
    }

}
