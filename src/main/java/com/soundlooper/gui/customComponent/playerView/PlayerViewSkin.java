package com.soundlooper.gui.customComponent.playerView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.CssColor;
import com.soundlooper.audio.player.Player;
import com.soundlooper.audio.player.Player.PlayerState;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.gui.PlayerExecutor;
import com.soundlooper.gui.customComponent.util.ArrowFactory;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.ImageGetter;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	private double handleWidth;
	private double handleHeight;

	// -------- Graphicals elements -----------//
	private FlowPane flowPane = new FlowPane();

	private Polygon leftHandle;
	private Polygon rightHandle;
	private Rectangle loopBarForeground;

	private Border borderTop;

	private Rectangle unselectedZoneBegin;
	private Rectangle unselectedZoneEnd;
	private Line currentTimeLine;
	private ImageView imageView;

	private Border borderBottom;

	// -------- Events variable -----------//
	private SoundLooperPlayerDragEvent leftHandleDrag = new SoundLooperPlayerDragEvent();
	private SoundLooperPlayerDragEvent rightHandleDrag = new SoundLooperPlayerDragEvent();
	private SoundLooperPlayerDragEvent loopBarDrag = new SoundLooperPlayerDragEvent();

	// -------- Size values and change flags -----------//
	private boolean invalidWidth = true;
	private boolean invalidHeight = true;
	private double contentWidth = 0;

	// -------- Others -----------//
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

		// --------------LISTENER DEFINITION----------------//
		// Control listener definition
		control.setOnMousePressed(e -> control.requestFocus());
		control.widthProperty().addListener(l -> {
			invalidWidth = true;
		});
		control.heightProperty().addListener(l -> {
			invalidHeight = true;
		});

		// Player listener definition
		player.markProperty().addListener((observable, oldValue, newValue) -> {
			layout();
			updateMarkTimeListener(oldValue, newValue);
		});
		updateMarkTimeListener(null, player.getCurrentMark());

		player.currentSongImageProperty().addListener(observable -> {
			Platform.runLater(() -> {
				logger.info("The image is changing in the player view " + new Date().getTime());
				File file = player.currentSongImageProperty().get();
				try {
					imageView.setImage(new Image(file.toURI().toURL().toExternalForm()));
				} catch (MalformedURLException e) {
					// This exception will normally never be throwed
					logger.error("Unable to get new Image", e);
				}
			});
		});

		// -------------------UI DEFINITION----------------//
		getChildren().add(flowPane);
		flowPane.setPadding(new Insets(0, BORDER_PADDING, 0, BORDER_PADDING));

		// Handle definitions
		leftHandle = ArrowFactory.getArrow(0.7);
		leftHandle.setFill(CssColor.BLUE.getColor());

		handleWidth = leftHandle.getBoundsInParent().getWidth();
		handleHeight = leftHandle.getBoundsInParent().getHeight();
		double screenLeft = getScreenLeft();

		rightHandle = ArrowFactory.getArrow(0.7);
		rightHandle.setScaleX(-1);
		rightHandle.setFill(CssColor.BLUE.getColor());

		loopBarForeground = new Rectangle(screenLeft, 0, 0, TOP_MARGIN);
		loopBarForeground.setFill(Color.TRANSPARENT);

		Pane handlePanel = new Pane();
		handlePanel.getChildren().add(leftHandle);
		handlePanel.getChildren().add(rightHandle);
		handlePanel.getChildren().add(loopBarForeground);

		flowPane.getChildren().add(handlePanel);

		// Border top definition
		borderTop = new Border(screenLeft);
		flowPane.getChildren().add(borderTop);

		// Screen definition
		imageView = new ImageView(ImageGetter.getDrawableURL("loading_32.png"));
		imageView.setFitWidth(1);
		imageView.setPreserveRatio(false);
		imageView.setSmooth(true);

		unselectedZoneBegin = new Rectangle(screenLeft, 0, 0, 0);
		unselectedZoneBegin.setFill(CssColor.WHITE.getColor(0.6));

		unselectedZoneEnd = new Rectangle(0, 0, 0, 0);
		unselectedZoneEnd.setFill(CssColor.WHITE.getColor(0.6));

		currentTimeLine = new Line(0, 0, 100, 100);
		currentTimeLine.setStroke(CssColor.GRAY.getColor());

		Pane imagePane = new Pane();
		imagePane.getChildren().add(imageView);
		imagePane.getChildren().add(unselectedZoneBegin);
		imagePane.getChildren().add(unselectedZoneEnd);
		imagePane.getChildren().add(currentTimeLine);
		flowPane.getChildren().add(imagePane);

		// Border bottom definition
		borderBottom = new Border(screenLeft);
		flowPane.getChildren().add(borderBottom);

		initDragEvents();
		startTimer();
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		this.contentWidth = contentWidth;

		// default values if there is no sound loaded, display en empty player
		int mediaTime = 0;
		int loopPointBegin = 0;
		int loopPointEnd = PlayerExecutor.DEFAULT_DURATION;
		int duration = PlayerExecutor.DEFAULT_DURATION;
		if (player.isSoundInitialized()) {
			mediaTime = player.getMediaTime();
			loopPointBegin = player.getLoopPointBeginMillisecond();
			loopPointEnd = player.getLoopPointEndMillisecond();
			try {
				duration = player.getCurrentSound().getDuration();
			} catch (PlayerException e) {
				logger.warn("unable to get duration", e);
			}
		}

		double loopPointBeginPx = convertMsToPx(loopPointBegin, duration);
		double loopPointEndPx = convertMsToPx(loopPointEnd, duration);

		double screenRight = getScreenRight();
		double screenHeight = getScreenHeight(contentHeight);
		double screenWidth = getScreenWidth();

		// left handle and decorations
		if (leftHandleDrag.isNotDrag() && loopBarDrag.isNotDrag()) {
			leftHandle.setTranslateX(loopPointBeginPx - handleWidth);
		}
		if (rightHandleDrag.isNotDrag() && loopBarDrag.isNotDrag()) {
			rightHandle.setTranslateX(loopPointEndPx);
		}

		unselectedZoneEnd.setWidth(screenRight - rightHandle.getTranslateX());
		unselectedZoneBegin.setWidth(leftHandle.getTranslateX());
		unselectedZoneEnd.setX(rightHandle.getTranslateX());

		double loopBarLeft = leftHandle.getTranslateX() + handleWidth;
		loopBarForeground.setX(loopBarLeft);
		loopBarForeground.setWidth(rightHandle.getTranslateX() - loopBarLeft);

		if (invalidWidth || invalidHeight) {
			unselectedZoneBegin.setHeight(screenHeight);
			unselectedZoneEnd.setHeight(screenHeight);

			imageView.setFitHeight(screenHeight);
			imageView.setFitWidth(screenWidth);
			imageView.setX(handleWidth);
			imageView.setY(0);
			imageView.setOnMouseClicked(me -> {
				double newTimeMs = convertPxToMs(me.getX());
				PlayerExecutor.setMediaTime(player, Double.valueOf(newTimeMs).intValue());
				layout();
				me.consume();
			});

			unselectedZoneBegin.setOnMouseClicked(me -> {
				PlayerExecutor.setMediaTimeBegin(player);
				layout();
				me.consume();
			});
			unselectedZoneEnd.setOnMouseClicked(me -> {
				PlayerExecutor.setMediaTimeBegin(player);
				layout();
				me.consume();
			});

			borderTop.setLineWidth(screenWidth);
			borderBottom.setLineWidth(screenWidth);
		}

		double mediaTimePx = convertMsToPx(mediaTime, duration);

		currentTimeLine.setStartX(mediaTimePx);
		currentTimeLine.setEndX(mediaTimePx);
		currentTimeLine.setStartY(0);
		currentTimeLine.setEndY(screenHeight);

		invalidWidth = false;
		invalidHeight = false;
	}

	private double getScreenRight() {
		return this.contentWidth - handleWidth - BORDER_PADDING * 2;
	}

	private double getScreenLeft() {
		return handleWidth;
	}

	private double getScreenWidth() {
		return getScreenRight() - getScreenLeft();
	}

	private double getScreenHeight(double contentHeight) {
		return contentHeight - handleHeight - borderBottom.getHeight() - borderTop.getHeight();
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

	private void setMediaTimeIfNeeded(Player player) {
		double startX = unselectedZoneBegin.getX() + unselectedZoneBegin.getWidth();
		if (currentTimeLine.getStartX() > unselectedZoneEnd.getX() || currentTimeLine.getStartX() < startX) {
			double newTime = convertPxToMs(startX);
			PlayerExecutor.setMediaTime(player, newTime);
		}
	}

	private double convertMsToPx(int ms, int totalMs) {
		if (totalMs == 0) {
			return 0;
		}
		return handleWidth + (getScreenWidth() * ms) / totalMs;
	}

	private double convertPxToMs(double px) {
		double pxInScreen = px - handleWidth;

		int duration = PlayerExecutor.getDuration();
		double screenWidth = getScreenWidth();

		double msPerPx = duration / screenWidth;
		return msPerPx * pxInScreen;
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
		Platform.runLater(() -> PlayerViewSkin.this.getSkinnable().forceLayout());
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
				double newTimeMs = convertPxToMs(newTimePx);
				leftHandleDrag.endDrag();

				setMediaTimeIfNeeded(player);

				PlayerExecutor.setLoopPointBegin(player, newTimeMs);
				layout();
				me.consume();
			});

			rightHandle.setOnMousePressed(me -> {
				double dragOffset = me.getSceneX() - rightHandle.getTranslateX();
				rightHandleDrag.startDrag(me.getSceneX(), dragOffset);
			});

			rightHandle.setOnMouseDragged(me -> {
				double newHandleX = me.getSceneX() - rightHandleDrag.getDragOffset();

				double screenRight = getScreenRight();
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
				double newTimeMs = convertPxToMs(newTimePx);
				rightHandleDrag.endDrag();

				setMediaTimeIfNeeded(player);
				PlayerExecutor.setLoopPointEnd(player, newTimeMs);
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
				double screenRight = getScreenRight();
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
				double newTimeBeginMs = convertPxToMs(newTimeBeginPx);
				double newTimeEndMs = convertPxToMs(newTimeEndPx);
				loopBarDrag.endDrag();

				PlayerExecutor.setLoopPoints(player, newTimeBeginMs, newTimeEndMs);
				layout();
				me.consume();
			});
		}
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
