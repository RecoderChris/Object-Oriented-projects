import com.oocourse.TimableOutput;

public class ControllerDemo {
    public static void main(String[] args) {
        //获取唯一可用的对象
        // please MUST initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        Controller controller = Controller.getController();
        ElevatorThread elevator = new ElevatorThread();
        controller.start();
        elevator.start();
    }
}
