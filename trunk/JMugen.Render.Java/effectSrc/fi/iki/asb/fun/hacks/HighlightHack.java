package fi.iki.asb.fun.hacks;

import java.util.Arrays;

public class HighlightHack extends Hack {

    private static final int FIXEDP = 8;
    private int[] mul;

    private int highlight = 0xFFFFFFFF;
    private int r = 0xFF;
    private int g = 0xFF;
    private int b = 0xFF;

    private int intensity = 1 << (FIXEDP - 1);
    private int length = 1 << (FIXEDP - 6);

    // ==================================================================== //

    public HighlightHack(int highlight, int light) {
        this.highlight = highlight;
        r = (light >> 16) & 0xFF;
        g = (light >> 16) & 0xFF;
        b = (light >> 16) & 0xFF;
    }

    // ==================================================================== //
    
    public void init() {
        mul = new int[frame.width];
        Arrays.fill(mul, 0);
    }

    /**
     * Generate the next frame.
     */
    public void tick() {
        int index = 0;
        for (int y = 0; y < frame.height; y++) {
            for (int x = 0; x < frame.width; x++) {

                int m = mul[x];
                int texel = frame.pixels[index];

                if (m > 0) {

                    int r1 = (texel >> 16) & 0xFF;
                    r1 = r1 + ((r * m) >> FIXEDP);
                    if (r1 > 0xFF) r1 = 0xFF;

                    int g1 = (texel >> 8) & 0xFF;
                    g1 = g1 + ((g * m) >> FIXEDP);
                    if (g1 > 0xFF) g1 = 0xFF;

                    int b1 = texel & 0xFF;
                    b1 = b1 + ((b * m) >> FIXEDP);
                    if (b1 > 0xFF) b1 = 0xFF;

                    frame.pixels[index]
                        = (texel & 0xFF000000)
                        | (r1 << 16) | (g1 << 8) | b1;

                    mul[x] = m - length;
                }

                if (texel == highlight) {
                    mul[x] = intensity;
                }

                index++;
            }
        }
    }
}
