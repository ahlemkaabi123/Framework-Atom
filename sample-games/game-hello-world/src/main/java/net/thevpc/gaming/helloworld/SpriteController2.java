package net.thevpc.gaming.helloworld;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.maintasks.MoveToPointSpriteMainTask;
import net.thevpc.gaming.atom.model.ModelPoint;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.DefaultSceneController;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneMouseEvent;

import java.awt.event.MouseEvent;

public class SpriteController2  extends DefaultSceneController{

    public void mouseClicked(SceneMouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Scene scene = e.getScene();
            SceneEngine sceneEngine = scene.getSceneEngine();
            Sprite ball2 = sceneEngine.findSpriteByName("Ball2");

            if (ball2 != null) {
                // Convertir les coordonnées de l'écran vers les coordonnées du modèle
                ModelPoint targetPoint = scene.toModelPoint(e.getPoint());

                // Créer et assigner la tâche de déplacement
                MoveToPointSpriteMainTask moveTask = new MoveToPointSpriteMainTask(targetPoint);
                sceneEngine.setSpriteMainTask(ball2, moveTask);
            }
        }
    }
}
