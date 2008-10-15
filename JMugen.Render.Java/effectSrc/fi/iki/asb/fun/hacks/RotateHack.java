package fi.iki.asb.fun.hacks;

/**
 * A base class for graphics hacks. All hack objects draw their pixels
 * to a PixelImage.
 */
public class RotateHack extends Hack {

    private static final int FIXEDP = 8;

    private static final double PI2 = Math.PI / 2.0;

    private static final double MUL = (double)(1 << FIXEDP);

    /**
     * The original texture.
     */
    public PixelImage texture;

    /**
     * The rotation angle in radians.
     */
    private double rotation = 0;

    /**
     * The horizontal scroll amount.
     */
    private int scroll_x = 0;

    /**
     * The vertical scroll amount.
     */
    private int scroll_y = 0;

    private double scale = 1.0;

    // ==================================================================== //

    /**
     * Create a rotate hack.
     *
     * @param texture the texture.
     */
    public RotateHack(PixelImage texture) {
        this.texture = texture;
    }

    // ==================================================================== //

    /**
     * Get the rotation.
     */
    public double getRotation() {
        return this.rotation;
    }

    /**
     * Set the rotation in radians.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public int getScrollX() {
        return this.scroll_x >> FIXEDP;
    }

    public void setScrollX(int scroll_x) {
        this.scroll_x = scroll_x << FIXEDP;
    }

    public int getScrollY() {
        return this.scroll_y >> FIXEDP;
    }

    public void setScrollY(int scroll_y) {
        this.scroll_y = scroll_y << FIXEDP;
    }

    public double getScale() {
        return 1.0 / this.scale;
    }

    public void setScale(double scale) {
        this.scale = 1.0 / scale;
    }

    /**
     * Called automatically by <code>init(PixelImage)</code>. Subclasses
     * should implement this and do their initialization here.
     */
    public void init() {
        setScrollX(texture.width >> 1);
        setScrollY(texture.height >> 1);
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        int th = texture.height;
        int tw = texture.width;
        int fh = frame.height;
        int fw = frame.width;
 
        // Calculate the size of the step to be taken in the source image when
        // the drawing advances from column to column in the result image.
        int scanline_csx = (int)(Math.cos(rotation) * MUL * scale);
        int scanline_csy = (int)(Math.sin(rotation) * MUL * scale);

        // And the size of the step when advancing from row to row.
        int scanline_rsx = (int)(Math.cos(rotation + PI2) * MUL * scale);
        int scanline_rsy = (int)(Math.sin(rotation + PI2) * MUL * scale);

        // Calculate the start location of the first scanline.
        int scanline_x = scroll_x - (scanline_csx * (fw >> 1))
            - (scanline_rsx * (fh >> 1));
        int scanline_y = scroll_y - (scanline_csy * (fw >> 1))
            - (scanline_rsy * (fh >> 1));

        int row_ix, col_ix;
        int row_iy, col_iy;

        int index = 0;
        for (int yi = 0; yi < fh; yi++) {

            // Take a copy from the start of scanline to temporary variables.
            row_ix = scanline_x;
            row_iy = scanline_y;

            // Advanvce the "start of scanline"
            scanline_x += scanline_rsx;
            scanline_y += scanline_rsy;

            for (int xi = 0; xi < fw; xi++) {
                row_ix += scanline_csx;
                col_ix = (row_ix >> FIXEDP) % tw;
                if (col_ix < 0) col_ix += tw;

                row_iy += scanline_csy;
                col_iy = (row_iy >> FIXEDP) % th;
                if (col_iy < 0) col_iy += th;

                int texel = texture.pixels[col_ix + col_iy * tw];
                if ((texel & 0xFF000000) == 0xFF000000) {
                    frame.pixels[index++] = texel;
                } else {
                    index++;
                }
            }

        }
    }

}
