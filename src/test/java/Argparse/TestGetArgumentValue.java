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

import javax.management.InvalidAttributeValueException;
import org.junit.Test;

public class TestGetArgumentValue {
  @Test
  public void testGetArgumentValueWhenNoExplicitValueIsGiven() {
    final Argparse parser =
        new Argparse(new String[] {"./foo.exe", "--bar=foo"}, null, null, null, null);

    try {
      parser.addArgument(
          new Argparse.ArgparseOption(
              "f",
              "foo",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_STRING,
              null,
              "bar",
              "Help text for foo."));
      parser.addArgument(
          new Argparse.ArgparseOption(
              "b",
              "bar",
              Argparse.ArgparseOptionType.ARGPARSE_OPT_STRING,
              null,
              "foo",
              "Help text for bar."));
    } catch (InvalidAttributeValueException exc) {
      System.err.println(exc.toString());
    }

    parser.parse();
    assertEquals("bar", parser.getArgumentValue("foo"));
  }

  @Test
  public void testGetArgumentValueWhenExplicitValueIsGiven() {
    final Argparse parser =
        new Argparse(new String[] {"./foo.exe", "--foo=foo"}, null, null, null, null);

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
    assertEquals("foo", parser.getArgumentValue("foo"));
  }
}
