package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.RMIClientService;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientServiceImpl extends UnicastRemoteObject implements RMIClientService {
    private MainClientDAOListener listener;

    public RMIClientServiceImpl(MainClientDAOListener listener) throws RemoteException {
        super();
        this.listener = listener;
    }


    public void ModelChanged(DynamicGameModel dynamicGameModel) throws RemoteException {
        listener.onModelChanged(dynamicGameModel);
    }
}
