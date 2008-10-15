package fi.iki.asb.fun.hacks;

/**
 * A hack which adjusts the brightness of the texture.
 */
public class FadeHack extends Hack {

    private static final int FIXEDP = 7;

    /**
     * The brightness adjustment.
     */
    private int brightness = 1 << FIXEDP;

    // ==================================================================== //

    /**
     * Set the brightness adjustment.
     */
    public void setBrightness(double brightness) {
        this.brightness = (int)((1 << FIXEDP) * brightness);
    }

    // ==================================================================== //

    /**
     * Generate the next frame.
     */
    public void tick() {

        int[] pixels = frame.pixels;

        for (int i = pixels.length - 1; i >= 0; i--) {
            int pixel = pixels[i];

            int r = (((pixel >> 16) & 0xFF) * brightness) >> FIXEDP;
            if (r > 0xFF) r = 0xFF;

            int g = (((pixel >> 8) & 0xFF) * brightness) >> FIXEDP;
            if (g > 0xFF) g = 0xFF;

            int b = ((pixel & 0xFF) * brightness) >> FIXEDP;
            if (b > 0xFF) b = 0xFF;

            pixels[i]
                = (pixel & 0xFF000000)
                | (r << 16) | (g << 8) | b;
        }
    }

}
