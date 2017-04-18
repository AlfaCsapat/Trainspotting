package com.teamalpha;

import com.teamalpha.command.CommandManager;
import com.teamalpha.game.GameManager;

public class Main {

    public static void main(String[] args) {
        GameManager.instance.newGame();
        CommandManager.instance.loop();
    }
}
