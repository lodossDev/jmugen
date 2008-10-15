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

import java.awt.Point;

/**
 * A Filter to pixellate images.
 */
public class BlockFilter extends TransformFilter {

	static final long serialVersionUID = 8077109551486196569L;
	
	private int blockSize = 2;

	/**
	 * Set the pixel block size
	 * @param blockSize the number of pixels along each block edge
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	/**
	 * Get the pixel block size
	 * @return the number of pixels along each block edge
	 */
	public int getBlockSize() {
		return blockSize;
	}


	public BlockFilter() {
	}

	protected void transform(int x, int y, Point out) {
		out.x = (x / blockSize) * blockSize;
		out.y = (y / blockSize) * blockSize;
	}

	protected void transformInverse(int x, int y, float[] out) {
		out[0] = (x / blockSize) * blockSize;
		out[1] = (y / blockSize) * blockSize;
	}

	public String toString() {
		return "Stylize/Mosaic...";
	}
}

