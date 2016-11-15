/**
 *
 */
package com.soundlooper.system.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.soundlooper.exception.AlreadyLockedException;

/**--------------------------------------------------------------------------------
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
 * @since 8 août 2011
 *--------------------------------------------------------------------------------
 */
public class Lock {

	/**
	 * The lock map
	 */
	private static HashMap<String, FileOutputStream> locksMap = new HashMap<String, FileOutputStream>();

	/**
	 * Put a lock on a name (creating a file with this name)
	 * @param lockName the lock name
	 * @throws AlreadyLockedException if the lock is already put
	 * @throws IOException If an unknown error occurred
	 */
	public static void lock(String lockName) throws AlreadyLockedException, IOException {
		if (Lock.isLocked(lockName)) {
			throw new AlreadyLockedException("Lock '" + lockName + "' is aready locked");
		}
		File lockFile = Lock.getLockFile(lockName);
		lockFile.createNewFile();
		lockFile.deleteOnExit();
		Lock.locksMap.put(lockName, new FileOutputStream(lockFile));
	}

	/**
	 * Get the lock file from name
	 * @param lockName the lock name
	 * @return the lock file from name
	 */
	private static File getLockFile(String lockName) {
		return new File(lockName + ".lock");
	}

	/**
	 * Check is the lock corresponding to the name is locked
	 * @param lockName the lock name
	 * @return true if the locked is in use
	 */
	public static boolean isLocked(String lockName) {
		File lockFile = Lock.getLockFile(lockName);
		if (lockFile.exists() && !lockFile.delete()) {
			//File exists with a stream on it, so program is still opened
			return true;
		}
		return false;
	}

	/**
	 * Release a lock on a name (creating a file with this name)
	 * @param lockName the lock name
	 * @throws IOException If an error occured
	 */
	public static void unlock(String lockName) throws IOException {
		try {
			if (Lock.locksMap.containsKey(lockName)) {
				Lock.locksMap.get(lockName).close();
				Lock.locksMap.remove(lockName);
			} else {
				throw new IOException("Lock name '" + lockName + "is not in map");
			}
		} finally {
			Lock.getLockFile(lockName).delete();
		}
	}
}
