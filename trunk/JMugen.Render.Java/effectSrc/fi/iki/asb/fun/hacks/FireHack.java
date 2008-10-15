package fi.iki.asb.fun.hacks;

import java.util.Arrays;
import java.util.Random;

/**
 * Classic fire effect.
 */
public class FireHack extends Hack {

    /** The color palette. */
    private int colors[];

    /** The heat factor */
    private int heat;

    /** The current fire pattern in 8.8 fixed point. */
    private int fire[];

    /** Copy of the fire pattern in 8.8 fixed point. */
    private int copy[];

    /** The random number generator. */
    private Random rand = new Random();

    // The offsets.
    private int o1;
    private int o2;
    private int o3;
    private int o4;
    private int o5;
    private int o6;
    private int o7;
    private int o8;

    // ==================================================================== //

    /**
     * Create a new fire hack.
     */
    public FireHack(double heat) {
        setHeat(heat);
    }

    /**
     * Set the heat factor. Values below one are warmer.
     */
    public void setHeat(double heat) {
        this.heat = (int)((1 << 8) * heat);
    }

    // ==================================================================== //

    /**
     * Initialize this hack to the spesified size. Subclasses that override
     * this method should call <tt>super.init(frame, original)</tt> first to
     * get the <tt>frame</tt> and <tt>original</tt> attributes initialized
     * properly.
     */
    public void init() {

	colors = new int[256];

        shadePal(  0,  63,   0,   0,   0,   0,   0,   0); // bla->bla
        shadePal( 64, 111,   0,   0,   0, 255,   0,   0); // bla->red
        shadePal(112, 119, 255,   0,   0, 255, 255,   0); // red->yel
        shadePal(120, 127, 255, 255,   0, 255, 255, 255); // yel->whi
        shadePal(128, 255, 255, 255, 255, 255, 255, 255); // whi->whi

	fire = new int[frame.width * (frame.height + 3)];
	copy = new int[frame.width * (frame.height + 3)];

        o1 = -1;
        o2 = 1;
        o3 = frame.width - 1;
        o4 = frame.width;
        o5 = frame.width + 1;
        o6 = frame.width + o3;
        o7 = frame.width + o4;
        o8 = frame.width + o5;

        for (int i = 0; i < 100; i++) {
            tick();
        }
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        // Optimizations:
        //     - combined the two inner loops
        //     - replaced the add operation in the first loop with ++

        int i, c;
        int offs;
        int heat = this.heat;

        // Add fuel to the fire. The heat values are 8.8 fixed point values
        // (that's why the bit shift is there).
        offs = frame.width * frame.height;
        for (i = frame.width * 2; i > 0; i--) {
            fire[offs++] = (rand.nextInt(224) + 32) << 8;
        }

        offs = 1;
        for (i = frame.height * frame.width - 1; i > 0; i--) {

            // There is too much code in this loop to get a performance
            // boost by unfolding it.

            // Burn!
            c = (fire[offs + o1] +                   fire[offs + o2] +
                 fire[offs + o3] + fire[offs + o4] + fire[offs + o5] +
                 fire[offs + o6] + fire[offs + o7] + fire[offs + o8]) >> 3;

            // Cool the fire a bit as it rises upwards.
            if (c > heat) c -= heat;

            // Copy the filtered color value to the copy array. The xor
            // makes the fire "flicker". Again 8.8 fixed point.
            copy[offs] = c ^ 0x0100;

            // Put the pixel to the frame. Again 8.8 fixed point.
            frame.pixels[offs++] = colors[c >> 8];
        }

        // Flip the arrays.
        int tmp[] = copy;
        copy = fire;
        fire = tmp;
    }

    // ==================================================================== //

    /**
     * This method was written by Alex J. Champandard.
     */
    private void shadePal(int s, int e,
                          int r1, int g1, int b1,
                          int r2, int g2, int b2) {
        int i;
        float k;
        for (i=0; i<=e-s; i++) {
            k = (float)i/(float)(e-s);
            colors[s+i] =
                0xFF000000 |
                ((int)(r1+(r2-r1)*k) << 16) |
                ((int)(g1+(g2-g1)*k) << 8) |
                (int)(b1+(b2-b1)*k);
        }
    }

    // ==================================================================== //

    public static void main(String args[]) {

        long[] time = new long[12];

        FireHack fire = new FireHack(0.72);
        fire.init(new PixelImage(320, 200));

        // DO NOT CHANGE THIS LOOP!
        for (int i = 0; i < time.length; i++) {
            System.out.print(".");
            long start = System.currentTimeMillis();
            for (int j = 0; j < 200; j++) {
                fire.tick();
            }
            long stop = System.currentTimeMillis();
            time[i] = stop - start;
        }

        Arrays.sort(time);

        long total = 0;
        for (int i = 1; i < time.length - 1; i++) {
            total += time[i];
        }
        System.err.println("avg:\t" + (total / (time.length - 2)));
    }
}
