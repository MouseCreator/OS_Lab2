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
            process.addBreak();
            if (process.getBreaks() >= maxBreaks) {
                changeProcessPriority(process);
                queues.get(process.getCurrentPriority()).registerProcess(process.getScheduledProcess());
                return;
            }
        }
        queues.get(process.getCurrentPriority()).enqueue(process);
    }

    private void changeProcessPriority(RunningProcess runningProcess) {
        int priority = runningProcess.getCurrentPriority();
        if (priority != maxPriority()) {
            runningProcess.setCurrentPriority(priority+1);
        }
        runningProcess.resetBreaks();
    }
    public Optional<RunningProcess> dequeue() {
        for (RoundRobinScheduler queue : queues) {
            Optional<RunningProcess> p;
            if ((p = queue.getNextProcess()).isPresent())
                return p;
        }
        return Optional.empty();
    }

    public void applyElapsedTime(int timeElapsed) {
        forEachProcess(p-> {
            var s = p.getScheduledProcess();
            s.applyTime(timeElapsed);
        });
    }

    private void forEachProcess(Consumer<RunningProcess> action) {
        for (RoundRobinScheduler roundRobinScheduler : queues) {
            for (RunningProcess process : roundRobinScheduler.eachProcess()) {
                action.accept(process);
            }
        }
    }

    public void boost() {
        List<RunningProcess> processesToBoost = new ArrayList<>();
        for (RoundRobinScheduler roundRobinScheduler : queues) {
            processesToBoost.addAll(roundRobinScheduler.removeBoostable());
        }
        for (RunningProcess process : processesToBoost) {
            process.setCurrentPriority(CONSOLE_PRIORITY_QUEUE);
            queues.get(CONSOLE_PRIORITY_QUEUE).enqueueFirst(process);
        }
    }
}
