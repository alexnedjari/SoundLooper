/**
 *
 */
package com.soundlooper.model;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.soundlooper.audio.player.Player;
import com.soundlooper.audio.player.ThreadImageGenerator;
import com.soundlooper.exception.PlayerException;
import com.soundlooper.exception.SoundLooperException;
import com.soundlooper.exception.SoundLooperObjectAlreadyExistsException;
import com.soundlooper.exception.SoundLooperRecordNotFoundException;
import com.soundlooper.exception.SoundLooperRuntimeException;
import com.soundlooper.model.mark.Mark;
import com.soundlooper.model.song.Song;
import com.soundlooper.model.tag.Tag;
import com.soundlooper.model.tag.TagDAO;
import com.soundlooper.service.entite.mark.MarkService;
import com.soundlooper.service.entite.song.SongService;
import com.soundlooper.service.entite.tag.TagService;
import com.soundlooper.system.preferences.Preferences;

/**
 * ------------------------------------------------------- Sound Looper is an
 * audio player that allow user to loop between two points Copyright (C) 2014
 * Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * The sound Looper general model
 *
 * @author Alexandre NEDJARI
 * @since 17 févr. 2014 -------------------------------------------------------
 */
public class SoundLooperPlayer extends Player {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static SoundLooperPlayer instance = null;

	/**
	 * The current song
	 */
	private SimpleObjectProperty<Song> song = new SimpleObjectProperty<>();

	/**
	 * The current mark This mark is NEVER an element of the mark list of the
	 * current song, but a copy to avoid problems when the user update it but
	 * without savin it
	 */
	private SimpleObjectProperty<Mark> mark = new SimpleObjectProperty<Mark>();

	private SimpleBooleanProperty isCurrentSongFavorite = new SimpleBooleanProperty(false);

	private SimpleBooleanProperty isCurrentMarkEditable = new SimpleBooleanProperty(false);

	private SimpleBooleanProperty isCurrentMarkDirty = new SimpleBooleanProperty(false);

	private SimpleObjectProperty<File> currentSongImage = new SimpleObjectProperty<>();

	public SoundLooperPlayer() {

	}

	/**
	 * @return the song
	 */
	public Song getSong() {
		return this.song.get();
	}

	/**
	 * Access the song property
	 * 
	 * @return the song property
	 */
	public SimpleObjectProperty<Song> songProperty() {
		return this.song;
	}

	/**
	 * Access the mark property
	 * 
	 * @return the mark property
	 */
	public SimpleObjectProperty<Mark> markProperty() {
		return this.mark;
	}

	/**
	 * Get the current Mark
	 * 
	 * @return the current mark or null if there is no current mark
	 */
	public Mark getCurrentMark() {
		return mark.get();
	}

	public static SoundLooperPlayer getInstance() {
		if (SoundLooperPlayer.instance == null) {
			SoundLooperPlayer.instance = new SoundLooperPlayer();

		}
		return SoundLooperPlayer.instance;
	}

	/**
	 * Switch the favorite attribute on the current loaded song
	 * 
	 * @throws SoundLooperException
	 *             if an exception is threw
	 */
	public void switchFavoriteOnCurrentSong() throws SoundLooperException {
		if (getSong() == null) {
			return;
		}
		setSongFavorite(getSong(), !getSong().isFavorite());
	}

	private void setSongFavorite(Song song, boolean isFavorite) throws SoundLooperException {
		logger.info("Set favorite = " + isFavorite + " on song '" + song.getFile().getAbsolutePath() + "'");
		song.setFavorite(isFavorite);
		SongService.getInstance().validateSong(song);
	}

	/**
	 * @param idMark
	 *            the mark id
	 * @throws SoundLooperException
	 *             if an error es detected
	 */
	public void deleteMarkOnCurrentSong(String idMark) throws SoundLooperException {
		logger.info("Delete mark with id '" + idMark + "' on song '" + getSong().getFile().getAbsolutePath() + "'");
		Mark mark = this.getMarkFromId(idMark);
		MarkService.getInstance().delete(mark);

	}

	/**
	 * @param nom
	 *            the mark's name
	 * @throws SoundLooperException
	 *             if error is detected
	 * @throws PlayerException
	 */
	public void createNewMarkAtCurrentPosition(String nom) throws SoundLooperException, PlayerException {
		logger.info("Create current mark named '" + nom + "' at current position");
		Mark currentMark = getCurrentMark();
		if (currentMark != null) {
			Mark mark = MarkService.getInstance().createMark(getSong(), nom, getLoopPointBeginMillisecond(),
					getLoopPointEndMillisecond(), true);
			this.selectMark(mark);
		}
	}

	public int getLoopPointBeginMillisecond() {
		Mark currentMark = getCurrentMark();
		if (currentMark != null) {
			return currentMark.getBeginMillisecond();
		}
		return 0;
	}

