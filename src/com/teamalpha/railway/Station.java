package com.teamalpha.railway;

import com.teamalpha.game.GameColor;
import com.teamalpha.train.Train;

/**
 * Egy �llom�s p�lyaelemet le�r� oszt�ly.
 */
public class Station extends RailWay {

	public GameColor color;
	public int passangers;
	
	/********************************************/
	
	public Station() {
	}
	public Station(String _id) {
		id = _id;
	}
	
	@Override
	public void interact(Train train) {
		catchPassangers(train);
	}
	
	/**
	 * A megadott vonat utasainak lesz�ll�t�s�t kezel�/v�gz� met�dus.
	 * @param train	A vonat melyen el kell v�gezni az utasok lesz�ll�t�s�t.
	 */
	private void catchPassangers(Train train) {
		train.onArrivedToStation(this);
	}
	
}
