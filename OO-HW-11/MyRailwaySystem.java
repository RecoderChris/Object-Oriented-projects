import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;
import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import consultingsystems.AdjConsultSystem;
import consultingsystems.NodeConsultSystem;
import consultingsystems.NodeStations;
import consultingsystems.PathConsultSystem;
import consultingsystems.LeastUnpleasantConsultSystem;
import consultingsystems.WeightConsultSystem;
import com.oocourse.specs3.models.Path;

public class MyRailwaySystem implements RailwaySystem {
    private PathConsultSystem pcs = new PathConsultSystem();
    private NodeConsultSystem ncs = new NodeConsultSystem();
    private AdjConsultSystem acs = new AdjConsultSystem();
    private WeightConsultSystem[] wcs = new WeightConsultSystem[4];
    private NodeStations nds = new NodeStations();

    public MyRailwaySystem() {
        wcs[0] = new WeightConsultSystem(0, 1);
        wcs[1] = new WeightConsultSystem(2, 1);
        wcs[2] = new WeightConsultSystem(1, 0);
        wcs[3] = new LeastUnpleasantConsultSystem(32);
    }

    @Override
    public int size() {
        return pcs.size();
    }

    @Override
    public boolean containsPath(Path path) {
        return pcs.containsPath(path);
    }

    @Override
    public boolean containsPathId(int i) {
        return pcs.containsPathId(i);
    }

    @Override
    public Path getPathById(int i) throws PathIdNotFoundException {
        if (containsPathId(i)) {
            return pcs.getPathbyId(i);
        } else {
            throw new PathIdNotFoundException(i);
        }
    }

    @Override
    public int getPathId(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            return pcs.getPathId(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public int addPath(Path path) {
        if (path == null || !path.isValid()) {
            return 0;
        } else {
            if (containsPath(path)) {
                return pcs.getPathId(path);
            } else {
                int id = pcs.add(path);
                nds.add(path,id);
                ncs.add(path);
                acs.add(path);
                for (int i = 0; i < 4; i++) {
                    wcs[i].setNodeStations(nds);
                    wcs[i].add(path,pcs.getPathId(path));
                }
                return id;
            }
        }
    }

    @Override
    public int removePath(Path path) throws PathNotFoundException {
        if (path != null && path.isValid() && containsPath(path)) {
            int id = pcs.remove(path);
            nds.remove(path,id);
            ncs.delete(path);
            acs.remove(path);
            for (int i = 0; i < 4; i++) {
                wcs[i].setNodeStations(nds);
                wcs[i].remove(path,id);
            }
            return id;
        } else {
            throw new PathNotFoundException(path);
        }
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!containsPathId(i)) {
            throw new PathIdNotFoundException(i);
        } else {
            Path p = pcs.removePathById(i);
            nds.remove(p,i);
            ncs.delete(p);
            acs.remove(p);
            for (int index = 0; index < 4; index++) {
                wcs[index].setNodeStations(nds);
                wcs[index].remove(p,i);
            }
        }
    }

    @Override
    public int getDistinctNodeCount() {
        return ncs.getdistinctsize();
    }

    @Override
    public boolean containsNode(int i) {
        return ncs.containsnode(i);
    }

    @Override
    public boolean containsEdge(int i, int i1) {
        if (containsNode(i) && containsNode(i1)) {
            return acs.containsEdge(i, i1);
        } else {
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
        return pcs.isConnected(i, i1);
    }

    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        if (i == i1) {
            return 0;
        }
        return wcs[0].getDistance(i, i1);
    }

    @Override
    public int getLeastTicketPrice(int i,
                                   int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        if (i == i1) {
            return 0;
        }
        return wcs[1].getDistance(i, i1);
    }

    @Override
    public int getLeastTransferCount(int i,
                                     int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        if (i == i1) {
            return 0;
        }
        return wcs[2].getDistance(i, i1);
    }

    public int getUnpleasantValue(Path path, int i, int i1) {
        return 0;
    }

    @Override
    public int getLeastUnpleasantValue(int i,
                                       int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!containsNode(i)) {
            throw new NodeIdNotFoundException(i);
        }
        if (!containsNode(i1)) {
            throw new NodeIdNotFoundException(i1);
        }
        if (!isConnected(i, i1)) {
            throw new NodeNotConnectedException(i, i1);
        }
        if (i == i1) {
            return 0;
        }
        return wcs[3].getDistance(i, i1);
    }

    @Override
    public int getConnectedBlockCount() {
        return pcs.countConnection();
    }
}
