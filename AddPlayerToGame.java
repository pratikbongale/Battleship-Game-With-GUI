/*
 * AddPlayerToGame.java
 *
 * Version:
 *      $Id AddPlayerToGame.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

/**
 * AddPlayerToGame is interface known to both server and clients. 
 * It declares methods to add a player object in the game. 
 * This is implemented by server component of the program to be invoked remotely by a client
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

public interface AddPlayerToGame extends java.rmi.Remote {
	String addPlayer(Player p) throws java.rmi.RemoteException;
	Player[] getPlayers() throws java.rmi.RemoteException;

}