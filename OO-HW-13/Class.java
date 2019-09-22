import classproperties.Attribute;
import classproperties.Operation;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAssociation;
import graphstructure.Graph;
import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Class {
    private Attribute attrs = new Attribute();
    private Operation ops = new Operation();
    private Graph graph = new Graph();

    private HashMap<String, String> id2name = new HashMap<>();
    private HashMap<String, String> name2id = new HashMap<>();
    private int classnum = 0;
    private HashMap<String, Integer> frequency = new HashMap<>();
    private HashMap<String, UmlOperation> opid2op = new HashMap<>();
    private HashMap<String, HashSet<String>> opreturnmap = new HashMap<>();
    private HashMap<String, HashSet<String>> opparammap = new HashMap<>();

    void addClass(UmlElement cls) {
        boolean isInterface = false;
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
        } else {
            isInterface = true;
        }
        graph.addNode(cls.getId(), classname, isInterface);
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
        String classId = operation.getParentId();
        if (parameter.getDirection() == Direction.RETURN) {
            if (!opreturnmap.containsKey(classId)) {
                opreturnmap.put(classId, new HashSet<>());
            }
            opreturnmap.get(classId).add(operation.getId());
        } else {
            if (!opparammap.containsKey(classId)) {
                opparammap.put(classId, new HashSet<>());
            }
            opparammap.get(classId).add(operation.getId());
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
            String classname,AttributeQueryType attributeQueryType) {
        String classid = name2id.get(classname);
        if (attributeQueryType == AttributeQueryType.SELF_ONLY) {
            return attrs.countAttr(classid);
        } else {
            if (attrs.containsAllAttr(classid)) {
                return attrs.countAllAttr(classid, null);
            } else {
                ArrayList<String> fathersnodeid
                        = graph.consultAllfather(classid);
                return attrs.countAllAttr(classid, fathersnodeid);
            }
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
        return attrs.isDuplicate(name2id.get(classname), attr);
    }

    Visibility getClassAttributeVisibility(String classname, String opname) {
        return attrs.consultVis(name2id.get(classname), opname);
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
        return attrs.getInformationNotHidden(fathersnodeid, id2name);
    }
}