	public int getLoopPointEndMillisecond() {
		Mark currentMark = getCurrentMark();
		if (currentMark != null) {
			return currentMark.getEndMillisecond();
		}
		return 0;
	}

	/**
	 *
	 * @param idMark
	 *            the mark's id
	 * @return the finded mark
	 * @throws SoundLooperException
	 *             if the mark is not found
	 */
	private Mark getMarkFromId(String idMark) throws SoundLooperException {
		Map<String, Mark> mapMarks = this.getSong().getMarks();
		Set<String> marksNames = mapMarks.keySet();
		for (String name : marksNames) {
			Mark mark = mapMarks.get(name);
			if (mark.getId() == Integer.valueOf(idMark).intValue()) {
				logger.info("The mark with id '" + idMark + "' is named '" + mark.getName() + "'");
				return mark;
			}
		}
		throw new SoundLooperException("Le marqueur avec l'id '" + idMark
				+ "' n'existe pas pour la chanson actuellement chargée");
	}

	public SimpleObjectProperty<File> currentSongImageProperty() {
		return this.currentSongImage;
	}

	/**
	 * @return the favorite list
	 */
	public List<Song> getFavoriteSongList() {
		return SongService.getInstance().getFavoriteSongList();
	}

	/**
	 *
	 */
	public void purgeSong() {
		SongService.getInstance().purgeSong();

	}

	/**
	 * @param mark
	 *            the mark to select id
	 * @throws PlayerException
	 */
	public void selectMark(Mark mark) throws PlayerException {

		if (mark != null) {
			logger.info("Set current mark : " + mark.getName());

			Mark clone = mark.clone();
			applyLoopPoints(clone.getBeginMillisecond(), clone.getEndMillisecond());

			// the mark is first set to null, so, if the mark was the same than
			// the old one, the binded properties will be notified.
			// Usefull if a mark is modified but not saved. If the user select
			// the same mark, the binded properties will get back the persisted
			// state of the mark
			SoundLooperPlayer.this.mark.set(null);
			SoundLooperPlayer.this.mark.set(clone);
			isCurrentMarkDirty.bind(getCurrentMark().dirtyProperty());
			isCurrentMarkEditable.bind(getCurrentMark().editableProperty());
		} else {
			logger.info("Set current mark to null");
			this.mark.set(null);
			isCurrentMarkDirty.set(false);
			isCurrentMarkEditable.set(false);
		}
	}

	/**
	 * Get the default mark for this song
	 * 
	 * @param song
	 *            the default mark for this song
	 * @return
	 */
	public Mark getDefaultMark(Song song) {
		logger.info("Get the default mark for song '" + song.getFile().getAbsolutePath() + "'");
		Collection<Mark> marks = song.getMarks().values();
		for (Mark mark : marks) {
			if (!mark.isEditable()) {
				logger.info("Find default mark " + mark.getName());
				return mark;
			}
		}
		logger.info("Unable to find default mark");
		return null;
	}

	public void saveCurrentMark() throws SoundLooperException {
		logger.info("Save the current mark");
		if (getCurrentMark() == null) {
			return;
		}

		// Mark clone = mark.get().clone();
		MarkService.getInstance().validateMark(getCurrentMark());
		getSong().getMarks().put(getCurrentMark().getName(), getCurrentMark());
	}

	public void setSong(Song song) {
		logger.info("Set the song : " + song);
		this.song.set(song);
		if (song != null) {
			isCurrentSongFavorite.bind(song.isFavoriteProperty());
		} else {
			isCurrentSongFavorite.set(false);
		}

	}

	public SimpleBooleanProperty isCurrentSongFavoriteProperty() {
		return isCurrentSongFavorite;
	}

	public SimpleBooleanProperty isCurrentMarkEditableProperty() {
		return isCurrentMarkEditable;
	}

	public SimpleBooleanProperty isCurrentMarkDirtyProperty() {
		return isCurrentMarkDirty;
	}

	/**
	 * Get an unique valid name for mark for this song
	 * 
	 * @param song
	 *            the song
	 * @param nom
	 *            the wanted name
	 * @return a valid unique name
	 */
	public String getValidNameForMark(Song song, String name) {
		String nomValide = MarkService.getInstance().getNomValide(song, name);
		return nomValide;
	}

	// TODO passer en privé

