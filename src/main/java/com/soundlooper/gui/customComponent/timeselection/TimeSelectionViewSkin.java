package com.soundlooper.gui.customComponent.timeselection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;

public class TimeSelectionViewSkin extends SkinBase<TimeSelectionView> {

	private final class TimeStringConverter extends StringConverter<Number> {
		@Override
		public Number fromString(String arg0) {
			try {
				return Integer.valueOf(arg0);
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		@Override
		public String toString(Number arg0) {
			return arg0.toString();
		}
	}

	private static final int MARGIN_LEFT = 30;
	private static final int MARGIN_RIGHT = MARGIN_LEFT;
	private static final int MARGIN_TOP = 5;

	BorderPane borderPane = new BorderPane();

	TimeTextFieldView timeTextFieldBegin = new TimeTextFieldView();
	TimeTextFieldView timeTextFieldEnd = new TimeTextFieldView();

	protected TimeSelectionViewSkin(TimeSelectionView control) {
		super(control);

		borderPane.setLeft(timeTextFieldBegin);
		borderPane.setRight(timeTextFieldEnd);

		this.getChildren().add(borderPane);

		timeTextFieldBegin.timeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				control.forceLayout();
			}
		});
		timeTextFieldEnd.timeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				control.forceLayout();
			}
		});

		initBinding();

	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		borderPane.resize(contentWidth, 35);
		borderPane.setPadding(new Insets(MARGIN_TOP, MARGIN_RIGHT, 0, MARGIN_LEFT));
	}

	public void initBinding() {
		SoundLooperPlayer soundLooperPlayer = getSkinnable().getSoundLooperPlayer();
		Mark currentMark = soundLooperPlayer.getCurrentMark();
		if (currentMark != null) {
			timeTextFieldBegin.timeProperty().bindBidirectional(currentMark.beginMillisecondProperty());
			timeTextFieldEnd.timeProperty().bindBidirectional(currentMark.endMillisecondProperty());
		}

		soundLooperPlayer.markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldMark, Mark newMark) {
				// Unbind old mark
				if (oldMark != null) {
					timeTextFieldBegin.timeProperty().unbindBidirectional(oldMark.beginMillisecondProperty());
					timeTextFieldEnd.timeProperty().unbindBidirectional(oldMark.endMillisecondProperty());
				}

				// bind new mark
				if (newMark != null) {
					timeTextFieldBegin.timeProperty().bindBidirectional(newMark.beginMillisecondProperty());
					timeTextFieldEnd.timeProperty().bindBidirectional(newMark.endMillisecondProperty());
				}

			}
		});

	}
}
