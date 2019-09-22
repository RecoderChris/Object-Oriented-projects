package consultingsystems;

import com.oocourse.specs3.models.Path;
import utils.NodeInPath;
import utils.StationsDistance;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Map;

public class WeightConsultSystem {
    private HashMap<NodeInPath, HashMap<NodeInPath,Integer>> weightmatrix
            = new HashMap<>();
    private HashMap<NodeInPath, HashSet<NodeInPath>> adjmatrix
            = new HashMap<>();
    private HashMap<HashMap<Integer, Integer>, Integer> dismatrix
            = new HashMap<>();
    private NodeStations nodeStations;
    private int transweight;
    private int weight;

    WeightConsultSystem(int transferweight) {
        transweight = transferweight;
    }

    public WeightConsultSystem(int transferweight, int normalweight) {
        transweight = transferweight;
        weight = normalweight;
    }

    private void rebuild() {
        dismatrix.clear();
    }

    void constructOneEdge(NodeInPath station1,
                                  NodeInPath station2, int weight) {
        if (station1.equals(station2)) {
            return;
        }
        adjmatrix.get(station1).add(station2);
        adjmatrix.get(station2).add(station1);
        if (!weightmatrix.containsKey(station1)) {
            weightmatrix.put(station1,new HashMap<>());
        }
        if (!weightmatrix.containsKey(station2)) {
            weightmatrix.put(station2,new HashMap<>());
        }
        weightmatrix.get(station1).put(station2,weight);
        weightmatrix.get(station2).put(station1,weight);
    }

    private void constructTrans(int node, int pid,Path path) {
        for (NodeInPath station : nodeStations.getNodestations().get(node)) {
            NodeInPath station1 = new NodeInPath(node, pid,path);
            if (!station.equals(station1)) {
                constructOneEdge(station, station1, transweight);
            }
        }
    }

    public void constructWeight(NodeInPath from, NodeInPath to) {
        constructOneEdge(from, to, weight);
    }

    private void constructAdjMatrix(NodeInPath from, NodeInPath to) {
        if (!adjmatrix.containsKey(from)) {
            adjmatrix.put(from, new HashSet<>());
        }
        if (!adjmatrix.containsKey(to)) {
            adjmatrix.put(to, new HashSet<>());
        }
        adjmatrix.get(from).add(to);
        adjmatrix.get(to).add(from);
    }

