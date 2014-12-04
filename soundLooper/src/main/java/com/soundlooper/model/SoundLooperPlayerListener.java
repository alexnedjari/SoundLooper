/**
 *
 */
package com.soundlooper.model;

import java.awt.image.BufferedImage;

import com.aned.exception.PlayerRuntimeException;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;

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
 * Listener for sound looper
 *
 * @author Alexandre NEDJARI
 * @since  17 févr. 2014
 *-------------------------------------------------------
 */
public interface SoundLooperPlayerListener {

	/**
	 * Called when the played location is changed
	 * @param newMillisecondLocation the new position in sound
	 */
	public void onPlayLocationChanged(int newMillisecondLocation);

	/**
	 * Called when the loop points are changed
	 * @param beginPoint the loop begin point
	 * @param endPoint the loop end point
	 */
	public void onLoopPointChanged(int beginPoint, int endPoint);

	/**
	 * Called when volume is modified
	 * @param percent the new percent
	 */
	public void onVolumeUpdate(int percent);

	/**
	 * Called when the timestretch is updated
	 * @param percent the new percent
	 */
	public void onTimestretchUpdated(int percent);

	/**
	 * Called when a song load is complete
	 * @param song the loaded song
	 */
	public void onSongLoaded(Song song);

	/**
	 * @param e
	 */
	public void onFatalError(PlayerRuntimeException e);

	/**
	 * Called when the song image generation start
	 */
	public void onBeginGenerateImage();

	/**
	 * Called when the song image generation start
	 */
	public void onEndGenerateImage(BufferedImage image);

}
