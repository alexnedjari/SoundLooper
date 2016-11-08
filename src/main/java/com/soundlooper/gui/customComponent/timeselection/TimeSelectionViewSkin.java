package com.soundlooper.gui.customComponent.timeselection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;

import com.soundlooper.exception.PlayerException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.util.MessagingUtil;

public class TimeSelectionViewSkin extends SkinBase<TimeSelectionView> {

	private static final int MARGIN_LEFT = 30;
	private static final int MARGIN_RIGHT = MARGIN_LEFT;
	private static final int MARGIN_TOP = 5;

	BorderPane borderPane = new BorderPane();

	TimeTextFieldView timeTextFieldBegin;
	TimeTextFieldView timeTextFieldEnd;

	protected TimeSelectionViewSkin(TimeSelectionView control) {
		super(control);
		timeTextFieldBegin = new TimeTextFieldView() {
			@Override
			public void setNewTime(int newTime) {
				try {
					SoundLooperPlayer.getInstance().setLoopPointBegin(newTime);
				} catch (PlayerException e) {
					MessagingUtil.displayError("Impossible de déplacer le marqueur de début", e);
				}

			}
		};
		timeTextFieldEnd = new TimeTextFieldView() {
			@Override
			public void setNewTime(int newTime) {
				try {
					SoundLooperPlayer.getInstance().setLoopPointEnd(newTime);
				} catch (PlayerException e) {
					MessagingUtil.displayError("Impossible de déplacer le marqueur de fin", e);
				}
			}
		};

		// borderPane.setBackground(new Background(new BackgroundFill(Color.RED,
		// CornerRadii.EMPTY, Insets.EMPTY)));

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
			timeTextFieldBegin.timeProperty().bind(currentMark.beginMillisecondProperty());
			timeTextFieldEnd.timeProperty().bind(currentMark.endMillisecondProperty());
			getSkinnable().forceLayout();
		}

		soundLooperPlayer.markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldMark, Mark newMark) {
				// Unbind old mark
				if (oldMark != null) {
					timeTextFieldBegin.timeProperty().unbind();
					timeTextFieldEnd.timeProperty().unbind();
				}

				// bind new mark
				if (newMark != null) {
					timeTextFieldBegin.timeProperty().bind(newMark.beginMillisecondProperty());
					timeTextFieldEnd.timeProperty().bind(newMark.endMillisecondProperty());
				}

			}
		});

	}
}
