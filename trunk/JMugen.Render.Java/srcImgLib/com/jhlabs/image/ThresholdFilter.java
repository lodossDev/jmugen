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


public class ThresholdFilter extends PointFilter implements java.io.Serializable {

	static final long serialVersionUID = -1899610620205446828L;
	
	private int lowerThreshold;
	private int lowerThreshold3;
	private int upperThreshold;
	private int upperThreshold3;
	private int white = 0xffffff;
	private int black = 0x000000;
	
	public ThresholdFilter() {
		this(127);
	}

	public ThresholdFilter(int t) {
		setLowerThreshold(t);
		setUpperThreshold(t);
	}

	public void setLowerThreshold(int lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
		lowerThreshold3 = lowerThreshold*3;
	}
	
	public int getLowerThreshold() {
		return lowerThreshold;
	}
	
	public void setUpperThreshold(int upperThreshold) {
		this.upperThreshold = upperThreshold;
		upperThreshold3 = upperThreshold*3;
	}

	public int getUpperThreshold() {
		return upperThreshold;
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

	public int filterRGB(int x, int y, int rgb) {
		int a = rgb & 0xff000000;
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >> 8) & 0xff;
		int b = rgb & 0xff;
		int l = r + g + b;
		if (l < lowerThreshold3)
			return a | black;
		else if (l > upperThreshold3)
			return a | white;
		return rgb;
	}

	public String toString() {
		return "Stylize/Threshold...";
	}
}
