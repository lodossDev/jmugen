package org.lee.mugen.renderer.lwjgl;

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
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.lee.mugen.imageIO.ImageUtils;
import org.lee.mugen.imageIO.PCXLoader;
import org.lee.mugen.imageIO.PCXPalette;
import org.lee.mugen.imageIO.RawPCXImage;
import org.lee.mugen.imageIO.PCXLoader.PCXHeader;
import org.lee.mugen.renderer.AngleDrawProperties;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenDrawer;
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
			float type = 0;
			if (dp.getImageProperties().getTrans() == Trans.ADD) {
				GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
				type = 1;
			} else if (dp.getImageProperties().getTrans() == Trans.ADD1) {
				GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
				GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
				type = 2;
			} else if (dp.getImageProperties().getTrans() == Trans.SUB) {
				GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
				GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_ONE_MINUS_SRC_COLOR);
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE);
				type = 3;
			}
			RGB bits = new RGB(1f/255f, 1f/255f, 1f/255f, 1f/255f);
			getAfterImageShader().render(
					dp.getImageProperties().getPalbright().mul(bits), 
					dp.getImageProperties().getPalcontrast().mul(bits), 
					dp.getImageProperties().getPalpostbright().mul(bits),
					dp.getImageProperties().getPaladd().mul(bits),
					dp.getImageProperties().getPalmul(),
					type
			
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



	public class ImageContainerText extends ImageContainer {

		public ImageContainerText(Object img, int width, int height) {
			super(img, width, height);
		}
		
		
		private static final int RAW_PCX = 0;
		private static final int BYTE = 1;
		private static final int TEXTURE = 2;
		
		AtomicInteger imageStatus = new AtomicInteger(0);
		@Override
		public Object getImg() {
			synchronized (this) {
				if (imageStatus.get() == TEXTURE) {
					return img;
				} else if (imageStatus.get() == BYTE) {
					try {
						img = TextureLoader.getTextureLoader().getTexture((byte[]) img, width, height);
						imageStatus.set(TEXTURE);
						return img;
					} catch (Exception e) {
						throw new IllegalStateException("Ca ne devrait pas arrive", e);
					}
				} else if (imageStatus.get() == RAW_PCX) {
					RawPCXImage pcx = (RawPCXImage) img;
					try {
						BufferedImage image = (BufferedImage) PCXLoader.loadImageColorIndexed(new ByteArrayInputStream(
								pcx.getData()), pcx.getPalette(), false, true);

//						BufferedImage image = (BufferedImage) PCXLoader.loadImage(new ByteArrayInputStream(
//								pcx.getData()), pcx.getPalette(), false, true);

						img = TextureLoader.getTextureLoader().getTexture(image);
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
					TextureLoader.getTextureLoader().free((Texture) img);
			}
		}
		
		@Override
		public void reload(ImageContainer img) {
			synchronized (this) {
				ImageContainerText imgText = (ImageContainerText) img;
				this.img = imgText.img;
				this.width = img.getWidth();
				this.height = img.getHeight();
				imageStatus.set(imgText.imageStatus.get());
			}

		}
		
		public void prepareImageToTexture() {
			synchronized (this) {
				if (imageStatus.get() == RAW_PCX) {
					RawPCXImage pcx = (RawPCXImage) img;
					
					try {
						img = pcxToRawRGBA(pcx.getData());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					imageStatus.set(BYTE);
					

				}
			}
		}
		
	}
	
	private static long memoryusage = 0;
	
	
	private static final int LIST_IMG_TO_PROCESS_COUNT = 2;
	private static final int LIST_IMG_TO_PROCESS_THREAD_YELD_TIME = 100;
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
		if (currentListToAdd > IMAGE_TO_PROCESS_LIST.length - 1) {
			currentListToAdd = 0;
		}
		IMAGE_TO_PROCESS_LIST[currentListToAdd].add(img);
		currentListToAdd++;
	}
	

	private static void prepareImageToProcess(List<ImageContainerText> list) {
		Collections.sort(list, IMAGE_CONTAINER_COMPARATOR);
		for (Iterator<LMugenDrawer.ImageContainerText> iter = list.iterator(); iter.hasNext();) {
			iter.next().prepareImageToTexture();
			iter.remove();
			try {
				Thread.sleep(LIST_IMG_TO_PROCESS_THREAD_YELD_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static Comparator<ImageContainerText> IMAGE_CONTAINER_COMPARATOR = new Comparator<ImageContainerText>() {

		@Override
		public int compare(ImageContainerText o1, ImageContainerText o2) {
			return -(o1.getWidth() * o1.getHeight()) + (o2.getWidth() * o2.getHeight());
		}};
	public static void createImageToTextPreparer() {
		
		for (int i = 0; i < IMAGE_TO_PROCESS_LIST.length; ++i) {
			final int pos = i;
			new Thread() {
				@Override
				public void run() {
					prepareImageToProcess(IMAGE_TO_PROCESS_LIST[pos]);
					jobFinish[pos] = true;
				}
			}.start();
			
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
		return result;
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
        
        ImageContainerText result = new ImageContainerText(pcx , width, height);
        addToImageToProcess(result);
		return result;
			
			
	}


	
	private LwjgGameWindow gameWindow = new LwjgGameWindow();

	@Override
	public GameWindow getInstanceOfGameWindow() {
		return gameWindow;
	}

	
	
	
	static ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
			.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true,
			false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
    static ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8,8,8,0},
            false,
            false,
            ComponentColorModel.BITMASK,
            DataBuffer.TYPE_BYTE);
    
    
	public static byte[] pcxToRawRGBA(byte[] data) throws IOException {
    	BufferedImage image;

    	PCXPalette pal = new PCXPalette();
    	PCXHeader header = new PCXHeader(data);
       	pal.load(new ByteArrayInputStream(data));
        
        InputStream in = new ByteArrayInputStream(data);
        
        in.skip(128);
        int width = header.xmax - header.xmin + 1;
        int height = header.ymax - header.ymin + 1;
        
        int xp = 0;
        int yp = 0;
        int value;
        int count;
        
        int texWidth = 2;
        int texHeight = 2;
        
        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < width) {
            texWidth *= 2;
        }
        while (texHeight < height) {
            texHeight *= 2;
        }

        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
        image = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        
        while (yp < height) {
            value = in.read();
            // if the byte has the top two bits set
            if (value >= 192) {
                count = (value - 192);
                value = in.read();
            } else {
                count = 1;
            }
            
            // update data
            for (int i = 0; i < count; i++) {
                if (xp < width) {
                	int[] alpha = new int[] {255};

                     
                	if (
                	isAboutTheSameColor(
                             pal.r[value],
                             pal.g[value],
                             pal.b[value], 
                             pal.r[0], 
                             pal.g[0], 
                             pal.b[0], 
                             alpha)) {
//                		
                	} else {
                        int color = ImageUtils.getARGB(
                                alpha[0],
                                pal.r[value],
                                pal.g[value],
                                pal.b[value]);
                        image.setRGB(xp, yp, color);
                		
                	}

//                    texinfo.texels[xp + yp * width] = ImageUtils.getARGB(alpha[0], pal.r[value], pal.g[value], pal.b[value]);
//                    pal.r[value];
//                    texinfo.texels[xp * 4 + yp * width + 1] = pal.g[value];
//                    texinfo.texels[xp * 4 + yp * width + 2] = pal.b[value];
//                    texinfo.texels[xp * 4 + yp * width + 3] = alpha[0];

                    
                    // TODO Find a way to load it directly in a byte
                }
                xp++;
                if (xp == header.bytesPerLine) {
                    xp = 0;
                    yp ++;
                    break;
                }
            }
        }
        in.close();
        return ((DataBufferByte) image.getRaster().getDataBuffer()).getData(); 
	}
	
	private static boolean isAboutTheSameColor(int r1, int g1, int b1, int r2, int g2, int b2, int[] alpha) {
        alpha[0] = 255;
        if (r1 == r2 && b1 == b2 && g1 == g2) {
            alpha[0] = 0;
            return true;
        } else
            return false;
    }	
	
	
   
    

}
