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

import java.awt.image.BufferedImage;

public class StampFilter extends PointFilter {

	private float threshold;
	private float softness = 0;
    protected float radius = 5;
	private float lowerThreshold3;
	private float upperThreshold3;
	private int white = 0xffffffff;
	private int black = 0xff000000;

	public StampFilter() {
		this(0.5f);
	}

	public StampFilter( float threshold ) {
		setThreshold( threshold );
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getRadius() {
		return radius;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public void setSoftness(float softness) {
		this.softness = softness;
	}

	public float getSoftness() {
		return softness;
	}

	public void setWhite(int white) {
		this.white = white;
	}

	public int getWhite() {
		return white;
	}

	public void setBlack(int black) {
		this.black = black;
	}

	public int getBlack() {
		return black;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        dst = new GaussianFilter( (int)radius ).filter( src, null );
        lowerThreshold3 = 255*3*(threshold - softness*0.5f);
        upperThreshold3 = 255*3*(threshold + softness*0.5f);
		return super.filter(dst, dst);
	}

	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		int l = r + g + b;
		float f = ImageMath.smoothStep(lowerThreshold3, upperThreshold3, l);
        return ImageMath.mixColors(f, black, white);
	}

	public String toString() {
		return "Stylize/Stamp...";
	}
}
