/**
 *
 */
package com.soundlooper.audio.player;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jouvieje.fmodex.Channel;
import org.jouvieje.fmodex.DSP;
import org.jouvieje.fmodex.Sound;
import org.jouvieje.fmodex.defines.FMOD_MODE;
import org.jouvieje.fmodex.defines.FMOD_TIMEUNIT;
import org.jouvieje.fmodex.enumerations.FMOD_CHANNELINDEX;
import org.jouvieje.fmodex.enumerations.FMOD_DSP_PITCHSHIFT;
import org.jouvieje.fmodex.enumerations.FMOD_DSP_TYPE;
import org.jouvieje.fmodex.enumerations.FMOD_RESULT;
import org.jouvieje.fmodex.structures.FMOD_CREATESOUNDEXINFO;
import org.jouvieje.fmodex.utils.BufferUtils;
import org.jouvieje.fmodex.utils.SizeOfPrimitive;

import com.soundlooper.exception.PlayerException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.SoundLooperColor;

/**
 * ----------------------------------------------------------------------------
 * ---- AudioEngine is an audio engine based on FMOD Copyright (C) 2014
 * Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * represent a sound
 *
 * @author Alexandre NEDJARI
 * @since 9 août 2011
 *        ------------------------------------------------------------
 *        --------------------
 */
public class SoundFile {

	/**
	 * Timer to update the slider media time
	 */
	private Timer timerSlide;

	/**
	 * The original file
	 */
	private File file;

	/**
	 * The sound
	 */
	Sound sound = new Sound();

	/**
	 * The channel
	 */
	Channel channel = new Channel();

	/**
	 * The initial frequency of the sound
	 */
	float initialFrequency;

	/**
	 * The logger dor this class
	 */
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * DSP for pitch effect (for timstretch)
	 */
	private DSP dspPitch = new DSP();

	/**
	 * duration of the current song
	 */
	int duration;

	/**
	 * Save the current timeStretch
	 */
	private int currentTimeStretch;

	/**
	 * buffer utilisé pour les appels
	 */
	ByteBuffer buffer = BufferUtils.newByteBuffer(SizeOfPrimitive.SIZEOF_INT);

	/**
	 * buffer utilisé pour les appels
	 */
	ByteBuffer buffer2 = BufferUtils.newByteBuffer(SizeOfPrimitive.SIZEOF_INT);

