package image_char_matching;

import java.util.*;

/**
 * The SubImgCharMatcher class facilitates matching characters to image brightness levels.
 * It allows users to add characters along with their corresponding brightness values,
 * and retrieve the character that best matches a given brightness value.
 */
public class SubImgCharMatcher {
    // TreeMap to store characters mapped to their unnormalized brightness values
    private final HashMap<Character, Double> charToUnnormalizedBrightnessMap;

    // Variables to store the minimum and maximum brightness values
    private Double minBrightness, maxBrightness;

    /**
     * Constructs a SubImgCharMatcher object with the given charset.
     * Initializes the charToUnnormalizedBrightnessMap and adds characters
     * along with their brightness values.
     *
     * @param charset Array of characters representing the charset
     */
    public SubImgCharMatcher(char[] charset) {
        this.charToUnnormalizedBrightnessMap = new HashMap<>();

        // adding to the charByBrightness map (character, brightness),
        for (char c : charset) {
            addChar(c);
        }
    }

    /**
     * Retrieves the character that best matches the given image brightness.
     *
     * @param brightness The brightness value of the image
     * @return The character that best matches the given brightness
     */
    public char getCharByImageBrightness(double brightness) {
        //searching for closest value
        Map.Entry<Character, Double> foundEntry = null;
        for (Map.Entry<Character, Double> entry : charToUnnormalizedBrightnessMap.entrySet()) {
            if (foundEntry == null) {
                foundEntry = entry;
            } else {
                double currNormalizedBrightness = (entry.getValue() - minBrightness) /
                        (maxBrightness - minBrightness);
                double currDiff = Math.abs(currNormalizedBrightness - brightness);

                double foundNormalizedBrightness = (foundEntry.getValue() - minBrightness) /
                        (maxBrightness - minBrightness);
                double foundDiff = Math.abs(foundNormalizedBrightness - brightness);

                if (currDiff <= foundDiff) {
                    if (currDiff == foundDiff) {
                        if (currNormalizedBrightness == foundNormalizedBrightness) {
                            foundEntry = (int) entry.getKey() < (int) foundEntry.getKey() ?
                                    entry : foundEntry;
                        } else {
                            foundEntry = currNormalizedBrightness < foundNormalizedBrightness ?
                                    entry : foundEntry;
                        }
                    } else {
                        foundEntry = entry;
                    }
                }
            }
        }
        assert foundEntry != null;
        return foundEntry.getKey();
    }


    /**
     * Adds a character along with its corresponding brightness value to the map.
     * Updates minBrightness and maxBrightness if necessary.
     *
     * @param c The character to be added
     */
    public void addChar(char c) {
        if (!this.charToUnnormalizedBrightnessMap.containsKey(c)) {
            double brightness = getCharBrightness(c);
            this.charToUnnormalizedBrightnessMap.put(c, brightness);

            if (minBrightness == null || brightness < minBrightness) {
                minBrightness = brightness;
            }
            if (maxBrightness == null || brightness > maxBrightness) {
                maxBrightness = brightness;
            }
        }
    }

    /**
     * Removes a character from the map and updates minBrightness and maxBrightness if necessary.
     *
     * @param c The character to be removed
     */
    public void removeChar(char c) {
        if (!this.charToUnnormalizedBrightnessMap.containsKey(c)) {
            return;
        }
        double currCharBrightness = this.charToUnnormalizedBrightnessMap.get(c);
        this.charToUnnormalizedBrightnessMap.remove(c);
        if (this.charToUnnormalizedBrightnessMap.isEmpty()) {
            minBrightness = null;
            maxBrightness = null;
        } else {
            minBrightness = currCharBrightness == minBrightness ?
                    Collections.min(this.charToUnnormalizedBrightnessMap.values()) : minBrightness;
            maxBrightness = currCharBrightness == maxBrightness ?
                    Collections.max(this.charToUnnormalizedBrightnessMap.values()) : maxBrightness;
        }
    }

    /**
     * Retrieves the list of characters in the charset sorted in ascending order.
     *
     * @return The sorted list of characters in the charset
     */
    public ArrayList<Character> getCharset() {
        ArrayList<Character> charset = new ArrayList<>(this.charToUnnormalizedBrightnessMap.keySet());
        Collections.sort(charset);
        return charset;
    }

    /**
     * Computes the brightness value for the given character.
     *
     * @param c The character for which brightness is to be calculated
     * @return The brightness value of the character
     */
    private double getCharBrightness(char c) {
        boolean[][] boolArr = CharConverter.convertToBoolArray(c);
        int whitePixelsCounter = 0;
        for (boolean[] row : boolArr) {
            for (boolean pixel : row) {
                if (pixel) {
                    whitePixelsCounter++;
                }
            }
        }
        return (double) whitePixelsCounter / (boolArr.length * boolArr[0].length);
    }
}
