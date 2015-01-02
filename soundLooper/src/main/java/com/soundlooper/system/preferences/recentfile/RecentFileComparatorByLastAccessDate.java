/**
 *
 */
package com.soundlooper.system.preferences.recentfile;

import java.util.Comparator;

/**
 *-------------------------------------------------------
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
 * Compare by date
 *
 * @author Alexandre NEDJARI
 * @since  19 juin 2012
 *-------------------------------------------------------
 */
public class RecentFileComparatorByLastAccessDate implements Comparator<RecentFile> {

	@Override
	public int compare(RecentFile o1, RecentFile o2) {
		return o1.getLastAccessDate().compareTo(o2.getLastAccessDate());
	}

}
