import classproperties.Attribute;
import classproperties.Operation;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.Visibility;
import graphstructure.Graph;
import graphstructure.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Class {
    private Attribute attrs = new Attribute();
    private Operation ops = new Operation();
    private Graph graph = new Graph();

    private int classnum = 0;
    private HashMap<String, String> id2name = new HashMap<>();
    private HashMap<String, String> name2id = new HashMap<>();
    private HashMap<String, Integer> frequency = new HashMap<>();

    private HashMap<String, UmlOperation> opid2op = new HashMap<>();
    private HashMap<String, HashSet<String>> opreturnmap = new HashMap<>();
    private HashMap<String, HashSet<String>> opparammap = new HashMap<>();

    private HashMap<String, UmlClass> classes = new HashMap<>();
    private HashMap<String, UmlInterface> interfaces = new HashMap<>();

    void addClass(UmlElement cls) {
        boolean wheinterface = false;
        String classname = cls.getName();
        id2name.put(cls.getId(), classname);
        name2id.put(classname, cls.getId());
        if (cls.getElementType() == ElementType.UML_CLASS) {
            classnum++;
            if (frequency.containsKey(classname)) {
                int fre = frequency.get(classname);
                frequency.replace(classname, ++fre);
            } else {
                frequency.put(classname, 1);
            }
            classes.put(cls.getId(), ((UmlClass) cls));
        } else {
            wheinterface = true;
            interfaces.put(cls.getId(), ((UmlInterface) cls));
        }
        graph.addNode(cls.getId(), classname, wheinterface);
    }

    void addAttribute(UmlAttribute attribute) {
        attrs.add(attribute);
    }

    void addOperation(UmlOperation operation) {
        opid2op.put(operation.getId(), operation);
        ops.add(operation);
    }

    void addParameter(UmlParameter parameter) {
        UmlOperation operation = opid2op.get(parameter.getParentId());
        String classid = operation.getParentId();
        if (parameter.getDirection() == Direction.RETURN) {
            if (!opreturnmap.containsKey(classid)) {
                opreturnmap.put(classid, new HashSet<>());
            }
            opreturnmap.get(classid).add(operation.getId());
        } else {
            if (!opparammap.containsKey(classid)) {
                opparammap.put(classid, new HashSet<>());
            }
            opparammap.get(classid).add(operation.getId());
        }
    }

    void addGeneralization(UmlGeneralization generalization) {
        graph.addFatherandSon(generalization);
    }

    void addInterfaceRealization(
            UmlInterfaceRealization umlInterfaceRealization) {
        graph.addInterfaceRealization(umlInterfaceRealization);
    }

    void addAssociation(UmlAssociation association) {
        graph.addAssociation(association);
    }

    void addAssociationEnd(UmlAssociationEnd associationEnd) {
        graph.addAssociationEnd(associationEnd);
    }

    boolean foundClass(String classname) {
        return frequency.containsKey(classname);
    }

    boolean isDuplicated(String classname) {
        return frequency.get(classname) != 1;
    }

    int countClasses() {
        return classnum;
    }

    int getClassOperationCount(String classname,
                               OperationQueryType operationQueryType) {
        String classid = name2id.get(classname);
        switch (operationQueryType) {
            case ALL:
                return ops.countclassOp(classid);
            case PARAM:
                if (opparammap.containsKey(classid)) {
                    return opparammap.get(classid).size();
                }
                return 0;
            case NON_PARAM:
                if (opparammap.containsKey(classid)) {
                    return ops.
                            countclassOp(classid) -
                            opparammap.get(classid).size();
                }
                return ops.countclassOp(classid);
            case RETURN:
                if (opreturnmap.containsKey(classid)) {
                    return opreturnmap.get(classid).size();
                }
                return 0;
            default:
                if (opreturnmap.containsKey(classid)) {
                    return ops.
                            countclassOp(classid)
                            - opreturnmap.
                            get(classid).size();
                }
                return ops.countclassOp(classid);
        }
    }

    int getClassAttributeCount(
            String classname, AttributeQueryType attributeQueryType) {
        String classid = name2id.get(classname);
        if (attributeQueryType == AttributeQueryType.SELF_ONLY) {
            return attrs.countAttr(classid);
        } else {
            ArrayList<String> fathersnodeid
                    = graph.consultAllfather(classid);
            return attrs.countAllAttr(classid, fathersnodeid);
        }
    }

    int getClassAssociationCount(String classname) {
        return graph.countAssociations(name2id.get(classname));
    }

    List<String> getAssociation(String classname) {
        return graph.getClassAssociatedClassList(name2id.get(classname));
    }

    String getTopClass(String classname) {
        return graph.getTopClass(name2id.get(classname));
    }

    Map<Visibility, Integer> getClassOperationVisibility(
            String classname, String opname) {
        return ops.getClassOperationVisibility(name2id.get(classname), opname);
    }

    boolean foundAttr(String classname, String attr) {
        String nodeid = name2id.get(classname);
        ArrayList<String> fathersnodeid = graph.consultAllfather(nodeid);
        return attrs.foundAttr(nodeid, attr, fathersnodeid);
    }

    boolean isDuplicatedAttr(String classname, String attr) {
        String nodeid = name2id.get(classname);
        ArrayList<String> fathersnodeid = graph.consultAllfather(nodeid);
        return attrs.isDuplicate(name2id.get(classname), attr, fathersnodeid);
    }

    Visibility getClassAttributeVisibility(String classname, String opname) {
        String nodeid = name2id.get(classname);
        ArrayList<String> fathersnodeid = graph.consultAllfather(nodeid);
        return attrs.consultVis(name2id.get(classname), opname, fathersnodeid);
    }

    List<String> getImplementInterfaceList(String classname) {
        String classid = name2id.get(classname);
        List<String> interfaceids = graph.getImplementInterfaceList(classid);
        ArrayList<String> interfacename = new ArrayList<>();
        for (String id : interfaceids) {
            interfacename.add(id2name.get(id));
        }
        return new ArrayList<>(interfacename);
    }

    List<AttributeClassInformation> getInformationNotHidden(String classname) {
        String classid = name2id.get(classname);
        ArrayList<String> fathersnodeid = graph.consultAllfather(classid);
        fathersnodeid.add(classid);
        return attrs.getInformationNotHidden(fathersnodeid, id2name);
    }

    Set<AttributeClassInformation> getRepeatedInfo() {
        Set<AttributeClassInformation> result = new HashSet<>();
        for (String id : id2name.keySet()) {
            ArrayList<String> list = graph.getAssoList(id);
            list.addAll(attrs.getAttr(id));
            HashMap<String, Integer> repeatedSet = new HashMap<>();
            for (String s : list) {
                if (!repeatedSet.containsKey(s)) {
                    repeatedSet.put(s, 1);
                } else {
                    int num = repeatedSet.get(s) + 1;
                    repeatedSet.replace(s, num);
                }
            }
            for (String s : repeatedSet.keySet()) {
                if (repeatedSet.get(s) > 1) {
                    result.add(new AttributeClassInformation(s,
                            id2name.get(id)));
                }
            }
        }
        return result;
    }

    HashSet<UmlClassOrInterface> getCircleInfos() {
        HashSet<Node> nodes = graph.getCircularNodes();
        HashSet<UmlClassOrInterface> result = new HashSet<>();
        for (Node node : nodes) {
            String id = node.getNodeid();
            if (classes.containsKey(id)) {
                result.add(classes.get(id));
            } else if (interfaces.containsKey(id)) {
                result.add(interfaces.get(id));
            }
        }
        return result;
    }

    HashSet<UmlClassOrInterface> getGeneInfos() {
        HashSet<UmlClassOrInterface> result = new HashSet<>();
        HashSet<Node> nodeset = graph.getMoreThanOneAncestorNode();
        for (Node node : nodeset) {
            String id = node.getNodeid();
            if (classes.containsKey(id)) {
                result.add(classes.get(id));
            } else if (interfaces.containsKey(id)) {
                result.add(interfaces.get(id));
            }
        }
        return result;

    }
}
