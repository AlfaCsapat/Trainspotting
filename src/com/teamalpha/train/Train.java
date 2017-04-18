package com.teamalpha.train;
import com.teamalpha.game.Game;
import com.teamalpha.railway.RailWay;
import com.teamalpha.railway.Station;
import com.teamalpha.train.element.Locomotive;
import com.teamalpha.train.element.LocomotiveDelegate;
import com.teamalpha.train.element.TrainElement;

import java.util.ArrayList;

/**
 * Egy vonatot le�r� osz�ly.
 */
public class Train implements LocomotiveDelegate {

	public Locomotive locomotive;		//A vonathoz tartoz� mozdony
	public ArrayList<TrainElement> wagons = new ArrayList<TrainElement>();		//A vonathoz tartoz� kocsik (vagonok)
	public float speed;					//A vonat sebess�ge
	
	public String id;
	
	/********************************************/
	
	public Train() {	
	}
	public Train(String _id, float _speed) {
		id = _id;
		speed = _speed;
	}
	
	/**
	 * A vonat friss�t�s�t v�gz� met�dus.
	 * @param game	A j�t�kkezel� Game objektum referenci�ja.
	 */
	public void update(Game game) {
		move();
	}
	
	/**
	 * A vonat mozgat�s�t kezel�/v�gz� met�dus.
	 */
	public void move() {
		float travelDistance = getTravelDistance();
		locomotive.move(travelDistance);
	}
	
	/**
	 * Megadja a t�vols�got, amennyit a vonat mozogni tud/akar.
	 * @return	A t�vols�g, amennyit a vonat mozogni tud/akar.
	 */
	public float getTravelDistance() {
		return speed;
	}
	
	/**
	 * �tk�z�sek detekt�l�s�t v�gz� met�dus
	 */
	public void traceCollision() {
		///TO-DO
	}

	@Override
	public void onRailEntered(RailWay rail) {
		System.out.println("\tTrain "+id+" stepped on rail "+rail.id+".");
		rail.interact(this);
	}
	
	public void onArrivedToStation(Station station) {
			//Minden vagonnak sz�lunk hogy �llom�shoz �rt a vonat
			boolean canGetOff = true; //Jelzi, hogy szabad-e m�g lesz�llni vagonokr�l
			for(int i=0; i<wagons.size(); ++i) {
				canGetOff = wagons.get(i).interactStation(station, canGetOff, this, i+1);
			}
	}
	@Override
	public void derailed() {
		System.out.println("\tTrain "+id+" derailed.");
		Game.notifyLose();
	}
	
}
