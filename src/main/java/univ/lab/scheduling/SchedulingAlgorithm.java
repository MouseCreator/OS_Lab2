package univ.lab.scheduling;

import univ.lab.ontko.Results;
import univ.lab.ontko.SProcess;

import java.util.Vector;

public interface SchedulingAlgorithm {
    Results run(int runtime, Vector<SProcess> processVector, Results result);
}
