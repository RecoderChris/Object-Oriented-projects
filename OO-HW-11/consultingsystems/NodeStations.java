package consultingsystems;

import com.oocourse.specs3.models.Path;
import utils.NodeInPath;

import java.util.HashMap;
import java.util.HashSet;

public class NodeStations {
    private HashMap<Integer, HashSet<NodeInPath>> nodestations
            = new HashMap<>();

    public void add(Path path,int pid) {
        for (int node : path) {
            NodeInPath station = new NodeInPath(node, pid, path);
            if (!nodestations.containsKey(node)) {
                nodestations.put(node, new HashSet<>());
            }
            nodestations.get(node).add(station);
        }
    }

    public void remove(Path path,int pid) {
        for (int node : path) {
            NodeInPath station = new NodeInPath(node, pid,path);
            if (nodestations.containsKey(node)) {
                nodestations.get(node).remove(station);
                if (nodestations.get(node).size() == 0) {
                    nodestations.remove(node);
                }
            }
        }
    }

    HashMap<Integer, HashSet<NodeInPath>> getNodestations() {
        return nodestations;
    }
}
