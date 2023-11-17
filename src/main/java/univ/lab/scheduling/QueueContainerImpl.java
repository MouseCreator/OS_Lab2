package univ.lab.scheduling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class QueueContainerImpl implements QueueContainer {
    private List<SimpleScheduler> queues;
    public final static int BOOSTED_QUEUE = -1;
    public final static int CONSOLE_PRIORITY_QUEUE = 0;
    public final static int IO_PRIORITY_QUEUE = 1;
    public final static int SHORT_PRIORITY_QUEUE = 2;
    public final static int LONG_PRIORITY_QUEUE = 3;
    private final int maxBreaks;
    private SimpleScheduler boostedList;
    public QueueContainerImpl(int maxBreaks) {
        this.maxBreaks = maxBreaks;
        initQueues();
    }
    public int maxPriority() {
        return LONG_PRIORITY_QUEUE;
    }
    public static QueueContainerImpl commonContainer(int maxBreaks) {
        return new QueueContainerImpl(maxBreaks);
    }

    private void initQueues() {
        queues = new ArrayList<>(4);
        queues.add(new SimpleRoundRobinScheduler(CONSOLE_PRIORITY_QUEUE, 1));
        queues.add(new SimpleRoundRobinScheduler(IO_PRIORITY_QUEUE, 2));
        queues.add(new SimpleRoundRobinScheduler(SHORT_PRIORITY_QUEUE, 4));
        queues.add(new SimpleRoundRobinScheduler(LONG_PRIORITY_QUEUE, 8));

        boostedList = new SimpleRoundRobinScheduler(BOOSTED_QUEUE, 1);
    }
    public void register(ScheduledProcess process) {
        this.queues.get(CONSOLE_PRIORITY_QUEUE).registerProcess(process);
    }
    public void enqueue(RunningProcess process) {
        if (process.getCurrentPriority() == BOOSTED_QUEUE) {
            if (process.isBlocked()) {
                changeProcessPriority(process);
                queues.get(process.getCurrentPriority()).registerProcess(process.getScheduledProcess());
                return;
            }
            if (process.usedProvidedQuantum()) {
                process.addBreak();
                if (process.getBreaks() >= maxBreaks) {
                    changeProcessPriority(process);
                    queues.get(process.getCurrentPriority()).registerProcess(process.getScheduledProcess());
                    return;
                }
            }
            boostedList.enqueue(process);
            return;
        }
        if (process.isBlocked()) {
            queues.get(process.getCurrentPriority()).enqueue(process);
            return;
        }
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
        Optional<RunningProcess> boosted = boostedList.getNextProcess();
        if (boosted.isPresent())
            return boosted;
        for (SimpleScheduler queue : queues) {
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
        for (RunningProcess process : boostedList.eachProcess()){
            action.accept(process);
        }
        for (SimpleScheduler roundRobinScheduler : queues) {
            for (RunningProcess process : roundRobinScheduler.eachProcess()) {
                action.accept(process);
            }
        }
    }

    public void boost(PrintStream outStream) {
        List<RunningProcess> processesToBoost = new ArrayList<>();
        for (SimpleScheduler roundRobinScheduler : queues) {
            processesToBoost.addAll(roundRobinScheduler.removeBoostable());
        }
        for (RunningProcess process : processesToBoost) {
            process.setCurrentPriority(BOOSTED_QUEUE);
            process.getScheduledProcess().getStats().addBoosted();
            outStream.println(process.getScheduledProcess().getName() + " was boosted!");
            boostedList.enqueueFirst(process);
        }
    }
}
