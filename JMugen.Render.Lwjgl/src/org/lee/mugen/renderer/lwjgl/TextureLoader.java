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
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lee.mugen.imageIO.RawPCXImage;
import org.lwjgl.BufferUtils;
import org.lwjgl.devil.IL;
import org.lwjgl.devil.ILU;
import org.lwjgl.opengl.ARBTextureCompression;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.glu.GLU;


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
public class TextureLoader {
	static final TextureLoader instance = new TextureLoader();
	public static TextureLoader getTextureLoader() {
		return instance;
	}
    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;
    
    /** The colour model for the GL image */
    private ColorModel glColorModel;
    
    /** 
     * Create a new texture loader based on the game panel
     *
     * @param gl The GL content in which the textures should be loaded
     */
    private TextureLoader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);
                                            
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    }
    
    /**
     * Create a new texture ID 
     *
     * @return A new texture ID
     */
    private int createTextureID() 
    { 
       IntBuffer tmp = createIntBuffer(1); 
       GL11.glGenTextures(tmp); 
       return tmp.get(0);
    } 
    
    /**
     * Load a texture
     *
     * @param resourceName The location of the resource to load
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(BufferedImage img) throws IOException {
        Texture tex = getTexture(img,
                GL11.GL_TEXTURE_2D, // target
                GL11.GL_RGBA,     // dst pixel format
                GL11.GL_NEAREST, // min filter (unused)
                GL11.GL_NEAREST);
//        Texture tex = getTexture(img,
//                GL11.GL_TEXTURE_2D, // target
//                GL11.GL_RGBA,     // dst pixel format
//                GL11.GL_LINEAR, // min filter (unused)
//                GL11.GL_LINEAR);
        
        return tex;
    }
    public Texture loadTextureFromPCX(RawPCXImage pcx) throws IOException {
    	
		return loadTexture(new ByteArrayInputStream(pcx.getData()), false, IL.IL_PCX);
    }
    public Texture loadTexture(InputStream in, boolean flip, int type) throws IOException {
    	  Texture texture = null;
    	  ByteBuffer imageData = null;
    	  int ilImageHandle;
    	  int oglImageHandle;
    	  IntBuffer scratch = BufferUtils.createIntBuffer(1);
    	 
    	  // create image in DevIL and bind it
    	  IL.ilGenImages(scratch);
    	  IL.ilBindImage(scratch.get(0));
    	  ilImageHandle = scratch.get(0);
    	 
    	  // load the image
    	  if(!IL.ilLoadFromStream(in, type)) {
    	    return null;
    	  }
    	 
    	  // convert image to RGBA
    	  IL.ilConvertImage(IL.IL_RGBA, IL.IL_BYTE);
    	 
    	  // flip if needed
    	  if(flip) {
    	    ILU.iluFlipImage();
    	  }
    	 
    	  // get image attributes
    	  int width = IL.ilGetInteger(IL.IL_IMAGE_WIDTH);
    	  int height = IL.ilGetInteger(IL.IL_IMAGE_HEIGHT);
    	  int textureWidthSize = get2Fold(width);
    	  int textureHeightSize = get2Fold(height);
    	 
    	  // resize image according to poweroftwo
    	  if (textureWidthSize != width || textureHeightSize != height) {
    	    imageData = BufferUtils.createByteBuffer(textureWidthSize * textureHeightSize * 4);
    	    IL.ilCopyPixels(0, 0, 0, textureWidthSize, textureHeightSize, 1, IL.IL_RGBA, IL.IL_BYTE, imageData);
    	  } else {
    	    imageData = IL.ilGetData();
    	  }
    	 
    	  // create OpenGL counterpart
    	  GL11.glGenTextures(scratch);
    	  GL11.glBindTexture(GL11.GL_TEXTURE_2D, scratch.get(0));
    	  oglImageHandle = scratch.get(0);
    	 
    	  GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    	  GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    	  GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureWidthSize, textureHeightSize, 
    	                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
    	 
    	  // Create image (either resized by copying, else directly from IL)
    	  if (textureWidthSize != width || textureHeightSize != height) {
    	    texture = new Texture(oglImageHandle, width, height, (width / (float) textureWidthSize), 
    	                         (height / (float) textureHeightSize), textureWidthSize, textureHeightSize);
    	  } else {
    	    texture = new Texture(oglImageHandle, width, height);
    	  }
    	 
    	  // delete Image in DevIL
    	  scratch.put(0, ilImageHandle);
    	  IL.ilDeleteImages(scratch);
    	 
    	  // revert the gl state back to the default so that accidental texture binding doesn't occur
    	  GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	 
    	  // return OpenGL texture handle
    	  return texture;
    	}
    /**
     * Load a texture into OpenGL from a image reference on
     * disk.
     *
     * @param resourceName The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimising filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws IOException Indicates a failure to access the resource
     */
    public Texture getTexture(BufferedImage img, 
                              int target, 
                              int dstPixelFormat, 
                              int minFilter, 
                              int magFilter) throws IOException 
    { 
        int srcPixelFormat = 0;
        
        // create the texture ID for this texture 
        int textureID = createTextureID(); 
        Texture texture = new Texture(target,textureID); 
        
        // bind this texture 
        GL11.glBindTexture(target, textureID); 
 
        BufferedImage bufferedImage = img;
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL11.GL_RGBA;
        } else {
            srcPixelFormat = GL11.GL_RGB;
        }

        // convert that image into a byte buffer of texture data 
