import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;
import java.util.ArrayList;

class Dispatcher {
    private ElevatorThread aele;
    private ElevatorThread bele;
    private ElevatorThread cele;
    private ArrayList<PersonRequest> areqs;
    private ArrayList<PersonRequest> breqs;
    private ArrayList<PersonRequest> creqs;
    private ArrayList<PersonRequest> allreqs;
    private boolean endflag;
    private static volatile Dispatcher dispatcher;

    private Dispatcher(ElevatorThread a,ElevatorThread b,ElevatorThread c) {
        allreqs = new ArrayList<>();
        areqs = new ArrayList<>();
        breqs = new ArrayList<>();
        creqs = new ArrayList<>();
        aele = a;
        bele = b;
        cele = c;
        endflag = false;
        a.setDispatcher(this);
        b.setDispatcher(this);
        c.setDispatcher(this);
        a.start();
        b.start();
        c.start();
    }

    static Dispatcher getDispatcher(ElevatorThread a,ElevatorThread b,
                                    ElevatorThread c) {
        if (dispatcher == null) {
            synchronized (Dispatcher.class) {
                if (dispatcher == null) {
                    dispatcher = new Dispatcher(a,b,c);
                }
            }
        }
        return dispatcher;
    }

    synchronized void signalOthersExit() {
        notifyAll();
    }

    private synchronized void setEndflag() {
        endflag = true;
        notifyAll();
    }

    private synchronized boolean runningThread(ElevatorThread thread) {
        if (thread.abtainName() == 'A') {
            return thread.abtainNum() != 0 ||
                    !thread.abtainRequest().isEmpty() ||
                    !areqs.isEmpty();
        }
        else if (thread.abtainName() == 'B') {
            return thread.abtainNum() != 0 ||
                    !thread.abtainRequest().isEmpty() ||
                    !breqs.isEmpty();
        }
        else {
            return thread.abtainNum() != 0 ||
                    !thread.abtainRequest().isEmpty() ||
                    !creqs.isEmpty();
        }
    }

    private synchronized boolean runningThreadbyname(char ch) {
        if (ch == 'A') {
            return aele.abtainNum() != 0 ||
                    !aele.abtainRequest().isEmpty() ||
                    !areqs.isEmpty();
        } else if (ch == 'B') {
            return bele.abtainNum() != 0 ||
                    !bele.abtainRequest().isEmpty() ||
                    !breqs.isEmpty();
        } else {
            return cele.abtainNum() != 0 ||
                    !cele.abtainRequest().isEmpty() ||
                    !creqs.isEmpty();
        }
    }

    synchronized boolean notEnd() {
        if (!endflag) {
            return true;
        }
        else {
            return (runningThread(aele) || runningThread(bele)
                    || runningThread(cele) || allreqs.size() != 0
                    || areqs.size() != 0 || breqs.size() != 0 ||
                    creqs.size() != 0);
        }
    }

    synchronized void insertReq(PersonRequest req) {
        allreqs.add(req);
        if (req == null) {
            setEndflag();
        }
        else {
            notifyAll();
        }
    }

    synchronized void insertReqAtHead(PersonRequest req,char name) {
        //TimableOutput.println("Insert a request at head " + req);
        if (name == 'A') {
            areqs.add(req);
        }
        else if (name == 'B') {
            breqs.add(req);
        }
        else {
            creqs.add(req);
        }
        //allreqs.add(0,req);
        notifyAll();
    }

    synchronized void insertReqSpecially(PersonRequest req,char ex) {
        if (ex == 'A') {
            areqs.add(req);
        }
        else if (ex == 'B') {
            breqs.add(req);
        }
        else {
            creqs.add(req);
        }
        notifyAll();
    }

    private synchronized ArrayList<PersonRequest> getSpecialReq(char name) {
        if (name == 'A') {
            return areqs;
        }
        else if (name == 'B') {
            return breqs;
        }
        else {
            return creqs;
        }
    }

    private synchronized char direct(PersonRequest request) {
        if (aele.abtainReachable().contains(request.getFromFloor()) &&
                aele.abtainReachable().contains(request.getToFloor())) {
            return 'A';
        } else if (bele.abtainReachable().contains(request.getFromFloor()) &&
                bele.abtainReachable().contains(request.getToFloor())) {
            return 'B';
        } else if (cele.abtainReachable().contains(request.getFromFloor()) &&
                cele.abtainReachable().contains(request.getToFloor())) {
            return 'C';
        }
        else {
            return 'D';
        }
    }

