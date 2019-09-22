import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;
import java.util.ArrayList;
import java.util.HashMap;

public class ElevatorThread extends Thread {
    private Dispatcher dispatcher;
    private ArrayList<PersonRequest> reqlist;
    private int destination;
    private int floor;
    private int direction;
    private int numberofperson;
    private char name; // A or B or C
    private ArrayList<Integer> stoppablefloor;
    private int timeforonefloor;
    private int context;
    private ArrayList<Integer> abcommon;
    private ArrayList<Integer> bccommon;
    private ArrayList<Integer> accommon;
    private ArrayList<Integer> areach;
    private ArrayList<Integer> breach;
    private ArrayList<Integer> creach;
    private HashMap<Integer,ReqAndXchange> reqleft;
    private boolean firston;
    private HashMap<Integer,PersonRequest> container;
    private PersonRequest first;

    void setDispatcher(Dispatcher ds) {
        dispatcher = ds;
    }

    ElevatorThread(char n,ArrayList<Integer> stop,int time,int containnum)
    {
        reqlist = new ArrayList<>();
        floor = 1;
        direction = 0;
        numberofperson = 0;
        name = n;
        stoppablefloor = stop;
        timeforonefloor = time;
        context = containnum;
        reqleft = new HashMap<>();
        int[] ab = new int[]{-2,-1,1,15};
        abcommon = new ArrayList<>();
        for (int i : ab) { abcommon.add(i); }
        int[] bc = new int[]{1,5,7,9,11,15};
        bccommon = new ArrayList<>();
        for (int i : bc) { bccommon.add(i); }
        int[] ac = new int[]{1,15};
        accommon = new ArrayList<>();
        for (int i : ac) { accommon.add(i); }
        int[] a = new int[]{-3,-2,-1,1,15,16,17,18,19,20};
        areach = new ArrayList<>();
        for (int i : a) { areach.add(i); }
        int[] b = new int[]{-2,-1,1,2,4,5,6,7,8,9,10,11,12,13,14,15};
        breach = new ArrayList<>();
        for (int i : b) { breach.add(i); }
        int[] c = new int[]{1,3,5,7,9,11,13,15};
        creach = new ArrayList<>();
        for (int i : c) { creach.add(i); }
        firston = false;
        container = new HashMap<>();
    }

    ArrayList<Integer> abtainReachable() {
        return stoppablefloor;
    }

    int abtainNum() {
        return numberofperson;
    }

    char abtainName() {
        return name;
    }

    ArrayList<PersonRequest> abtainRequest() {
        return reqlist;
    }

    private void setDirectionandDestination() {
        if (floor == destination) {
            if (reqlist.size() != 0) {
                destination = reqlist.get(0).getToFloor();
                if (destination - floor > 0) {
                    direction = 1;
                } else {
                    direction = -1;
                }
            }
        }
    }

    private void setDestination(ArrayList<PersonRequest> newcome) {
        for (PersonRequest pr : newcome) {
            if (direction == 1) {
                if (pr.getToFloor() > destination) {
                    destination = pr.getToFloor();
                }
            }
            else {
                if (pr.getToFloor() < destination) {
                    destination = pr.getToFloor();
                }
            }
        }
    }

    private void setDirection(PersonRequest req) {
        if (req.getFromFloor() > floor) {
            direction = 1;
            destination = req.getFromFloor();
        }
        else if (req.getFromFloor() < floor) {
            direction = -1;
            destination = req.getFromFloor();
        }
        else {
            destination = req.getToFloor();
            if (req.getToFloor() > floor) {
                direction = 1;
            }
            else {
                direction = -1;
            }
        }
    }

    private int findMin(ArrayList<Integer> reachable,int des) {
        int division = 1;
        int distance = 30;
        for (int i : reachable) {
            if ((Math.abs(i - des) <= distance) && (i != floor)) {
                distance = Math.abs(i - des);
                division = i;
            }
        }
        return division;
    }

    private PersonRequest divideReq(PersonRequest pr) {
        int division = 1;
        char ex = 'A';
        if (name == 'A') {
            if (breach.contains(pr.getToFloor())) {
                division = findMin(abcommon,pr.getToFloor());
                ex = 'B';
            }
            else if (creach.contains(pr.getToFloor())) {
                division = findMin(accommon,pr.getToFloor());
                ex = 'C';
            }
        }
        else if (name == 'B') {
            if (areach.contains(pr.getToFloor())) {
                division = findMin(abcommon,pr.getToFloor());
            }
            else if (creach.contains(pr.getToFloor())) {
                division = findMin(bccommon,pr.getToFloor());
                ex = 'C';
            }
        }
        else if (name == 'C') {
            if (areach.contains(pr.getToFloor())) {
                division = findMin(accommon,pr.getToFloor());
            }
            else if (breach.contains(pr.getToFloor())) {
                division = findMin(bccommon,pr.getToFloor());
                ex = 'B';
            }
        }
        PersonRequest newreq = new PersonRequest(
                pr.getFromFloor(),division,pr.getPersonId());
        PersonRequest newleft = new PersonRequest(
                division,pr.getToFloor(),pr.getPersonId());
        reqleft.put(newleft.getPersonId(),new ReqAndXchange(newleft,ex));
        return newreq;
    }

