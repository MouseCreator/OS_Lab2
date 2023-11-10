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

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getQuantumDuration() {
        return quantumDuration;
    }

    public void setQuantumDuration(int quantumDuration) {
        this.quantumDuration = quantumDuration;
    }

    public int getBreaks() {
        return breaks;
    }

    public void setBreaks(int breaks) {
        this.breaks = breaks;
    }
}
