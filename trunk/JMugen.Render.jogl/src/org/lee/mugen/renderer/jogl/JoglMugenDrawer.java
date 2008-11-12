package org.lee.mugen.renderer.jogl;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.media.opengl.GL;

import org.lee.mugen.imageIO.PCXLoader;
import org.lee.mugen.imageIO.RawPCXImage;
import org.lee.mugen.imageIO.PCXLoader.PCXHeader;
import org.lee.mugen.object.Rectangle;
import org.lee.mugen.renderer.AngleDrawProperties;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.renderer.Trans;
import org.lee.mugen.renderer.jogl.shader.AfterImageShader;
import org.lee.mugen.renderer.jogl.shader.PalFxShader;
import org.lee.mugen.util.Logger;

import com.sun.opengl.util.j2d.TextureRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import com.sun.opengl.util.texture.spi.TextureProvider;

public class JoglMugenDrawer extends MugenDrawer {
	private JoglGameWindow gameWindow = new JoglGameWindow();
	
	//
	private RGB rgba = new RGB();
	
	
	private static PalFxShader palFxShader;
	private static AfterImageShader afterImageShader;
	
	//
	
	public PalFxShader getPalFxShader() {
		if (palFxShader == null) {
			palFxShader = new PalFxShader();
			palFxShader.compileShader(gameWindow.getGl());
		}
		return palFxShader;
	}
	
	public AfterImageShader getAfterImageShader() {
		if (afterImageShader == null) {
			afterImageShader = new AfterImageShader();
			afterImageShader.compileShader(gameWindow.getGl());
		}
		return afterImageShader;
	}
	private boolean isScaleByForMeDebug;
	public void setScaleByForMeDebug(boolean isScaleByForMeDebug) {
		this.isScaleByForMeDebug = isScaleByForMeDebug;
	}

	public boolean isScaleByForMeDebug() {
		return isScaleByForMeDebug;
	}
	
	private void drawImage(float xlDst, float xrDst, float ytDst, float ybDst,
			float xlSrc, float xrSrc, float ytSrc, float ybSrc,
			DrawProperties dp) {
		GL gl = getGl();
		if (gl == null)
			return;
		float xScale = 1f;
		float yScale = 1f;

		if (dp.getAngleDrawProperties() != null) {
			xScale = dp.getAngleDrawProperties().getXScale();
			yScale = dp.getAngleDrawProperties().getYScale();
		}

		// draw a quad textured to match the sprite
		Texture texture = (Texture) dp.getIc().getImg();
		TextureCoords coords = texture.getImageTexCoords();
		gl.glBegin(GL.GL_QUADS);
		{
			
			gl.glNormal3f(0.0f, 0.0f, 1.0f);
			// Left Bottom
//			gl.glTexCoord2f(coords.left(), coords.top());
			gl.glTexCoord2f(xlSrc/texture.getWidth(), ytSrc/texture.getHeight());
			// gl.glVertex2f(0, 0);
			gl.glVertex2f(xlDst, ytDst);

			// Left Top

//			gl.glTexCoord2f(coords.left(), coords.bottom());
			gl.glTexCoord2f(xlSrc/texture.getWidth(), ybSrc/texture.getHeight());
			gl.glVertex2f(xlDst, (ybDst - ytDst) * dp.getYScaleFactor()
					* yScale + ytDst);

			// Right Top
//			gl.glTexCoord2f(coords.right(), coords.bottom());
			gl.glTexCoord2f(xrSrc/texture.getWidth(), ybSrc/texture.getHeight());
			gl.glVertex2f((xrDst - xlDst) * dp.getXScaleFactor() * xScale
					+ xlDst, (ybDst - ytDst) * dp.getYScaleFactor() * yScale
					+ ytDst);

			// Right Bottom
//			gl.glTexCoord2f(coords.right(), coords.top());
			gl.glTexCoord2f(xrSrc/texture.getWidth(), ytSrc/texture.getHeight());
			gl.glVertex2f((xrDst - xlDst) * dp.getXScaleFactor() * xScale
					+ xlDst, ytDst);
		}
		gl.glEnd();
	}
	
