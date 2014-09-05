package com.soundlooper.system.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
public final class MailMessage {

	private String _subject = "";

	private Object _content = "";

	private boolean _html = false;

	private InternetAddress _from = null;

	private InternetAddress[] _to = null;

	private InternetAddress[] _cc = null;

	private InternetAddress[] _bcc = null;

	private String[] _attachmentURL = null;

	private Date _sendDate = new Date();

	private Date _receivedDate = null;

	public Date get_receivedDate() {
		return this._receivedDate;
	}

	private InternetAddress[] getAddress(final InternetAddress address) {
		return new InternetAddress[] { address };
	}

	private InternetAddress[] getAddress(final String[] address) throws AddressException {
		final InternetAddress[] result = new InternetAddress[address.length];
		for (int i = 0; i < address.length; ++i) {
			result[i] = new InternetAddress(address[i]);
		}
		return result;
	}

	private InternetAddress[] getAddress(final String address) throws AddressException {
		return new InternetAddress[] { new InternetAddress(address) };
	}

	private InternetAddress[] getInternetAddress(final Address[] address) throws AddressException, UnsupportedEncodingException {
		if (null == address) {
			return null;
		}
		final InternetAddress[] result = new InternetAddress[address.length];
		for (int i = 0; i < address.length; ++i) {
			result[i] = new InternetAddress(MailMessage.decodeText(address[i].toString()));
		}
		return result;
	}

	public MailMessage(final Message msg) throws IOException, MessagingException {
		this._from = this.getInternetAddress(msg.getFrom())[0];
		this._to = this.getInternetAddress(msg.getRecipients(Message.RecipientType.TO));
		this._cc = this.getInternetAddress(msg.getRecipients(Message.RecipientType.CC));
		this._subject = msg.getSubject();
		this._content = msg.getContent();
		this._sendDate = msg.getSentDate();
		this._receivedDate = msg.getReceivedDate();
	}

	public MailMessage() {
		//rien à faire
	}

	private static String decodeText(final String text) throws UnsupportedEncodingException {
		return null == text ? text : new String(text.getBytes("iso-8859-1"));
	}

	public void reset() {
		this._subject = "";
		this._content = "";
		this._html = false;
		this._from = null;
		this._to = null;
		this._cc = null;
		this._bcc = null;
		this._attachmentURL = null;
	}

	// Getters
	public InternetAddress getFrom() {
		return this._from;
	}

	public InternetAddress[] getTo() {
		return this._to;
	}

	public InternetAddress[] getCc() {
		return this._cc;
	}

	public InternetAddress[] getBcc() {
		return this._bcc;
	}

	public Object getContent() {
		return this._content;
	}

	public String getSubject() {
		return this._subject;
	}

	public String[] getAttachmentURL() {
		return this._attachmentURL;
	}

	public boolean isHtml() {
		return this._html;
	}

	public Date getSendDate() {
		return this._sendDate;
	}

	// Setters
	public void setFrom(final InternetAddress from) {
		this._from = from;
	}

	public void setFrom(final String from) throws AddressException {
		this._from = new InternetAddress(from);
	}

	public void setTo(final InternetAddress[] to) {
		this._to = to;
	}

	public void setTo(final InternetAddress to) {
		this._to = this.getAddress(to);
	}

	public void setTo(final String[] to) throws AddressException {
		this._to = this.getAddress(to);
	}

	public void setTo(final String to) throws AddressException {
		this._to = this.getAddress(to);
	}

	public void setCc(final InternetAddress[] cc) {
		this._cc = cc;
	}

	public void setCc(final InternetAddress cc) {
		this._cc = this.getAddress(cc);
	}

	public void setCc(final String[] cc) throws AddressException {
		this._cc = this.getAddress(cc);
	}

	public void setCc(final String cc) throws AddressException {
		this._cc = this.getAddress(cc);
	}

	public void setBcc(final InternetAddress[] bcc) {
		this._bcc = bcc;
	}

	public void setBcc(final InternetAddress bcc) {
		this._bcc = this.getAddress(bcc);
	}

	public void setBcc(final String[] bcc) throws AddressException {
		this._bcc = this.getAddress(bcc);
	}

	public void setBcc(final String bcc) throws AddressException {
		this._bcc = this.getAddress(bcc);
	}

	public void setSubject(final String subject) {
		this._subject = subject;
	}

	public void setContent(final String content, final boolean html) {
		this._content = content;
		this._html = html;
	}

	public void setAttachmentURL(final String[] attachmentURL) {
		this._attachmentURL = attachmentURL;
	}

	public void setAttachmentURL(final String attachmentURL) {
		this._attachmentURL = new String[] { attachmentURL };
	}

}