    public void add(Path path,int pid) {
        rebuild();
        Iterator<Integer> iter1 = path.iterator();
        Iterator<Integer> iter2 = path.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            int from = iter1.next();
            int to = iter2.next();
            NodeInPath fromstation = new NodeInPath(from, pid,path);
            NodeInPath tostation = new NodeInPath(to, pid,path);
            constructAdjMatrix(fromstation, tostation);
            constructWeight(fromstation, tostation);
            constructTrans(from, pid,path);
        }
        constructTrans(iter1.next(), pid,path);
    }

    private void removeEdge(NodeInPath station1, NodeInPath station2) {
        if (adjmatrix.containsKey(station1)) {
            adjmatrix.get(station1).remove(station2);
        }
        if (adjmatrix.containsKey(station2)) {
            adjmatrix.get(station2).remove(station1);
        }
        if (weightmatrix.containsKey(station1) &&
                weightmatrix.get(station1).containsKey(station2)) {
            weightmatrix.get(station1).remove(station2);
            if (weightmatrix.get(station1).isEmpty()) {
                weightmatrix.remove(station1);
            }
        }
        if (weightmatrix.containsKey(station2) &&
                weightmatrix.get(station2).containsKey(station1)) {
            weightmatrix.get(station2).remove(station1);
            if (weightmatrix.get(station2).isEmpty()) {
                weightmatrix.remove(station2);
            }
        }
    }

    private void removeTrans(int node, int pid, Path path) {
        NodeInPath station = new NodeInPath(node, pid, path);
        if (!nodeStations.getNodestations().containsKey(node)) {
            return;
        }
        for (NodeInPath station1 : nodeStations.getNodestations().get(node)) {
            removeEdge(station, station1);
        }
    }

    private void removeAdj(NodeInPath from, NodeInPath to) {
        if (adjmatrix.containsKey(from)) {
            adjmatrix.get(from).remove(to);
            if (adjmatrix.get(from).size() == 0) {
                adjmatrix.remove(from);
            }
        }
        if (adjmatrix.containsKey(to)) {
            adjmatrix.get(to).remove(from);
            if (adjmatrix.get(to).size() == 0) {
                adjmatrix.remove(to);
            }
        }
    }

    public void remove(Path path,int pid) {
        rebuild();
        Iterator<Integer> iter1 = path.iterator();
        Iterator<Integer> iter2 = path.iterator();
        iter2.next();
        while (iter2.hasNext()) {
            int from = iter1.next();
            int to = iter2.next();
            NodeInPath fromstation = new NodeInPath(from, pid, path);
            NodeInPath tostation = new NodeInPath(to, pid, path);
            removeAdj(fromstation, tostation);
            removeEdge(fromstation, tostation);
            removeTrans(from, pid, path);
        }
        removeTrans(iter1.next(), pid,path);
    }

    private void valueSamePoint(int src, int value) {
        HashSet<NodeInPath> stations = nodeStations.getNodestations().get(src);
        for (NodeInPath station1 : stations) {
            for (NodeInPath station2 : stations) {
                if (!weightmatrix.containsKey(station1)) {
                    weightmatrix.put(station1,new HashMap<>());
                }
                weightmatrix.get(station1).put(station2,value);
            }
        }
    }

    private void dijstraCalMin(NodeInPath src) {
        PriorityQueue<StationsDistance> pq = new PriorityQueue<>();
        HashSet<NodeInPath> visited = new HashSet<>();
        HashMap<NodeInPath, Integer> dist = new HashMap<>();
        pq.add(new StationsDistance(src, 0));
        dist.put(src, 0);
        while (!pq.isEmpty()) {
            NodeInPath curStation = pq.poll().getNodes();
            if (!visited.contains(curStation)) {
                visited.add(curStation);
                int curDistance = dist.get(curStation);
                for (NodeInPath adjStation : adjmatrix.get(curStation)) {
                    if (adjStation.equals(curStation)) {
                        continue;
                    }
                    int weight = weightmatrix.get(curStation).get(adjStation);
                    int newDistance = curDistance + weight;
                    if (!dist.containsKey(adjStation) ||
                            newDistance < dist.get(adjStation)) {
                        dist.put(adjStation, newDistance);
                        pq.add(new StationsDistance(adjStation, newDistance));
                    }
                }
            }
        }
        for (Map.Entry<NodeInPath, Integer> entry : dist.entrySet()) {
            putDistance(src.getNode(), entry.getKey().getNode(),
                    entry.getValue());
            putDistance(entry.getKey().getNode(), src.getNode(),
                    entry.getValue());
        }
    }

    private void putDistance(int src, int des, int distance) {
        HashMap<Integer, Integer> intpair = new HashMap<>();
        intpair.put(src, des);
        if (dismatrix.containsKey(intpair)) {
            if (dismatrix.get(intpair) > distance) {
                dismatrix.put(intpair, distance);
            }
        } else {
            dismatrix.put(intpair, distance);
        }
    }

    private void calculete(int src) {
        if (nodeStations.getNodestations().get(src).size() > 1) {
            valueSamePoint(src, 0);
        }
        NodeInPath startstation
                = nodeStations.getNodestations().get(src).iterator().next();
        dijstraCalMin(startstation);
        valueSamePoint(src, transweight);
    }

    public int getDistance(int src, int des) {
        HashMap<Integer, Integer> fromandto = new HashMap<>();
        fromandto.put(src, des);
        if (!dismatrix.containsKey(fromandto)) {
            calculete(src);
        }
        return dismatrix.get(fromandto);
    }

    public void setNodeStations(NodeStations nds) {
        nodeStations = nds;
    }
}
