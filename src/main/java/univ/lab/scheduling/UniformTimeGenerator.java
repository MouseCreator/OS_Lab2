package univ.lab.scheduling;

import java.util.Random;

public class UniformTimeGenerator implements TimeGenerator {
    private final int deviation;
    private final int meanValue;
    private final Random random;

    public UniformTimeGenerator(int deviation, int meanValue) {
        this.deviation = deviation;
        this.meanValue = meanValue;
        random = new Random();
    }

    public int generateTime() {
        if (deviation == 0) {
            return meanValue;
        }
        return meanValue + random.nextInt(meanValue-deviation, meanValue+deviation+1);
    }
}
