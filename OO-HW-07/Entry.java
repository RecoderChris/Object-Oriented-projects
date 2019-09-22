import com.oocourse.TimableOutput;

public class Entry {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        MainThread mainThread = MainThread.getMainthread();
        mainThread.setPriority(7);
        mainThread.start();
    }
}