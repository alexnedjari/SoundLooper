package com.soundlooper.gui.customComponent.timeselection;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TimeTextFieldViewSkin extends SkinBase<TimeTextFieldView> {

	private static final int MILLISECOND_SECOND = 1000;
	private static final int MILLISECOND_MINUT = 60000;

	GridPane flowPane = new GridPane();

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
		labelSeparator1.resize(10, 20);

		Label labelSeparator2 = new Label(" :");
		labelSeparator2.resize(10, 20);

		// flowPane.setBackground(new Background(new BackgroundFill(Color.BLUE,
		// CornerRadii.EMPTY, Insets.EMPTY)));
		flowPane.resize(76, 23);
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

		GridPane.setConstraints(digitMinutDecade, 0, 0);
		GridPane.setConstraints(digitMinutUnit, 1, 0);
		GridPane.setConstraints(labelSeparator1, 2, 0);

		GridPane.setConstraints(digitSecondDecade, 3, 0);
		GridPane.setConstraints(digitSecondUnit, 4, 0);
		GridPane.setConstraints(labelSeparator2, 5, 0);

		GridPane.setConstraints(digitMillisecondHundred, 6, 0);
		GridPane.setConstraints(digitMillisecondDecade, 7, 0);
		GridPane.setConstraints(digitMillisecondUnit, 8, 0);
		getChildren().add(flowPane);

		getSkinnable().timeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> obs, Number oldValue, Number newValue) {
				if (newValue != oldValue) {
					applyTime();
				}
			}
		});

		getSkinnable().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					select(null);
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

}
