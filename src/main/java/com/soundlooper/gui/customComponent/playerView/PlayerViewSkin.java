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
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.audio.player.Player;
import com.soundlooper.audio.player.Player.PlayerState;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.PlayerNotInitializedException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.system.ImageGetter;
import com.soundlooper.system.util.MessagingUtil;

public class PlayerViewSkin extends SkinBase<PlayerView> {
	private final static int LEFT_MARGIN = 30;
	private final static int HANDLE_WIDTH = LEFT_MARGIN;
	private final static int RIGTH_MARGIN = 30;
	private final static int TOP_MARGIN = 30;
	private final static int MIN_HANDLE_SPACING = 2;
	private final static int DEFAULT_DURATION = 1000;

	private Logger logger = LogManager.getLogger(this.getClass());

	private Rectangle loopBarBackground;
	private Rectangle loopBarForeground;

	private Rectangle unselectedZoneBegin;
	private Rectangle unselectedZoneEnd;

	private Line currentTimeLine;
	private Line loopPointBeginLine;
	private Line loopPointEndLine;
	TextArea label = new TextArea("texte");
	ImageView imageView;

	ImageView leftHandleImage;
	ImageView rightHandleImage;

	private double dragStartLeftHandlePx;
	private boolean leftHandleDrag = false;

	private double dragStartRightHandlePx;
	private boolean rightHandleDrag = false;

	private double dragStartLoopBarForegroundPx;
	private boolean loopBarForegroundDrag = false;

	private double dragOffset;

	private boolean invalidWidth = true;
	private boolean invalidHeight = true;

	Pane pane = new Pane();

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

