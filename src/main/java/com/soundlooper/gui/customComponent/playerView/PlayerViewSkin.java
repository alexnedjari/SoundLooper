package com.soundlooper.gui.customComponent.playerView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.audio.player.Player;
import com.soundlooper.audio.player.Player.PlayerState;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.PlayerNotInitializedException;
import com.soundlooper.gui.customComponent.util.ArrowFactory;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.ImageGetter;
import com.soundlooper.system.SoundLooperColor;
import com.soundlooper.system.util.MessagingUtil;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class PlayerViewSkin extends SkinBase<PlayerView> {

	private Logger logger = LogManager.getLogger(this.getClass());

	// -------- CONSTANTS and "statics" dimensions -----------//
	private final static int TOP_MARGIN = 35;
	private final static int BORDER_PADDING = 20;
	private final static int MIN_HANDLE_SPACING = 2;
	private final static int DEFAULT_DURATION = 1000;

	private double handleWidth;
	private double handleHeight;

	// -------- Graphicals elements -----------//
	private Rectangle loopBarForeground;

	private Rectangle unselectedZoneBegin;
	private Rectangle unselectedZoneEnd;

	private Line currentTimeLine;
	private ImageView imageView;

	private Polygon leftHandle;
	private Polygon rightHandle;

	private Border borderTop;
	private Border borderBottom;

	private FlowPane flowPane = new FlowPane();

	// -------- Events variable -----------//
	private SoundLooperPlayerDragEvent leftHandleDrag = new SoundLooperPlayerDragEvent();
	private SoundLooperPlayerDragEvent rightHandleDrag = new SoundLooperPlayerDragEvent();
	private SoundLooperPlayerDragEvent loopBarDrag = new SoundLooperPlayerDragEvent();

	// -------- Size values and change flags -----------//
	private boolean invalidWidth = true;
	private boolean invalidHeight = true;
	private double contentWidth = 0;

	PlayerView playerView = getSkinnable();
	SoundLooperPlayer player = getSkinnable().getSoundLooperPlayer();

	private ChangeListener<Number> markTimeListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			layout();
		}
	};

	protected PlayerViewSkin(PlayerView control) {
		super(control);

		control.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				control.requestFocus();
			}
		});

		control.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					// must refresh on focus lost, to set control to "unfocused"
					// aspect
					layout();
				}
			}
		});

		SoundLooperPlayer soundLooperPlayer = this.getSkinnable().getSoundLooperPlayer();

		soundLooperPlayer.markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldValue, Mark newValue) {
				layout();
				updateMarkTimeListener(oldValue, newValue);
			};
		});
		updateMarkTimeListener(null, soundLooperPlayer.getCurrentMark());

		soundLooperPlayer.currentSongImageProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						logger.info("The image is changing in the player view " + new Date().getTime());
						File file = soundLooperPlayer.currentSongImageProperty().get();
						try {
							imageView.setImage(new Image(file.toURI().toURL().toExternalForm()));
						} catch (MalformedURLException e) {
							// This exception will normally never be throwed
							logger.error("Unable to get new Image", e);
						}
					}
				});
			}
		});

		control.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					layout();
				}
			}
		});

		imageView = new ImageView(ImageGetter.getDrawableURL("loading_32.png"));
		imageView.setFitWidth(1);
		imageView.setPreserveRatio(false);
		imageView.setSmooth(true);

		leftHandle = ArrowFactory.getArrow(0.7);
		leftHandle.setFill(SoundLooperColor.getBlue());

		rightHandle = ArrowFactory.getArrow(0.7);
		rightHandle.setScaleX(-1);
		rightHandle.setFill(SoundLooperColor.getBlue());

		handleWidth = leftHandle.getBoundsInParent().getWidth();
		handleHeight = leftHandle.getBoundsInParent().getHeight();

		double screenLeft = getScreenLeft();
		borderTop = new Border(screenLeft);
		borderBottom = new Border(screenLeft);

		loopBarForeground = new Rectangle(screenLeft, 0, 0, TOP_MARGIN);
		loopBarForeground.setFill(Color.TRANSPARENT);

		unselectedZoneBegin = new Rectangle(screenLeft, 0, 0, 0);
		unselectedZoneBegin.setFill(SoundLooperColor.getWhite(0.6));

		unselectedZoneEnd = new Rectangle(0, 0, 0, 0);
		unselectedZoneEnd.setFill(SoundLooperColor.getWhite(0.6));

		currentTimeLine = new Line(0, 0, 100, 100);
		currentTimeLine.setStroke(SoundLooperColor.getSeparatorColor());

		getChildren().add(flowPane);
		flowPane.setPadding(new Insets(0, BORDER_PADDING, 0, BORDER_PADDING));

		Label e2 = new Label("Pane1");
		e2.setBackground(
				new Background(new BackgroundFill(javafx.scene.paint.Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		Pane handlePanel = new Pane();
		handlePanel.getChildren().add(leftHandle);
		handlePanel.getChildren().add(rightHandle);
		handlePanel.getChildren().add(loopBarForeground);

		flowPane.getChildren().add(handlePanel);

		flowPane.getChildren().add(borderTop);

		Pane imagePane = new Pane();
		imagePane.getChildren().add(imageView);
		imagePane.getChildren().add(unselectedZoneBegin);
		imagePane.getChildren().add(unselectedZoneEnd);
		imagePane.getChildren().add(currentTimeLine);
		flowPane.getChildren().add(imagePane);

		flowPane.getChildren().add(borderBottom);

		control.widthProperty().addListener(l -> {
			invalidWidth = true;
		});
		control.heightProperty().addListener(l -> {
			invalidHeight = true;
		});
		initDragEvents();
		startTimer();
	}

	protected void updateMarkTimeListener(Mark oldValue, Mark newValue) {

		if (oldValue != null) {
			oldValue.beginMillisecondProperty().removeListener(markTimeListener);
			oldValue.endMillisecondProperty().removeListener(markTimeListener);
		}
		if (newValue != null) {
			newValue.beginMillisecondProperty().addListener(markTimeListener);
			newValue.endMillisecondProperty().addListener(markTimeListener);
		}

	}

	private double getScreenRight(double contentWidth) {
		return contentWidth - handleWidth - BORDER_PADDING * 2;
	}

	private double getScreenLeft() {
		return handleWidth;
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		this.contentWidth = contentWidth;

		int mediaTime;
		int loopPointBegin;
		int loopPointEnd;
		int duration = 0;
		if (!player.isSoundInitialized()) {
			// there is no sound loaded, display en empty player
			mediaTime = 0;
			loopPointBegin = 0;
			loopPointEnd = DEFAULT_DURATION;
			duration = DEFAULT_DURATION;
		} else {
			mediaTime = player.getMediaTime();
			loopPointBegin = player.getLoopPointBeginMillisecond();
			loopPointEnd = player.getLoopPointEndMillisecond();
			try {
				duration = player.getCurrentSound().getDuration();
			} catch (PlayerException e) {
				logger.warn("unable to get duration", e);
			}
		}

		double loopPointBeginPx = convertMsToPx(contentWidth, loopPointBegin, duration);
		double loopPointEndPx = convertMsToPx(contentWidth, loopPointEnd, duration);

		// left handle and decorations
		if (leftHandleDrag.isNotDrag() && loopBarDrag.isNotDrag()) {

			leftHandle.setTranslateX(loopPointBeginPx - handleWidth);
		}
		if (rightHandleDrag.isNotDrag() && loopBarDrag.isNotDrag()) {
			rightHandle.setTranslateX(loopPointEndPx);
		}
		double screenRight = getScreenRight(contentWidth);

		double screenHeight = getScreenHeight(contentHeight);
		double screenWidth = getScreenWidth(contentWidth);

		unselectedZoneEnd.setWidth(screenRight - rightHandle.getTranslateX());

		unselectedZoneBegin.setWidth(leftHandle.getTranslateX());
		unselectedZoneEnd.setX(rightHandle.getTranslateX());

		double loopBarLeft = leftHandle.getTranslateX() + handleWidth;
		loopBarForeground.setX(loopBarLeft);
		loopBarForeground.setWidth(rightHandle.getTranslateX() - loopBarLeft);

		if (invalidWidth || invalidHeight) {

			// loopBarBackground.setWidth(getScreenWidth(contentWidth));
			unselectedZoneBegin.setHeight(screenHeight);

			unselectedZoneEnd.setHeight(screenHeight);

			flowPane.setPrefSize(contentWidth, contentHeight);
			flowPane.setMaxSize(contentWidth, contentHeight);
			flowPane.setMinSize(contentWidth, contentHeight);

			imageView.setFitHeight(screenHeight);
			imageView.setFitWidth(screenWidth);
			imageView.setX(handleWidth);
			imageView.setY(0);
			imageView.setOnMouseClicked(me -> {
				double newTimeMs = convertPxToMs(PlayerViewSkin.this.getSkinnable().getWidth(), me.getX());
				setMediaTime(player, Double.valueOf(newTimeMs).intValue());
				layout();
				me.consume();

			});

			unselectedZoneBegin.setOnMouseClicked(me -> {
				setMediaTimeBegin(player);
				layout();
				me.consume();
			});
			unselectedZoneEnd.setOnMouseClicked(me -> {
				setMediaTimeBegin(player);
				layout();
				me.consume();
			});

			borderTop.setLineWidth(screenWidth);
			borderBottom.setLineWidth(screenWidth);

		}

		double mediaTimePx = convertMsToPx(contentWidth, mediaTime, duration);

		currentTimeLine.setStartX(mediaTimePx);
		currentTimeLine.setEndX(mediaTimePx);
		currentTimeLine.setStartY(0);
		currentTimeLine.setEndY(screenHeight);

		invalidWidth = false;
		invalidHeight = false;
	}

	private void initDragEvents() {
		if (leftHandleDrag.getStartPx() == 0) {
			leftHandle.setOnMousePressed(me -> {
				double dragOffset = me.getSceneX() - leftHandle.getTranslateX();
				leftHandleDrag.startDrag(me.getSceneX(), dragOffset);
			});

			leftHandle.setOnMouseDragged(me -> {
				double newHandleX = me.getSceneX() - leftHandleDrag.getDragOffset();
				if (newHandleX < 0) {
					newHandleX = 0;
				}
				if (newHandleX + handleWidth + MIN_HANDLE_SPACING > rightHandle.getTranslateX()) {
					newHandleX = rightHandle.getTranslateX() - handleWidth - MIN_HANDLE_SPACING;
				}
				leftHandle.setTranslateX(newHandleX);
				layout();
				me.consume();
			});

			leftHandle.setOnMouseReleased(me -> {
				double newTimePx = leftHandle.getTranslateX() + handleWidth;
				double newTimeMs = convertPxToMs(contentWidth, newTimePx);
				leftHandleDrag.endDrag();

				setMediaTimeIfNeeded(player, contentWidth);

				setLoopPointBegin(player, newTimeMs);
				layout();
				me.consume();
			});

			rightHandle.setOnMousePressed(me -> {
				double dragOffset = me.getSceneX() - rightHandle.getTranslateX();
				rightHandleDrag.startDrag(me.getSceneX(), dragOffset);
			});

			rightHandle.setOnMouseDragged(me -> {
				double newHandleX = me.getSceneX() - rightHandleDrag.getDragOffset();

				double screenRight = getScreenRight(contentWidth);
				if (newHandleX > screenRight) {
					newHandleX = screenRight;
				}

				if (newHandleX - MIN_HANDLE_SPACING < leftHandle.getTranslateX() + handleWidth) {
					newHandleX = MIN_HANDLE_SPACING + leftHandle.getTranslateX() + handleWidth;
				}

				rightHandle.setTranslateX(newHandleX);
				layout();
				me.consume();
			});

			rightHandle.setOnMouseReleased(me -> {
				double newTimePx = rightHandle.getTranslateX();
				double newTimeMs = convertPxToMs(contentWidth, newTimePx);
				rightHandleDrag.endDrag();

				setMediaTimeIfNeeded(player, contentWidth);
				setLoopPointEnd(player, newTimeMs);
				layout();
				me.consume();
			});

			loopBarForeground.setOnMousePressed(me -> {
				double dragOffset = me.getSceneX() - loopBarForeground.getX();
				loopBarDrag.startDrag(me.getSceneX(), dragOffset);
			});

			loopBarForeground.setOnMouseDragged(me -> {
				double newHandleX = me.getSceneX() - loopBarDrag.getDragOffset();
				double positionLeftHandle = newHandleX - handleWidth;
				double positionRightHandle = newHandleX + loopBarForeground.getWidth();
				if (positionLeftHandle <= 0) {
					positionLeftHandle = 0;
					positionRightHandle = loopBarForeground.getWidth() + handleWidth;
				}
				double screenRight = getScreenRight(contentWidth);
				if (positionRightHandle >= screenRight) {
					positionRightHandle = screenRight;
					positionLeftHandle = screenRight - loopBarForeground.getWidth() - handleWidth;
				}

				leftHandle.setTranslateX(positionLeftHandle);
				rightHandle.setTranslateX(positionRightHandle);

				layout();
				me.consume();
			});

			loopBarForeground.setOnMouseReleased(me -> {
				double newTimeBeginPx = loopBarForeground.getX();
				double newTimeEndPx = loopBarForeground.getX() + loopBarForeground.getWidth();
				double newTimeBeginMs = convertPxToMs(contentWidth, newTimeBeginPx);
				double newTimeEndMs = convertPxToMs(contentWidth, newTimeEndPx);
				loopBarDrag.endDrag();

				setLoopPoints(player, newTimeBeginMs, newTimeEndMs);
				layout();
				me.consume();
			});
		}
	}

	private void setLoopPointBegin(SoundLooperPlayer player, double newTimeBeginMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointBegin(Double.valueOf(newTimeBeginMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position de début", e);
		}
	}

	private void setLoopPoints(SoundLooperPlayer player, double newTimeBeginMs, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPoints(Double.valueOf(newTimeBeginMs).intValue(),
						Double.valueOf(newTimeEndMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier les positions", e);
		}
	}

	private void setLoopPointEnd(SoundLooperPlayer player, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointEnd(Double.valueOf(newTimeEndMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position de fin", e);
		}
	}

	private void setMediaTimeIfNeeded(Player player, double contentWidth) {
		double startX = unselectedZoneBegin.getX() + unselectedZoneBegin.getWidth();
		if (currentTimeLine.getStartX() > unselectedZoneEnd.getX() || currentTimeLine.getStartX() < startX) {
			double newTime = convertPxToMs(contentWidth, startX);
			setMediaTime(player, newTime);
		}
	}

	private void setMediaTime(Player player, double newTime) {
		try {
			if (player.isSoundInitialized()) {
				player.setMediaTime(Double.valueOf(newTime).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position actuelle", e);
		}
	}

	private void setMediaTimeBegin(SoundLooperPlayer player) {
		try {
			if (player.isSoundInitialized()) {
				int loopPointBenginMs = player.getLoopPointBeginMillisecond();
				player.setMediaTime(loopPointBenginMs);
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position actuelle", e);
		}
	}

	private double convertMsToPx(double contentWidth, int ms, int totalMs) {
		if (totalMs == 0) {
			return 0;
		}
		return handleWidth + (getScreenWidth(contentWidth) * ms) / totalMs;
	}

	private double convertPxToMs(double contentWidth, double px) {
		double pxInScreen = px - handleWidth;

		int duration = getDuration();
		double screenWidth = getScreenWidth(contentWidth);

		double msPerPx = duration / screenWidth;
		return msPerPx * pxInScreen;
	}

	private int getDuration() {
		if (SoundLooperPlayer.getInstance().isSoundInitialized()) {
			try {
				return SoundLooperPlayer.getInstance().getCurrentSound().getDuration();
			} catch (PlayerNotInitializedException e) {
				MessagingUtil.displayError("Impossible de récupérer la durée", e);
			}
		}
		return DEFAULT_DURATION;
	}

	private double getScreenWidth(double contentWidth) {
		return getScreenRight(contentWidth) - getScreenLeft();
	}

	private double getScreenHeight(double contentHeight) {
		return contentHeight - handleHeight - borderBottom.getHeight() - borderTop.getHeight();
	}

	public void startTimer() {
		Timer timerSlide = new Timer();
		timerSlide.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				SoundLooperPlayer soundLooperPlayer = SoundLooperPlayer.getInstance();
				if (soundLooperPlayer.getState() == PlayerState.STATE_PLAYING) {
					layout();
				}
			}

		}, new Date(), 100);
	}

	protected void layout() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// PlayerViewSkin.this.getSkinnable().requestLayout();
				// PlayerViewSkin.this.getSkinnable().layout();
				PlayerViewSkin.this.getSkinnable().forceLayout();
			}
		});
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 400;
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 50;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computeMinWidth(height, topInset, rightInset, bottomInset, leftInset);
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return 50;
	}

	@Override
	protected double computePrefWidth(double width, double topInset, double rightInset, double bottomInset,
			double leftInset) {
		return super.computePrefWidth(width, topInset, rightInset, bottomInset, leftInset);
	}
}
