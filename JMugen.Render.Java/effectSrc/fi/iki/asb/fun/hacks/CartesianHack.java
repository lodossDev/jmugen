package fi.iki.asb.fun.hacks;


/**
 * Map an image from cartesian to cartesian coordinates.
 */
public class CartesianHack extends Hack {

    /**
     * The original texture.
     */
    private PixelImage texture;

    private int[] toCartesian;

    private int[] columns;
    private int[] rows;

    // ==================================================================== //

    /**
     * Create a tunnel hack.
     *
     * @param texture the texture
     * @param topdown read texture from top to bottom or bottom to top
     */
    public CartesianHack(PixelImage texture) {
        this.texture = texture;
    }

    // ==================================================================== //

    /**
     *
     */
    public void init() {

        columns = new int[frame.height * frame.width];
        rows = new int[frame.height * frame.width];

        int xmax = frame.width / 2;
        int ymax = frame.height / 2;

        // Calculate the angle of the line from the center of the screen
        // to each pixel on the screen. This angle maps the pixels on the
        // screen to pixel columns on the texture.

        double angle; // The angle in radians.
        int index = 0;
        for (int y = 0; y < frame.height; y++) {
            for (int x = 0; x < frame.width; x++) {
                int x1 = x - xmax;
                int y1 = y - ymax;

                angle = (Math.atan2(x1, y1) / (Math.PI * 2) + 0.5);
                columns[index] = ((int)(angle * texture.width)) % texture.width;
                rows[index++] = ((int)Math.sqrt((x1 * x1) + (y1 * y1))) % texture.height;
            }
        }
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        // Map a pixel to a row and column in the texture.
        // Copy the pixel from the texture to the frame buffer.
        int tmp;
        int index = frame.pixels.length;
        while (index > 0) {
            int x = columns[--index];
            int y = rows[index];
            frame.pixels[index] = texture.pixels[y * texture.width + x];
        }
    }
}
