public class WatchdogThread extends Thread {
    private MyLongRunningProcess[] threads;
    private long timeoutMillis;
    private GUI gui;

    public WatchdogThread(MyLongRunningProcess[] threads, long timeoutMillis, GUI gui) {
        this.threads = threads;
        this.timeoutMillis = timeoutMillis;
        this.gui = gui;
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            for (MyLongRunningProcess thread : threads) {
                if (thread != null && currentTime - thread.getLastProgressTime() > timeoutMillis && thread.isRunning()) {
                    gui.printToConsole("Thread " + thread.getThreadId() + " is stuck. Restarting...");
                    thread.stop();
                    thread.createNewThread();
                    int restartCount = thread.getRestartCount();
                    gui.updateRestartCounter(Integer.parseInt(thread.getThreadId()), restartCount + 1);
                    thread.setRestartCount(restartCount + 1);
                }
            }
            try {
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
