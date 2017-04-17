package com.teamalpha.railway;

/**
 * Egy v�lt� p�lyaelemet le�r� oszt�ly.
 */
public class RailShift extends RailWay {

	public boolean useAlternativeExit;				//true eset�n a v�lt� alternativeExitJunction fel� tov�bb�tja
													//a be�rkez� vonatelemeket
	public RailJunction alternativeExitJunction;	//a v�lt� alternat�v kimeneti csom�pontja.
	
	/********************************************/
	
	public RailShift() {	
	}
	public RailShift(String _id) {
		id = _id;
	}
	
	public boolean switchIt() {
		//Ha van vonat a v�lt�n, akkor nem v�lthatunk
		if (passingTrainElements.size() > 0) {
			System.out.println("There�s a train on the shift.");
			return false;
		}
		useAlternativeExit = !useAlternativeExit;
		System.out.println("Shift switched.");
		return true;
	}
	
	@Override
	public float driveElement(RailDriven element, float distance) {
		return 0;
	}
	
}