	private void drawWithPropertiesColor(DrawProperties dp) {
		GL gl = getGl();
		if (gl == null)
			return;
		
		Texture texture = (Texture) dp.getIc().getImg();
		float xlDst = dp.getXLeftDst();
		float xrDst = dp.getXRightDst();
		float ytDst = dp.getYTopDst();
		float ybDst = dp.getYBottomDst();

		float xlSrc = (dp.getXLeftSrc() / texture.getImageWidth())
				* texture.getWidth();
		float xrSrc = (dp.getXRightSrc() / texture.getImageWidth())
				* texture.getWidth();

		float ytSrc = (dp.getYTopSrc() / texture.getImageHeight())
				* texture.getHeight();
		float ybSrc = (dp.getYBottomSrc() / texture.getImageHeight())
				* texture.getHeight();

		ImageContainer ic = dp.getIc();
		if (dp.getTrans() == Trans.ADD1) {
			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_ADD);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
		} else if (dp.getTrans() == Trans.ADD) {
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
		} else if (dp.getTrans() == Trans.ADDALPHA) {
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			gl.glColor4f(1f, 1f, 1f, 0.5f);
		}
		if (dp.getPalfx() != null) {
			float type = 0;
			if (dp.getTrans() == Trans.ADD) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_SUBTRACT);
				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA);
				type = 1;
			} else if (dp.getTrans() == Trans.ADD1) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);

				type = 2;
			} else if (dp.getTrans() == Trans.SUB) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_SUBTRACT);
				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA);
				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_ALPHA, GL.GL_ONE_MINUS_SRC_COLOR);
				gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ONE);
				type = 3;
			}
			float alpha = (float) (Math.PI * dp.getPalfx().getTimeActivate() / dp
					.getPalfx().getSinadd().getPeriod());

			int rPlus = (int) (dp.getPalfx().getSinadd().getAmpl_r() * Math
					.sin(2 * alpha));
			int gPlus = (int) (dp.getPalfx().getSinadd().getAmpl_g() * Math
					.sin(2 * alpha));
			int bPlus = (int) (dp.getPalfx().getSinadd().getAmpl_b() * Math
					.sin(2 * alpha));

			RGB ampl = new RGB(rPlus, gPlus, bPlus, 255f);
			RGB bits = new RGB(1f/255f, 1f/255f, 1f/255f, 1f/255f);
			getPalFxShader().render(gl,
					dp.getPalfx().getAdd().mul(bits),
					dp.getPalfx().getMul().mul(bits),
					ampl.mul(bits), dp.getAlpha());
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc, dp);
			getPalFxShader().endRender(gl);

		} else if (dp.getImageProperties() != null) {
			float type = 0;
			if (dp.getImageProperties().getTrans() == Trans.ADD) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
				gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
				type = 1;
			} else if (dp.getImageProperties().getTrans() == Trans.ADD1) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA);
				type = 2;
			} else if (dp.getImageProperties().getTrans() == Trans.SUB) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
