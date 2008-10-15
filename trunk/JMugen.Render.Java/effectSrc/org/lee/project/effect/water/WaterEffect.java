package org.lee.project.effect.water;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Random;

import org.lee.project.raster.ImageRaster;

/** build the water effect */
public class WaterEffect {

    private Image imgageRest;
	private ImageRaster _imageDest;
	private ImageRaster _imageSrc;
	private WaterDeform _waveDeform;
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
	public WaterEffect(int width, int height) {
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
        _waveDeform = new WaterDeform(_width, _height);
        _waveDeform.setDim(dim);
	}
        /** initialize the source to raster
         * @param img Image: source
         */        
	public void initialize(Image img) {
		Image image = img;
		_imageSrc.grabImage(image);
		imgageRest = Toolkit.getDefaultToolkit().createImage(_memImage);
		imgageRest.flush();

        
	}

        /** set a pick point to deform
         * @param x int: x position
         * @param y int: y position
         */        
	public void setPoint(int x, int y) {
		_xPos = x;
		_yPos = y;
		if (x > _width - _dropSize)
			x = _width - _dropSize;
		if (y > _height - _dropSize)
			y = _height - _dropSize;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		_waveDeform.drop(x, y, 300, _dropSize);
	}
	Random r = new Random();
        /** process this effect
         * @param g Graphics: the graphics contexte where is draw the effect
         */        
	public void process(Graphics g) {
		_waveDeform.propag(_imageDest, _imageSrc);

		imgageRest.flush();
		g.drawImage(imgageRest, 0, 0, null);

		for (int x = 0; x < 00000; x++)
			setPoint(r.nextInt(320), r.nextInt(240));
		
		
		
		
	}

}
