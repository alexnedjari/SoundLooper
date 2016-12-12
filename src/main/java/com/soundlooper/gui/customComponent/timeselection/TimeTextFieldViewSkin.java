package com.soundlooper.gui.customComponent.timeselection;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TimeTextFieldViewSkin extends SkinBase<TimeTextFieldView> {

	private static final int MILLISECOND_SECOND = 1000;
	private static final int MILLISECOND_MINUT = 60000;

	public static final int DIGIT_WIDTH = 8;

	private static final int WIDTH = 85;
	private static final int HEIGHT = 23;

	private static final int DIGIT_OFFSET = (WIDTH - (DIGIT_WIDTH * 9)) / 2;
	AnchorPane gridPane = new AnchorPane();

	List<TimeDigitView> listDigit = new ArrayList<>();

	TimeDigitView digitMinutDecade;
	TimeDigitView digitMinutUnit;

	TimeDigitView digitSecondDecade;
	TimeDigitView digitSecondUnit;

	TimeDigitView digitMillisecondHundred;
	TimeDigitView digitMillisecondDecade;
	TimeDigitView digitMillisecondUnit;

	protected TimeTextFieldViewSkin(TimeTextFieldView control) {
		super(control);

		digitMinutDecade = new TimeDigitView(control);
		digitMinutUnit = new TimeDigitView(control);

		digitSecondDecade = new TimeDigitView(control);
		digitSecondUnit = new TimeDigitView(control);

		digitMillisecondHundred = new TimeDigitView(control);
		digitMillisecondDecade = new TimeDigitView(control);
		digitMillisecondUnit = new TimeDigitView(control);

		listDigit.add(digitMinutDecade);
		listDigit.add(digitMinutUnit);
		listDigit.add(digitSecondDecade);
		listDigit.add(digitSecondUnit);
		listDigit.add(digitMillisecondHundred);
		listDigit.add(digitMillisecondDecade);
		listDigit.add(digitMillisecondUnit);

		Label labelSeparator1 = new Label(" :");
		// labelSeparator1.setBackground(new Background(new
		// BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		labelSeparator1.setTextAlignment(TextAlignment.CENTER);
		labelSeparator1.resize(DIGIT_WIDTH, 20);

		Label labelSeparator2 = new Label(" :");
		labelSeparator2.resize(DIGIT_WIDTH, 20);

		// flowPane.setBackground(new Background(new BackgroundFill(Color.BLUE,
		// CornerRadii.EMPTY, Insets.EMPTY)));
		gridPane.resize(WIDTH, HEIGHT);

		// gridPane.setPadding(new Insets(0, 5, 0, 5));
		gridPane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(0),
				new BorderWidths(1), new Insets(0))));
		// gridPane.setBackground(new Background(new
		// BackgroundFill(Color.TRANSPARENT, new CornerRadii(0), new
		// Insets(0))));
		gridPane.getStyleClass().add("white");

		gridPane.getChildren().add(digitMinutDecade);
		digitMinutDecade.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 0, 1);

		gridPane.getChildren().add(digitMinutUnit);
		digitMinutUnit.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 1, 1);

		gridPane.getChildren().add(labelSeparator1);
		labelSeparator1.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 2, 1);

		gridPane.getChildren().add(digitSecondDecade);
		digitSecondDecade.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 3, 1);

		gridPane.getChildren().add(digitSecondUnit);
		digitSecondUnit.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 4, 1);

		gridPane.getChildren().add(labelSeparator2);
		labelSeparator2.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 5, 1);

		gridPane.getChildren().add(digitMillisecondHundred);
		digitMillisecondHundred.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 6, 1);

		gridPane.getChildren().add(digitMillisecondDecade);
		digitMillisecondDecade.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 7, 1);

		gridPane.getChildren().add(digitMillisecondUnit);
		digitMillisecondUnit.relocate(DIGIT_OFFSET + DIGIT_WIDTH * 8, 1);

		InnerShadow shadow = new InnerShadow();
		shadow.setRadius(5);
		gridPane.setEffect(shadow);

		// GridPane.setConstraints(digitMinutDecade, 0, 0);
		// GridPane.setConstraints(digitMinutUnit, 1, 0);
		// GridPane.setConstraints(labelSeparator1, 2, 0);
		//
		// GridPane.setConstraints(digitSecondDecade, 3, 0);
		// GridPane.setConstraints(digitSecondUnit, 4, 0);
		// GridPane.setConstraints(labelSeparator2, 5, 0);
		//
		// GridPane.setConstraints(digitMillisecondHundred, 6, 0);
		// GridPane.setConstraints(digitMillisecondDecade, 7, 0);
		// GridPane.setConstraints(digitMillisecondUnit, 8, 0);
		getChildren().add(gridPane);

		getSkinnable().timeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
				if (newValue != oldValue) {
					getSkinnable().forceLayout();
				}
			}
		});

		getSkinnable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					select(null);
				} else {
					select(listDigit.get(0));
				}
				getSkinnable().forceLayout();
			}
		});

		getSkinnable().addEventHandler(KeyEvent.ANY, e -> {
			if (e.getCode() == KeyCode.LEFT) {
				TimeDigitView selectedDigit = getSelectedDigit();
				if (selectedDigit != null && selectedDigit != listDigit.get(0)) {
					if (e.getEventType() == KeyEvent.KEY_PRESSED) {
						select(listDigit.get(listDigit.indexOf(selectedDigit) - 1));
					}
				}
				e.consume();
			} else if (e.getCode() == KeyCode.RIGHT) {
				TimeDigitView selectedDigit = getSelectedDigit();
				if (selectedDigit != null && selectedDigit != listDigit.get(listDigit.size() - 1)) {
					if (e.getEventType() == KeyEvent.KEY_PRESSED) {
						select(listDigit.get(listDigit.indexOf(selectedDigit) + 1));
					}
				}
				e.consume();
			} else if (e.getCode() == KeyCode.UP && !e.isControlDown()) {
				TimeDigitView selectedDigit = getSelectedDigit();
				if (selectedDigit != null) {
					if (e.getEventType() == KeyEvent.KEY_PRESSED) {
						int weight = getDigitMillisecondWeight(selectedDigit);
						getSkinnable().setNewTime(getSkinnable().getTime() + weight);
					}
				}
				e.consume();
			} else if (e.getCode() == KeyCode.DOWN && !e.isControlDown()) {
				TimeDigitView selectedDigit = getSelectedDigit();
				if (selectedDigit != null) {
					if (e.getEventType() == KeyEvent.KEY_PRESSED) {
						int weight = getDigitMillisecondWeight(selectedDigit);
						getSkinnable().setNewTime(getSkinnable().getTime() - weight);
					}
				}
				e.consume();
			}
		});
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return HEIGHT;
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return HEIGHT;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return WIDTH;
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return WIDTH;
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return HEIGHT;
	}

	@Override
	protected double computePrefWidth(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return WIDTH;
	}

	private int getDigitMillisecondWeight(TimeDigitView timeDigitView) {
		if (timeDigitView == digitMillisecondUnit) {
			return 1;
		}
		if (timeDigitView == digitMillisecondDecade) {
			return 10;
		}
		if (timeDigitView == digitMillisecondHundred) {
			return 100;
		}
		if (timeDigitView == digitSecondUnit) {
			return MILLISECOND_SECOND;
		}
		if (timeDigitView == digitSecondDecade) {
			return MILLISECOND_SECOND * 10;
		}
		if (timeDigitView == digitMinutUnit) {
			return MILLISECOND_MINUT;
		}
		if (timeDigitView == digitMinutDecade) {
			return MILLISECOND_MINUT * 10;
		}
		return 0;
	}

	private void applyTime() {
		int time = getSkinnable().getTime();
		int minut = time / MILLISECOND_MINUT;
		int minutInMilli = minut * MILLISECOND_MINUT;

		int second = (time - minutInMilli) / MILLISECOND_SECOND;
		int secondInMilli = second * MILLISECOND_SECOND;

		int millisecond = time - minutInMilli - secondInMilli;

		int minutDecade = minut / 10;
		int minutUnit = minut % 10;

		int secondDecade = second / 10;
		int secondUnit = second % 10;

		int millisecondHundred = millisecond / 100;
		int millisecondDecade = (millisecond % 100) / 10;
		int millisecondUnit = millisecond - (millisecondHundred * 100) - (millisecondDecade * 10);

		digitMinutDecade.setDigit(minutDecade);
		digitMinutUnit.setDigit(minutUnit);
		digitSecondDecade.setDigit(secondDecade);
		digitSecondUnit.setDigit(secondUnit);
		digitMillisecondHundred.setDigit(millisecondHundred);
		digitMillisecondDecade.setDigit(millisecondDecade);
		digitMillisecondUnit.setDigit(millisecondUnit);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		// if (getSkinnable().isFocused()) {
		// gridPane.setBackground(new Background(new BackgroundFill(Color.WHITE,
		// CornerRadii.EMPTY, Insets.EMPTY)));
		// } else {
		// gridPane.setBackground(new Background(new
		// BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		// }
		applyTime();
	}

	public void select(TimeDigitView control) {
		for (TimeDigitView timeDigitView : listDigit) {
			if (timeDigitView.isSelected()) {
				timeDigitView.setSelected(false);
				timeDigitView.forceLayout();
			}
		}
		if (control != null) {
			control.setSelected(true);
			control.forceLayout();
		}

	}

	public TimeDigitView getSelectedDigit() {
		for (TimeDigitView timeDigitView : listDigit) {
			if (timeDigitView.isSelected()) {
				return timeDigitView;
			}
		}
		return null;
	}

}
