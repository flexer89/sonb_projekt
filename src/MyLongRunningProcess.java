import java.util.concurrent.atomic.AtomicLong;

public class MyLongRunningProcess implements Runnable {
    private int threadId;
    private boolean running;
    private Thread shutdownEvent;
    private int restartCount;
    private GUI gui;
    int successfulCalculations;
    private int fibonacciRangeStart;
    private int fibonacciRangeEnd;
    private AtomicLong lastProgressTime;

    public MyLongRunningProcess(int threadId, Thread shutdownEvent, int restartCount, int successfulCount, GUI gui) {
        this.threadId = threadId;
        this.shutdownEvent = shutdownEvent;
        this.restartCount = restartCount;
        this.gui = gui;
        this.successfulCalculations = successfulCount;
        this.lastProgressTime = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public void run() {
        running = true;
        recursiveWork();
    }

    public void setRestartCount(int count) {
        restartCount = count;
    }
    

    private void recursiveWork() {
        while (running && !shutdownEvent.isInterrupted()) {
            running = true;
            gui.updateStatus(threadId, "Starting");
            gui.printToConsole("Thread " + threadId + ": Starting");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (!running) break;
            fibonacciRangeEnd = Integer.parseInt(gui.getEndRanges()[threadId].getText());
            fibonacciRangeStart = Integer.parseInt(gui.getStartRanges()[threadId].getText());

            int index = (int) (Math.random() * (fibonacciRangeEnd - fibonacciRangeStart + 1)) + fibonacciRangeStart;
            gui.printToConsole("Thread " + threadId + ": Calculating fibonacci(" + index + ")");
            long result = fibonacci(index);
            gui.printToConsole("Thread " + threadId + ": Result(" + index + ") = " + result);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            successfulCalculations++;
            gui.updateSuccessfulCounter(threadId, successfulCalculations);
            gui.updateStatus(threadId, "Fibonacci(" + index + ") = " + result);
            lastProgressTime.set(System.currentTimeMillis()); // Update last progress time

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        gui.updateStatus(threadId, "Exiting");
    }

    public void stop() {
        running = false;
    }

    public long getLastProgressTime() {
        return lastProgressTime.get();
    }

    private long fibonacci(long n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    public void setFibonacciRange(int start, int end) {
        fibonacciRangeStart = start;
        fibonacciRangeEnd = end;
    }

    public String getThreadId() {
        return Integer.toString(threadId);
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        new Thread(this).start();
    }

    public void setAtomicLastProgressTime(long time) {
        lastProgressTime.set(time);
    }

    public int getRestartCount() {
        return restartCount;
    }
}

