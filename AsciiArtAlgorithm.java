package ascii_art;

import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class represents an algorithm for generating ASCII art from images.
 * It uses an ImageManager to handle image processing and a SubImgCharMatcher to match image brightness
 * with ASCII characters.
 */
public class AsciiArtAlgorithm {

    //brightness map field.
    private final double[][] brightnessMap;

    //sub images char matcher field.
    private final SubImgCharMatcher subImgCharMatcher;


    /**
     * Constructs an AsciiArtAlgorithm with the given brightness,subImgCharMatcher
     *
     * @param brightnessMap       a 2D array of doubles representing the brightness of the image
     * @param subImgCharMatcher a SubImgCharMatcher object representing the sub-image character mapping
     */
    public AsciiArtAlgorithm(double[][] brightnessMap, SubImgCharMatcher subImgCharMatcher) {
        this.brightnessMap = brightnessMap;
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * Generates ASCII art from the image.
     *
     * @return a 2D char array representing the ASCII art
     * @throws EmptyCharsetException if the character set is empty
     */
    public char[][] run() throws EmptyCharsetException {
        if (subImgCharMatcher.getCharset().isEmpty()) {
            throw new EmptyCharsetException();
        }
        char[][] asciiArt = new char[brightnessMap.length][brightnessMap[0].length];
        for (int i = 0; i < asciiArt.length; i++) {
            for (int j = 0; j < asciiArt[i].length; j++) {
                asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(brightnessMap[i][j]);
            }
        }
        return asciiArt;
    }

}
