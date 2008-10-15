package fi.iki.asb.fun.hacks;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;

/**
 * An image producer which consumes pixel images.
 */
public class PixelImageProducer implements ImageProducer {

    /**
     * The image constructed from <tt>pixels</tt>.
     */
    private Image image = null;

    /**
     * The registered image consumer.
     */
    private ImageConsumer consumer;

    /**
     * The color model.
     */
    private ColorModel model;

    /**
     * The pixel image.
     */
    private PixelImage pixelImage;

    // ==================================================================== //

    /**
     * Create a pixel image from a size spec.
     */
    public PixelImageProducer(PixelImage pixelImage) {
        this.pixelImage = pixelImage;

        // Setup color model
        model = new DirectColorModel(32,
                                     0x00FF0000,
                                     0x0000FF00,
                                     0x000000FF,0);
    }

    // ==================================================================== //

    /**
     * Tell this image that there are new pixels in the
     * <tt>pixels</tt> array.
     */
    public void update() {

        if (consumer != null) {

            // copy integer pixel data to image consumer
            consumer.setPixels(0, 0,
                               pixelImage.width, pixelImage.height,
                               model, pixelImage.pixels,
                               0, pixelImage.width);

            // notify image consumer that the frame is done
            consumer.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
        }
    }

    /**
     * Set the image consumer.
     */
    public synchronized void addConsumer(ImageConsumer ic) {

        // Register image consumer
        consumer = ic;

        // Set image dimensions
        consumer.setDimensions(pixelImage.width, pixelImage.height);

        // Set image consumer hints for speed
        consumer.setHints(ImageConsumer.TOPDOWNLEFTRIGHT |
                          ImageConsumer.COMPLETESCANLINES |
                          ImageConsumer.SINGLEPASS |
                          ImageConsumer.SINGLEFRAME);

        // set image color model
        consumer.setColorModel(model);
    }

    /**
     * Check if the specified consumer is the registered consumer.
     */
    public synchronized boolean isConsumer(ImageConsumer ic) {
        return (ic == consumer);
    }

    /**
     * Add the consumer.
     */
    public synchronized void removeConsumer(ImageConsumer ic) {
        if (ic == consumer) {
            consumer = null;
        }
    }

    /**
     * 
     */
    public void startProduction(ImageConsumer ic) {
        addConsumer(ic);
    }

    /**
     *
     */
    public void requestTopDownLeftRightResend(ImageConsumer ic) {
        // Ignored.
    }

}
