package univ.lab.scheduling;

import java.io.PrintStream;
import java.util.Optional;

public interface QueueContainer {
    int maxPriority();
    void register(ScheduledProcess process);
    void enqueue(RunningProcess process);
    Optional<RunningProcess> dequeue();
    void applyElapsedTime(int timeElapsed);
    void boost(PrintStream outStream);
}
