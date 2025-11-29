package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.BorderCollision;
import net.thevpc.gaming.atom.model.Sprite;

public class MissileCollisionTask implements SpriteCollisionTask {

    @Override
    public void collideWithBorder(BorderCollision collision) {
        collision.getSceneEngine().removeSprite(collision.getSprite().getId());
    }

    @Override
    public void collideWithSprite(SpriteCollision collision) {
        Sprite missile = collision.getSprite();
        collision.getSceneEngine().removeSprite(missile.getId());
    }
}
