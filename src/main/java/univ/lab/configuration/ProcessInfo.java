package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.Fillable;

@Fillable(name = "process")
public class ProcessInfo {
    @Fill(attribute = "name")
    private String name;
    @Fill(attribute = "complete-time")
    private Integer runtime;
    @Fill(attribute = "arrive-time")
    private Integer arrive;
    @Fill(attribute = "block-time-average")
    private String blockTime;
    @Fill(attribute = "work-time-average")
    private String workTime;

    @Fill(attribute = "boost")
    private Boolean boost;

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

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public Boolean getBoost() {
        return boost;
    }

    public void setBoost(Boolean boost) {
        this.boost = boost;
    }
}
