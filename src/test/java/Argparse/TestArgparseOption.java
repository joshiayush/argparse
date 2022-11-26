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

import static org.junit.Assert.*;
import org.junit.Test;

public class TestArgparseOption {
  @Test
  public void testInstanceWithDefaultOptionPrefix() {
    final ArgparseOption option = new ArgparseOption("f", "foo", ArgparseOptionType.ARGPARSE_OPT_BOOLEAN, null,
        "false", "Help text.");

    assertEquals(option.prefix, "--");
  }

  @Test
  public void testInstanceWithExplicitOptionPrefix() {
    final ArgparseOption option = new ArgparseOption("~~", "f", "foo", ArgparseOptionType.ARGPARSE_OPT_BOOLEAN, null,
        "false", "Help text.");

    assertEquals(option.prefix, "~~");
  }

  @Test
  public void testAttributeIsValueRequired() {
    for (final ArgparseOptionType optionType : ArgparseOptionType.values()) {
      final ArgparseOption option = new ArgparseOption("f", "foo", optionType, null,
          null, "Help text.");

      if (optionType == ArgparseOptionType.ARGPARSE_OPT_BOOLEAN) {
        assertFalse("Attribute 'isValueRequired' is supposed to be false for 'ARGPARSE_OPT_BOOLEAN' types.",
            option.isValueRequired);
      } else {
        assertTrue("Attribute 'isValueRequired' is supposed to be true for '" + optionType.toString() + "' types.",
            option.isValueRequired);
      }
    }
  }

  @Test
  public void testMethodToString() {
    final ArgparseOption option = new ArgparseOption("f", "foo", ArgparseOptionType.ARGPARSE_OPT_BOOLEAN, null,
        "false", "Help text.");

    final String optionStringified = "<ArgparseOption shortName('f'), longName('foo'), value('null'), optionType('ARGPARSE_OPT_BOOLEAN'), help('Help text.')>";

    assertEquals(option.toString(), optionStringified);
  }

  @Test
  public void testMethodToStringWithALongHelpText() {
    final ArgparseOption option = new ArgparseOption("f", "foo", ArgparseOptionType.ARGPARSE_OPT_BOOLEAN, null,
        "false",
        "This help text is written so lengthy just because we want to test if our toString() function even trims out the content after the specific length given to it or not.");

    final String optionStringified = "<ArgparseOption shortName('f'), longName('foo'), value('null'), optionType('ARGPARSE_OPT_BOOLEAN'), help('This help text is wr...')>";

    assertEquals(option.toString(), optionStringified);
  }
}
