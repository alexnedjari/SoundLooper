/**
 *
 */
package com.aned.audio.player;

import java.awt.image.BufferedImage;
import java.io.File;

import com.aned.exception.PlayerRuntimeException;

/**
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
 * @since  16 févr. 2014
 *-------------------------------------------------------
 */
public interface PlayerActionListener {

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
	 * @param percent the new percent
	 */
	public void onTimestretchUpdated(int percent);

	/**
	 * Called when the song is loaded
	 * @param songFile the loaded file
	 */
	public void onSongLoaded(File songFile);

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
