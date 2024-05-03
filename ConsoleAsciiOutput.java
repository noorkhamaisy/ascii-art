package ascii_output;

/**
 * Output a 2D array of chars to the console.
 *
 * @author Dan Nirel
 */
public class ConsoleAsciiOutput implements AsciiOutput {
    /**
     * Outputs a 2D array of characters to the console.
     * Each character is printed followed by a space, and a new line is printed after each row.
     *
     * @param chars the 2D array of characters to be output
     */
    @Override
    public void out(char[][] chars) {
        for (int y = 0; y < chars.length; y++) {
            for (int x = 0; x < chars[y].length; x++) {
                System.out.print(chars[y][x] + " ");
            }
            System.out.println();
        }
    }
}
