/**
 *
 */
package com.soundlooper.system.util;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * ====================================================================
 * Sound Looper is an audio player that allow user to loop between two points
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
 * @author Alexandre NEDJARI
 * @since 9 mars 2010
 *
 * ====================================================================
 */
public class TimeMeasurer {

	/**
	 * The begin time of the measure
	 */
	private long localBeginTime;

	/**
	 * The name of the measure
	 */
	private String localProcessName;

	/**
	 * The indentation level of log for this measure
	 * (number of tabulations)
	 */
	private int indentation;

	/**
	 * The logger used to log result
	 */
	private Logger logger;

	/**
	 * Constructor
	 * @param indentation The indentation level of log for this measure
	 * @param logger The logger used to log result
	 */
	public TimeMeasurer(int indentation, Logger logger) {
		this.indentation = indentation;
		this.logger = logger;
	}

	/**
	 * Constructor
	 * @param logger The logger used to log result
	 */
	public TimeMeasurer(Logger logger) {
		this(0, logger);
	}

	/**
	 * Start a new measure
	 * @param measureName the name of the measure
	 */
	public void startMeasure(String measureName) {
		this.localProcessName = measureName;
		this.localBeginTime = new Date().getTime();
	}

	/**
	 * End the current measure and log result
	 */
	public void endLocalMeasure() {
		long millisecondTime = new Date().getTime() - this.localBeginTime;
		StringBuffer tabs = new StringBuffer();
		for (int i = 0; i < this.indentation; i++) {
			tabs.append('\t');
		}
		this.logger.info(tabs + "Treatment \"" + this.localProcessName + "\" ended in " + millisecondTime + "ms");
	}

	/**
	 * Stop the current measure, log it and start a nex measure
	 * @param measureName the name of the new measure
	 */
	public void endAndStartNewMeasure(String measureName) {
		this.endLocalMeasure();
		this.startMeasure(measureName);
	}
}
