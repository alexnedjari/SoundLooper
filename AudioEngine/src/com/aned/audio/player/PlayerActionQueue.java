/**
 *
 */
package com.aned.audio.player;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * AudioEngine is an audio engine based on FMOD
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
 * @author ANEDJARI
 *
 */
public final class PlayerActionQueue {

	/**
	 * The thread used to execute actions
	 */
	private PlayerActionQueueThread thread = new PlayerActionQueueThread();

	/**
	 * Logger for this class
	 */
	protected static final Logger LOGGER = Logger.getLogger(PlayerActionQueue.class);

	/**
	 * Private constructor to
	 * prevent external instanciation
	 */
	private PlayerActionQueue() {
		super();
		this.thread.start();
	}

	/**
	 * get the instance
	 * @return the instance
	 */
	static PlayerActionQueue getNewInstance() {
		return new PlayerActionQueue();
	}

	/**
	 * The list of actions to do
	 */
	protected ArrayList<PlayerAction> actionsToDo = new ArrayList<PlayerAction>();

	/**
	 * Add an Action to do when queue will be empty
	 * @param action the action to add
	 */
	public void addAction(PlayerAction action) {
		synchronized (this.actionsToDo) {
			this.actionsToDo.add(action);
			this.thread.interrupt();
		}
	}

	/**
	 * Stop the queue
	 */
	public void killProcess() {
		this.thread.askExitFlag();
		this.thread.interrupt();
	}

	/**
	 * Class treating the queue
	 * @author ANEDJARI
	 *
	 */
	public class PlayerActionQueueThread extends Thread {

		/**
		 * to exit the thread
		 */
		private boolean exit = false;

		@Override
		public void run() {
			while (!this.exit) {
				try {
					PlayerActionQueue.LOGGER.info("The action queue start waiting for actions");
					Thread.sleep(1000000);
					//Thread.sleep(10);
					PlayerActionQueue.LOGGER.info("End waiting");
				} catch (InterruptedException e) {
					//nothing to do
					PlayerActionQueue.LOGGER.info("The action queue wait is interrupted");
				}
				this.threatQueue();
			}
		}

		/**
		 * Put the exist flag to true
		 */
		public void askExitFlag() {
			this.exit = true;
		}

		/**
		 * Threat all the elements of the queue
		 */
		public void threatQueue() {
			PlayerActionQueue.LOGGER.info("ThreatQueue must treat " + PlayerActionQueue.this.actionsToDo.size() + " actions");
			while (PlayerActionQueue.this.actionsToDo.size() > 0) {
				PlayerActionQueue.LOGGER.info("Start " + PlayerActionQueue.this.actionsToDo.get(0).getClass().getSimpleName());
				try {
					synchronized (PlayerActionQueue.this.actionsToDo) {
						PlayerActionQueue.this.actionsToDo.get(0).runAndDeallocate();
					}
				} finally {
					PlayerActionQueue.LOGGER.info("Fin de l'action " + PlayerActionQueue.this.actionsToDo.get(0).getClass().getSimpleName());
					synchronized (PlayerActionQueue.this.actionsToDo) {
						PlayerAction actionToRemove = PlayerActionQueue.this.actionsToDo.get(0);
						PlayerActionQueue.LOGGER.info("Avant remove " + PlayerActionQueue.this.actionsToDo.size() + " : " + actionToRemove);
						Iterator<PlayerAction> iterator = PlayerActionQueue.this.actionsToDo.iterator();
						iterator.next();
						iterator.remove();
						PlayerActionQueue.LOGGER.info("Après remove" + " : " + actionToRemove);
					}
				}
			}
			PlayerActionQueue.LOGGER.info("ThreatQueue end of method");
		}
		//		public void threatQueue() {
		//			LOGGER.info("ThreatQueue must treat " + PlayerActionQueue.this.actionsToDo.size() + " actions");
		//			Iterator<PlayerAction> iterator = PlayerActionQueue.this.actionsToDo.iterator();
		//			while (true) {
		//				LOGGER.info("Start " + PlayerActionQueue.this.actionsToDo.get(0).getClass().getSimpleName());
		//				while (!iterator.hasNext()) {
		//					try {
		//						Thread.sleep(10);
		//					} catch (InterruptedException e) {
		//						e.printStackTrace();
		//					}
		//				}
		//				
		//				synchronized (PlayerActionQueue.this.actionsToDo) {
		//					iterator.
		//					PlayerAction next = iterator.next();
		//					next.runAndDeallocate();
		//				}
		//			}
		//		}
	}
}
