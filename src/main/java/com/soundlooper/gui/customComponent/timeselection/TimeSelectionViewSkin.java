package com.soundlooper.gui.customComponent.timeselection;

import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class TimeSelectionViewSkin extends SkinBase<TimeSelectionView> {

	private static final int MARGIN_LEFT = 30;
	private static final int MARGIN_RIGHT = MARGIN_LEFT;
	private static final int MARGIN_TOP = 5;

	BorderPane borderPane = new BorderPane();
	TextField textFieldBegin = new TextField("Begin");
	TextField textFieldEnd = new TextField("End");

	protected TimeSelectionViewSkin(TimeSelectionView control) {
		super(control);

		borderPane.setLeft(textFieldBegin);
		borderPane.setRight(textFieldEnd);

		this.getChildren().add(borderPane);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		borderPane.resize(contentWidth, 35);
		borderPane.setPadding(new Insets(MARGIN_TOP, MARGIN_RIGHT, 0, MARGIN_LEFT));
	}
}
