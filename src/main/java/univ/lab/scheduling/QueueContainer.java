package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueContainer {
    private final int containerSize;
    private List<Queue<ScheduledProcess>> queues;
    private final static int CONSOLE_PRIORITY_QUEUE = 0;
    private final static int IO_PRIORITY_QUEUE = 1;
    private final static int SHORT_PRIORITY_QUEUE = 2;
    private final static int LONG_PRIORITY_QUEUE = 3;

    private int maxTimeBreaks = 1;
    public QueueContainer(int containerSize) {
        this.containerSize = containerSize;
        initQueues();
    }

    private void initQueues() {
        queues = new ArrayList<>(containerSize);
        for (int i = 0; i < containerSize; i++) {
            queues.add(new ArrayBlockingQueue<>(containerSize));
        }
    }

    public void enqueue(ScheduledProcess process) {
        process.setTimesBreak(0);
        process.setCurrentPriority(CONSOLE_PRIORITY_QUEUE);
        addProcess(process);
    }

    private void addProcess(ScheduledProcess process) {
        queues.get(process.getCurrentPriority()).add(process);
    }

    private void addProcess(ScheduledProcess process, int priority) {
        queues.get(priority).add(process);
        process.setCurrentPriority(priority);
    }

    public void enqueueAndModify(ScheduledProcess process) {
        int priority = process.getCurrentPriority();
        int timesBreak = process.getTimesBreak();
        if (timesBreak >= maxTimeBreaks) {
            priority = Math.min(LONG_PRIORITY_QUEUE, ++priority);
            process.setTimesBreak(0);
        } else {
            process.setTimesBreak(timesBreak+1);
        }
        addProcess(process, priority);
    }
    public Optional<ScheduledProcess> dequeue() {
        for (Queue<ScheduledProcess> queue : queues) {
            if (queue.isEmpty())
                continue;
            return Optional.of(queue.poll());
        }
        return Optional.empty();
    }

    public int getMaxTimeBreaks() {
        return maxTimeBreaks;
    }
    private void remove(ScheduledProcess process) {
        for (Queue<ScheduledProcess> queue : queues) {
           queue.remove(process);
        }
    }
    public void processBoost(ScheduledProcess process) {
        remove(process);
        enqueue(process);
    }

    public void setMaxTimeBreaks(int maxTimeBreaks) {
        this.maxTimeBreaks = maxTimeBreaks;
    }
}
