import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

import java.io.IOException;

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
    private void getRequestFromStdIn() throws IOException {
        PersonRequest request;
        RequestList requestlist = RequestList.getRequestList();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                requestlist.insertRequest(null);
                break;
            } else {
                // a new valid request
                requestlist.insertRequest(request);
            }
        }
        elevatorInput.close();
    }

    @Override
    public void run() {
        try {
            getRequestFromStdIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
