

package org.lee.project.effect.motionBlur;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import org.lee.project.raster.ImageRaster;

import fi.iki.asb.fun.hacks.MotionBlurHack;
import fi.iki.asb.fun.hacks.PixelImage;

/** build the water effect */
public class MotionBlur {

    private Image imgageRest;
    private ImageRaster _imageDest;
    private ImageRaster _imageSrc;

    private int _width;
    private int _height;
    private int _dropSize;
    private MemoryImageSource _memImage;
    private int _xPos = 0;
    private int _yPos = 0;
    
    /** the constructor of this effect
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public MotionBlur(int width, int height) {
        _width = width;
        _height = height;
        int dim = 3;
        _dropSize = 2;
        _imageDest = new ImageRaster(_width, _height);
        _imageSrc = new ImageRaster(_width, _height);
        DirectColorModel colorModel =
            new DirectColorModel(32, 0xff0000, 65280, 255);
        _memImage =
            new MemoryImageSource(
                _imageDest.getWidth(),
                _imageDest.getHeight(),
                colorModel,
                _imageDest.getPixels(),
                0,
                _imageDest.getWidth());

    }
        /** initialize the source to raster
         * @param img Image: source
         */        
    public void initialize(Image img) {
        Image image = img;
        _imageSrc.grabImage(image);
        imgageRest = Toolkit.getDefaultToolkit().createImage(_memImage);
        imgageRest.flush();
        if (pImg == null)
            pImg = new PixelImage(_imageDest.getPixels(), _width, _height);
        else
            pImg.pixels = _imageDest.getPixels();
        if (mb == null) {
            mb = new MotionBlurHack(2, pImg);
            mb.init(pImg);
        }
        
    }
    PixelImage pImg;
    MotionBlurHack mb;
        /** set a pick point to deform
         * @param x int: x position
         * @param y int: y position
         */        


        /** process this effect
         * @param g Graphics: the graphics contexte where is draw the effect
         */        
    public void process(Graphics g) {
    	System.arraycopy(_imageSrc.getPixels(), 0, _imageDest.getPixels(), 0, _imageDest.getPixels().length);
        
        mb.tick();
        
//        int i = _width;
//        boolean flag = false;
//        int[] bufferData = _imageDest.getPixels();
//        for(int j = i; j < bufferData.length - i; j++)
//            bufferData[j] = (bufferData[j - 1] & 0xff) + (bufferData[j + 1] & 0xff) + (bufferData[j - i] & 0xff) + (bufferData[j + i] & 0xff) >> 2 | (bufferData[j - 1] & 0xff00) + (bufferData[j + 1] & 0xff00) + (bufferData[j - i] & 0xff00) + (bufferData[j + i] & 0xff00) >> 2 & 0xff00 | (bufferData[j - 1] & 0xff0000) + (bufferData[j + 1] & 0xff0000) + (bufferData[j - i] & 0xff0000) + (bufferData[j + i] & 0xff0000) >> 2 & 0xff0000 | 0xff000000;

        imgageRest.flush();
        g.drawImage(imgageRest, 0, 0, null);
    }

}