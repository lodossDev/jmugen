/*
 * Created on 24 avr. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.lee.project.effect.intro.speed;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Random;

import org.lee.project.raster.ImageRaster;

/** this class make a speed effect when the gamezone starts
 * @author Dr Wong
 */
public final class EffectSpeed {
	private static final int _SPEED = 23;
    private Image imgageRest;
    private ImageRaster _imageDest;
    private ImageRaster _imageSrc;

    private int _width;
    private int _height;

    private MemoryImageSource _memImage;
    int[] xStarts;
    int[] xSpeed;
    private int[] xFoyer;
    
    boolean _isFirstTimeFinish = false;

    /** the constructor of this effect
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
    public EffectSpeed(int width, int height) {
       
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
        xStarts = new int[_imageDest.getHeight()];
        xSpeed = new int[_imageDest.getHeight()];
        for (int i = 0; i < xStarts.length; i++) {
            xStarts[i] = _imageDest.getWidth() - 1;
            xSpeed[i] = i + 1;
        }

    }

    private Random _random = new Random();

    
        /** initialize the source to raster
         * @param component Component: the parent component
         * @param img Image: source
         */        
    public void initialize(Container component, Image img) {
        _imageSrc.grabImage(img);
        imgageRest = component.createImage(_memImage);
        imgageRest.flush();
        _isInit = true;
    }



    /** Engime of this effect
     * @param g2D Graphics2D: the graphics context you want to draw
     */    
    public void run(Graphics2D g2D) {
        _isFirstTimeFinish = true;
        for (int i = 0; i < _imageDest.getHeight(); i++) {

            fill(xStarts[i], i, _imageDest.getWidth() - xStarts[i] - 1);
            if (xStarts[i] <= 0)
                continue;
            xStarts[i] -= _random.nextInt(_SPEED);
            _isFirstTimeFinish = _isFirstTimeFinish && xStarts[i] <= 0;
        }
        imgageRest.flush();
        g2D.drawImage(imgageRest, 0, 0, null);
    }
    

    
    private void fill(int xDest, int y, int width) {
        if (xDest < 0) {
            xDest = 0;
            xStarts[y] = 0;
        }
        for (int i = 0; i < width; i++) {
            if (xDest + i >= _imageDest.getWidth() - 1) {
                return;
            }
            if (xDest + i > 0)
            	_imageDest.setPixel(xDest + i, y, _imageSrc.getPixel(i, y));
        }
    }

    private boolean _isInit = false;
    /** check if this effect is init
     * @return boolean: true if this effect is init
     */    
    public boolean isInit() {
        return _isInit;
    }

	/** is the first time you launch this effect is finished
         * @return Returns the isFirstTimeFinish.
         */
	public boolean isFirstTimeFinish() {
		return _isFirstTimeFinish;
	}

}


