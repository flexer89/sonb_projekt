import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;

class Watchdog {
    private static final int WATCHDOG_HARD_KILL_TIMEOUT = 1000; // Timeout in milliseconds

    private int numThreads;
    private MyLongRunningProcess[] threads;
    private GUI gui;

    public Watchdog(int numThreads, MyLongRunningProcess[] threads, GUI gui) {
        this.numThreads = numThreads;
        this.threads = threads;
        this.gui = gui;
    }

    public void startWatchdog() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < numThreads; i++) {
                    long lastProgressTime = threads[i].getLastProgressTime();
                    if (System.currentTimeMillis() - lastProgressTime > WATCHDOG_HARD_KILL_TIMEOUT) {
                        gui.printToConsole("Thread " + i + ": No progress in " + WATCHDOG_HARD_KILL_TIMEOUT / 1000 + " seconds. Restarting thread...");
                        // Restart the thread here (optional)
                        // successfulCalculations = threads
                        // threads[i] = new MyLongRunningProcess(i, new Thread(), 0, gui, 0);
                        threads[i].setFibonacciRange(Integer.parseInt(gui.getStartRanges()[i].getText()), Integer.parseInt(gui.getEndRanges()[i].getText()));
                        gui.updateStatus(i, "Restarting");
                        gui.updateSuccessfulCounter(i, 0);
                        new Thread(threads[i]).start();
                    }
                }
            }
        }, 0, 2000); // Check every 2 seconds
    }
}

public class Main {
    public static void main(String[] args) {
        int numThreads = Integer.parseInt(JOptionPane.showInputDialog("Number of Threads", "Enter the number of threads:"));
        GUI gui = new GUI(numThreads);
        MyLongRunningProcess[] threads = new MyLongRunningProcess[numThreads];
        Watchdog watchdog = new Watchdog(numThreads, threads, gui);

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new MyLongRunningProcess(i, new Thread(), 0, gui);
            threads[i].setFibonacciRange(Integer.parseInt(gui.getStartRanges()[i].getText()), Integer.parseInt(gui.getEndRanges()[i].getText()));
            new Thread(threads[i]).start();
        }

        watchdog.startWatchdog();
    }
}

// public class Main {
//     public static void main(String[] args) {
//         int numThreads = Integer.parseInt(JOptionPane.showInputDialog("Number of Threads", "Enter the number of threads:"));
//         GUI gui = new GUI(numThreads);
//     }
// }