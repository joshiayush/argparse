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

package Argparse;

/**
 * Command-line parsing library.
 * 
 * This module is an optparse-inspired command-line parsing library that:
 *  
 *  * handles both optional and positional arguments
 *  * produces highly informative usage messages
 */

import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.InvalidAttributeValueException;

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
   * We map the short and long options with each other so to easily find the short
   * or the longer version of an argument given over the command-line.
   * 
   * These maps will also help us to quickly identify if the option given over the
   * command-line is a valid argument or not.
   */
  private Map<String, String> shortToLongOptionsMap = null;
  private Map<String, String> longToShortOptionsMap = null;

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
      if (this.progname.indexOf(".") > -1) {
        this.progname = this.progname.substring(0, this.progname.lastIndexOf("."));
      }
    }
    this.usage = usage;
    this.description = description;
    this.epilog = epilog;
    this.sysargs = sysargs.clone();

    this.optionsMap = new HashMap<Integer, ArgparseOption>();

    this.shortToLongOptionsMap = new HashMap<String, String>();
    this.longToShortOptionsMap = new HashMap<String, String>();
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
    this.optionsMap.put(argparseOption.longName != null && !argparseOption.longName.isEmpty()
        ? argparseOption.longName.hashCode()
        : argparseOption.shortName.hashCode(), argparseOption);

    if (argparseOption.shortName != null && !argparseOption.shortName.isEmpty()) {
      this.shortToLongOptionsMap.put(argparseOption.shortName,
          !argparseOption.longName.isEmpty() ? argparseOption.longName : null);
    }
    if (argparseOption.longName != null && !argparseOption.longName.isEmpty()) {
      this.longToShortOptionsMap.put(argparseOption.longName,
          !argparseOption.shortName.isEmpty() ? argparseOption.shortName : null);
    }
  }

  /**
   * This function takes a string of text and wraps it to a specified width.
   * It takes several optional parameters to handle whitespaces, newline
   * characters, special characters, and indentation.
   *
   * @param text                             The string of text to be wrapped.
   * @param width                            The maximum width of the wrapped
   *                                         text.
   * @param handleWhitespaces                A boolean flag to determine whether
   *                                         leading and trailing whitespaces
   *                                         should be removed from the text.
   * @param handleNewLines                   A boolean flag to determine whether
   *                                         newline characters in the text should
   *                                         be handled.
   * @param handleSpecialCharacters          A boolean flag to determine whether
   *                                         special characters in the text should
   *                                         be handled.
   * @param specialCharacterHandlingStrategy A string indicating the strategy for
   *                                         handling special characters, default
   *                                         is console.
   * @param indentNextLine                   A boolean flag to determine whether
   *                                         the next line should be indented.
   * @param indentationString                A string to be used as the
   *                                         indentation.
   *
   * @return A list of strings, each representing a line of the wrapped text.
   */
  public static List<String> wrapText(String text, int width, boolean handleWhitespaces, boolean handleNewLines,
      boolean handleSpecialCharacters, String specialCharacterHandlingStrategy, boolean indentNextLine,
      String indentationString) throws Exception {
    if (handleWhitespaces) {
      text = text.trim();
    }

    if (handleSpecialCharacters) {
      if (specialCharacterHandlingStrategy.equals("console")) {
        text = text.replace("\t", "    ");
      } else {
        throw new Exception(String.format("Strategy %s is not supported", specialCharacterHandlingStrategy));
      }
    }

    List<String> lines = new ArrayList<String>();
    String[] words = text.split("\\s+");
    String line = "";
    int lastWordIndex = 0;
    for (int i = 0; i < words.length; i++) {
      if (handleNewLines && words[i].contains("\n")) {
        lines.add(line);
        lines.add(words[i].replace("\n", ""));
        line = "";
        lastWordIndex = i + 1;
      } else if (line.length() + words[i].length() <= width) {
        line += words[i] + " ";
      } else if (words[i].length() > width) {
        int j = 0;
        while (j < words[i].length()) {
          lines.add(line + words[i].substring(j, Math.min(j + width, words[i].length())));
          j += width;
        }
        line = "";
        lastWordIndex = i + 1;
      } else {
        lines.add(line);
        line = words[i] + " ";
        lastWordIndex = i;
      }
    }

    if (handleWhitespaces) {
      if (!line.isEmpty()) {
        lines.add(line.trim());
      }
    } else {
      lines.add(line);
    }

    if (indentNextLine) {
      for (int i = lastWordIndex; i < lines.size(); i++) {
        lines.set(i, indentationString + lines.get(i));
      }
    }
    return lines;
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

    boolean isGroupPresent = false;
    for (final ArgparseOption option : this.optionsMap.values()) {
      if (option.optionType == ArgparseOptionType.ARGPARSE_OPT_GROUP) {
        isGroupPresent = true;
        break;
      }
    }

    String formattedOptionsGroupString = "";
    if (isGroupPresent) {
      formattedOptionsGroupString = "(";
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
    }

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

  public void parse() {
    if (this.sysargs.length == 1 && !this.optionsMap.isEmpty()) {
      this.error("Expected arguments but none is given.");
    }

    /** Stores the already encountered group argument. */
    String groupArgument = null;
    boolean isGroupArgumentEncountered = false;

    final char argumentValueAssigner = '=';

    /** Now iterate through the `sysargs` array and parse each argument. */
    for (final String sysarg : this.sysargs) {
      /**
       * Check if the argument value assigner character is present in the argument. If
       * yes then that means the argument holds a value otherwise, it may be a boolean
       * type argument or it is missing a value.
       */
      if (sysarg.indexOf(argumentValueAssigner) > 0) {
        /** Split the argument at '=' and separate argument with its value. */
        String[] argumentValueArray = sysarg.split(String.format("%c", argumentValueAssigner));
        String argument = argumentValueArray[0];
        String value = argumentValueArray[1];

        /**
         * Now check if the argument is present in our options map.
         *
         * Assuming that the option given over the command line is a long option, we
         * generates its hash code and look for that hash code inside of our options
         * map. If we do not find anything there then we generate the hash code of its
         * shorter version and look for that hash code inside of our options map.
         * 
         * If we find neither of these; we error out.
         */
        String argumentWithoutPrefix = argument.substring(2, argument.length());
        if (this.optionsMap.containsKey(argumentWithoutPrefix.hashCode())) {
          if (this.optionsMap
              .get(argumentWithoutPrefix.hashCode()).optionType == ArgparseOptionType.ARGPARSE_OPT_GROUP) {
            if (isGroupArgumentEncountered) {
              this.error(String.format("%s and %s is a part of group, hence can be used only one at a time.",
                  groupArgument, argumentWithoutPrefix));
            }
            groupArgument = argumentWithoutPrefix;
          }
          this.optionsMap.get(argumentWithoutPrefix.hashCode()).value = value;
        } else if (this.optionsMap.containsKey(this.longToShortOptionsMap.get(argumentWithoutPrefix).hashCode())) {
          if (this.optionsMap
              .get(this.longToShortOptionsMap.get(argumentWithoutPrefix)
                  .hashCode()).optionType == ArgparseOptionType.ARGPARSE_OPT_GROUP) {
            if (isGroupArgumentEncountered) {
              this.error(String.format("%s and %s is a part of group, hence can be used only one at a time.",
                  groupArgument, argumentWithoutPrefix));
            }
            groupArgument = argumentWithoutPrefix;
          }
          this.optionsMap.get(this.longToShortOptionsMap.get(argumentWithoutPrefix).hashCode()).value = value;
        } else {
          this.error(String.format("Un-recognized argument %s.", argumentWithoutPrefix));
        }
      } else {
        String argument = sysarg;
        String argumentWithoutPrefix = argument.substring(2, argument.length());

        /**
         * We need to check if the given argument is of type boolean. If yes then mark
         * its value true because of its presence, otherwise error out.
         */
        if (this.optionsMap.containsKey(argumentWithoutPrefix.hashCode())) {
          if (this.optionsMap
              .get(argumentWithoutPrefix.hashCode()).optionType == ArgparseOptionType.ARGPARSE_OPT_GROUP) {
            if (isGroupArgumentEncountered) {
              this.error(String.format("%s and %s is a part of group, hence can be used only one at a time.",
                  groupArgument, argumentWithoutPrefix));
            }
            groupArgument = argumentWithoutPrefix;
          }
          if (this.optionsMap
              .get(argumentWithoutPrefix.hashCode()).optionType != ArgparseOptionType.ARGPARSE_OPT_BOOLEAN) {
            this.error(String.format("Expected a value for argument %s.", argumentWithoutPrefix));
          }
          this.optionsMap.get(argumentWithoutPrefix.hashCode()).value = "true";
        } else if (this.optionsMap.containsKey(this.longToShortOptionsMap.get(argumentWithoutPrefix).hashCode())) {
          if (this.optionsMap
              .get(this.longToShortOptionsMap.get(argumentWithoutPrefix)
                  .hashCode()).optionType == ArgparseOptionType.ARGPARSE_OPT_GROUP) {
            if (isGroupArgumentEncountered) {
              this.error(String.format("%s and %s is a part of group, hence can be used only one at a time.",
                  groupArgument, argumentWithoutPrefix));
            }
            groupArgument = argumentWithoutPrefix;
          }
          if (this.optionsMap.get(this.longToShortOptionsMap.get(argumentWithoutPrefix)
              .hashCode()).optionType != ArgparseOptionType.ARGPARSE_OPT_BOOLEAN) {
            this.error(String.format("Expected a value for argument %s.", argumentWithoutPrefix));
          }
          this.optionsMap.get(this.longToShortOptionsMap.get(argumentWithoutPrefix).hashCode()).value = "true";
        } else {
          this.error(String.format("Un-recognized argument %s.", argumentWithoutPrefix));
        }
      }
    }
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
    if (this.usage == null || this.usage.isEmpty()) {
      this.usage = this.formatUsage();
    }
    Argparse.printMessage(String.format("%s%s", this.usage, "\n"));
    if (this.description != null && !this.description.isEmpty()) {
      Argparse.printMessage("\n" + this.description + "\n");
    }
    if (this.epilog != null && !this.epilog.isEmpty()) {
      Argparse.printMessage("\n" + this.epilog + "\n");
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
