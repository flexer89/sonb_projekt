import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;



public class Main {
    public static void main(String[] args) {
        int numThreads = Integer.parseInt(JOptionPane.showInputDialog("Number of Threads", "Enter the number of threads:"));
        GUI gui = new GUI(numThreads);
    }
}