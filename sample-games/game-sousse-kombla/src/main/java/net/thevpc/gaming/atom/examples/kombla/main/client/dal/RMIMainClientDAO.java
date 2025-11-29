package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIClientService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIServerService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIMainClientDAO implements MainClientDAO {


    private MainClientDAOListener listener;
    private RMIServerService server;
    private RMIClientService clientService;
    private String playerName;
    private int playerId;

    @Override
    public void start(MainClientDAOListener listener, AppConfig properties) {

    }

    @Override
    public StartGameInfo connect() {
        try {
            StartGameInfo info = server.connect(playerName, clientService);
            this.playerId = info.getPlayerId();
            return info;
        } catch (Exception e) {
            throw new RuntimeException("RMI connect() failed", e);
        }
    }

    @Override
    public void sendMoveLeft() { safe(() -> server.moveLeft(playerId)); }
    @Override
    public void sendMoveRight() { safe(() -> server.moveRight(playerId)); }
    @Override
    public void sendMoveUp() { safe(() -> server.moveUp(playerId)); }
    @Override
    public void sendMoveDown() { safe(() -> server.moveDown(playerId)); }
    @Override
    public void sendFire() { safe(() -> server.fire(playerId)); }

    private void safe(ThrowingRunnable r) {
        try { r.run(); } catch (Exception e) { e.printStackTrace(); }
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
