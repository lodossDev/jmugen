package fi.iki.asb.fun.hacks;


/**
 * A base class for graphics hacks. All hack objects draw their pixels
 * to a PixelImage.
 */
public abstract class Hack {

    /**
     * The frame.
     */
    public PixelImage frame;

    // ==================================================================== //

    /**
     * Initialize this hack to the spesified size. Subclasses that override
     * this method should call <tt>super.init(frame, original)</tt> to get
     * the frame attribute initialized.
     *
     * @param frame the frame where this hack draws to.
     */
    public void init(PixelImage frame) {
	this.frame = frame;
        this.init();
    }

    /**
     * Called automatically by <code>init(PixelImage)</code>. Subclasses
     * should implement this and do their initialization here.
     */
    public void init() {
    }

    /**
     * Generate the next frame.
     */
    public abstract void tick();

    // ==================================================================== //

    private static final int[] table = {
        0,    16,  22,  27,  32,  35,  39,  42,  45,  48,  50,  53,  55,  57,
        59,   61,  64,  65,  67,  69,  71,  73,  75,  76,  78,  80,  81,  83,
        84,   86,  87,  89,  90,  91,  93,  94,  96,  97,  98,  99, 101, 102,
        103, 104, 106, 107, 108, 109, 110, 112, 113, 114, 115, 116, 117, 118,
        119, 120, 121, 122, 123, 124, 125, 126, 128, 128, 129, 130, 131, 132,
        133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 144, 145,
        146, 147, 148, 149, 150, 150, 151, 152, 153, 154, 155, 155, 156, 157,
        158, 159, 160, 160, 161, 162, 163, 163, 164, 165, 166, 167, 167, 168,
        169, 170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178,
        179, 180, 181, 181, 182, 183, 183, 184, 185, 185, 186, 187, 187, 188,
        189, 189, 190, 191, 192, 192, 193, 193, 194, 195, 195, 196, 197, 197,
        198, 199, 199, 200, 201, 201, 202, 203, 203, 204, 204, 205, 206, 206,
        207, 208, 208, 209, 209, 210, 211, 211, 212, 212, 213, 214, 214, 215,
        215, 216, 217, 217, 218, 218, 219, 219, 220, 221, 221, 222, 222, 223,
        224, 224, 225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231,
        231, 232, 232, 233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238,
        239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 245, 245, 246,
        246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252, 252, 253,
        253, 254, 254, 255
    };

    /**
     * A *much* faster replacement for (int)(java.lang.Math.sqrt(x)).
     */
    public static final int fast_sqrt(int x) {
        if (x >= 0x10000)
            if (x >= 0x1000000)
                if (x >= 0x10000000)
                    if (x >= 0x40000000)
                        return (table[x >> 24] << 8);
                    else
                        return (table[x >> 22] << 7);
                else if (x >= 0x4000000)
                    return (table[x >> 20] << 6);
                else
                    return (table[x >> 18] << 5);
            else if (x >= 0x100000)
                if (x >= 0x400000)
                    return (table[x >> 16] << 4);
                else
                    return (table[x >> 14] << 3);
            else if (x >= 0x40000)
                return (table[x >> 12] << 2);
            else
                return (table[x >> 10] << 1);
        else if (x >= 0x100)
            if (x >= 0x1000)
                if (x >= 0x4000)
                    return (table[x >> 8]);
                else
                    return (table[x >> 6] >> 1);
            else if (x >= 0x400)
                return (table[x >> 4] >> 2);
            else
                return (table[x >> 2] >> 3);
        else
            if (x >=0)
                return table[x] >> 4;
        return -1; // negative argument...
    }

    /**
     * Convert degrees to radians.
     */
    public static final double toRadians(double angle) {
        return (angle * Math.PI) / 180.0;
    }

    /**
     * Return the biggest common denominator.
     */
    public static final int biggestCommonDenominator(int x, int y) {
        if (x == y) return x;
	else if (x < y) return biggestCommonDenominator(x, y - x);
	else return biggestCommonDenominator(x - y, y);
    }

    /**
     * Clamp val between min and max (inclusive).
     */
    public static final double clamp(double val, double min, double max) {
        if (val < min) return min;
        if (val > max) return max;
        return val;
    }
}
