
package org.lee.project.effect.water;

import org.lee.project.raster.ImageRaster;

/** engime of this water effect */
public final class WaterDeform {
    private int Mask1[];
    private int Mask2[];
    private int width;
    private int height;
    private int halfWidth;
    private int halfHeight;
    private int dim;

    /** the constructor
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public WaterDeform(int width, int height) {
        this.width = width;
        this.height = height;
        halfWidth = width >> 1;
        halfHeight = height >> 1;
        dim = 5;
        Mask1 = new int[width * (height + 2)];
        Mask2 = new int[width * (height + 2)];

    }

    /** drop a pick point
     * @param x int: x position
     * @param y int: y position
     * @param ampl int: amplitude
     * @param radius int: radius
     */    
    public void drop(int x, int y, int ampl, int radius) {
        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                if (y + i + 1 > 0 && y + i + 1 < height - radius) {
                    Mask1[width + getOffset(y + i + 1) + x + j + 1] += ampl;
                }
            }
        }
    }

    /** propagtion of dample
     * @param dest ImageRaster: Destination
     * @param source ImageRaster: Source
     */    
    public void propag(ImageRaster dest, ImageRaster source) {
        int[] tempTab = Mask1;
        Mask1 = Mask2;
        Mask2 = tempTab;
        int offset = width;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int k =
                    (Mask1[offset
                        - width]
                        + Mask1[offset
                        + width]
                        + Mask1[offset
                        - 1]
                        + Mask1[offset
                        + 1])
                        >> 1;
                k -= Mask2[offset];
                k -= k >> dim;
                Mask2[offset] = k;
                k = 1024 - k;
                int xx = ((x - halfWidth) * k >> 10) + halfWidth;
                if (xx >= width || xx < 0)
                    xx = 0;
                int yy = ((y - halfHeight) * k >> 10) + halfHeight;
                if (yy >= height || yy < 0)
                    yy = 0;
                dest.setPixel(
                    offset - width,
                    source.getPixel(xx + source.getOffset(yy)));
                offset++;
            }
        }
    }

    /** set Dimension of the dample
     * @param dim int: dample dimension
     */    
    public void setDim(int dim) {
        this.dim = dim;
    }

    /** get width of the work screen
     * @return int: width of the work screen
     */    
    public int getWidth() {
        return width;
    }

    /** get height of the work screen
     * @return int: height of the work screen
     */    
    public int getHeight() {
        return height;
    }

    /** get y offset (line * width)
     * @param line int: y line you want to get offset
     * @return int: y offset (x + offset)
     */    
    public int getOffset(int line) {
        return line * width;
    }

}