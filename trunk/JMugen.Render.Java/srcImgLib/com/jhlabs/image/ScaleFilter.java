/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.jhlabs.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * Scales an image using the area-averaging algorithm, which can't be done with AffineTransformOp.
 */
public class ScaleFilter extends AbstractBufferedImageOp {

	private int width;
	private int height;

	public ScaleFilter() {
		this(32, 32);
	}

	public ScaleFilter( int width, int height ) {
		this.width = width;
		this.height = height;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        int w = src.getWidth();
        int h = src.getHeight();

		if ( dst == null ) {
			ColorModel dstCM = src.getColorModel();
			dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(w, h), dstCM.isAlphaPremultiplied(), null);
		}

		Image scaleImage = src.getScaledInstance( w, h, Image.SCALE_SMOOTH );
		Graphics2D g = dst.createGraphics();
		g.drawImage( src, 0, 0, width, height, null );
		g.dispose();

        return dst;
    }

	public String toString() {
		return "Distort/Scale";
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public void setWidth(int width) {
		this.width = width;
	}

}
