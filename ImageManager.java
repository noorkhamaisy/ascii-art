package image;

import ascii_art.ResolutionExceedingBoundariesException;

import java.awt.Color;
import java.io.IOException;

/**
 * The ImageManager class handles image processing operations such as loading an image,
 * setting image resolution, and computing brightness maps.
 */
public class ImageManager {
    // log base to calculate the resolution.
    private static final int LOG_BASE = 2;

    // max RGB value.
    private static final int MAX_RGB_VALUE = 255;

    // red factor to calculate the brightness.
    private static final double RED_FACTOR = 0.2126;

    // green factor to calculate the brightness.
    private static final double GREEN_FACTOR = 0.7152;

    // blue factor to calculate the brightness.
    private static final double BLUE_FACTOR = 0.0722;

    // minimum number of characters in a row.
    private static final int CHARS_IN_ROW_MINIMUM_VALUE = 1;

    // image to manage.
    private Image image;

    //the resolution.
    private int resolution;

    //2D brightness map.
    private double[][] brightnessMap;

    /**
     * Constructs an ImageManager object with the specified image path and resolution.
     * Initializes the image object and computes the brightness map.
     *
     * @param imagePath  The path to the image file
     * @param resolution The resolution for image processing
     * @throws IOException If there is an error reading the image file
     */
    public ImageManager(String imagePath, int resolution) throws IOException {
        this.image = new Image(imagePath);
        this.resolution = resolution;
        this.brightnessMap = getImageBrightness();
    }

    /**
     * Sets the image to the one specified by the given image path.
     *
     * @param imagePath The path to the new image file
     * @throws IOException If there is an error reading the new image file
     */
    public void setImage(String imagePath) throws IOException {
        this.image = new Image(imagePath);
        brightnessMap = getImageBrightness();
    }

    /**
     * Sets the resolution for image processing.
     *
     * @param resolution The new resolution value
     * @throws ResolutionExceedingBoundariesException If the specified resolution is invalid
     */
    public void setResolution(int resolution) throws ResolutionExceedingBoundariesException {
        int paddedWidth = (int) Math.pow(LOG_BASE, log2(image.getWidth()));
        int paddedHeight = (int) Math.pow(LOG_BASE, log2(image.getHeight()));

        int minCharsInRow = Math.max(CHARS_IN_ROW_MINIMUM_VALUE, paddedWidth / paddedHeight);

        if (resolution < minCharsInRow || resolution > paddedWidth) {
            throw new ResolutionExceedingBoundariesException();
        }

        this.resolution = resolution;
        brightnessMap = getImageBrightness();
    }

    /**
     * Retrieves the current resolution for image processing.
     *
     * @return The current resolution value
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Retrieves the brightness map of the image.
     *
     * @return The brightness map as a 2D array of doubles
     */
    public double[][] getBrightnessMap() {
        return brightnessMap;
    }

    /**
     * Computes the brightness map of the image based on the current resolution.
     *
     * @return The brightness map as a 2D array of doubles
     */
    private double[][] getImageBrightness() {
        Image[][] subImages = getSubImages();
        double[][] brightnessMap = new double[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[0].length; j++) {
                brightnessMap[i][j] = getBrightness(subImages[i][j]);
            }
        }
        return brightnessMap;
    }

    /**
     * Computes the base-2 logarithm of a given number.
     *
     * @param num The number to compute the logarithm for
     * @return The base-2 logarithm of the number
     */
    private static int log2(int num) {
        return (int) Math.ceil(Math.log(num) / Math.log(LOG_BASE));
    }

    /**
     * Divides the padded image into sub-images based on the resolution.
     *
     * @return A 2D array of Image objects representing sub-images
     */
    private Image[][] getSubImages() {
        Image paddedImage = padImage();
        int subImageSize = paddedImage.getWidth() / resolution;
        return divideToSubImages(paddedImage, subImageSize);
    }

    /**
     * Pads the original image to ensure even division for sub-image creation.
     *
     * @return The padded image
     */
    private Image padImage() {
        int newWidth = (int) Math.pow(LOG_BASE, log2(image.getWidth()));
        int widthPadding = (newWidth - image.getWidth()) / 2;
        int newHeight = (int) Math.pow(LOG_BASE, log2(image.getHeight()));
        int heightPadding = (newHeight - image.getHeight()) / 2;
        Color[][] paddedPixelArray = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (
                        i < heightPadding ||
                                i >= newHeight - heightPadding ||
                                j < widthPadding ||
                                j >= newWidth - widthPadding
                ) {
                    paddedPixelArray[i][j] = new Color(MAX_RGB_VALUE, MAX_RGB_VALUE, MAX_RGB_VALUE);
                } else {
                    paddedPixelArray[i][j] = image.getPixel(i - heightPadding, j - widthPadding);
                }
            }
        }
        return new Image(paddedPixelArray, newWidth, newHeight);
    }

    /**
     * Divides the padded image into sub-images based on the specified sub-image size.
     *
     * @param paddedImage  The padded image
     * @param subImageSize The size of each sub-image
     * @return A 2D array of Image objects representing sub-images
     */
    private Image[][] divideToSubImages(Image paddedImage, int subImageSize) {
        Image[][] subImages = new Image[paddedImage.getHeight() / subImageSize][resolution];
        for (int i = 0; i < (paddedImage.getHeight() / subImageSize); i++) {
            for (int j = 0; j < resolution; j++) {
                Color[][] subImagePixels = new Color[subImageSize][subImageSize];
                for (int k = 0; k < subImageSize; k++) {
                    for (int l = 0; l < subImageSize; l++) {
                        subImagePixels[k][l] =
                                paddedImage.getPixel(i * subImageSize + k, j * subImageSize + l);
                    }
                }
                subImages[i][j] = new Image(subImagePixels, subImageSize, subImageSize);
            }
        }
        return subImages;
    }

    /**
     * Computes the brightness of an image.
     *
     * @param image The image for which brightness is to be computed
     * @return The brightness value of the image (normalized to [0, 1])
     */
    private static double getBrightness(Image image) {
        double greyPixelSum = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = image.getPixel(i, j);
                greyPixelSum += color.getRed() * RED_FACTOR +
                        color.getGreen() * GREEN_FACTOR +
                        color.getBlue() * BLUE_FACTOR;
            }
        }
        double brightness = greyPixelSum / (image.getHeight() * image.getWidth());
        return brightness / MAX_RGB_VALUE;
    }

}
