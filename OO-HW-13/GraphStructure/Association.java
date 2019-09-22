package graphstructure;

class Association {
    private Node end1 = null;

    void getEnd(Node endnode) {
        if (end1 == null) {
            end1 = endnode;
        } else {
            end1.addRelationships(endnode);
            endnode.addRelationships(end1);
        }
    }

}
