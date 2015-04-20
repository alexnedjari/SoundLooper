package com.soundlooper.gui;

import java.util.Arrays;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class UpperCaseDocument extends PlainDocument {
	public boolean numericOnly;
	public int maxLength;
	public List<Character> numericChars = Arrays.asList(new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
