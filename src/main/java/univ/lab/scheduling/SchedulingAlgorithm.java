package univ.lab.scheduling;

import univ.lab.ontko.Results;

import java.util.Vector;

public interface SchedulingAlgorithm<T> {
    Results run(int runtime, Vector<T> processVector, Results result);
}
