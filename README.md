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

## Examples

```java
import Argparse;

import javax.management.InvalidAttributeValueException;

class Main {
  public void main(String[] args) {
    Argparse parser = new Argparse(args, null, null, null, null);

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