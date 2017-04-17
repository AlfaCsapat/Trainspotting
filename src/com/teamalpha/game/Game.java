package com.teamalpha.game;

import com.teamalpha.Board;
import com.teamalpha.Observer;
import com.teamalpha.railway.*;
import com.teamalpha.railway.tunnel.TunnelGate;
import com.teamalpha.train.Train;
import com.teamalpha.train.element.Axis;
import com.teamalpha.train.element.Locomotive;
import com.teamalpha.train.element.TrainElement;
import com.teamalpha.train.spawn.TrainSpawn;
import com.teamalpha.utils.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A terepasztalt, observert,  menedzselő osztály.
 */
public class Game {

	Board board;
	Observer observer = new Observer();

	public static boolean endOfGame = true;

	public static void notifyLose() { endOfGame = true; }
	public static void notifyWin() { endOfGame = true; }

	/********************************************/
	
	public Game() {
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
	
	/**
	 * Itt zajlik a parancsok beolvasása, kiértékelése.
	 */
	public void loop() {
		
		//Jelenleg betöltött board fájlja. A restart parancs miatt kell ismernünk, hogy betölthessük újra.
		String actualBoardFile = "";
		
		//"Végtelen" ciklus a parancsok beolvasásához
		Scanner input=new Scanner(System.in);
		while(true) {
			
			String line = input.nextLine();			//Beolvasunk egy sort
			
			String[] cmd = line.split(" ");	//Szóközök mentén darabolunk
			
			String command = cmd[0].toUpperCase();
			
			//EXIT: kilépés parancs
			if (command.equals("EXIT"))
				break;
			
			//Terepasztal betöltése fájlból
			if (command.equals("LOADBOARD")) {
				actualBoardFile = cmd[1];
				loadBoardFromFile(actualBoardFile);
				endOfGame = false;
			}
			//Restart: aktuális terepasztal újratöltése
			else if (command.equals("RESTART")) {
				loadBoardFromFile(actualBoardFile);
				endOfGame = false;
			}
			///NEXTBOARD
			else if (command.equals("NEXTBOARD")) {
				System.out.println("No board to load.");
			}
			
			
			if (endOfGame) continue;	//ha a game veget ert, akkor nem ertelmezzuk a tobbi parancsot

			///DISABLEAUTOSPAWN
			if (command.equals("DISABLEAUTOSPAWN")) {
				board.spawnManager.setAutoSpawn(false);
				System.out.println("Autospawn disabled.");
			}
			///ENABLEAUTOSPAWN
			else if (command.equals("ENABLEAUTOSPAWN")) {
				board.spawnManager.setAutoSpawn(true);
				System.out.println("Autospawn enabled.");
			}
			///UPDATE [ticks]
			else if (command.equals("UPDATE")) {
				
				//N: hányszor update-elünk (alapból 1-szer)
				int N = 1;
				//de ha megadtunk számot, akkor annyiszor
				if (cmd.length > 1) N = Integer.parseInt(cmd[1]);
				
				System.out.println("Board updated.");
				try {
					for(int i=0; i<N; ++i) {
						observer.update(board, this);
					}
				}
				catch(Exception ex) {
					
				}
				
			}
			///SWITCH railshiftid
			else if (command.equals("SWITCH")) {
				RailShift shift = (RailShift)board.rails.get(cmd[1]);
				if (shift == null)
					System.out.println("Given shift id is not valid.");
				else {
					shift.switchIt();
				}
			}
			///OPENGATE gateid
			else if (command.equals("OPENGATE")) {
				TunnelGate tunnelGate = (TunnelGate)board.rails.get(cmd[1]);
				if (tunnelGate == null)
					System.out.println("Given gate id is not valid.");
				else {
					board.tunnelSystem.openGate(tunnelGate);
				}
			}
			///CLOSEGATE gateid
			else if (command.equals("CLOSEGATE")) {
				TunnelGate tunnelGate = (TunnelGate)board.rails.get(cmd[1]);
				if (tunnelGate == null)
					System.out.println("Given gate id is not valid.");
				else {
					board.tunnelSystem.closeGate(tunnelGate);
				}
			}
			///TRAINSPAWN speed railid [ [type/color][passangers]   [  [type/color][passangers]  [...] ] ]
			else if (command.equals("TRAINSPAWN")) {

				TrainSpawn trainSpawn = new TrainSpawn();
				trainSpawn.spawnCode = line;

				board.spawnManager.spawnTrain(trainSpawn, board);

			}
			///SETSTATIONPASSANGERS stationid passangers
			else if (command.equals("SETSTATIONPASSANGERS")) {
				Station station = (Station)board.rails.get(cmd[1]);
				station.passangers = Integer.parseInt(cmd[2]);
			}

		}
		input.close();

	}

}
