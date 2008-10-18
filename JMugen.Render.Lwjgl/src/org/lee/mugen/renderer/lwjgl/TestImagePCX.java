package org.lee.mugen.renderer.lwjgl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import org.lee.mugen.imageIO.PCXLoader;
import org.lee.mugen.imageIO.PCXPalette;


/**
 * A utility class to load textures for JOGL. This source is based
 * on a texture that can be found in the Java Gaming (www.javagaming.org)
 * Wiki. It has been simplified slightly for explicit 2D graphics use.
 * 
 * OpenGL uses a particular image format. Since the images that are 
 * loaded from disk may not match this format this loader introduces
 * a intermediate image which the source image is copied into. In turn,
 * this image is used as source for the OpenGL texture.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class TestImagePCX extends JFrame {
	final String parentDir = "C:/dev/workspace/JMugen/ryu2/";
	public static void main(String[] args) {
		new TestImagePCX();
		
		System.out.println(System.currentTimeMillis());
		int t= 0;
		for (int i = 0; i < 640 * 480 * 50; i++) {
			t = 1 +3 + 5 + i;
			
		}
		System.out.println(System.currentTimeMillis());
		
	}
	Image image = null;
	String[] imagesList;
	int index = 0;
	public TestImagePCX() {
		setSize(600, 400);
		imagesList = new File(parentDir).list();
		try {
			image = PCXLoader.loadImage(new FileInputStream(parentDir + imagesList[index]), new PCXPalette(), false, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					index++;
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					index--;
				}
				if (index > imagesList.length -1)
					index--;
				if (index < 0)
					index++;
				
				try {
					image = PCXLoader2.loadImageColorIndexed(new FileInputStream(parentDir + imagesList[index]), new PCXPalette(), false, true);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
		setVisible(true);
	
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, 800, 800);
		g.drawString(imagesList[index], 10, 50);
		g.drawImage(image, 10, 100, null);
	}
	
	
	
}
