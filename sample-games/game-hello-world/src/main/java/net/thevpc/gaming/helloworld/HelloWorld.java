package net.thevpc.gaming.helloworld;


import net.thevpc.gaming.atom.Atom;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.presentation.Game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 */
public class HelloWorld {

    public static void main(String[] args) {
        //create and start the game
        //Atom.startGame();
        Game game = Atom.createGame();
        GameEngine gameEngine = game.getGameEngine();

        game.start();

        // Changer de scène après un court délai, une fois l'initialisation terminée
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameEngine.setActiveSceneEngine("welcome");
            }
        }, 100);

    }

}
