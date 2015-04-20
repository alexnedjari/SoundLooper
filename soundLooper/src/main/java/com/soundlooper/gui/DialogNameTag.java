package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import com.soundlooper.model.tag.Tag;

public class DialogNameTag extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Tag tag;
	
	private boolean validatedWindow = false;
	
	public DialogNameTag(Window owner, Tag tag) {
		super(owner);
		setModal(true);
		setTitle("Nom du tag");
		this.tag = tag;
		this.setPreferredSize(new Dimension(200, 90));
		this.add(getjTextFieldName(), BorderLayout.NORTH);
		this.add(getJbuttonValidate(), BorderLayout.SOUTH);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private JTextField jTextFieldName;
	
	private JButton jButtonValidate;

	private JButton getJbuttonValidate() {
		if (jButtonValidate == null) {
			jButtonValidate = new JButton("Valider");
			jButtonValidate.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					validateDialog();
				}

				
			});
		}
		return jButtonValidate;
	}
	
	protected void validateDialog() {
		tag.setName(getjTextFieldName().getText());
		validatedWindow = true;
		DialogNameTag.this.dispose();
	}
	
	private JTextField getjTextFieldName() {
		if (jTextFieldName == null) {
			jTextFieldName = new JTextField();
			jTextFieldName.setText(tag.getName());
			jTextFieldName.selectAll();
			jTextFieldName.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					super.keyPressed(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						validateDialog();
					}
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						DialogNameTag.this.dispose();
					}
				}
			});
		}
		return jTextFieldName;
	}

	public boolean isValidatedWindow() {
		return validatedWindow;
	}
	
	
	
}
