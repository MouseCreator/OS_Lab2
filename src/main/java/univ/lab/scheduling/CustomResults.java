package univ.lab.scheduling;

import univ.lab.configuration.SimulationProperties;

import java.util.ArrayList;
import java.util.List;

public class CustomResults {
    private List<ScheduledProcess> processList;
    private int calculationTime;

    private SimulationProperties properties;
    private String title = "Algorithm";

    public void setProcessList(List<ScheduledProcess> processList) {
        this.processList = processList;
    }

    public void setCalculationTime(int calculationTime) {
        this.calculationTime = calculationTime;
    }

    public void setProperties(SimulationProperties properties) {
        this.properties = properties;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public String toResultString() {
        StringBuilder builder = new StringBuilder(title);
        builder.append("Boost frequency: ").append(properties.getBoostFrequency()).append('\n');
        builder.append("Quantum duration: ").append(properties.getQuantumDuration()).append('\n');
        builder.append("Breaks limit: ").append(properties.getBreaks()).append('\n');
        builder.append("Expected Runtime: ").append(properties.getRuntime()).append('\n');
        builder.append("Actual Runtime: ").append(calculationTime).append('\n');
        List<List<String>> table = new ArrayList<>();
        List<String> titleRow = new ArrayList<>();
        titleRow.add("Name");
        titleRow.add("Time_Run");
        titleRow.add("Time_Blocked");
        titleRow.add("IO_Blocked");
        titleRow.add("Boosted_times");
        table.add(titleRow);
        for (ScheduledProcess scheduledProcess : processList) {
            List<String> currentRow = new ArrayList<>();
            currentRow.add(scheduledProcess.getName());
            SPStats spStats = scheduledProcess.getStats();
            currentRow.add(String.valueOf(spStats.getTotalTimeRunning()));
            currentRow.add(String.valueOf(spStats.getTotalTimeBlocked()));
            currentRow.add(String.valueOf(spStats.getTimesBlocked()));
            currentRow.add(String.valueOf(spStats.getTimesBoosted()));
            table.add(currentRow);
        }
        List<Integer> lengths = new ArrayList<>();
        for (int i = 0; i < table.get(0).size(); i++) {
            int maxLen = 0;
            for (List<String> strings : table) {
                maxLen = Math.max(strings.get(i).length() + 4, maxLen);
            }
            lengths.add(maxLen);
        }
        for (List<String> row : table) {
            for (int i = 0; i < lengths.size(); i++) {
                int diff = lengths.get(i) - row.get(i).length();
                builder.append(row.get(i)).append(" ".repeat(diff % 4)).append("\t".repeat(diff / 4));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
