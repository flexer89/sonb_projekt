import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        int numThreads = Integer.parseInt(JOptionPane.showInputDialog("Number of Threads", "Enter the number of threads:"));
        GUI gui = new GUI(numThreads);

        MyLongRunningProcess[] threads = new MyLongRunningProcess[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new MyLongRunningProcess(i, new Thread(), 0, 0, gui);
            int startRange = Integer.parseInt(gui.getStartRanges()[i].getText());
            int endRange = Integer.parseInt(gui.getEndRanges()[i].getText());
            threads[i].setFibonacciRange(startRange, endRange);
        }

        gui.setThreads(threads);

        WatchdogThread watchdog = new WatchdogThread(threads, 1000, gui);
        watchdog.start();
    }
}