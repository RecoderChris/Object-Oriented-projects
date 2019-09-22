package classproperties;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Attribute {
    private HashMap<String, String> attrid2name = new HashMap<>();
    private HashMap<String, String> name2attrid = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> frequency
            = new HashMap<>();
    private HashMap<String, HashMap<String, Visibility>> classattrs
            = new HashMap<>();
    private HashMap<String, HashMap<Visibility, HashSet<String>>> visset
            = new HashMap<>();

    private HashMap<String, HashSet<String>> allclassattrs = new HashMap<>();
    private HashMap<String, HashMap<String, Integer>> allfrequency
            = new HashMap<>();
    private HashMap<String, HashMap<String, Visibility>> allvis
            = new HashMap<>();

    public void add(UmlAttribute attr) {
        String attrid = attr.getId();
        String classId = attr.getParentId();
        attrid2name.put(attrid, attr.getName());
        name2attrid.put(attr.getName(), attrid);
        if (!classattrs.containsKey(classId)) {
            classattrs.put(classId, new HashMap<>());
            visset.put(classId, new HashMap<>());
            frequency.put(classId, new HashMap<>());
        }
        if (frequency.get(classId).containsKey(attrid)) {
            int fre = frequency.get(classId).get(attrid);
            frequency.get(classId).put(classId, ++fre);
        } else {
            frequency.get(classId).put(attrid, 1);
            classattrs.get(classId).put(attrid, attr.getVisibility());
            if (!visset.get(classId).containsKey(attr.getVisibility())) {
                visset.get(classId).put(attr.getVisibility(), new HashSet<>());
            }
            visset.get(classId).get(attr.getVisibility()).add(attrid);
        }
    }

    public Visibility consultVis(String classid, String attr) {
        return allvis.get(classid).get(attr);
    }

    public boolean containsAllAttr(String classid) {
        return allclassattrs.containsKey(classid);
    }

    public int countAttr(String classid) {
        if (classattrs.containsKey(classid)) {
            return classattrs.get(classid).size();
        }
        return 0;
    }

    public int countAllAttr(String classid, ArrayList<String> fatherids) {
        if (!containsAllAttr(classid)) {
            HashSet<String> oldset = new HashSet<>();
            for (int i = fatherids.size() - 1; i >= 0; i--) {
                String termid = fatherids.get(i);
                if (classattrs.containsKey(termid)) {
                    oldset.addAll(classattrs.get(termid).keySet());
                }
                HashSet<String> newset = new HashSet<>(oldset);
                allclassattrs.put(termid, newset);
            }
        }
        return allclassattrs.get(classid).size();
    }

    public boolean foundAttr(
            String classid, String attrname, ArrayList<String> fatherids) {
        if (allfrequency.containsKey(classid) && allfrequency.
                get(classid).
                containsKey(name2attrid.get(attrname))) {
            return true;
        }
        HashMap<String, Visibility> newvismap = new HashMap<>();
        HashMap<String, Integer> newfremap = new HashMap<>();
        for (int i = fatherids.size() - 1; i >= 0; i--) {
            HashMap<String, Visibility> itervis = new HashMap<>(newvismap);
            HashMap<String, Integer> iterfre = new HashMap<>(newfremap);
            String levelid = fatherids.get(i);
            allfrequency.put(levelid, iterfre);
            allvis.put(levelid, itervis);
            if (classattrs.containsKey(levelid)) {
                for (String attr : classattrs.get(levelid).keySet()) {
                    String name = attrid2name.get(attr);
                    if (!allfrequency.get(levelid).containsKey(name)) {
                        allfrequency.get(levelid).put(name, 1);
                        allvis.get(levelid).
                                put(name, classattrs.get(levelid).get(attr));
                    } else {
                        int fre = allfrequency.get(levelid).get(name);
                        allfrequency.get(levelid).replace(name, ++fre);
                    }
                }
            }
            newfremap.putAll(allfrequency.get(levelid));
            newvismap.putAll(allvis.get(levelid));
        }
        return allfrequency.get(classid).containsKey(attrname);
    }

    public boolean isDuplicate(String classid, String attrname) {
        return allfrequency.get(classid).get(attrname) > 1;
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
}
