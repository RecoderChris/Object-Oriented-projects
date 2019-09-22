import com.oocourse.specs2.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {

    private ArrayList<Integer> nodes = new ArrayList<>();
    private HashSet<Integer> nodeset = new HashSet<>();

    public MyPath(int... nodeList) {
        for (Integer i : nodeList) {
            nodes.add(i);
            nodeset.add(i);
        }
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public int getNode(int i) {
        if (i >= 0 && i < size()) {
            return nodes.get(i);
        }
        return 0;
    }

    @Override
    public boolean containsNode(int node) {
        return nodeset.contains(node);
    }

    @Override
    public int getDistinctNodeCount() {
        return nodeset.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        else {
            if (nodes.size() != ((Path)obj).size()) {
                return false;
            }
            Iterator<Integer> iterator1 = this.iterator();
            Iterator<Integer> iterator2 = ((Path) obj).iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                if (!iterator1.next().equals(iterator2.next())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isValid() {
        return (size() >= 2);
    }

    @Override
    public int compareTo(Path o) {
        Iterator<Integer> iterator1 = this.iterator();
        Iterator<Integer> iterator2 = o.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            int iter1 = iterator1.next();
            int iter2 = iterator2.next();
            if (iter1 > iter2) {
                return 1;
            }
            else if (iter1 < iter2) {
                return -1;
            }
        }
        if (this.size() > o.size()) {
            return 1;
        }
        else if (this.size() < o.size()) {
            return -1;
        }
        return 0;
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    @Override
    public int hashCode() {
        return nodes.hashCode();
    }
}
