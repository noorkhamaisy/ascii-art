package ascii_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Output a 2D array of chars to an HTML file viewable in a web browser.
 *
 * @author Dan Nirel
 */
public class HtmlAsciiOutput implements AsciiOutput {
    //baseline spacing.
    private static final double BASE_LINE_SPACING = 0.8;

    //base font size.
    private static final double BASE_FONT_SIZE = 150.0;

    //font name.
    private final String fontName;

    //file name.
    private final String filename;

    /**
     * Constructs an HtmlAsciiOutput object with the specified filename and font name.
     *
     * @param filename the name of the HTML file to write output to
     * @param fontName the name of the font to use for displaying characters in the HTML
     */
    public HtmlAsciiOutput(String filename, String fontName) {
        this.fontName = fontName;
        this.filename = filename;
    }

    /**
     * Outputs a 2D array of characters to an HTML file.
     *
     * @param chars the 2D array of characters to be output
     */
    @Override
    public void out(char[][] chars) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.format(
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<body style=\"" +
                            "\tCOLOR:#000000;" +
                            "\tTEXT-ALIGN:center;" +
                            "\tFONT-SIZE:1px;\">\n" +
                            "<p style=\"" +
                            "\twhite-space:pre;" +
                            "\tFONT-FAMILY:%s;" +
                            "\tFONT-SIZE:%frem;" +
                            "\tLETTER-SPACING:0.15em;" +
                            "\tLINE-HEIGHT:%fem;\">\n",
                    fontName, BASE_FONT_SIZE / chars[0].length, BASE_LINE_SPACING));

            for (int y = 0; y < chars.length; y++) {
                for (int x = 0; x < chars[y].length; x++) {
                    String htmlRep;
                    switch (chars[y][x]) {
                        case '<':
                            htmlRep = "&lt;";
                            break;
                        case '>':
                            htmlRep = "&gt;";
                            break;
                        case '&':
                            htmlRep = "&amp;";
                            break;
                        default:
                            htmlRep = String.valueOf(chars[y][x]);
                    }
                    writer.write(htmlRep);
                }
                writer.newLine();
            }
            writer.write(
                    "</p>\n" +
                            "</body>\n" +
                            "</html>\n");
        } catch (IOException e) {
            Logger.getGlobal().severe(String.format("Failed to write to \"%s\"", filename));
        }
    }
}
