package com.soundlooper.gui.customComponent.timeselection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TimeTextFieldViewSkin extends SkinBase<TimeTextFieldView> {

	private static final int MILLISECOND_SECOND = 1000;
	private static final int MILLISECOND_MINUT = 60000;

	FlowPane flowPane = new FlowPane();

	TimeDigitView digitMinutDecade = new TimeDigitView();
	TimeDigitView digitMinutUnit = new TimeDigitView();

	TimeDigitView digitSecondDecade = new TimeDigitView();
	TimeDigitView digitSecondUnit = new TimeDigitView();

	TimeDigitView digitMillisecondHundred = new TimeDigitView();
	TimeDigitView digitMillisecondDecade = new TimeDigitView();
	TimeDigitView digitMillisecondUnit = new TimeDigitView();

	protected TimeTextFieldViewSkin(TimeTextFieldView control) {
		super(control);

		Label labelSeparator1 = new Label(" :");
		// labelSeparator1.setBackground(new Background(new
		// BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		labelSeparator1.setTextAlignment(TextAlignment.CENTER);
		labelSeparator1.resize(10, 20);

		Label labelSeparator2 = new Label(" :");
		labelSeparator2.resize(10, 20);

		flowPane.resize(73, 23);
		flowPane.setPadding(new Insets(0, 5, 0, 5));
		flowPane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5),
				new BorderWidths(1), new Insets(0))));

		flowPane.getChildren().add(digitMinutDecade);
		flowPane.getChildren().add(digitMinutUnit);
		flowPane.getChildren().add(labelSeparator1);

		flowPane.getChildren().add(digitSecondDecade);
		flowPane.getChildren().add(digitSecondUnit);
		flowPane.getChildren().add(labelSeparator2);

		flowPane.getChildren().add(digitMillisecondHundred);
		flowPane.getChildren().add(digitMillisecondDecade);
		flowPane.getChildren().add(digitMillisecondUnit);
		getChildren().add(flowPane);

		getSkinnable().timeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
				if (newValue != oldValue) {
					applyTime();
				}
			}
		});
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

		digitMinutDecade.setDigit(minutDecade);
		digitMinutDecade.setDigit(minutDecade);
		digitMinutDecade.setDigit(minutDecade);
		digitMinutDecade.setDigit(minutDecade);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
	}

}
