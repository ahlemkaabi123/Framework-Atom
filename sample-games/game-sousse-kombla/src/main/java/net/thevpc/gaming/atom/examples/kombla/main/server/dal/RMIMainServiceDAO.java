package net.thevpc.gaming.atom.examples.kombla.main.server.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIClientService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIServerService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RMIMainServiceDAO implements MainServerDAO {
    private MainServerDAOListener listener;
    private Map<Integer, RMIClientService> clients = new HashMap<>();

    @Override
    public void start(MainServerDAOListener listener, AppConfig properties) {
        try {
            this.listener = listener;

            Registry registry;

            try {
                registry = LocateRegistry.createRegistry(properties.getServerPort());
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(properties.getServerPort());
            }

            RMIServerServiceImpl service = new RMIServerServiceImpl(listener, clients);

            registry.bind("kombla_rmi", service);

            System.out.println("RMI Server started on port " + properties.getServerPort());

        } catch (Exception e) {
            throw new RuntimeException("RMI Server start() failed", e);
        }
    }

    @Override
    public void sendModelChanged(DynamicGameModel dynamicGameModel) {
        clients.values().forEach(c -> {
            try { c.ModelChanged(dynamicGameModel); }
            catch (Exception ignored) {}
        });

    }
}
