/**
 *
 */
package com.soundlooper.system.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ====================================================================
 *
 * Sound Looper is an audio player that allow user to loop between two points
 * Copyright (C) 2014 Alexandre NEDJARI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Process searchs
 *
 * @author Alexandre NEDJARI
 * @since 8 mars 2011
 *
 * ====================================================================
 */
public class SearchEngine {

	/**
	 * Search state : not initialized
	 */
	public static final String STATE_NOT_INITIALIZED = "Not initialized";

	/**
	 * Search state : initialized but not started
	 */
	public static final String STATE_NOT_STARTED = "Not started";

	/**
	 * Search state : Search is running
	 */
	public static final String STATE_RUNNING = "Running";

	/**
	 * Search state : Search was canceled
	 */
	public static final String STATE_CANCELED = "Canceled";

	/**
	 * Search state : Search is ended
	 */
	public static final String STATE_TERMINATED = "Terminated";

	private static String PAS_DE_RECHERCHE_EN_ATTENTE = "PAS_DE_RECHERCHE_EN_ATTENTE";

	/**
	 * The transformer list to apply to the string used for search
	 * (To have a permissive search)
	 */
	protected ArrayList<StringTransformer> transformers;

	/**
	 * The listeners to notifie when a search event occurred
	 */
	protected ArrayList<SearchListener> listeners;

	protected List<Searchable> searchables;

	protected ArrayList<Searchable> lastResult = new ArrayList<Searchable>();

	protected String lastSeach = null;

	protected String waitingSearch = SearchEngine.PAS_DE_RECHERCHE_EN_ATTENTE;

	/**
	 * The search thread. initialized when a search is launched
	 */
	private ThreadSearch search;

	/**
	 * Constructor
	 */
	public SearchEngine(List<? extends Searchable> searchables) {
		this.transformers = new ArrayList<StringTransformer>();
		this.listeners = new ArrayList<SearchListener>();
		this.searchables = new ArrayList<Searchable>();
		this.searchables.addAll(searchables);
	}

