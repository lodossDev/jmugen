package org.lee.mugen.imageIO;

import java.io.IOException;
import java.io.InputStream;

/**
 * Quake 2 Palette
 *
 * @author Kevin Glass
 */
public class PCXPalette {
	public static final int PALETTE_SIZE = 3 * 256;
    public int[] r = new int[256];
    public int[] g = new int[256];
    public int[] b = new int[256];
    
    /** 
     * Creates new PCX Palette 
     *
     * @param in The input stream to read the palette from
     */
    public PCXPalette(InputStream in) throws IOException {
        in.skip(in.available() - PALETTE_SIZE);
        for (int i = 0; i < 256; i++) {
            r[i] = (int) in.read();
            g[i] = (int) in.read();
            b[i] = (int) in.read();       
        }
    }
    public void load(InputStream in) throws IOException {
        in.skip(in.available() - PALETTE_SIZE);
        for (int i = 0; i < 256; i++) {
            r[i] = (int) in.read();
            g[i] = (int) in.read();
            b[i] = (int) in.read();       
        }
    }
    public PCXPalette() {
	}

	/**
     * Get a colour a specified index 
     *
     * @param i The index of the colour to retrieve
     */
    public int getColor(int i) {
        if (i < 0)
            i = 255 + i;
        return (255 << 24) + (r[i] << 16)+(g[i] << 8)+(b[i]);
    }
    
}