    private void waiting() {
        PersonRequest req = reqlist.get(0);
        setDirection(req);
        if (req.getFromFloor() == floor) {
            firston = true;
            closingWithPerson();
        }
        else {
            firston = false;
            movingOnefloor();
        }
    }

    private void arriveSomefloor() {
        boolean flag = false;
        ArrayList<PersonRequest> removeset = new ArrayList<>();
        dispatcher.arrive(floor,name);
        if (stoppablefloor.contains(floor)) {
            ArrayList<PersonRequest> pickup = dispatcher.
                    getPickupList(direction,floor,name);
            reqlist.addAll(pickup);
            //setDestination(pickup);
            for (PersonRequest p : reqlist) {
                if (p.getFromFloor() == floor) {
                    if (context <= numberofperson) {
                        removeset.add(p);
                    } else {
                        flag = true;
                        if (p.equals(first)) {
                            firston = true;
                        }
                    }
                }
                else if (p.getToFloor() == floor) {
                    if (p.equals(first)) {
                        if (firston) {
                            flag = true;
                        }
                    }
                    else {
                        flag = true;
                    }
                }
            }
        }
        if (flag) { closingWithPerson(); }
        else {
            if (removeset.size() != 0) {
                for (PersonRequest pr:removeset) {
                    dispatcher.insertReq(pr);
                    reqlist.remove(pr);
                }
            }
            movingOnefloor();
        }
    }

    private void movingOnefloor() {
        try {
            long time = timeforonefloor;
            sleep(time);
            floor = floor + direction;
            if (floor == 0) {
                floor = floor + direction;
            }
            arriveSomefloor();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 2 please check..");
        }
    }

    private void closingWithPerson() {
        try {
            ArrayList<PersonRequest> removeset1 = new ArrayList<>();
            ArrayList<PersonRequest> removeset2 = new ArrayList<>();
            ArrayList<PersonRequest> inputset = new ArrayList<>();
            dispatcher.open(floor,name);
            sleep(400);
            for (PersonRequest p : reqlist) {
                if ((p != first) || (firston)) {
                    if (p.getToFloor() == floor) {
                        dispatcher.getoutPerson(p, name, floor);
                        container.remove(p.getPersonId());
                        removeset1.add(p);
                        numberofperson--;
                        if (reqleft.containsKey(p.getPersonId())) {
                            dispatcher.insertReqSpecially(reqleft.get(p.
                                    getPersonId()).getReq(), reqleft.
                                    get(p.getPersonId()).getExelevator());
                            reqleft.remove(p.getPersonId());
                        }
                    }
                }
            }
            reqlist.removeAll(removeset1);
            reqlist.addAll(dispatcher.getPickupList(direction,floor,name));
            for (PersonRequest p : reqlist) {
                if ((p.getFromFloor() == floor) &&
                        (!container.containsKey(p.getPersonId()))) {
                    if (numberofperson < context) {
                        if (!stoppablefloor.contains(p.getToFloor())) {
                            removeset2.add(p);
                            PersonRequest addone = divideReq(p);
                            inputset.add(addone);
                        }
                        dispatcher.getinPerson(p,name,floor);
                        container.put(p.getPersonId(),p);
                        numberofperson++;
                    }
                    else {
                        dispatcher.insertReqAtHead(p,name);
                        removeset2.add(p);
                    }
                }
            }
            reqlist.removeAll(removeset2);
            reqlist.addAll(inputset);
            setDestination(inputset);
            setDirectionandDestination();
            setDestination(inputset);
            if (reqlist.size() != 0) {
                dispatcher.close(floor,name);
                movingOnefloor();
            }
            else { dispatcher.close(floor,name); }
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 3 please check..");
        }
    }

    private void changeState() {
        PersonRequest newreq;
        while (dispatcher.notEnd()) {
            newreq = dispatcher.getRequest(stoppablefloor,name,floor);
            if (newreq != null) {
                reqlist.add(newreq);
                first = newreq;
                waiting();
            }

        }
        //TimableOutput.println("I am out --" + name);
        dispatcher.signalOthersExit();

    }

    @Override
    public void run() {
        changeState();
    }

}