//        Logger.log("Transform to ByteBuffer");
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture); 
//        Logger.log("End Transform to ByteBuffer");
        if (target == GL11.GL_TEXTURE_2D) 
        { 
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        } 
 
        // produce a texture from the byte buffer
//        Logger.log("glTexImage2D to ByteBuffer");
//        GL11.glTexImage2D(target, 
//        				0, 
//        				ARBTextureCompression.GL_COMPRESSED_RGBA_ARB, 
//        				get2Fold(bufferedImage.getWidth()), 
//        				get2Fold(bufferedImage.getHeight()), 
//        				0, 
//        				srcPixelFormat, 
//        				GL11.GL_UNSIGNED_BYTE, 
//        				textureBuffer);
        
        GL11.glTexImage2D(target, 
                      0, 
                      dstPixelFormat, 
                      get2Fold(bufferedImage.getWidth()), 
                      get2Fold(bufferedImage.getHeight()), 
                      0, 
                      srcPixelFormat, 
                      GL11.GL_UNSIGNED_BYTE, 
                      textureBuffer ); 
//        Logger.log("end glTexImage2D to ByteBuffer");
        return texture; 
    } 
    
    /**
     * Get the closest greater power of 2 to the fold number
     * 
     * @param fold The target number
     * @return The power of 2
     */
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    } 
    
    /**
     * Convert the buffered image to a texture
     *
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
     * @return A buffer containing the data
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage,Texture texture) { 
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
        
        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);
        
        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }
            
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
    
    /** 
     * Load a given resource as a buffered image
     * 
     * @param ref The location of the resource to load
     * @return The loaded buffered image
     * @throws IOException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(String ref) throws IOException 
    { 
        URL url = TextureLoader.class.getClassLoader().getResource(ref);
        
        if (url == null) {
            throw new IOException("Cannot find: "+ref);
        }
        
        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref))); 
 
        return bufferedImage;
    }
    
    /**
     * Creates an integer buffer to hold specified ints
     * - strictly a utility method
     *
     * @param size how many int to contain
     * @return created IntBuffer
     */
    protected IntBuffer createIntBuffer(int size) {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }    
    
    
    public Texture createTexture(final int width, final int height) throws IOException {
    	int textWidth = get2Fold(width);
    	int texHeight = get2Fold(height);
    	ByteBuffer buffer = BufferUtils.createByteBuffer(textWidth * texHeight * 4);
    	return getTexture(32, width, height, textWidth, texHeight, buffer, GL11.GL_NEAREST, GL11.GL_RGBA);
    }
    
    public Texture getTexture(int detpth, int width, int height, int texWidth, int texHeight, ByteBuffer byteBuffer, int filter, int dstPixelFormat) throws IOException
    { 
    	int target = GL11.GL_TEXTURE_2D;
    	
        // create the texture ID for this texture 
        int textureID = createTextureID(); 
        Texture texture = new Texture(target ,textureID); 
        
        int minFilter = filter;
        int magFilter = filter;
        boolean flipped = false;
        
        // bind this texture 
        GL11.glBindTexture(target, textureID); 
 
        ByteBuffer textureBuffer;

        
        boolean hasAlpha;
    	textureBuffer = byteBuffer;
    	
    	hasAlpha = detpth == 32;
    	
    	texture.setTextureWidth(texWidth);
    	texture.setTextureHeight(texHeight);

        
        int srcPixelFormat = hasAlpha ? GL11.GL_RGBA : GL11.GL_RGB;
        int componentCount = hasAlpha ? 4 : 3;
        
        texture.setWidth(width);
        texture.setHeight(height);
//        texture.setAlpha(hasAlpha);
        
        IntBuffer temp = BufferUtils.createIntBuffer(16);
        GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, temp);
        int max = temp.get(0);
        if ((texWidth > max) || (texHeight > max)) {
        	throw new IOException("Attempt to allocate a texture to big for the current hardware");
        }
        
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
        GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
        
        if (minFilter == GL11.GL_LINEAR_MIPMAP_NEAREST) {
        	// generate a mip map textur
        	GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, componentCount, texWidth,
        					      texHeight, srcPixelFormat, 
        					      GL11.GL_UNSIGNED_BYTE, textureBuffer);
        } else {
	        // produce a texture from the byte buffer
	        GL11.glTexImage2D(target, 
	                      0, 
	                      dstPixelFormat, 
	                      get2Fold(width), 
	                      get2Fold(height), 
	                      0, 
	                      srcPixelFormat, 
	                      GL11.GL_UNSIGNED_BYTE, 
	                      textureBuffer); 
        }
        
        return texture; 
    } 
    
    public void free(Texture texture) {
        IntBuffer scratch = BufferUtils.createIntBuffer(1);
        scratch.put(0, texture.getTextureID());
        GL11.glDeleteTextures(scratch);
    }
}
