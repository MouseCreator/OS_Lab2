package univ.lab.scheduling;


import java.util.List;
import java.util.Optional;

public interface SimpleScheduler {
    Optional<RunningProcess> getNextProcess();
    void registerProcess(ScheduledProcess process);
    void enqueue(RunningProcess process);
    int getMaxQuantum();
    int getPriority();
    List<RunningProcess> eachProcess();
    List<RunningProcess> removeBoostable();
    void enqueueFirst(RunningProcess process);
}
