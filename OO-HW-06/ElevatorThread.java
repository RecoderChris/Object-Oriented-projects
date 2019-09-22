import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;
import java.util.ArrayList;

public class ElevatorThread extends Thread {
    private int floor;

    private ArrayList<PersonRequest> passerbyReq;
    private PersonRequest mainReq;
    private RequestList requestList;

    ElevatorThread()
    {
        requestList = RequestList.getRequestList();
        passerbyReq = new ArrayList<>();
        floor = 1;
    }

    //waitingState
    private void waiting() {
        if (mainReq.getFromFloor() == floor) {
            closingWithPerson();
        }
        else {
            //TimableOutput.println("[Info ]: This is a new turn of " +
            //        "elevator..." + "(without person from " + floor + ")");
            movingWithoutPerson();
        }
    }

    private void arriveSomefloor() {
        TimableOutput.println("ARRIVE-" + floor);
        if (mainReq.getFromFloor() == floor ||
                requestList.judgePasserby(mainReq,floor) ||
                floor == mainReq.getToFloor()) {
            //TimableOutput.println("[Info ]: Open at FLOOR " + floor);
            closingWithPerson();
        }
        else {
            movingOnefloor();
        }
    }

    private void movingWithoutPerson() {
        try {
            long time = 400;
            while (floor != mainReq.getFromFloor()) {
                //TimableOutput.println("[Info ]: Begin moving from " + floor);
                sleep(time);
                if (mainReq.getFromFloor() > floor) {
                    floor = floor + 1;
                    if (floor == 0) {
                        floor = floor + 1;
                    }
                } else {
                    floor = floor - 1;
                    if (floor == 0) {
                        floor = floor - 1;
                    }
                }
                if (floor == mainReq.getFromFloor()) {
                    break;
                }
                TimableOutput.println("ARRIVE-" + floor);
            }
            arriveSomefloor();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 2 please check..");
        }
    }

    private void movingOnefloor() {
        try {
            long time = 400;
            //TimableOutput.println("[Info ]: Begin moving from " + floor);
            sleep(time);
            if (mainReq.getToFloor() < floor) {
                floor = floor - 1;
                if (floor == 0) {
                    floor = floor - 1;
                }
            } else {
                floor = floor + 1;
                if (floor == 0) {
                    floor = floor + 1;
                }
            }
            arriveSomefloor();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 2 please check..");
        }
    }

    //ClosingWithPerson
    private void closingWithPerson() {
        try {
            long time = 200;
            TimableOutput.println("OPEN-" + floor);
            sleep(time);
            sleep(time);
            //get in the elevator
            if (mainReq.getFromFloor() == floor) {
                TimableOutput.println("IN-" +
                        mainReq.getPersonId() + "-" + floor);
            }
            if (requestList.setPasserby(mainReq,floor)) {
                ArrayList<PersonRequest> temp = requestList.getPasserby();
                passerbyReq.addAll(temp);
                requestList.emptyPasserby();
                mainReq = requestList.getMainreq();
                for (PersonRequest pr : temp) {
                    TimableOutput.println("IN-" +
                            pr.getPersonId() + "-" + floor);
                }
            }
            //out the elevator
            if (floor == mainReq.getToFloor()) {
                int min = 200;
                ArrayList<PersonRequest> removeset = new ArrayList<>();
                for (PersonRequest pr: passerbyReq) {
                    if (floor == pr.getToFloor()) {
                        TimableOutput.println("OUT-" + pr.getPersonId()
                                + "-" + floor);
                        removeset.add(pr);
                    } else if (pr.getToFloor() < min) {
                        mainReq = pr;
                    }
                }
                passerbyReq.removeAll(removeset);
            }
            TimableOutput.println("CLOSE-" + floor);
            if (passerbyReq.size() != 0) {
                movingOnefloor();
            }
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 3 please check..");
        }
    }

    private void changeState() {
        while (true) {
            mainReq = requestList.getRequest();
            if (mainReq == null) {
                break;
            }
            passerbyReq.add(mainReq);
            waiting();
        }

    }

    @Override
    public void run() {
        changeState();
    }
}
