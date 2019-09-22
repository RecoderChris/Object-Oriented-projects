import com.oocourse.elevator3.PersonRequest;

class ReqAndXchange {
    private PersonRequest req;
    private char exelevator;

    ReqAndXchange(PersonRequest r,char ex) {
        req = r;
        exelevator = ex;
    }

    char getExelevator() {
        return exelevator;
    }

    PersonRequest getReq() {
        return req;
    }
}
