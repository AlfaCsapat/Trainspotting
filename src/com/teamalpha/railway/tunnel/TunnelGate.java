package com.teamalpha.railway.tunnel;

import com.teamalpha.railway.RailJunction;
import com.teamalpha.railway.RailWay;

/**
 * Alag�tbej�ratot le�r� oszt�ly.
 */
public class TunnelGate extends RailWay {

	public boolean open = false;		//Megadja, hogy az alag�tbej�rat nyitva van-e (true) avagy nem (false)

	/********************************************/
	
	public TunnelGate() {
	}
	public TunnelGate(String _id) {
		id = _id;
	}
	
	/**
	 * Az alag�tbej�rat �s az alag�t k�zti csom�pont lek�rdez�se
	 * @return	Az alag�tbej�rat �s az alag�t k�zti csom�pont
	 */
	public RailJunction getTunnelJunction() {
		return junctions[1];
	}
	
}
