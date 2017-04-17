package com.teamalpha.railway;

import com.teamalpha.utils.Position;

/**
 * A p�lyaelem (RailWay) ezen kereszt�l �ri el a tengelyt (Axis).
 */
public interface RailDriven {
	public Position getPosition();					//Tengely poz�ci�j�nak lek�rdez�se
	public void setPosition(Position position);		//Poz�ci� be�ll�t�sa
	public RailWay getRail();						//Aktu�lis p�lyaelem lek�rdez�se
	public void setRail(RailWay rail);				//Aktu�lis p�lyaelem be�ll�t�sa
	public RailJunction getDestinationJunction();	//Aktu�lis c�l-csom�pont lek�rdez�se
	public void setDestinationJunction(RailJunction junction);	//Aktu�lis c�l-csom�pont be�ll�t�sa
}
