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

public enum ArgparseOptionType {
  /**
   * This option does not have to do with values that an option can accept over
   * the command-line but the way an option can be used over the command-line. A
   * group restricts the use of options over the command-line. If there are
   * several options that shares a group, only one at a time can be used over the
   * command-line.
   */
  ARGPARSE_OPT_GROUP,

  /**
   * These types of options can only accept boolean values or there presence over
   * the command-line can also be considered as a boolean value for them, means if
   * they are present over the command-line the value would be considered as true,
   * false otherwise.
   */
  ARGPARSE_OPT_BOOLEAN,

  /**
   * These types of options can only accept bit values. The only two discrete bit
   * values that we can have is 0 and 1. None other than these values can be used
   * over the command-line for these types of arguments or an error will be
   * raised.
   */
  ARGPARSE_OPT_BIT,

  /**
   * These types of options can only accept integer values i.e., can only accept
   * those strings that can be parsed to a valid integer value.
   */
  ARGPARSE_OPT_INTEGER,

  /**
   * These types of options can only accept float values i.e., can only accept
   * those strings that can be parsed to a valid floating point value.
   */
  ARGPARSE_OPT_FLOAT,

  /**
   * These types of options can only accept strings values. By default everything
   * given over the command line is a string value.
   */
  ARGPARSE_OPT_STRING,
}
