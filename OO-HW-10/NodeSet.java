import com.oocourse.specs2.models.Path;

import java.util.HashMap;

class NodeSet {
    private HashMap<Integer,Integer> nodeandfre = new HashMap<>();

    NodeSet() {

    }

    void add(Path path) {
        for (int node : path) {
            addnode(node);
        }
    }

    private void addnode(int node) {
        if (nodeandfre.containsKey(node)) {
            int nodefre = nodeandfre.get(node);
            nodefre++;
            nodeandfre.replace(node,nodefre);
        }
        else {
            nodeandfre.put(node,1);
        }
    }

    void delete(Path path) {
        for (int node : path) {
            deletenode(node);
        }
    }

    private void deletenode(int node) {
        if (nodeandfre.containsKey(node)) {
            int nodefre = nodeandfre.get(node);
            nodefre--;
            if (nodefre == 0) {
                nodeandfre.remove(node);
            }
            else {
                nodeandfre.replace(node,nodefre);
            }
        }
    }

    int getdistinctsize() {
        return nodeandfre.size();
    }

    boolean containsnode(int node) {
        return nodeandfre.keySet().contains(node);
    }
}
