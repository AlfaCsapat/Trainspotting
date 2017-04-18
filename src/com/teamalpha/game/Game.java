package com.teamalpha.game;

import com.google.gson.*;
import com.teamalpha.railway.*;
import com.teamalpha.railway.tunnel.TunnelGate;
import com.teamalpha.utils.ColorHelper;
import com.teamalpha.train.Train;
import com.teamalpha.train.element.Axis;
import com.teamalpha.train.element.Locomotive;
import com.teamalpha.train.element.TrainElement;
import com.teamalpha.train.spawn.TrainSpawn;
import com.teamalpha.utils.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * A terepasztalt, observert,  menedzselő osztály.
 */
public class Game {

	Board board;

	public Board getBoard() {
		return board;
	}

	Observer observer = new Observer();

	public static boolean endOfGame = false;

	public static void notifyLose() { endOfGame = true; }
	public static void notifyWin() { endOfGame = true; }

	/********************************************/
	
	public Game() {
	}

	public void update() {
		this.observer.update(this.board, this);
	}

	/**
	 * A megadott fájlból betölti a terepasztalt
	 */
	public void loadBoardFromFile(String file) {
		
		board = new Board();
		
		//Tároljuk ideiglenesen a csomópont-pozíciókat
		HashMap<String, Position> junctions = new HashMap<String, Position>();
		
		//Soronként beolvassuk a fájlt
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {

		    	String[] cmd = line.split(" ");
		    	
		    	if (cmd[0].equals("JUNCTION")) {
		    		junctions.put(cmd[1], new Position(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3])));
		    	}
		    	else if (cmd[0].equals("RAIL")) {
		    		//Létrehozzuk a sínt a megadott id-vel
		    		RailWay rail = new RailWay(cmd[1]);
		    		//Beallitjuk a vegpontjait:
		    		rail.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),	//A vegpont (x,y) pozicioja
		    				cmd[3]					//Az itt kapcsolodo palyaelem ID-je
		    		);
		    		rail.junctions[1] = new RailJunction(
		    				junctions.get(cmd[4]),
		    				cmd[5]
		    		);
		    		//Hozz�adjuk a s�nt a terepasztalhoz
		    		board.rails.put(rail.id, rail);
		    	}
		    	else if (cmd[0].equals("SHIFT")) {
		    		//L�trehozzuk a v�lt�t az ID-vel
		    		RailShift shift = new RailShift(cmd[1]);
		    		//Be�ll�tjuk a junctionokat
		    		shift.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[3]
		    		);
		    		shift.junctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[4]
		    		);
		    		//Az alternat�v junction-t is
		    		shift.alternativeExitJunction = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[5]
		    		);
		    		//Hozz�adjuk a v�lt�t a terepasztalhoz
		    		board.rails.put(shift.id, shift);
		    	}
		    	///M�dosult a kereszst�n, pontk�nt kezelj�k, nem k�t s�nk�nt
		    	///Parancs: RAILCROSSING (id) (junction) (kereszt1_s�n1) (kereszt1_s�n2) (kereszt2_s�n1) (kereszt2_s�n2)
		    	else if (cmd[0].equals("RAILCROSSING")) {
		    		RailCrossing railCrossing = new RailCrossing(cmd[1]);
		    		//Be�ll�tjuk az egyik kereszt�t junctionokat
		    		railCrossing.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[3]
		    		);
		    		railCrossing.junctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[4]
		    		);
		    		//Be�ll�tjuk a m�sik kereszt�t junction-okat
		    		railCrossing.otherJunctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[5]
		    		);
		    		railCrossing.otherJunctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[6]
		    		);
		    		//Hozz�adjuk a kereszts�nt a terepasztalhoz
		    		board.rails.put(railCrossing.id, railCrossing);
		    	}
		    	else if (cmd[0].equals("STATION")) {
		    		Station station = new Station(cmd[1]);
		    		//Sz�n

		    		//station.color = new ColorHelper(Integer.parseInt(cmd[5]));
					station.color = Integer.parseInt(cmd[5]);

		    		//V�rakoz� utasok
		    		station.passangers = Integer.parseInt(cmd[6]);
		    		//Be�ll�tjuk a junctionokat
		    		station.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[3]
		    		);
		    		station.junctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[4]
		    		);
		    		//Hozz�adjuk az �llom�st a terepasztalhoz
		    		board.rails.put(station.id, station);
		    	}
		    	else if (cmd[0].equals("TUNNELGATE")) {
		    		TunnelGate tunnelGate = new TunnelGate(cmd[1]);
		    		tunnelGate.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				cmd[3]
		    		);
		    		//mivel az alagutak alapból zátak, a másik csomópontjukat "null" id-vel inicializáljuk
		    		tunnelGate.junctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				"null"
		    		);

		    		//Hozzáadjuk az alagútbejáratot a terepasztalhoz
		    		board.rails.put(tunnelGate.id, tunnelGate);
		    		//és az alagútrendszerhez is (csak az id-t)
		    		board.tunnelSystem.gateIds.add(tunnelGate.id);
		    	}
		    	else if (cmd[0].equals("TRAINSPAWN")) {
		    		TrainSpawn trainSpawn = new TrainSpawn();
					trainSpawn.spawnTime = Integer.parseInt(cmd[1]);
		    		trainSpawn.spawnCode = "TRAINSPAWN";
		    		for(int i=2; i<cmd.length; ++i)
		    			trainSpawn.spawnCode += " " + cmd[i];
		    		board.spawnManager.trainSpawns.add(trainSpawn);
				}
		    	
		    }
		    //A szomszédosságokat beállitjuk a terepasztalon:
		    board.loadElements();
		    System.out.println("Board loaded.");
		}
		catch(Exception ex) {
			System.out.println("Can't load "+file);
			ex.printStackTrace();
		}
		
	}
	public static class GameSerializer implements JsonSerializer<Game> {
		@Override
		public JsonElement serialize(Game game, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject result = new JsonObject();
			result.add("board", context.serialize(game.board, Board.class));
			return result;
		}
	}

}
