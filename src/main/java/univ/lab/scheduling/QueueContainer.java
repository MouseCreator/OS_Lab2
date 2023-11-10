package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

public class QueueContainer {
    private final int containerSize;
    private List<Queue<ScheduledProcess>> queues;
    public final static int CONSOLE_PRIORITY_QUEUE = 0;
    public final static int IO_PRIORITY_QUEUE = 1;
    public final static int SHORT_PRIORITY_QUEUE = 2;
    public final static int LONG_PRIORITY_QUEUE = 3;
    private int quantumDuration;
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
        changePriority(process, CONSOLE_PRIORITY_QUEUE);
        addProcess(process);
    }

    public void enqueueAndModify(ScheduledProcess process) {
        if (isProcessUsedFullQuantum(process)) {
            lowerPriorityForProcess(process);
        }
        addProcess(process);
    }
    public Optional<ScheduledProcess> dequeue() {
        for (Queue<ScheduledProcess> queue : queues) {
            if (queue.isEmpty())
                continue;
            return Optional.of(queue.poll());
        }
        return Optional.empty();
    }
    public void processBoost(ScheduledProcess process) {
        remove(process);
        enqueue(process);
    }

    private void addProcess(ScheduledProcess process) {
        queues.get(process.getCurrentPriority()).add(process);
    }

    private void addProcess(ScheduledProcess process, int priority) {
        queues.get(priority).add(process);
        process.setCurrentPriority(priority);
    }

    private void lowerPriorityForProcess(ScheduledProcess process) {
        int priority = process.getCurrentPriority();
        int timesBreak = process.getTimesBreak();
        if (timesBreak >= maxTimeBreaks) {
            priority = Math.min(LONG_PRIORITY_QUEUE, ++priority);
            process.setTimesBreak(0);
            changePriority(process, priority);
        } else {
            process.setTimesBreak(timesBreak+1);
        }
    }

    private void changePriority(ScheduledProcess process, int priority) {
        process.setCurrentPriority(priority);
        int quantum = switch (priority) {
            case CONSOLE_PRIORITY_QUEUE -> quantumDuration;
            case IO_PRIORITY_QUEUE -> quantumDuration * 2;
            case SHORT_PRIORITY_QUEUE -> quantumDuration * 3;
            case LONG_PRIORITY_QUEUE -> quantumDuration * 4;
            default -> throw new IllegalArgumentException("Unexpected priority: " + priority);
        };
        process.setMaxQuantum(quantum);
    }

    private boolean isProcessUsedFullQuantum(ScheduledProcess process) {
        return process.getMaxQuantum() <= process.getCurrentQuantum();
    }


    private void remove(ScheduledProcess process) {
        for (Queue<ScheduledProcess> queue : queues) {
           queue.remove(process);
        }
    }
    public int getMaxTimeBreaks() {
        return maxTimeBreaks;
    }
    public void setMaxTimeBreaks(int maxTimeBreaks) {
        this.maxTimeBreaks = maxTimeBreaks;
    }

    public int getQuantumDuration() {
        return quantumDuration;
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
    }

    public void applyElapsedTime(int timeElapsed) {
        forEachProcess(p-> {
            p.applyTime(timeElapsed);
        });
    }

    private void forEachProcess(Consumer<ScheduledProcess> action) {
        for (Queue<ScheduledProcess> queue : queues) {
            for (ScheduledProcess process : queue) {
                action.accept(process);
            }
        }
    }
}
