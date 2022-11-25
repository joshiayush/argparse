/**
 * Copyright 2022, argparse Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of argparse Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package src.main.java.Argparse;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InvalidAttributeValueException;

import java.lang.Runtime;

/**
 * OS Utils; cannot be inheritable further.
 * 
 * Defines common utility functions to identify the OS.
 */
final class OsUtils {
    private static String OS = null;

    /**
     * Command to obtain the column size of a terminal in a Linux machine.
     * 
     * @TODO: Also handle the case when the library is running on Windows, and MacOS
     *        machines.
     */
    private static final String LINUX_TERMINAL_COL_WIDTH_CMD = "tput cols";

    /**
     * Returns the name of the Operating System.
     */
    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    /**
     * Returns true if the Operating System is Windows.
     */
    public static boolean isWindows() {
        return getOsName().toLowerCase().indexOf("win") >= 0;
    }

    /**
     * Returns true if the Operating System is Unix.
     */
    public static boolean isUnix() {
        return getOsName().toLowerCase().indexOf("nix") >= 0 || getOsName().toLowerCase().indexOf("nux") >= 0
                || getOsName().toLowerCase().indexOf("aix") >= 0;
    }

    /**
     * Returns true if the Operating System is MacOS.
     */
    public static boolean isMac() {
        return getOsName().toLowerCase().indexOf("mac") >= 0;
    }

    /**
     * Returns true if the Operating System is Solaris.
     */
    public static boolean isSolaris() {
        return getOsName().toLowerCase().indexOf("sunos") >= 0;
    }

    /**
     * getTerminalWidth() returns the number of columns present in the terminal
     * open.
     * 
     * This function is only supported in Linux and returns `-1` when used in MacOs,
     * or Windows.
     */
    public static int getTerminalWidth() {
        /**
         * @TODO: Make this function compatible with MacOS and Windows.
         */
        if (!OsUtils.isUnix())
            return -1;
        try {
            /**
             * Note, we don't use `ProcessBuilder` because it internally uses `ForkAndExec`
             * which in turns makes the copy of the entire parents address space, so that
             * even a little command can lead to `OutOfMemoryErrors`, when the parent
             * process has big amount of memory acquired.
             */
            return Integer.parseInt(Runtime.getRuntime().exec(OsUtils.LINUX_TERMINAL_COL_WIDTH_CMD).toString());
        } catch (IOException | NumberFormatException e) {
            Argparse.LOGGER.log(Level.WARNING, e.getMessage());
            return -1;
        }
    }
}

enum ArgparseOptionType {
    ARGPARSE_OPT_GROUP,
    ARGPARSE_OPT_BOOLEAN,
    ARGPARSE_OPT_BIT,
    ARGPARSE_OPT_INTEGER,
    ARGPARSE_OPT_FLOAT,
    ARGPARSE_OPT_STRING,
}

/**
 * Creates a parser option.
 */
class ArgparseOption {
    /** Most programs use this prefix. */
    private static final String DEFAULT_PREFIX = "--";

    public String prefix;
    public String shortName;
    public String longName;
    public String value;
    public String defaultValue;
    public String help;
    public ArgparseOptionType optionType;

    /**
     * Flag to check if the option instance must have an explicit value provided
     * over the command line.
     */
    public boolean isValueRequired = false;

    /**
     * Initializes an `ArgparseOption` instance with the default prefix and the user
     * given values.
     */
    public ArgparseOption(String shortName, String longName,
            ArgparseOptionType optionType, String value, String defaultValue, String help) {
        this.prefix = ArgparseOption.DEFAULT_PREFIX;
        this.shortName = shortName;
        this.longName = longName;
        this.optionType = optionType;
        this.value = value;
        this.defaultValue = defaultValue;

        /**
         * Boolean options are considered to have a value of true if present over the
         * command line, false otherwise.
         */
        if (this.optionType != ArgparseOptionType.ARGPARSE_OPT_BOOLEAN && this.defaultValue == null)
            this.isValueRequired = true;
        this.help = help;
    }

    /**
     * Initializes an `ArgparseOption` instance with the user given values.
     */
    public ArgparseOption(String prefix, String shortName, String longName,
            ArgparseOptionType optionType, String value, String defaultValue, String help) {
        this.prefix = prefix;
        this.shortName = shortName;
        this.longName = longName;
        this.optionType = optionType;
        this.value = value;
        this.defaultValue = defaultValue;

        /**
         * Boolean options are considered to have a value of true if present over the
         * command line, false otherwise.
         */
        if (this.optionType != ArgparseOptionType.ARGPARSE_OPT_BOOLEAN && this.defaultValue == null)
            this.isValueRequired = true;
        this.help = help;
    }

