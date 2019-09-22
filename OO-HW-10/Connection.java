import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

class Connection {
    private HashMap<HashMap<Integer,Integer>,Integer> connectDistance
            = new HashMap<>();
    private HashMap<Integer,HashSet<Integer>> adjacency = new HashMap<>();
    private HashSet<Integer> nodeSet = new HashSet<>();

    boolean isConnected(int src,int dst,HashMap<Integer,HashSet<Integer>> adj) {
        adjacency = adj;
        HashMap<Integer,Integer> srcanddst = new HashMap<>();
        srcanddst.put(src,dst);
        if (!connectDistance.containsKey(srcanddst)) {
            bfsFromSrcWithDistance(src);
        }
        return connectDistance.containsKey(srcanddst);
    }

    void bfsFromSrcWithDistance(int src) {
        HashMap<Integer,Integer> oneSrcDis = new HashMap<>();
        oneSrcDis.put(src,0);
        Queue<Integer> bfsQueue = new LinkedList<>();
        bfsQueue.offer(src);
        nodeSet.add(src);
        while (!bfsQueue.isEmpty()) {
            int popout = bfsQueue.poll();
            for (int adjnodes:adjacency.get(popout)) {
                if (!oneSrcDis.containsKey(adjnodes)) {
                    oneSrcDis.put(adjnodes,oneSrcDis.get(popout) + 1);
                    bfsQueue.offer(adjnodes);
                    nodeSet.add(adjnodes);
                }
            }
        }
        for (Integer key : oneSrcDis.keySet()) {
            HashMap<Integer,Integer> srcanddst = new HashMap<>();
            srcanddst.put(src,key);
            HashMap<Integer,Integer> dstandsrc = new HashMap<>();
            dstandsrc.put(key,src);
            connectDistance.put(srcanddst,oneSrcDis.get(key));
            connectDistance.put(dstandsrc,oneSrcDis.get(key));
        }
    }

    int getDistanceBetweenTwoNode(int src,int dst) {
        HashMap<Integer,Integer> fromdis = new HashMap<>();
        HashMap<Integer,Integer> todis = new HashMap<>();
        fromdis.put(src,dst);
        todis.put(dst,src);
        if (connectDistance.containsKey(fromdis)) {
            return connectDistance.get(fromdis);
        }
        else if (connectDistance.containsKey(todis)) {
            return connectDistance.get(todis);
        }
        else if (src == dst) {
            return 0;
        }
        else {
            bfsFromSrcWithDistance(src);
            return connectDistance.get(fromdis);
        }
    }

    HashSet<Integer> getNodeSet() {
        return nodeSet;
    }
}
