/*
 * Created on 24 avr. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.lee.project.effect.Fire;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Random;

import org.lee.project.raster.ImageRaster;

/** this class create de fire effect
 * @author Dr Wong
 */
public final class FireEffectLeft {

    
    private Image imgageRest;
    private ImageRaster _imageDest;
    private ImageRaster _imageSrc;
    private ImageRaster _imageRemanante;
    private int _width;
    private int _height;
    private boolean _isInit = false;
    private MemoryImageSource _memImage;
    private Random _random = new Random();
   

    
    /** the constructor of this effect
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public FireEffectLeft(int width, int height) {
        _width = width;
        _height = height;

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

        _imageSrc.fill(ImageRaster.getRGB(0, 0, 0));
        _imageDest.fill(ImageRaster.getRGB(0, 0, 0));
        

    }

        /** initialize the source to raster
         * @param component Container: the parent container
         */        
    public void Initialize(Container component) {
        BufferedImage img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
        _imageSrc.grabImage(img);
        imgageRest = component.createImage(_memImage);
        imgageRest.flush();
        _isInit = true;

    }

    /** engime of this effect
     * @param g2D Graphics2D: the graphic context that you want to draw
     */    
    public void run(Graphics2D g2D) {            
        // the Brume
        for (int x = 0; x < _width; x++) {
            _imageSrc.setPixel(x, _height - 1 - 1, ImageRaster.getRGB(0, 0, _random.nextInt(50) + 50));
            _imageSrc.setPixel(x, _height - 1 - 2, ImageRaster.getRGB(0, 0, _random.nextInt(50) + 50));
        }
            
        // random flame pick
        int max = _random.nextInt(_width) ;
        for (int x = 1; x < max; x++) {

            // RED
            if (x >= max / 2) {
                _imageSrc.setPixel(x - 1, _height - 1 - 1, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x, _height - 1 - 1, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x + 1, _height - 1 - 1, ImageRaster.getRGB(0, 0, 200));
                                
                _imageSrc.setPixel(x - 1, _height - 1 - 2, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x, _height - 1 - 2, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x + 1, _height - 1 - 2, ImageRaster.getRGB(0, 0, 200));
                                
                _imageSrc.setPixel(x - 1, _height - 1 - 3, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x, _height - 1 - 3, ImageRaster.getRGB(0, 0, 200));
                _imageSrc.setPixel(x + 1, _height - 1 - 3, ImageRaster.getRGB(0, 0, 200));   
            }
            // ORANGE
            if (x < max / 2) {
                _imageSrc.setPixel(x - 1, _height - 1 - 1, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x, _height - 1 - 1, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 1, ImageRaster.getRGB(50, 150, 255));
                
                _imageSrc.setPixel(x - 1, _height - 1 - 2, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x, _height - 1 - 2, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 2, ImageRaster.getRGB(50, 150, 255));
                
                _imageSrc.setPixel(x - 1, _height - 1 - 3, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x, _height - 1 - 3, ImageRaster.getRGB(50, 150, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 3, ImageRaster.getRGB(50, 150, 255)); 
            }
            // Yellow
            if (x < max / 4) {
                _imageSrc.setPixel(x - 1, _height - 1 - 1, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x, _height - 1 - 1, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 1, ImageRaster.getRGB(50, 200, 255));
                
                _imageSrc.setPixel(x - 1, _height - 1 - 2, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x, _height - 1 - 2, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 2, ImageRaster.getRGB(50, 200, 255));
                
                _imageSrc.setPixel(x - 1, _height - 1 - 3, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x, _height - 1 - 3, ImageRaster.getRGB(50, 200, 255));
                _imageSrc.setPixel(x + 1, _height - 1 - 3, ImageRaster.getRGB(50, 200, 255));
            }
        }

            
        // move up flame
        for (int x = 0; x < _width; x++) {
          for (int y = 1; y < _height; y++) {
              _imageDest.setPixel(x, y, _imageSrc.getPixel(x, y));
            _imageDest.setPixel(x, y - 1, _imageSrc.getPixel(x, y));
          }
        }
            

        // the mini Blur
        for (int x = 1; x < _width - 1; x++) {
          for (int y = 1; y < _height - 1; y++) {
            int colorR = (ImageRaster.getR(_imageSrc.getPixel(x - 1, y)) + ImageRaster.getR(_imageSrc.getPixel(x + 1, y)) +
            ImageRaster.getR(_imageSrc.getPixel(x, y - 1)) + ImageRaster.getR(_imageSrc.getPixel(x, y + 1))) / 4;
            if (colorR > 0)
                colorR--;
                    
            int colorG = (ImageRaster.getG(_imageSrc.getPixel(x - 1, y)) + ImageRaster.getG(_imageSrc.getPixel(x + 1, y)) +
            ImageRaster.getG(_imageSrc.getPixel(x, y - 1)) + ImageRaster.getG(_imageSrc.getPixel(x, y + 1))) / 4;
            if (colorG > 0)
                colorG--;
                    
            int colorB = (ImageRaster.getB(_imageSrc.getPixel(x - 1, y)) + ImageRaster.getB(_imageSrc.getPixel(x + 1, y)) +
            ImageRaster.getB(_imageSrc.getPixel(x, y - 1)) + ImageRaster.getB(_imageSrc.getPixel(x, y + 1))) / 4;
            if (colorB > 0)
                colorB--;
            _imageDest.setPixel(x, y - 1, ImageRaster.getRGB(colorR, colorG, colorB));
          }
        }

        imgageRest.flush();
        g2D.drawImage(imgageRest, 0, 0, null);
        _imageSrc.copy(_imageDest);
    }

    /** is this effect is init if not call Initialize(Container)
     * @return boolean: true if this effect is init
     */    
    public boolean isInit() {
        return _isInit;
    }
}


