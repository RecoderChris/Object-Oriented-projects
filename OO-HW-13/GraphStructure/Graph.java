package graphstructure;

import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {
    private HashMap<String, Node> id2node = new HashMap<>();
    private HashMap<String, Association> id2association = new HashMap<>();
    private HashMap<String, HashSet<String>> allnodeid2associations
            = new HashMap<>();
    private HashMap<String, Integer> node2countasso = new HashMap<>();

    private void addNode(String nodeId) {
        id2node.put(nodeId, new Node(nodeId));
    }

    public void addNode(String nodeId, String classname, boolean isInterface) {
        if (id2node.containsKey(nodeId)) {
            id2node.get(nodeId).setName(classname);
        } else {
            id2node.put(nodeId, new Node(nodeId, classname));
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
        sonnode.constructFather(id2node.get(target));
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
        realization.addInterface(target);
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
    }

    public ArrayList<String> consultAllfather(String nodeid) {
        ArrayList<String> fathers = new ArrayList<>();
        Node consultnode = id2node.get(nodeid);
        Node fathernode = consultnode.getFather();
        fathers.add(consultnode.getNodeid());
        while (fathernode != null) {
            fathers.add(fathernode.getNodeid());
            consultnode = fathernode;
            fathernode = consultnode.getFather();
        }
        return fathers;
    }

    public int countAssociations(String nodeid) {
        if (!node2countasso.containsKey(nodeid)) {
            ArrayList<String> fathers = consultAllfather(nodeid);
            int asso = 0;
            for (int i = fathers.size() - 1; i >= 0; i--) {
                asso = asso + id2node.get(fathers.get(i)).getAssocount();
                node2countasso.put(fathers.get(i), asso);
            }
        }
        return node2countasso.get(nodeid);

    }

    public List<String> getClassAssociatedClassList(String nodeid) {
        if (!allnodeid2associations.containsKey(nodeid)) {
            ArrayList<String> fathers = consultAllfather(nodeid);
            HashSet<String> associations = new HashSet<>();
            for (int i = fathers.size() - 1; i >= 0; i--) {
                associations.
                        addAll(id2node.
                                get(fathers.get(i)).getRelationships());
                HashSet<String> result = new HashSet<>(associations);
                allnodeid2associations.put(fathers.get(i), result);
            }
        }
        ArrayList<String> result = new ArrayList<>();
        for (String id : allnodeid2associations.get(nodeid)) {
            if (!id2node.get(id).isIntface()) {
                result.add(id2node.get(id).getClassname());
            }
        }
        return result;
    }

    public String getTopClass(String nodeid) {
        Node present = id2node.get(nodeid);
        Node father = present.getFather();
        while (father != null) {
            present = father;
            father = present.getFather();
        }
        return present.getClassname();
    }

    public List<String> getImplementInterfaceList(String nodeid) {
        Node node = id2node.get(nodeid);
        Node father = node.getFather();
        HashSet<String> interfaces = new HashSet<>(node.getInterfaces());
        while (father != null) {
            interfaces.addAll(father.getInterfaces());
            node = father;
            father = node.getFather();
        }
        HashSet<String> temp = new HashSet<>(interfaces);
        for (String interfaceid : temp) {
            Node present = id2node.get(interfaceid);
            interfaces.addAll(reverseInterface(present));
        }
        return new ArrayList<>(interfaces);
    }

    private HashSet<String> reverseInterface(Node present) {
        HashSet<String> newset = new HashSet<>();
        newset.add(present.getNodeid());
        if (present.getFathers().size() == 0) {
            return newset;
        } else {
            for (Node node : present.getFathers()) {
                newset.addAll(reverseInterface(node));
            }
            return newset;
        }
    }
}
