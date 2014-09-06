package com.aned.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 *-------------------------------------------------------
 * AudioEngine is an audio engine based on FMOD
 * Copyright (C) 2014 Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @author Alexandre NEDJARI
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class StackTracer {
	public static String getStackTrace(Throwable t) {
		String stackTrace = "";
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		stackTrace = sw.toString();

		return stackTrace;
	}
}
