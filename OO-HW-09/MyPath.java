import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.Iterator;

public class MyPath implements Path {
    // person checked
    // Iterable<Integer>和Comparable<Path>接口的规格请参阅JDK
    //@ public instance model non_null int[] nodes;
    private ArrayList<Integer> nodes = new ArrayList<>();

    public MyPath(int... nodeList) {
        for (Integer i:nodeList) {
            nodes.add(i);
        }
    }

    //@ ensures \result == nodes.length;
    @Override
    public /*@pure@*/int size() {
        if (nodes == null) {
            return 0;
        }
        else {
            return nodes.size();
        }
    }

    /*@ requires index >= 0 && index < size();
      @ assignable \nothing;
      @ ensures \result == nodes[index];
      @*/
    @Override
    public /*@pure@*/ int getNode(int index) {
        if ((index >= 0) && (index < size())) {
            return nodes.get(index);
        }
        else {
            throw new ArrayIndexOutOfBoundsException(); // 这里是不是有问题
        }
    }

    //@ ensures \result == (\exists int i;
    // 0 <= i && i < nodes.length; nodes[i] == node);
    @Override
    public /*@pure@*/boolean containsNode(int i) {
        return nodes.contains(i);
    }

    ArrayList<Integer> getDistinctNodeSet() {
        ArrayList<Integer> distinctnodes = new ArrayList<>();
        if (nodes.size() == 0) {
            return null;
        }
        else if (nodes.size() == 1) {
            return nodes;
        }
        int i;
        int j;
        distinctnodes.add(nodes.get(0));
        for (j = 1;j < nodes.size();j++) {
            for (i = 0; i < j; i++) {
                if (nodes.get(i).equals(nodes.get(j))) {
                    break;
                }
            }
            if (i == j) {
                distinctnodes.add(nodes.get(j));
            }
        }
        return distinctnodes;
    }

    /*@ ensures \result == (\num_of int i, j;
     0 <= i && i < j && j < nodes.length;
                                 nodes[i] != nodes[j]);
      @*/

    @Override
    public /*pure*/int getDistinctNodeCount() {
        int count = 1;
        if ((nodes.size() == 0) || (nodes.size() == 1)) {
            return nodes.size(); // not Valid
        }
        int i;
        int j;
        for (j = 1; j < nodes.size(); j++) {
            for (i = 0; i < j; i++) {
                if (nodes.get(i).equals(nodes.get(j))) {
                    break;
                }
            }
            if (i == j) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        else {
            return ((MyPath)obj).nodes.equals(nodes);
        }
    }

    //@ ensures \result == (nodes.length >= 2);
    @Override
    public /*@pure@*/boolean isValid() {
        return (nodes.size() >= 2);
    }

    //this > o,return 1;
    //this == o,return 0;
    //this < o,return -1
    @Override
    public int compareTo(Path o) {
        int length;
        int flag;
        if (o.size() > this.size()) {
            length = this.size();
            flag = -1;
        }
        else if (o.size() < this.size()) {
            length = o.size();
            flag = 1;
        }
        else {
            length = o.size();
            flag = 0;
        }
        for (int i = 0;i < length;i++) {
            if (this.getNode(i) < o.getNode(i)) {
                return -1;
            }
            else if (this.getNode(i) > o.getNode(i)) {
                return 1;
            }
        }
        return flag;
    }

    @Override
    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }
}
