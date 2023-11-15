package univ.lab.configuration;

import univ.lab.lib.fill.Fill;
import univ.lab.lib.fill.Fillable;

@Fillable(name = "configuration")
public class SimulationProperties {
    @Fill(attribute = "runtime")
    private int runtime;
    @Fill(attribute = "quantum")
    private int quantumDuration;
    @Fill(attribute = "breaks")
    private int breaks;
    @Fill(attribute = "boost_freq")
    private int boostFrequency;

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getQuantumDuration() {
        return quantumDuration;
    }

    public int getBreaks() {
        return breaks;
    }
    public int getBoostFrequency() {
        return boostFrequency;
    }
}
