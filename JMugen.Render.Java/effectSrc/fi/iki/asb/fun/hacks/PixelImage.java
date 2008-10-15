package fi.iki.asb.fun.hacks;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.PixelGrabber;

/**
 *
 */
public class PixelImage {

    /**
     * The pixels that construct this image.
     */
    public int[] pixels = null;

    /**
     * Width of this image.
     */
    public int width;

    /**
     * Height of this image.
     */
    public int height;

    // ==================================================================== //

    /**
     * Create a pixel image from a size spec.
     */
    public PixelImage(int width, int height) {
        this.width = width;
        this.height = height;
    pixels = new int[width * height];
    }
    public PixelImage(int [] pixels, int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    /**
     * Create a pixel image from a size spec.
     */
    public PixelImage(Dimension size) {
        this(size.width, size.height);
    }

    /**
     * Create a pixel image from an existing image.
     */
    public PixelImage(Image image) {
        this(image.getWidth(null), image.getHeight(null));

        try {
            PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, 
                                               pixels, 0, width);
            pg.grabPixels();
        } catch (InterruptedException e) {
	    e.printStackTrace();
        }
    }

    /**
     * Create a pixel image from an existing pixel image.
     */
    public PixelImage(PixelImage image) {
        this(image.width, image.height);
        System.arraycopy(image.pixels, 0, pixels, 0, pixels.length);
    }

    /**
     * Crop an area from the specified image.
     */
    public PixelImage(PixelImage image, int x, int y, int w, int h) {
        this(w, h);

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                pixels[xx + yy * w] =
                    image.pixels[(x + xx) + (y + yy) * image.width];
            }
        }
    }

    // ==================================================================== //

    /**
     * Draw this pixel image to the specified frame.
     */
    public void drawTo(PixelImage frame, int x, int y) {

        int frame_xs = x;
        int frame_ys = y;
        int frame_xe = x + width;
        int frame_ye = y + height;

        // Check if the location is completely outside the frame.
        if (frame_xs >= frame.width
            || frame_ys >= frame.height
            || frame_xe < 0
            || frame_ye < 0) {
            return;
        }

        int texture_x = 0;
        int texture_y = 0;

        // Check if the location is partially inside the frame.
        if (x < 0) {
            frame_xs = 0;
            texture_x = -x;
        } else if (frame_xe > frame.width) {
            frame_xe = frame.width;
        }

        if (y < 0) {
            frame_ys = 0;
            texture_y = -y;
        } else if (frame_ye > frame.height) {
            frame_ye = frame.height;
        }

        for (int frame_y = frame_ys; frame_y < frame_ye; frame_y++) {
            int frame_i = frame_y * frame.width + frame_xs;
            int texture_i = texture_y * width + texture_x;

            for (int frame_x = frame_xs; frame_x < frame_xe; frame_x++) {
                int texel = pixels[texture_i++];

                if ((texel & 0xFF000000) == 0xFF000000) {
                    frame.pixels[frame_i++] = texel;
                } else {
                    frame_i++;
                }
            }

            texture_y++;
        }
    }

    /**
     * Set the color of the pixel in the specified location to the
     * specified color.
     */
    public final void addPixel(int index, int red, int green, int blue) {

        int c = pixels[index];
        int r, g, b;

        r = ((c >> 16) & 0xFF) + red;
        if (r > 0xFF) r = 0xFF;
        g = ((c >> 8) & 0xFF) + green;
        if (g > 0xFF) g = 0xFF;
        b = ((c) & 0xFF) + blue;
        if (b > 0xFF) b = 0xFF;

        pixels[index] = 0xFF000000 | (r << 16) | (g << 8) | b;
    }

}
