package graphstructure;

import java.util.ArrayList;
import java.util.HashSet;

class Node {
    private int assocount = 0;
    private String nodeid;
    private String classname;
    private Node top;
    private boolean isintface = false;
    private Node father = null;
    private HashSet<Node> fathers = new HashSet<>();
    private ArrayList<String> interfaces = new ArrayList<>();
    private HashSet<String> relationshipids = new HashSet<>();

    Node(String id) {
        nodeid = id;
        top = this;
    }

    Node(String id, String name) {
        nodeid = id;
        classname = name;
        top = this;
    }

    void constructFather(Node fatherNode) {
        if (father == null) {
            father = fatherNode;
            top = father.top;
        }
        fathers.add(fatherNode);
    }

    void addRelationships(Node relationend) {
        relationshipids.add(relationend.getNodeid());
        assocount++;
    }

    void addInterface(String interfaceId) {
        interfaces.add(interfaceId);
    }

    Node getFather() {
        return father;
    }

    HashSet<Node> getFathers() {
        return fathers;
    }

    String getClassname() {
        return classname;
    }

    String getNodeid() {
        return nodeid;
    }

    HashSet<String> getRelationships() {
        return relationshipids;
    }

    ArrayList<String> getInterfaces() {
        return interfaces;
    }

    void setName(String nodename) {
        classname = nodename;
    }

    void setIntflag() {
        isintface = true;
    }

    boolean isIntface() {
        return isintface;
    }

    int getAssocount() {
        return assocount;
    }
}
