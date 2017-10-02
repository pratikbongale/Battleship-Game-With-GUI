/*
 * TurnOnServer.java
 *
 * Version:
 *      $Id TurnOnServer.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * TurnOnServer is the main class to be invoked in order to start the server.
 * It creates the server object which is registered to RMI registry.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class TurnOnServer {
	public static void main(String args[]) {

		
		Server s = new Server();
		s.start();
		System.out.println("Ending main thread server");
	
	}
}