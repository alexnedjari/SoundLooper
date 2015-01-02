/**
 *
 */
package com.aned.audio.player;

import org.apache.log4j.Logger;
import org.jouvieje.fmodex.FmodEx;
import org.jouvieje.fmodex.Init;
import org.jouvieje.fmodex.defines.FMOD_INITFLAGS;
import org.jouvieje.fmodex.defines.VERSIONS;
import org.jouvieje.fmodex.enumerations.FMOD_DSP_RESAMPLER;
import org.jouvieje.fmodex.enumerations.FMOD_SOUND_FORMAT;
import org.jouvieje.fmodex.exceptions.InitException;

import com.aned.audio.player.Player.PlayerState;
import com.aned.exception.PlayerException;
import com.aned.exception.PlayerRuntimeException;
import com.aned.exception.StackTracer;

/**
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
 * @author ANEDJARI
 *
 */
public class PlayerActionInit extends PlayerAction {

	/**
	 * Logger for this class
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * Constructor
	 * @param player the player
	 */
	public PlayerActionInit(Player player) {
		super(player);
	}

	@Override
	public void run() {
		try {
			this.getPlayer().setState(PlayerState.STATE_PLAYER_INITIALIZING);

			this.logger.info("JNI : avant load librairies");
			Init.loadLibraries();
			this.logger.info("JNI : après load librairies");

			this.logger.info("JNI : avant creation systeme");
			Player.errorCheck(FmodEx.System_Create(Player.getSystem()));
			this.logger.info("JNI : après creation systeme");

			if (VERSIONS.NATIVEFMODEX_LIBRARY_VERSION != VERSIONS.NATIVEFMODEX_JAR_VERSION) {
				throw new PlayerException("Error!  NativeFmodEx library version (" + VERSIONS.NATIVEFMODEX_LIBRARY_VERSION + ") is different to jar version ("
						+ VERSIONS.NATIVEFMODEX_JAR_VERSION + ")\n");
			}

			this.logger.info("JNI : avant initialisation format");
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_GCADPCM);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_IMAADPCM);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_MPEG);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM16);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM24);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM32);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCM8);
			//			this.initializeSoundFormat(FMOD_SOUND_FORMAT.FMOD_SOUND_FORMAT_PCMFLOAT);
			this.logger.info("JNI : après initialisation format");

			this.logger.info("JNI : avant initialisation systeme");
			try {
				Player.errorCheck(Player.getSystem().init(32, FMOD_INITFLAGS.FMOD_INIT_NORMAL, null));
				this.logger.info("JNI : après initialisation systeme");
			} catch (PlayerException e) {
				//cas particulier de la carte so
				this.logger.error(StackTracer.getStackTrace(e));
				throw new PlayerRuntimeException("Une erreur est survenue à l'initialisation du lecteur. Une carte son est elle disponible?", e);
			}

			this.getPlayer().setState(PlayerState.STATE_PLAYER_INITIALIZED);
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());

		} catch (InitException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			this.getPlayer().getMessageNotifier().sendError(e.getMessage());
		}
	}

	/**
	 * @param fmodSoundFormatCelt
	 */
	private void initializeSoundFormat(FMOD_SOUND_FORMAT fmodSoundFormatCelt) {
		try {
			Player.errorCheck(Player.getSystem().setSoftwareFormat(88200, fmodSoundFormatCelt, 0, 0, FMOD_DSP_RESAMPLER.FMOD_DSP_RESAMPLER_SPLINE));
		} catch (PlayerException e) {
			this.logger.error(StackTracer.getStackTrace(e));
			System.err.println("Impossible d'initialiser le format pour le type de son : " + fmodSoundFormatCelt);
		}

	}
}
