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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class TestWrapText {
  @Test
  public void testWrapText() {
    String text =
        "This is a sample text to be wrapped. It has some words that are longer than the specified"
            + " width.";
    int width = 20;
    boolean handleWhitespaces = true;
    boolean handleNewLines = false;
    boolean handleSpecialCharacters = false;
    boolean indentNextLine = false;
    String indentationString = "    ";
    String specialCharacterHandlingStrategy = "console";
    List<String> expected =
        Arrays.asList(
            "This is a sample ",
            "text to be wrapped. ",
            "It has some words ",
            "that are longer than ",
            "the specified width.");
    List<String> result = new ArrayList<String>();
    try {
      result =
          Argparse.wrapText(
              text,
              width,
              handleWhitespaces,
              handleNewLines,
              handleSpecialCharacters,
              specialCharacterHandlingStrategy,
              indentNextLine,
              indentationString);
    } catch (final Exception e) {
      System.err.printf("%s", e.getMessage());
    }
    assertEquals(expected, result);
  }

  @Test
  public void testWrapTextWithNewLines() {
    String text =
        "This is a sample text\n"
            + " to be wrapped. It has some words that are longer than the specified width.";
    int width = 20;
    boolean handleWhitespaces = true;
    boolean handleNewLines = true;
    boolean handleSpecialCharacters = false;
    boolean indentNextLine = false;
    String indentationString = "";
    String specialCharacterHandlingStrategy = "console";
    List<String> expected =
        Arrays.asList(
            "This is a sample ",
            "text to be wrapped. ",
            "It has some words ",
            "that are longer than ",
            "the specified width.");
    List<String> result = new ArrayList<String>();
    try {
      result =
          Argparse.wrapText(
              text,
              width,
              handleWhitespaces,
              handleNewLines,
              handleSpecialCharacters,
              specialCharacterHandlingStrategy,
              indentNextLine,
              indentationString);
    } catch (final Exception e) {
      System.err.printf("%s", e.getMessage());
    }
    assertEquals(expected, result);
  }
}
