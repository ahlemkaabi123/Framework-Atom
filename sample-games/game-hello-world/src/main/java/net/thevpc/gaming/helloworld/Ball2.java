package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.engine.collisiontasks.BounceSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.DefaultSpriteView;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SpriteDrawingContext;

import java.awt.*;

@AtomSprite(
        name = "Ball2",
        kind = "Ball2",
        sceneEngine = "hello",
        x=8,
        y=8,
        direction = -Math.PI/4,
        speed = 0.4,
        mainTask = MoveSpriteMainTask.class,
       // collisionTask = Ball2DefaultSpriteCollisionManager.class
       // collisionTask = StopSpriteCollisionTask.class
        collisionTask = BounceSpriteCollisionTask.class

)

public class Ball2 {
        @Inject
        Sprite sprite;
        @Inject
        SceneEngine sceneEngine;
        @Inject
        Scene scene;

        @OnInit
        private void init(){
            sprite.setLocation(8,8);
            sprite.setSize(2, 2);
            /*scene.setSpriteView(SpriteFilter.byName("Ball2"), new DefaultSpriteView() {
                @Override
                public void draw(SpriteDrawingContext context) {
                    Graphics2D g = (Graphics2D) context.getGraphics();
                    g.setColor(Color.MAGENTA);
                    g.fill(context.getSpriteShape());
                }
            });
*/


        }

}
