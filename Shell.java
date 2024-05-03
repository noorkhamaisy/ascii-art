package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.ImageManager;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The Shell class serves as the command-line interface for interacting with the ASCII art algorithm
 * and managing its settings.
 */
public class Shell {
    //path for the default image.
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";

    //default resolution.
    private static final int DEFAULT_RESOLUTION = 128;

    //default char set.
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    //default image path error message.
    private static final String DEFAULT_IMAGE_PATH_ERROR_MESSAGE = "The default image could not be found. " +
            "Please provide a valid default image path.";

    // empty char set error message.
    private static final String EMPTY_CHARSET_ERROR_MESSAGE = "Did not execute. Charset is empty.";

    //incorrect add format error message.
    private static final String INCORRECT_ADD_FORMAT_ERROR_MESSAGE = "Did not add due to incorrect format.";

    //incorrect remove format error message.
    private static final String INCORRECT_REMOVE_FORMAT_ERROR_MESSAGE = "Did not remove due to incorrect " +
            "format.";

    //incorrect res format error message.
    private static final String INCORRECT_RES_FORMAT_ERROR_MESSAGE = "Did not change resolution due to " +
            "incorrect format.";

    //resolution exceeding boundaries error message.
    private static final String RESOLUTION_EXCEEDING_BOUNDARIES = "Did not change resolution due to " +
            "exceeding boundaries.";

    //resolution set successfully message.
    private static final String RESOLUTION_SET_SUCCESSFULLY = "Resolution set to %d.";

    //image path error message.
    private static final String IMAGE_PATH_ERROR_MESSAGE = "Did not execute due to problem with image file.";

    //output incorrect format.
    private static final String OUTPUT_INCORRECT_FORMAT = "Did not change output method due to incorrect " +
            "format.";

    //incorrect command error messages.
    private static final String INCORRECT_COMMAND_ERROR_MESSAGE = "Did not execute due to incorrect " +
            "command.";

    //html output.
    private static final String HTML_OUTPUT = "html";

    //default html output path.
    private static final String DEFAULT_HTML_OUTPUT_PATH = "out.html";

    //default html font.
    private static final String DEFAULT_HTML_FONT = "Courier New";

    //console output.
    private static final String CONSOLE_OUTPUT = "console";

    //up resolution.
    private static final String UP_RES = "up";

    //down resolution.
    private static final String DOWN_RES = "down";

    //command start string.
    private static final String COMMAND_START_STRING = ">>> ";

    //exit command.
    private static final String EXIT_COMMAND = "exit";

    //chars command.
    private static final String CHARS_COMMAND = "chars";

    //add command.
    private static final String ADD_COMMAND = "add";

    //remove command.
    private static final String REMOVE_COMMAND = "remove";

    //add all command.
    private static final String ADD_ALL_COMMAND = "all";

    //add space command.
    private static final String ADD_SPACE_COMMAND = "space";

    //image command.
    private static final String IMAGE_COMMAND = "image";

    //output command.
    private static final String OUTPUT_COMMAND = "output";

    //ascii art command.
    private static final String ASCII_ART_COMMAND = "asciiArt";

    //res command.
    private static final String RES_COMMAND = "res";

    //space char.
    private static final char SPACE_CHAR = ' ';

    //hyphen char.
    private static final char HYPHEN_CHAR = '-';

    //add a range command length.
    private static final int RANGE_COMMAND_LENGTH = 3;

    //res command length.
    private static final int RES_COMMAND_LENGTH = 2;

    //output command length.
    private static final int OUTPUT_COMMAND_LENGTH = 2;

    //minimum ascii value.
    private static final int MINIMUM_ASCII_VALUE = 32;

    //maximum ascii value.
    private static final int MAXIMUM_ASCII_VALUE = 127;

    //resolution multiply factor.
    private static final int RESOLUTION_MUL_FACTOR = 2;

