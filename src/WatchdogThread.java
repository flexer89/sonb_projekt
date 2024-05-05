public class WatchdogThread extends Thread {
    private MyLongRunningProcess[] threads;
    private long timeoutMillis;
    private GUI gui;

    public WatchdogThread(MyLongRunningProcess[] threads, long timeoutMillis, GUI gui) {
        this.threads = threads;
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            for (MyLongRunningProcess thread : threads) {
                System.out.println(thread.getLastProgressTime());
                if (thread != null && currentTime - thread.getLastProgressTime() > timeoutMillis && thread.isRunning()) {
                    if (gui != null) {
                        gui.printToConsole("Thread " + thread.getThreadId() + " is stuck. Restarting...");
                        thread.stop();
                        thread.start();
                        gui.updateRestartCounter(Integer.parseInt(thread.getThreadId()), thread.getRestartCount());
                    }
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
