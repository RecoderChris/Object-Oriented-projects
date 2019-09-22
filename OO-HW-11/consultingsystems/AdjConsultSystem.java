package consultingsystems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.oocourse.specs3.models.Path;

public class AdjConsultSystem {
    private HashMap<Integer, HashSet<Integer>> adjacencyMatrix
            = new HashMap<>();
    private HashMap<HashMap<Integer, Integer>, Integer> edgeFreq
            = new HashMap<>();

    private void adjAddNode(int fromnode, int tonode) {
        if (!adjacencyMatrix.containsKey(fromnode)) {
            adjacencyMatrix.put(fromnode, new HashSet<>());
        }
        if (!adjacencyMatrix.containsKey(tonode)) {
            adjacencyMatrix.put(tonode, new HashSet<>());
        }
    }

    private void constructAdjMatrix(int fromnode, int tonode) {
        adjAddNode(fromnode, tonode);
        HashMap<Integer, Integer> edgeFrom = new HashMap<>();
        HashMap<Integer, Integer> edgeTo = new HashMap<>();
        edgeFrom.put(fromnode, tonode);
        edgeTo.put(tonode, fromnode);
        if (!adjacencyMatrix.get(fromnode).contains(tonode)) {
            adjacencyMatrix.get(fromnode).add(tonode);
            adjacencyMatrix.get(tonode).add(fromnode);
            edgeFreq.put(edgeFrom, 1);
            edgeFreq.put(edgeTo, 1);
        } else {
            int fre = edgeFreq.get(edgeFrom);
            fre = fre + 1;
            edgeFreq.replace(edgeFrom, fre);
            edgeFreq.replace(edgeTo, fre);
        }
    }

    public void add(Path p) {
        Iterator<Integer> iter1 = p.iterator();
        Iterator<Integer> iter2 = p.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            int from = iter1.next();
            int to = iter2.next();
            constructAdjMatrix(from, to);
        }
    }

    private void removeEdge(int fromnode, int tonode) {
        HashMap<Integer, Integer> edgeFrom = new HashMap<>();
        HashMap<Integer, Integer> edgeTo = new HashMap<>();
        edgeFrom.put(fromnode, tonode);
        edgeTo.put(tonode, fromnode);
        int freq = edgeFreq.get(edgeFrom) - 1;
        if (freq == 0) {
            adjacencyMatrix.get(fromnode).remove(tonode);
            adjacencyMatrix.get(tonode).remove(fromnode);
            edgeFreq.remove(edgeFrom);
            edgeFreq.remove(edgeTo);
        } else {
            edgeFreq.replace(edgeFrom, freq);
            edgeFreq.replace(edgeTo, freq);
        }
    }

    public void remove(Path p) {
        Iterator<Integer> iter1 = p.iterator();
        Iterator<Integer> iter2 = p.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            int from = iter1.next();
            int to = iter2.next();
            removeEdge(from, to);
        }
    }

    public boolean containsEdge(int node1, int node2) {
        return adjacencyMatrix.get(node1).contains(node2);
    }
}
