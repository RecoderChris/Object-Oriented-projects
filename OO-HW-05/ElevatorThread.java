import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class ElevatorThread extends Thread {
    private int floor;
    private PersonRequest currentR;

    ElevatorThread()
    {
        floor = 1;
    }

    //waitingState
    private void waiting() {
        if (currentR.getFromFloor() == floor) {
            closingWithPerson();
        }
        else {
            movingWithoutPerson();
        }
    }

    //movingWithPersonState
    private void movingWithPerson() {
        try {
            long time = Math.abs(currentR.getToFloor() - floor) * 500;
            sleep(time);
            floor = currentR.getToFloor();
            TimableOutput.println("OPEN-" + floor);
            sleep(250);
            TimableOutput.println("OUT-" + currentR.
                    getPersonId() + "-" + floor);
            sleep(250);
            TimableOutput.println("CLOSE-" + floor);
            RequestList.getRequestList().removeFirstRequest();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]: Interrupt in Place 1 please check.");
        }
    }

    //movingWithoutPersonState
    private void movingWithoutPerson()  {
        try {
            long time = Math.abs(currentR.getFromFloor() - floor) * 500;
            sleep(time);
            floor = currentR.getFromFloor();
            closingWithPerson();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 2 please check..");
        }
    }

    //ClosingWithPerson
    private void closingWithPerson() {
        try {
            long time = 250;
            TimableOutput.println("OPEN-" + floor);
            sleep(time);
            TimableOutput.println("IN-" + currentR.getPersonId() + "-" + floor);
            sleep(time);
            TimableOutput.println("CLOSE-" + floor);
            movingWithPerson();
        } catch (InterruptedException e) {
            TimableOutput.println(
                    "[Warning ]:Interrupt in Place 3 please check..");
        }
    }

    private void changeState() {
        RequestList requestList = RequestList.getRequestList();
        while (true) {
            if (requestList.getEndflag() &&
                    requestList.getRequests().size() == 0) {
                break;
            }
            if (requestList.getRequests().size() > 0) {
                currentR = requestList.getRequests().get(0);
                waiting();
            }
        }
    }

    @Override
    public void run() {
        changeState();
    }
}
