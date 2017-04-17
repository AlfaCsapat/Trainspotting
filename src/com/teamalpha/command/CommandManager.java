package com.teamalpha.command;

import com.teamalpha.game.Board;
import com.teamalpha.game.GameManager;
import com.teamalpha.railway.RailShift;
import com.teamalpha.railway.RailWay;
import com.teamalpha.railway.Station;
import com.teamalpha.railway.tunnel.TunnelGate;
import com.teamalpha.train.Train;
import com.teamalpha.train.element.Axis;
import com.teamalpha.train.element.Locomotive;
import com.teamalpha.train.element.TrainElement;
import com.teamalpha.utils.Position;

import java.util.Scanner;

/**
 * Created by zsolt on 2017. 04. 17..
 */
public class CommandManager {

    public static CommandManager instance = new CommandManager();

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
//            if (command.equals("LOADBOARD")) {
//                actualBoardFile = cmd[1];
//                loadBoardFromFile(actualBoardFile);
//                endOfGame = false;
//            }
//            //Restart: �jra bet�ltj�k a terepasztalt
//            else if (command.equals("RESTART")) {
//                loadBoardFromFile(actualBoardFile);
//                endOfGame = false;
//            }
//            ///NEXTBOARD
//            else if (command.equals("NEXTBOARD")) {
//                System.out.println("No board to load.");
//            }
//
//
//            if (endOfGame) continue;	///HA a j�t�k v�get �rt, akkor a k�vetkez� parancsokat nem �rtelmezz�k

            Board board = GameManager.instance.getCurrentGame().getBoard();

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
                        GameManager.instance.getCurrentGame().update();
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
