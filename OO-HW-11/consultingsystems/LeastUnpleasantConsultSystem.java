package consultingsystems;

import utils.NodeInPath;

public class LeastUnpleasantConsultSystem extends WeightConsultSystem {

    public LeastUnpleasantConsultSystem(int transferweight) {
        super(transferweight);
    }

    @Override
    public void constructWeight(NodeInPath from, NodeInPath to) {
        int maximum;
        maximum = Math.max(from.getPath().getUnpleasantValue(from.getNode()),
                to.getPath().getUnpleasantValue(to.getNode()));
        super.constructOneEdge(from,to,maximum);
    }
}
