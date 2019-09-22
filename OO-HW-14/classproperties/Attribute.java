package classproperties;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Attribute {
    private HashMap<String, String> attrid2name = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> frequency
            = new HashMap<>();
    private HashMap<String, HashMap<String, Visibility>> classattrs
            = new HashMap<>();
    private HashMap<String, HashMap<Visibility, HashSet<String>>> visset
            = new HashMap<>();

    public void add(UmlAttribute attr) {
        String attrid = attr.getId();
        String classid = attr.getParentId();
        attrid2name.put(attrid, attr.getName());
        if (!classattrs.containsKey(classid)) {
            classattrs.put(classid, new HashMap<>());
            visset.put(classid, new HashMap<>());
            frequency.put(classid, new HashMap<>());
        }
        if (frequency.get(classid).containsKey(attrid)) {
            int fre = frequency.get(classid).get(attrid);
            frequency.get(classid).put(classid, ++fre);
        } else {
            frequency.get(classid).put(attrid, 1);
            classattrs.get(classid).put(attrid, attr.getVisibility());
            if (!visset.get(classid).containsKey(attr.getVisibility())) {
                visset.get(classid).put(attr.getVisibility(), new HashSet<>());
            }
            visset.get(classid).get(attr.getVisibility()).add(attrid);
        }
    }

    public Visibility consultVis(String classid, String attr,
                                 ArrayList<String> fatherids) {
        for (int i = fatherids.size() - 1; i >= 0; i--) {
            String levelid = fatherids.get(i);
            if (classattrs.containsKey(levelid)) {
                for (String attrid : classattrs.get(levelid).keySet()) {
                    String attrname = attrid2name.get(attrid);
                    if (attrname.equals(attr)) {
                        return classattrs.get(levelid).get(attrid);
                    }
                }
            }
        }
        if (classattrs.containsKey(classid)) {
            for (String attrid : classattrs.get(classid).keySet()) {
                String attrname = attrid2name.get(attrid);
                if (attrname.equals(attr)) {
                    return classattrs.get(classid).get(attrid);
                }
            }
        }
        return null;
    }

    public int countAttr(String classid) {
        if (classattrs.containsKey(classid)) {
            return classattrs.get(classid).size();
        }
        return 0;
    }

    public int countAllAttr(String classid, ArrayList<String> fatherids) {
        HashSet<String> oldset = new HashSet<>();
        if (fatherids != null) {
            for (int i = fatherids.size() - 1; i >= 0; i--) {
                String termid = fatherids.get(i);
                if (classattrs.containsKey(termid)) {
                    oldset.addAll(classattrs.get(termid).keySet());
                }
            }
        }
        if (classattrs.containsKey(classid)) {
            oldset.addAll(classattrs.get(classid).keySet());
        }
        return oldset.size();
    }

    public boolean foundAttr(
            String classid, String attrname, ArrayList<String> fatherids) {
        HashSet<String> nameset = new HashSet<>();
        for (int i = fatherids.size() - 1; i >= 0; i--) {
            String levelid = fatherids.get(i);
            if (classattrs.containsKey(levelid)) {
                for (String attr : classattrs.get(levelid).keySet()) {
                    String name = attrid2name.get(attr);
                    nameset.add(name);
                }
            }
        }
        if (classattrs.containsKey(classid)) {
            for (String attr : classattrs.get(classid).keySet()) {
                String name = attrid2name.get(attr);
                nameset.add(name);
            }
        }
        return nameset.contains(attrname);
    }

    public boolean isDuplicate(String classid, String attrname,
                               ArrayList<String> fatherids) {
        int number = 0;
        for (int i = fatherids.size() - 1; i >= 0; i--) {
            String levelid = fatherids.get(i);
            if (classattrs.containsKey(levelid)) {
                for (String attr : classattrs.get(levelid).keySet()) {
                    String name = attrid2name.get(attr);
                    if (name.equals(attrname)) {
                        number++;
                        if (number > 1) {
                            return true;
                        }
                    }
                }
            }
        }
        if (classattrs.containsKey(classid)) {
            for (String attr : classattrs.get(classid).keySet()) {
                String name = attrid2name.get(attr);
                if (name.equals(attrname)) {
                    number++;
                    if (number > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<AttributeClassInformation> getInformationNotHidden(
            ArrayList<String> fatherids, HashMap<String, String> id2name) {
        List<AttributeClassInformation> result = new ArrayList<>();
        for (int i = fatherids.size() - 1; i >= 0; i--) {
            String id = fatherids.get(i);
            if (visset.containsKey(id)) {
                if (visset.get(id).containsKey(Visibility.PUBLIC)) {
                    for (String s : visset.get(id).get(Visibility.PUBLIC)) {
                        result.add(new AttributeClassInformation(attrid2name.
                                get(s), id2name.get(id)));
                    }
                }
                if (visset.get(id).containsKey(Visibility.PROTECTED)) {
                    for (String s : visset.get(id).get(Visibility.PROTECTED)) {
                        result.add(new AttributeClassInformation(attrid2name.
                                get(s), id2name.get(id)));
                    }
                }
                if (visset.get(id).containsKey(Visibility.PACKAGE)) {
                    for (String s : visset.get(id).get(Visibility.PACKAGE)) {
                        result.add(new
                                AttributeClassInformation(attrid2name.
                                get(s), id2name.get(id)));
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<String> getAttr(String id) {
        ArrayList<String> result = new ArrayList<>();
        if (frequency.containsKey(id)) {
            for (String attr : frequency.get(id).keySet()) {
                for (int i = 0; i < frequency.get(id).get(attr); i++) {
                    result.add(attrid2name.get(attr));
                }
            }
        }
        return result;
    }
}
