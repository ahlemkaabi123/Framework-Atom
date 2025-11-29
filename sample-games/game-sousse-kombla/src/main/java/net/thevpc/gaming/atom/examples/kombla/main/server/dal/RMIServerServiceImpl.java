package net.thevpc.gaming.atom.examples.kombla.main.server.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIClientService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIServerService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class RMIServerServiceImpl extends UnicastRemoteObject implements RMIServerService {

    private MainServerDAOListener listener;
    private Map<Integer, RMIClientService> clients;

    public RMIServerServiceImpl(MainServerDAOListener listener, Map<Integer, RMIClientService> clients)
            throws RemoteException {
        super();
        this.listener = listener;
        this.clients = clients;
    }

    @Override
    public StartGameInfo connect(String playerName, RMIClientService client) throws RemoteException {
        StartGameInfo info = listener.onReceivePlayerJoined(playerName);
        clients.put(info.getPlayerId(), client);
        return info;
    }

    @Override
    public StartGameInfo moveRight(Integer playerId) throws RemoteException {
        listener.onReceiveMoveRight(playerId);
        return null;
    }

    @Override
    public StartGameInfo moveLeft(Integer playerId) throws RemoteException {
        listener.onReceiveMoveLeft(playerId);
        return null;
    }

    @Override
    public StartGameInfo moveUp(Integer playerId) throws RemoteException {
        listener.onReceiveMoveUp(playerId);
        return null;
    }

    @Override
    public StartGameInfo moveDown(Integer playerId) throws RemoteException {
        listener.onReceiveMoveDown(playerId);
        return null;
    }

    @Override
    public StartGameInfo fire(Integer playerId) throws RemoteException {
        listener.onReceiveReleaseBomb(playerId);
        return null;
    }
}
