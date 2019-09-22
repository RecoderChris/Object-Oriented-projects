import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

class StateMachine {
    private HashMap<String, Integer> statemachine2specialpoint
            = new HashMap<>();
    private HashMap<String, Integer> region2transnum = new HashMap<>();
    private HashMap<String, String> region2statemachine = new HashMap<>();
    private HashMap<String, String> statemachine2region = new HashMap<>();
    private HashMap<String, Integer> name2fre = new HashMap<>();
    private HashMap<String, String> name2id = new HashMap<>();
    private HashMap<String, Integer> id2num = new HashMap<>();
    private HashMap<String, HashMap<String, String>> statename2id
            = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> statename2fre
            = new HashMap<>();
    private HashMap<String, HashSet<String>> id2neibor = new HashMap<>();

    private void processUmlstateMachine(UmlElement e) {
        if (!name2fre.containsKey(e.getName())) {
            name2fre.put(e.getName(), 1);
        } else {
            int newnum = name2fre.get(e.getName()) + 1;
            name2fre.replace(e.getName(), newnum);
        }
        if (!name2id.containsKey(e.getName())) {
            name2id.put(e.getName(), e.getId());
        }
    }

    private void processUmlregion(UmlElement e) {
        if (!statemachine2region.containsKey(e.getParentId())) {
            statemachine2region.put(e.getParentId(), e.getId());
        }
        if (!region2statemachine.containsKey(e.getId())) {
            region2statemachine.put(e.getId(), e.getParentId());
        }
        statemachine2specialpoint.put(e.getParentId(), 0);
    }

    private void processPsedostate(UmlElement e) {
        String id2 = region2statemachine.get(e.getParentId());
        if (!statemachine2specialpoint.containsKey(id2)) {
            statemachine2specialpoint.put(id2, 0);
        }
        int newnum2 = statemachine2specialpoint.get(id2) + 1;
        statemachine2specialpoint.put(id2, newnum2);
    }

    private void processFinalState(UmlElement e) {
        String id1 = region2statemachine.get(e.getParentId());
        if (!statemachine2specialpoint.containsKey(id1)) {
            statemachine2specialpoint.put(id1, 0);
        }
        int newnum1 = statemachine2specialpoint.get(id1) + 1;
        statemachine2specialpoint.put(id1, newnum1);
    }

    private void processUmlState(UmlElement e) {
        String regionid = e.getParentId();
        String id = region2statemachine.get(regionid);
        if (!id2num.containsKey(id)) {
            id2num.put(id, 1);
        } else {
            int newnum = id2num.get(id) + 1;
            id2num.replace(id, newnum);
        }
        if (!statename2id.containsKey(id)) {
            HashMap<String, String> newhash = new HashMap<>();
            newhash.put(e.getName(), e.getId());
            statename2id.put(id, newhash);
        } else if (!statename2id.get(id).containsKey(e.getName())) {
            statename2id.get(id).put(e.getName(), e.getId());
        }
        if (!statename2fre.containsKey(id)) {
            HashMap<String, Integer> newhash = new HashMap<>();
            newhash.put(e.getName(), 0);
            statename2fre.put(id, newhash);
        } else if (!statename2fre.get(id).containsKey(e.getName())) {
            statename2fre.get(id).put(e.getName(), 0);
        }
        int num = statename2fre.get(id).get(e.getName()) + 1;
        statename2fre.get(id).replace(e.getName(), num);
    }

    private void processTransition(UmlElement e) {
        UmlTransition trans = (UmlTransition) e;
        if (!region2transnum.containsKey(trans.getParentId())) {
            region2transnum.put(trans.getParentId(), 1);
        } else {
            int newnum = region2transnum.get(e.getParentId()) + 1;
            region2transnum.replace(trans.getParentId(), newnum);
        }
        if (!id2neibor.containsKey(trans.getSource())) {
            id2neibor.put(trans.getSource(), new HashSet<>());
        }
        id2neibor.get(trans.getSource()).add(trans.getTarget());
    }

    void add(UmlElement e) {
        switch (e.getElementType()) {
            case UML_STATE_MACHINE:
                processUmlstateMachine(e);
                break;
            case UML_REGION:
                processUmlregion(e);
                break;
            case UML_PSEUDOSTATE:
                processPsedostate(e);
                break;
            case UML_FINAL_STATE:
                processFinalState(e);
                break;
            case UML_STATE:
                processUmlState(e);
                break;
            case UML_TRANSITION:
                processTransition(e);
                break;
            default:
                break;
        }
    }

    int getStateCount(String s) {
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        }
        if (statemachine2specialpoint.containsKey(name2id.get(s))) {
            if (!id2num.containsKey(name2id.get(s))) {
                return statemachine2specialpoint.get(name2id.get(s));
            }
            return statemachine2specialpoint.
                    get(name2id.get(s)) +
                    id2num.get(name2id.get(s));
        } else {
            if (!id2num.containsKey(name2id.get(s))) {
                return 0;
            }
            return id2num.get(name2id.get(s));
        }
    }

    int getTransitionCount(String s) {
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        }
        String machineid = name2id.get(s);
        String regionid = statemachine2region.get(machineid);
        if (!region2transnum.containsKey(regionid)) {
            return 0;
        }
        return region2transnum.get(regionid);
    }

    int getSubsequentStateCount(String s, String s1) {
        String statemachineid = name2id.get(s);
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        } else if (!statename2id.containsKey(statemachineid)) {
            return -3;
        } else if (!statename2id.get(statemachineid).containsKey(s1)) {
            return -3;
        } else if (statename2fre.get(statemachineid).get(s1) != 1) {
            return -4;
        }
        String stateid = statename2id.get(statemachineid).get(s1);
        HashSet<String> visited = new HashSet<>();
        Queue<String> bfsQueue = new LinkedList<>();
        bfsQueue.offer(stateid);
        boolean first = true;
        while (!bfsQueue.isEmpty()) {
            String id = bfsQueue.poll();
            if (first) {
                first = false;
            } else {
                visited.add(id);
            }
            if (id2neibor.containsKey(id)) {
                for (String neighbors : id2neibor.get(id)) {
                    if (!visited.contains(neighbors)) {
                        bfsQueue.offer(neighbors);
                    }
                }
            }
        }
        return visited.size();
    }
}
