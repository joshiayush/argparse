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

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OS Utils; cannot be inheritable further.
 * 
 * Defines common utility functions to identify the OS.
 */
public final class OsUtils {
  private static String OS = null;

  /**
   * Command to obtain the column size of a terminal in a Linux machine.
   * 
   * @TODO: Also handle the case when the library is running on Windows, and MacOS
   *        machines.
   */
  private static final String LINUX_TERMINAL_COL_WIDTH_CMD = "tput cols";

  /**
   * Logger for class `OsUtils`. Instantiate with the name of the class.
   */
  public static final Logger LOGGER = Logger.getLogger(Argparse.class.toString());

  /**
   * Returns the name of the Operating System.
   */
  public static String getOsName() {
    if (OS == null) {
      OS = System.getProperty("os.name");
    }
    return OS;
  }

  /**
   * Returns true if the Operating System is Windows.
   */
  public static boolean isWindows() {
    return getOsName().toLowerCase().indexOf("win") >= 0;
  }

  /**
   * Returns true if the Operating System is Unix.
   */
  public static boolean isUnix() {
    return getOsName().toLowerCase().indexOf("nix") >= 0 || getOsName().toLowerCase().indexOf("nux") >= 0
        || getOsName().toLowerCase().indexOf("aix") >= 0;
  }

  /**
   * Returns true if the Operating System is MacOS.
   */
  public static boolean isMac() {
    return getOsName().toLowerCase().indexOf("mac") >= 0;
  }

  /**
   * Returns true if the Operating System is Solaris.
   */
  public static boolean isSolaris() {
    return getOsName().toLowerCase().indexOf("sunos") >= 0;
  }

  /**
   * getTerminalWidth() returns the number of columns present in the terminal
   * open.
   * 
   * This function is only supported in Linux and returns `-1` when used in MacOs,
   * or Windows.
   */
  public static int getTerminalWidth() {
    /**
     * @TODO: Make this function compatible with MacOS and Windows.
     */
    if (!OsUtils.isUnix())
      return -1;
    try {
      /**
       * Note, we don't use `ProcessBuilder` because it internally uses `ForkAndExec`
       * which in turns makes the copy of the entire parents address space, so that
       * even a little command can lead to `OutOfMemoryErrors`, when the parent
       * process has big amount of memory acquired.
       */
      return Integer.parseInt(Runtime.getRuntime().exec(OsUtils.LINUX_TERMINAL_COL_WIDTH_CMD).toString());
    } catch (IOException | NumberFormatException e) {
      OsUtils.LOGGER.log(Level.WARNING, e.getMessage());
      return -1;
    }
  }
}
