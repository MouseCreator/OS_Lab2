package univ.lab.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class QueueContainer {
    private final int containerSize;
    private List<RoundRobinScheduler> queues;
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
        queues.add(new RoundRobinScheduler(CONSOLE_PRIORITY_QUEUE, 1));
        queues.add(new RoundRobinScheduler(IO_PRIORITY_QUEUE, 2));
        queues.add(new RoundRobinScheduler(SHORT_PRIORITY_QUEUE, 4));
        queues.add(new RoundRobinScheduler(LONG_PRIORITY_QUEUE, 8));
    }
    public void register(ScheduledProcess process) {
        this.queues.get(CONSOLE_PRIORITY_QUEUE).registerProcess(process);
    }
    public void enqueue(RunningProcess process) {
        int maxBreaks = 2;
        if (process.usedProvidedQuantum()) {
            if (process.getBreaks() >= maxBreaks) {
                changeProcessPriority(process);
            } else {
                process.addBreak();
            }
        }
        queues.get(process.getCurrentPriority()).enqueue(process);
    }

    private void changeProcessPriority(RunningProcess runningProcess) {
        int priority = runningProcess.getCurrentPriority();
        if (priority != LONG_PRIORITY_QUEUE) {
            runningProcess.setCurrentPriority(priority+1);
        }
        runningProcess.resetBreaks();
    }

    public void addFirst(ScheduledProcess process) {
       // add first to this.queues.get(CONSOLE_PRIORITY_QUEUE)
    }
    public Optional<RunningProcess> dequeue() {
        for (RoundRobinScheduler queue : queues) {
            Optional<RunningProcess> p;
            if ((p = queue.getNextProcess()).isPresent())
                return p;
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

    public void remove(RunningProcess process) {
        for (RoundRobinScheduler roundRobinScheduler : queues) {
           roundRobinScheduler.remove(process);
        }
    }

    public void applyElapsedTime(int timeElapsed) {
        forEachProcess(p-> p.getScheduledProcess().applyTime(timeElapsed));
    }

    private void forEachProcess(Consumer<RunningProcess> action) {
        for (RoundRobinScheduler roundRobinScheduler : queues) {
            for (RunningProcess process : roundRobinScheduler.eachProcess()) {
                action.accept(process);
            }
        }
    }

}
