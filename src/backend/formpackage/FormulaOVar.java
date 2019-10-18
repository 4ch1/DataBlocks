package backend.formpackage;

import backend.parsing.Parser;
import backend.parsing.PlainToken;
import backend.parsing.VarToken;

import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing variable formulas(containing variables) needed to transform data from in Port of block to Out ports of block.
 */
public class FormulaOVar extends AbstractFormula {

    public FormulaOVar(ArrayList<PlainToken> tokens) {
        super(tokens);
    }

    /**
     * Interchanges data of VarToken to actual Data(doubles).
     *
     * @return
     */
    public FormulaOConst interchangeVars() {
        String tokenString = "";

        for (PlainToken token : this.tokens) {
            if (token instanceof VarToken) {
                tokenString = tokenString.concat(String.valueOf(((VarToken) token).getActualData()));
            } else {
                tokenString = tokenString.concat(token.getRaw());
            }
        }

        return (FormulaOConst) Parser.getInstance().parse(tokenString);
    }
}
