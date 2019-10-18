package backend.formpackage;

import backend.parsing.PlainToken;

import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing constant(containing only doubles) formulas needed to transform data from in Port of block to Out ports of block.
 */
public class FormulaOConst extends AbstractFormula {

    public FormulaOConst(ArrayList<PlainToken> tokens) {
        super(tokens);
    }

}
