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

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * A Filter which distorts an image by twisting it from the centre out.
 * The twisting is centred at the centre of the image and extends out to the smallest of
 * the width and height. Pixels outside this radius are unaffected.
 */
public class TwirlFilter extends TransformFilter {

	static final long serialVersionUID = 1550445062822803342L;
	
	private float angle = 0;
	private float centreX = 0.5f;
	private float centreY = 0.5f;
	private float radius = 100;

	private float radius2 = 0;
	private float icentreX;
	private float icentreY;

	/**
	 * Construct a TwirlFilter with no distortion.
	 */
	public TwirlFilter() {
		setEdgeAction( CLAMP );
	}

	/**
	 * Set the angle of twirl in radians. 0 means no distortion.
	 * @param angle the angle of twirl. This is the angle by which pixels at the nearest edge of the image will move.
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	/**
	 * Get the angle of twist.
	 * @return the angle in radians.
	 */
	public float getAngle() {
		return angle;
	}
	
	public void setCentreX( float centreX ) {
		this.centreX = centreX;
	}

	public float getCentreX() {
		return centreX;
	}
	
	public void setCentreY( float centreY ) {
		this.centreY = centreY;
	}

	public float getCentreY() {
		return centreY;
	}
	
	public void setCentre( Point2D centre ) {
		this.centreX = (float)centre.getX();
		this.centreY = (float)centre.getY();
	}

	public Point2D getCentre() {
		return new Point2D.Float( centreX, centreY );
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		icentreX = src.getWidth() * centreX;
		icentreY = src.getHeight() * centreY;
		if ( radius == 0 )
			radius = Math.min(icentreX, icentreY);
		radius2 = radius*radius;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float distance = dx*dx + dy*dy;
		if (distance > radius2) {
			out[0] = x;
			out[1] = y;
		} else {
			distance = (float)Math.sqrt(distance);
			float a = (float)Math.atan2(dy, dx) + angle * (radius-distance) / radius;
			out[0] = icentreX + distance*(float)Math.cos(a);
			out[1] = icentreY + distance*(float)Math.sin(a);
		}
	}

	public String toString() {
		return "Distort/Twirl...";
	}

}
