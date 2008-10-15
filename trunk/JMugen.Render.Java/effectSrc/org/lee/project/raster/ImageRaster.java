
package org.lee.project.raster;

import java.awt.Image;
import java.awt.image.PixelGrabber;

/** this class ImageRaster allows Image low-level manipulation */
public class ImageRaster {
    private int pixels[];
    private int width;
    private int height;
    private int size;

    /** the constructor
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public ImageRaster(int width, int height) {
        this.width = width;
        this.height = height;
        size = width * height;
        pixels = new int[size];
    }

    /** get the height of this ImageRaster
     * @return int: height of this ImageRaster
     */    
    public final int getHeight() {
        return height;
    }

    /** get all pixels of this Image
     * @return int[]: pixel[] => each pixel is in ARGB format
     */    
    public final int[] getPixels() {
        return pixels;
    }

    /** grab the image into pixel[]
     * @param image Image: the image you want to grab
     */    
    public final void grabImage(Image image) {
        PixelGrabber pixelgrabber =
            new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pixelgrabber.grabPixels();
            return;
        } catch (InterruptedException _ex) {
            _ex.printStackTrace();
            return;
        }
    }

    /** copy another Image raster
     * @param screen ImageRaster: the image raster you want to copy
     */    
    public final void copy(ImageRaster screen) {
        for (int i = 0; i < size; i++)
            pixels[i] = screen.pixels[i];

    }

    /** fill the ImageRaster with a color (RGB base)
     * @param color int: color (RGB base)
     */    
    public final void fill(int color) {
        for (int j = 0; j < size; j++)
            pixels[j] = color;

    }

    /** get the width of this image
     * @return int: width of the image
     */    
    public final int getWidth() {
        return width;
    }

    /** set a color to a specific pixel
     * @param index int: position of the pixel (x + y * width)
     * @param value int: color (RGB base)
     */    
    public void setPixel(int index, int value) {
        pixels[index] = value;
    }
    public void setPixel(int x, int y, int value) {
        pixels[x + (y * width)] = value;
    }

    /** get color of specific pixel
     * @param index int: position of the pixel (x + y * width)
     * @return int: position of the pixel (x + y * width)
     */    
    public int getPixel(int index) {
        return pixels[index];
    }
    public int getPixel(int x, int y) {
        return pixels[x + y * width];
    }

    /** get y offset (line * width)
     * @param line int: y line you want to get offset
     * @return int: y offset (x + offset)
     */    
    public int getOffset(int line) {
        return line * width;
    }
    public static int getRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }
    public static int getAlpha(int rgb) {
        return (rgb >> 24) & 0x0000FF;
    }
    public static int getR(int rgb) {
        return (rgb >> 16) & 0x0000FF;
    }
    public static int getG(int rgb) {
        return (rgb >> 8) & 0x0000FF;
    }
    public static int getB(int rgb) {
        return rgb & 0x0000FF;
    }
    
    public int getAlpha(int x, int y) {
        return getAlpha(getPixel(x, y));
    }
    public int getR(int x, int y) {
        return getR(getPixel(x, y));
    }
    public int getG(int x, int y) {
        return getG(getPixel(x, y));
    }
    public int getB(int x, int y) {
        return getB(getPixel(x, y));
    }
    
    public int getAlphaI(int index) {
        return getAlpha(getPixel(index));
    }
    public int getRI(int index) {
        return getR(getPixel(index));
    }
    public int getGI(int index) {
        return getG(getPixel(index));
    }
    public int getBI(int index) {
        return getB(getPixel(index));
    }
    
}