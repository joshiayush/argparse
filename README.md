# Introduction

The purpose of [Argparse][_argparse] is to facilitate the process of parsing command-line arguments passed to a program. Command-line arguments are a way for users to provide input to a program when it is run from the command-line, and are a common way to configure the behavior of command-line applications.

Some of the features of [Argparse][_argparse]:
* Support for different types of arguments, such as options, flags, key-value pairs, and positional arguments.
* Automatic generation of help and usage messages.
* Automatic error handling, such as handling missing or invalid arguments.

By using an [Argparse][_argparse], developers can make their command-line applications more user-friendly, easier to use, and more robust.

## Getting Started

For now it is a single page module, which you can directly copy from [here](https://github.com/joshiayush/argparse/blob/master/src/main/java/Argparse/Argparse.java) and paste in your project. But if you want to properly install it in your project with the `pom.xml` file (that includes the dependency tree), then you should look at the [Releases][_releases] and download one of the zip files from there.

## API Reference

* __`ArgparseOptionType`__

  This `enum` includes the type of options that can be given over the command-line if using [Argparse][_argparse] as your main parsing program.

  This `enum` includes the following types of options:
  
  * `ARGPARSE_OPT_GROUP`
  * `ARGPARSE_OPT_BOOLEAN`
  * `ARGPARSE_OPT_BIT`
  * `ARGPARSE_OPT_INTEGER`
  * `ARGPARSE_OPT_FLOAT`
  * `ARGPARSE_OPT_STRING`

* __`ArgparseOption`__

  This class creates a parser option.

  It has the following attributes attached to its instance:

  * `prefix` - Flag prefix.
  * `shortName` - Flag short name.
  * `longName` - Flag long name.
  * `value` - Flag value.
  * `defaultValue` - Flag default value.
  * `help` - Flag help text.
  * `optionType` - Type of the flag.

* __`Argparse`__

  Creates the parser instance for your command-line application.

  * __`Argparse(String[] sysargs, String progname, String usage, String description, String epilog);`__

    The `Argparse` constructor initializes the `Argparse` object with the provided arguments. The constructor takes in the `sysargs`, `progname`, `usage`, `description`, and `epilog` arguments.

    If the `progname` argument is `null` or empty, it retrieves the program name from the first element of the `sysargs` array. If the program name has an extension, it removes the extension from the name.

    The `usage`, `description`, and `epilog` arguments are assigned to the corresponding instance variables of the `Argparse` object.

    The constructor shifts the `sysargs` array by one position to remove the program name from the array, and assigns it to the `sysargs` instance variable of the `Argparse` object.

    The constructor initializes three maps: `optionsMap`, `shortToLongOptionsMap`, and `longToShortOptionsMap`. The `optionsMap` maps an integer key to an `ArgparseOption` object. The `shortToLongOptionsMap` maps short option strings to their corresponding long option strings. The `longToShortOptionsMap` maps long option strings to their corresponding short option strings.

  * __`public void addArgument(final ArgparseOption argparseOption) throws InvalidAttributeValueException;`__

    This function, `addArgument`, is used to add an argument to the `optionsMap`, which is a hash map that maps a hash code generated from the short or long name of the argument to the argument itself. The function first checks if the argument's short name or long name is provided, and throws an `InvalidAttributeValueException` if both are empty. The argument is then added to the `optionsMap` with its hash code as the key. If the argument has a short name, the short-to-long name mapping is added to `shortToLongOptionsMap`. If the argument has a long name, the long-to-short name mapping is added to `longToShortOptionsMap`. This function helps build the argument parser by allowing the user to add arguments and their associated short and long names.

  * __`public void parse();`__

    The `parse()` function is responsible for parsing the command line arguments provided to the program. It first checks if any arguments have been provided and if so, it iterates through them. If an argument contains an equal sign, it splits the argument into the argument name and value, and then looks for the corresponding option in the `optionsMap`. If it finds a match, it assigns the value to the option's value attribute. If the argument does not contain an equal sign, it checks if the option is a `boolean` type. If it is, it assigns the value "true" to the option's value attribute, otherwise it errors out. If the argument is not recognized, it also errors out. The function locks the current group argument if it exists before setting the value.

  * __`public String getArgumentValue(String argument);`__

    This is a method that returns the value of a given argument if present in the `optionsMap` hash map. If the argument is not present, it returns `null`. The method checks if the argument is present in the `longToShortOptionsMap` and `shortToLongOptionsMap` hash maps and retrieves the value of the corresponding option from the `optionsMap`. If the retrieved value is null, it returns the default value for the option.

  * __`public static void printMessage(final ArgparseOption argparseOption);`__

    This method prints out a formatted string of an `ArgparseOption` object, which can be used for debugging purposes.

  * __`public static void printMessage(final String message);`__

    This method is a simple wrapper around the `System.out.print()` method to make it easier to print messages to the console. It takes a string as input and prints it to the console. There are no overloads for integer or `float` values, so `String.format()` can be used to pass `strings` with `integer` or `float` values.

  * __`public void printUsage();`__

    This function prints the usage string of the program along with the description and epilog, if provided during initialization of the parser. It also uses `Argparse.colorEncodedString` to add color to the printed output.

  * __`public void exit();`__

    The `exit()` method exits the program and releases the resources acquired by the `logger`. It calls the `System.exit(0)` method to terminate the program with a successful status code.

  * __`public void exit(final int status, final String message);`__

    This method overloads the `exit()` method and takes two parameters: an `integer` status and a `String` message. It prints the message to the console if it is not null or empty, then exits the program with the specified status code.

  * __`public void error(final String message);`__

    This method prints an error message, followed by the usage string, and then exits the program with an error code of `2`. The error message indicates that there was an error in the usage of a shell built-in command. The error message is passed as an argument to the method.

## Examples

```java
import Argparse;

import javax.management.InvalidAttributeValueException;

class Main {
  public void main(String[] args) {
    Argparse parser = new Argparse(
      args,
      null,  /** Program name is automatically deduced. */
      "Usage: progname --foo\n" +
      "\n" +
      "     --foo=<STRING>\n" +
      "           Just a flag argument like any other flag argument.",
      "This is the program description and the program parses the value for flag 'foo'.",
      "This is the epilog (set the footer here).");

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_STRING,
              null,
              "bar",
              "Help text for foo."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    parser.parse();
    final String fooValue = parser.getArgumentValue("foo");
    ...
  }
}
```

[_argparse]: https://github.com/joshiayush/argparse/tree/master
[_releases]: https://github.com/joshiayush/argparse/releases