	public void onSongLoaded(File songFile) throws PlayerException {
		Preferences.getInstance().setLastPathUsed(songFile.getAbsolutePath());
		Preferences.getInstance().addFileToRecentFileList(songFile.getAbsolutePath());
		// recherche de la chanson dans la liste des favoris
		ObservableList<Song> favoriteList = Song.getFavoriteList();
		Song favoriteSong = null;
		for (Song song : favoriteList) {
			if (song.getFile().equals(songFile)) {
				favoriteSong = song;
			}
		}

		if (favoriteSong != null) {
			setSong(favoriteSong);
		} else {
			try {
				// Recherche l'objet en base de données
				setSong(SongService.getInstance().getSong(songFile));
			} catch (SoundLooperRecordNotFoundException e) {
				// La chanson n'est pas encore enregistrée, on crée un nouvel
				// objet
				setSong(SongService.getInstance().createSong(songFile));
			}
		}

		// création du marqueur par défaut
		try {
			String defaultMarkName = "Piste complète";
			if (getSong().getMarks().get(defaultMarkName) == null) {
				// If the user load a favorite song 1, default mark will be
				// created on it. The user then get on a song2, and load again
				// Song 1. The song 1 it getted from favorite list, where it
				// already has the default mark
				Mark mark = new Mark(getValidNameForMark(getSong(), defaultMarkName), 0, getCurrentSound()
						.getDuration(), getSong(), false);
				mark.setDirty(false);
				this.selectMark(mark);
			}
		} catch (SoundLooperObjectAlreadyExistsException e) {
			// Impossible case
			throw new SoundLooperRuntimeException("Unable to create default mark", e);
		}
	}

	public void selectDefaultMark() throws PlayerException {
		if (getSong() != null) {
			selectMark(getDefaultMark(getSong()));
		}

	}

	public Tag createTag(String name, Tag parent) throws SoundLooperException {
		return TagService.getInstance().createTag(name, parent);
	}

	public void saveTag(Tag newTag) {
		TagDAO.getInstance().persist(newTag);

	}

	/**
	 * Delete tag and childs recursively
	 * 
	 * @param tag
	 *            the tag
	 * @throws SoundLooperException
	 */
	public void deleteTag(Tag tag) throws SoundLooperException {
		TagService.getInstance().deleteTag(tag);
	}

	public void moveTag(Tag movedTag, Tag destinationTag) {
		TagService.getInstance().moveTag(movedTag, destinationTag);

	}

	public void addTagToSong(Song song, Tag newTag) {
		TagService.getInstance().addTagToSong(song, newTag);
	}

	public void removeTagFromSong(Song song, Tag tagToRemove) {
		TagService.getInstance().removeTagFromSong(song, tagToRemove);
	}

	@Override
	public void desallocate() throws PlayerException {
		if (isSystemInitialized()) {
			if (isSoundInitialized()) {
				stop();
			}
			super.desallocate();
		}

	}

	public void setLoopPointEnd(int position) throws PlayerException {
		Mark currentMark = this.getCurrentMark();
		if (isSoundInitialized() && currentMark != null) {
			if (position > getCurrentSound().getDuration()) {
				position = getCurrentSound().getDuration();
			}

			if (position < getLoopPointBeginMillisecond() + MINIMAL_MS_LOOP) {
				position = getLoopPointBeginMillisecond() + MINIMAL_MS_LOOP;
			}
			setLoopPoints(getLoopPointBeginMillisecond(), position);
		}
	}

	public void setLoopPointBegin(int position) throws PlayerException {
		Mark currentMark = this.getCurrentMark();
		if (isSoundInitialized() && currentMark != null) {
			if (position < 0) {
				position = 0;
			}

			int loopPointEndMillisecond = getLoopPointEndMillisecond();
			if (loopPointEndMillisecond > getCurrentSound().getDuration()) {
				// Possible in we are in file loading
				loopPointEndMillisecond = getCurrentSound().getDuration();
			}

			if (position > loopPointEndMillisecond - MINIMAL_MS_LOOP) {
				position = loopPointEndMillisecond - MINIMAL_MS_LOOP;
			}
			setLoopPoints(position, loopPointEndMillisecond);
		}
	}

	public void setLoopPoints(int beginPosition, int endPosition) throws PlayerException {
		Mark currentMark = this.getCurrentMark();
		if (isSoundInitialized() && currentMark != null) {
			if (beginPosition < 0) {
				beginPosition = 0;
			}

			if (endPosition > getCurrentSound().getDuration()) {
				beginPosition = getCurrentSound().getDuration();
			}

			// Unable to know if the begin or the end was moved, arbitrary try
			// first to move the begin one
			if (beginPosition > endPosition) {
				if (endPosition > MINIMAL_MS_LOOP) {
					beginPosition = endPosition - MINIMAL_MS_LOOP;
				} else {
					endPosition = beginPosition + MINIMAL_MS_LOOP;
				}
			}

			applyLoopPoints(beginPosition, endPosition);
			currentMark.setLoopPoints(beginPosition, endPosition);
		}
	}

	@Override
	public void loadSong(File file) throws PlayerException {
		super.loadSong(file);
		SoundLooperPlayer.getInstance().onSongLoaded(file);
		new ThreadImageGenerator(SoundLooperPlayer.getInstance().getCurrentSound(), new Consumer<File>() {
			@Override
			public void accept(File file) {
				logger.info("Image " + file.getAbsolutePath() + " generated");
				currentSongImage.set(file);
			}
		}).start();
	}
}
