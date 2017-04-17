package com.teamalpha.train.element;

import com.teamalpha.*;

/**
 * Egy vonatelem egy tengelye.
 */
public class Axis implements RailDriven {

	public Position position = new Position();	//A tengely poz�ci�ja a p�ly�n
	public RailWay rail;						//A tengely e p�lyaelemen van
	public RailJunction destinationJunction;	//C�l-csom�pont, amerre a tengely halad
	public RailChangeListener listener;
	
	/********************************************/
	
	public Axis() {
	}
	public Axis(RailChangeListener _listener) {
		listener = _listener;
	}
	
	/**
	 * A tengely mozgat�sa
	 * @param travelDistance	A t�vols�g, amennyit a tengely mozog
	 */
	public void move(float travelDistance) {
		
		while(travelDistance > 0) {

			float actualTravel = rail.driveElement(this, travelDistance);
			travelDistance -= actualTravel;
			
		}
		
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void setPosition(Position _position) {
		position = _position;
	}

	@Override
	public RailWay getRail() {
		return rail;
	}

	@Override
	public void setRail(RailWay newRail) {
		RailWay oldRail = rail;
		rail = newRail;
		listener.onRailChanged(this, oldRail, newRail);
	}

	@Override
	public RailJunction getDestinationJunction() {
		return destinationJunction;
	}

	@Override
	public void setDestinationJunction(RailJunction junction) {
		destinationJunction = junction;
	}
	
}
