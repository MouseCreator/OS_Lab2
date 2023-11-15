package univ.lab.scheduling;

import java.util.List;

public class RoundRobinScheduler {
    private List<ScheduledProcess> processList;
    private List<ScheduledProcess> timeoutProcesses;
    private int quantumNumber = 0;
    private ScheduledProcess currentProcess;
    public RoundRobinScheduler() {
    }

    public void updateState(List<ScheduledProcess> list, int quantumNumber) {
        this.processList = list;
        this.quantumNumber = quantumNumber;
    }
    enum RRState {
        DONE,
    }
    public RRState run() {
        if (currentProcess == null) {
            if (processList == null) {
                throw new IllegalStateException("Round-robin scheduler is not initialized with list of processes");
            }
            if (processList.isEmpty()) {
                return RRState.DONE;
            }
            currentProcess = processList.remove(0);
        }
        currentProcess.nextTick();
        ScheduledProcess.State processState = currentProcess.getState();
        switch (processState) {
            case TIMEOUT -> {
                currentProcess.setState(ScheduledProcess.State.READY);
                if (currentProcess.getTimeouts() < quantumNumber) {
                    processList.add(currentProcess);
                } else {
                    timeoutProcesses.add(currentProcess);
                }
            }
            case BLOCKED -> processList.add(currentProcess);
            case TERMINATED -> currentProcess = null;

        }
        return RRState.DONE;
    }
}
