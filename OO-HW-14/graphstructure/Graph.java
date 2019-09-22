package graphstructure;

import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import java.util.Queue;
import java.util.Stack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private HashMap<String, Node> id2node = new HashMap<>();
    private HashSet<Node> circularNodes = new HashSet<>();

    private HashMap<String, Association> id2association = new HashMap<>();
    private HashMap<String, String> assoid2name = new HashMap<>();

    private void addNode(String nodeId) {
        id2node.put(nodeId, new Node(nodeId));
    }

    public void addNode(String nodeId, String classname, boolean isInterface) {
        if (!id2node.containsKey(nodeId)) {
            id2node.put(nodeId, new Node(nodeId, classname));
        } else {
            id2node.get(nodeId).setClassname(classname);
        }
        if (isInterface) {
            id2node.get(nodeId).setIntflag();
        }
    }

    public void addFatherandSon(UmlGeneralization generalization) {
        String target = generalization.getTarget();
        String source = generalization.getSource();
        if (!id2node.containsKey(target)) {
            addNode(target);
        }
        if (!id2node.containsKey(source)) {
            addNode(source);
        }
        Node sonnode = id2node.get(source);
        sonnode.addFatherNode(id2node.get(target));
    }

    public void addInterfaceRealization(
            UmlInterfaceRealization interfaceRealization) {
        String target = interfaceRealization.getTarget();
        String source = interfaceRealization.getSource();
        if (!id2node.containsKey(target)) {
            addNode(target);
        }
        if (!id2node.containsKey(source)) {
            addNode(source);
        }
        Node realization = id2node.get(source);
        Node intface = id2node.get(target);
        realization.addInterface(intface);
    }

    public void addAssociation(UmlAssociation association) {
        Association as = new Association();
        id2association.put(association.getId(), as);
    }

    public void addAssociationEnd(UmlAssociationEnd associationEnd) {
        Association as = id2association.get(associationEnd.getParentId());
        if (!id2node.containsKey(associationEnd.getReference())) {
            addNode(associationEnd.getReference());
        }
        as.getEnd(id2node.get(associationEnd.getReference()));
        if (!assoid2name.containsKey(associationEnd.getParentId())) {
            assoid2name.put(associationEnd.getParentId(),
                    associationEnd.getName());
        } else {
            Node node1 = id2node.get(associationEnd.getReference());
            node1.addAssoname(assoid2name.get(associationEnd.getParentId()));
            as.addAssoname(associationEnd.getName());
        }
    }

    private void dfs(Node theNode, HashSet<Node> nonvisited, Stack<Node> path) {
        if (nonvisited.contains(theNode)) {
            nonvisited.remove(theNode);
            path.push(theNode);
            for (Node adjNode : theNode.getFathers()) {
                dfs(adjNode, nonvisited, path);
            }
            path.pop();
        } else {
            int loc = path.indexOf(theNode);
            if (loc != -1) {
                do {
                    circularNodes.add(path.elementAt(loc++));
                } while (loc < path.size());
            }
        }
    }

    public HashSet<Node> getCircularNodes() {
        HashSet<Node> nodes = new HashSet<>(id2node.values());
        Stack<Node> path = new Stack<>();
        for (Node node : nodes) {
            HashSet<Node> newnode = new HashSet<>(id2node.values());
            if (!circularNodes.contains(node)) {
                dfs(node, newnode, path);
            }
        }
        return circularNodes;
    }

    private void topologicalSort() {
        Queue<Node> queue = new LinkedList<>();
        for (Node vertex : id2node.values()) {
            if (vertex.getFathers().size() == 0) {
                queue.add(vertex);
            }
        }
        while (!queue.isEmpty()) {
            Node v = queue.poll();
            for (Node u : v.getSons()) {
                u.removeFather(v);
                if (u.getInDegree() == 0) {
                    queue.offer(u);
                }
            }
        }
    }

    public HashSet<Node> getMoreThanOneAncestorNode() {
        topologicalSort();
        HashSet<Node> result = new HashSet<>();
        for (Node node : id2node.values()) {
            if (node.hasMoreThanOneAncestor()) {
                result.add(node);
            }
        }
        for (Node node : id2node.values()) {
            if (node.multiRealization()) {
                result.add(node);
            }
            for (Node interface1 : node.getInterfaces()) {
                if (result.contains(interface1)) {
                    result.add(node);
                    break;
                }
            }
            int sum1 = 0;
            HashSet<Node> temp1 = new HashSet<>();
            for (Node node1 : node.getAncestors()) {
                for (Node interface1 : node1.getInterfaces()) {
                    temp1.addAll(interface1.getAncestors());
                    temp1.add(interface1);
                    sum1 = sum1 + interface1.getAncestors().size() + 1;
                }
            }
            for (Node interface2 : node.getInterfaces()) {
                sum1 = sum1 + interface2.getAncestors().size() + 1;
                temp1.addAll(interface2.getAncestors());
                temp1.add(interface2);
            }
            if (temp1.size() != sum1) {
                result.add(node);
            }
        }
        for (Node node : id2node.values()) {
            for (Node node1 : node.getAncestors()) {
                if (result.contains(node1)) {
                    result.add(node);
                }
            }
        }
        return result;
    }

    public ArrayList<String> consultAllfather(String nodeid) {
        ArrayList<String> fathers = new ArrayList<>();
        Node node = id2node.get(nodeid);
        for (Node father : node.getAncestors()) {
            fathers.add(father.getNodeid());
        }
        return fathers;
    }

    public String getTopClass(String nodeid) {
        return id2node.get(id2node.get(nodeid).getTop()).getClassname();
    }

    public List<String> getImplementInterfaceList(String nodeid) {
        Node node = id2node.get(nodeid);
        HashSet<Node> fathers = new HashSet<>(node.getAncestors());
        HashSet<Node> interfaces = new HashSet<>();
        for (Node father : fathers) {
            for (Node interface1 : father.getInterfaces()) {
                interfaces.add(interface1);
                interfaces.addAll(interface1.getAncestors());
            }
        }
        for (Node interface1 : node.getInterfaces()) {
            interfaces.add(interface1);
            interfaces.addAll(interface1.getAncestors());
        }
        HashSet<String> result = new HashSet<>();
        for (Node node1 : interfaces) {
            result.add(node1.getNodeid());
        }
        return new ArrayList<>(result);
    }

    public int countAssociations(String nodeid) {
        ArrayList<Node> assos = new ArrayList<>();
        ArrayList<String> fathers = consultAllfather(nodeid);
        for (String s : fathers) {
            Node node = id2node.get(s);
            assos.addAll(node.getRelationships());
        }
        Node node = id2node.get(nodeid);
        assos.addAll(node.getRelationships());
        return assos.size();
    }

    public List<String> getClassAssociatedClassList(String nodeid) {
        HashSet<Node> assos = new HashSet<>();
        ArrayList<String> fathers = consultAllfather(nodeid);
        for (String s : fathers) {
            Node node = id2node.get(s);
            assos.addAll(node.getRelationships());
        }
        Node node = id2node.get(nodeid);
        assos.addAll(node.getRelationships());
        ArrayList<String> result = new ArrayList<>();
        for (Node node1 : assos) {
            if (!node1.isIntface()) {
                result.add(node1.getClassname());
            }
        }
        return result;
    }

    public ArrayList<String> getAssoList(String nodeid) {
        Node node = id2node.get(nodeid);
        return node.getAssolist();
    }

}