    /**
     * Returns a formatted string of `ArgparseOption` object.
     * 
     * Good for debugging purposes.
     */
    public String toString() {
        String trimmedHelpString = this.help;
        /**
         * Trim the help string for a `Argparse` option so to avoid overloading the
         * terminal with characters when debugging.
         */
        if (trimmedHelpString.length() > 23) {
            trimmedHelpString = trimmedHelpString.substring(0, 20) + "...";
        }
        return ("<ArgparseOption shortName('" + this.shortName + "'), longName('"
                + this.longName + "'), value('" + this.value + "'), optionType('" + this.optionType.toString()
                + "'), help('" + trimmedHelpString + "')>\n");
    }
}

class Argparse {
    private String progname;
    private String usage;

    /** A description of the command line software at the end of the usage. */
    private String description;

    /** A description of the command line software at the end of the description. */
    private String epilog;

    /**
     * To reduce the time complexity of finding a argument using its short or long
     * name (depending on what's given) we generate a hash code using the short or
     * the long name (long name is preferred) then we map the option with the
     * generated hash code. By doing this in future whenever we will need to check
     * the other attributes of the `ArgparseOption` instance and the option name is
     * given to us we can easily find it in constant time.
     */
    private Map<Integer, ArgparseOption> optionsMap = null;

    /**
     * We clone the system arguments in an instance variable so to later parse the
     * system arguments in a argument-to-value map.
     *
     * Note that we are not creating just another reference of the `sysargs` that we
     * get in our constructor but we are actually cloning so that changing the
     * former one will not reflect any changes in the later one.
     */
    private String[] sysargs = null;

    /**
     * Logger for class `Argparse`. Instantiate with the name of the class.
     */
    public static final Logger LOGGER = Logger.getLogger(Argparse.class.toString());

    /** We don't support formatted output below this terminal width. */
    private static final int DEFAULT_TERMINAL_WIDTH = 80;

    /**
     * Constructs a `Argparse` instance.
     */
    Argparse(String[] sysargs, String progname, String usage, String description, String epilog) {
        if (progname == null || progname.isEmpty()) {
            this.progname = new File(sysargs[0]).getName();
        }
        this.usage = usage;
        this.description = description;
        this.epilog = epilog;
        this.sysargs = sysargs.clone();
    }

    /**
     * Adds an argument to the `optionsMap`. The argument is mapped to a hash code
     * generated from short or long name (depending upon what is given; long name is
     * preferred).
     */
    public void addArgument(final ArgparseOption argparseOption) throws InvalidAttributeValueException {
        if ((argparseOption.shortName == null || argparseOption.shortName.isEmpty())
                && (argparseOption.longName == null || argparseOption.longName.isEmpty())) {
            throw new InvalidAttributeValueException(
                    "Both argparseOption.shortName and argparseOption.longName can't be empty.\n" +
                            "You have to provide at least one of these.\n");
        }
        this.optionsMap.put(argparseOption.longName != null || !argparseOption.longName.isEmpty()
                ? argparseOption.longName.hashCode()
                : argparseOption.shortName.hashCode(), argparseOption);
    }

