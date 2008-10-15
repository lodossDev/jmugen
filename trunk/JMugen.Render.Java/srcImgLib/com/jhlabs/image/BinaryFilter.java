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

import com.jhlabs.math.BinaryFunction;
import com.jhlabs.math.BlackFunction;

public abstract class BinaryFilter extends WholeImageFilter {

	protected int newColor = 0xff000000;
	protected BinaryFunction blackFunction = new BlackFunction();
	protected int iterations = 1;
	protected Colormap colormap;

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}

	public void setColormap(Colormap colormap) {
		this.colormap = colormap;
	}

	public Colormap getColormap() {
		return colormap;
	}

	public void setNewColor(int newColor) {
		this.newColor = newColor;
	}

	public int getNewColor() {
		return newColor;
	}

	public void setBlackFunction(BinaryFunction blackFunction) {
		this.blackFunction = blackFunction;
	}

	public BinaryFunction getBlackFunction() {
		return blackFunction;
	}

}

