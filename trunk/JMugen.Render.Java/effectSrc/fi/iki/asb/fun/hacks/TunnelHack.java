package fi.iki.asb.fun.hacks;


//
// Optimizing the main loop
//
// 1. Changed the main loop to run from frame.pixels.length down to zero.
//    100 -> 99
//
// 2. Removed two temporary variables that held the values fetched from the
//    two arrays from the main loop and used the arrays directly.
//    99 -> 88
//
// 3. Unrolled the main loop 8-fold.
//    88 -> 82
//
// 4. Combined the two arrays so that the first array is in the upper 16 bits
//    of the integer and the second array is in the lower 16 bits. The amount
//    of code increased in the main loop so that unrolling the loop 8-fold no
//    longer provided increased performance. 4-fold unrolling was much better.
//    82 -> 80
//

/**
 * A classic tunnel hack. This is basically a displacement hack with a special
 * displacement map.
 */
public class TunnelHack extends Hack {

    /**
     * The original texture.
     */
    private PixelImage texture;

    /**
     * Read texture from top to bottom or bottom to top?
     */
    private boolean topToBottom;

    /**
     * Fake perspective?
     */
    private boolean perspective;

    /**
     * Pre-calculated maps that map pixels on the frame buffer to pixels
     * on the texture.
     */
    private int[] col_map /*, row_map*/;

    /**
     * Rotation offset.
     */
    private double rotation;

    private int col_offset;
    private int row_offset;

    // ==================================================================== //

    /**
     * Create a tunnel hack.
     *
     * @param texture the texture
     */
    public TunnelHack(PixelImage texture) {
        this(texture, true, true);
    }

    /**
     * Create a tunnel hack.
     *
     * @param texture the texture
     * @param topdown read texture from top to bottom or bottom to top
     */
    public TunnelHack(PixelImage texture,
                      boolean topToBottom,
                      boolean perspective) {
        this.texture = texture;
        this.topToBottom = topToBottom;
        this.perspective = perspective;
    }

    // ==================================================================== //

    /**
     *
     */
    public void init() {

        col_map = new int[frame.height * frame.width];
        // row_map = new int[frame.height * frame.width];

        int xmax = frame.width / 2;
        int ymax = frame.height / 2;

        // Calculate the angle of the line from the center of the screen
        // to each pixel on the screen. This angle maps the pixels on the
        // screen to pixel columns on the texture.
        double angle;
        for (int y = -ymax; y < ymax; y++) {
            for (int x = -xmax; x < xmax; x++) {
                if(x != 0 || y != 0) {
                    angle = Math.atan2((double)y, (double)x) *
                        ((double)texture.width / 6.284);
                    col_map[(y + ymax) * frame.width + (x + xmax)]
                        = (((int)angle) % texture.width) << 16;
                }
            }
        }

        // Calculate the distance between the center of the screen and each
        // pixel. This distance maps the pixels in the screen to pixel rows
        // on the texture.
        double max_distance = Math.sqrt((xmax * xmax) + (ymax * ymax));
        double distance;
        for (int y = -ymax; y < ymax; y++) {
            for (int x = -xmax; x < xmax; x++) {
                distance = Math.sqrt(x*x + y*y);

                // Add perspective to make closer pixels look larger.
                if (perspective) {
                    distance = 192.0 * Math.pow(65535 * distance, 0.1);
                }

                if (topToBottom) {
                    col_map[((y + ymax) * frame.width + (x + xmax))]
                        |= ((((int)distance) % texture.height) & 0x0000FFFF);
                } else {
                    col_map[((y + ymax) * frame.width + (x + xmax))]
                        |= ((((texture.height - 1) - ((int)distance))
                             % texture.height) & 0x0000FFFF);
                }

                
                // row_map[(y + ymax) * frame.width + (x + xmax)]
                //     = ((int)distance) % texture.height;
            }
        }
    }

    /**
     * Generate the next frame.
     */
    public void tick() {
        int w = texture.width;
        int h = texture.height;

        int h_row = w + row_offset;
        int w_col = h + col_offset;

        // Map a pixel to a row and column in the texture.
        // Copy the pixel from the texture to the frame buffer.
        int foo;
        int index = frame.pixels.length;
        while (index > 0) {

            // Optimization: if the texture height and width are 2^x and 2^y
            // pixels then we could force the row and column indexes to proper
            // range with bitwise and instead of modulo operations.

            foo = col_map[--index];
            int texel = texture.pixels[((h_row + (foo & 0xFFFF)) % h) * w +
                                      ((w_col + (foo >> 16)) % w)];
            if ((texel & 0xFF000000) == 0xFF000000) {
                frame.pixels[index] = texel;
            }
        }
    }

    /**
     * Set the rotation (in radians).
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;

        col_offset = (int)((rotation * (double)texture.width) /
                           6.283185307179586);
        col_offset %= texture.width;
    }

    /**
     * Set the row offset (zooming).
     */
    public void setRowOffset(int offset) {
        row_offset = offset % texture.height;
    }

    /**
     * Get the rotation (in radians).
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Get the row offset (zooming).
     */
    public int getRowOffset() {
        return row_offset;
    }

}
