package org.lee.mugen.imageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import org.lee.mugen.io.LittleEndianDataInputStream;

public class PCXLoader {
	public static final int HEADER_SIZE = 128;
	
    public static Image loadImageColorIndexed(InputStream file, PCXPalette pal, boolean isPalUse, boolean isUseColorKey) throws IOException {
    	PCXHeader header;
    	BufferedImage image;

    	byte[] data = ByteArrayBuilder.fromStream(file);
        file.close();
        
        header = new PCXHeader(data);
        if (!isPalUse)
        	pal.load(new ByteArrayInputStream(data));
        
        InputStream in = new ByteArrayInputStream(data);
        
        in.skip(128);
        int width = header.xmax - header.xmin + 1;
        int height = header.ymax - header.ymin + 1;
        
        IndexColorModel icm = new IndexColorModel(8, 256, convertToByteArray(pal.r), convertToByteArray(pal.g), convertToByteArray(pal.b), 0);
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);
        
//        BufferedImage.TYPE_INT_RGB

//WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height , 3,null);
//		
//        image = new BufferedImage(icm , raster, false, new Hashtable());

        int xp = 0;
        int yp = 0;
        int value;
        int count;
        
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
                     int color = ImageUtils.getARGB(
                             alpha[0],
                             pal.r[value],
                             pal.g[value],
                             pal.b[value]);
                     
                     if (value == 0) {
                    	 color = ImageUtils.getARGB(
                                 alpha[0],
                                 pal.r[0],
                                 pal.g[0],
                                 pal.b[0]);
                    	 //image.setRGB(xp, yp, 0);
                     } else {
                         image.setRGB(xp, yp, color);
                     }
                
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
        return image;
    }

//    public static int[] decode(InputStream file, PCXPalette pal, boolean isPalUse, boolean isUseColorKey, WrapInt width, WrapInt height) throws IOException {
//    	
//    }
    private static final ColorModel glAlphaColorModel = 
		new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new int[] {8,8,8,8},
            true,
            false,
            ComponentColorModel.TRANSLUCENT,
            DataBuffer.TYPE_BYTE);
    
    public static Image loadImage(InputStream file, PCXPalette pal, boolean isPalUse, boolean isUseColorKey) throws IOException {
    	return loadImage(file, pal, isPalUse, isUseColorKey, false, false);
    }
	/** 
     * Creates new PCXLoader
     *
     * @param file The input stream to load the PCX from
     */
    public static Image loadImage(InputStream file, PCXPalette pal, boolean isPalUse, boolean isUseColorKey, boolean isFlipH, boolean isFlipV) throws IOException {
    	PCXHeader header;
    	BufferedImage image;

    	byte[] data = ByteArrayBuilder.fromStream(file);
        file.close();
        if (pal == null)
        	pal = new PCXPalette();
        header = new PCXHeader(data);
        if (!isPalUse)
        	pal.load(new ByteArrayInputStream(data));
        
        InputStream in = new ByteArrayInputStream(data);
        
        in.skip(128);
        int width = header.xmax - header.xmin + 1;
        int height = header.ymax - header.ymin + 1;

//        IndexColorModel icm = new IndexColorModel(8, 256, convertToByteArray(pal.r), convertToByteArray(pal.g), convertToByteArray(pal.b), 0);
//        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, icm);
//
//        IndexColorModel icmFromImg = (IndexColorModel) image.getColorModel();
        
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height , 4, null);
//		
        image = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        
//      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int xp = 0;
        int yp = 0;
        int value;
        int count;
        
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

                     if (isUseColorKey)
                         isAboutTheSameColor(
                             pal.r[value],
                             pal.g[value],
                             pal.b[value], 
                             pal.r[0], 
                             pal.g[0], 
                             pal.b[0], 
                             alpha);
                     int color = ImageUtils.getARGB(
                             alpha[0],
                             pal.r[value],
                             pal.g[value],
                             pal.b[value]);
                    image.setRGB(xp, yp, color);
                
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
//        
        BufferedImage result = image;
        if (isFlipH || isFlipV) {
            result = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
            Graphics2D g = (Graphics2D) result.getGraphics();
            g.scale(isFlipH?-1:1, isFlipV?-1:1);
            g.drawImage(image, isFlipH?-width:0, isFlipV?-height:0, null);
        	
        }
        return result;
    }

    private static byte[] convertToByteArray(int[] b) {
		byte[] result = new byte[b.length];
		for (int i = 0; i < b.length; i++)
			result[i] = (byte) (b[i] < 0? 0: (b[i] > 255? 255: b[i]));
		return result;
	}

	private static boolean isAboutTheSameColor(int r1, int g1, int b1, int r2, int g2, int b2, int[] alpha) {
        alpha[0] = 255;
        if (r1 == r2 && b1 == b2 && g1 == g2) {
            alpha[0] = 0;
            return true;
        } else
            return false;
    }	

    
    /**
     * The header data block
     */
    public static class PCXHeader {
        public byte manufacturer;
        public byte version;
        public byte encoding;
        public byte bitsPerPixel;
        public int xmin;
        public int ymin;
        public int xmax;
        public int ymax;
        public int hdpi;
        public int vdpi;
        public byte[] colormap = new byte[48];
        public byte reserved;
        public byte planes;
        public int bytesPerLine;
        
        public PCXHeader(byte[] data) throws IOException {
            LittleEndianDataInputStream in = new LittleEndianDataInputStream(new DataInputStream((new ByteArrayInputStream(data))));
            manufacturer = (byte) in.read();
            version = (byte) in.read();
            encoding = (byte) in.read();
            bitsPerPixel = (byte) in.read();
            xmin = in.readUnsignedShort();
            ymin = in.readUnsignedShort();
            xmax = in.readUnsignedShort();
            ymax = in.readUnsignedShort();
            hdpi = in.readUnsignedShort();
            vdpi = in.readUnsignedShort();
            
            in.read(colormap);
            
            reserved = (byte) in.read();
            planes = (byte) in.read();
            bytesPerLine = in.readUnsignedShort();
            
            in.close();
        }
    }
}
