package univ.lab.scheduling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SmartQueueContainer implements QueueContainer {
    private List<SimpleScheduler> queues;
    public final static int CONSOLE_PRIORITY_QUEUE = 0;
    public final static int IO_PRIORITY_QUEUE = 1;
    public final static int SHORT_PRIORITY_QUEUE = 2;
    public final static int LONG_PRIORITY_QUEUE = 3;
    private final int maxBreaks;
    private final List<RunningProcess> blockedProcesses;
    public SmartQueueContainer(int maxBreaks) {
        this.maxBreaks = maxBreaks;
        blockedProcesses = new ArrayList<>();
        initQueues();
    }
    public int maxPriority() {
        return LONG_PRIORITY_QUEUE;
    }
    public static SmartQueueContainer commonContainer(int maxBreaks) {
        return new SmartQueueContainer(maxBreaks);
    }

    private void initQueues() {
        queues = new ArrayList<>(4);
        queues.add(new SimpleRoundRobinScheduler(CONSOLE_PRIORITY_QUEUE, 1));
        queues.add(new SimpleRoundRobinScheduler(IO_PRIORITY_QUEUE, 2));
        queues.add(new SimpleRoundRobinScheduler(SHORT_PRIORITY_QUEUE, 4));
        queues.add(new SimpleRoundRobinScheduler(LONG_PRIORITY_QUEUE, 8));

    }
    public void register(ScheduledProcess process) {
        this.queues.get(CONSOLE_PRIORITY_QUEUE).registerProcess(process);
    }
    public void enqueue(RunningProcess process) {
        if (process.isBlocked()) {
            blockedProcesses.add(process);
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
        rescheduleBlockedProcesses();
    }

    private void rescheduleBlockedProcesses() {
        List<RunningProcess> unblocked = new ArrayList<>();
        for (RunningProcess process : blockedProcesses) {
            if (process.isReady()) {
                unblocked.add(process);
            }
        }
        blockedProcesses.removeAll(unblocked);
        for (RunningProcess process : unblocked) {
            if (process.getBoosted()) {
                queues.get(IO_PRIORITY_QUEUE).enqueueFirst(process);
                process.setBoosted(false);
            } else {
                queues.get(process.getCurrentPriority()).enqueue(process);
            }
        }
    }

    private void forEachProcess(Consumer<RunningProcess> action) {
        for (RunningProcess process : blockedProcesses){
            action.accept(process);
        }
        for (SimpleScheduler roundRobinScheduler : queues) {
            for (RunningProcess process : roundRobinScheduler.eachProcess()) {
                action.accept(process);
            }
        }
    }

    public void boost(PrintStream outStream) {
        for (RunningProcess process : blockedProcesses) {
            if (process.getScheduledProcess().isToBoost()) {
                boostProcess(process);
                outStream.println(process.getScheduledProcess().getName() + " was boosted! (waiting...)");
                process.setBoosted(true);
            }
        }
        List<RunningProcess> processesToBoost = new ArrayList<>();
        for (SimpleScheduler roundRobinScheduler : queues) {
            processesToBoost.addAll(roundRobinScheduler.removeBoostable());
        }
        for (RunningProcess process : processesToBoost) {
            boostProcess(process);
            outStream.println(process.getScheduledProcess().getName() + " was boosted!");
            queues.get(IO_PRIORITY_QUEUE).enqueueFirst(process);
        }
    }

    private void boostProcess(RunningProcess process) {
        process.setCurrentPriority(IO_PRIORITY_QUEUE);
        process.getScheduledProcess().getStats().addBoosted();
        process.resetBreaks();
    }
}
