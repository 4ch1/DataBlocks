package backend.parsing;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing variable token.
 */
public class VarToken extends PlainToken {
    private double actualData;
    private final boolean negative;

    public VarToken(String rawData, boolean isNeg) {
        super(tokenType.VAR_TOKEN, rawData);
        this.negative = isNeg;
    }

    public double getActualData() {
        return actualData;
    }

    public void setActualData(double actualData) {
        this.actualData = this.negative ? -actualData : actualData;
    }
}
