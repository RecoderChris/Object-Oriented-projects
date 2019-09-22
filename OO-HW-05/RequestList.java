import java.util.ArrayList;
import com.oocourse.elevator1.PersonRequest;

class RequestList {
    //single-pattern mode
    private static volatile RequestList requestList;
    private ArrayList<PersonRequest> requests;
    private boolean endflag;

    private RequestList() {
        requests = new ArrayList<>();
        endflag = false;
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

    synchronized ArrayList<PersonRequest> getRequests() {
        return requests;
    }

    synchronized void insertRequest(PersonRequest r) {
        requests.add(r);
    }

    synchronized void removeFirstRequest() {
        requests.remove(0);
    }

    synchronized void setEndflag() {
        endflag = true;
    }

    synchronized boolean getEndflag() {
        return endflag;
    }
}
