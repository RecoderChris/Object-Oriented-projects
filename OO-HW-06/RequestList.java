import java.util.ArrayList;
import com.oocourse.elevator2.PersonRequest;

class RequestList {
    //single-pattern mode
    private static volatile RequestList requestList;

    private ArrayList<PersonRequest> requests;
    private ArrayList<PersonRequest> passerby;
    private PersonRequest mainreq;
    private int currentFloor;

    private RequestList() {
        requests = new ArrayList<>();
        passerby = new ArrayList<>();
    }

    static RequestList getRequestList() {
        if (requestList == null) {
            synchronized (Controller.class) {
                if (requestList == null) {
                    requestList = new RequestList();
                }
            }
        }
        return requestList;
    }

    //operations for requests.
    synchronized void insertRequest(PersonRequest r) {
        requests.add(r);
        notifyAll();
    }

    synchronized PersonRequest getRequest() {
        while (requests.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PersonRequest p = requests.get(0);
        requests.remove(p);
        return p;
    }

    //setings for the passerby
    private synchronized void setMainreq(PersonRequest p) {
        mainreq = p;
    }

    synchronized PersonRequest getMainreq() {
        return mainreq;
    }

    private void setCurrentFloor(int c) {
        currentFloor = c;
    }

    private int getDirection() {
        if (mainreq.getToFloor() - currentFloor > 0) {
            return 1;
        }
        else {
            return -1;
        }
    }

    synchronized boolean judgePasserby(PersonRequest p,int c) {
        setMainreq(p);
        setCurrentFloor(c);
        int direction = getDirection();
        try {
            for (PersonRequest pr : requests) {
                if (pr.getFromFloor() == currentFloor &&
                        direction * (pr.getToFloor() - currentFloor) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.print("");
        }
        return false;
    }

    synchronized boolean setPasserby(PersonRequest p,int c) {
        setMainreq(p);
        setCurrentFloor(c);
        int direction = getDirection();
        ArrayList<PersonRequest> removeset = new ArrayList<>();
        try {
            for (PersonRequest pr : requests) {
                if (pr.getFromFloor() == currentFloor &&
                        direction * (pr.getToFloor() - currentFloor) > 0) {
                    passerby.add(pr);
                    if (direction * (mainreq.
                            getToFloor() - pr.getToFloor()) > 0) {
                        mainreq = pr;
                    }
                    removeset.add(pr);
                }
            }
        } catch (Exception e) {
            System.out.print("");
        }
        requests.removeAll(removeset);
        return (passerby.size() != 0);
    }

    synchronized ArrayList<PersonRequest> getPasserby() {
        return passerby;
    }

    synchronized void emptyPasserby() {
        passerby = new ArrayList<>();
    }
}
