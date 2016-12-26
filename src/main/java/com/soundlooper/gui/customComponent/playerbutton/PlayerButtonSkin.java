package com.soundlooper.gui.customComponent.playerbutton;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;

import com.soundlooper.system.SoundLooperColor;
import com.soundlooper.system.SoundLooperLigthing;

public class PlayerButtonSkin extends SkinBase<PlayerButton> {

	private Ellipse ellipse;
	private boolean keyDown;

	private static final int RADIUS = 16;

	protected PlayerButtonSkin(PlayerButton control) {
		super(control);

		control.setPadding(Insets.EMPTY);

		ellipse = new Ellipse(RADIUS, RADIUS);
		ellipse.setFill(SoundLooperColor.DARK_GRAY);
		getChildren().add(ellipse);

		DropShadow shadow = new DropShadow();
		shadow.setRadius(2);
		ellipse.setEffect(shadow);

		control.setEffect(SoundLooperLigthing.getPotentiometerLighting());

		if (control.getGraphic() instanceof ImageView) {
			ImageView graphic = (ImageView) control.getGraphic();
			getChildren().add(graphic);
			InnerShadow innerShadow = new InnerShadow();
			innerShadow.setRadius(1);
			graphic.setEffect(innerShadow);
			graphic.setMouseTransparent(true);
		}

		ellipse.setOnMouseEntered(e -> {
			control.setEffect(SoundLooperLigthing.getPotentiometerLightingOver());
		});
		ellipse.setOnMouseExited(e -> {
			control.setEffect(SoundLooperLigthing.getPotentiometerLighting());
		});

		ellipse.setOnMouseClicked(e -> {
			keyReleased();
		});

		ellipse.setOnMousePressed(e -> {
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
		ellipse.resize(contentWidth, contentHeight);
		ellipse.relocate(contentX, contentY);
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