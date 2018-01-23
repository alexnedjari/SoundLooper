package com.soundlooper.gui;

import com.soundlooper.audio.player.Player;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.PlayerNotInitializedException;
import com.soundlooper.model.SoundLooperPlayer;
import com.soundlooper.system.util.MessagingUtil;

public class PlayerExecutor {
	public final static int DEFAULT_DURATION = 1000;

	public static void setMediaTime(Player player, double newTime) {
		try {
			if (player.isSoundInitialized()) {
				player.setMediaTime(Double.valueOf(newTime).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position actuelle", e);
		}
	}

	public static void setMediaTimeBegin(SoundLooperPlayer player) {
		try {
			if (player.isSoundInitialized()) {
				int loopPointBenginMs = player.getLoopPointBeginMillisecond();
				player.setMediaTime(loopPointBenginMs);
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position actuelle", e);
		}
	}

	public static void setLoopPointBegin(SoundLooperPlayer player, double newTimeBeginMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointBegin(Double.valueOf(newTimeBeginMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position de début", e);
		}
	}

	public static void setLoopPoints(SoundLooperPlayer player, double newTimeBeginMs, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPoints(Double.valueOf(newTimeBeginMs).intValue(),
						Double.valueOf(newTimeEndMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier les positions", e);
		}
	}

	public static void setLoopPointEnd(SoundLooperPlayer player, double newTimeEndMs) {
		try {
			if (player.isSoundInitialized()) {
				player.setLoopPointEnd(Double.valueOf(newTimeEndMs).intValue());
			}
		} catch (PlayerException e) {
			MessagingUtil.displayError("Impossible de modifier la position de fin", e);
		}
	}

	public static int getDuration() {
		if (SoundLooperPlayer.getInstance().isSoundInitialized()) {
			try {
				return SoundLooperPlayer.getInstance().getCurrentSound().getDuration();
			} catch (PlayerNotInitializedException e) {
				MessagingUtil.displayError("Impossible de récupérer la durée", e);
			}
		}
		return DEFAULT_DURATION;
	}
}