		soundLooperPlayer.stateProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() == PlayerState.STATE_PLAYING) {
					// When play is lanch, focus on player to activate shortcuts
					control.requestFocus();
				}
			};
		});

		soundLooperPlayer.markProperty().addListener(new ChangeListener<Mark>() {
			@Override
			public void changed(ObservableValue<? extends Mark> observable, Mark oldValue, Mark newValue) {
				PlayerViewSkin.this.getSkinnable().forceLayout();
			};
		});

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

		imageView = new ImageView(ImageGetter.getIconURL("loading_32.png"));
		imageView.setPreserveRatio(false);

		rightHandleImage = ImageGetter.getIcon("rightHandle.png");
		leftHandleImage = ImageGetter.getIcon("leftHandle.png");

		loopPointBeginLine = new Line(0, TOP_MARGIN, 0, 0);
		loopPointEndLine = new Line(0, TOP_MARGIN, 0, 0);

		loopBarBackground = new Rectangle(LEFT_MARGIN, 0, 0, TOP_MARGIN);
		loopBarBackground.setFill(Color.LIGHTGRAY);

		loopBarForeground = new Rectangle(LEFT_MARGIN, 0, 0, TOP_MARGIN);
		loopBarForeground.setFill(Color.BLUE);

		unselectedZoneBegin = new Rectangle(LEFT_MARGIN, TOP_MARGIN, 0, 0);
		unselectedZoneBegin.setFill(new Color(0, 0, 0, 0.20));

		unselectedZoneEnd = new Rectangle(0, TOP_MARGIN, 0, 0);
		unselectedZoneEnd.setFill(new Color(0, 0, 0, 0.20));

		currentTimeLine = new Line(0, 0, 100, 100);
		currentTimeLine.setStroke(Color.RED);

		loopPointEndLine.startXProperty().bind(rightHandleImage.xProperty());
		loopPointEndLine.endXProperty().bind(rightHandleImage.xProperty());
		unselectedZoneEnd.xProperty().bind(rightHandleImage.xProperty());

		DoubleBinding leftPropertyBinding = leftHandleImage.xProperty().add(HANDLE_WIDTH);
		loopPointBeginLine.startXProperty().bind(leftPropertyBinding);
		loopPointBeginLine.endXProperty().bind(leftPropertyBinding);
		unselectedZoneBegin.widthProperty().bind(leftPropertyBinding.subtract(LEFT_MARGIN));

		loopBarForeground.xProperty().bind(leftPropertyBinding);
		loopBarForeground.widthProperty().bind(rightHandleImage.xProperty().subtract(leftPropertyBinding));

		getChildren().add(pane);
		pane.getChildren().add(imageView);
		pane.getChildren().add(currentTimeLine);
		pane.getChildren().add(loopBarBackground);
		pane.getChildren().add(loopBarForeground);
		pane.getChildren().add(loopPointBeginLine);
		pane.getChildren().add(loopPointEndLine);
		pane.getChildren().add(rightHandleImage);
		pane.getChildren().add(leftHandleImage);
		pane.getChildren().add(unselectedZoneBegin);
		pane.getChildren().add(unselectedZoneEnd);
		// getChildren().add(label);

		control.widthProperty().addListener(l -> {
			invalidWidth = true;
		});
		control.heightProperty().addListener(l -> {
			invalidHeight = true;
		});
		startTimer();
	}

	private double getScreenRight(double contentWidth) {
		return contentWidth - RIGTH_MARGIN;
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		PlayerView playerView = getSkinnable();
		SoundLooperPlayer player = playerView.getSoundLooperPlayer();

		if (getSkinnable().isFocused()) {
			loopBarForeground.setFill(Color.BLUE);
		} else {
			loopBarForeground.setFill(Color.DARKBLUE);
		}

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
			leftHandleImage.setX(loopPointBeginPx - HANDLE_WIDTH);
		}
		if (!rightHandleDrag && !loopBarForegroundDrag) {
			rightHandleImage.setX(loopPointEndPx);
		}
		loopPointBeginLine.setEndY(contentHeight);
		loopPointEndLine.setEndY(contentHeight);
		double screenRight = getScreenRight(contentWidth);

		unselectedZoneEnd.setWidth(screenRight - rightHandleImage.getX());
		if (invalidWidth || invalidHeight) {
			loopBarBackground.setWidth(getScreenWidth(contentWidth));
			unselectedZoneBegin.setHeight(getScreenHeight(contentHeight));

			unselectedZoneEnd.setHeight(getScreenHeight(contentHeight));
			pane.setPrefSize(contentWidth, contentHeight);
			imageView.setFitHeight(getScreenHeight(contentHeight));
			imageView.setFitWidth(getScreenWidth(contentWidth));
			imageView.setX(LEFT_MARGIN);
			imageView.setY(TOP_MARGIN);
			imageView.setOnMouseClicked(me -> {
				double newTimeMs = convertPxToMs(PlayerViewSkin.this.getSkinnable().getWidth(), me.getX());
				setMediaTime(player, new Double(newTimeMs).intValue());
				playerView.forceLayout();
				me.consume();

			});
		}

		double mediaTimePx = convertMsToPx(contentWidth, mediaTime, duration, true);

		currentTimeLine.setStartX(mediaTimePx);
		currentTimeLine.setEndX(mediaTimePx);
		currentTimeLine.setStartY(TOP_MARGIN);
		currentTimeLine.setEndY(contentHeight);

		if (invalidWidth) {
			invalidWidth = false;
			if (dragStartLeftHandlePx == 0) {
				leftHandleImage.setOnMousePressed(me -> {
					leftHandleDrag = true;
					dragOffset = me.getSceneX() - leftHandleImage.getX();
					dragStartLeftHandlePx = me.getSceneX();
				});

				leftHandleImage.setOnMouseDragged(me -> {
					double move = me.getSceneX() - dragStartLeftHandlePx;
					double newHandleX = dragStartLeftHandlePx + move - dragOffset;
					if (newHandleX < 0) {
						newHandleX = 0;
					}
					if (newHandleX + HANDLE_WIDTH + MIN_HANDLE_SPACING > rightHandleImage.getX()) {
						newHandleX = rightHandleImage.getX() - HANDLE_WIDTH - MIN_HANDLE_SPACING;
					}
					leftHandleImage.setX(newHandleX);
				});

				leftHandleImage.setOnMouseReleased(me -> {
					double newTimePx = leftHandleImage.getX() + HANDLE_WIDTH;
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
				rightHandleImage.setOnMousePressed(me -> {
					rightHandleDrag = true;
					dragOffset = me.getSceneX() - rightHandleImage.getX();
					dragStartRightHandlePx = me.getSceneX();
				});

				rightHandleImage.setOnMouseDragged(me -> {
					double move = me.getSceneX() - dragStartRightHandlePx;
					double newHandleX = dragStartRightHandlePx + move - dragOffset;

					if (newHandleX > screenRight) {
						newHandleX = screenRight;
					}

					if (newHandleX - MIN_HANDLE_SPACING < leftHandleImage.getX() + HANDLE_WIDTH) {
						newHandleX = MIN_HANDLE_SPACING + leftHandleImage.getX() + HANDLE_WIDTH;
					}

					rightHandleImage.setX(newHandleX);
				});

				rightHandleImage.setOnMouseReleased(me -> {
					double newTimePx = rightHandleImage.getX();
					double newTimeMs = convertPxToMs(contentWidth, newTimePx);
					dragStartRightHandlePx = 0;
					rightHandleDrag = false;

					setMediaTimeIfNeeded(player, contentWidth);
					setLoopPointEnd(player, newTimeMs);
					playerView.forceLayout();
					me.consume();
				});
			}

			if (dragStartLoopBarForegroundPx == 0) {
				loopBarForeground.setOnMousePressed(me -> {
					loopBarForegroundDrag = true;
					dragOffset = me.getSceneX() - loopBarForeground.getX();
					dragStartLoopBarForegroundPx = me.getSceneX();
				});

				loopBarForeground.setOnMouseDragged(me -> {
					double move = me.getSceneX() - dragStartLoopBarForegroundPx;
					double newHandleX = dragStartLoopBarForegroundPx + move - dragOffset;
					double positionLeftHandle = newHandleX - HANDLE_WIDTH;
					double positionRightHandle = newHandleX + loopBarForeground.getWidth();
					if (positionLeftHandle <= 0) {
						positionLeftHandle = 0;
						positionRightHandle = loopBarForeground.getWidth() + HANDLE_WIDTH;
					}
					if (positionRightHandle >= screenRight) {
						positionRightHandle = screenRight;
						positionLeftHandle = screenRight - loopBarForeground.getWidth() - HANDLE_WIDTH;
					}

					leftHandleImage.setX(positionLeftHandle);
					rightHandleImage.setX(positionRightHandle);

				});

				loopBarForeground.setOnMouseReleased(me -> {
					double newTimeBeginPx = loopBarForeground.getX();
					double newTimeEndPx = loopBarForeground.getX() + loopBarForeground.getWidth();
					double newTimeBeginMs = convertPxToMs(contentWidth, newTimeBeginPx);
					double newTimeEndMs = convertPxToMs(contentWidth, newTimeEndPx);
					dragStartLoopBarForegroundPx = 0;
					loopBarForegroundDrag = false;

					setMediaTimeIfNeeded(player, contentWidth);
					setLoopPointEnd(player, newTimeEndMs);
					setLoopPointBegin(player, newTimeBeginMs);
					playerView.forceLayout();
					me.consume();
				});

			}
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
		if (currentTimeLine.getStartX() > loopPointEndLine.getStartX()
				|| currentTimeLine.getStartX() < loopPointBeginLine.getStartX()) {
			double newTime = convertPxToMs(contentWidth, loopPointBeginLine.getStartX());
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
		return contentWidth - LEFT_MARGIN - RIGTH_MARGIN;
	}

	private double getScreenHeight(double contentHeight) {
		return contentHeight - TOP_MARGIN;
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
}
