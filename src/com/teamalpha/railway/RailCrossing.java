package com.teamalpha.railway;

/**
 * Kereszts�n oszt�ly.
 *
 */
public class RailCrossing extends RailWay {

	//A kereszts�n m�sik k�t v�gpontja
	public RailJunction[] otherJunctions = new RailJunction[2];

	/********************************/

	public RailCrossing() {
	}
	public RailCrossing(String _id) {
		id = _id;
	}

}
