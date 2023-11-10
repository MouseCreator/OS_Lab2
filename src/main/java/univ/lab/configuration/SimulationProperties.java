package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.Fillable;

@Fillable(name = "properties")
public class SimulationProperties {
    @Fill(attribute = "runtime")
    private int runtime;
    @Fill(attribute = "quantum")
    private int quantumDuration;
}
