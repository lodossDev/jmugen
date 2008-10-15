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

import com.jhlabs.math.Noise;

public class SwimFilter extends TransformFilter {

	private float scale = 32;
	private float stretch = 1.0f;
	private float angle = 0.0f;
	private float amount = 1.0f;
	private float turbulence = 1.0f;
	private float time = 0.0f;
	private float m00 = 1.0f;
	private float m01 = 0.0f;
	private float m10 = 0.0f;
	private float m11 = 1.0f;

	public SwimFilter() {
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setStretch(float stretch) {
		this.stretch = stretch;
	}

	public float getStretch() {
		return stretch;
	}

	public void setAngle(float angle) {
		this.angle = angle;
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		m00 = cos;
		m01 = sin;
		m10 = -sin;
		m11 = cos;
	}

	public float getAngle() {
		return angle;
	}

	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}

	public float getTurbulence() {
		return turbulence;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getTime() {
		return time;
	}

	protected void transformInverse(int x, int y, float[] out) {
		float nx = m00*x + m01*y;
		float ny = m10*x + m11*y;
		nx /= scale;
		ny /= scale * stretch;

		if ( turbulence == 1.0f ) {
			out[0] = x + amount * Noise.noise3(nx+0.5f, ny, time);
			out[1] = y + amount * Noise.noise3(nx, ny+0.5f, time);
		} else {
			out[0] = x + amount * Noise.turbulence3(nx+0.5f, ny, turbulence, time);
			out[1] = y + amount * Noise.turbulence3(nx, ny+0.5f, turbulence, time);
		}
	}

	public String toString() {
		return "Distort/Swim...";
	}
}
