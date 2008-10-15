package img;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/** Just an abstract class to get Sprite image with out double
 * @author Dr Wong
 */
public abstract class BaseImgSpr {
    private static HashMap<String, Image> _ImgMap = new HashMap<String, Image>();
    /** <p>Image can have differents size,
     * then how to draw it if you have a x, y position.
     * Simple : draw(x + _XPadding, y + _yPadding)</p>
     */
    protected int _XPadding;
    /** <p>Image can have differents size,
     * then how to draw it if you have a x, y position.
     * Simple : draw(x + _XPadding, y + _yPadding)</p>
     */
    protected int _YPadding;
    /** just an Image of one tick time of the sprite */
    protected transient Image _ImageSprite;
    

    
    /** get the map where is stock all images an their offset
     * @return HashMap of images and their offset (Key: Path - Value: BaseImgSpr)
     */    
    public static HashMap getMap() {
    	return _ImgMap;
    }
    
    /** reverse an image that are load 
     * @throws IOException*/    
    protected void reverse() throws IOException {
        URL url = null;
    	try {
			url = new URL("file:" + System.getProperty("user.dir") + System.getProperty("file.separator") + _ImageSprite);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(1);
        }
        Image img = ImageIO.read(url);
        Graphics g = img.getGraphics();
        g.drawImage(_ImageSprite,
        		    _ImageSprite.getWidth(null), 0, 0, _ImageSprite.getHeight(null),
                     0, 0, _ImageSprite.getWidth(null), _ImageSprite.getHeight(null),
                     null);
        g.dispose();
        _ImageSprite = img;
    }
    
    
    /** create a new instance of the Image you need or if it exist you get it.
     * @param sName String: path of the image
     * @return Image: return Image you want
     * @throws 
     */
    public static Image getInstanceOfImage(final String sName) {
        Image img = (Image)_ImgMap.get(sName);
        if (null == _ImgMap.get(sName)) {
            URL url = BaseImgSpr.class.getResource("/" + sName);
            try {
				img = getRawImage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
            _ImgMap.put(sName, img);
        }
        return img;
    }
    
    private static Image getRawImage(final URL sUrl) throws IOException {
       Image img = new ImageIcon(sUrl).getImage();
       return img;
    }

    /** return the image of the sprite
     * @return Image: the image of the sprite
     */
    public Image getImage() {
        return _ImageSprite;
    }
    
    /** <p>Image can have differents size,
     * then how to draw it if you have a x, y position.
     * Simple : draw(x + _XPadding, y + _yPadding)</p>
     * @return int : X padding
     */
    public int getXPad() {
        return _XPadding;
    }
    
    /** <p>Image can have differents size,
     * then how to draw it if you have a x, y position.
     * Simple : draw(x + _XPadding, y + _yPadding)</p>
     * @return int: y Padding
     */
    public int getYPad() {
        return _YPadding;
    }
}
