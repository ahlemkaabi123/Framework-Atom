package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.engine.collisiontasks.BorderCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollision;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.model.CollisionSides;
import net.thevpc.gaming.atom.model.Sprite;

public class Ball2DefaultSpriteCollisionManager  implements SpriteCollisionTask {
    @Override
    public void collideWithBorder(BorderCollision collision) {
        // 5.1 - Collision avec un bord : inverser le mouvement
        collision.adjustSpritePosition();
        Sprite sprite = collision.getSprite();
        int borderSides = collision.getSpriteCollisionSides().value();
        if ((borderSides & CollisionSides.SIDE_EAST) != 0 || (borderSides & CollisionSides.SIDE_WEST) != 0) {
            double newDirection = Math.PI - sprite.getDirection();
            sprite.setDirection(newDirection);
        }
        if ((borderSides & CollisionSides.SIDE_NORTH) != 0 || (borderSides & CollisionSides.SIDE_SOUTH) != 0) {
            double newDirection = -sprite.getDirection();
            sprite.setDirection(newDirection);
        }
    }

    @Override
    public void collideWithSprite(SpriteCollision collision) {
        // 5.2 - Collision avec un autre sprite : inverser le mouvement des deux sprites
        collision.adjustSpritePosition();
        Sprite sprite = collision.getSprite();
        Sprite otherSprite = collision.getOther();
        double spriteDirection = sprite.getDirection();
        double otherDirection = otherSprite.getDirection();
        sprite.setDirection(otherDirection);
        otherSprite.setDirection(spriteDirection);
    }
}
