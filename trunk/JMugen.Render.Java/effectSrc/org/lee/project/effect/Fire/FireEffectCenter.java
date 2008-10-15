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
public final class FireEffectCenter {

    
   
    private Image imgageRest;
    private ImageRaster _imageDest;
    private ImageRaster _imageSrc;
    private ImageRaster _imageRemanante;
    private int _width;
    private int _height;
    private boolean _isInit = false;
    private MemoryImageSource _memImage;
    private Random _random = new Random();
   
    private int[] xFoyer;
    
    
    /** the constructor
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public FireEffectCenter(int width, int height) {
        this(1, width, height);
    }
    
    /** the constructor of this effect
     * @param foyerCount int: fire's pick point count
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public FireEffectCenter(int foyerCount, int width, int height) {
        xFoyer = new int[foyerCount];
        
        int x = width / (foyerCount + 1);
        
        if (foyerCount == 1)
        	xFoyer[0] = x;
        else
            for (int i = 0; i < foyerCount; i++) {
                xFoyer[i] = x * i + x / 2 + (x / ((foyerCount - 1)) * i);
            }
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

    /** not use this methode
     * it is here juste because i want to make another effect
     * @param image Image: the picture you want to create fire
     */    
    public void setImageRemanante(Image image) {
        _imageRemanante = new ImageRaster(_width, _height);
        BufferedImage img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(image, 0, 0, null);

        _imageRemanante.grabImage(img); 
    }
    
        /** initialize the source to raster with compatible raster
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
            // Po encore o Point
//            if (_imageRemanante != null) {
//                _imageSrc.fusion(_imageRemanante, 0.5);
//            }
            
            for (int iFoyer = 0; iFoyer < xFoyer.length; iFoyer++) {
                // the Brume
                for (int y = _height - 1; y >= 0; y--) {
                    _imageSrc.setPixel(xFoyer[iFoyer] - 1, y, ImageRaster.getRGB(_random.nextInt(50) + 50, 0, 0));
                    _imageSrc.setPixel(xFoyer[iFoyer], y, ImageRaster.getRGB(_random.nextInt(50) + 50, 0, 0));
                    _imageSrc.setPixel(xFoyer[iFoyer] + 1, y, ImageRaster.getRGB(_random.nextInt(50) + 50, 0, 0));
                }
                
                // random flame pick & create Fire
                int min = _random.nextInt(_height) - 1;
    
                for (int y = _height - 2; y > min + 1; y--) {
    
                    // RED
                    if (y <= (min + (_height - min)/2)) {
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y - 1, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y + 1, ImageRaster.getRGB(200, 0, 0));
                                    
                        _imageSrc.setPixel(xFoyer[iFoyer], y - 1, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer], y, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer], y + 1, ImageRaster.getRGB(200, 0, 0));
                        
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y - 1, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y, ImageRaster.getRGB(200, 0, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y + 1, ImageRaster.getRGB(200, 0, 0));
    
                    }
                    // ORANGE
                    if (y > (min + (_height - min)/2)) {
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y - 1, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y + 1, ImageRaster.getRGB(255, 150, 50));
                    
                        _imageSrc.setPixel(xFoyer[iFoyer], y - 1, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer], y, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer], y + 1, ImageRaster.getRGB(255, 150, 50));
                                        
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y - 1, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y, ImageRaster.getRGB(255, 150, 50));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y + 1, ImageRaster.getRGB(255, 150, 50));
                    }
                    // Yellow
                    if (y > (min + (3 * (_height - min))/4)) {
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y - 1, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] - 1, y + 1, ImageRaster.getRGB(255, 200, 0));
                    
                        _imageSrc.setPixel(xFoyer[iFoyer], y - 1, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer], y, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer], y + 1, ImageRaster.getRGB(255, 200, 0));
                     
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y - 1, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y, ImageRaster.getRGB(255, 200, 0));
                        _imageSrc.setPixel(xFoyer[iFoyer] + 1, y + 1, ImageRaster.getRGB(255, 200, 0));
                    }
                }
            }

            // smooth move flame 

//            for (int y = 0; y < _height; y++) {
//                for (int x = 1; x < xCenter - 1; x++) {
//                    _imageDest.setPixel(xCenter + x + 1, y, _imageSrc.getPixel(xCenter + x, y));
//                    _imageDest.setPixel(xCenter - x - 1, y, _imageSrc.getPixel(xCenter - x, y));
//                          
//                }
//            }
//            
            int size = (_width / (xFoyer.length + 1)) / 2;
            
            // the mini Blur
            for (int iFoyer = 0; iFoyer < xFoyer.length; iFoyer++) {
                int right = _width;
                if (iFoyer < xFoyer.length - 1)
                	right = ((xFoyer[iFoyer + 1] - xFoyer[iFoyer]) / 2) + xFoyer[iFoyer];
                for (int x = xFoyer[iFoyer]; x < right; x++) {
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
                    _imageDest.setPixel(x + 1, y, ImageRaster.getRGB(colorR, colorG, colorB));
                  }
                }
                int left = 0;
                if (iFoyer > 0)
                    left = ((xFoyer[iFoyer] - xFoyer[iFoyer - 1]) / 2) + xFoyer[iFoyer - 1];

                for (int x = xFoyer[iFoyer] + 1; x > left; x--) {
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
                    _imageDest.setPixel(x - 1, y, ImageRaster.getRGB(colorR, colorG, colorB));
                  }
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


