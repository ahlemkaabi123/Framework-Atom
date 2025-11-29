package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.*;
import net.thevpc.gaming.atom.debug.AdjustViewController;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.model.*;
import net.thevpc.gaming.atom.model.Dimension;
import net.thevpc.gaming.atom.model.Point;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.layers.FillBoardColorLayer;
import net.thevpc.gaming.atom.presentation.layers.FillScreenColorLayer;
import net.thevpc.gaming.atom.presentation.layers.ImageBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.Layers;

import java.awt.*;

/**
 * Created by vpc on 9/23/16.
 */
@AtomScene(
        id = "hello",
        title = "Hello World",
        tileWidth = 80,
        tileHeight = 80
        
)
@AtomSceneEngine(
        id="hello",
        columns = 10,
        rows = 10,
        fps = 25
)
public class HelloWorldScene {

    @Inject
    private Scene scene;
    @Inject
    private SceneEngine sceneEngine;
    private SLabel viesLabel;

    @OnSceneStarted
    private void init() {
        //scene.addLayer(new FillBoardColorLayer(Color.GREEN));
       /* scene.addLayer(Layers.fillBoardGradient(
                Color.GRAY,
                Color.RED, Orientation.NORTH));

        */

        scene.addLayer(new FillScreenColorLayer(Color.PINK));
        scene.addLayer(Layers.debug());
        scene.addLayer(new ImageBoardLayer("/img1.jpg"));
        //scene.addLayer(Layers.fillScreen(Color.BLUE));
        scene.addController(new SpriteController(SpriteFilter.byName("Ball1")).setArrowKeysLayout());
        scene.addController(new SpriteController(SpriteFilter.byName("Ball2")).setESDFLayout());
        scene.addController(new SpriteController2());
        scene.addController(new AdjustViewController());
        scene.addComponent(
                new SLabel("Click CTRL-D to switch debug mode, use Arrows to move the ball")
                .setLocation(Point.ratio(0.5f,0.5f))
        );
        scene.addComponent(
                new SLabel("Vies : 3")
                        .setLocation(Point.ratio(0.1f, 0.1f))

        );
        scene.setSpriteView(SpriteFilter.byKind("Ball"), new ImageSpriteView("/ball.png", 8, 4));
        scene.setSpriteView(SpriteFilter.byKind("Ball2"), new ImageSpriteView("/ball22.png", 5, 2));

    }
    @OnNextFrame
    private void aChaqueTic(){
        Sprite ball = sceneEngine.findSpriteByName("Ball0");
        if (ball != null) {
            int vies = ball.getLife();
            viesLabel.setText("Vies : " + vies);
        }

    }


}