//				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA);
//				gl.glBlendFunc(GL.GL_ONE_MINUS_DST_ALPHA, GL.GL_ONE_MINUS_SRC_COLOR);
				gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ONE);
				type = 3;
			}
			
			
			RGB bits = new RGB(1f/255f, 1f/255f, 1f/255f, 1f);
			
			getAfterImageShader().render(gl,
					dp.getImageProperties().getPalbright().mul(bits), 
					dp.getImageProperties().getPalcontrast().mul(bits), 
					dp.getImageProperties().getPalpostbright().mul(bits),
					dp.getImageProperties().getPaladd().mul(bits),
					dp.getImageProperties().getPalmul(),
					type,
					dp.getAlpha()
				);
			
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc, dp);
			getAfterImageShader().endRender(gl);			

			
		} else {

			gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
					GL.GL_MODULATE);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

			float type = 0;
			gl.glColor4f(1f, 1f, 1f, dp.getAlpha());
			if (dp.getTrans() == Trans.ADD) {
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
				gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_DST_ALPHA);
				
				type = 1;
			} else if (dp.getTrans() == Trans.ADD1) {

				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_DST_ALPHA);
				type = 2;
			} else if (dp.getTrans() == Trans.SUB) {
				
				gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_COMBINE);
				gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_RGB, GL.GL_SUBTRACT);
				gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_COMBINE_ALPHA, GL.GL_SUBTRACT);
				gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_SRC0_RGB, GL.GL_PREVIOUS);
				gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_SRC1_RGB, GL.GL_TEXTURE);
				gl.glBlendFunc (GL.GL_DST_COLOR, GL.GL_SRC_COLOR);
				type = 3;
				float coef = 0.5f;
				
				gl.glColor4f(coef, coef, coef, dp.getAlpha());
				
			}
			
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc, dp);
			
		}

		gl.glDisable(GL.GL_ALPHA_TEST);
	}
	
	
	private void processRotationProperties(AngleDrawProperties dp) {
		GL gl = getGl();
		if (gl == null)
			return;
		if (dp != null) {
			gl.glTranslatef(dp.getXAnchor(), dp.getYAnchor(), 0);
			gl.glRotatef(dp.getAngleset(), 0, 0, 1);
			gl.glTranslatef(-dp.getXAnchor(), -dp.getYAnchor(), 0);

		}
	}
	
	///
	
	@Override
	public GameWindow getInstanceOfGameWindow() {
		return gameWindow;
	}
	
	private GL getGl() {
		return gameWindow.getGl();
	}

	TextureRenderer animRenderer = null;
	JoglTextureDrawer joglTextureDrawer = new JoglTextureDrawer();
	
	public JoglTextureDrawer getJoglTextureDrawer() {
		return joglTextureDrawer;
	}

	private List<DrawProperties> oldDp = new LinkedList<DrawProperties>();
	@Override
	public void draw(DrawProperties dp) {
		GL gl = getGl();
		if (gl == null)
			return;

		if (dp.getIc().getImg() instanceof BufferedImage) {
			Graphics2D g = ((JoglMugenDrawer)GraphicsWrapper.getInstance()).getJoglTextureDrawer().getBackBuffer().createGraphics();
	        g.setBackground(new Color(0,0,0,0));
	        g.clearRect(0, 0, 640, 480);
//			((JoglMugenDrawer)GraphicsWrapper.getInstance()).getJoglTextureDrawer().getBackBuffer().markDirty(
//					0, 
//					0, 
//					640, 
//					480
//					);
			joglTextureDrawer.draw(dp);

			for (DrawProperties d: oldDp) {
				((JoglMugenDrawer)GraphicsWrapper.getInstance()).getJoglTextureDrawer().getBackBuffer().markDirty(
						(int) (d.getXLeftDst()), 
						(int) (d.getYTopDst()), 
						(int) Math.abs(d.getXRightDst() - d.getXLeftDst()), 
						(int) Math.abs(d.getYTopDst() - d.getYBottomDst())
						);
			}
			oldDp.clear();
			oldDp.add(dp);
			((JoglMugenDrawer)GraphicsWrapper.getInstance()).getJoglTextureDrawer().getBackBuffer().markDirty(
					(int) (dp.getXLeftDst()), 
					(int) (dp.getYTopDst()), 
					(int) Math.abs(dp.getXRightDst() - dp.getXLeftDst()), 
					(int) Math.abs(dp.getYTopDst() - dp.getYBottomDst())
					);

			
	        TextureRenderer textRender = ((JoglMugenDrawer)GraphicsWrapper.getInstance()).getJoglTextureDrawer().getBackBuffer();

	        
	        Texture tex = textRender.getTexture();
	        TextureCoords tc = tex.getImageTexCoords();
	        float tx1 = tc.left();
	        float ty1 = tc.top();
	        float tx2 = tc.right();
	        float ty2 = tc.bottom();

	        // Enable blending, using the SrcOver rule
	        gl.glEnable(GL.GL_BLEND);
	        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);

	        // Use the GL_MODULATE texture function to effectively multiply
	        // each pixel in the texture by the current alpha value
	        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

	        float x = 0;
	        float y = 0;
	        float w = 640;
	        float h = 480;

	        tex.bind();
	        tex.enable();
	        gl.glBegin(GL.GL_QUADS);
	        // Render image right-side up
	        float a = 1f;
	        gl.glColor4f(a, a, a, a);
	        gl.glTexCoord2f(tx1, ty2); gl.glVertex3f(x  , y+h, 0f);
	        gl.glTexCoord2f(tx2, ty2); gl.glVertex3f(x+w, y+h, 0f);
	        gl.glTexCoord2f(tx2, ty1); gl.glVertex3f(x+w, y  , 0f);
	        gl.glTexCoord2f(tx1, ty1); gl.glVertex3f(x  , y  , 0f);
	        gl.glEnd();
			return;
		}
		Texture texture = (Texture) dp.getIc().getImg();


		// store the current model matrix
		gl.glPushMatrix();

		// bind to the appropriate texture for this sprite

		texture.bind();
		
		gl.glEnable(GL.GL_TEXTURE_2D);

		gl.glColorMask(true, true, true, true);
		gl.glDisable(GL.GL_COLOR_LOGIC_OP);
		gl.glDisable(GL.GL_BLEND);
