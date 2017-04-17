package com.teamalpha;

import com.teamalpha.train.Train;
import com.teamalpha.train.element.Axis;
import com.teamalpha.train.element.Locomotive;
import com.teamalpha.train.element.TrainElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A terepasztalt, observert, level-inf�kat menedzsel� oszt�ly.
 */
public class Game {

	Board board;
	Observer observer = new Observer();

	public static boolean endOfGame = true;
	
	public static void notifyLose() { endOfGame = true; }
	public static void notifyWin() { endOfGame = true; }
	
	/********************************************/
	
	Game() {
	}

	/**
	 * A megadott f�jlb�l bet�lti a p�ly�t. A f�jl tartalma a p�lyale�r� nyelv alapj�n kell
	 * hogy k�sz�lj�n.
	 * @param file	A bet�ltend� p�ly�t tartalmaz� f�jl neve, el�r�si �ttal
	 */
	public void loadBoardFromFile(String file) {
		
		board = new Board();
		
		//T�roljuk ideiglenesen a csom�pont-poz�ci�kat
		HashMap<String, Position> junctions = new HashMap<String, Position>();
		
		//Soronk�nt beolvassuk a f�jlt
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {

		    	String[] cmd = line.split(" ");
		    	
		    	if (cmd[0].equals("JUNCTION")) {
		    		junctions.put(cmd[1], new Position(Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3])));
		    	}
		    	else if (cmd[0].equals("RAIL")) {
		    		//L�trehozzuk a s�nt a megadott id-vel
		    		RailWay rail = new RailWay(cmd[1]);
		    		//Be�ll�tjuk a v�gpontjait:
		    		rail.junctions[0] = new RailJunction(
		    				junctions.get(cmd[2]),	//A v�gpont (x,y) poz�ci�ja
		    				cmd[3]					//Az itt kapcsol�d� p�lyaelem ID-je
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
		    		station.color = new GameColor(Integer.parseInt(cmd[5]));
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
		    		//mivel az alagutak alapb�l z�rtak, a m�sik csom�pontjukat "null" id-vel inicializ�ljuk
		    		tunnelGate.junctions[1] = new RailJunction(
		    				junctions.get(cmd[2]),
		    				"null"
		    		);

		    		//Hozz�adjuk az alag�tbej�ratot a terepasztalhoz
		    		board.rails.put(tunnelGate.id, tunnelGate);
		    		//�s az alag�trendszerhez is (csak az id-t)
		    		board.tunnelSystem.gateIds.add(tunnelGate.id);
		    	}
		    	
		    }
		    //A szomsz�doss�gokat be�ll�tjuk a terepasztalon:
		    board.loadElements();
		    System.out.println("Board loaded.");
		}
		catch(Exception ex) {
			System.out.println("Can't load "+file);
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Itt zajlik a parancsok beolvas�sa, ki�rt�kel�se.
	 */
	public void loop() {
		
		//Jelenleg bet�lt�tt board f�jlja. A restart parancs miatt kell ismern�nk, hogy bet�lthess�k �jra.
		String actualBoardFile = "";
		
		//"V�gtelen" ciklus a parancsok beolvas�s�hoz
		Scanner input=new Scanner(System.in);
		while(true) {
			
			String line = input.nextLine();			//Beolvasunk egy sort
			
			String[] cmd = line.split(" ");	//Sz�k�z�k ment�n daraboljuk
			
			String command = cmd[0].toUpperCase();
			
			//EXIT: kil�p�s parancs
			if (command.equals("EXIT"))
				break;
			
			//Terepasztal bet�lt�se megadott f�jlb�l
			if (command.equals("LOADBOARD")) {
				actualBoardFile = cmd[1];
				loadBoardFromFile(actualBoardFile);
				endOfGame = false;
			}
			//Restart: �jra bet�ltj�k a terepasztalt
			else if (command.equals("RESTART")) {
				loadBoardFromFile(actualBoardFile);
				endOfGame = false;
			}
			///NEXTBOARD
			else if (command.equals("NEXTBOARD")) {
				System.out.println("No board to load.");
			}
			
			
			if (endOfGame) continue;	///HA a j�t�k v�get �rt, akkor a k�vetkez� parancsokat nem �rtelmezz�k
			
			///SAVE [filename]
			if (command.equals("SAVE")) {
				//TO-DO
			}
			///LOAD [filename]
			else if (command.equals("LOAD")) {
				//TO-DO
			}
			///DISABLEAUTOSPAWN
			else if (command.equals("DISABLEAUTOSPAWN")) {
				System.out.println("Autospawn disabled.");
			}
			///ENABLEAUTOSPAWN
			else if (command.equals("ENABLEAUTOSPAWN")) {
				System.out.println("Autospawn enabled.");
			}
			///UPDATE [ticks]
			else if (command.equals("UPDATE")) {
				
				//N: h�nyszor update-el�nk (alapb�l 1-szer)
				int N = 1;
				//de ha megadtunk sz�mot, akkor annyiszor
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
				Train train = new Train("train"+(board.trains.size()+1), Float.parseFloat(cmd[1]));
				RailWay railWay = board.rails.get(cmd[2]);
				
				if (railWay == null)
					System.out.println("Given rail id is not valid.");
				else {
					
					//Egys�g- ir�nyvektor l�trehoz�sa s�n v�gpontjai alapj�n
					Position v = new Position(
						railWay.junctions[1].position.x - railWay.junctions[0].position.x,
						railWay.junctions[1].position.y - railWay.junctions[0].position.y
					);
					v.x = v.x/( (float)Math.sqrt( (v.x*v.x)+(v.y*v.y) ) ); //egys�gvektorr� alak�t�s
					v.y = v.y/( (float)Math.sqrt( (v.x*v.x)+(v.y*v.y) ) );
					
					//El�sz�r mozdony l�trehoz�sa
					Locomotive loco = new Locomotive();
					loco.delegate = train;
					railWay.registerPassingTrainElement(loco); //regisztr�ljuk a s�nre a mozdonyt
					
					loco.frontAxis = new Axis(loco);							//A tengely a loco-hoz tartozik
					loco.frontAxis.position.x = railWay.junctions[0].position.x;//poz�ci�ja
					loco.frontAxis.position.y = railWay.junctions[0].position.y;
					loco.frontAxis.rail = railWay;							//jelenleg mely s�nen van
					loco.frontAxis.destinationJunction = railWay.junctions[1];	//mely v�gpont fel� halad

					loco.rearAxis = new Axis(loco);
					loco.rearAxis.position.x = loco.frontAxis.position.x - v.x* TrainElement.length;
					loco.rearAxis.position.y = loco.frontAxis.position.y - v.y*TrainElement.length;
					loco.rearAxis.rail = railWay;
					loco.rearAxis.destinationJunction = railWay.junctions[1];
					
					train.locomotive = loco;
					
					for(int i=3; i<cmd.length; ) {
						//TO-DO
					}
					
					board.trains.add(train);
					System.out.println("Train "+train.id+" spawned.");
					
				}
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
