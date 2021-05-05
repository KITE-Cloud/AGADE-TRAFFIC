package utility;

public class AgentIndexer {

    private static int index = 0;

    public static int getIndex() {
        return index++;
    }

    public static void increaseIndex() {
        AgentIndexer.index = AgentIndexer.index +1;
    }
}