    private synchronized boolean directbyName(PersonRequest request,char name) {
        if (name == 'A') {
            return aele.abtainReachable().contains(request.getFromFloor()) &&
                    aele.abtainReachable().contains(request.getToFloor());
        }
        else if (name == 'B') {
            return bele.abtainReachable().contains(request.getFromFloor()) &&
                    bele.abtainReachable().contains(request.getToFloor());
        }
        else {
            return cele.abtainReachable().contains(request.getFromFloor()) &&
                    cele.abtainReachable().contains(request.getToFloor());
        }
    }

    private synchronized PersonRequest findProperRequest(
            char name, int floor,ArrayList<Integer> reachable) {
        PersonRequest p = null;
        int mindis = 30;
        if (getSpecialReq(name).size() != 0) {
            p = getSpecialReq(name).get(0);
            getSpecialReq(name).remove(p);
            //TimableOutput.println("special req is" + p + " in " + name);
            return p;
        }
        else if (allreqs.size() != 0) {
            //TimableOutput.println("Elevator " + name + " station 1");
            if (allreqs.get(0) == null) {
                allreqs.remove(0);
                return null;
            }
            for (PersonRequest pr : allreqs) {
                if (pr != null) {
                    if (directbyName(pr,name)) {
                        p = pr;
                        allreqs.remove(p);
                        return p;
                    }
                    else if (reachable.contains(pr.getFromFloor())) {
                        if (direct(pr) != name &&
                                direct(pr) != 'D' &&
                                !runningThreadbyname(direct(pr))) {
                            continue;
                        }
                        if (Math.abs(floor - pr.getFromFloor()) <= mindis) {
                            p = pr;
                        }
                    }
                }
            }
            if (p != null) {
                allreqs.remove(p);
                return p;
            }
            else {
                return new PersonRequest(-4,-4,0);
            }
        }
        else {
            return null;
        }
    }

    synchronized PersonRequest getRequest(
            ArrayList<Integer> reachable,char name,int floor) {
        PersonRequest p;
        PersonRequest pspc = new PersonRequest(-4,-4,0);
        if (allreqs.size() != 0 || getSpecialReq(name).size() != 0) {
            p = findProperRequest(name,floor,reachable);
            if (p == null) {
                return null;
            }
            else if (!p.equals(pspc)) {
                return p;
            }
        }
        while (true) {
            try {
                wait();
                if (allreqs.size() != 0 || getSpecialReq(name).size() != 0) {
                    p = findProperRequest(name,floor,reachable);
                    if (p == null) {
                        return null;
                    }
                    else if (!p.equals(pspc)) {
                        return p;
                    }
                    else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized ArrayList<PersonRequest> getPickupList(int direction,
                                                        int floor,char name) {
        ArrayList<PersonRequest> pickuplist = new ArrayList<>();
        ArrayList<PersonRequest> removelistall = new ArrayList<>();
        for (PersonRequest p : allreqs) {
            if (p != null) {
                if (p.getFromFloor() == floor &&
                        direction * (p.getToFloor() - p.getFromFloor()) > 0) {
                    pickuplist.add(p);
                    removelistall.add(p);
                }
            }
            else {
                break;
            }
        }
        ArrayList<PersonRequest> removelistspc = new ArrayList<>();
        ArrayList<PersonRequest> specialReq = getSpecialReq(name);
        for (PersonRequest q : specialReq) {
            if (q.getFromFloor() == floor &&
                    direction * (q.getToFloor() - q.getFromFloor()) > 0) {
                pickuplist.add(q);
                removelistspc.add(q);
            }
        }
        allreqs.removeAll(removelistall);
        getSpecialReq(name).removeAll(removelistspc);
        return pickuplist;
    }

    synchronized void arrive(int floor,char name) {
        TimableOutput.println("ARRIVE-" + floor + "-" + name);
    }

    synchronized void open(int floor,char name) {
        TimableOutput.println("OPEN-" + floor + "-" + name);
    }

    synchronized void getinPerson(PersonRequest p,char name,int floor) {
        TimableOutput.println("IN-" + p.
                getPersonId() + "-" + floor + "-" + name);
    }

    synchronized void getoutPerson(PersonRequest p,char name,int floor) {
        TimableOutput.println("OUT-" + p.
                getPersonId() + "-" + floor + "-" + name);
    }

    synchronized void close(int floor,char name) {
        TimableOutput.println("CLOSE-" + floor + "-" + name);
    }
}
