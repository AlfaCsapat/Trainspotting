package com.teamalpha.game;

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

    public void newGame() {
        this.currentGame = new Game();
    }

    public void loadGame(String filename) {

    }

    public void saveGame(String filename) {

    }

}
