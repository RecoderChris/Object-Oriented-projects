package consultingsystems;

import com.oocourse.specs3.models.Path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class PathConsultSystem {
    private HashMap<Integer, Path> idandpath = new HashMap<>();
    private HashMap<Path, Integer> pathandid = new HashMap<>();
    private HashMap<Integer, HashSet<Path>> nodeandpaths = new HashMap<>();
    private HashMap<Path, HashSet<Path>> pathadj = new HashMap<>();
    private HashMap<Path, Integer> pathandconnection = new HashMap<>();
    private int totalpath;
    private int totalconnection;
    private boolean modified;

    public PathConsultSystem() {
        modified = true;
        totalpath = 1;
        totalconnection = 1;
    }

    public int size() {
        return idandpath.size();
    }

    public boolean containsPath(Path p) {
        return idandpath.containsValue(p);
    }

    public boolean containsPathId(int i) {
        return idandpath.containsKey(i);
    }

    public Path getPathbyId(int i) {
        return idandpath.get(i);
    }

    public int getPathId(Path p) {
        return pathandid.get(p);
    }

    private void addMapBetweenNodeAndPath(Path p) {
        pathadj.put(p, new HashSet<>());
        pathadj.get(p).add(p);
        for (int node : p) {
            if (!nodeandpaths.containsKey(node)) {
                nodeandpaths.put(node, new HashSet<>());
            } else {
                for (Path p1 : nodeandpaths.get(node)) {
                    pathadj.get(p).add(p1);
                    pathadj.get(p1).add(p);
                }
            }
            nodeandpaths.get(node).add(p);
        }
    }

    public int add(Path p) {
        modified = true;
        int id = totalpath;
        totalpath++;
        idandpath.put(id, p);
        pathandid.put(p, id);
        addMapBetweenNodeAndPath(p);
        return id;
    }

    private void removeMapBetweenNodeAndPath(Path p) {
        pathadj.remove(p);
        for (int node : p) {
            nodeandpaths.get(node).remove(p);
            for (Path p1 : nodeandpaths.get(node)) {
                pathadj.get(p1).remove(p);
            }
        }
    }

    public int remove(Path p) {
        modified = true;
        int id = getPathId(p);
        idandpath.remove(id);
        pathandid.remove(p);
        removeMapBetweenNodeAndPath(p);
        return id;
    }

    public Path removePathById(int i) {
        modified = true;
        Path p = getPathbyId(i);
        idandpath.remove(i);
        pathandid.remove(p);
        removeMapBetweenNodeAndPath(p);
        return p;
    }

    private void clear() {
        modified = false;
        pathandconnection.clear();
        totalconnection = 1;
    }

    private void constructConnection() {
        clear();
        Queue<Path> bfsQueue = new LinkedList<>();
        HashSet<Path> pathset = new HashSet<>(pathadj.keySet());
        Path p = pathset.iterator().next();
        bfsQueue.offer(p);
        pathandconnection.put(p, totalconnection);
        while (pathset.size() != 0 || bfsQueue.size() != 0) {
            if (bfsQueue.size() == 0) {
                totalconnection++;
                Path path = pathset.iterator().next();
                bfsQueue.offer(path);
                pathandconnection.put(path, totalconnection);
            }
            Path poppath = bfsQueue.poll();
            pathset.remove(poppath);
            for (Path path : pathadj.get(poppath)) {
                if (!pathandconnection.containsKey(path)) {
                    pathandconnection.put(path, totalconnection);
                    bfsQueue.offer(path);
                }
            }
        }
    }

    public boolean isConnected(int fromid, int toid) {
        if (modified) {
            constructConnection();
        }
        return pathandconnection.get(nodeandpaths.get(fromid).iterator().next())
                .equals(pathandconnection.
                        get(nodeandpaths.get(toid).iterator().next()));
    }

    public int countConnection() {
        if (pathadj.size() == 0 ||
                pathadj.size() == 1) {
            return pathadj.size();
        } else if (modified) {
            constructConnection();
        }
        return totalconnection;
    }

}
