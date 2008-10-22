package org.lee.mugen.imageIO;

import java.awt.Dimension;
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import org.lee.mugen.io.LittleEndianDataInputStream;
import org.lee.mugen.util.Logger;

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
//        image = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
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
    
    public static class pcx_t {

		// size of byte arrays
		static final int PALETTE_SIZE = 48;
		static final int FILLER_SIZE = 58;

		public byte manufacturer;
		public byte version;
		public byte encoding;
		public byte bits_per_pixel;
		public int xmin, ymin, xmax, ymax; // unsigned short
		public int hres, vres; // unsigned short
		public byte[] palette; //unsigned byte; size 48
		public byte reserved;
		public byte color_planes;
		public int bytes_per_line; // unsigned short
		public int palette_type; // unsigned short
		public byte[] filler; // size 58
		public ByteBuffer data; //unbounded data

		public pcx_t(byte[] dataBytes) {
			this(ByteBuffer.wrap(dataBytes));
		}

		public pcx_t(ByteBuffer b) {
			// is stored as little endian
			b.order(ByteOrder.LITTLE_ENDIAN);

			// fill header
			manufacturer = b.get();
			version = b.get();
			encoding = b.get();
			bits_per_pixel = b.get();
			xmin = b.getShort() & 0xffff;
			ymin = b.getShort() & 0xffff;
			xmax = b.getShort() & 0xffff;
			ymax = b.getShort() & 0xffff;
			hres = b.getShort() & 0xffff;
			vres = b.getShort() & 0xffff;
			b.get(palette = new byte[PALETTE_SIZE]);
			reserved = b.get();
			color_planes = b.get();
			bytes_per_line = b.getShort() & 0xffff;
			palette_type = b.getShort() & 0xffff;
			b.get(filler = new byte[FILLER_SIZE]);

			// fill data
			data = b.slice();
		}
	}
    
    public static byte[] LoadPCX(RawPCXImage pcxRaw, byte[][] outPalette, Dimension outDim) {

    	byte[] raw = pcxRaw.getData();

        //
        // parse the PCX file
        //
    	pcx_t pcx = new pcx_t(raw);

        if (pcx.manufacturer != 0x0a || pcx.version != 5 || pcx.encoding != 1
                || pcx.bits_per_pixel != 8) {

            Logger.log("Bad pcx file ");
            return null;
        }

        int width = pcx.xmax - pcx.xmin + 1;
        int height = pcx.ymax - pcx.ymin + 1;

        byte[] pix = new byte[width * height];

        if (outPalette != null) {
            outPalette[0] = new byte[768];
            System.arraycopy(raw, raw.length - 768, outPalette[0], 0, 768);
        }

        if (outDim != null) {
            outDim.width = width;
            outDim.height = height;
        }

        //
        // decode pcx
        //
        int count = 0;
        byte dataByte = 0;
        int runLength = 0;
        int x, y;

        for (y = 0; y < height; y++) {
            for (x = 0; x < width;) {

                dataByte = pcx.data.get();

                if ((dataByte & 0xC0) == 0xC0) {
                    runLength = dataByte & 0x3F;
                    dataByte = pcx.data.get();
                    // write runLength pixel
                    while (runLength-- > 0) {
                        pix[count++] = dataByte;
                        x++;
                    }
                } else {
                    // write one pixel
                    pix[count++] = dataByte;
                    x++;
                }
            }
        }
        return pix;
    }
}
