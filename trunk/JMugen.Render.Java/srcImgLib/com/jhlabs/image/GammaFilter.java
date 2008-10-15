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


/**
 * A filter for changing the gamma of an image.
 */
public class GammaFilter extends TransferFilter {

	private float rGamma, gGamma, bGamma;

	public GammaFilter() {
		this(1.0f);
	}

	public GammaFilter(float gamma) {
		this(gamma, gamma, gamma);
	}

	public GammaFilter(float rGamma, float gGamma, float bGamma) {
		setGamma(rGamma, gGamma, bGamma);
	}

	public void setGamma(float rGamma, float gGamma, float bGamma) {
		this.rGamma = rGamma;
		this.gGamma = gGamma;
		this.bGamma = bGamma;
		initialized = false;
	}

	public void setGamma(float gamma) {
		setGamma(gamma, gamma, gamma);
	}
	
	public float getGamma() {
		return rGamma;
	}
	
	protected void initialize() {
		rTable = makeTable(rGamma);

		if (gGamma == rGamma)
			gTable = rTable;
		else
			gTable = makeTable(gGamma);

		if (bGamma == rGamma)
			bTable = rTable;
		else if (bGamma == gGamma)
			bTable = gTable;
		else
			bTable = makeTable(bGamma);
	}

	protected int[] makeTable(float gamma) {
		int[] table = new int[256];
		for (int i = 0; i < 256; i++) {
			int v = (int) ((255.0 * Math.pow(i/255.0, 1.0 / gamma)) + 0.5);
			if (v > 255)
				v = 255;
			table[i] = v;
		}
		return table;
	}

	public String toString() {
		return "Colors/Gamma...";
	}

}

