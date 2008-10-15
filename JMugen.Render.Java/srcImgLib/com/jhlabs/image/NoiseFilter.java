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

import java.util.Random;

/**
 * A filter which adds random noise into an image.
 */
public class NoiseFilter extends PointFilter {
	
	public final static int GAUSSIAN = 0;
	public final static int UNIFORM = 1;
	
	private int amount = 25;
	private int distribution = UNIFORM;
	private boolean monochrome = false;
	private float density = 1;
	private Random randomNumbers = new Random();
	
	public NoiseFilter() {
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setDistribution( int distribution ) {
		this.distribution = distribution;
	}
	
	public int getDistribution() {
		return distribution;
	}
	
	public void setMonochrome(boolean monochrome) {
		this.monochrome = monochrome;
	}
	
	public boolean getMonochrome() {
		return monochrome;
	}
	
	public void setDensity( float density ) {
		this.density = density;
	}
	
	public float getDensity() {
		return density;
	}
	
	private int random(int x) {
		x += (int)(((distribution == GAUSSIAN ? randomNumbers.nextGaussian() : 2*randomNumbers.nextFloat() - 1)) * amount);
		if (x < 0)
			x = 0;
		else if (x > 0xff)
			x = 0xff;
		return x;
	}
	
	public int filterRGB(int x, int y, int rgb) {
		if ( randomNumbers.nextFloat() <= density ) {
			int a = rgb & 0xff000000;
			int r = (rgb >> 16) & 0xff;
			int g = (rgb >> 8) & 0xff;
			int b = rgb & 0xff;
			if (monochrome) {
				int n = (int)(((distribution == GAUSSIAN ? randomNumbers.nextGaussian() : 2*randomNumbers.nextFloat() - 1)) * amount);
				r = PixelUtils.clamp(r+n);
				g = PixelUtils.clamp(g+n);
				b = PixelUtils.clamp(b+n);
			} else {
				r = random(r);
				g = random(g);
				b = random(b);
			}
			return a | (r << 16) | (g << 8) | b;
		}
		return rgb;
	}

	public String toString() {
		return "Stylize/Add Noise...";
	}
}
