package org.lee.mugen.sprite.baseForParse;

import java.io.IOException;

import org.lee.mugen.imageIO.RawPCXImage;
import org.lee.mugen.object.Rectangle;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;

public class ImageSpriteSFF {
	private ImageContainer _image;
    private int _grpNum;
    private int _imgNum;
    private Rectangle _rect;

    private int _xAxis;
    
    private int _yAxis;

    public ImageSpriteSFF(int grpNum, int imgNum, RawPCXImage pcx, int xAxis, int yAxis) throws IOException {
        _grpNum = grpNum;
         _imgNum = imgNum;
        _xAxis = xAxis;
        _yAxis = yAxis;
       	_image = GraphicsWrapper.getInstance().getImageContainer(pcx);
        _rect = new Rectangle(0, 0, _image.getWidth(), _image.getHeight());
    }
    
    public ImageSpriteSFF(int grpNum, int imgNum, ImageContainer image, int xAxis, int yAxis) throws IOException {
        _grpNum = grpNum;
         _imgNum = imgNum;
        _xAxis = xAxis;
        _yAxis = yAxis;
       	_image = image;
        _rect = new Rectangle(0, 0, _image.getWidth(), _image.getHeight());
    }
    public int getGrpNum() {
        return _grpNum;
    }
    public int getHeight() {
        return _image.getHeight();
    }

    public ImageContainer getImage() {
    	return _image;
    }

    public int getImgNum() {
        return _imgNum;
    }
    public int getWidth() {
        return _image.getWidth();
    }
    public Rectangle getRect() {
        return _rect;
    }
    public int getXAxis() {
        return _xAxis;
    }


    public int getYAxis() {
        return _yAxis;
    }
}