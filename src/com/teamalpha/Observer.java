package com.teamalpha;

import com.teamalpha.game.Game;
import com.teamalpha.train.Train;

/**
 * Observer oszt�ly, a p�lya, �llapotok friss�t�s�t v�gzi
 */
public class Observer {

	//Minden tick-re megh�v�dik, friss�ti az �llapotokat
	public void update(Board board, Game game) {
		//Minden vonatot update-el�nk
		for(Train train : board.trains) {
			
			train.update(game);
			
		}
	}
	
}
