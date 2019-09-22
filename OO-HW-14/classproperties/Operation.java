package classproperties;

import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlOperation;

import java.util.HashMap;
import java.util.Map;

public class Operation {
    private HashMap<String, Integer> nodeid2opnum = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> opmap = new HashMap<>();
    private HashMap<Integer, HashMap<Visibility, Integer>> visStatics
            = new HashMap<>();
    private int mapnum = 0;

    private int constructMap(String classid, String opname) {
        if (opmap.containsKey(classid)) {
            int num = nodeid2opnum.get(classid);
            nodeid2opnum.put(classid, ++num);
            if (!opmap.get(classid).containsKey(opname)) {
                mapnum++;
                opmap.get(classid).put(opname, mapnum);
                return mapnum;
            } else {
                return opmap.get(classid).get(opname);
            }
        } else {
            mapnum++;
            nodeid2opnum.put(classid, 1);
            HashMap<String, Integer> newhash = new HashMap<>();
            newhash.put(opname, mapnum);
            opmap.put(classid, newhash);
            return mapnum;
        }
    }

    public void add(UmlOperation operation) {
        String classid = operation.getParentId();
        int pairnum = constructMap(classid, operation.getName());
        if (visStatics.containsKey(pairnum)) {
            int fre = visStatics.get(pairnum).get(operation.getVisibility());
            visStatics.get(pairnum).replace(operation.getVisibility(), ++fre);
        } else {
            HashMap<Visibility, Integer> newhash = new HashMap<>();
            newhash.put(Visibility.PUBLIC, 0);
            newhash.put(Visibility.PROTECTED, 0);
            newhash.put(Visibility.PRIVATE, 0);
            newhash.put(Visibility.PACKAGE, 0);
            newhash.put(operation.getVisibility(), 1);
            visStatics.put(pairnum, newhash);
        }
    }

    public int countclassOp(String classid) {
        if (nodeid2opnum.containsKey(classid)) {
            return nodeid2opnum.get(classid);
        }
        return 0;
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String classid, String opname) {
        if (!opmap.containsKey(classid)) {
            return new HashMap<>();
        }
        if (!opmap.get(classid).containsKey(opname)) {
            return new HashMap<>();
        }
        return new HashMap<>(visStatics.get(opmap.get(classid).get(opname)));
    }

}