    /**
     * Auto generates the usage for a parser. The main benefit of having this
     * functions is that the user does not need to provide explicit usage string
     * (s)he only has to provide the arguments and their respective attributes.
     */
    private String formatUsage() {
        int terminalWidth = OsUtils.getTerminalWidth();
        if (terminalWidth < Argparse.DEFAULT_TERMINAL_WIDTH)
            terminalWidth = Argparse.DEFAULT_TERMINAL_WIDTH;

        int usagePrognameSpace = "Usage: ".length() + this.progname.length() + 1;
        int formattedOptionsStringSpace = terminalWidth - usagePrognameSpace;
        int formattedOptionsStringCounter = 0;

        String formattedOptionsGroupString = "(";
        for (final ArgparseOption option : this.optionsMap.values()) {
            if (option.optionType != ArgparseOptionType.ARGPARSE_OPT_GROUP)
                continue;
            if (option.shortName != null && !option.shortName.isEmpty()) {
                formattedOptionsGroupString += String.format("%c%s|", option.prefix.charAt(0), option.shortName);
            } else {
                formattedOptionsGroupString += option.prefix + option.longName + "|";
            }
        }
        /** Remove the trailing '|'. */
        formattedOptionsGroupString = formattedOptionsGroupString.substring(0,
                formattedOptionsGroupString.length() - 1);
        formattedOptionsGroupString += ")";

        String formattedOptionsString = "";
        for (final ArgparseOption option : this.optionsMap.values()) {
            formattedOptionsString += "[";
            if (option.shortName != null && !option.shortName.isEmpty()) {
                formattedOptionsString += String.format("%c%s", option.prefix.charAt(0), option.shortName);
            } else {
                formattedOptionsString += option.prefix + option.longName;
            }
            formattedOptionsString += "]";

            /**
             * Keep a track of the number of characters written so far in our
             * `formattedOptionsString`. This helps us keep the formatted options string
             * inside the bound of our terminal width.
             */
            formattedOptionsStringCounter = formattedOptionsStringCounter
                    + (formattedOptionsString.length() - formattedOptionsStringCounter);
            if (formattedOptionsStringCounter >= formattedOptionsStringSpace) {
                /**
                 * We double the `formattedOptionsStringSpace` to keep a logical distance with
                 * `formattedOptionsStringCounter`.
                 */
                formattedOptionsStringSpace *= 2;
                /** Give a left padding to our data. */
                formattedOptionsString += "\n" + new String(new char[usagePrognameSpace]).replace("\0", " ");
            }
        }
        /** Now add the group. */
        formattedOptionsString += formattedOptionsGroupString;

        String formattedUsageString = "";
        formattedUsageString += "Usage: " + this.progname + " " + formattedOptionsString + "\n";
        formattedUsageString += "\n";
        for (final ArgparseOption option : this.optionsMap.values()) {
            /** Initial padding before every arugment usage line. */
            int initialpad = 4;
            formattedUsageString += new String(new char[initialpad]).replace("\0", " ");
            String optionUsage = "";
            if (option.shortName != null && !option.shortName.isEmpty()) {
                optionUsage += String.format("%c%s", option.prefix.charAt(0), option.shortName);
            }
            if (option.longName != null && !option.longName.isEmpty()) {
                optionUsage += ", " + option.prefix + option.longName;
            }
            switch (option.optionType) {
                case ARGPARSE_OPT_INTEGER:
                    optionUsage += "=<INT>";
                    break;
                case ARGPARSE_OPT_FLOAT:
                    optionUsage += "=<FLOAT>";
                    break;
                case ARGPARSE_OPT_BOOLEAN:
                    optionUsage += "=<BOOL>";
                    break;
                case ARGPARSE_OPT_STRING:
                    optionUsage += "=<STRING>";
                    break;
                case ARGPARSE_OPT_BIT:
                    optionUsage += "=<BIT>";
                    break;
                case ARGPARSE_OPT_GROUP:
                    break;
            }
            if (option.help != null && !option.help.isEmpty()) {
                /**
                 * Format the per line usage of a arugment.
                 * 
                 * -a, --argument
                 * Help text for the argument.
                 */
                formattedUsageString += optionUsage;
                formattedUsageString += "\n";
                formattedUsageString += new String(new char[initialpad << 2]).replace("\0", " ");
                formattedUsageString += option.help;
            }
            formattedUsageString += "\n";
        }
        return formattedUsageString;
    }

    /**
     * Prints out a formatted string of `ArgparseOption` object.
     * 
     * Good for debugging purposes.
     */
    private static void printMessage(final ArgparseOption argparseOption) {
        Argparse.printMessage(argparseOption.toString());
    }

    /**
     * Wrapper around the `System.out.print` function call. Reduces the amount of
     * code needs to be written in order to send message to the console.
     * 
     * We don't provide an overload for integer or float values so use
     * `String.format()` function to pass string with integer or float values.
     */
    public static void printMessage(final String message) {
        System.out.print(message);
    }

    /**
     * Prints the 'Usage' string for the program. It also prints the 'description'
     * and 'epilog' if given during initialization of the parser.
     */
    public void printUsage() {
        if (this.usage == null) {
            this.usage = this.formatUsage();
        }
        Argparse.printMessage(String.format("%s%s", this.usage, "\n"));
        if (!this.description.isEmpty()) {
            Argparse.printMessage(this.description);
        }
        if (!this.epilog.isEmpty()) {
            Argparse.printMessage(this.epilog);
        }
    }

    /**
     * Exits the program and releases the resources.
     */
    public void exit() {
        /** Release the resources that we acquire in our logger. */
        System.exit(0);
    }

    /**
     * Exits the program and releases the resources.
     */
    public void exit(final int status, final String message) {
        if (message != null && !message.isEmpty()) {
            Argparse.printMessage(message);
        }
        /** Release the resources that we acquire in our logger. */
        System.exit(status);
    }

    /**
     * Prints the given message and exits with error code 2 (invalid usage of some
     * shell built-in command).
     */
    public void error(final String message) {
        this.printUsage();
        this.exit(2, this.progname + ": error: " + message + "\n");
    }
}
