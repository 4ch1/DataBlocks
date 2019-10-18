package backend.calculus;

import backend.parsing.PlainToken;

import java.util.ArrayList;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for calculations( Facade Design Pattern, Singleton Design Pattern ).
 */

public class Solver {
    private static final Solver ourInstance = new Solver();
    private final InfixToPostfixConverter infixToPostfixConverter = new InfixToPostfixConverter();
    private final PostFixCalculator postFixCalculator = new PostFixCalculator();

    private Solver() {
    }

    public static Solver getInstance() {
        return ourInstance;
    }

    /**
     * Calculates value from expression.
     * @param tokens Expression divided to tokens.
     * @return Value of tokens.
     * @throws IllegalArgumentException If something went wrong during calculations.
     * @see PlainToken
     */
    public double calculate(ArrayList<PlainToken> tokens) throws IllegalArgumentException {
        ArrayList<PlainToken> postTokens = this.infixToPostfixConverter.convert(tokens);
        try {
            return postFixCalculator.calculate(postTokens);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
