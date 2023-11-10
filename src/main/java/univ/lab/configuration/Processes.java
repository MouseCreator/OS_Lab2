package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.FillList;
import univ.lab.lib.fill.Fillable;

import java.util.List;
@Fillable(name = "properties")
public class Processes {
    @FillList(attribute = "process")
    private List<ProcessInfo> processInfoList;

    @Fill(attribute = "properties")
    private SimulationProperties simulationProperties;

    public List<ProcessInfo> getProcessInfoList() {
        return processInfoList;
    }

    public SimulationProperties getSimulationProperties() {
        return simulationProperties;
    }
}
