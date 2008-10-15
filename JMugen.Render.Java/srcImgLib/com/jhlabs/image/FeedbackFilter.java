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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FeedbackFilter extends AbstractBufferedImageOp {
    private float centreX = 0.5f, centreY = 0.5f;
    private float distance;
    private float angle;
    private float rotation;
    private float zoom;
    private float startAlpha = 1;
    private float endAlpha = 1;
    private int iterations;

    public FeedbackFilter() {
	}
	
	public FeedbackFilter( float distance, float angle, float rotation, float zoom ) {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
    }
    
	public void setAngle( float angle ) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}
	
	public void setDistance( float distance ) {
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}
	
	public void setRotation( float rotation ) {
		this.rotation = rotation;
	}

	public float getRotation() {
		return rotation;
	}
	
	public void setZoom( float zoom ) {
		this.zoom = zoom;
	}

	public float getZoom() {
		return zoom;
	}
	
	public void setStartAlpha( float startAlpha ) {
		this.startAlpha = startAlpha;
	}

	public float getStartAlpha() {
		return startAlpha;
	}
	
	public void setEndAlpha( float endAlpha ) {
		this.endAlpha = endAlpha;
	}

	public float getEndAlpha() {
		return endAlpha;
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
	
	public void setIterations( int iterations ) {
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}
	
    public BufferedImage filter( BufferedImage src, BufferedImage dst ) {
        if ( dst == null )
            dst = createCompatibleDestImage( src, null );
        float cx = (float)src.getWidth() * centreX;
        float cy = (float)src.getHeight() * centreY;
        float imageRadius = (float)Math.sqrt( cx*cx + cy*cy );
        float translateX = (float)(distance * Math.cos( angle ));
        float translateY = (float)(distance * -Math.sin( angle ));
        float scale = (float)Math.exp(zoom);
        float rotate = rotation;

        if ( iterations == 0 ) {
            Graphics2D g = dst.createGraphics();
            g.drawRenderedImage( src, null );
            g.dispose();
            return dst;
        }
        
		Graphics2D g = dst.createGraphics();
		g.drawImage( src, null, null );
        for ( int i = 0; i < iterations; i++ ) {
			g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
			g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, ImageMath.lerp( (float)i/(iterations-1), startAlpha, endAlpha ) ) );

            g.translate( cx+translateX, cy+translateY );
            g.scale( scale, scale );  // The .0001 works round a bug on Windows where drawImage throws an ArrayIndexOutofBoundException
            if ( rotation != 0 )
                g.rotate( rotate );
            g.translate( -cx, -cy );

            g.drawImage( src, null, null );
        }
		g.dispose();
        return dst;
    }
    
	public String toString() {
		return "Effects/Feedback...";
	}
}
