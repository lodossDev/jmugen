package fi.iki.asb.fun.hacks;

/**
 * An oscillator.
 */
public class Oscillator {

    /**
     * The minimum value.
     */
    private double min;

    /**
     * Frequency.
     */
    private double frequency;

    /**
     * 2 * Math.PI / frequency
     */
    private double freqDelta;

    /**
     * Amplitudde.
     */
    private double amplitude;

    /**
     * Current value.
     */
    private double current = 0;

    /**
     * Create an oscillator.
     */
    public Oscillator(double frequency, double min, double max) {
        this(frequency, min, max, false);
    }

    /**
     * Create an oscillator.
     */
    public Oscillator(double frequency, double min, double max, boolean rand) {
        this.frequency = frequency;
        this.freqDelta = (2.0 * Math.PI) / frequency;
        this.min = min;
        this.amplitude = (max - min) / 2.0;

        if (rand) {
            randomize();
        }
    }

    // ==================================================================== //

    /**
     * Randomize this oscillator.
     */
    public void randomize() {
        current = Math.random() * frequency * freqDelta;
    }

    /**
     * Get the next value.
     */
    public double next() {
        double value = (Math.sin(current) + 1.0) * amplitude + min;
        current += freqDelta;
        return value;
    }

    // ==================================================================== //

    public static void main(String args[]) {
        Oscillator osc = new Oscillator(32.0, 0.0, 20.0);

        for (int i = 0; i < 32; i++) {
            System.err.println(osc.next());
        }
    }

}
