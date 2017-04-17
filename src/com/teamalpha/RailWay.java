package com.teamalpha;
import com.teamalpha.train.Train;
import com.teamalpha.train.element.TrainElement;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * P�lyaelemet le�r� oszt�ly (�soszt�lyk�nt), ill. a s�n p�lyaelemet le�r� oszt�ly (�nmag�ban).
 */
public class RailWay {

	public String id = "";										//P�lyaelem egyedi ID-je
	public RailJunction[] junctions = new RailJunction[2];	//A p�lyaelem k�t v�g-csom�pontja
	public ArrayList<TrainElement> passingTrainElements = new ArrayList<TrainElement>();	//A p�lyaelemen halad� vonatelemek list�ja
	
	/********************************************/
	
	public RailWay() {
	}
	public RailWay(String _id) {
		id = _id;
	}
	
	/**
	 * Inicializ�l�skor a Board h�vja meg, ez �ll�tja be a p�lyaelem-szomsz�doss�gokat a csom�pontok seg�ts�g�vel
	 * @param rails		A Board-t�l kapott ID-p�lyaelem map
	 */
	public void setAdjacentRails(HashMap<String,RailWay> rails) {
		if (junctions[0] != null) junctions[0].setAdjacentRail(rails);
		if (junctions[1] != null) junctions[1].setAdjacentRail(rails);
	}
	
	/**
	 * Egy RailDriven elem mozgat�s�t kezel� met�dus.
	 * @param element	A mozogni k�v�n� RailDriven elem.
	 * @param distance	A t�vols�g, amennyit az elem mozogni k�v�n.
	 * @return
	 */
	public float driveElement(RailDriven element, float travelDistance) {
		
		RailJunction destinationJunction = element.getDestinationJunction();
		Position position = element.getPosition();
		
		float destinationJunctionDistance = getJunctionDistance(position, destinationJunction);
		
		float actualTravel = 0;
		
		//Ha a c�lpont t�volabb van, mint amennyit mozogni akar a tengely
		if (destinationJunctionDistance > travelDistance) {
			//akkor mozgatjuk a c�lpont fel�
			moveTowardsJunction(element, travelDistance);
			//�s annyit mozgott amennyit akart
			actualTravel = travelDistance;
		}
		//Ha a c�lpont k�zelebb van a mozg�s t�vols�g�n�l
		else {
			//A tengelyt a c�lpontba mozgatjuk
			actualTravel = moveToJunction(element);

			//A tengely c�lpontj�t �t�ll�tjuk
			//ha nincs k�vetkez� s�n, akkor a vonat kisiklott
			if (destinationJunction.adjacentRail == null) {
				element.setRail(null); //ezzel tudjuk jelezni hogy a vonat kisiklott
				return 0;
			}
			destinationJunction.enterElement(element, this);
			
		}
		
		return actualTravel;
	}
	
	/**
	 * Megadja egy adott pont t�vols�g�t egy csom�pontt�l.
	 * @param position	A pont poz�ci�ja.
	 * @param junction	A csom�pont.
	 * @return
	 */
	private float getJunctionDistance(Position position, RailJunction junction) {
		return (float)Math.sqrt(  (junction.position.x-position.x)*(junction.position.x-position.x) +
								  (junction.position.y-position.y)*(junction.position.y-position.y));
	}
	
	/**
	 * Egy RailDriven elemet mozgat a megfelel� csom�pont fel�.
	 * @param element	A mozgatand� RailDriven elem.
	 * @param distance	A t�vols�g, amit az element mozogni k�v�n.
	 */
	//Ez t�nyleg void?
	private void moveTowardsJunction(RailDriven element, float distance) {
		
		RailJunction junction = element.getDestinationJunction();
		
		Position v = new Position(
				junction.position.x - element.getPosition().x,
				junction.position.y - element.getPosition().y
		);
		float vd = (float)Math.sqrt( v.x*v.x + v.y*v.y );
		v.x = v.x/vd;
		v.y = v.y/vd;
		
		element.setPosition(new Position(
				element.getPosition().x + v.x*distance,
				element.getPosition().y + v.y*distance
		));
		
	}
	
	/**
	 * Egy RailDriven elemet a megfelel� c�l-csom�pontba mozgat.
	 * @param element	A mozgatand� RailDriven elem.
	 * @return
	 */
	private float moveToJunction(RailDriven element) {
		
		float distance = getJunctionDistance(element.getPosition(), element.getDestinationJunction());
		element.setPosition(new Position(element.getDestinationJunction().position.x,
										 element.getDestinationJunction().position.y));
		return distance;
		
	}
	
	/**
	 * Akkor h�v�dik, amikor egy RailDriven elem r�l�pett a p�lyaelemre
	 * @param element		A RailDriven elem, mely a p�lyaelemre l�pett.
	 * @param fromJunction	A csom�pont, amely fel�l a RailDriven elem �rkezett.
	 * @param oldRail		A s�n, melyen a tengely (element) kor�bban haladt
	 */
	public void enterElement(RailDriven element, RailWay oldRail) {
		//Be�ll�tjuk a tengely �j c�lpontj�t
		element.setDestinationJunction(getExitJunction(oldRail));
		//Be�ll�tjuk hogy a tengely mely s�nen halad mostant�l
		element.setRail(this);
	}
	
	/**
	 * Megadja a p�lyaelem azon v�g-csom�pontj�t, mely nem egyenl� az entranceJunction csom�ponttal.
	 * @param entranceJunction	Ez alapj�n lehet eld�nteni, hogy mely csom�pontot adja vissza a f�ggv�ny.
	 * @return
	 */
	private RailJunction getExitJunction(RailWay oldRail) {
		System.out.println("ID="+id);
		//Ha a kapott s�nt a junction[0] csom�ponton �t �rj�k el, akkor a junction[1] lesz a c�l
		if (junctions[0].adjacentRail == oldRail)
			return junctions[1];
		//-"- csak ford�tva
		else if (junctions[1].adjacentRail == oldRail)
			return junctions[0];
		else
		{
			///ha ide jutunk az HIBA
			System.out.println("getExitJunction error");
			return null;
		}
	}
	
	/**
	 * Akkor h�v�dik, ha egy vonatelem r�l�p a p�lyaelemre. Ekkor a vonatelem elt�rol�dik a p�lyaelem
	 * passingTrainElements list�j�ban ameddig a vonatelem le nem l�p a p�lyaelemr�l.
	 * @param trainElement	A regisztr�land� vonatelem
	 */
	public void registerPassingTrainElement(TrainElement trainElement) {
		passingTrainElements.add(trainElement);
	}
	
	/**
	 * T�rli a megadott vonatelemet a passingTrainElements list�b�l.
	 * @param trainElement	Az elt�vol�tand� vonatelem
	 */
	public void unregisterPassingTrainElement(TrainElement trainElement) {
		passingTrainElements.remove(trainElement);
	}
	
	/**
	 * A p�lyaelem �s egy vonat k�zti interakci�t kezel� met�dus.
	 * @param train		A vonat, mellyel a p�lyaelem interakci�ba l�p.
	 */
	public void interact(Train train) {
		///TO-DO
	}
	
}
