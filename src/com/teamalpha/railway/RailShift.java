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

	@Override
	protected RailJunction[] getRailDefiningJunctionPair(RailWay oldRail) {

		// Set the value of this variable to make the shift facing other direction
		boolean directionForward = true;

		// The Apex Junction is the entrance of the Shift, where the TrainElement/Axis can go into multiple directions
		RailJunction apexJunction = directionForward ? this.junctions[0] : this.junctions[1];
		RailJunction[] junctions = new RailJunction[2];

		if(oldRail == apexJunction.adjacentRail) {
			if(this.useAlternativeExit) {
				// Taking a turn
				junctions[0] = apexJunction;
				junctions[1] = this.alternativeExitJunction;
			}
			else {
				// Going straight
				return super.getRailDefiningJunctionPair(oldRail);
			}
		}
		// The TrainElement/Axis from an exit (the alteratnive junction end)
		else if (oldRail == alternativeExitJunction.adjacentRail) {
			// Joining a rail
			junctions[0] = apexJunction;
			junctions[1] = this.alternativeExitJunction;
		}
		// The TrainElement/Axis from an exit (the default other end)
		else {
			// Going straight
			return super.getRailDefiningJunctionPair(oldRail);
		}

		return junctions;
	}

}
