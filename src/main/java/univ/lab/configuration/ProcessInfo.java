package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.Fillable;

@Fillable(name = "processes")
public class ProcessInfo {
    @Fill(attribute = "name")
    private String name;
    @Fill(attribute = "complete-time")
    private int runtime;
    @Fill(attribute = "arrive-time")
    private int arrive;
    @Fill(attribute = "block-time-average")
    private int averageBlockTime;
    @Fill(attribute = "work-time-average")
    private int averageWorkTime;
    @Fill(attribute = "block-time-deviation")
    private int deviationBlockTime;
    @Fill(attribute = "work-time-deviation")
    private int deviationRunTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getArrive() {
        return arrive;
    }

    public void setArrive(int arrive) {
        this.arrive = arrive;
    }

    public int getAverageBlockTime() {
        return averageBlockTime;
    }

    public void setAverageBlockTime(int averageBlockTime) {
        this.averageBlockTime = averageBlockTime;
    }

    public int getAverageWorkTime() {
        return averageWorkTime;
    }

    public void setAverageWorkTime(int averageWorkTime) {
        this.averageWorkTime = averageWorkTime;
    }

    public int getDeviationBlockTime() {
        return deviationBlockTime;
    }

    public void setDeviationBlockTime(int deviationBlockTime) {
        this.deviationBlockTime = deviationBlockTime;
    }

    public int getDeviationRunTime() {
        return deviationRunTime;
    }

    public void setDeviationRunTime(int deviationRunTime) {
        this.deviationRunTime = deviationRunTime;
    }
}
