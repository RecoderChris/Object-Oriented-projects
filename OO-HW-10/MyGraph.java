import com.oocourse.specs2.models.Path;
import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.PathNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;

import java.util.HashMap;

public class MyGraph implements Graph {
    private HashMap<Integer, Path> idandpath = new HashMap<>();
    private HashMap<Path,Integer> pathandid = new HashMap<>();
    private NodeSet nodeSet = new NodeSet();
    private EdgeSet edgeset = new EdgeSet();
    private int totalpath;

    public MyGraph() {
        totalpath = 1;
    }

    @Override
    public int size() {
        return idandpath.size();
    }

    @Override
    public boolean containsPath(Path path) {
        return idandpath.containsValue(path);
    }

    @Override
    public boolean containsPathId(int i) {
        return idandpath.containsKey(i);
    }

    @Override
    public Path getPathById(int i) throws Exception {
        if (containsPathId(i)) {
            return idandpath.get(i);
        }
        else {
            throw new PathIdNotFoundException(i);
        }
    }

    @Override
    public int getPathId(Path path) throws Exception {
        if  (path != null && path.isValid() && containsPath(path)) {
            return pathandid.get(path);
        }
        else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public int addPath(Path path) throws Exception {
        if (path == null || !path.isValid()) {
            return 0;
        }
        else {
            if (containsPath(path)) {
                return getPathId(path);
            }
            else {
                int id = totalpath;
                totalpath++;
                idandpath.put(id,path);
                pathandid.put(path,id);
                nodeSet.add(path);
                edgeset.add(path);
                return id;
            }
        }
    }

    @Override
    public int removePath(Path path) throws Exception {
        if (path != null && path.isValid() && containsPath(path)) {
            int id = getPathId(path);
            idandpath.remove(id);
            pathandid.remove(path);
            nodeSet.delete(path);
            edgeset.remove(path);
            return id;
        }
        else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        }
        else {
            Path p = idandpath.remove(i);
            pathandid.remove(p);
            nodeSet.delete(p);
            edgeset.remove(p);
        }
    }

    @Override
    public int getDistinctNodeCount() {
        return nodeSet.getdistinctsize();
    }

    @Override
    public boolean containsNode(int i) {
        return nodeSet.containsnode(i);
    }

    @Override
    public boolean containsEdge(int i, int i1) {
        if (containsNode(i) && containsNode(i1)) {
            return edgeset.containsEdge(i, i1);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isConnected(int i, int i1) throws NodeIdNotFoundException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (i == i1) {
            return true;
        }
        return edgeset.pairConnected(i,i1);
    }

    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw  new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw  new NodeIdNotFoundException(i1);
        }
        if (!isConnected(i,i1)) {
            throw  new NodeNotConnectedException(i,i1);
        }
        if (i == i1) {
            return 0;
        }
        else {
            return edgeset.getDistanceBetweenTwoNodes(i, i1);
        }
    }
}
