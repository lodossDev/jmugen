package fi.iki.asb.fun.hacks;


/**
 * Zoom the frame.
 */
public class ZoomHack extends Hack {

    private static final int FIXEDP = 15;

    /**
     * Temporary storage.
     */
    private PixelImage zoomed;

    /**
     * Zoom center.
     */
    private int x, y;

    /**
     * Deltas for each pixel.
     */
    private double zx, zy;

    private double zstep;
    private double zwidth, zheight;

    // ==================================================================== //

    /**
     * Set zoom.
     */
    public void setZoom(double zoom) {

        zwidth = frame.width / zoom;
        zheight = frame.height / zoom;

        zx = (frame.width - zwidth) / 2.0;
        zy = (frame.height - zheight) / 2.0;

        zstep = (zwidth) / frame.width;
    }

    /**
     * Called automatically by <code>init(PixelImage)</code>. Subclasses
     * should implement this and do their initialization here.
     */
    public void init() {
        this.zoomed = new PixelImage(frame);
        this.x = frame.width / 2;
        this.y = frame.height / 2;
    }

    // ==================================================================== //

    /**
     *
     */
    public void tick() {

        int findex = 0;
        int zindex = 0;
        for (int y = 0; y < zoomed.height; y++) {
            findex = ((int)(zy + (y * zstep))) * frame.width;
            for (int x = 0; x < zoomed.width; x++) {
                zoomed.pixels[zindex++] =
                    frame.pixels[findex + ((int)(zx + (x * zstep)))];
            }
        }

        System.arraycopy(zoomed.pixels, 0,
                         frame.pixels, 0,
                         frame.pixels.length);
    }
}
