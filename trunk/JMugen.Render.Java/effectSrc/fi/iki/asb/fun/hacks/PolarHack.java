package fi.iki.asb.fun.hacks;


/**
 * Map an image from cartesian to polar coordinates.
 */
public class PolarHack extends Hack {

    /**
     * The original texture.
     */
    private PixelImage texture;

    /**
     * Conversion table to convert from cartesian to polar. Index in this
     * array represents an index in the source texture. 16 high bits represent
     * the column in the polar vcoordinate system and lower 16 the row.
     */
    private int[] toPolar;

    // ==================================================================== //

    /**
     * Create a tunnel hack.
     *
     * @param texture the texture
     */
    public PolarHack(PixelImage texture) {
        this.texture = texture;
    }

    // ==================================================================== //

    /**
     *
     */
    public void init() {

        toPolar = new int[frame.height * frame.width];

        // int xmax = frame.width / 2;
        // int ymax = frame.height / 2;
        int xmax = texture.width / 2;
        int ymax = texture.height / 2;

        // Calculate the angle of the line from the center of the screen
        // to each pixel on the screen. This angle maps the pixels on the
        // screen to pixel columns on the texture.

        double angle; // The angle in radians.
        double dist; // The distance from center.
        int index = 0;
        for (int y = 0; y < frame.height; y++) {
            for (int x = 0; x < frame.width; x++) {
                double column = (double)x / (double)frame.width;

                angle = column * Math.PI * 2 + Math.PI;
                dist = (double)y;

                int x1 = xmax + (int)(Math.sin(angle) * dist);
                int y1 = ymax + (int)(Math.cos(angle) * dist);

                toPolar[index++] = (x1 << 16) | y1;
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
            tmp = toPolar[--index];

            int x = tmp >> 16;
            int y = tmp & 0xFFFF;

            if (x < 0 || x >= texture.width ||
                y < 0 || y >= texture.height) continue;

            frame.pixels[index] = texture.pixels[y * texture.width + x];
        }
    }
}
