package consultingsystems;

import com.oocourse.specs3.models.Path;

import java.util.HashMap;

public class NodeConsultSystem {
    private HashMap<Integer, Integer> nodeandfre = new HashMap<>();

    public NodeConsultSystem() {

    }

    public void add(Path path) {
        for (int node : path) {
            addnode(node);
        }
    }

    private void addnode(int node) {
        if (nodeandfre.containsKey(node)) {
            int nodefre = nodeandfre.get(node);
            nodefre++;
            nodeandfre.replace(node, nodefre);
        } else {
            nodeandfre.put(node, 1);
        }
    }

    public void delete(Path path) {
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
            } else {
                nodeandfre.replace(node, nodefre);
            }
        }
    }

    public int getdistinctsize() {
        return nodeandfre.size();
    }

    public boolean containsnode(int node) {
        return nodeandfre.keySet().contains(node);
    }
}
