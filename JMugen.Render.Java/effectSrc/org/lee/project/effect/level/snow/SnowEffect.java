/*
 * Created on 25 mars 2004
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.lee.project.effect.level.snow;

import img.BaseImgSpr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/** Snow Effect
 * @author Dr Wong
 */
public class SnowEffect {
    private final int _width;
    private final int _height;
    private final int _snowCount;
    private final List<Point> _SnowPointList;
    private final Color _color = new Color(210, 210, 255);
    private double _speed = 1;
    private double _inc = 1;
    
    /** Path where is the snow pict */
    public static final String _SNOWPICT = "img/Snow.gif";
    /** Simple snow picture */
    public Image _SnowPict = BaseImgSpr.getInstanceOfImage(_SNOWPICT).getScaledInstance(5,5, Image.SCALE_SMOOTH);
    
    /** Constructor
     * @param snowCount int: Snow Count
     * @param width int: width of the area
     * @param height int: height of the area
     */
    public SnowEffect(int snowCount, int width, int height) {
        
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
    
    
    /** the engime that draw Snows
     * @param g2D Graphics2D: the graphics Context
     */
    public void SnowEngime(Graphics2D g2D) {
        
        for(Iterator iter = _SnowPointList.iterator(); iter.hasNext();) {
            Point p = (Point)iter.next();
            p.y += 1;
            _inc += 1;
            double x = p.x + Math.sin(p.y/10)*10;
            g2D.setColor(_color);
            g2D.drawImage(_SnowPict, (int) x,(int)p.y, null);
            if (p.y > _height) {
                p.y = 0;
                _inc = 1;
            }
        }
    }
    
}
