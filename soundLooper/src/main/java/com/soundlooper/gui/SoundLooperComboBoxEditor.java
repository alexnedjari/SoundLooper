/**
 *
 */
package com.soundlooper.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

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
 * @author Alexandre NEDJARI
 * @since  21 nov. 2012
 *-------------------------------------------------------
 */
public class SoundLooperComboBoxEditor extends JTextField implements ComboBoxEditor {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected boolean numericOnly = false;

	protected int maxLength = -1;

	public void setNumericOnly(boolean numericOnly) {
		((UpperCaseDocument) this.getDocument()).setNumericOnly(numericOnly);
	}

	public void setMaxLength(int maxLength) {
		((UpperCaseDocument) this.getDocument()).setMaxLength(maxLength);
	}

	SoundLooperComboBoxEditor() {
		super(new UpperCaseDocument(), null, 0);
	}

	@Override
	public Component getEditorComponent() {
		return this;
	}

	@Override
	public Object getItem() {
		return this.getText();
	}

	@Override
	public void setItem(final Object anObject) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SoundLooperComboBoxEditor.this.setText(anObject != null ? anObject.toString() : "");

			}
		});
	}

	private static class UpperCaseDocument extends PlainDocument {

		protected boolean numericOnly = false;

		protected int maxLength = -1;

		ArrayList<Character> numericChars = new ArrayList<Character>(Arrays.asList(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' }));

		public void setNumericOnly(boolean numericOnly) {
			this.numericOnly = numericOnly;
		}

		public void setMaxLength(int maxLength) {
			this.maxLength = maxLength;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			String inputText = str;
			//gère le maxlenght éventuel
			if (this.maxLength != -1) {
				if (inputText != null && inputText.length() + this.getLength() > this.maxLength) {
					inputText = inputText.substring(0, inputText.length() - (inputText.length() + this.getLength() - this.maxLength));
				}
			}

			if (this.numericOnly) {
				char[] strAsChar = inputText.toCharArray();
				StringBuffer charsToKeep = new StringBuffer();
				for (char c : strAsChar) {
					if (this.numericChars.contains(c)) {
						charsToKeep.append(c);
					}
				}
				inputText = charsToKeep.toString();
			}

			super.insertString(offs, inputText, a);
		}
	}
}
