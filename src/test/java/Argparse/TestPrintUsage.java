/**
 * Copyright 2022, argparse Inc. All rights reserved.
 *
 * <p>Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * <p>* Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. * Neither the name of argparse Inc. nor
 * the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package Argparse;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.management.InvalidAttributeValueException;
import org.junit.Test;

public class TestPrintUsage {
  @Test
  public void testPrintUsageMethodWhenUsageIsGiven() {
    String programUsage = "Usage: foo foo\n";
    programUsage += "\n";
    programUsage += "Options:\n";
    programUsage += "\n";
    programUsage += "    --help     Use this option to print the help text.";

    final Argparse parser =
        new Argparse(new String[] {"./foo.exe", "foo"}, null, programUsage, null, null);

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    parser.printUsage();

    System.setOut(null);
    final String capturedStdout = outStream.toString();

    /**
     * An extra new-line character for the trailing end-of-line character appended by the
     * `printUsage()` function at the end of the usage.
     */
    final String expectedProgramUsage = programUsage + "\n" + "\033[0m";

    assertEquals(expectedProgramUsage, capturedStdout);
  }

  @Test
  public void testPrintUsageWhenUsageIsEmpty() {
    final Argparse parser = new Argparse(new String[] {"./foo.exe", "foo"}, null, "", "", "");

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for foo."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    parser.printUsage();

    System.setOut(null);
    final String capturedStdout = outStream.toString();

    final int initialpad = 4;
    String expectedProgramUsage = "Usage: foo [-f]\n";
    expectedProgramUsage += "\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-f, --foo=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for foo.";

    /**
     * Two extra new-line characters for the trailing end-of-line character appended by the
     * `printUsage()` function at the end of the usage and the `formatUsage()` function at the end
     * of every option string.
     */
    expectedProgramUsage += "\n\n" + "\033[0m";

    assertEquals(expectedProgramUsage, capturedStdout);
  }

  @Test
  public void testPrintUsageWhenMultipleOptionalArgumentsAreGiven() {
    final Argparse parser =
        new Argparse(new String[] {"./foo.exe", "foo", "bar"}, null, "", "", "");

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for foo."));
      parser.addArgument(
          new Argparse.ArgparseOption(
              "b",
              "bar",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for bar."));
      parser.addArgument(
          new Argparse.ArgparseOption(
              "bz",
              "buzz",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for buzz."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    parser.printUsage();

    System.setOut(null);
    final String capturedStdout = outStream.toString();

    final int initialpad = 4;
    String expectedProgramUsage = "Usage: foo [-b][-f][-bz]\n";
    expectedProgramUsage += "\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-b, --bar=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for bar.\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-f, --foo=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for foo.\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-bz, --buzz=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for buzz.\n";

    /**
     * Two extra new-line characters for the trailing end-of-line character appended by the
     * `printUsage()` function at the end of the usage and the `formatUsage()` function at the end
     * of every option string.
     */
    expectedProgramUsage += "\n" + "\033[0m";

    assertEquals(expectedProgramUsage, capturedStdout);
  }

  @Test
  public void testPrintUsageWhenDescriptionIsGiven() {
    String programDescription = "argparse version 1.0.0\n";
    programDescription +=
        "Post bug reports here at https://github.com/joshiayush/argparse/issues/new";

    final Argparse parser =
        new Argparse(new String[] {"./foo.exe", "foo"}, null, "", programDescription, "");

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for foo."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    parser.printUsage();

    System.setOut(null);
    final String capturedStdout = outStream.toString();

    final int initialpad = 4;
    String expectedProgramUsage = "Usage: foo [-f]\n";
    expectedProgramUsage += "\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-f, --foo=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for foo.";

    /**
     * Three extra new-line characters for the trailing end-of-line character appended by the
     * `printUsage()` function at the end of the usage and the `formatUsage()` function at the end
     * of every option string and the end-of-line character before printing the description in
     * `printUsage()` function.
     */
    expectedProgramUsage += "\n\n" + "\033[0m" + "\n";
    expectedProgramUsage += programDescription;
    expectedProgramUsage += "\n" + "\033[0m";

    assertEquals(expectedProgramUsage, capturedStdout);
  }

  @Test
  public void testPrintUsageWhenDescriptionAndEpilogIsGiven() {
    String programDescription = "argparse version 1.0.0\n";
    programDescription +=
        "Post bug reports here at https://github.com/joshiayush/argparse/issues/new";

    String programEpilog = "Every command line tool has a parser behind it.";

    final Argparse parser =
        new Argparse(
            new String[] {"./foo.exe", "foo"}, null, "", programDescription, programEpilog);

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_BOOLEAN,
              null,
              "false",
              "Help text for foo."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    parser.printUsage();

    System.setOut(null);
    final String capturedStdout = outStream.toString();

    final int initialpad = 4;
    String expectedProgramUsage = "Usage: foo [-f]\n";
    expectedProgramUsage += "\n";
    expectedProgramUsage += new String(new char[initialpad]).replace("\0", " ");
    expectedProgramUsage += "-f, --foo=<BOOL>\n";
    expectedProgramUsage += new String(new char[initialpad << 2]).replace("\0", " ");
    expectedProgramUsage += "Help text for foo.";

    /**
     * Three extra new-line characters for the trailing end-of-line character appended by the
     * `printUsage()` function at the end of the usage and the `formatUsage()` function at the end
     * of every option string and the end-of-line character before printing the description in
     * `printUsage()` function.
     */
    expectedProgramUsage += "\n\n" + "\033[0m" + "\n";
    expectedProgramUsage += programDescription;
    expectedProgramUsage += "\n" + "\033[0m";

    expectedProgramUsage += "\n";
    expectedProgramUsage += programEpilog;
    expectedProgramUsage += "\n" + "\033[0m";

    assertEquals(expectedProgramUsage, capturedStdout);
  }
}
