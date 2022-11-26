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
 * Creates a parser option.
 */
public class ArgparseOption {
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
        + "'), help('" + trimmedHelpString + "')>");
  }
}
