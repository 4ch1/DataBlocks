package backend.blockpackage;

import backend.calculus.Solver;
import backend.datapackage.Data;
import backend.datapackage.DataTypeStorage;
import backend.formpackage.AbstractFormula;
import backend.formpackage.FormulaOConst;
import backend.formpackage.FormulaOVar;
import backend.parsing.PlainToken;
import backend.parsing.VarToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class describing block and block calculation related data.
 */

public class Block implements Serializable {
    final private String id;
    // < Out Port Id , < Out Port Data Field Name, Formula > >
    private final HashMap<String, HashMap<String, AbstractFormula>> formulas = new HashMap<>();
    private final HashMap<String, Port> ports = new HashMap<>();
    //              <varName,inPortId>
    private final HashMap<String, String> innerIds = new HashMap<>();
    private boolean initial = true;
    private boolean calced = false;

    public Block(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Adds port with type Out to block.
     * @param port Initialized port to be added to block.
     * @param formula Formulas for calculating data on port connected.
     */
    public void addPort(Port port, HashMap<String, AbstractFormula> formula) {
        Set<String> keys = formula.keySet();

        for (String key : keys) {
            AbstractFormula abstractFormula = formula.get(key);

            if (abstractFormula instanceof FormulaOVar) {
                if (!ownsVars((FormulaOVar) abstractFormula)) throw new IllegalArgumentException("Formulas uses undeclared variables");
                initial = false;
            }
            this.ports.put(port.getId(), port);
            this.formulas.put(port.getId(), formula);
        }
    }

    /**
     * Checks if port owns variables, used in formula
     * @param abstractFormula Formula containing variables
     * @return True if port fulfills needed variables.
     */
    private boolean ownsVars(FormulaOVar abstractFormula) {
        try {
            for (PlainToken token : abstractFormula.getTokens()) {
                if (token instanceof VarToken) getActualForToken((VarToken) token);
            }

            return true;
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    /**
     * Adds Port with type In.
     * @param port Initialized port.
     * @param innerId Inner Id of port, by which formulas on Out Ports can use data from it.
     * @throws IllegalArgumentException If variable name for port is used.
     */
    public void addPort(Port port, String innerId) throws IllegalArgumentException {
        if (innerIds.containsKey(innerId)) throw new IllegalArgumentException("Can't use variable name " + innerId + ".\nThe variable is already assigned.");

        this.ports.put(port.getId(), port);
        innerIds.put(innerId, port.getId());
    }

    public Port getPort(String portId) {
        return this.ports.get(portId);
    }

    /**
     * Gives information about block having Formulas with Variables.
     * @return True if block has only Constant including formulas
     * @see FormulaOVar
     * @see FormulaOConst
     */
    public boolean hasVarFormulas() {
        return !initial;
    }

    /**
     * Interchanges data in Variable Formula to actual data ( x => x's double value )
     * @param formulaOVar Formula to interchange the data
     * @see FormulaOVar
     * @see VarToken
     */
    private void interchangeData(FormulaOVar formulaOVar) {
        ArrayList<PlainToken> tokens = formulaOVar.getTokens();
        for (PlainToken token : tokens) {
            if (token instanceof VarToken) {
                ((VarToken) token).setActualData(getActualForToken((VarToken) token));
            }
        }
    }

    /**
     * Gets actual data for Variable Token
     * @param token Token to get the actual data from
     * @return Actual data of token.
     * @throws IllegalArgumentException If user tries to refer to non-existant In Port(aka Variable names)
     * @see VarToken
     */
    private Double getActualForToken(VarToken token) throws IllegalArgumentException {
        String[] split = token.getRaw().split("\\.");
        String inId = this.innerIds.get(split[0]);
        try {
            Port port = this.ports.get(inId);
            if (port != null) return port.getData().getVal(split[1]);
            else throw new IllegalArgumentException("There're no ports with variable name " + split[0] + "\n");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void startCalc() throws IllegalArgumentException {
        Set<String> keySet = formulas.keySet();

        System.out.println("Solving " + this.id);//DEBUG

        for (String key : keySet) {
            Set<String> varSet = formulas.get(key).keySet();
            Data data = DataTypeStorage.getInstance().getDataType(ports.get(key).getData().getName());
            for (String varName : varSet) {
                AbstractFormula abstractFormula = formulas.get(key).get(varName);
                if (abstractFormula instanceof FormulaOVar) {
                    FormulaOVar formulaOVar = (FormulaOVar) abstractFormula;
                    interchangeData(formulaOVar);
                    abstractFormula = formulaOVar.interchangeVars();
                }
                FormulaOConst formulaOConst = (FormulaOConst) abstractFormula;
                double result;
                try {
                    result = Solver.getInstance().calculate(formulaOConst.getTokens());
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                data.setVal(varName, result);
                System.out.println(varName + " " + result);
            }
            this.ports.get(key).setData(data);
        }
        this.calced = true;
        System.out.println(this.id + " solved");

    }


    public Set<String> portsReady() {
        Set<String> strings = this.ports.keySet();
        HashSet<String> missingPorts = new HashSet<>();
        for (String key : strings) {
            if (this.ports.get(key).getPortType() == 1 && !this.ports.get(key).gotData()) {
                missingPorts.add(key);
            }
        }
        return missingPorts;
    }

    /**
     * Gives info about block being instantly ready for calculations.
     * @return True if is ready.
     */
    public boolean isInitial() {
        return this.initial;
    }

    /**
     * Gives info about block being already calculated.
     * @return True if calculations were held on block.
     */
    public boolean isCalced() {
        return this.calced;
    }

    public void reset() {
        calced = false;
        Set<String> strings = ports.keySet();
        for (String key : strings) {
            ports.get(key).reset();
        }
    }

    /**
     * Gets port fulfilling give condition
     * @param condition Condition
     * @return Ports fulfilling condition.
     * @see Predicate
     */
    public HashSet<String> getPortsIf(Predicate<Byte> condition) {
        Set<String> strings = ports.keySet();
        HashSet<String> retSet = new HashSet<>();
        for (String key : strings) {
            if (condition.test(ports.get(key).getPortType())) retSet.add(key);
        }

        return retSet;
    }
}