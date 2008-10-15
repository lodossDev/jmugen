package fi.iki.asb.fun.hacks;


/**
 * Motion blur hack.
 */
public class MotionBlurHack extends Hack {

    /**
     * Last 8 frames.
     */
    private PixelImage[] history;

    private int[] red;
    private int[] green;
    private int[] blue;

    private PixelImage source;

    private int index = 0;

    private int shift;

    // ==================================================================== //

    /**
     * Create a new motion blur hack.
     *
     * @param source the frame source.
     */
    public MotionBlurHack(int frameCount, PixelImage source) {
        this.source = source;

        switch (frameCount) {
        case 2: shift = 1; break;
        case 4: shift = 2; break;
        case 8: shift = 3; break;
        case 16: shift = 4; break;
        case 32: shift = 5; break;
        case 64: shift = 6; break;
        default:
            throw new IllegalArgumentException("frameCount must be a power "
                                               + "of 2 between 2 and 64");
        }

        // Create the frame history.
        history = new PixelImage[frameCount];
        for (int i = 0; i < history.length; i++) {
            history[i] = new PixelImage(source);
        }

        // Create the sums of the pixels in the eight (identical) frames.
        red = new int[source.pixels.length];
        green = new int[source.pixels.length];
        blue = new int[source.pixels.length];
        for (int i = 0; i < source.pixels.length; i++) {
            red[i] = ((source.pixels[i] >> 16) & 0xFF) * frameCount;
            green[i] = ((source.pixels[i] >> 8) & 0xFF) * frameCount;
            blue[i] = ((source.pixels[i] >> 0) & 0xFF) * frameCount;
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

        // Substract the r/g/b values of the oldest frame from the sums.
        for (int i = 0; i < frame.pixels.length; i++) {
            red[i] -= ((current.pixels[i] >> 16) & 0xFF);
            green[i] -= ((current.pixels[i] >> 8) & 0xFF);
            blue[i] -= (current.pixels[i] & 0xFF);
        }

        // Copy the frame from the source to the current frame.
        System.arraycopy(source.pixels, 0,
                         current.pixels, 0,
                         source.pixels.length);

        // Generate the frame and add the new pixel values to the sums.
        for (int i = 0; i < frame.pixels.length; i++) {
            red[i] += ((current.pixels[i] >> 16) & 0xFF);
            green[i] += ((current.pixels[i] >> 8) & 0xFF);
            blue[i] += (current.pixels[i] & 0xFF);

            frame.pixels[i]
                = 0xFF000000
                | ((red[i] >> shift) << 16)
                | ((green[i] >> shift) << 8)
                | (blue[i] >> shift);
        }
    }
}
