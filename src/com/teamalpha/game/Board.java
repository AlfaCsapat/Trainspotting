package com.teamalpha.game;
import com.teamalpha.railway.RailWay;
import com.teamalpha.railway.tunnel.TunnelSystem;
import com.teamalpha.train.Train;
import com.teamalpha.train.spawn.TrainSpawnManager;

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

	public int height;						//terepasztal magass�ga (egys�gben)
	public int width;						//terepasztal sz�less�ge (egys�gben)
	public HashMap<String, RailWay> rails = new HashMap<String, RailWay>();	//ID - p�lyaelem p�rok
	public ArrayList<Train> trains = new ArrayList<Train>();	//A j�t�kban megjelen� vonatok list�ja
	public TunnelSystem tunnelSystem = new TunnelSystem();		//Alag�trendszer
	public TrainSpawnManager spawnManager;	//Vonatok spawnol�s�t menedzsel� objektum
	
	/********************************************/
	
	public Board() {
	}
	
	/**
	 * P�lyaelem-szomsz�doss�gok �s alag�trendszer inicializ�l�sa
	 */
	public void loadElements() {
		
		//V�gigmegy�nk minden p�lyaelemen
		for (HashMap.Entry<String, RailWay> entry : rails.entrySet())
		{
			//�s azok mindegyik v�gpontj�n be�ll�tjuk a szomsz�dokat
			entry.getValue().setAdjacentRails(rails);
		}
		
		//Az alag�trendszert is be�ll�tjuk
		tunnelSystem.registerGates(rails);
		
	}
	
}
