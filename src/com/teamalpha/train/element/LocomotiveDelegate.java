package com.teamalpha.train.element;

import com.teamalpha.RailWay;

/**
 * A mozdony (Locomotive) �s a hozz� tartoz� vonat (Train) k�z�tti
 * kapcsolatot biztos�t� interf�sz.
 */
public interface LocomotiveDelegate {

	/**
	 * Akkor h�v�dik meg, ha a mozdony �j p�lyaelemre �rkezett.
	 * @param rail	Az �j p�lyaelem.
	 */
	public void onRailEntered(RailWay rail);
	public void derailed();
	
}
