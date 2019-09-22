import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;

class TimeSequence {
    private HashMap<String, Integer> id2obj = new HashMap<>();
    private HashMap<String, Integer> id2massage = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> id2obj2incoming
            = new HashMap<>();
    private HashMap<String, HashMap<String, String>> id2name2obj
            = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> id2class2fre
            = new HashMap<>();
    private HashMap<String, Integer> name2fre = new HashMap<>();
    private HashMap<String, String> name2id = new HashMap<>();

    private void processInteraction(UmlElement element) {
        if (!name2fre.containsKey(element.getName())) {
            name2fre.put(element.getName(), 1);
        } else {
            int newnum = name2fre.get(element.getName()) + 1;
            name2fre.replace(element.getName(), newnum);
        }
        name2id.put(element.getName(), element.getId());
    }

    private void processLifeline(UmlElement element) {
        if (!id2class2fre.containsKey(element.getParentId())) {
            HashMap<String, Integer> newhash = new HashMap<>();
            newhash.put(element.getName(), 1);
            id2class2fre.put(element.getParentId(), newhash);
        } else if (!id2class2fre.get(element.getParentId()).
                containsKey(element.getName())) {
            id2class2fre.get(element.getParentId()).
                    put(element.getName(), 1);
        } else {
            int newnum = id2class2fre.get(element.getParentId()).
                    get(element.getName()) + 1;
            id2class2fre.get(element.getParentId()).
                    replace(element.getName(), newnum);
        }
        if (!id2obj.containsKey(element.getParentId())) {
            id2obj.put(element.getParentId(), 1);
        } else {
            int newnum = id2obj.get(element.getParentId()) + 1;
            id2obj.replace(element.getParentId(), newnum);
        }
        if (!id2name2obj.containsKey(element.getParentId())) {
            HashMap<String, String> newhash = new HashMap<>();
            newhash.put(element.getName(), element.getId());
            id2name2obj.put(element.getParentId(), newhash);
        } else {
            id2name2obj.get(element.getParentId()).
                    put(element.getName(), element.getId());
        }
    }

    private void processUmlmessage(UmlElement element) {
        if (!id2massage.containsKey(element.getParentId())) {
            id2massage.put(element.getParentId(), 1);
        } else {
            int newnum = id2massage.get(element.getParentId()) + 1;
            id2massage.replace(element.getParentId(), newnum);
        }
        UmlMessage message = (UmlMessage) element;
        if (!id2obj2incoming.containsKey(element.getParentId())) {
            HashMap<String, Integer> newhash = new HashMap<>();
            newhash.put(message.getTarget(), 1);
            id2obj2incoming.put(message.getParentId(), newhash);
        } else if (!id2obj2incoming.get(element.getParentId()).
                containsKey(((UmlMessage) element).getTarget())) {
            id2obj2incoming.get(element.getParentId()).
                    put(((UmlMessage) element).getTarget(), 1);
        } else {
            int newnum = id2obj2incoming.
                    get(element.getParentId()).
                    get(((UmlMessage) element).getTarget()) + 1;
            id2obj2incoming.get(element.getParentId()).
                    replace(((UmlMessage) element).getTarget(), newnum);
        }
    }

    void add(UmlElement element) {
        switch (element.getElementType()) {
            case UML_INTERACTION:
                processInteraction(element);
                break;
            case UML_LIFELINE:
                processLifeline(element);
                break;
            case UML_MESSAGE:
                processUmlmessage(element);
                break;
            default:
                break;
        }
    }

    int getParticipantCount(String s) {
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        } else {
            String id = name2id.get(s);
            return id2obj.get(id);
        }
    }

    int getMessageCount(String s) {
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        } else {
            String id = name2id.get(s);
            return id2massage.get(id);
        }
    }

    int getIncomingMessageCount(String s, String s1) {
        if (!name2id.containsKey(s)) {
            return -1;
        } else if (name2fre.get(s) != 1) {
            return -2;
        } else if (!id2name2obj.get(name2id.get(s)).containsKey(s1)) {
            return -3;
        } else if (id2class2fre.get(name2id.get(s)).get(s1) != 1) {
            return -4;
        }
        String id = id2name2obj.get(name2id.get(s)).get(s1);
        if (!id2obj2incoming.get(name2id.get(s)).containsKey(id)) {
            return 0;
        }
        return id2obj2incoming.get(name2id.get(s)).get(id);
    }

    boolean judgeWhetherTs(UmlAttribute attr) {
        String parentID = attr.getParentId();
        return name2id.containsValue(parentID);
    }
}