//		gl.glDisable(GL.GL_ALPHA_TEST);

		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
//		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_COLOR, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1f, 1f, 1f, 1f);
		
		if (isScaleByForMeDebug()) {
			scale(0.5f, 0.5f); // This scale help me to see out of screen
			gl.glTranslated(160, 240, 0);
		}
		
//		
//		float coef = 1.5f;
//		scale(coef, coef);
//		gl.glTranslated(0, -120*(coef-1), 0);
		processRotationProperties(dp.getAngleDrawProperties());

		drawWithPropertiesColor(dp);
		gl.glPopMatrix();
//		texture.disable();
	

	}
	
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		GL gl = getGl();
		if (gl == null)
			return;		
		if (isScaleByForMeDebug()) {
			scale(0.5f, 0.5f); // This scale help me to see out of screen
			gl.glTranslated(160, 240, 0);
		}
		
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x2, y2);
		gl.glEnd();
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		if (isScaleByForMeDebug()) {
			gl.glTranslated(-160, -240, 0);
			scale(2f, 2f);
		}
	}

	@Override
	public void drawRect(float x, float y, float width, float height) {
		GL gl = getGl();
		if (gl == null)
			return;
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex2f(x, y);
		gl.glVertex2f(x + width, y);
		gl.glVertex2f(x + width, y + height);
		gl.glVertex2f(x, y + height);
		gl.glVertex2f(x, y);
		gl.glEnd();
		gl.glEnable(GL.GL_TEXTURE_2D);
	}

	@Override
	public void fillRect(float x1, float y1, float width, float height) {
		GL gl = getGl();
		if (gl == null)
			return;
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glColor4f(rgba.getR(), rgba.getG(), rgba.getB(), rgba.getA());

		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2f(x1, y1);
		gl.glVertex2f(x1 + width, y1);
		gl.glVertex2f(x1 + width, y1 + height);
		gl.glVertex2f(x1, y1 + height);
		gl.glEnd();
		gl.glEnable(GL.GL_TEXTURE_2D);
	}



	@Override
	public void scale(float x, float y) {
		GL gl = getGl();
		if (gl == null)
			return;
		gl.glScaled(x, y, 0);
	}


	@Override
	public void setColor(float r, float g, float b, float a) {
		GL gl = getGl();
		if (gl == null)
			return;
		gl.glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);

	}

	@Override
	public void setColor(float r, float g, float b) {
		GL gl = getGl();
		if (gl == null)
			return;
		gl.glColor3f(r / 255f, g / 255f, b / 255f);
		rgba = new RGB(r, g, b, 1f);

	}	

	public class ImageContainerText extends ImageContainer {
		int color;
		public ImageContainerText(int color, Object img, int width, int height) {
			super(img, width, height);
			this.color = color;
		}
		public ImageContainerText(Object img, int width, int height) {
			super(img, width, height);
		}
		
		
		private static final int RAW_PCX = 0;
		private static final int DATA = 1;
		private static final int TEXTURE = 2;
		
		AtomicInteger imageStatus = new AtomicInteger(0);
		@Override
		public Object getImg() {
			synchronized (this) {
				if (imageStatus.get() == TEXTURE) {
					return img;
				} else if (imageStatus.get() == DATA) {
					try {
						
						Texture texture = TextureIO.newTexture((TextureData)img);
						texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
						texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
						texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
						texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
						img = texture;
						imageStatus.set(TEXTURE);
						return img;
					} catch (Exception e) {
						throw new IllegalStateException("Ca ne devrait pas arrive", e);
					}
				} else if (imageStatus.get() == RAW_PCX) {
					RawPCXImage pcx = (RawPCXImage) img;
					try {
						BufferedImage image = (BufferedImage) PCXLoader.loadImageColorIndexed(new ByteArrayInputStream(
								pcx.getData()), pcx.getPalette(), false, true, color);

						Texture texture = TextureIO.newTexture((BufferedImage)image, false);
						texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
						texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
						texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
						texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
						img = texture;
						imageStatus.set(TEXTURE);
						return img;
					} catch (IOException e) {
						throw new IllegalStateException("Ca ne devrait pas arrive");
					}
				}
			}
			throw new IllegalStateException();
		}

		public void free() {
			synchronized (this) {
				if (imageStatus.get() == TEXTURE && img != null)
					((Texture)img).dispose();
			}
		}
		
		@Override
		public void reload(ImageContainer img) {
			synchronized (this) {
				
//				if (imageStatus.get() == TEXTURE) {
//					Texture oldImg = (Texture)this.img;
//					oldImg.disable();
//					oldImg.dispose();
//						
//				}
				ImageContainerText imgText = (ImageContainerText) img;
				this.img = imgText.img;
				this.width = img.getWidth();
				this.height = img.getHeight();
				imageStatus.set(imgText.imageStatus.get());
//				prepareImageToTexture();
			}

		}
		
		public void prepareImageToTexture() {
			synchronized (this) {
				if (imageStatus.get() == RAW_PCX) {
					RawPCXImage pcx = (RawPCXImage) img;
					
					try {
						BufferedImage image = (BufferedImage) PCXLoader.loadImageColorIndexed(new ByteArrayInputStream(pcx.getData()), pcx.getPalette(), false, true);
						img = TextureIO.newTextureData(image, false);
//						img = image;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					imageStatus.set(DATA);
					

				}
			}
		}
		
	}
	
	
	//////////////////////
	private static final int LIST_IMG_TO_PROCESS_COUNT = 1;
	private static final int LIST_IMG_TO_PROCESS_THREAD_YELD_TIME = 0;
	private static List<ImageContainerText>[] IMAGE_TO_PROCESS_LIST = null;
	private static boolean[] jobFinish = new boolean[LIST_IMG_TO_PROCESS_COUNT];
	private static int currentListToAdd = 0;
	
	
	private static void addToImageToProcess(ImageContainerText img) {
		
		if (IMAGE_TO_PROCESS_LIST == null) {
			IMAGE_TO_PROCESS_LIST = new List[LIST_IMG_TO_PROCESS_COUNT];
			for (int i = 0; i < LIST_IMG_TO_PROCESS_COUNT; i++) {
				IMAGE_TO_PROCESS_LIST[i] = new LinkedList<ImageContainerText>();
			}
		}
		if (LIST_IMG_TO_PROCESS_COUNT <= 0)
			return;
		if (currentListToAdd > IMAGE_TO_PROCESS_LIST.length - 1) {
			currentListToAdd = 0;
		}
		IMAGE_TO_PROCESS_LIST[currentListToAdd].add(img);
		currentListToAdd++;
	}
	

	private static void prepareImageToProcess(List<ImageContainerText> list) {
		Collections.sort(list, IMAGE_CONTAINER_COMPARATOR);
		LinkedList<ImageContainerText> alist = new LinkedList<ImageContainerText>();
		alist.addAll(list);
		list.removeAll(alist);
		for (Iterator<ImageContainerText> iter = alist.iterator(); iter.hasNext();) {
			iter.next().prepareImageToTexture();
			if (LIST_IMG_TO_PROCESS_THREAD_YELD_TIME > 0) {
				try {
					Thread.sleep(LIST_IMG_TO_PROCESS_THREAD_YELD_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	private static Comparator<ImageContainerText> IMAGE_CONTAINER_COMPARATOR = new Comparator<ImageContainerText>() {

		@Override
		public int compare(ImageContainerText o1, ImageContainerText o2) {
			return -(o1.getWidth() * o1.getHeight()) + (o2.getWidth() * o2.getHeight());
		}};
		public static void createImageToTextPreparer() {
			
			for (int i = 0;IMAGE_TO_PROCESS_LIST != null && i < IMAGE_TO_PROCESS_LIST.length; ++i) {
				final int pos = i;
				new Thread() {
					@Override
					public void run() {
						prepareImageToProcess(IMAGE_TO_PROCESS_LIST[pos]);
						jobFinish[pos] = true;
					}
				}.run();
				
			}
		}
		
		public static void prepareImageToTextPreparer() {
			
			for (int i = 0;IMAGE_TO_PROCESS_LIST != null && i < IMAGE_TO_PROCESS_LIST.length; ++i) {
				final int pos = i;
				new Thread() {
					@Override
					public void run() {
						prepareImageToProcess(IMAGE_TO_PROCESS_LIST[pos]);
						jobFinish[pos] = true;
					}
				}.run();
				
			}
		}
	
	public static void newThreadJob() {
		jobFinish = new boolean[LIST_IMG_TO_PROCESS_COUNT];
	}
	
	public static boolean isConverImageToBufferFinish() {
		boolean result = true;
		for (boolean b: jobFinish) {
			result = result && b;
		}
		if (result)
			Logger.log("No More Texture To Load");
		return result;
	}

	@Override
	public ImageContainer getImageContainer(Object imageData) {
		if (imageData instanceof RawPCXImage) {
			RawPCXImage pcx = (RawPCXImage) imageData;
			
			PCXHeader header = null;

	    	byte[] data = pcx.getData();
	        
	        try {
				header = new PCXHeader(data);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        int width = header.xmax - header.xmin + 1;
	        int height = header.ymax - header.ymin + 1;
	        
	        ImageContainerText result = new ImageContainerText(pcx , width, height);
	        addToImageToProcess(result);
	        return result;
		} else if (imageData instanceof BufferedImage) {
			
			return null;
		}
		throw new IllegalArgumentException();

		
			
			
	}

	@Override
	public ImageContainer getImageContainer(Object imageData, int colors) {
		RawPCXImage pcx = (RawPCXImage) imageData;
		
		PCXHeader header = null;

    	byte[] data = pcx.getData();
        
        try {
			header = new PCXHeader(data);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        int width = header.xmax - header.xmin + 1;
        int height = header.ymax - header.ymin + 1;
        
        ImageContainerText result = new ImageContainerText(colors, pcx , width, height);
        addToImageToProcess(result);
		return result;
			
			
	}
	@Override
	public void setClip(Rectangle r) {
		GL gl = getGl();
		if (gl == null)
			return;
		if (r != null) {
			r = (Rectangle) r.clone();
			
			int xl = r.getX1() * 2;
			int yt = r.getY1() * 2;
			int xr = r.getX2() * 2;
			int yb = r.getY2() * 2;
			
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(0, xr-xl, (yb-yt), 0, -10000, 10000);
			gl.glViewport(xl, 480 - yb, xr-xl, (yb-yt));
//			
			gl.glScaled(2f, 2f, 0);
			
		} else {
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(0, 640, 640, 0, -10000, 10000);
			gl.glViewport(0, 0, 640, 480);
			gl.glScaled((float) 640 / 320, (float) 640 / 240, 0);
		}
		
	}
}
