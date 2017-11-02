package com.soundlooper.gui.customComponent.playerView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

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

public class PlayerViewSkin extends SkinBase<PlayerView> {
	public final static int LEFT_MARGIN = 20;
	private final static int RIGTH_MARGIN = 20;
	private final static int TOP_MARGIN = 30;
	private final static int TOP_PADDING = 5;
	private final static int BORDER_PADDING = 10;
	private final static int MIN_HANDLE_SPACING = 2;
	private final static int DEFAULT_DURATION = 1000;

	private Logger logger = LogManager.getLogger(this.getClass());
	private double handleWidth;
	private double handleHeight;

	ChangeListener<Number> markTimeListener = new ChangeListener<Number>() {
		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
			getSkinnable().forceLayout();
		}
	};

	// private Rectangle loopBarBackground;
	// private Rectangle loopBarForeground;

	private Line lineTop1 = new Line();

	private Rectangle unselectedZoneBegin;
	private Rectangle unselectedZoneEnd;

	private Line currentTimeLine;
	// private Line loopPointBeginLine;
	// private Line loopPointEndLine;
	TextArea label = new TextArea("texte");
	ImageView imageView;

	// ImageView leftHandleImage;
	// ImageView rightHandleImage;

	Polygon leftHandle;
	Polygon rightHandle;

	private double dragStartLeftHandlePx;
	private boolean leftHandleDrag = false;

	private double dragStartRightHandlePx;
	private boolean rightHandleDrag = false;

	private double dragStartLoopBarForegroundPx;
	private boolean loopBarForegroundDrag = false;

	private double dragOffset;

	private boolean invalidWidth = true;
	private boolean invalidHeight = true;
	Border borderTop = new Border(LEFT_MARGIN);
	Border borderBottom = new Border(LEFT_MARGIN);

	Pane pane = new Pane();
	FlowPane stackPane = new FlowPane();

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
					control.requestLayout();
				}
			}
		});

		SoundLooperPlayer soundLooperPlayer = this.getSkinnable().getSoundLooperPlayer();

		soundLooperPlayer.markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldValue, Mark newValue) {
				PlayerViewSkin.this.getSkinnable().forceLayout();
				updateMarkTimeListener(oldValue, newValue);
			};
		});
		updateMarkTimeListener(null, soundLooperPlayer.getCurrentMark());

		soundLooperPlayer.currentSongImageProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				logger.info("The image is changing in the player view");
				File file = soundLooperPlayer.currentSongImageProperty().get();
				try {
					imageView.setImage(new Image(file.toURI().toURL().toExternalForm()));
					imageView.setPreserveRatio(false);
					PlayerViewSkin.this.getSkinnable().forceLayout();
				} catch (MalformedURLException e) {
					// This exception will normally never be throwed
					logger.error("Unable to get new Image", e);
				}
			}
		});

		control.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					control.forceLayout();
				}
			}
		});

		imageView = new ImageView(ImageGetter.getIconURL("loading_32.png"));
		imageView.setPreserveRatio(false);

		leftHandle = ArrowFactory.getArrow(0.7);
		leftHandle.setFill(SoundLooperColor.getBlue());

		rightHandle = ArrowFactory.getArrow(0.7);
		rightHandle.setScaleX(-1);
		rightHandle.setFill(SoundLooperColor.getBlue());

		handleWidth = leftHandle.getBoundsInParent().getWidth();
		handleHeight = leftHandle.getBoundsInParent().getHeight();

		// rightHandleImage = ImageGetter.getIcon("rightHandle.png");

		// leftHandleImage = ImageGetter.getIcon("leftHandle.png");

		// loopPointBeginLine = new Line(0, TOP_MARGIN, 0, 0);
		// loopPointEndLine = new Line(0, TOP_MARGIN, 0, 0);

		// loopBarBackground = new Rectangle(LEFT_MARGIN, 0, 0, TOP_MARGIN);
		// loopBarBackground.setFill(SoundLooperColor.WHITE);

		// loopBarForeground = new Rectangle(LEFT_MARGIN, 0, 0, TOP_MARGIN);
		// loopBarForeground.setFill(SoundLooperColor.DARK_GRAY);

		unselectedZoneBegin = new Rectangle(LEFT_MARGIN, 0, 0, 0);
		unselectedZoneBegin.setFill(SoundLooperColor.getWhite(0.6));

		unselectedZoneEnd = new Rectangle(0, 0, 0, 0);
		unselectedZoneEnd.setFill(SoundLooperColor.getWhite(0.6));

		currentTimeLine = new Line(0, 0, 100, 100);
		currentTimeLine.setStroke(SoundLooperColor.getSeparatorColor());

		// loopPointEndLine.startXProperty().bind(rightHandleImage.xProperty());
		// loopPointEndLine.endXProperty().bind(rightHandleImage.xProperty());
		unselectedZoneEnd.xProperty().bind(rightHandle.translateXProperty());

		DoubleBinding leftPropertyBinding = leftHandle.translateXProperty().add(handleWidth);
		// loopPointBeginLine.startXProperty().bind(leftPropertyBinding);
		// loopPointBeginLine.endXProperty().bind(leftPropertyBinding);
		unselectedZoneBegin.widthProperty().bind(leftPropertyBinding.subtract(LEFT_MARGIN));

		// loopBarForeground.xProperty().bind(leftPropertyBinding);
		// loopBarForeground.widthProperty().bind(rightHandleImage.xProperty().subtract(leftPropertyBinding));

		// getChildren().add(pane);
		pane.getChildren().add(imageView);
		pane.getChildren().add(currentTimeLine);
		// pane.getChildren().add(loopBarBackground);
		// pane.getChildren().add(loopBarForeground);
		// pane.getChildren().add(loopPointBeginLine);
		// pane.getChildren().add(loopPointEndLine);
		pane.getChildren().add(rightHandle);
		pane.getChildren().add(leftHandle);
		pane.getChildren().add(unselectedZoneBegin);
		pane.getChildren().add(unselectedZoneEnd);
		// pane.getChildren().add(lineTop1);
		// pane.getChildren().add(borderTop);
		// getChildren().add(label);

		getChildren().add(stackPane);
		stackPane.setPadding(new Insets(TOP_PADDING, BORDER_PADDING, 0, BORDER_PADDING));

		Label e2 = new Label("Pane1");
		e2.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.BLUE, CornerRadii.EMPTY,
				Insets.EMPTY)));

		Pane handlePanel = new Pane();
		handlePanel.getChildren().add(leftHandle);
		handlePanel.getChildren().add(rightHandle);
		stackPane.getChildren().add(handlePanel);

		stackPane.getChildren().add(borderTop);

		Pane imagePane = new Pane();
		imagePane.getChildren().add(imageView);
		imagePane.getChildren().add(unselectedZoneBegin);
		imagePane.getChildren().add(unselectedZoneEnd);
		imagePane.getChildren().add(currentTimeLine);
		stackPane.getChildren().add(imagePane);

		stackPane.getChildren().add(borderBottom);

		control.widthProperty().addListener(l -> {
			invalidWidth = true;
		});
		control.heightProperty().addListener(l -> {
			invalidHeight = true;
		});
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
		return contentWidth - RIGTH_MARGIN;
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		PlayerView playerView = getSkinnable();
		SoundLooperPlayer player = playerView.getSoundLooperPlayer();

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

		// boundRectangle.setHeight(contentHeight);
		// boundRectangle.setWidth(contentWidth);

		double loopPointBeginPx = convertMsToPx(contentWidth, loopPointBegin, duration, true);
		double loopPointEndPx = convertMsToPx(contentWidth, loopPointEnd, duration, true);

		// left handle and decorations
		if (!leftHandleDrag && !loopBarForegroundDrag) {
			leftHandle.setTranslateX(loopPointBeginPx - handleWidth);
		}
		if (!rightHandleDrag && !loopBarForegroundDrag) {
			rightHandle.setTranslateX(loopPointEndPx);
		}
		// loopPointBeginLine.setEndY(contentHeight);
		// loopPointEndLine.setEndY(contentHeight);
		double screenRight = getScreenRight(contentWidth);

		unselectedZoneEnd.setWidth(screenRight - rightHandle.getTranslateX());
		double screenHeight = getScreenHeight(contentHeight);
		double screenWidth = getScreenWidth(contentWidth);
		if (invalidWidth || invalidHeight) {

			// loopBarBackground.setWidth(getScreenWidth(contentWidth));
			unselectedZoneBegin.setHeight(screenHeight);

			unselectedZoneEnd.setHeight(screenHeight);
			stackPane.setPrefSize(contentWidth, contentHeight);
			stackPane.setMaxSize(contentWidth, contentHeight);
			stackPane.setMinSize(contentWidth, contentHeight);
			System.out.println("PARENT : " + contentWidth + ", " + contentHeight);
			System.out.println("PANE : " + stackPane.getWidth() + ", " + stackPane.getHeight());

			imageView.setFitHeight(screenHeight);
			imageView.setFitWidth(screenWidth);
			imageView.setX(LEFT_MARGIN);
			imageView.setY(0);
			imageView.setOnMouseClicked(me -> {
				double newTimeMs = convertPxToMs(PlayerViewSkin.this.getSkinnable().getWidth(), me.getX());
				setMediaTime(player, new Double(newTimeMs).intValue());
				playerView.forceLayout();
				me.consume();

			});

			borderTop.setLineWidth(screenWidth);
			borderBottom.setLineWidth(screenWidth);

		}

		double mediaTimePx = convertMsToPx(contentWidth, mediaTime, duration, true);

		currentTimeLine.setStartX(mediaTimePx);
		currentTimeLine.setEndX(mediaTimePx);
		currentTimeLine.setStartY(0);
		currentTimeLine.setEndY(screenHeight);

		if (invalidWidth) {
			invalidWidth = false;
			if (dragStartLeftHandlePx == 0) {
				leftHandle.setOnMousePressed(me -> {
					leftHandleDrag = true;
					dragOffset = me.getSceneX() - leftHandle.getTranslateX();
					dragStartLeftHandlePx = me.getSceneX();
				});

				leftHandle.setOnMouseDragged(me -> {
					double move = me.getSceneX() - dragStartLeftHandlePx;
					double newHandleX = dragStartLeftHandlePx + move - dragOffset;
					if (newHandleX < 0) {
						newHandleX = 0;
					}
					if (newHandleX + handleWidth + MIN_HANDLE_SPACING > rightHandle.getTranslateX()) {
						newHandleX = rightHandle.getTranslateX() - handleWidth - MIN_HANDLE_SPACING;
					}
					leftHandle.setTranslateX(newHandleX);
				});

				leftHandle.setOnMouseReleased(me -> {
					double newTimePx = leftHandle.getTranslateX() + handleWidth;
					double newTimeMs = convertPxToMs(contentWidth, newTimePx);
					leftHandleDrag = false;
					dragStartLeftHandlePx = 0;

					setMediaTimeIfNeeded(player, contentWidth);

					setLoopPointBegin(player, newTimeMs);
					playerView.forceLayout();
					me.consume();
				});
			}

			if (dragStartRightHandlePx == 0) {
				rightHandle.setOnMousePressed(me -> {
					rightHandleDrag = true;
					dragOffset = me.getSceneX() - rightHandle.getTranslateX();
					dragStartRightHandlePx = me.getSceneX();
				});

				rightHandle.setOnMouseDragged(me -> {
					double move = me.getSceneX() - dragStartRightHandlePx;
					double newHandleX = dragStartRightHandlePx + move - dragOffset;

					if (newHandleX > screenRight) {
						newHandleX = screenRight;
					}

					if (newHandleX - MIN_HANDLE_SPACING < leftHandle.getTranslateX() + handleWidth) {
						newHandleX = MIN_HANDLE_SPACING + leftHandle.getTranslateX() + handleWidth;
					}

					rightHandle.setTranslateX(newHandleX);
				});

				rightHandle.setOnMouseReleased(me -> {
					double newTimePx = rightHandle.getTranslateX();
					double newTimeMs = convertPxToMs(contentWidth, newTimePx);
					dragStartRightHandlePx = 0;
					rightHandleDrag = false;

					setMediaTimeIfNeeded(player, contentWidth);
					setLoopPointEnd(player, newTimeMs);
					playerView.forceLayout();
					me.consume();
				});
			}

			// if (dragStartLoopBarForegroundPx == 0) {
			// loopBarForeground.setOnMousePressed(me -> {
			// loopBarForegroundDrag = true;
			// dragOffset = me.getSceneX() - loopBarForeground.getX();
			// dragStartLoopBarForegroundPx = me.getSceneX();
			// });
			//
			// loopBarForeground.setOnMouseDragged(me -> {
			// double move = me.getSceneX() - dragStartLoopBarForegroundPx;
			// double newHandleX = dragStartLoopBarForegroundPx + move -
			// dragOffset;
			// double positionLeftHandle = newHandleX - HANDLE_WIDTH;
			// double positionRightHandle = newHandleX +
			// loopBarForeground.getWidth();
			// if (positionLeftHandle <= 0) {
			// positionLeftHandle = 0;
			// positionRightHandle = loopBarForeground.getWidth() +
			// HANDLE_WIDTH;
			// }
			// if (positionRightHandle >= screenRight) {
			// positionRightHandle = screenRight;
			// positionLeftHandle = screenRight - loopBarForeground.getWidth() -
			// HANDLE_WIDTH;
			// }
			//
			// leftHandleImage.setX(positionLeftHandle);
			// rightHandleImage.setX(positionRightHandle);
			//
			// });
			//
			// loopBarForeground.setOnMouseReleased(me -> {
			// double newTimeBeginPx = loopBarForeground.getX();
			// double newTimeEndPx = loopBarForeground.getX() +
			// loopBarForeground.getWidth();
			// double newTimeBeginMs = convertPxToMs(contentWidth,
			// newTimeBeginPx);
			// double newTimeEndMs = convertPxToMs(contentWidth, newTimeEndPx);
			// dragStartLoopBarForegroundPx = 0;
			// loopBarForegroundDrag = false;
			//
			// // setMediaTimeIfNeeded(player, contentWidth);
			//
			// setLoopPoints(player, newTimeBeginMs, newTimeEndMs);
			// playerView.forceLayout();
			// me.consume();
			// });
			// }

		}
		invalidWidth = false;
		invalidHeight = false;
	}

	private void setLoopPointBegin(SoundLooperPlayer player, double newTimeBeginMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointBegin(new Double(newTimeBeginMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position de début", e);
		}
	}

	private void setLoopPoints(SoundLooperPlayer player, double newTimeBeginMs, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPoints(new Double(newTimeBeginMs).intValue(), new Double(newTimeEndMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier les positions", e);
		}
	}

	private void setLoopPointEnd(SoundLooperPlayer player, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointEnd(new Double(newTimeEndMs).intValue());
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
				player.setMediaTime(new Double(newTime).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position actuelle", e);
		}
	}

	private double convertMsToPx(double contentWidth, int ms, int totalMs, boolean addMargin) {
		if (totalMs == 0) {
			return 0;
		}
		int margin = 0;
		if (addMargin) {
			margin = LEFT_MARGIN;
		}
		return margin + (getScreenWidth(contentWidth) * ms) / totalMs;
	}

	private double convertPxToMs(double contentWidth, double px) {
		double pxInScreen = px - LEFT_MARGIN;

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
		return contentWidth - LEFT_MARGIN - RIGTH_MARGIN - BORDER_PADDING * 2;
	}

	private double getScreenHeight(double contentHeight) {
		return contentHeight - handleHeight - TOP_PADDING - borderBottom.getHeight() - borderTop.getHeight();
	}

	public void startTimer() {
		Timer timerSlide = new Timer();
		timerSlide.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				SoundLooperPlayer soundLooperPlayer = SoundLooperPlayer.getInstance();
				if (soundLooperPlayer.getState() == PlayerState.STATE_PLAYING) {
					PlayerViewSkin.this.getSkinnable().forceLayout();

				}
			}
		}, new Date(), 100);
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
