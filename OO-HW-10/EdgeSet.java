import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import com.oocourse.specs2.models.Path;

class EdgeSet {
    private HashMap<Integer, HashSet<Integer>> adjacencyMatrix
            = new HashMap<>();
    private HashMap<HashMap<Integer,Integer>,Integer> edgeFreq
            = new HashMap<>();
    private HashMap<Integer,Connection> nodeConnection = new HashMap<>();
    private boolean modified = true;

    private void adjAddNode(int fromnode,int tonode) {
        if (!adjacencyMatrix.keySet().contains(fromnode)) {
            adjacencyMatrix.put(fromnode, new HashSet<>());
        }
        if (!adjacencyMatrix.keySet().contains(tonode)) {
            adjacencyMatrix.put(tonode, new HashSet<>());
        }
    }

    private void constructAdjMatrix(int fromnode,int tonode) {
        adjAddNode(fromnode,tonode);
        HashMap<Integer,Integer> edgeFrom = new HashMap<>();
        HashMap<Integer,Integer> edgeTo = new HashMap<>();
        edgeFrom.put(fromnode,tonode);
        edgeTo.put(tonode,fromnode);
        if (!adjacencyMatrix.get(fromnode).contains(tonode)) {
            adjacencyMatrix.get(fromnode).add(tonode);
            adjacencyMatrix.get(tonode).add(fromnode);
            edgeFreq.put(edgeFrom,1);
            edgeFreq.put(edgeTo,1);
        }
        else {
            int fre = edgeFreq.get(edgeFrom);
            fre = fre + 1;
            edgeFreq.replace(edgeFrom,fre);
            edgeFreq.replace(edgeTo,fre);
        }
    }

    void add(Path p) {
        modified = true;
        nodeConnection.clear();

        //nodeConnection.clear();
        Iterator<Integer> iter1 = p.iterator();
        Iterator<Integer> iter2 = p.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            constructAdjMatrix(iter1.next(),iter2.next());
        }

    }

    private void removeEdge(int fromnode,int tonode) {
        HashMap<Integer,Integer> edgeFrom = new HashMap<>();
        HashMap<Integer,Integer> edgeTo = new HashMap<>();
        edgeFrom.put(fromnode,tonode);
        edgeTo.put(tonode,fromnode);
        int freq = edgeFreq.get(edgeFrom) - 1;
        if (freq == 0) {
            adjacencyMatrix.get(fromnode).remove(tonode);
            adjacencyMatrix.get(tonode).remove(fromnode);
            edgeFreq.remove(edgeFrom);
            edgeFreq.remove(edgeTo);
        }
        else {
            edgeFreq.replace(edgeFrom,freq);
            edgeFreq.replace(edgeTo,freq);
        }
    }

    void remove(Path p) {
        modified = true;
        nodeConnection.clear();
        Iterator<Integer> iter1 = p.iterator();
        Iterator<Integer> iter2 = p.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            removeEdge(iter1.next(),iter2.next());
        }
    }

    boolean containsEdge(int node1,int node2) {
        return adjacencyMatrix.get(node1).contains(node2);
    }

    private boolean constructConnectionFromOnePoint(int n1,int n2) {
        Connection connection = new Connection();
        boolean result = connection.isConnected(n1,n2,adjacencyMatrix);
        for (Integer node : connection.getNodeSet()) {
            nodeConnection.put(node,connection);
        }
        return result;
    }

    private boolean constructConnection(int src,int dst) {
        boolean result;
        result = constructConnectionFromOnePoint(src,dst);
        return result;
    }

    boolean pairConnected(int src,int dst) {
        if (modified || !nodeConnection.containsKey(src) ||
                !nodeConnection.containsKey(dst)) {
            modified = false;
            return constructConnection(src,dst);
        }
        else {
            return nodeConnection.get(src).isConnected(src,dst,adjacencyMatrix);
        }
    }

    int getDistanceBetweenTwoNodes(int src,int dst) {
        if (modified) {
            constructConnection(src,dst);
            Connection connection = nodeConnection.get(src);
            return connection.getDistanceBetweenTwoNode(src,dst);
        }
        else {
            return nodeConnection.get(src).getDistanceBetweenTwoNode(src,dst);
        }
    }
}