	/**
	 * Construct a sound from file
	 * 
	 * @param file
	 *            the file to read
	 * @throws PlayerException
	 *             If a {@link PlayerException} is threw
	 * @throws IOException
	 *             If an {@link IOException} is threw
	 */
	public SoundFile(File file, Player player) throws PlayerException, IOException {
		super();
		logger.info("Create sound file on file '" + file.getAbsolutePath() + "'");
		this.file = file;
		player.setCurrentSound(this);

		// A SUPPRIMER
		FMOD_CREATESOUNDEXINFO exinfo = this.getExifInfo();

		// A RESTAURER
		// ByteBuffer soundBuffer =
		// SoundFile.loadMediaIntoMemory(this.file.getAbsolutePath());
		//
		// this.logger.info("JNI : Avant allocation exinfo");
		// FMOD_CREATESOUNDEXINFO exinfo = FMOD_CREATESOUNDEXINFO.allocate();
		// exinfo.setLength(soundBuffer.capacity());

		// /////
		this.logger.info("JNI : Après allocation exinfo");

		this.logger.info("JNI : Avant création du stream");
		Player.errorCheck(Player.getSystem().createStream(this.file.getAbsolutePath(),
				FMOD_MODE.FMOD_SOFTWARE | FMOD_MODE.FMOD_ACCURATETIME | FMOD_MODE.FMOD_LOOP_NORMAL, exinfo, this.sound));
		this.logger.info("JNI : Après création du stream");
		// soundBuffer.clear();
		// soundBuffer = null;
		exinfo.release();

		this.logger.info("JNI : Avant lecture du son (pour initialiser le channel");
		Player.errorCheck(Player.getSystem().playSound(FMOD_CHANNELINDEX.FMOD_CHANNEL_FREE, this.sound, false,
				this.channel));
		this.logger.info("JNI : Après lecture du son (pour initialiser le channel");

		this.logger.info("JNI : Avant pause du son (pour initialiser le channel");
		Player.errorCheck(this.channel.setPaused(true));
		this.stopTimer();
		this.logger.info("JNI : Après pause du son (pour initialiser le channel");

		// FloatBuffer frequencyBuffer = BufferUtils.newFloatBuffer(1);

		this.logger.info("JNI : Avant récupération de la fréquence");
		Player.errorCheck(this.channel.getFrequency(this.buffer.asFloatBuffer()));
		this.logger.info("JNI : Après récupération de la fréquence");

		this.initialFrequency = this.buffer.getFloat(0);
		this.buffer.clear();

		this.initializeDuration();
		this.initializeLoopPoint();

		this.setTimeStrechPercent(100);
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private FMOD_CREATESOUNDEXINFO getExifInfo() throws FileNotFoundException, IOException {
		InputStream is = PlayerActionLoad.class.getResourceAsStream(this.file.getAbsolutePath());
		if (is == null) {
			if (new File(this.file.getAbsolutePath()).exists()) {
				is = new FileInputStream(new File(this.file.getAbsolutePath()));
			} else if (new File("." + this.file.getAbsolutePath()).exists()) {
				is = new FileInputStream(new File("." + this.file.getAbsolutePath()));
			} else {
				throw new FileNotFoundException("Le fichier '" + this.file.getAbsolutePath() + "' n'existe pas");
			}
		}
		this.logger.info("JNI : Avant allocation exinfo");
		FMOD_CREATESOUNDEXINFO exinfo = FMOD_CREATESOUNDEXINFO.allocate();
		exinfo.setLength(is.available());
		return exinfo;
	}

	/**
	 * Retourne la représentation temporelle de la chanson
	 * 
	 * @throws PlayerException
	 *             Si une fonction retourne une erreur
	 */
	public BufferedImage generateImage() throws PlayerException, IOException {
		// Comme cette génération est faite dans un thread à part, n'utilise que
		// des variables locales pour éviter les conflits
		File fileLocal = this.file;
		ByteBuffer bufferLocal = BufferUtils.newByteBuffer(SizeOfPrimitive.SIZEOF_INT);

		FMOD_CREATESOUNDEXINFO exinfo = this.getExifInfo();
		Sound soundImage = new Sound();
		Player.errorCheck(Player.getSystem().createStream(fileLocal.getAbsolutePath(),
				FMOD_MODE.FMOD_SOFTWARE | FMOD_MODE.FMOD_ACCURATETIME | FMOD_MODE.FMOD_LOOP_NORMAL, exinfo, soundImage));
		exinfo.release();

		// http: //www.asawicki.info/news_1385_music_analysis_-_spectrogram.html
		Player.errorCheck(soundImage.getLength(bufferLocal.asIntBuffer(), FMOD_TIMEUNIT.FMOD_TIMEUNIT_PCM));
		int tailleTotaleInt = bufferLocal.getInt(0);
		bufferLocal.clear();

		int min = Integer.MAX_VALUE;
		int max = 0;

		int largeurImage = 2048;
		int hauteurImage = 512;
		int facteurImageY = (Integer.MAX_VALUE / hauteurImage * 2);

		// The signal must take 60 percent of the total height
		facteurImageY = facteurImageY / 60 * 100;

		int sampleParPixel = tailleTotaleInt / largeurImage;

		// La taille du beffer est un multiple du nombre de samble par pixel *
		// 4, pour éviter tout problème de décalage
		int tailleBuffer = sampleParPixel * 4;
		ByteBuffer dataBuffer = BufferUtils.newByteBuffer(SizeOfPrimitive.SIZEOF_BYTE * tailleBuffer);
		IntBuffer nbReadBuffer = BufferUtils.newIntBuffer(SizeOfPrimitive.SIZEOF_INT);

		int minImage = new Double(min * facteurImageY).intValue();
		int maxImage = new Double(max * facteurImageY).intValue();

		soundImage.seekData(0);
		BufferedImage off_Image = new BufferedImage(largeurImage, hauteurImage, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = off_Image.createGraphics();

		g2.setColor(SoundLooperColor.getAwtWhite());
		g2.fillRect(0, 0, largeurImage, hauteurImage);
		g2.setColor(SoundLooperColor.getAwtBlue());

		int pixelX = 0;
		FMOD_RESULT resultat;
		int lu;
		int valeur;
		IntBuffer intDataBuffer;
		do {
			resultat = soundImage.readData(dataBuffer, tailleBuffer, nbReadBuffer);
			lu = nbReadBuffer.get() / 4;

			intDataBuffer = dataBuffer.asIntBuffer();
			min = Integer.MAX_VALUE;
			max = 0;
			for (int i = 0; i < lu; i++) {
				valeur = intDataBuffer.get(i);
				if (valeur > max) {
					max = valeur;
				}
				if (valeur < min) {
					min = valeur;
				}
				if ((i + 1) % sampleParPixel == 0) {
					minImage = min / facteurImageY;
					maxImage = max / facteurImageY;
					// on vient de finir de calculer un pixel, on le sauvegarde
					// et on réinitialise les variables
					if (minImage > 0) {
						g2.drawRect(pixelX, hauteurImage / 2 - minImage, 1, minImage);
					} else {
						g2.drawRect(pixelX, hauteurImage / 2, 1, -minImage);
					}
					if (maxImage > 0) {
						g2.drawRect(pixelX, hauteurImage / 2 - maxImage, 1, maxImage);
					} else {
						g2.drawRect(pixelX, hauteurImage / 2, 1, -maxImage);
					}

					// On met à jour les variables
					min = Integer.MAX_VALUE;
					max = 0;
					pixelX++;
				}

			}
			dataBuffer.clear();
			nbReadBuffer.clear();
		} while (resultat != FMOD_RESULT.FMOD_ERR_FILE_EOF);

		dataBuffer.clear();
		return off_Image;
	}

	/**
	 * get the file
	 * 
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * Update the song volume
	 * 
	 * @param volume
	 *            the new volume value (between 0 and 1)
	 * @throws PlayerException
	 *             If the volume cannot be set
	 */
	public void setVolume(float volume) throws PlayerException {
		this.logger.info("JNI : Avant application volume");
		Player.errorCheck(this.channel.setVolume(volume));
		this.logger.info("JNI : Après application volume");

	}

	/**
	 * Deallocate the sound and free streams
	 * 
	 * @throws PlayerException
	 *             If deallocation fail
	 *
	 */
	public void deallocate() throws PlayerException {
		this.logger.info("JNI : Avant désallocation chanson");
		Player.errorCheck(this.channel.stop());
		this.stopTimer();
		this.logger.info("JNI : Après désallocation chanson");
	}

	/**
	 * Stop the play
	 * 
	 * @throws PlayerException
	 *             if a {@link PlayerException} is threw
	 */
	public void stop() throws PlayerException {
		this.logger.info("JNI : Avant stop chanson (pause)");
		Player.errorCheck(this.channel.setPaused(true));
		this.stopTimer();
		this.logger.info("JNI : Après stop chanson (pause)");
	}

	/**
	 * set the media time
	 * 
	 * @param millisecondMediaTime
	 *            the millisecond time
	 * @throws PlayerException
	 *             If a {@link PlayerException} is threw
	 */
	public void setMediaTime(int millisecondMediaTime) throws PlayerException {
		this.logger.info("JNI : Avant changement media time (" + millisecondMediaTime + "/" + this.duration + ")");
		Player.errorCheck(this.channel.setPosition(millisecondMediaTime, FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS));

		this.logger.info("JNI : Après changement media time (" + millisecondMediaTime + ")");
	}

	/**
	 * get the media duration in ms
	 * 
	 * @return the media duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * Initialize the duration
	 * 
	 * @throws PlayerException
	 *             if a {@link PlayerException} is threw
	 */
	private void initializeDuration() throws PlayerException {
		this.logger.info("JNI : Avant init durée");
		Player.errorCheck(this.sound.getLength(this.buffer.asIntBuffer(), FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS));
		this.logger.info("JNI : Après init durée");
		this.duration = this.buffer.getInt(0);
		this.buffer.clear();
	}

	/**
	 * Initialize the loop points
	 * 
	 * @throws PlayerException
	 *             if a {@link PlayerException} is threw
	 */
	private void initializeLoopPoint() throws PlayerException {
		this.logger.info("JNI : Avant init loop points");
		Player.errorCheck(this.sound.getLoopPoints(this.buffer.asIntBuffer(), FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS,
				this.buffer2.asIntBuffer(), FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS));
		this.logger.info("JNI : Après init loop points");
		int loopPointBegin = this.buffer.getInt(0);
		int loopPointEnd = this.buffer2.getInt(0);

		SoundLooperPlayer.getInstance().setLoopPointBegin(loopPointBegin);
		SoundLooperPlayer.getInstance().setLoopPointEnd(loopPointEnd);
		// setLoopPoints(loopPointBegin, loopPointEnd);
		this.buffer.clear();
		this.buffer2.clear();
	}

	/**
	 * get the media time in ms
	 * 
	 * @return the media time
	 * @throws PlayerException
	 *             If a {@link PlayerException} is threw
	 */
	protected int getMediaTime() throws PlayerException {
		Player.errorCheck(this.channel.getPosition(this.buffer.asIntBuffer(), FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS));
		int mediaTime = this.buffer.getInt(0);
		this.buffer.clear();
		// result.clear();
		return mediaTime;
	}

	/**
	 * Start playing song
	 */
	public void start() {
		this.logger.info("JNI : Avant play");
		this.channel.setPaused(false);
		this.logger.info("JNI : Après play");
	}

	/**
	 * Set a timestretch on the song
	 * 
	 * @param percent
	 *            the percent between 50% and 200%
	 * @throws PlayerException
	 *             if a {@link PlayerException} is threw
	 */
	public void setTimeStrechPercent(int percent) throws PlayerException {
		if (percent > 200 || percent < 50) {
			throw new PlayerException("for timeStretch, percent factor must be between 50% and 200%, here it's "
					+ percent + "%");
		}

		if (percent == 100) {
			this.setTimeStrech(1);
		}
		float factor = 0;
		if (percent > 100) {
			factor = 1 - ((percent - 100) / 200.0f);

		} else {
			factor = 1 + (100 - percent) / 50.0f;
		}
		this.setTimeStrech(factor);
		this.currentTimeStretch = percent;

	}

	/**
	 * remove the current timestretch
	 * 
	 * @throws PlayerException
	 *             If a {@link PlayerException} is threw
	 */
	public void removeTimeStrech() throws PlayerException {
		this.logger.info("JNI : Avant test dsp");
		if (!this.dspPitch.isNull()) {
			this.logger.info("JNI : Avant remove timestretch (dsp)");
			Player.errorCheck(this.dspPitch.release());
			this.logger.info("JNI : Après remove timestretch (dsp)");
			this.logger.info("JNI : Avant remove timestretch (frequency)");
			Player.errorCheck(this.channel.setFrequency(this.initialFrequency));
			this.logger.info("JNI : Après remove timestretch (frequency)");
		}

	}

	/**
	 * Apply a timestretch
	 * 
	 * @param facteur
	 *            the factor (between 0.5 an 2
	 * @throws PlayerException
	 *             if a playerException is threw
	 */
	private void setTimeStrech(float facteur) throws PlayerException {
		try {

			this.logger.info("JNI : Avant release du DSP");
			if (!this.dspPitch.isNull()) {
				Player.errorCheck(this.dspPitch.release());
			}
			this.logger.info("JNI : Après release du DSP");

			// set new Timestretch
			this.logger.info("JNI : Avant creation du DSP");
			Player.errorCheck(Player.getSystem().createDSPByType(FMOD_DSP_TYPE.FMOD_DSP_TYPE_PITCHSHIFT, this.dspPitch));
			this.logger.info("JNI : Avant setParameter du DSP");
			Player.errorCheck(this.dspPitch.setParameter(FMOD_DSP_PITCHSHIFT.FMOD_DSP_PITCHSHIFT_PITCH.asInt(), facteur));

			this.logger.info("JNI : Avant add du DSP");
			Player.errorCheck(Player.getSystem().addDSP(this.dspPitch, null));
			this.logger.info("JNI : Après add du DSP");

			this.logger.info("JNI : Avant changement de fréquence : " + (this.initialFrequency / facteur));
			Player.errorCheck(this.channel.setFrequency(this.initialFrequency / facteur));
			this.logger.info("JNI : Après changement de fréquence");

		} catch (PlayerException e) {
			// if an exception occurred, try to restore original timestrecht
			this.logger.info("JNI : Avant restauration DSP (frequency)");
			this.channel.setFrequency(this.initialFrequency);
			this.logger.info("JNI : Avant restauration DSP (DSP)");
			this.dspPitch.release();
			this.logger.info("JNI : Après restauration DSP");
			throw e;
		}
	}

	/**
	 * set the loop points
	 * 
	 * @param beginPoint
	 *            the begin point
	 * @param endPoint
	 *            the end point
	 * @throws PlayerException
	 *             if a PlayerException is threw
	 */
	public void setLoopPoints(int beginPoint, int endPoint) throws PlayerException {
		int mediaTime = getMediaTime();
		this.logger.info("JNI : Avant set loop points " + beginPoint + " to " + endPoint + "(position:" + mediaTime
				+ ", duration:" + this.getDuration() + ")");
		if (endPoint == this.duration) {
			endPoint--;
		}
		logger.error("CHANGEMENT DE LOOP POINT : " + beginPoint + " -> " + endPoint);
		Player.errorCheck(this.channel.setLoopPoints(beginPoint, FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS, endPoint,
				FMOD_TIMEUNIT.FMOD_TIMEUNIT_MS));
		if (mediaTime > endPoint || mediaTime < beginPoint) {
			setMediaTime(beginPoint);
		}

		this.logger.info("JNI : Après set loop points ");

	}

	/**
	 * @return the currentTimeStretch
	 */
	public int getCurrentTimeStretch() {
		return this.currentTimeStretch;
	}

	/**
	 * Stop the timer for the slider
	 */
	public void stopTimer() {
		if (this.timerSlide != null) {
			this.timerSlide.cancel();
			this.timerSlide = null;
		}
	}

}
