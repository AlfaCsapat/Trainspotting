package com.teamalpha.train.element;

import com.teamalpha.utils.ColorHelper;
import com.teamalpha.railway.Station;
import com.teamalpha.train.Train;

/**
 * Egy kocsit (vagont) le�r� oszt�ly.
 */
public class PassangerWagon extends TrainElement {
	public ColorHelper color;	//A kocsi sz�ne
	public int passangers;	//A kocsiban utaz� utasok sz�ma
	
	/*****************************/
	
	@Override
	public boolean interactStation(Station station, boolean canGetOff, Train train, int wagonId) {
		
		//Ha az �llom�s �s a kocsi sz�ne nem egyezik meg, de vannak utasok a kocsin, akkor a t�bbi
		//kocsir�l m�r nem sz�llhatnak le utasok
		if (!station.color.equals(color) && passangers > 0)
			return false;
		
		//ha az �llom�s �s kocsi sz�ne egyezik
		if (station.color.equals(color)) {
			
			//Ha a kocsi �res, az �llom�sr�l felsz�llnak az utasok
			if (passangers == 0) {
				if (station.passangers > 0) {
					System.out.println("\tPassangers on station "+station.id+"changed to 0");
					System.out.println("\tPassangers on train "+train.id+" wagon "+wagonId+" changed to "+station.passangers);
				}
				passangers = station.passangers;
				station.passangers = 0;
			}
			//Ha nem �res, �s szabad a lesz�ll�s, akkor lesz�llnak az utasok
			else if (canGetOff) {
				System.out.println("\tPassangers on train "+train.id+" wagon "+wagonId+" changed to 0");
				passangers = 0;
			}
			
		}
		
		return canGetOff;
	}
	
}
