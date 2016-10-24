/**
 *
 */
package com.soundlooper.audio.player;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.exception.PlayerException;
import com.soundlooper.system.util.FileUtil;
import com.soundlooper.system.util.MessagingUtil;

/**
 * ------------------------------------------------------- AudioEngine is an
 * audio engine based on FMOD Copyright (C) 2014 Alexandre NEDJARI
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
 *
 * @author Alexandre NEDJARI
 * @since 27 août 2014 -------------------------------------------------------
 */
public class ThreadImageGenerator extends Thread {
	private Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * The current player (corresponding to the current song)
	 */
	private SoundFile sound;

	private Consumer<File> onSuccessConsumer;

	/**
	 * @param notifier
	 * @param listPlayerActionListener
	 * @param sound
	 */
	public ThreadImageGenerator(SoundFile sound, Consumer<File> onSuccessConsumer) {
		super();
		logger.info("Image thread generation fot sound '" + sound.getFile().getAbsolutePath() + "'");
		this.sound = sound;
		this.onSuccessConsumer = onSuccessConsumer;
	}

	@Override
	public void run() {
		try {
			logger.info("Start image generation for file '" + sound.getFile().getAbsolutePath() + "'");
			BufferedImage image = this.sound.generateImage();
			if (this.isInterrupted()) {
				return;
			}
			File tempFile = FileUtil.getTempFile("SLImage_", ".jpg");
			BufferedOutputStream baos = new BufferedOutputStream(new FileOutputStream(tempFile));
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			baos.close();

			logger.info("End image generation for file '" + sound.getFile().getAbsolutePath() + "' : consumer accept");
			onSuccessConsumer.accept(tempFile);

		} catch (PlayerException | IOException e) {
			MessagingUtil.displayError("Impossible de générer l'image pour le fichier '"
					+ sound.getFile().getAbsolutePath() + "'", e);
		}
	}
}
