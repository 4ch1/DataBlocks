package backend.calculus;

import backend.parsing.OperationToken;
import backend.parsing.PlainToken;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for calculating the expressions in postfix notation.
 */

class PostFixCalculator {
    private final Stack<Double> stack;

    public PostFixCalculator() {
        stack = new Stack<>();
    }

    public double calculate(ArrayList<PlainToken> tokens) throws IllegalArgumentException {
        for (PlainToken token : tokens) {
            if (token instanceof OperationToken) doOperation((OperationToken) token);
            else stack.push(Double.parseDouble(token.getRaw()));

        }

        Double result = stack.pop();
        if (!stack.empty()) {
            stack.clear();
            throw new IllegalArgumentException("Can't solve given formula");
        }
        return result;
    }

    private void doOperation(OperationToken operationToken) {
        Double secondOperand = stack.pop();
        Double firstOperand = stack.pop();

        switch (operationToken.getRaw()) {
            case "+":
                stack.push(firstOperand + secondOperand);
                break;
            case "-":
                stack.push(firstOperand - secondOperand);
                break;
            case "*":
                stack.push(firstOperand * secondOperand);
                break;
            case "/":
                stack.push(firstOperand / secondOperand);
                break;
        }
    }
}
