package com.soundlooper.exception;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.mail.internet.InternetAddress;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import com.soundlooper.gui.WindowWaiting;
import com.soundlooper.system.mail.MailMessage;
import com.soundlooper.system.mail.MailSender;
import com.soundlooper.system.util.StackTracer;

/**
 *
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
 * @since  28 août 2014
 *-------------------------------------------------------
 */
public class WindowsException extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null; // @jve:decl-index=0:visual-constraint="10,10"

	private JTextArea jTextAreaAvertissment = null;

	private Throwable e;

	private JPanel jPanelNavigation = null;

	private JButton jButtonFermer = null;

	private JButton jButtonRapport = null;

	private JButton jButtonDetail = null;

	private JTextArea jTextAreaStack = null;

	private JTextArea jTextAreaConsole = null;

	private JPanel jPanelBas = null;

	private JScrollPane jScrollPaneStack = null;

	private JScrollPane jScrollPaneConsole = null;

	protected boolean enroule = true;

	protected Dimension dimensionEnroule = new Dimension(600, 120);
	protected Dimension dimensionDeroule = new Dimension(600, 400);
	protected String libelleEnroule = new String("Détails >>");
	protected String libelleDeroule = new String("Détails <<");

	protected String titreApplication;

	protected String[] attachmentsURL;

	protected ImageIcon icon = null;

	// @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public WindowsException(Throwable e, String titreApplication, String[] attachmentsURL) {
		super();
		this.e = e;
		this.titreApplication = titreApplication;
		this.attachmentsURL = attachmentsURL;
		this.setModal(true);
		this.initialize();

	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(this.getImageIcon().getImage());
		this.setSize(this.dimensionEnroule);
		this.setMinimumSize(this.dimensionEnroule);
		this.setContentPane(this.getJContentPane());
		this.setTitle("Une exception n'a pas été récupérée");
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e2) {
				//TODO refaire en moins bourrin
				//(passer en paramètre une action à exécuter)
				System.exit(0);
			}
		});
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 */
	protected ImageIcon getImageIcon() {
		if (this.icon == null) {
			this.icon = new ImageIcon("redcross-16.png");
		}
		return this.icon;
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new BorderLayout());
			this.jContentPane.setSize(new Dimension(234, 202));
			this.jContentPane.setBackground(Color.white);
			this.jContentPane.add(this.getJTextAreaAvertissment(), BorderLayout.NORTH);
			this.jContentPane.add(this.getJPanelBas(), BorderLayout.CENTER);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jTextAreaAvertissment
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextAreaAvertissment() {
		if (this.jTextAreaAvertissment == null) {
			this.jTextAreaAvertissment = new JTextArea();
			this.jTextAreaAvertissment.setPreferredSize(new Dimension(0, 48));
			this.jTextAreaAvertissment.setEditable(false);
			this.jTextAreaAvertissment.setRows(3);
			this.jTextAreaAvertissment
					.setText("Une erreur a eu lieu... \nSi vous possédez une connexion internet, vous pouvez cliquer sur le bouton \"Envoyer le rapport d'erreur\" \nafin que nous puissions corriger le problème");
			this.jTextAreaAvertissment.setFont(new Font("Dialog", Font.PLAIN, 12));
			this.jTextAreaAvertissment.setLineWrap(false);
		}
		return this.jTextAreaAvertissment;
	}

	/**
	 * This method initializes jPanelNavigation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelNavigation() {
		if (this.jPanelNavigation == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			this.jPanelNavigation = new JPanel();
			this.jPanelNavigation.setBackground(Color.white);
			this.jPanelNavigation.setLayout(flowLayout);
			this.jPanelNavigation.setComponentOrientation(ComponentOrientation.UNKNOWN);
			this.jPanelNavigation.add(this.getJButtonRapport(), null);
			this.jPanelNavigation.add(this.getJButtonDetail(), null);
			this.jPanelNavigation.add(this.getJButtonFermer(), null);
		}
		return this.jPanelNavigation;
	}

	/**
	 * This method initializes jButtonFermer
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonFermer() {
		if (this.jButtonFermer == null) {
			this.jButtonFermer = new JButton();
			this.jButtonFermer.setText("Fermer");
			this.jButtonFermer.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e2) {
					WindowsException.this.dispose();
					//TODO refaire en moins oburrin
					System.exit(0);
				}
			});
		}
		return this.jButtonFermer;
	}

	/**
	 * This method initializes jButtonRapport
	 *
	 * @return javax.swing.JButton
	 */
	protected JButton getJButtonRapport() {
		if (this.jButtonRapport == null) {
			this.jButtonRapport = new JButton();
			this.jButtonRapport.setText("Envoyer le rapport d'erreur");
			this.jButtonRapport.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e2) {
					WindowsException.this.getJButtonRapport().setEnabled(false);
					new SwingWorker<Object, Object>() {
						@Override
						protected Object doInBackground() throws Exception {
							WindowWaiting windowWaiting = new WindowWaiting("Envoi du rapport en cours,\n veuillez patienter", WindowsException.this.getImageIcon().getImage());
							try {
								windowWaiting.setVisible(true);
								final MailMessage msg = new MailMessage();
								final MailSender mail2 = new MailSender("smtp.free.fr");
								msg.setFrom(new InternetAddress("alex.nedjari@gmail.com", "Sound Looper"));
								msg.setTo("alex.nedjari@gmail.com");
								msg.setSubject("Exception dans le programme \"" + WindowsException.this.titreApplication + "\"");
								msg.setContent("L'exception suivante a été levée dans le programme : \n\n" + WindowsException.this.getJTextAreaStack().getText(), false);

								java.util.List<String> listeAttachement = new ArrayList<String>();
								for (String filePath : WindowsException.this.attachmentsURL) {
									if (new File(filePath).exists()) {
										listeAttachement.add(filePath);
									}
								}

								msg.setAttachmentURL(listeAttachement.toArray(new String[listeAttachement.size()]));
								mail2.sendMessage(msg);
								windowWaiting.dispose();
								JOptionPane.showMessageDialog(WindowsException.this, "Le message a bien été envoyé", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception e3) {
								e3.printStackTrace();
								windowWaiting.dispose();
								JOptionPane.showMessageDialog(WindowsException.this, "Le message n'a pas pu être envoyé : \n\n" + StackTracer.getStackTrace(e3), "Erreur",
										JOptionPane.ERROR_MESSAGE);
							}
							return null;
						}

						@Override
						protected void done() {
							WindowsException.this.getJButtonRapport().setEnabled(true);
						}
					}.execute();

				}
			});
		}
		return this.jButtonRapport;
	}

	/**
	 * This method initializes jButtonDetail
	 *
	 * @return javax.swing.JButton
	 */
	protected JButton getJButtonDetail() {
		if (this.jButtonDetail == null) {
			this.jButtonDetail = new JButton();
			this.jButtonDetail.setText(this.libelleEnroule);
			this.jButtonDetail.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e2) {
					WindowsException.this.enroule = !WindowsException.this.enroule;
					if (WindowsException.this.enroule) {
						WindowsException.this.setSize(WindowsException.this.dimensionEnroule);
						WindowsException.this.getJButtonDetail().setText(WindowsException.this.libelleEnroule);
					} else {
						WindowsException.this.setSize(WindowsException.this.dimensionDeroule);
						WindowsException.this.getJButtonDetail().setText(WindowsException.this.libelleDeroule);
					}
				}
			});
		}
		return this.jButtonDetail;
	}

	/**
	 * This method initializes jTextAreaStack
	 *
	 * @return javax.swing.JTextArea
	 */
	protected JTextArea getJTextAreaStack() {
		if (this.jTextAreaStack == null) {

			String chaine = new String(this.e.toString());
			for (int i = 0; i < this.e.getStackTrace().length; i++) {
				chaine += "\n    " + this.e.getStackTrace()[i].toString();
			}

			this.jTextAreaStack = new JTextArea(chaine);
			this.jTextAreaStack.setEditable(false);
		}
		return this.jTextAreaStack;
	}

	/**
	 * This method initializes jTextAreaConsole
	 *
	 * @return javax.swing.JTextArea
	 */
	protected JTextArea getJTextAreaConsole() {
		if (this.jTextAreaConsole == null) {
			this.jTextAreaConsole = new JTextArea("Les fichiers suivants vont être envoyés :\n");
			for (String filePath : this.attachmentsURL) {
				this.jTextAreaConsole.append("\t- " + filePath + "\n");
			}
			this.jTextAreaConsole.setEditable(false);
			this.jTextAreaConsole.setPreferredSize(new Dimension(0, 100));
		}
		return this.jTextAreaConsole;
	}

	/**
	 * This method initializes jPanelBas
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelBas() {
		if (this.jPanelBas == null) {
			this.jPanelBas = new JPanel();
			this.jPanelBas.setLayout(new BorderLayout());
			this.jPanelBas.setBackground(Color.white);
			this.jPanelBas.add(this.getJPanelNavigation(), BorderLayout.NORTH);
			this.jPanelBas.add(this.getJScrollPaneStack(), BorderLayout.CENTER);
			this.jPanelBas.add(this.getJScrollPaneConsole(), BorderLayout.SOUTH);
		}
		return this.jPanelBas;
	}

	/**
	 * This method initializes jScrollPaneStack
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneStack() {
		if (this.jScrollPaneStack == null) {
			this.jScrollPaneStack = new JScrollPane();
			this.jScrollPaneStack.setViewportView(this.getJTextAreaStack());
		}
		return this.jScrollPaneStack;
	}

	/**
	 * This method initializes jScrollPaneConsole
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPaneConsole() {
		if (this.jScrollPaneConsole == null) {
			this.jScrollPaneConsole = new JScrollPane();
			this.jScrollPaneConsole.setViewportView(this.getJTextAreaConsole());
		}
		return this.jScrollPaneConsole;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
