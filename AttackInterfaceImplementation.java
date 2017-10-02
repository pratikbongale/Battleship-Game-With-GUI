/*
 * AttackInterfaceImplementation.java
 *
 * Version:
 *      $Id AttackInterfaceImplementation.java, 1.0 2016/11/12 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import javax.swing.*;          //This is the final package name.
import java.awt.*;
import java.awt.event.*;

/**
 * AttackInterfaceImplementation implements the AttackInterface on client end.
 * It implements methods that are remotely invoked by server to get some data from client or send some data to client.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class AttackInterfaceImplementation 
	extends UnicastRemoteObject 
	implements AttackInterface {

		int minRow;
		int minCol;
		int maxRow;
		int maxCol;
		
		Scanner sc;
		Client client;

		public AttackInterfaceImplementation( int minR, int maxR, int minC, int maxC, Client c ) throws RemoteException {
			sc = new Scanner(System.in);
			minRow = minR;
			maxRow = maxR;
			minCol = minC;
			maxCol = maxC;
			client = c;

		}

		public Point getAttackPosition() throws RemoteException {

			try {
				// show a msg dialog asking for player to start
				client.accessToAttackGridAllowed = true;
				if(client.changeOfTurn)
					client.showMessage("Click a position on your Attack grid to indicate the opponents territory you intend to attack");
				
				// enable attack grid
				client.attackGridPane.setEnabled(true);

				// wait for click event
				while(!client.isAttackPosSet()) {
					try{ Thread.sleep(100); } catch(Exception e) { e.printStackTrace(); }
				}

				int row, col;
				Point xy;
				
				row = AttackGridListener.row;
				col = AttackGridListener.col;
				
				xy = new Point(row,col);
				client.accessToAttackGridAllowed = false;
				return xy;
			

			} catch (Exception e) {
				System.out.println(e.toString());
				System.exit(1);	
			}
		
			return null;		
		}

		public String sendMessage(String msg) throws java.rmi.RemoteException {
			System.out.print(msg);		

			if(msg.equals("hit") || msg.equals("miss")) {
				// color the grid with green(hit) or red(miss)
				client.setOutcomeColor( AttackGridListener.row, AttackGridListener.col, msg );
				AttackGridListener.row = -1;
				AttackGridListener.col = -1;	

				if(msg.equals("hit"))
					client.changeOfTurn = false;
				else if(msg.equals("miss"))
					client.changeOfTurn = true;
			}
			
					

			if(msg.endsWith("over")) {
				if(msg.startsWith("win")){
					client.showMessage("You win");	
				} else {
					client.showMessage("You lose");
				}
				
				client.dispose();
				client.notifySelf();
			}	
				

			return "Done";
			
		}

		public String showHitOnShip(int r, int c) throws java.rmi.RemoteException  {

			client.markHit(r,c);
			return "Done";
		}

		boolean checkBoundaries(Point p) {
			if( p.getRow() < minRow || p.getRow() > maxRow ||
					p.getCol() < minCol || p.getCol() > maxCol )
				return false;
			else
				return true;
		}	

}