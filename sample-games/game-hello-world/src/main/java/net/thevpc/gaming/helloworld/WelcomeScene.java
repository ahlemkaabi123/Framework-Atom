package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.*;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.DefaultSceneController;
import net.thevpc.gaming.atom.presentation.SceneKeyEvent;
import net.thevpc.gaming.atom.presentation.KeyCode;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.layers.ImageBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.Layers;
import net.thevpc.gaming.atom.model.Point;

import java.awt.*;

@AtomScene(
        id = "welcome",
        title = "Welcome Screen",
        tileWidth = 80,
        tileHeight = 80
)
@AtomSceneEngine(
        id = "welcome",
        columns = 10,
        rows = 10
)
public class WelcomeScene {

    @Inject
    private Scene scene;

    @Inject
    private SceneEngine sceneEngine;

    @Inject
    private GameEngine gameEngine;

    @OnSceneStarted
    private void init() {
        scene.addLayer(new ImageBoardLayer("/welcome.jpg"));
        scene.addLayer(Layers.debug());

        SLabel instructionLabel = new SLabel("Appuyez sur ESPACE pour commencer")
                .setLocation(Point.ratio(0.5f, 0.5f));
        scene.addComponent(instructionLabel);

        SLabel controlsLabel = new SLabel("Contrôles: Ball1 (Flèches) | Ball2 (ESDF + Souris)")
                .setLocation(Point.ratio(0.5f, 0.7f));
        scene.addComponent(controlsLabel);

        scene.addController(new DefaultSceneController() {
            @Override
            public void keyPressed(SceneKeyEvent e) {
                if (e.getKeyCode() == KeyCode.SPACE) {
                    gameEngine.setActiveSceneEngine("hello");
                }
            }
        });
    }
}