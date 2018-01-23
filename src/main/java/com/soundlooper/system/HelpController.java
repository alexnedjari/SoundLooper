package com.soundlooper.system;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pegdown.PegDownProcessor;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
public class HelpController {

	@FXML
	private WebView webView;

	private Logger logger = LogManager.getLogger(this.getClass());

	public void init(Stage stage) {
		stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ESCAPE) {
					stage.close();
				}
			}
		});
	}

	public void loadContent(String fileName) {
		WebEngine engine = webView.getEngine();

		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		try {
			if (stream == null) {
				throw new IOException("The stream is null");
			}
			String markdown = IOUtils.toString(stream, Charset.forName("UTF-8"));
			PegDownProcessor processor = new PegDownProcessor();

			String html = processor.markdownToHtml(markdown);
			engine.loadContent(html);
		} catch (IOException e) {
			engine.loadContent("Impossible de charger l'aide");
			logger.error("Error loading file " + fileName, e);
		}
	}

}
