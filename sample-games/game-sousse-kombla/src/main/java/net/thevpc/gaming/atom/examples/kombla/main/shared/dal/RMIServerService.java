package net.thevpc.gaming.atom.examples.kombla.main.shared.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerService extends Remote {

    public StartGameInfo connect(String PlayerName , RMIClientService client) throws RemoteException;
    public StartGameInfo moveRight(Integer PlayerId) throws RemoteException;
    public StartGameInfo moveLeft(Integer PlayerId) throws RemoteException;
    public StartGameInfo moveUp(Integer PlayerId) throws RemoteException;
    public StartGameInfo moveDown(Integer PlayerId) throws RemoteException;
    public StartGameInfo fire (Integer PlayerId) throws RemoteException;





}
