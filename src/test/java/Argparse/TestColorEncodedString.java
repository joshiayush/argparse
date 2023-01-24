
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

import org.junit.Test;
import static org.junit.Assert.*;

public class TestColorEncodedString {
  @Test
  public void testColorEncodedString() {
    String text = "This is a @Rsample text@B to be @Gformatted.";
    String expected = "This is a \033[0;31msample text\033[0;34m to be \033[0;32mformatted.\033[0m";
    String result = Argparse.colorEncodedString(text);
    assertEquals(expected, result);
  }

  @Test
  public void testColorEncodedStringWithDoubleAtTheRate() {
    String text = "This is a @@Rsample text@@B to be @@Gformatted.";
    String expected = "This is a @Rsample text@B to be @Gformatted.\033[0m";
    String result = Argparse.colorEncodedString(text);
    assertEquals(expected, result);
  }

  @Test
  public void testColorEncodedStringWithRegularText() {
    String text = "This is a sample text with @Rcolor@B encoders@G, but regular text.";
    String expected = "This is a sample text with \033[0;31mcolor\033[0;34m encoders\033[0;32m, but regular text.\033[0m";
    String result = Argparse.colorEncodedString(text);
    assertEquals(expected, result);
  }
}