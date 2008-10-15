package fi.iki.asb.fun.hacks;


/**
 * A base class for graphics hacks. All hack objects draw their pixels
 * to a PixelImage.
 */
public class LensHack extends Hack {

    private int[][] lens;

    private Oscillator xo, yo;

    private int lensWidth;

    private int lensZoom = 30;

    private PixelImage background;

    public LensHack(int width, PixelImage background) {
        lens = new int[width][width];
        lensWidth = width;
        this.background = background;
    }

    /**
     * Called automatically by <code>init(PixelImage)</code>. Subclasses
     * should implement this and do their initialization here.
     */
    public void init() {

        xo = new Oscillator(frame.width * (Math.random() + 0.5),
                            0, frame.width - lensWidth, true);
        yo = new Oscillator(frame.height * (Math.random() + 0.5),
                            0, frame.height - lensWidth, true);

        int r = lensWidth/2;
        int d = lensZoom;

        /* it is sufficient to generate 1/4 of the lens and reflect this
         * around; a sphere is mirrored on both the x and y axes */
        for (int y = 0; y < lensWidth >> 1; y++) {
            for (int x = 0; x < lensWidth >> 1; x++) {
                int ix, iy, offset;
                if ((x * x + y * y) < (r * r)) {
                    double shift = d / Math.sqrt(d*d - (x*x + y*y - r*r));
                    ix = (int)(x * shift - x);
                    iy = (int)(y * shift - y);
                } else {
                    ix = 0;
                    iy = 0;
                }

                offset = (iy * frame.width + ix);
                lens[lensWidth/2 - y][lensWidth/2 - x] = -offset;
                lens[lensWidth/2 + y][lensWidth/2 + x] = offset;
                offset = (-iy * frame.width + ix);
                lens[lensWidth/2 + y][lensWidth/2 - x] = -offset;
                lens[lensWidth/2 - y][lensWidth/2 + x] = offset;
            }
        }
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        System.arraycopy(background.pixels, 0,
                         frame.pixels, 0,
                         frame.pixels.length);

        int ox = (int)xo.next();
        int oy = (int)yo.next();

        for (int y = 0; y < lensWidth; y++) {
            int pos = (y + oy) * frame.width + ox;
            for (int x = 0; x < lensWidth; x++) {
                frame.pixels[pos] = background.pixels[pos + lens[y][x]];
                pos++;
            }
        }
    }
}
