package com.teamalpha.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamalpha.railway.RailWay;

import java.io.*;


public class GameManager {
	
//	public static void main(String[] args) {
//
//		Game currentGame = new Game();
//		currentGame.loop();
//
//	}

    public static GameManager instance = new GameManager();

    private Game currentGame;

    public Game getCurrentGame() {
        return currentGame;
    }

    public Game newGame() {
        Game game = new Game();
        this.currentGame = game;
        return game;
    }



    public void loadGame(String filename) {
        Game game = this.newGame();
        this.lastLoadedFileName = filename;
        game.loadBoardFromFile(filename);
    }

    private String lastLoadedFileName;

    public void restartGame() {
        if(this.lastLoadedFileName != null) {
            this.loadGame(this.lastLoadedFileName);
        }
        else {
            System.out.println("No board used before, cannot restart.");
        }
    }

    public void saveGame(String filename) {
        this.writeGameToFile(this.currentGame, filename);
    }

    private void writeGameToFile(Game game, String filename) {
        String jsonFilename = filename + ".json";
        try(Writer writer = new OutputStreamWriter(new FileOutputStream(jsonFilename) , "UTF-8")) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Game.class, new Game.GameSerializer());
            builder.registerTypeAdapter(Board.class, new Board.BoardSerializer());
            builder.registerTypeAdapter(RailWay.class, new RailWay.RailWaySerializer());
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            System.out.println(gson.toJson(game));
//            gson.toJson(game, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
