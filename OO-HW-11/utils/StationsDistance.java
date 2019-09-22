package utils;

public class StationsDistance implements Comparable<StationsDistance> {
    private NodeInPath nodes;
    private int distance;

    public StationsDistance(NodeInPath nodepair, int dist) {
        nodes = nodepair;
        distance = dist;
    }

    @Override
    public int compareTo(StationsDistance st) {
        if (distance > st.distance) {
            return 1;
        } else if (distance < st.distance) {
            return -1;
        }
        return 0;
    }

    public NodeInPath getNodes() {
        return nodes;
    }
}
