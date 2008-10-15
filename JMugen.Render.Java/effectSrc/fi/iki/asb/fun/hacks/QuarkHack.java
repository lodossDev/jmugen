package fi.iki.asb.fun.hacks;


/**
 * A hack which adjusts the brightness of the texture.
 */
public class QuarkHack extends Hack {

    /**
     * Last 8 frames.
     */
    private PixelImage[] history;

    private PixelImage source;

    private MersenneTwisterFast rand = new MersenneTwisterFast();

    private int index = 0;

    // ==================================================================== //

    /**
     * Create a new quark hack.
     *
     * @param source the frame source.
     */
    public QuarkHack(int frameCount, PixelImage source) {
        this.source = source;

        /**
         * Create the frame history.
         */
        history = new PixelImage[frameCount];
        for (int i = 0; i < history.length; i++) {
            history[i] = new PixelImage(source);
        }
    }

    // ==================================================================== //

    /**
     * Generate the next frame.
     */
    public void tick() {

        // Get the current frame from the array.
        index = (index + 1) % history.length;
        PixelImage current = history[index];

        // Copy the frame from the source to the current frame.
        System.arraycopy(source.pixels, 0,
                         current.pixels, 0,
                         source.pixels.length);

        // Generate the frame.
        for (int i = 0; i < frame.pixels.length; i++) {
            frame.pixels[i] = history[rand.nextInt(history.length)].pixels[i];
        }
    }
}
