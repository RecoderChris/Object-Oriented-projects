import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

class Controller extends Thread {
    // create a singleton-pattern controller
    private static volatile Controller controller;

    private Controller() {}

    static Controller getController() {
        if (controller == null) {
            synchronized (Controller.class) {
                if (controller == null) {
                    controller = new Controller();
                }
            }
        }
        return controller;
    }

    //get requests from standard input.
    private void getRequestFromStdIn() {
        PersonRequest request;
        RequestList requestlist = RequestList.getRequestList();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                requestlist.setEndflag();
                break;
            } else {
                // a new valid request
                requestlist.insertRequest(request);
            }
        }
    }

    @Override
    public void run() {
        getRequestFromStdIn();
    }

}
