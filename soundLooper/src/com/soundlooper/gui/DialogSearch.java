/**
 *
 */
package com.soundlooper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.soundlooper.system.search.SearchEngine;
import com.soundlooper.system.search.SearchListener;
import com.soundlooper.system.search.Searchable;
import com.soundlooper.system.search.StringTransformerAccentuation;
import com.soundlooper.system.search.StringTransformerNoCase;

/**
 * ====================================================================
 *
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
 * @author Alexandre NEDJARI
 * @since 10 mars 2011
 *
 * ====================================================================
 */
public class DialogSearch extends JDialog implements SearchListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected List<? extends Searchable> listeSearchable;
	private Vector<Searchable> resultForList = new Vector<Searchable>();
	protected Searchable resultat = null;
	JList jList = null;
	JTextField jTextField = null;
	SearchEngine search;
	JScrollPane scrollPanelList = null;
	JPanel panelList = null;
	private JPanel panelTextField;

	private boolean accolades;
	private int minimalTextSize;

	/**
	 * Constructor
	 * @param songs
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public DialogSearch(JFrame parent, List<? extends Searchable> listeSearchable, int minimalTextSize, String title) {
		super(parent, true);
		this.listeSearchable = listeSearchable;
		this.minimalTextSize = minimalTextSize;
		this.setTitle(title);

		this.search = new SearchEngine(this.listeSearchable);

		this.search.addSearchListener(this);

		//this.search.addTransformer(new StringTransformerTrim());
		//this.search.addTransformer(new StringTransformerDeleteGenericWords());
		//this.search.addTransformer(new StringTransformerDeletePonctuation());
		this.search.addTransformer(new StringTransformerAccentuation());
		this.search.addTransformer(new StringTransformerNoCase());

		//this.setContentPane(this.getContentPane());

		this.getContentPane().setBackground(Color.WHITE);
		this.getContentPane().add(this.getPanelTextField(), BorderLayout.NORTH);

		this.getContentPane().add(this.getScrolledPanelList(), BorderLayout.WEST);
		this.setPreferredSize(new Dimension(640, 480));
		this.pack();
		this.setLocationRelativeTo(null);
		this.performSearch();
	}

	public void setFullScreen() {
		this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.pack();
		this.setLocation(0, 0);
	}

	private JPanel getPanelTextField() {
		if (this.panelTextField == null) {
			this.panelTextField = new JPanel();
			this.panelTextField.setLayout(new BorderLayout());
			this.panelTextField.add(this.getTextField(), BorderLayout.CENTER);
		}
		return this.panelTextField;
	}

	private JScrollPane getScrolledPanelList() {
		if (this.scrollPanelList == null) {
			this.scrollPanelList = new JScrollPane();
			this.scrollPanelList.setViewportView(this.getPanelList());
			this.scrollPanelList.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
			this.scrollPanelList.setBackground(Color.WHITE);

		}
		return this.scrollPanelList;
	}

	public Searchable getResultat() {
		return this.resultat;
	}

	/**
	 * @return
	 */
	private Component getPanelList() {
		if (this.panelList == null) {
			this.panelList = new JPanel();
			this.panelList.setLayout(new BorderLayout());
			this.panelList.add(this.getList(), BorderLayout.CENTER);
		}
		return this.panelList;
	}

	private JList getList() {
		if (this.jList == null) {
			this.jList = new JList(this.resultForList);
			this.jList.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getModifiers() == InputEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_ENTER) {
						DialogSearch.this.validerResultatAvecAccolades();
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER && DialogSearch.this.getList().getSelectedIndex() != -1) {
						DialogSearch.this.validerSearch();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						DialogSearch.this.annulerSearch();
					}
				}

			});
			this.jList.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() > 1) {
						DialogSearch.this.validerSearch();
					}
				}
			});
		}
		return this.jList;
	}

	private JTextField getTextField() {
		if (this.jTextField == null) {
			this.jTextField = new JTextField();
			this.jTextField.getDocument().addDocumentListener(new DocumentListener() {
				//String oldText = FrameSearch.this.jTextField.getText();

				@Override
				public void removeUpdate(DocumentEvent documentevent) {
					System.out.println("RemoveUpdate");
					DialogSearch.this.performSearch();
					//this.oldText = FrameSearch.this.jTextField.getText();
				}

				@Override
				public void insertUpdate(DocumentEvent documentevent) {
					//TODO essayer de faire un swing worker
					System.out.println("InserUpdate");

					//						if (newText.contains(this.oldText) && (this.oldText.length() > 0)) {
					//							try {
					//								System.out.println("REFINE");
					//								//exécuter dans le thread (faire un needRefine)
					//								FrameSearch.this.search.askToRefineSearch(newText);
					//							} catch (SearchException e) {
					//								e.printStackTrace();
					//								FrameSearch.this.performSearch();
					//							}
					//						} else {
					DialogSearch.this.performSearch();
					//}
					//	this.oldText = newText;
					//}
				}

				@Override
				public void changedUpdate(DocumentEvent documentevent) {
					//System.out.println("ChangedUpdate");
					DialogSearch.this.performSearch();
					//this.oldText = FrameSearch.this.jTextField.getText();
				}
			});

			this.jTextField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {

					if (e.getModifiers() == InputEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_ENTER) {
						DialogSearch.this.validerResultatAvecAccolades();
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER && DialogSearch.this.getList().getModel().getSize() == 1) {
						// il n'y a qu'un seul élement dans la liste, on le sélectionne
						DialogSearch.this.getList().setSelectedIndex(0);
						DialogSearch.this.validerSearch();
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN && DialogSearch.this.getList().getModel().getSize() > 0) {
						DialogSearch.this.getList().requestFocus();
						if (DialogSearch.this.getList().getSelectedIndex() == -1) {
							DialogSearch.this.getList().setSelectedIndex(0);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						DialogSearch.this.annulerSearch();
					}

				}
			});

		}
		return this.jTextField;
	}

	protected void validerResultatAvecAccolades() {
		this.accolades = true;
		this.resultat = (Searchable) this.getList().getSelectedValue();
		this.dispose();
	}

	protected void validerSearch() {
		this.accolades = false;
		this.resultat = (Searchable) this.getList().getSelectedValue();
		this.dispose();
	}

	public boolean isAccolades() {
		return this.accolades;
	}

	protected void annulerSearch() {
		this.dispose();
	}

	protected void performSearch() {
		String newText = DialogSearch.this.jTextField.getText();
		if (newText.length() >= DialogSearch.this.minimalTextSize) {
			this.search.cancelSearch();
			this.search.performSearch(newText);
		}
	}

	@Override
	public void onFullResultAvailable(String searchState, ArrayList<Searchable> result, long millisecondTime) {
		System.out.println("Full result available in " + millisecondTime + "ms (" + result.size() + ") results");
		this.resultForList.clear();
		this.resultForList.addAll(result);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DialogSearch.this.jList.updateUI();
			}
		});
	}
}
