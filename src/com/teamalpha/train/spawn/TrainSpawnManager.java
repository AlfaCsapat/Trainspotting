package com.teamalpha.train.spawn;

import com.teamalpha.game.Board;
import com.teamalpha.railway.RailWay;
import com.teamalpha.train.Train;
import com.teamalpha.train.element.*;
import com.teamalpha.utils.Position;

import java.util.ArrayList;

/**
 * A terepasztalon l�v�/leend� vonatok spawn-ol�s�t kezel�/v�gz� oszt�ly.
 */
public class TrainSpawnManager {

	public boolean autoSpawn = true;
	public ArrayList<TrainSpawn> trainSpawns = new ArrayList<TrainSpawn>();
	
	/********************************************/
	
	public TrainSpawnManager() {
	}

	public void setAutoSpawn(boolean _autoSpawn) {
		autoSpawn = _autoSpawn;
	}

	public void update(Board board) {

		if (!autoSpawn) return;

		for(int i=0; i<trainSpawns.size(); ++i) {
			if (trainSpawns.get(i).spawnTime <= board.ticks) {
				spawnTrain(trainSpawns.get(i), board);
				trainSpawns.remove(i);
				return;
			}
		}
	}

	public void spawnTrain(TrainSpawn trainSpawn, Board board) {

		String[] cmd = trainSpawn.spawnCode.split(" ");

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

			TrainElement lastTe = loco;

			//Hozzáadjuk a vagonokat a vonathoz
			try {
				int teN = 1;    //Hányadik vagonnál tartunk
				for (int i = 3; i < cmd.length; ++i) {

					TrainElement te;
					int type = Integer.parseInt(cmd[i]);

					//0 esetén szenes kocsi
					if (type == 0) {
						te = new CoalWagon();
					}
					//ha nem nulla akkor utas kocsi
					else {
						te = new PassangerWagon(type, Integer.parseInt(cmd[i + 1]));
						++i;
					}

					railWay.registerPassingTrainElement(te); //regisztr�ljuk a s�nre

					te.frontAxis = new Axis(te);
					//2 egység távolság lesz a vagonok közt
					te.frontAxis.position.x =
							railWay.junctions[0].position.x - v.x * (teN * (TrainElement.length + 2));
					te.frontAxis.position.y =
							railWay.junctions[0].position.y - v.y * (teN * (TrainElement.length + 2));
					te.frontAxis.rail = railWay;
					te.frontAxis.destinationJunction = railWay.junctions[1];

					te.rearAxis = new Axis(te);
					te.rearAxis.position.x = te.frontAxis.position.x - v.x * TrainElement.length;
					te.rearAxis.position.y = te.frontAxis.position.y - v.y * TrainElement.length;
					te.rearAxis.rail = railWay;
					te.rearAxis.destinationJunction = railWay.junctions[1];

					//Hozzáadjuk a vonathoz
					train.wagons.add(te);
					//Beállítjuk az előtte lévő vagonnál is
					lastTe.followingTrainElement = te;
					lastTe = te;

					++teN;

				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}

			board.trains.add(train);
			System.out.println("Train "+train.id+" spawned.");

		}

	}
	
}
