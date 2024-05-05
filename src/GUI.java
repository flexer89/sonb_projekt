import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;


class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel leftPanel;
    private JPanel[] threadPanels;
    private JLabel[] threadLabels;
    private JLabel[] statusLabels;
    private JLabel[] restartCounters;
    private JLabel[] successfulCounters;
    private JButton[] startButtons;
    private JButton[] stopButtons;
    private JTextField[] startRanges;
    private JTextField[] endRanges;
    private JTextPane consoleOutput;
    private StyledDocument doc;
    private Style style;
    private MyLongRunningProcess[] threads;
    private int numThreads;

    public JTextField[] getStartRanges() {
        return startRanges;
    }

    public JTextField[] getEndRanges() {
        return endRanges;
    }

public synchronized int getSuccessfulCount(int threadId) {
    return threads[threadId].successfulCalculations;
}

    public GUI(int numThreads) {
        super("My GUI");
        this.numThreads = numThreads;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        leftPanel = new JPanel(new GridLayout(numThreads, 1));
        threadPanels = new JPanel[numThreads];
        threadLabels = new JLabel[numThreads];
        statusLabels = new JLabel[numThreads];
        restartCounters = new JLabel[numThreads];
        successfulCounters = new JLabel[numThreads];
        startButtons = new JButton[numThreads];
        stopButtons = new JButton[numThreads];
        startRanges = new JTextField[numThreads];
        endRanges = new JTextField[numThreads];
        threads = new MyLongRunningProcess[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threadPanels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
            threadLabels[i] = new JLabel("<html><b><font size='5'>Thread " + i + ":</font></b></html>");
            threadPanels[i].add(threadLabels[i]);

            statusLabels[i] = new JLabel("<html><b><font size='3'>Status:</font></b><br>Waiting</html>");
            statusLabels[i].setPreferredSize(new Dimension(200, 40)); // Adjust width and height as needed
            threadPanels[i].add(statusLabels[i]);
            
            restartCounters[i] = new JLabel("<html><b><font size='3'>Restarts:</font></b><br>0</html>");
            restartCounters[i].setPreferredSize(new Dimension(200, 40)); // Adjust width and height as needed
            threadPanels[i].add(restartCounters[i]);
            
            successfulCounters[i] = new JLabel("<html><b><font size='3'>Successful Calculations:</font></b><br>0</html>");
            successfulCounters[i].setPreferredSize(new Dimension(200, 40)); // Adjust width and height as needed
            threadPanels[i].add(successfulCounters[i]);
                      

            startButtons[i] = new JButton("Start");
            threadPanels[i].add(startButtons[i]);
            startButtons[i].addActionListener(new StartButtonListener(i));

            stopButtons[i] = new JButton("Stop");
            threadPanels[i].add(stopButtons[i]);
            stopButtons[i].addActionListener(new StopButtonListener(i));

            // Add labels for start and end ranges
            JLabel startLabel = new JLabel("Start Range:");
            threadPanels[i].add(startLabel);
            startRanges[i] = new JTextField("10");
            threadPanels[i].add(startRanges[i]);

            JLabel endLabel = new JLabel("End Range:");
            threadPanels[i].add(endLabel);
            endRanges[i] = new JTextField("50");
            threadPanels[i].add(endRanges[i]);

            leftPanel.add(threadPanels[i]);
        }

        add(leftPanel, BorderLayout.WEST);

        consoleOutput = new JTextPane();
        consoleOutput.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleOutput);
        add(scrollPane, BorderLayout.CENTER);

        doc = consoleOutput.getStyledDocument();
        style = consoleOutput.addStyle("Style", null);

        setSize(800, 500);
        setVisible(true);
    }

    public synchronized void updateStatus(int threadId, String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabels[threadId].setText("<html>Status:<br>" + status + "</html>");

            if (status.startsWith("Fibonacci")) {
                statusLabels[threadId].setForeground(Color.GREEN);
            } else {
                switch (status) {
                    case "Starting":
                        statusLabels[threadId].setForeground(Color.BLACK);
                        break;
                    case "Restarting":
                        statusLabels[threadId].setForeground(Color.RED);
                        break;
                    case "Exiting":
                        statusLabels[threadId].setForeground(Color.BLUE);
                        break;
                    case "Waiting":
                        statusLabels[threadId].setForeground(Color.BLACK);
                        break;
                    default:
                        statusLabels[threadId].setForeground(Color.BLACK); // Default color
                        break;
                }
            }
        });
    }
    
    public synchronized void updateRestartCounter(int threadId, int count) {
        SwingUtilities.invokeLater(() -> {
            restartCounters[threadId].setText("<html>Restarts:<br>" + count + "</html>");
        });
    }
    
    public synchronized void updateSuccessfulCounter(int threadId, int count) {
        SwingUtilities.invokeLater(() -> {
            successfulCounters[threadId].setText("<html>Successful Calculations:<br>" + count + "</html>");
        });
    }
    

    public synchronized void printToConsole(String text) {
        SwingUtilities.invokeLater(() -> {
            try {
                doc.insertString(doc.getLength(), text + "\n", style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private class StartButtonListener implements ActionListener {
        private int threadId;

        public StartButtonListener(int threadId) {
            this.threadId = threadId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        // Dont create new thread, start existing thread
        if (threads[threadId] != null) {
            threads[threadId].start();
        }
    }
    }

    private class StopButtonListener implements ActionListener {
        private int threadId;

        public StopButtonListener(int threadId) {
            this.threadId = threadId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (threads[threadId] != null) {
                threads[threadId].stop();
            }
        }
    }

    public void setThreads(MyLongRunningProcess[] threads) {
        this.threads = threads;
    }
}