	/**
	 * Add a search listener
	 * @param listener the listener
	 */
	public void addSearchListener(SearchListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Add a string transformer
	 * @param transformer the string transformer
	 */
	public void addTransformer(StringTransformer transformer) {
		this.transformers.add(transformer);
	}

	/**
	 * Launch a search. When the search ended, the listeners are notified
	 * @param stringToSearch the String to search
	 * @param searchables the searched objects
	 */
	public void performSearch(String stringToSearch) {
		synchronized (this.waitingSearch) {
			if (!this.getSearchState().equals(SearchEngine.STATE_RUNNING)) {
				this.search = new ThreadSearch(stringToSearch);
				this.search.start();
			} else {
				this.waitingSearch = stringToSearch;
			}
		}
	}

	//	/**
	//	 * Affine the last search.
	//	 * @param newStringToSearch New String to search. old string must be include in the new Search
	//	 * @throws SearchException if the old string to search is not include in new string to search
	//	 */
	//	public void askToRefineSearch(String newStringToSearch) throws SearchException {
	//		if (this.search != null) {
	//			this.search.askToRefineSearch(newStringToSearch);
	//		}
	//	}

	//	/**
	//	 * Get the search result or empty result if the search is not initialized
	//	 * @return the search result or empty result if the search is not initialized
	//	 */
	//	public ArrayList<Searchable> getResult() {
	//		if (this.search == null) {
	//			return new ArrayList<Searchable>();
	//		}
	//		return this.search.getResult();
	//	}

	/**
	 * Get the search state
	 * @return the search state
	 */
	public String getSearchState() {
		if (this.search == null) {
			return SearchEngine.STATE_NOT_INITIALIZED;
		}
		return this.search.getSearchState();
	}

	/**
	 * Cancel the current search if any
	 */
	public void cancelSearch() {
		if (this.search != null) {
			this.search.cancelSearch();
		}
	}

	/**
	 *
	 * ====================================================================
	 *
	 * Search is make in a thread
	 *
	 * @author Alexandre NEDJARI
	 * @since 9 mars 2011
	 *
	 * ====================================================================
	 */
	private class ThreadSearch extends Thread {

		/**
		 * The serach state, equal to a constant of SearchEngine class
		 */
		private String searchState;

		/**
		 * is the search canceled?
		 */
		boolean canceled = false;

		/**
		 * The String to search
		 */
		protected String stringToSearch;

		//		/**
		//		 * The transformed tring to search
		//		 */
		//		protected String transformedStringToSearch;

		/**
		 * The searchables
		 */
		private List<Searchable> searchables;

		/**
		 * The result. updated when a new result is found
		 */
		protected final ArrayList<Searchable> result = new ArrayList<Searchable>();

		//		/**
		//		 * The delta of searchable to add.
		//		 */
		//		protected final ArrayList<Searchable> addDelta = new ArrayList<Searchable>();
		//
		//		/**
		//		 * The delta of searchable to remove.
		//		 */
		//private final ArrayList<Searchable> removeDelta = new ArrayList<Searchable>();

		/**
		 * Used to calculate search time
		 */
		long startTime;

		/**
		 * Used for deltas to know if the delta send to the listeners
		 * is the first one
		 */
		//boolean firstResult;

		//private ThreadRefine threadRefine = null;

		/**
		 * Constructor
		 * @param stringToSearch The String to search
		 * @param searchables The searchables
		 */
		public ThreadSearch(String stringToSearch) {
			super();
			this.searchState = SearchEngine.STATE_NOT_STARTED;
			this.stringToSearch = stringToSearch;
			this.startTime = new Date().getTime();
			//this.firstResult = true;

			if ((SearchEngine.this.lastSeach != null) && stringToSearch.contains(SearchEngine.this.lastSeach)) {
				//affine le résultat actuel
				this.searchables = SearchEngine.this.lastResult;
			} else {
				//Sinon traite l'ensemble
				this.searchables = SearchEngine.this.searchables;
			}
		}

		/**
		 * Get the search state
		 * @return the search state
		 */
		public String getSearchState() {
			return this.searchState;
		}

		@Override
		public void run() {

			this.searchState = SearchEngine.STATE_RUNNING;
			String transformedStringToSearch = this.getTransformedString(this.stringToSearch);
			for (Searchable searchableToCheck : this.searchables) {
				if (this.canceled) {
					break;
				}
				String transformedStringToCheck = this.getTransformedString(searchableToCheck.getSearchableString());
				boolean match = this.isMatch(transformedStringToSearch, transformedStringToCheck);
				if (match) {
					//this.addDelta.add(searchableToCheck);
					this.result.add(searchableToCheck);
				}
			}

			if (!this.canceled) {
				this.searchState = SearchEngine.STATE_TERMINATED;
				SearchEngine.this.lastSeach = this.stringToSearch;
				SearchEngine.this.lastResult = this.result;
			} else {
				this.searchState = SearchEngine.STATE_CANCELED;
			}
			//			synchronized (this.addDelta) {
			//				this.addToGeneralResult();
			//			}
			this.notifieSearchListenersOfFullResult();

			synchronized (SearchEngine.this.waitingSearch) {
				if (!SearchEngine.this.waitingSearch.equals(SearchEngine.PAS_DE_RECHERCHE_EN_ATTENTE)) {
					//il  a une recherche en attente, on l'exécute
					SearchEngine.this.performSearch(SearchEngine.this.waitingSearch);
					//suppression de la recherche en attente
					SearchEngine.this.waitingSearch = SearchEngine.PAS_DE_RECHERCHE_EN_ATTENTE;
				}
			}
		}

		/**
		 * Check if the string to search match with the string to search
		 * @param theTransformedStringToSearch transformed string to search
		 * @param transformedStringToCheck transformed string to check (transformed representation of one of the Searchable)
		 * @return
		 */
		protected boolean isMatch(String theTransformedStringToSearch, String transformedStringToCheck) {
			StringTokenizer tokenizer = new StringTokenizer(theTransformedStringToSearch, StringTransformer.SPACE_STRING);
			boolean allFinded = true;
			tokenLoop: while (tokenizer.hasMoreElements()) {
				String partOfStringToSearch = tokenizer.nextToken();
				if (!transformedStringToCheck.contains(partOfStringToSearch)) {
					allFinded = false;
					break tokenLoop;
				}
			}
			return allFinded;
		}

		/**
		 * Get the transformed strings to search
		 * @return the string with all the transformer applied
		 */
		protected String getTransformedString(String stringToTransform) {
			String transformedString = stringToTransform;
			for (StringTransformer transformer : SearchEngine.this.transformers) {
				transformedString = transformer.processTransformation(transformedString);
			}
			return transformedString;
		}

		//		/**
		//		 * Add a delta to the general result list
		//		 * @param deltaResult the delta result
		//		 * @param notifieListeners if true, the listeners will be notified
		//		 */
		//		private void addToGeneralResult() {
		//			synchronized (this.addDelta) {
		//				synchronized (this.result) {
		//					this.result.addAll(this.addDelta);
		//					this.notifieSearchListenersOfAdd(new ArrayList<Searchable>(this.addDelta));
		//					this.addDelta.clear();
		//				}
		//			}
		//		}

		//		/**
		//		 * Remove a delta from the general result list
		//		 * @param deltaResult the delta result
		//		 * @param notifieListeners if true, the listeners will be notified
		//		 */
		//		protected void removeFromGeneralResult(ArrayList<Searchable> deltaResult) {
		//			synchronized (this.result) {
		//				this.result.removeAll(deltaResult);
		//				this.notifieSearchListenersOfRemove(deltaResult);
		//				deltaResult.clear();
		//			}
		//		}

		//		/**
		//		 * Affine the last search.
		//		 * @param newStringToSearch New String to search. old string must be include in the new Search
		//		 * @throws SearchException if the old string to search is not include in new string to search
		//		 */
		//		protected void askToRefineSearch(String newStringToSearch) throws SearchException {
		//			synchronized (this.stringToSearch) {
		//				synchronized (this.transformedStringToSearch) {
		//					if (!newStringToSearch.contains(this.stringToSearch)) {
		//						throw new SearchException("Unable to refine '" + this.stringToSearch + "' by '" + " '" + newStringToSearch + "'");
		//					}
		//					this.stringToSearch = newStringToSearch;
		//					this.transformedStringToSearch = this.getTransformedString(this.stringToSearch);
		//
		//					this.threadRefine = new ThreadRefine();
		//					this.threadRefine.start();
		//				}
		//			}
		//		}

		/**
		//		 * Notifie the listeners that some results were added
		//		 */
		//		private void notifieSearchListenersOfAdd(ArrayList<Searchable> deltaResult) {
		//			long endTime = new Date().getTime();
		//			for (SearchListener listener : SearchEngine.this.listeners) {
		//				listener.onSearchResultAdded(this.getSearchState(), deltaResult, endTime - this.startTime, this.firstResult);
		//			}
		//			if (this.firstResult) {
		//				this.firstResult = false;
		//			}
		//		}
		//
		//		/**
		//		 * Notifie the listeners that some results were added
		//		 */
		//		private void notifieSearchListenersOfRemove(ArrayList<Searchable> deltaResult) {
		//			long endTime = new Date().getTime();
		//			for (SearchListener listener : SearchEngine.this.listeners) {
		//				listener.onSearchResultRemoved(this.getSearchState(), deltaResult, endTime - this.startTime);
		//			}
		//			if (this.firstResult) {
		//				this.firstResult = false;
		//			}
		//		}

		/**
		 * Notifie the listener when the full result is enable
		 */
		private void notifieSearchListenersOfFullResult() {
			long endTime = new Date().getTime();
			for (SearchListener listener : SearchEngine.this.listeners) {
				listener.onFullResultAvailable(this.getSearchState(), new ArrayList<Searchable>(this.result), endTime - this.startTime);
			}
		}

		/**
		 * Cancel the current search
		 */
		public void cancelSearch() {
			this.canceled = true;

		}

		//		protected class ThreadRefine extends Thread {
		//
		//			@Override
		//			public void run() {
		//				//delete from results list the results that does not match anymore
		//				ArrayList<Searchable> searchablesToRemove = new ArrayList<Searchable>();
		//
		//				//remove obsolete results from results
		//				for (Searchable searchable : ThreadSearch.this.result) {
		//					String transformedStringToCheck = ThreadSearch.this.getTransformedString(searchable.getSearchableString());
		//					if (!ThreadSearch.this.isMatch(ThreadSearch.this.transformedStringToSearch, transformedStringToCheck)) {
		//						searchablesToRemove.add(searchable);
		//					}
		//				}
		//				System.out.println("Must be deleted by refine : " + searchablesToRemove.size()); // TODO delete this, only for debug
		//				ThreadSearch.this.removeFromGeneralResult(searchablesToRemove);
		//
		//				//				searchablesToRemove = new ArrayList<Searchable>();
		//				//				synchronized (ThreadSearch.this.addDelta) {
		//				//					//remove obsolete result from add delta
		//				//					for (Searchable searchable : ThreadSearch.this.addDelta) {
		//				//						String transformedStringToCheck = getTransformedString(searchable.getSearchableString());
		//				//						if (!isMatch(transformedStringToSearch, transformedStringToCheck)) {
		//				//							searchablesToRemove.add(searchable);
		//				//						}
		//				//					}
		//				//					ThreadSearch.this.addDelta.removeAll(searchablesToRemove);
		//				//				}
		//			}
		//		}
		//
		//	}
	}
}
