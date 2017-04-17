package com.teamalpha.train.element;

import com.teamalpha.RailChangeListener;
import com.teamalpha.RailWay;
import com.teamalpha.Station;
import com.teamalpha.train.Train;

/**
 * Egy vonatelemet le�r� oszt�ly.
 */
public class TrainElement implements RailChangeListener {

	public static float length = 20.0f;			//Egy vonatelem hossza
	
	public TrainElement followingTrainElement = null;	//A vonatelemhez kapcsol�d� k�vetkez� vonatelem
	public Axis frontAxis;						//A vonatelem els� tengelye
	public Axis rearAxis;						//A vonatelem h�ts� tengelye
	
	/********************************************/
	
	public TrainElement() {
	}
	
	/**
	 * A vonatelem mozgat�s�t v�gz� met�dus
	 * @param travelDistance	A t�vols�g, amennyit a vonatelem mozogni tud/akar
	 */
	public void move(float travelDistance) {
		frontAxis.move(travelDistance);
		rearAxis.move(travelDistance);
		//Ha van k�vetkez� vonatelem, akkor annak mozgat�sa
		if (followingTrainElement != null)
			followingTrainElement.move(travelDistance);

		//System.out.println("Front axis: ("+frontAxis.position.x+", "+frontAxis.position.y+")");
		//System.out.println("Rear axis: ("+rearAxis.position.x+", "+rearAxis.position.y+")");
	}
	
	/**
	 * Megadja, hogy a vonatelem �tk�zik-e egy m�sik vonatelemmel.
	 * @param trainElement	A vonatelem, mellyel az �tk�z�s detekt�l�st v�gre kell hajtani.
	 * @return	true, ha �tk�z�s detekt�lva lett, false, ha nem.
	 */
	public boolean intersectsTrainElement(TrainElement trainElement) {
		///TO-DO
		return false;
	}

	/**
	 * �llom�shoz �rkez�skor a vonat ezzel sz�l a kocsinak
	 * @param station		az �llom�s melyhez a vonat �rkezett
	 * @param canGetOff		megadja, hogy amennyiben egy�b felt�telek teljes�lnek, lesz�llhatnak-e az utasok a kocsir�l
	 * @return				megadja, hogy az ezt k�vet� kocsikr�l lesz�llhatnak-e majd az utasok
	 */
	public boolean interactStation(Station station, boolean canGetOff, Train train, int wagonId) {
		return canGetOff;
	}
	
	@Override
	public void onRailChanged(Axis axis, RailWay oldRail, RailWay newRail) {
		
		if (axis == this.frontAxis) {
			newRail.registerPassingTrainElement(this);
		}
		else if (axis == this.rearAxis) {
			oldRail.unregisterPassingTrainElement(this);
		}
		
	}
	
}
