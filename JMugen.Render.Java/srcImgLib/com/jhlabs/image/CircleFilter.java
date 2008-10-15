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

public class CircleFilter extends TransformFilter {

	private float radius = 10;
	private float height = 20;
	private float angle = 0;
	private float spreadAngle = (float)Math.PI;
	private float centreX = 0.5f;
	private float centreY = 0.5f;

	private float icentreX;
	private float icentreY;
	private float iWidth;
	private float iHeight;

	public CircleFilter() {
		setEdgeAction( ZERO );
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public void setSpreadAngle(float spreadAngle) {
		this.spreadAngle = spreadAngle;
	}

	public float getSpreadAngle() {
		return spreadAngle;
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public float getRadius() {
		return radius;
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
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
		iWidth = src.getWidth();
		iHeight = src.getHeight();
		icentreX = iWidth * centreX;
		icentreY = iHeight * centreY;
		iWidth--;
		return super.filter( src, dst );
	}
	
	protected void transformInverse(int x, int y, float[] out) {
		float dx = x-icentreX;
		float dy = y-icentreY;
		float theta = (float)Math.atan2( -dy, -dx ) + angle;
		float r = (float)Math.sqrt( dx*dx + dy*dy );
/*
		if ( theta < 0 )
			theta += 2*(float)Math.PI;
		else if ( theta > 2*(float)Math.PI )
			theta -= 2*(float)Math.PI;
*/
		theta = ImageMath.mod( theta, 2*(float)Math.PI );

		out[0] = iWidth * theta/(spreadAngle+0.00001f);
		out[1] = iHeight * (1-(r-radius)/(height+0.00001f));
	}

	public String toString() {
		return "Distort/Circle...";
	}

}