    // ImageManager field.
    private final ImageManager imageManager;

    // SubImgCharMatcher field.
    private final SubImgCharMatcher subImgCharMatcher;

    //ascii output field.
    private AsciiOutput asciiOutput;


    /**
     * Constructs a Shell object, initializing the ASCII art algorithm and output method.
     *
     * @throws IOException If there is an error with the default image path
     */
    public Shell() throws IOException {
        try {
            this.imageManager = new ImageManager(DEFAULT_IMAGE_PATH, DEFAULT_RESOLUTION);
            this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
            this.asciiOutput = new ConsoleAsciiOutput();
        } catch (IOException e) {
            throw new IOException(DEFAULT_IMAGE_PATH_ERROR_MESSAGE);
        }
    }

    /**
     * The main method to start the shell and handle user commands.
     */
    public static void main(String[] args) {
        try {
            Shell shell = new Shell();
            shell.run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs the shell, continuously accepting and executing user commands until the exit command is given.
     */
    public void run() {
        System.out.print(COMMAND_START_STRING);
        String userInput = KeyboardInput.readLine();
        while (!userInput.equals(EXIT_COMMAND)) {
            String[] commandArguments = userInput.split(" ");
            switch (commandArguments[0]) {
                case CHARS_COMMAND:
                    runCharsCommand();
                    break;
                case ADD_COMMAND:
                    runAddCommand(commandArguments);
                    break;
                case REMOVE_COMMAND:
                    runRemoveCommand(commandArguments);
                    break;
                case ASCII_ART_COMMAND:
                    runAsciiArtCommand();
                    break;
                case RES_COMMAND:
                    runResCommand(commandArguments);
                    break;
                case IMAGE_COMMAND:
                    runImageCommand(commandArguments);
                    break;
                case OUTPUT_COMMAND:
                    runOutputCommand(commandArguments);
                    break;
                default:
                    System.out.println(INCORRECT_COMMAND_ERROR_MESSAGE);
            }
            System.out.print(COMMAND_START_STRING);
            userInput = KeyboardInput.readLine();
        }
    }

    /**
     * Executes the 'chars' command, displaying the current character set used for ASCII art generation.
     */
    private void runCharsCommand() {
        ArrayList<Character> charset = subImgCharMatcher.getCharset();
        for (char character : charset) {
            System.out.print(character + " ");
        }
        System.out.println();
    }

    /**
     * Executes the 'add' command, adding characters to the character set.
     *
     * @param commandArguments The arguments provided with the 'add' command
     */
    private void runAddCommand(String[] commandArguments) {
        changeCharset(commandArguments, INCORRECT_ADD_FORMAT_ERROR_MESSAGE, subImgCharMatcher::addChar);
    }

    /**
     * Executes the 'remove' command, removing characters from the character set.
     *
     * @param commandArguments The arguments provided with the 'remove' command
     */
    private void runRemoveCommand(String[] commandArguments) {
        changeCharset(commandArguments, INCORRECT_REMOVE_FORMAT_ERROR_MESSAGE, subImgCharMatcher::removeChar);
    }

    /**
     * Helper method to add or remove characters from the character set.
     *
     * @param commandArguments The arguments provided with the command
     * @param errorMessage     The error message to display in case of incorrect format
     * @param callback         The callback function to execute (add or remove character)
     */
    private void changeCharset(String[] commandArguments,
                               String errorMessage,
                               Consumer<Character> callback) {
        if (commandArguments[1].equals(ADD_ALL_COMMAND)) {
            for (char c = MINIMUM_ASCII_VALUE; c < MAXIMUM_ASCII_VALUE; c++) {
                callback.accept(c);
            }
        } else if (commandArguments[1].equals(ADD_SPACE_COMMAND)) {
            subImgCharMatcher.addChar(SPACE_CHAR);
        } else if (
                commandArguments[1].length() == 1 &&
                        commandArguments[1].charAt(0) >= MINIMUM_ASCII_VALUE &&
                        commandArguments[1].charAt(0) <= MAXIMUM_ASCII_VALUE
        ) {
            callback.accept(commandArguments[1].charAt(0));
        } else if (
                commandArguments[1].length() == RANGE_COMMAND_LENGTH &&
                        commandArguments[1].charAt(1) == HYPHEN_CHAR &&
                        commandArguments[1].charAt(0) >= MINIMUM_ASCII_VALUE &&
                        commandArguments[1].charAt(0) <= MAXIMUM_ASCII_VALUE &&
                        commandArguments[1].charAt(2) >= MINIMUM_ASCII_VALUE &&
                        commandArguments[1].charAt(2) <= MAXIMUM_ASCII_VALUE
        ) {
            for (
                    char c = (char) Math.min(commandArguments[1].charAt(0), commandArguments[1].charAt(2));
                    c <= (char) Math.max(commandArguments[1].charAt(0), commandArguments[1].charAt(2));
                    c++
            ) {
                callback.accept(c);
            }
        } else {
            System.out.println(errorMessage);
        }
    }

    /**
     * Executes the 'res' command, changing the resolution of ASCII art.
     *
     * @param commandArguments The arguments provided with the 'res' command
     */
    private void runResCommand(String[] commandArguments) {
        if (commandArguments.length != RES_COMMAND_LENGTH) {
            System.out.println(INCORRECT_RES_FORMAT_ERROR_MESSAGE);
        } else if (commandArguments[1].equals(UP_RES) || commandArguments[1].equals(DOWN_RES)) {
            int newResolution = commandArguments[1].equals(UP_RES) ?
                    imageManager.getResolution() * RESOLUTION_MUL_FACTOR :
                    imageManager.getResolution() / RESOLUTION_MUL_FACTOR;
            try {
                imageManager.setResolution(newResolution);
                System.out.println(String.format(RESOLUTION_SET_SUCCESSFULLY, newResolution));
            } catch (ResolutionExceedingBoundariesException e) {
                System.out.println(RESOLUTION_EXCEEDING_BOUNDARIES);
            }
        } else {
            System.out.println(INCORRECT_RES_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Executes the 'image' command, setting the image to be used for ASCII art generation.
     *
     * @param commandArguments The arguments provided with the 'image' command
     */
    private void runImageCommand(String[] commandArguments) {
        try {
            imageManager.setImage(commandArguments[1]);
        } catch (IOException e) {
            System.out.println(IMAGE_PATH_ERROR_MESSAGE);
        }
    }

    /**
     * Executes the 'output' command, changing the output method for displaying ASCII art.
     *
     * @param commandArguments The arguments provided with the 'output' command
     */
    private void runOutputCommand(String[] commandArguments) {
        if (commandArguments.length != OUTPUT_COMMAND_LENGTH) {
            System.out.println(OUTPUT_INCORRECT_FORMAT);
        } else if (commandArguments[1].equals(HTML_OUTPUT)) {
            asciiOutput = new HtmlAsciiOutput(DEFAULT_HTML_OUTPUT_PATH, DEFAULT_HTML_FONT);
        } else if (commandArguments[1].equals(CONSOLE_OUTPUT)) {
            asciiOutput = new ConsoleAsciiOutput();
        } else {
            System.out.println(OUTPUT_INCORRECT_FORMAT);
        }
    }

    /**
     * Executes the 'asciiArt' command, generating and displaying the ASCII art.
     */
    private void runAsciiArtCommand() {
        try {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(
                    imageManager.getBrightnessMap(),
                    subImgCharMatcher
            );
            char[][] asciiArt = asciiArtAlgorithm.run();
            asciiOutput.out(asciiArt);
        } catch (EmptyCharsetException e) {
            System.out.println(EMPTY_CHARSET_ERROR_MESSAGE);
        }
    }
}
