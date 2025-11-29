package net.thevpc.gaming.helloworld;
import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

@AtomSprite(
        name = "Missile",
        kind = "Missile",
        sceneEngine = "hello",
        speed = 0.2,
        mainTask = MoveSpriteMainTask.class,
        collisionTask = MissileCollisionTask.class
)
public class Missile {
    @Inject
    private Sprite sprite;

    @Inject
    private SceneEngine sceneEngine;

    @OnInit
    private void init() {
        sprite.setLife(3);
        sprite.setSize(0.5, 0.5);
    }
}
