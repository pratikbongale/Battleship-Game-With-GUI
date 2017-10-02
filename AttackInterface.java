/*
 * AttackInterface.java
 *
 * Version:
 *      $Id AttackInterface.java, 1.0 2016/11/12 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.util.*;

/**
 * AttackInterface is an interface known to server as well as client.
 * It declares methods that are remotely invoked by the thread acting as server to get some data from client or send some data to client.
 * This is implemented by server component of the program to be invoked remotely by a client
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

interface AttackInterface extends java.rmi.Remote {
	public Point getAttackPosition() throws java.rmi.RemoteException;
	public String sendMessage(String msg) throws java.rmi.RemoteException;
	public String showHitOnShip(int r, int c) throws java.rmi.RemoteException;
}