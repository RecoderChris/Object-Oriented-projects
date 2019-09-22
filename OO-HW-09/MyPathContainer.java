import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    //@ public instance model non_null Path[] pList;
    //@ public instance model non_null int[] pidList;
    private HashMap<Integer,MyPath> plist = new HashMap<>();
    private int nodeId;

    public MyPathContainer() {
        nodeId = 1;
    }

    //@ ensures \result == pList.length;
    @Override
    public /*@pure@*/int size() {
        return plist.size();
    }

    /*@ requires path != null;
      @ assignable \nothing;
      @ ensures \result == (\exists int i; 0 <= i && i < pList.length;
      @                     pList[i].equals(path));
      @*/
    @Override
    public boolean containsPath(Path path) {
        if (path != null) {
            for (MyPath p : plist.values()) {
                if (p.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*@ ensures \result == (\exists int i; 0 <= i && i < pidList.length;
      @                      pidList[i] == pathId);
      @*/
    @Override
    public boolean containsPathId(int i) {
        for (int j : plist.keySet()) {
            if (i == j) {
                return true;
            }
        }
        return false;
    }

    @Override
    public /*@pure@*/Path getPathById(int i) throws Exception {
        if (containsPathId(i)) {
            return plist.get(i);
        }
        else {
            throw new PathIdNotFoundException(i);
        }
    }

    @Override
    public /*@pure@*/ int getPathId(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            for (int i : plist.keySet()) {
                if (plist.get(i).equals(path)) {
                    return i;
                }
            }
        }
        throw new PathNotFoundException(path);
    }

    @Override
    public int addPath(Path path) throws Exception {
        if (path == null || !path.isValid()) {
            return 0;
        }
        else {
            int index = nodeId;
            if (!containsPath(path)) {
                nodeId++;
                plist.put(index, (MyPath) path);
            }
            else {
                index = getPathId(path);
            }
            return index;
        }
    }

    @Override
    public int removePath(Path path) throws Exception {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        }
        else {
            int index = 0;
            for (int i : plist.keySet()) {
                if (plist.get(i).equals(path)) {
                    index = i;
                }
            }
            plist.remove(index);
            return index;
        }
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (containsPathId(i)) {
            plist.remove(i);
        }
        else {
            throw new PathIdNotFoundException(i);
        }
    }

    @Override
    public int getDistinctNodeCount() {
        ArrayList<Integer> nodeslist = new ArrayList<>();
        for (MyPath path : plist.values()) {
            for (Integer i : path.getDistinctNodeSet()) {
                if (!nodeslist.contains(i)) {
                    nodeslist.add(i);
                }
            }
        }
        return nodeslist.size();
    }
}
