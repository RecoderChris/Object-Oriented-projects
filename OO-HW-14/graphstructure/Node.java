package graphstructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Node {
    private boolean multifather = false;
    private HashMap<Node, Integer> fatherfre = new HashMap<>();
    private String nodeid;
    private String classname;
    private boolean isintface = false;
    private int indegree = 0;
    private HashSet<Node> sons = new HashSet<>();
    private HashSet<Node> fathers = new HashSet<>();
    private ArrayList<String> assolist = new ArrayList<>();
    private ArrayList<Node> relationshipids = new ArrayList<>();
    private HashMap<String, Integer> interfaceid2fre = new HashMap<>();
    private HashSet<Node> interfaces = new HashSet<>();
    private String topid;
    private HashSet<Node> ancestors = new HashSet<>();
    private int totalGeneNum = 0;

    Node(String id) {
        nodeid = id;
        topid = id;
    }

    Node(String id, String name) {
        nodeid = id;
        classname = name;
        topid = id;
    }

    void setIntflag() {
        isintface = true;
    }

    void setClassname(String name) {
        classname = name;
    }

    void addFatherNode(Node fatherNode) {
        indegree++;
        fathers.add(fatherNode);
        fatherNode.addChildNode(this);
        if (!fatherfre.containsKey(fatherNode)) {
            fatherfre.put(fatherNode, 1);
        } else {
            int newnum = fatherfre.get(fatherNode) + 1;
            fatherfre.replace(fatherNode, newnum);
            multifather = true;
        }
    }

    private void addChildNode(Node sonnode) {
        sons.add(sonnode);
    }

    void addInterface(Node interfaceNode) {
        if (!interfaceid2fre.containsKey(interfaceNode.getNodeid())) {
            interfaceid2fre.put(interfaceNode.getNodeid(), 1);
        } else {
            int newnum = interfaceid2fre.get(interfaceNode.getNodeid()) + 1;
            interfaceid2fre.replace(interfaceNode.getNodeid(), newnum);
        }
        interfaces.add(interfaceNode);
    }

    boolean multiRealization() {
        for (int value : interfaceid2fre.values()) {
            if (value > 1) {
                return true;
            }
        }
        return false;
    }

    void addRelationships(Node relationend) {
        relationshipids.add(relationend);
    }

    void addAssoname(String assoname) {
        if (assoname != null) {
            assolist.add(assoname);
        }
    }

    boolean isIntface() {
        return isintface;
    }

    String getClassname() {
        return classname;
    }

    public String getNodeid() {
        return nodeid;
    }

    String getTop() {
        return topid;
    }

    HashSet<Node> getFathers() {
        return fathers;
    }

    HashSet<Node> getSons() {
        return sons;
    }

    private int getTotalGeneNum() {
        return totalGeneNum;
    }

    HashSet<Node> getAncestors() {
        return ancestors;
    }

    void removeFather(Node father) {
        indegree -= 1;
        topid = father.getTop();
        totalGeneNum += (father.getTotalGeneNum() + 1);
        this.ancestors.addAll(father.getAncestors());
        this.ancestors.add(father);
    }

    HashSet<Node> getInterfaces() {
        return interfaces;
    }

    int getInDegree() {
        return indegree;
    }

    private boolean isMultifather() {
        return multifather;
    }

    boolean hasMoreThanOneAncestor() {
        if (isMultifather()) {
            return true;
        }
        return totalGeneNum > ancestors.size();
    }

    ArrayList<Node> getRelationships() {
        return relationshipids;
    }

    ArrayList<String> getAssolist() {
        return assolist;
    }
}
