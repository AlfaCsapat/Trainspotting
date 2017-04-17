package com.teamalpha.railway;

import com.teamalpha.train.element.Axis;

/**
 * A tengelyek (Axis) �s a vonatelem (TrainElement) k�z�tt biztos�t kapcsolatot. A
 * tengely sz�lhat a hozz� tartoz� vonatelemnek, ha a tengely m�sik p�lyaelemre �rt.
 */
public interface RailChangeListener {

	/**
	 * Ezt h�vja a tengely ha m�sik p�lyaelemre �rkezett.
	 * @param axis		A h�v� tengely.
	 * @param oldRail	A p�lyaelem, melyen kor�bban a tengely volt.
	 * @param newRail	A p�lyaelem, melyen most a tengely van.
	 */
	public void onRailChanged(Axis axis, RailWay oldRail, RailWay newRail);
	
}
