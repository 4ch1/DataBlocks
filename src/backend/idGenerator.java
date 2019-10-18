package backend;


import java.io.Serializable;

/**
 * @author Ivan Manoilov(xmanoi00)
 * @author Farrukh Abdukhalikov(xabduk00)
 * <p>
 * Class responsible for generating id for backend objects.
 */
public class idGenerator implements Serializable {
    private static final idGenerator ourInstance = new idGenerator();

    private int baseBlockIds = 0;
    private int basePortIds = 0;

    private idGenerator() {
    }

    public static idGenerator getInstance() {
        return ourInstance;
    }

    public String generatePortId(String blockId) {
        basePortIds++;
        return blockId + ":" + String.valueOf(basePortIds - 1);
    }

    public String generateBlockId() {
        baseBlockIds++;
        return String.valueOf(baseBlockIds - 1);
    }

    /**
     * Merges bases with another idGenerator.(Example use: after loading a new scheme with
     * different bases, to avoid collision of ids)
     *
     * @param idGenerator Another idGenerator.
     */
    public void merge(idGenerator idGenerator) {
        this.baseBlockIds = idGenerator.baseBlockIds;
        this.basePortIds = idGenerator.basePortIds;
    }
}
