package fi.iki.asb.fun.hacks;

/**
 * A static image.
 */
public class StaticImageHack extends Hack {

    private PixelImage image;

    // ==================================================================== //

    /**
     * Create a new fire hack.
     */
    public StaticImageHack(PixelImage image) {
        this.image = image;
    }

    // ==================================================================== //

    public void tick() {
        System.arraycopy(image.pixels, 0, frame.pixels, 0,
                         image.pixels.length);
    }

}
