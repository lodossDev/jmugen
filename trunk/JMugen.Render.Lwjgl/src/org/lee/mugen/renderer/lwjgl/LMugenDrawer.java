package org.lee.mugen.renderer.lwjgl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lee.mugen.imageIO.PCXLoader;
import org.lee.mugen.imageIO.RawPCXImage;
import org.lee.mugen.imageIO.PCXLoader.PCXHeader;
import org.lee.mugen.input.MugenDrawer;
import org.lee.mugen.renderer.AngleDrawProperties;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.renderer.Trans;
import org.lee.mugen.renderer.lwjgl.shader.AfterImageShader;
import org.lee.mugen.renderer.lwjgl.shader.PalFxShader;
import org.lwjgl.opengl.GL11;

public class LMugenDrawer extends MugenDrawer {
	private boolean isScaleByForMeDebug() {
		return false;
	}
	private void processRotationProperties(AngleDrawProperties dp) {
		if (dp != null) {
			GL11.glTranslatef(dp.getXAnchor(), dp.getYAnchor(), 0);
			GL11.glRotatef(dp.getAngleset(), 0, 0, 1);
			GL11.glTranslatef(-dp.getXAnchor(), -dp.getYAnchor(), 0);

		}

	}

	public void drawRect(float x, float y, float width, float height) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public void fillRect(float x1, float y1, float width, float height) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// GL11.glColor4f(rgb.getA(), rgb.getG(), rgb.getB(), rgb.getA());

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1 + width, y1);
		GL11.glVertex2f(x1 + width, y1 + height);
		GL11.glVertex2f(x1, y1 + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	private void drawImage(float xlDst, float xrDst, float ytDst, float ybDst,
			float xlSrc, float xrSrc, float ytSrc, float ybSrc,
			DrawProperties dp) {
		float xScale = 1f;
		float yScale = 1f;

		if (dp.getAngleDrawProperties() != null) {
			xScale = dp.getAngleDrawProperties().getXScale();
			yScale = dp.getAngleDrawProperties().getYScale();
		}

		// draw a quad textured to match the sprite
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glNormal3f(0.0f, 0.0f, 1.0f);
			// Left Bottom
			GL11.glTexCoord2f(xlSrc, ytSrc);
			// GL11.glVertex2f(0, 0);
			GL11.glVertex2f(xlDst, ytDst);

			// Left Top

			GL11.glTexCoord2f(xlSrc, ybSrc);
			GL11.glVertex2f(xlDst, (ybDst - ytDst) * dp.getYScaleFactor()
					* yScale + ytDst);

			// Right Top
			GL11.glTexCoord2f(xrSrc, ybSrc);
			GL11.glVertex2f((xrDst - xlDst) * dp.getXScaleFactor() * xScale
					+ xlDst, (ybDst - ytDst) * dp.getYScaleFactor() * yScale
					+ ytDst);

			// Right Bottom
			GL11.glTexCoord2f(xrSrc, ytSrc);
			GL11.glVertex2f((xrDst - xlDst) * dp.getXScaleFactor() * xScale
					+ xlDst, ytDst);
		}
		GL11.glEnd();

	}


	static PalFxShader palFxShader = null;

	private PalFxShader getPalFxShader() {
		if (palFxShader == null)
			palFxShader = new PalFxShader();

		return palFxShader;
	}
	
	
	static AfterImageShader afterImageShader = null;

	private AfterImageShader getAfterImageShader() {
		if (afterImageShader == null)
			afterImageShader = new AfterImageShader();

		return afterImageShader;
	}

	private void drawWithPropertiesColor(DrawProperties dp) {
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
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
		} else if (dp.getTrans() == Trans.ADD) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
		} else if (dp.getTrans() == Trans.ADDALPHA) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
							GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1f, 1f, 1f, 0.5f);
		}
		if (dp.getPalfx() != null) {

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
			getPalFxShader().render(
					dp.getPalfx().getAdd().mul(bits),
					dp.getPalfx().getMul().mul(bits),
					ampl.mul(bits));
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc,
					dp);
			getPalFxShader().endRender();
			// shader.cleanup();

		} else if (dp.getImageProperties() != null) {
			RGB bits = new RGB(1f/255f, 1f/255f, 1f/255f, 1f/255f);
			getAfterImageShader().render(
					dp.getImageProperties().getPalbright().mul(bits), 
					dp.getImageProperties().getPalcontrast().mul(bits), 
					dp.getImageProperties().getPalpostbright().mul(bits),
					dp.getImageProperties().getPaladd().mul(bits),
					dp.getImageProperties().getPalmul()
			
				);
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc,
					dp);
			getAfterImageShader().endRender();			
			
		} else {

			GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
					GL11.GL_MODULATE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if (dp.getTrans() == Trans.ADD1) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			} else if (dp.getTrans() == Trans.ADD) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			} else if (dp.getTrans() == Trans.ADDALPHA) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA,
								GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(1f, 1f, 1f, 0.5f);
			}
			drawImage(xlDst, xrDst, ytDst, ybDst, xlSrc, xrSrc, ytSrc, ybSrc,
					dp);
		}

		// GL11.glColor4f(dp.getPalcontrast().getR(),
		// dp.getPalcontrast().getG(), dp.getPalcontrast().getB(),
		// dp.getPalcontrast().getA());

		// drawAdd(dp.getPalcontrast().mul(dp.getPalcontrast()), xlDst, xrDst,
		// ytDst, ybDst,
		// xlSrc, xrSrc, ytSrc, ybSrc, dp);

		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void draw(DrawProperties dp) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColorMask(true, true, true, true);
		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
				GL11.GL_MODULATE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		Texture texture = (Texture) dp.getIc().getImg();

		// store the current model matrix
		GL11.glPushMatrix();

		// bind to the appropriate texture for this sprite
		texture.bind();
		
		
		if (isScaleByForMeDebug()) {
			scale(0.5f, 0.5f); // This scale help me to see out of screen
			GL11.glTranslated(160, 240, 0);
		}

		
		processRotationProperties(dp.getAngleDrawProperties());

		drawWithPropertiesColor(dp);
		GL11.glPopMatrix();

	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// make sure the start and end points are drawn - GL implementations
		// seem a bit flakey on this
		
		if (isScaleByForMeDebug()) {
			scale(0.5f, 0.5f); // This scale help me to see out of screen
			GL11.glTranslated(160, 240, 0);
		}
		
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x2, y2);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if (isScaleByForMeDebug()) {
			GL11.glTranslated(-160, -240, 0);
			scale(2f, 2f);
		}

		
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		GL11.glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);

	}

	@Override
	public void setColor(float r, float g, float b) {
		GL11.glColor3f(r / 255f, g / 255f, b / 255f);

	}

	@Override
	public void scale(float x, float y) {
		GL11.glScaled(x, y, 0);
	}

	static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
			.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true,
			false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);


	private class ImageContainerText extends ImageContainer {

		public ImageContainerText(Object img, int width, int height) {
			super(img, width, height);
			RawPCXImage pcx = (RawPCXImage) img;
			try {
				this.img = (BufferedImage) PCXLoader.loadImage(new ByteArrayInputStream(
							pcx.getData()), pcx.getPalette(), false, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		Texture text = null;
		AtomicBoolean isTextSet = new AtomicBoolean(false);
		@Override
		public Object getImg() {
			if (isTextSet.get()) {
				return text;
			}
			try {
				text = TextureLoader.getTextureLoader().getTexture((BufferedImage) img);
				isTextSet.set(true);
			} catch (IOException e) {
				throw new Error();
			}
			img = null;
			return text;
		}

		public void free() {
			if (text != null)
				TextureLoader.getTextureLoader().free(text);
		}
		
		@Override
		public void reload(ImageContainer img) {
			ImageContainerText imgText = (ImageContainerText) img;
			Texture textTemp = text;
			this.img = imgText.img;
			if (text != null && isTextSet.get())
				TextureLoader.getTextureLoader().free(textTemp);
			isTextSet.set(false);
			this.width = img.getWidth();
			this.height = img.getHeight();


		}
	}
	
	
	@Override
	public ImageContainer getImageContainer(Object imageData) {
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
        
        ImageContainer result = new ImageContainerText(pcx , width, height);
        
		return result;
			
			
	}

	private ByteBuffer convertImageData(BufferedImage bufferedImage, int[] wh) {
        ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        int texWidth = 2;
        int texHeight = 2;
        
        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }
        
        wh[1] = texHeight;
        wh[0] = texWidth;
        
        // create a raster that can be used by OpenGL as a source
        // for a texture
        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
        texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
            
        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);
        
        // build a byte buffer from the temporary image 
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        
        return imageBuffer; 
    }

	private LwjgGameWindow gameWindow = new LwjgGameWindow();

	@Override
	public GameWindow getInstanceOfGameWindow() {
		return gameWindow;
	}

}
