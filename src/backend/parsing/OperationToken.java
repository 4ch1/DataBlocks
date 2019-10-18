package backend.parsing;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for representing operation token in formula.
 */
public class OperationToken extends PlainToken {

    private final int priority;

    public OperationToken(String rawToken) {
        super(tokenType.OPERATION, rawToken);
        this.priority = evalPriority();
    }

    /**
     * Evaluates priority of token.
     *
     * @return Priority;
     */
    private int evalPriority() {
        int prec = 1;
        switch (getRaw()) {
            case "+":
            case "-":
                break;
            case "*":
            case "/":
                prec = 2;
                break;
        }
        return prec;
    }

    public int getPriority() {
        return priority;
    }
}
