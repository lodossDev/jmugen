package fi.iki.asb.fun.hacks;

import java.util.Arrays;

/**
 * This hack distorts an image.
 */
public class DistortHack extends Hack {

    /** The horizontal layer. */
    private DistortLayer[] layers;

    /** The original texture. */
    private PixelImage texture;

    /** The distort values. */
    private int distort_x[];
    private int distort_y[];

    /**
     * The distance from the top left corner of the texture to the top left
     * corner of the frame.
     */
    private int x_offset;
    private int y_offset;

    /**
     * The amount of distortion.
     */
    private final int shift;

    // ==================================================================== //

    /**
     * Create a distort hack.
     *
     * @param texture the texture
     */
    public DistortHack(PixelImage texture, int layers, int shift) {
        this.texture = texture;
        this.layers = new DistortLayer[layers];
        this.shift = shift;
    }

    // ==================================================================== //

    /**
     *
     */
    public void init() {

        x_offset = (texture.width - frame.width) >> 1;
        y_offset = (texture.height - frame.height) >> 1;

        // Create distortion wave.
	distort_x = new int[(int)Math.sqrt((frame.width * frame.width) +
                                           (frame.height * frame.height))];
	distort_y = new int[(int)Math.sqrt((frame.width * frame.width) +
                                           (frame.height * frame.height))];
	double angle = 90;
	double delta = 4.0;
	double metadelta = delta / distort_x.length;
	for (int i = 0; i < distort_x.length; i++) {
	    double rad = toRadians(angle);
	    distort_x[i] = (int)(Math.sin(rad) * 255.0);
	    distort_y[i] = (int)(Math.cos(rad) * 255.0);
            angle += delta;
	    delta -= metadelta;
	}

        for (int i = 0; i < layers.length; i++) {
            layers[i] = new DistortLayer(frame.width, frame.height);
        }
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        int tmp;
        int xt, yt; // temporary cache variable
        int xd, yd; // the distortion amount
	int index = frame.pixels.length;
	for (int y = frame.height; y-- > 0; ) {
            for (int x = frame.width; x-- > 0; ) {

                xd = 0;
                yd = 0;
		for (int i = layers.length; i-- > 0; ) {
		    xt = layers[i].x - x;
		    yt = layers[i].y - y;
                    tmp = fast_sqrt((xt * xt) + (yt * yt));
                    xd += distort_x[tmp];
		    yd += distort_y[tmp];
		}

                xd = x_offset + x + (xd >> shift); 
                if (xd >= texture.width || xd < 0) xd = x;

                yd = y_offset + y + (yd >> shift); 
                if (yd >= texture.height || yd < 0) yd = y;

                int texel = texture.pixels[xd + yd * texture.width];
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

    // ==================================================================== //

    public static void main(String args[]) {

        long[] time = new long[12];

        DistortHack hack = new DistortHack(new PixelImage(360, 240), 3, 6);
        hack.init(new PixelImage(320, 200));

        // DO NOT CHANGE THIS LOOP!
        for (int i = 0; i < time.length; i++) {
            System.out.print(".");
            long start = System.currentTimeMillis();
            for (int j = 0; j < 100; j++) {
                hack.tick();
            }
            long stop = System.currentTimeMillis();
            time[i] = stop - start;
        }

        Arrays.sort(time);

        long total = 0;
        for (int i = 1; i < time.length - 1; i++) {
            total += time[i];
        }
        System.err.println("avg:\t" + (total / (time.length - 2)));
    }

}

class DistortLayer {

    /**
     * The oscillators.
     */
    private Oscillator xo, yo;

    /**
     * Location of the center of this layer.
     */
    public int x, y;

    // ================================================================ //

    public DistortLayer(int width, int height) {
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
