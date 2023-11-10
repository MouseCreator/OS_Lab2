package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.Fillable;

@Fillable(name = "processes")
public class ProcessInfo {
    @Fill(attribute = "complete-time")
    private int runtime;
    @Fill(attribute = "block-time-average")
    private int averageBlockTime;
    @Fill(attribute = "work-time-average")
    private int averageWorkTime;
    @Fill(attribute = "block-time-deviation")
    private int deviationBlockTime;
    @Fill(attribute = "work-time-deviation")
    private int deviationRunTime;
}
