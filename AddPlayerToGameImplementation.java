/*
 * AddPlayerToGameImplementation.java
 *
 * Version:
 *      $Id AddPlayerToGameImplementation.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * AddPlayerToGameImplementation is implemented on the server end and holds methods invoked by client remotely
 * to add its fully initialized player to the game so that the server can begin the game.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class AddPlayerToGameImplementation 
	extends UnicastRemoteObject 
	implements AddPlayerToGame {
		
		static int addCounter = 0;
		Player player[];

		public AddPlayerToGameImplementation() throws RemoteException {
			player = new Player[2];
		}

		public String addPlayer(Player p) throws RemoteException {
			player[addCounter++] = p;
			return "You are in!";
		}

		public Player[] getPlayers() throws RemoteException {
			return player;
		}
}