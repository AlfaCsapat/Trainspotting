package com.teamalpha.train.element;

import com.teamalpha.RailWay;

/**
 * Egy mozdonyt le�r� oszt�ly.
 */
public class Locomotive extends TrainElement {
	
	public LocomotiveDelegate delegate;	//A vonat (melynek a mozdony r�sze) el�r�se ezen kereszt�l t�rt�nik

	/********************************************/
	
	public Locomotive() {
	}
	
	/**
	 * Megvizsg�lja, hogy a mozdony �tk�z�tt-e egy m�sik vonatelemmel.
	 * @return	�tk�z�s est�n true, egy�bk�nt false.
	 */
	public boolean checkCollision() {
		///TO-DO
		return false;
	}
	
	@Override
	public void onRailChanged(Axis axis, RailWay oldRail, RailWay newRail) {

		if (axis == this.frontAxis) {
			//Ha a mozdony null s�nre l�pett akkor a vonat kisiklott
			if (newRail == null)
				delegate.derailed();
			else {
				newRail.registerPassingTrainElement(this);
				delegate.onRailEntered(newRail);
			}
		}
		else if (axis == this.rearAxis) {
			oldRail.unregisterPassingTrainElement(this);
		}
		
	}
	
}
