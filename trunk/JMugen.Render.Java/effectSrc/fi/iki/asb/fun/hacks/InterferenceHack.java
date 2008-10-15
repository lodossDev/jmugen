package fi.iki.asb.fun.hacks;

/**
 *Interference pattern.
 */
public class InterferenceHack extends Hack {

    /** Table if heights at each distance from center. */
    private int heights[];

    /** The color palette. */
    private int colors[] = null;

    /** The layers. */
    private InterferenceLayer[] layers;

    // ==================================================================== //

    public InterferenceHack(int layers) {
	this.layers = new InterferenceLayer[layers];
    }

    /**
     * Set the palette.
     *
     * @param palette the palette.
     */
    public void setPalette(int[] palette) {
        if (palette.length != 256) {
            throw new IllegalArgumentException
                ("palette must contain 256 colors");
        }
        this.colors = palette;
    }

    /**
     * Initialize this hack to the spesified size. Subclasses that override
     * this method should call <tt>super.init(frame, original)</tt> first to
     * get the <tt>frame</tt> and <tt>original</tt> attributes initialized
     * properly.
     */
    public void init() {
	heights = new int[(int)Math.sqrt((frame.width * frame.width) +
                                         (frame.height * frame.height))];
	double angle = 90;
	double delta = 4.0;
	double metadelta = delta / heights.length;
	for (int i = 0; i < heights.length; i++) {
	    double rad = toRadians(angle);
	    heights[i] = (int)((1.0 + Math.sin(rad)) * 127.0);
	    angle += delta;
	    delta -= metadelta;
	}

        // Create default palette.
        if (colors == null) {
            colors = new int[256];
            for (int i = 0; i < 256; i++) colors[i] = 0xFF000000;
            for (int i = 0; i < 128; i++) {
                int g = (int)((1.0 + Math.sin(toRadians(i / 256.0 * 360.0)))
                              * 127.0);

                colors[i] = colors[i] | g << 16;
                colors[i + 64] = colors[i + 64] | g << 8;
                colors[i + 128] = colors[i + 128] | g;
            }
        }

	for (int i = 0; i < layers.length; i++) {
	    layers[i] = new InterferenceLayer(frame.width, frame.height);
	}
    }

    /**
     * Generate the next frame.
     */
    public void tick() {
        int xd, yd;
        int height;
	int index = frame.pixels.length;
	for (int y = frame.height; y-- > 0; ) {
            for (int x = frame.width; x-- > 0; ) {

                height = 0;
		for (int i = layers.length; i-- > 0; ) {
		    xd = layers[i].x - x;
		    yd = layers[i].y - y;
		    height += heights[fast_sqrt((xd * xd) + (yd * yd))];
		}

                int texel = colors[height & 0xFF];
                if ((texel & 0xFF000000) == 0xFF000000) {
                    frame.pixels[--index] = texel;
                } else {
                    --index;
                }
	    }
	}

	for (int i = 0; i < layers.length; i++) {
	    layers[i].move();
	}
    }

}

class InterferenceLayer {

    /**
     * The oscillators.
     */
    private Oscillator xo, yo;

    /**
     * Location of the center of this layer.
     */
    public int x, y;

    // ================================================================ //

    public InterferenceLayer(int width, int height) {
        xo = new Oscillator((double)width / (Math.random() + 1.0),
                            0, (double)width, true);
        yo = new Oscillator((double)height / (Math.random() + 1.0),
                            0, (double)height, true);
        move();
    }

    /**
     * Move this layer.
     */
    public void move() {
        x = (int)xo.next();
        y = (int)yo.next();
    }

}














