package backend;

import backend.blockpackage.BlockController;
import backend.blockpackage.Connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for detecting cycles in programm.
 */
class CycleDetector implements Serializable {
    final private BlockController blockController;
    private final ArrayList<String> visited = new ArrayList<>();

    CycleDetector(BlockController blockController) {
        this.blockController = blockController;
    }

    /**
     * Detects cycles.
     *
     * @return String where cycle is located.
     */
    public String cyclePresent() {
        visited.clear();

        ArrayList<String> initials = blockController.getInitials();
        return compute(initials);
    }

    private String compute(ArrayList<String> initials) {

        for (String inId : initials) {
            visited.clear();
            String procResult = procedure(inId);

            if (procResult != null) return procResult;
        }

        return null;
    }

    /**
     * Gets blocks connected to block.
     * @param parent Block id.
     * @return Array of blocks Ids.
     */
    private ArrayList<String> getChildren(String parent) {
        HashSet<Connection> allConnectionsPossessing = this.blockController.getAllConnectionsPossessing(parent);
        ArrayList<String> retArray = new ArrayList<>();
        for (Connection connection : allConnectionsPossessing)
            retArray.add(Connection.parseBlockId(connection.getInPortId()));

        return retArray;
    }

    /**
     * DFS with backtracking.
     * @param start Starting node.
     * @return Block related to cycle or null if no cycles are present.
     */
    private String procedure(String start) {
        if (visited.contains(start)) return start;

        visited.add(start);

        for (String node : this.getChildren(start)) {
            if (procedure(node) != null) return node;
        }

        visited.remove(visited.size() - 1);
        return null;
    }

}
