package com.soundlooper.system.mail;

/*
 * MailSender Created on 28 oct. 2005
 * @author Toon from insia
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

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
 *
 *
 * @author Alexandre NEDJARI
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class MailSender {

	final private static String CHARSET = "charset=ISO-8859-1";

	final private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	final private static int DEFAULT_SMTP_PORT = 25;

	final private Session _session;

	// Constructeur n°1: Connexion au serveur mail
	public MailSender(final String host, final int port, final String userName, final String password, final boolean ssl) {
		final String strPort = String.valueOf(port);
		final Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", strPort);
		if (ssl) {
			// Connection SSL
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			props.put("mail.smtp.socketFactory.class", MailSender.SSL_FACTORY);
			props.put("mail.smtp.socketFactory.fallback", "false");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", strPort);
		}
		if (null == userName || null == password) {
			this._session = Session.getDefaultInstance(props, null);
		} else {
			// Connexion avec authentification
			this._session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});
		}
	}

	// Autres constructeurs
	public MailSender(final String host, final String userName, final String password, final boolean ssl) {
		this(host, MailSender.DEFAULT_SMTP_PORT, userName, password, ssl);
	}

	public MailSender(final String host, final String userName, final String password) {
		this(host, MailSender.DEFAULT_SMTP_PORT, userName, password, false);
	}

	public MailSender(final String host, final int port) {
		this(host, port, null, null, false);
	}

	public MailSender(final String host) {
		this(host, MailSender.DEFAULT_SMTP_PORT, null, null, false);
	}

	// Convertit un texte au format html en texte brut
	private static final String HtmlToText(final String s) {
		final HTMLEditorKit kit = new HTMLEditorKit();
		final Document doc = kit.createDefaultDocument();
		try {
			kit.read(new StringReader(s), doc, 0);
			return doc.getText(0, doc.getLength()).trim();
		} catch (final IOException ioe) {
			return s;
		} catch (final BadLocationException ble) {
			return s;
		}
	}

	// Défini les fichiers à joindre
	private void setAttachmentPart(final String[] attachmentPaths, final MimeMultipart related, final MimeMultipart attachment, final String body, final boolean htmlText)
			throws MessagingException {
		for (int i = 0; i < attachmentPaths.length; ++i) {
			// Création du fichier à inclure
			final MimeBodyPart messageFilePart = new MimeBodyPart();
			final DataSource source = new FileDataSource(attachmentPaths[i]);
			final String fileName = source.getName();
			messageFilePart.setDataHandler(new DataHandler(source));
			messageFilePart.setFileName(fileName);
			// Image à inclure dans un texte au format HTML ou pièce jointe
			if (htmlText && null != body && body.matches(".*<img[^>]*src=[\"|']?cid:([\"|']?" + fileName + "[\"|']?)[^>]*>.*")) {
				// " <-- pour éviter une coloration syntaxique désastreuse...
				messageFilePart.setDisposition("inline");
				messageFilePart.setHeader("Content-ID", '<' + fileName + '>');
				related.addBodyPart(messageFilePart);
			} else {
				messageFilePart.setDisposition("attachment");
				attachment.addBodyPart(messageFilePart);
			}
		}
	}

	// Texte alternatif = texte + texte html
	private void setHtmlText(final MimeMultipart related, final MimeMultipart alternative, final String body) throws MessagingException {
		// Plain text
		final BodyPart plainText = new MimeBodyPart();
		plainText.setContent(MailSender.HtmlToText(body), "text/plain; " + MailSender.CHARSET);
		alternative.addBodyPart(plainText);
		// Html text ou Html text + images incluses
		final BodyPart htmlText = new MimeBodyPart();
		htmlText.setContent(body, "text/html; " + MailSender.CHARSET);
		if (0 != related.getCount()) {
			related.addBodyPart(htmlText, 0);
			final MimeBodyPart tmp = new MimeBodyPart();
			tmp.setContent(related);
			alternative.addBodyPart(tmp);
		} else {
			alternative.addBodyPart(htmlText);
		}
	}

	// Définition du corps de l'e-mail
	private void setContent(final Message message, final MimeMultipart alternative, final MimeMultipart attachment, final String body) throws MessagingException {
		if (0 != attachment.getCount()) {
			// Contenu mixte: Pièces jointes + texte
			if (0 != alternative.getCount() || null != body) {
				// Texte alternatif = texte + texte html
				final MimeBodyPart tmp = new MimeBodyPart();
				tmp.setContent(alternative);
				attachment.addBodyPart(tmp, 0);
			} else {
				// Juste du texte
				final BodyPart plainText = new MimeBodyPart();
				plainText.setContent(body, "text/plain; " + MailSender.CHARSET);
				attachment.addBodyPart(plainText, 0);
			}
			message.setContent(attachment);
		} else {
			// Juste un message texte
			if (0 != alternative.getCount()) {
				// Texte alternatif = texte + texte html
				message.setContent(alternative);
			} else {
				// Texte
				message.setText(body);
			}
		}
	}

	// Prototype n°1: Envoi de message avec pièce jointe
	public void sendMessage(final MailMessage mailMsg) throws MessagingException {
		final Message message = new MimeMessage(this._session);
		// Subect
		message.setSubject(mailMsg.getSubject());
		// Expéditeur
		message.setFrom(mailMsg.getFrom());
		// Destinataires
		message.setRecipients(Message.RecipientType.TO, mailMsg.getTo());
		message.setRecipients(Message.RecipientType.CC, mailMsg.getCc());
		message.setRecipients(Message.RecipientType.BCC, mailMsg.getBcc());
		// Contenu + pièces jointes + images
		final MimeMultipart related = new MimeMultipart("related");
		final MimeMultipart attachment = new MimeMultipart("mixed");
		final MimeMultipart alternative = new MimeMultipart("alternative");
		final String[] attachments = mailMsg.getAttachmentURL();
		final String body = (String) mailMsg.getContent();
		final boolean html = mailMsg.isHtml();
		if (null != attachments) {
			this.setAttachmentPart(attachments, related, attachment, body, html);
		}
		if (html && null != body) {
			this.setHtmlText(related, alternative, body);
		}
		this.setContent(message, alternative, attachment, body);
		// Date d'envoi
		message.setSentDate(mailMsg.getSendDate());
		// Envoi
		Transport.send(message);
		// Réinitialise le message
		mailMsg.reset();
	}

	// Exemples
	public static void main(final String[] args) throws UnsupportedEncodingException, IOException, MessagingException {
		//		// connexion au serveur de mail
		//		final MailSender mail1 = new MailSender("smtp.xxxxxx.xxx");
		//
		//		// Message simple : (from et to sont indispensables)
		//		final MailMessage msg = new MailMessage();
		//		msg.setFrom("xxxxxx@xxxxxx.xxx");
		//		msg.setTo("xxxxxx@xxxxxx.xxx");
		//		msg.setCc("xxxxxx@xxxxxx.xxx");
		//		msg.setSubject("sujet");
		//		msg.setContent("corps du message", false);
		//		mail1.sendMessage(msg);
		//
		//		// connexion à un autre serveur de mail
		//		// (l'activation du compte pop est nécessaire pour gmail)
		//		final MailSender mail2 = new MailSender("smtp.gmail.com", 465, "xxxxxx", "xxxxxx", true);
		//
		//		// Message avec texte html + images incluses + pièces jointes
		//		msg.setFrom(new InternetAddress("martin@gmail.net", "Martin john"));
		//		msg.setTo("dupont@gmail.com");
		//		msg.setCc(new InternetAddress[] { new InternetAddress("gaston@gmail.net", "Gaston lagaffe"), new InternetAddress("gilbert@gmail.net") });
		//		msg.setSubject("sujet");
		//		msg.setContent("<p><h1>Salut</h1></p>" + "<p>Image 1:<img src=\"cid:image1.jpg\"></p>" + "<p>Image 2:<img src=\"cid:image2.jpg\"></p>" + "<p>Encore image 1:<img src=\"cid:image1.jpg\"></p>", true);
		//		msg.setAttachmentURL(new String[] { "c:\\toto.txt", "c:\\image1.jpg", "c:\\tata.txt", "c:\\image2.jpg" });
		//		mail2.sendMessage(msg);

		////		// connexion au serveur de mail
		//		final MailSender mail1 = new MailSender("smtp.live.com");
		////
		////		// Message simple : (from et to sont indispensables)
		final MailMessage msg = new MailMessage();
		//		msg.setFrom("alex_nedjari@hotmail.com");
		//		msg.setTo("alex.nedjari@gmail.com");
		////		msg.setCc("xxxxxx@xxxxxx.xxx");
		//		msg.setSubject("sujet");
		//		msg.setContent("corps du message", false);
		//		mail1.sendMessage(msg);

		// connexion à un autre serveur de mail
		// (l'activation du compte pop est nécessaire pour gmail)
		final MailSender mail2 = new MailSender("smtp.gmail.com", 465, "alex.nedjari@gmail.com", "mdp", true);

		// Message avec texte html + images incluses + pièces jointes
		msg.setFrom(new InternetAddress("alex.nedjari@gmail.com", "Alexandre NEDJARI"));
		msg.setTo("alex.nedjari@gmail.com");
		//msg.setCc(new InternetAddress[] { new InternetAddress("gaston@gmail.net", "Gaston lagaffe"), new InternetAddress("gilbert@gmail.net") });
		msg.setSubject("sujet");
		msg.setContent("<p><h1>Salut</h1></p>" + "<p>Image 1:<img src=\"cid:image1.jpg\"></p>" + "<p>Image 2:<img src=\"cid:image2.jpg\"></p>"
				+ "<p>Encore image 1:<img src=\"cid:image1.jpg\"></p>", true);
		//msg.setAttachmentURL(new String[] { "c:\\toto.txt", "c:\\image1.jpg", "c:\\tata.txt", "c:\\image2.jpg" });
		mail2.sendMessage(msg);
	}
}