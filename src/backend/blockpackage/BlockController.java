package backend.blockpackage;

import backend.idGenerator;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for block communication between themselves and outer packages ( Facade Design Pattern ).
 */

public class BlockController implements Serializable {

    private final HashMap<String, Block> blocks = new HashMap<>();

    //InPortId
    private final HashMap<String, Connection> connections = new HashMap<>();

    public BlockController() {
    }

    public String addBlock() {
        String id = idGenerator.getInstance().generateBlockId();
        blocks.put(id, new Block(id));
        return id;
    }

    public Pair<String, String> connect(String fPortId, String sPortId) {

        try {
            Connection connection = new Connection(getPortById(fPortId), getPortById(sPortId));
            this.connections.put(connection.getInPortId(), connection);
            return new Pair<>(connection.getInPortId(), connection.getOutPortId());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private Port getPortById(String portId) {
        int colPos = portId.indexOf(':');
        String blockId = String.valueOf(Integer.parseInt(portId.substring(0, colPos)));
        return this.blocks.get(blockId).getPort(portId);
    }

    public Block getBlock(String id) {
        return this.blocks.get(id);
    }

    public void delBlock(String id) {
        deleteBlockCons(id);
        this.blocks.remove(id);
    }

    /**
     * Deletes all connections related to block
     * @param id Block id.
     */
    private void deleteBlockCons(String id) {
        this.connections.entrySet().removeIf(entry -> this.blocks.get(id).getPort(entry.getValue().getInPortId()) != null || this.blocks.get(id).getPort(entry.getValue().getOutPortId()) != null);
    }

    /**
     * Get's all the blocks, which are ready to be calced.
     * @return ArrayList of Block Ids.
     */
    public ArrayList<String> getReadys() {
        ArrayList<String> retArray = new ArrayList<>();
        Set<String> strings = blocks.keySet();
        for (String string : strings) {
            if (isBlockReady(blocks.get(string))) retArray.add(string);
        }
        return retArray;
    }

    /**
     * Makes the calculations over ready blocks
     * @param readys Blocks ready to be calculated.
     */
    public void calc(ArrayList<String> readys) {
        for (String key : readys) {
            this.blocks.get(key).startCalc();
        }
    }

    /**
     * Passes the data from Out Port to In Port of next Port.
     * @param readys Ports ready to pass their's data.
     */
    public void passData(ArrayList<String> readys) {
        for (String key : readys) {
            HashSet<Connection> connections = getAllConnectionsPossessing(key);
            for (Connection connection : connections)
                connection.pass();
        }
    }

    /**
     * Get's all connection related to block.
     * @param blockId Id of Block we want to get related info.
     * @return Set of Connections related to block.
     */
    public HashSet<Connection> getAllConnectionsPossessing(String blockId) {
        Set<String> outIds = this.blocks.get(blockId).getPortsIf(aByte -> aByte == 0);
        HashSet<Connection> connections = new HashSet<>();
        Set<String> strings = this.connections.keySet();
        for (String id : outIds) {
            for (String conId : strings) {
                if (this.connections.get(conId).getOutPortId().equals(id)) {
                    connections.add(this.connections.get(conId));
                }
            }
        }
        return connections;
    }

    public String getConnectionInfo(String inPort) {
        return this.connections.get(inPort).getConStatus();
    }

    /**
     * Indicates if block is ready for calculations.
     * @param block The block.
     * @return True if it's ready.
     */
    private boolean isBlockReady(Block block) {
        return (!block.isCalced()) && (block.isInitial() || acceptedMissing(block, block.portsReady()));
    }

    /**
     * Checks if missing data on In Ports of Block is fine and can be completed manually.
     * @param block Block where missing data was detected.
     * @param strings Ids of ports missing data.
     * @return True if it's fine and data are filled.
     */
    private boolean acceptedMissing(Block block, Set<String> strings) {
        if (strings.size() == 0) return true;
        HashSet<String> manualInfoEntry = new HashSet<>();
        for (String key : strings) {
            if (this.connections.containsKey(key)) return false;

            manualInfoEntry.add(key);
        }
        for (String key : manualInfoEntry)
            block.getPort(key).manuallyFill();
        return true;
    }

    /**
     * Get's all block indicated Initial.
     * @return Array of blocks, which are initial.
     * @see Block
     */
    public ArrayList<String> getInitials() {
        Set<String> strings = blocks.keySet();
        ArrayList<String> stringArrayList = new ArrayList<>();

        for (String key : strings) {
            if (blocks.get(key).isInitial()) stringArrayList.add(key);
        }
        return stringArrayList;
    }

    public void reset() {
        Set<String> strings = blocks.keySet();
        for (String key : strings)
            blocks.get(key).reset();
    }

    public HashSet<Block> getBlocks() {
        return new HashSet<>(this.blocks.values());
    }

    public Collection<Connection> getConnections() {
        return this.connections.values();
    }

    public void clear() {
        this.blocks.clear();
        this.connections.clear();
    }
}
