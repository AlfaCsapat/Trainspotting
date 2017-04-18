package com.teamalpha.train.element;

import com.teamalpha.utils.ColorHelper;
import com.teamalpha.railway.Station;
import com.teamalpha.train.Train;

import java.awt.*;

/**
 * Egy kocsit (vagont) le�r� oszt�ly.
 */
public class PassangerWagon extends TrainElement {

	public Integer color;	//A kocsi színe

	public int passangers;	//A kocsiban utaz� utasok sz�ma
	
	/*****************************/

	public PassangerWagon() {
	}
	public PassangerWagon(int _color, int _passangers) {
		color = _color;
		passangers = _passangers;
	}



	private static final boolean PASSANGERS_GETOFF_MODE = true;

	@Override
	public boolean interactStation(Station station, boolean canGetOff, Train train, int wagonId) {

		//Attól függően hogy melyik módszert alkalmazzuk a le/felszálláshoz
		if (PassangerWagon.PASSANGERS_GETOFF_MODE) {

			//Ha az állomás és a kocsi színe nem egyezik meg, de vannak utasok a kocsin, akkor a többi
			//kocsiról már nem szállhatnak le utasok
			if (!station.color.equals(color) && passangers > 0)
				return false;

			//ha az állomás és kocsi színe egyezik
			if (station.color.equals(color)) {

				//Ha a kocsi üres, az állomásról felszállnak az utasok
				if (passangers == 0) {
					if (station.passangers > 0) {
						System.out.println("\tPassangers on station " + station.id + " changed to 0");
						System.out.println("\tPassangers on train " + train.id + " wagon " + wagonId +
								" changed to " + station.passangers);
					}
					passangers = station.passangers;
					station.passangers = 0;
				}
				//Ha nem üres, és szabad a leszállás, akkor leszállnak az utasok
				else if (canGetOff) {
					System.out.println("\tPassangers on train " + train.id + " wagon " + wagonId + " changed to 0");
					passangers = 0;
				}

			}
		}
		//Másik módszer
		else {

			//Ha az állomás és a kocsi színe nem egyezik meg, de vannak utasok a kocsin, akkor a többi
			//kocsiról már nem szállhatnak le utasok
			if (!station.color.equals(color) && passangers > 0)
				return false;

			if (station.color.equals(color)) {

				//Ha szabad leszállni és vannak utasok, akkor azok leszállnak
				if (canGetOff && passangers > 0) {
					System.out.println("\tPassangers on train " + train.id + " wagon " + wagonId + " changed to 0");
					passangers = 0;
				}

				//Ha az állomáson vannak utasok, azok felszállnak
				if (station.passangers > 0) {
					System.out.println("\tPassangers on station " + station.id + " changed to 0");
					System.out.println("\tPassangers on train " + train.id + " wagon " + wagonId +
							" changed to " + station.passangers);
					passangers += station.passangers;
					station.passangers = 0;
				}

			}

		}

		return canGetOff;
	}
	
}
