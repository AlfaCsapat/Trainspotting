package com.teamalpha.game;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.teamalpha.railway.RailWay;
import com.teamalpha.railway.tunnel.TunnelSystem;
import com.teamalpha.train.Train;
import com.teamalpha.train.spawn.TrainSpawnManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Board {

	public int height;						//terepasztal magass�ga (egys�gben)
	public int width;						//terepasztal sz�less�ge (egys�gben)
	public HashMap<String, RailWay> rails = new HashMap<String, RailWay>();	//ID - p�lyaelem p�rok
	public ArrayList<Train> trains = new ArrayList<Train>();	//A j�t�kban megjelen� vonatok list�ja
	public TunnelSystem tunnelSystem = new TunnelSystem();		//Alag�trendszer
	public TrainSpawnManager spawnManager = new TrainSpawnManager();	//Vonatok spawnol�s�t menedzsel� objektum
	public int ticks = 0;

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

	@Override
	public String toString() {
		return "height: " + this.height + "width: " + this.width;
	}

	public static class BoardSerializer implements JsonSerializer<Board> {
		@Override
		public JsonElement serialize(Board board, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			result.addProperty("width", board.width);
			result.addProperty("height", board.height);
			JsonObject rails = new JsonObject();
			for(Map.Entry<String, RailWay> entry : board.rails.entrySet()) {
				rails.add(entry.getKey(), context.serialize(entry.getValue(), RailWay.class));
			}
			result.add("rails", rails);

			return result;
		}
	}
	
}
