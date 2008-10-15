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

import com.jhlabs.math.Noise;

/**
 * This filter applies a marbling effect to an image, displacing pixels by random amounts.
 */
public class MarbleFilter extends TransformFilter {

	public float[] sinTable, cosTable;
	public float xScale = 4;
	public float yScale = 4;
	public float amount = 1;
	public float turbulence = 1;
	
	public MarbleFilter() {
		setEdgeAction(CLAMP);
	}
	
	public void setXScale(float xScale) {
		this.xScale = xScale;
	}

	public float getXScale() {
		return xScale;
	}

	public void setYScale(float yScale) {
		this.yScale = yScale;
	}

	public float getYScale() {
		return yScale;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getAmount() {
		return amount;
	}

	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}

	public float getTurbulence() {
		return turbulence;
	}

	private void initialize() {
		sinTable = new float[256];
		cosTable = new float[256];
		for (int i = 0; i < 256; i++) {
			float angle = ImageMath.TWO_PI*i/256f*turbulence;
			sinTable[i] = (float)(-yScale*Math.sin(angle));
			cosTable[i] = (float)(yScale*Math.cos(angle));
		}
	}

	private int displacementMap(int x, int y) {
		return PixelUtils.clamp((int)(127 * (1+Noise.noise2(x / xScale, y / xScale))));
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		int displacement = displacementMap(x, y);
		out[0] = x + sinTable[displacement];
		out[1] = y + cosTable[displacement];
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		initialize();
		return super.filter( src, dst );
	}

	public String toString() {
		return "Distort/Marble...";
	}
}
