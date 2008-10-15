/*
 * Created on 25 mars 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.lee.project.effect.level.rain;

import img.BaseImgSpr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** this class makes snowns on the screen
 * @author Dr_Wong
 */
public class RainEffect {
	private final int _width;
	private final int _height;
	private final int _snowCount;
	private final List<Point> _SnowPointList;
	private final Color _color = new Color(210, 210, 255);
    private double _speed = 1;
    private double _inc = 1;
    
    /** path of the snow picture */    
    public static final String _SNOWPICT = "img/Snow.gif";
    /** the snow picture */
    public Image _SnowPict = BaseImgSpr.getInstanceOfImage(_SNOWPICT).getScaledInstance(5,5, Image.SCALE_SMOOTH);
    
    /** the constructor
     * @param snowCount int: snow count
     * @param width int: width of the screen
     * @param height int: height of the screen
     */    
	public RainEffect(int snowCount, int width, int height) {
		_snowCount = snowCount;
		_width = width;
		_height = height;
		_SnowPointList = new ArrayList<Point>();
        Random rand = new Random();
        for (int i = 0; i < snowCount; i++) {

            double x = rand.nextInt(_width);
            double y = rand.nextInt(_height);
            _speed = rand.nextDouble() + .5f;
            _SnowPointList.add(new Point(x, y));
        }
	}
    
    private static class Point {
        double x;
        double y;
        public Point(double x, double y) {
        	this.x = x;
            this.y = y;
        }
    }
    

    /** the engime of this effect
     * @param g2D Graphics2D: graphics context
     */    
	public void SnowEngime(Graphics2D g2D) {
        for(Iterator iter = _SnowPointList.iterator(); iter.hasNext();) {
            Point p = (Point)iter.next();
            p.y += 1;
            p.x += 0.4;
            g2D.setColor(_color);
            g2D.drawImage(_SnowPict, (int) p.x,(int)p.y, null);
            if (p.y > _height) {
            	p.y = 0;
            }
            if (p.x > _width)
            	p.x = 0;
        }
	}
}
