package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class QueueContainer {
    private final int containerSize;
    private List<List<ScheduledProcess>> queues;
    public final static int CONSOLE_PRIORITY_QUEUE = 0;
    public final static int IO_PRIORITY_QUEUE = 1;
    public final static int SHORT_PRIORITY_QUEUE = 2;
    public final static int LONG_PRIORITY_QUEUE = 3;
    public QueueContainer(int containerSize) {
        this.containerSize = containerSize;
        initQueues();
    }
    public int maxPriority() {
        return LONG_PRIORITY_QUEUE;
    }
    public static QueueContainer commonContainer() {
        return new QueueContainer(4);
    }

    private void initQueues() {
        queues = new ArrayList<>(containerSize);
        for (int i = 0; i < containerSize; i++) {
            queues.add(new ArrayList<>(containerSize));
        }
    }
    public void enqueue(ScheduledProcess process) {
        this.queues.get(CONSOLE_PRIORITY_QUEUE).add(process);
    }
    public void enqueue(ScheduledProcess process, int priority) {
        this.queues.get(priority).add(process);
    }
    public void addFirst(ScheduledProcess process) {
        this.queues.get(CONSOLE_PRIORITY_QUEUE).add(0, process);
    }
    public Optional<ScheduledProcess> dequeue() {
        for (List<ScheduledProcess> queue : queues) {
            ScheduledProcess p;
            if ((p = getReadyProcess(queue)) != null)
                return Optional.of(p);
        }
        return Optional.empty();
    }

    private ScheduledProcess getReadyProcess(List<ScheduledProcess> queue) {
        for (ScheduledProcess process : queue) {
            if (process.getState()== ScheduledProcess.State.READY) {
                return process;
            }
        }
        return null;
    }

    public void remove(ScheduledProcess process) {
        for (List<ScheduledProcess> queue : queues) {
           queue.remove(process);
        }
    }

    public void applyElapsedTime(int timeElapsed) {
        forEachProcess(p-> p.applyTime(timeElapsed));
    }

    private void forEachProcess(Consumer<ScheduledProcess> action) {
        for (List<ScheduledProcess> queue : queues) {
            for (ScheduledProcess process : queue) {
                action.accept(process);
            }
        }
    }
}
