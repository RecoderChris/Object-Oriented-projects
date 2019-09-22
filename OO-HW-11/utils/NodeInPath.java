package utils;

import com.oocourse.specs3.models.Path;

public class NodeInPath {
    private Integer node;
    private Integer pathId;
    private Path path;

    public NodeInPath(int n, int pid,Path p) {
        node = n;
        pathId = pid;
        path = p;
    }

    public int getNode() {
        return node;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeInPath)) {
            return false;
        }
        NodeInPath np = (NodeInPath) obj;
        return (node.equals(np.node) && pathId.equals(np.pathId));
    }

    @Override
    public int hashCode() {
        return node;
    }

    @Override
    public String toString() {
        return node.toString();
    }
}
