package com.teamalpha;
import java.util.HashMap;

/**
 * P�lyaelemek k�zti csom�pontot le�r� oszt�ly.
 */
public class RailJunction {

	public Position position;		//A csom�pont poz�ci�ja a terepasztalon
	public RailWay adjacentRail;	//A csom�pont kimeneti p�lyaeleme (erre tov�bb�that�k az ide �rkez� elemek)
	public String adjacentRailId;	//Az adjacentRail be�ll�t�s�hoz haszn�lt p�lyaelem-ID

	/********************************************/
	
	public RailJunction() {
	}
	public RailJunction(Position _position, String _adjacentRailId) {
		position = _position;
		adjacentRailId = _adjacentRailId;
	}
	public RailJunction(Position _position, String _adjacentRailId, RailWay _adjacentRail) {
		position = _position;
		adjacentRailId = _adjacentRailId;
		adjacentRail = _adjacentRail;
	}

	/**
	 * Inicializ�l�skor az adjacentRail be�ll�t�sa az adjacentRailID �s a board-t�l kapott ID-p�lyelem map alapj�n.
	 * @param rails		ID-p�lyaelem map, melyet a Board ad �t
	 */
	public void setAdjacentRail(HashMap<String, RailWay> rails) {
		if (adjacentRailId == null || adjacentRailId.equals("null"))
			adjacentRail = null;
		else
			adjacentRail = rails.get(adjacentRailId);
	}
	
	/**
	 * Akkor h�v�dik, ha egy RailDriven elem (tengely) r�l�p a csom�pontra.
	 * @param element	A RailDriven elem, mely a csom�pontra �rkezett.
	 * @param oldRail	A h�v�t�l megkapjuk hogy eddig mely s�nen haladt a tengely
	 */
	public void enterElement(RailDriven element, RailWay oldRail) {
		adjacentRail.enterElement(element, oldRail);
	}
	
}
