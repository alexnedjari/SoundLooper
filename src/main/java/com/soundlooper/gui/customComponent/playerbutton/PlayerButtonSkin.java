package com.soundlooper.gui.customComponent.playerbutton;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;

import com.soundlooper.system.SoundLooperLigthing;

public class PlayerButtonSkin extends SkinBase<PlayerButton> {

	private boolean keyDown;

	private static final int RADIUS = 16;

	protected PlayerButtonSkin(PlayerButton control) {
		super(control);

		control.setPadding(Insets.EMPTY);

		if (control.getGraphic() instanceof ImageView) {
			ImageView graphic = (ImageView) control.getGraphic();
			getChildren().add(graphic);

			graphic.setMouseTransparent(true);
		}

		this.getSkinnable().setOnMouseEntered(e -> {
			control.setEffect(SoundLooperLigthing.getPotentiometerLightingOver());
		});
		this.getSkinnable().setOnMouseExited(e -> {
			control.setEffect(null);
		});

		this.getSkinnable().setOnMouseClicked(e -> {
			keyReleased();
		});

		this.getSkinnable().setOnMousePressed(e -> {
			keyPressed();
		});
	}

	private void keyPressed() {
		Button button = getSkinnable();
		if (!button.isArmed()) {
			keyDown = true;
			button.arm();
		}
	}

	/**
	 * Invoked when a valid keystroke release occurs which causes the button to
	 * fire if it was armed by a keyPress.
	 */
	private void keyReleased() {
		final ButtonBase button = getSkinnable();
		if (keyDown) {
			keyDown = false;
			if (button.isArmed()) {
				button.disarm();
				button.fire();
			}
		}
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

	@Override
	protected double computeMaxWidth(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

	@Override
	protected double computeMinWidth(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

	@Override
	protected double computePrefWidth(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return RADIUS * 2;
	}

}