package backend.calculus;

import backend.parsing.OperationToken;
import backend.parsing.PlainToken;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for converting expressions to Postfix notation.
 */

class InfixToPostfixConverter {
    private final Stack<PlainToken> stack;
    private final ArrayList<PlainToken> retTokens = new ArrayList<>();

    InfixToPostfixConverter() {
        this.stack = new Stack<>();
    }

    ArrayList<PlainToken> convert(ArrayList<PlainToken> tokens) {
        stack.clear();
        retTokens.clear();
        doConvert(tokens);
        return this.retTokens;
    }

    private void doConvert(ArrayList<PlainToken> tokens) {
        for (PlainToken token : tokens) {
            switch (token.getType()) {
                case OPERATION:
                    operation((OperationToken) token);
                    break;
                case LEFT_PAR:
                    stack.push(token);
                    break;
                case RIGHT_PAR:
                    rightPar();
                    break;
                case VAR_TOKEN:
                case CONST_TOKEN:
                    this.retTokens.add(token);
                    break;
            }
        }

        while (!stack.empty()) {
            this.retTokens.add(stack.pop());
        }
    }

    private void operation(OperationToken token) {
        while (!stack.empty()) {
            PlainToken topToken = stack.pop();
            if (topToken.getType() == PlainToken.tokenType.LEFT_PAR) {
                stack.push(topToken);
                break;
            } else {
                if (topToken instanceof OperationToken) {
                    OperationToken operationToken = (OperationToken) topToken;
                    if (operationToken.getPriority() < token.getPriority()) {
                        stack.push(topToken);
                        break;
                    } else {
                        this.retTokens.add(topToken);
                    }
                }
            }
        }
        stack.push(token);
    }

    private void rightPar() {
        while (!stack.empty()) {
            PlainToken token = stack.pop();
            if (token.getType() == PlainToken.tokenType.LEFT_PAR) break;
            retTokens.add(token);
        }
    }
}
