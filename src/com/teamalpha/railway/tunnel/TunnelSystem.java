package com.teamalpha.railway.tunnel;
import com.teamalpha.railway.RailJunction;
import com.teamalpha.railway.RailWay;
import com.teamalpha.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Az alag�trendszert kezel� oszt�ly, mely a terepasztalon l�v� alag�tbej�ratok (TunnelGate) �s
 * az alag�t (RailTunnel) menedzsel�s�t v�gzi.
 */
public class TunnelSystem {
	
	public ArrayList<TunnelGate> gates = new ArrayList<TunnelGate>();		//A terepasztalon l�v� minden alag�tbej�rat
	public ArrayList<TunnelGate> activeGates = new ArrayList<TunnelGate>();	//A nyitott alag�tbej�ratok
	public RailTunnel activeTunnel = null;				//Az akt�v alag�t (amennyiben van ilyen, k�l�nben null)
	
	public ArrayList<String> gateIds = new ArrayList<String>();			//Az alag�tbej�ratok ID-je
	public ArrayList<String> activeGateIds = new ArrayList<String>();	//Nyitott alag�tbej�ratok ID-je
	public String activeTunnelId = null;								//Akt�v alag�t ID-je

	/********************************************/
	
	public TunnelSystem() {
	}
	
	/**
	 * Alag�tbej�rat nyit�sa.
	 * @param gate	A megnyitand� alag�tbej�rat.
	 */
	public void openGate(TunnelGate gate) {
		if (gate.open) {
			System.out.println("Gate is already open.");
			return;
		}
		if (activeGates.size() >= 2) {
			System.out.println("There are already two open gates.");
			return;
		}
		gate.open = true;
		activeGates.add(gate);
		activeGateIds.add(gate.id);
		System.out.println("Gate opened.");
		//Ha van k�t nyitott alag�tbej. akkor alag�t l�trehoz�sa
		if (activeGates.size() == 2) {
			createTunnel(activeGates);
		}
	}
	
	/**
	 * Alag�tbej�rat bez�r�sa.
	 * @param gate	A bez�rand� alag�tbej�rat.
	 */
	public void closeGate(TunnelGate gate) {
		if (!gate.open) {
			System.out.println("Gate is already closed.");
			return;
		}
		if (activeTunnel != null && activeTunnel.passingTrainElements.size() > 0) {
			System.out.println("There�s a train in the tunnel.");
			return;
		}
		
		System.out.println("Gate closed.");
		
		//Ha volt k�t nyitott alag�tbej. (vagyis volt alag�t) akkor alag�t l�trehoz�sa
		if (activeTunnel != null) {
			destroyTunnel();
		}

		gate.open = false;
		activeGates.remove(gate);
		activeGateIds.remove(gate.id);
	}
	
	/**
	 * L�trehoz egy alagutat a megadott kapuk alapj�n, be�ll�tja a szomsz�doss�gokat a
	 * csatlakoz� alag�tbej�ratokkal.
	 * @param gates	A kapuk melyek alapj�n az alagutat l�tre kell hozni
	 */
	private void createTunnel(ArrayList<TunnelGate> gates) {
		
		activeTunnel = new RailTunnel("tunnel");	//�j alag�t, id="tunnel"
		activeTunnelId = "tunnel";
		
		//A gates[0]-hoz kapcsoljuk az alagutat
		activeTunnel.junctions[0] = new RailJunction(
					new Position(gates.get(0).junctions[0].position),
					gates.get(0).id,
					gates.get(0)
		);
			//A gates[0] oldal�n is bek�tj�k az alagutat
			//(amikor l�trej�n p�lya bet�lt�sekor egy alag�tbej�rat, akkor annak csak a junction[0] v�gpontj�hoz
			//van k�tve p�lyaelem, �gy tudjuk, hogy az alagutat a junction[1] pontj�hoz kell k�tni)
			gates.get(0).junctions[1] = new RailJunction(
					new Position(gates.get(0).junctions[0].position),
					"tunnel",
					activeTunnel
			);
		//A gates[1]-et is bek�tj�k
		activeTunnel.junctions[1] = new RailJunction(
				new Position(gates.get(1).junctions[0].position),
				gates.get(1).id,
				gates.get(1)
		);
			gates.get(1).junctions[1] = new RailJunction(
					new Position(gates.get(1).junctions[0].position),
					"tunnel",
					activeTunnel
			);
			
		System.out.println("\tTunnel created.");
	}
	
	/**
	 * Megsemmis�ti az alagutat, t�rli a megfelel� szomsz�doss�gokat a csatlakoz�
	 * alag�tbej�ratok eset�n.
	 */
	private void destroyTunnel() {

		//T�r�lj�k a kapuk oldal�n az alag�ttal val� kapcsolatukat
		activeGates.get(0).junctions[1].adjacentRail = null;
		activeGates.get(0).junctions[1].adjacentRailId = "null";
		activeGates.get(1).junctions[1].adjacentRail = null;
		activeGates.get(1).junctions[1].adjacentRailId = "null";
		
		//T�r�lj�k az alagutat
		activeTunnel = null;
		activeTunnelId = "null";
		
		System.out.println("\tTunnel destroyed.");
	}
	
	/**
	 * Inicializ�l�skor ez �ll�tja be az alag�tbej�ratokat, nyitott alag�tbej�ratokat �s akt�v alagutat a
	 * Board-t�l kapott ID-p�lyaelem map alapj�n.
	 * @param rails  A Board-t�l kapott ID-p�lyaelem map
	 */
	public void registerGates(HashMap<String, RailWay> rails) {
		for(String _id : gateIds) {
			gates.add((TunnelGate)rails.get(_id));
		}
		for(String _id : activeGateIds) {
			gates.add((TunnelGate)rails.get(_id));
		}
		if (activeTunnelId != null && !activeTunnelId.equals("null"))
			activeTunnel = (RailTunnel)rails.get(activeTunnelId);
	}
	
}
