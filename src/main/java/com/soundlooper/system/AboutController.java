package com.soundlooper.system;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.system.preferences.SoundLooperProperties;
import com.soundlooper.system.util.MessagingUtil;

/**
 * Properned is a software that can be used to edit java properties files 2015
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
 * @author Alexandre NEDJARI
 * @since 28 october 2015
 */
public class AboutController {

	@FXML
	private TextArea textAreaLicence;

	@FXML
	private Label labelApplication;

	@FXML
	private Label labelAuthorValue;

	private Logger logger = LogManager.getLogger(this.getClass());

	public void initialize() throws IOException, URISyntaxException {
		logger.info("Initialize about controller");
		labelApplication.setText(SoundLooperProperties.getInstance().getApplicationPresentation());
		labelAuthorValue.setText(SoundLooperProperties.getInstance().getAuthor());
		try {
			String licence = FileUtils.readFileToString(new File("linkedlibrairies.txt"), Charset.forName("UTF-8"));
			textAreaLicence.setText(licence);
		} catch (IOException e) {
			MessagingUtil.displayError("Impossible d'afficher les licences associ�es", e);
		}
	}
}
