import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import java.io.IOException;
import java.util.ArrayList;

class MainThread extends Thread {
    private static volatile MainThread mainthread;

    private MainThread() {}

    static MainThread getMainthread() {
        if (mainthread == null) {
            synchronized (MainThread.class) {
                if (mainthread == null) {
                    mainthread = new MainThread();
                }
            }
        }
        return mainthread;
    }

    private void getRequestFromStdIn() throws IOException {
        int[] a = new int[]{-3,-2,-1,1,15,16,17,18,19,20};
        ArrayList<Integer> stopa = new ArrayList<>();
        for (Integer i:a) { stopa.add(i); }
        ElevatorThread elevatorThreadA = new ElevatorThread('A',stopa,400,6);
        elevatorThreadA.setPriority(6);
        int[] b = new int[]{-2,-1,1,2,4,5,6,7,8,9,10,11,12,13,14,15};
        ArrayList<Integer> stopb = new ArrayList<>();
        for (Integer i:b) { stopb.add(i); }
        ElevatorThread elevatorThreadB = new ElevatorThread('B',stopb,500,8);
        elevatorThreadB.setPriority(5);
        int[] c = new int[]{1,3,5,7,9,11,13,15};
        ArrayList<Integer> stopc = new ArrayList<>();
        for (Integer i:c) { stopc.add(i); }
        ElevatorThread elevatorThreadC = new ElevatorThread('C',stopc,600,7);
        elevatorThreadC.setPriority(4);
        Dispatcher dispatcher = Dispatcher.getDispatcher(elevatorThreadA,
                elevatorThreadB,elevatorThreadC);
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                dispatcher.insertReq(null);
                break;
            }
            else {
                dispatcher.insertReq(request);
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
