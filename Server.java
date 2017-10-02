/*
 * Server.java
 *
 * Version:
 *      $Id Server.java, 1.0 2016/11/19 18:35:03 $
 *
 * Revisions:
 *      $Log$
 */

import java.rmi.*;
import java.net.*;

/**
 * Server is a thread that waits for client requests and creates seperate threads for each client.
 * The main game is played on the server. It manages all the communication between two connected players.
 * 
 * @author      Pratik S Bongale
 * @author      Shardul P Dabholkar
 */

class Server extends Thread {
	
	final static int maxNumberOfPlayers = 2;
	AddPlayerToGame rmiObj;
	Player playerArray[] = null;


	Server() {
	
		try {
				rmiObj = new AddPlayerToGameImplementation();    
            	Naming.rebind("//localhost/IamABattleshipServer", rmiObj);
            	System.out.println("IamABattleshipServer bound in registry");
		
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
           	System.exit(1);	
		}

	}

	public void run() {
		
		// check if both clients connected and decide who plays first and wait for co-ordinates
		while( AddPlayerToGameImplementation.addCounter < 2 ) {
		
			try{ sleep(10000); } catch(Exception e) { e.printStackTrace(); }
			System.out.println("Waiting for players to initialize their battlefield.");		// do nothing until both players have initiallized
			System.out.println("players added as of now " + AddPlayerToGameImplementation.addCounter);
			
		}

		System.out.println("Both players ready, notifying players to begin ");
		try{ sleep(100); } catch(Exception e) { e.printStackTrace(); }
		// both players are ready to play	

		try {
			playerArray = rmiObj.getPlayers();
		} catch(Exception e) {
			e.printStackTrace();
		}

		gameLogic();
		// we need to notify the waiting clients here
		System.out.println("Server thread ends");
	}

	void gameLogic() {
		// take co ordinates from client
		// check and notify
		// if hit client sends other co ordinates
		// else client waits

		try {
			
			// ask player 1 to give input
			Player player = playerArray[0];
			Player opponent = playerArray[1];
			AttackInterface playerRemoteObject = (AttackInterface)Naming.lookup("//localhost/IamABattleshipPlayer"+player.getName());
			AttackInterface opponentRemoteObject = (AttackInterface)Naming.lookup("//localhost/IamABattleshipPlayer"+opponent.getName());

			Player temp;
			AttackInterface tempInterface;
			String attackOutcome = "";
			
			temp = player;
			tempInterface = playerRemoteObject;
			while( opponent.fleet.isAfloat() ) {				// check if even a single ship is afloat
				Point p = playerRemoteObject.getAttackPosition();
				
				if( p.getRow() != -1 && p.getCol() != -1 ) {
					attackOutcome = opponent.attack( p.getRow(), p.getCol() );	
				} else {
					attackOutcome = "";
				}

				if( attackOutcome == "miss" ) {			// if the attack is a miss, swap references of player and its opponent
					player.ocean.mark( p.getRow(), p.getCol(), "miss");	// mark on the players hit miss grid about the miss
					
					playerRemoteObject.sendMessage("miss");

					// swap references
					temp = player;
					player = opponent;
					opponent = temp;

					tempInterface = playerRemoteObject;
					playerRemoteObject = opponentRemoteObject;
					opponentRemoteObject = tempInterface;
					
				} 

				if( attackOutcome == "hit" ) {		
					player.ocean.mark( p.getRow(), p.getCol(), "hit");  // mark hit in the grid
					temp = player;
					playerRemoteObject.sendMessage("hit");
					opponentRemoteObject.showHitOnShip(p.getRow(), p.getCol());
					//displayPersonalBoard(player,playerRemoteObject);

				}			 
			
			}

			playerRemoteObject.sendMessage("win-over");
			opponentRemoteObject.sendMessage("lose-over");
			//if(opponent.fleet.isAfloat() == false) {
				//playerRemoteObject.sendMessage("\n\n\n\n * * * * Game over for " + opponent.getName() + " * * * *\n");
				//playerRemoteObject.sendMessage("* * * * Player " + player.getName() +  " wins the game * * * *\n");
				//playerRemoteObject.sendMessage("Game Over");

				//opponentRemoteObject.sendMessage("\n\n\n\n * * * * Game over for " + opponent.getName() + " * * * *\n");
				//opponentRemoteObject.sendMessage("* * * * Player " + player.getName() +  " wins the game * * * *\n");
				//opponentRemoteObject.sendMessage("Game Over");
				
				
			//}
			
						
		} catch (Exception e) {
			System.out.println("get psoitioins exception : " + e.getMessage());
			e.printStackTrace();
		}

	}

	void displayPersonalBoard(Player p, AttackInterface remote) throws RemoteException {
		int row,col;
		
		remote.sendMessage("======= Strategy Board for " + p.getName() + " =======\n");

		remote.sendMessage( "R 0 1 2 3 4 5 6 7 8 9 \n" );
		for( row = p.ocean.minRow; row <= p.ocean.maxRow; row++ ) {
			remote.sendMessage(row + " ");
			for ( col = p.ocean.minCol; col <= p.ocean.maxCol; col++ ) {
				remote.sendMessage( p.ocean.attackGrid[row][col] + " ");
			}
			remote.sendMessage("\n");
		}

		remote.sendMessage("\n");
	}

	
}