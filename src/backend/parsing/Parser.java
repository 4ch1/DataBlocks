package backend.parsing;

import backend.formpackage.AbstractFormula;
import backend.formpackage.FormulaOConst;
import backend.formpackage.FormulaOVar;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for parsing formulas into tokens.
 */
public class Parser {
    private static final Parser ourInstance = new Parser();
    private final Pattern opPattern = Pattern.compile("[+\\-*/]");
    private final Pattern conPattern = Pattern.compile("(?<!\\d)[-]?\\d*\\.\\d+");
    private final Pattern varPattern = Pattern.compile("((?<!\\w\\.\\w)[-]?[a-zA-Z]+\\.[a-zA-Z]+)");

    private Parser() {
    }

    public static Parser getInstance() {
        return ourInstance;
    }

    /**
     * Parses data in tokens.
     *
     * @param data String of user input formula data.
     * @return Parsed formula.
     */
    public AbstractFormula parse(String data) {
        ArrayList<PlainToken> plainTokenArrayList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(" + conPattern.pattern() + ")|(" + varPattern.pattern() + ")|(" + opPattern.pattern() + ")|[(,)]");
        Matcher matcher = pattern.matcher(data);
        boolean isVar = false;
        String matchingString = "";
        while (matcher.find()) {
            String exFound = matcher.group();
            matchingString = matchingString.concat(exFound);
            try {
                switch (parseTokenType(exFound)) {
                    case VAR_TOKEN:
                        isVar = true;
                        boolean isNeg = (exFound.charAt(0) == '-');
                        if (isNeg) exFound = exFound.replaceFirst("-", "");
                        plainTokenArrayList.add(new VarToken(exFound, isNeg));
                        break;
                    case OPERATION:
                        plainTokenArrayList.add(new OperationToken(exFound));
                        break;
                    case LEFT_PAR:
                        plainTokenArrayList.add(new PlainToken(PlainToken.tokenType.LEFT_PAR, exFound));
                        break;
                    case RIGHT_PAR:
                        plainTokenArrayList.add(new PlainToken(PlainToken.tokenType.RIGHT_PAR, exFound));
                        break;
                    case CONST_TOKEN:
                        plainTokenArrayList.add(new PlainToken(PlainToken.tokenType.CONST_TOKEN, exFound));
                        break;
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Unparsable expression");
            }
        }
        if (data.trim().replaceAll("\\s+", "").equals(matchingString)) return isVar ? new FormulaOVar(plainTokenArrayList) : new FormulaOConst(plainTokenArrayList);
        else throw new IllegalArgumentException("Unparsable expression");
    }

    /**
     * Gets token type.
     * @param exFound Token raw(pure String) view.
     * @return Token type.
     * @see PlainToken For tokenType enumeration.
     */
    private PlainToken.tokenType parseTokenType(String exFound) {
        if (exFound.matches(opPattern.pattern())) return PlainToken.tokenType.OPERATION;
        if (exFound.matches(varPattern.pattern())) return PlainToken.tokenType.VAR_TOKEN;
        if (exFound.matches(conPattern.pattern())) return PlainToken.tokenType.CONST_TOKEN;
        if (exFound.equals("(")) return PlainToken.tokenType.LEFT_PAR;
        if (exFound.equals(")")) return PlainToken.tokenType.RIGHT_PAR;

        return null;
    }
}

