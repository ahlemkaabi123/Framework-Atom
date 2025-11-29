package net.thevpc.gaming.atom.examples.kombla.main.server.dal;
import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.ProtocolConstants;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPMainServerDAO implements MainServerDAO{
    //    l : Listener pour communiquer avec le métier
    //    c : Configuration de l'application (port)
    MainServerDAOListener l;
    AppConfig c ;
    //Q2
    static class ClientSession {
        public int playerId;
        public Socket socket;
        public DataInputStream dataInputStream;
        public DataOutputStream dataOutputStream;
    }
    //Map des sessions clients
    private Map<Integer, ClientSession> playerToSocketMap = new HashMap<>() ;

    @Override
    public void start(MainServerDAOListener l, AppConfig c) {
        this.l = l;
        this.c = c;
        int port= c.getServerPort();
        // Question 4: Lancement de l'écoute Socket dans un nouveau Thread
        new Thread(()-> {
            //ServerSocket s;
            try {
                //s = new ServerSocket(port);
                System.out.println("///STARTING TCP SERVER on port: " + c.getServerPort() + " ///");
                startServer();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        ;}
    void startServer() throws IOException {
        ServerSocket ss = new ServerSocket(c.getServerPort());
        while (true){
            Socket s = ss.accept();
            new Thread(()-> {
                try {
                    // Question 4: Appel de processClient pour chaque client
                    processClient(s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
    // Question 5: Méthode processClient traitant les connexions entrantes
    void processClient(Socket s) throws IOException {
        DataInputStream inputStream = new DataInputStream(s.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(s.getOutputStream());
        // Question 5a: Lecture de la commande (entier)
        int c = inputStream.readInt();
        // Question 5c: Implémentation CONNECT
        if (c!= ProtocolConstants.CONNECT){
            s.close();
            return;
        }
        String n = inputStream.readUTF();
        StartGameInfo sgi = l.onReceivePlayerJoined(n);
        ClientSession cs = new ClientSession();
        cs.playerId = sgi.getPlayerId();
        cs.dataInputStream = inputStream;
        cs.dataOutputStream = outputStream;
        cs.socket = s;
        playerToSocketMap.put(sgi.getPlayerId(),cs);

        // Question 5b: Envoi StartGameInfo
        //writeStartGameInfo(outputStream, sgi);
        outputStream.writeInt(ProtocolConstants.OK);
        outputStream.writeInt(cs.playerId);
        outputStream.writeInt(sgi.getMaze().length);
        outputStream.writeInt(sgi.getMaze()[0].length);
        for(int i=0; i<sgi.getMaze().length;i++){
            for (int j=0; j<sgi.getMaze()[0].length;j++){
                outputStream.writeInt(sgi.getMaze()[i][j]);
            }
        }
    }

    // Question 6: Méthode writePlayer
    private void writePlayer(DataOutputStream outputStream, Player player) throws IOException {
        outputStream.writeInt(player.getId());
        outputStream.writeUTF(player.getName());

        // Question 6b: Propriétés (Map<String, String>)
        Map<String, Object> properties = player.getProperties();
        outputStream.writeInt(properties.size());
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            outputStream.writeUTF(entry.getKey());
            outputStream.writeUTF(entry.getValue().toString());
        }
    }




    // Question 7: Méthode writeSprite
    private void writeSprite(DataOutputStream outputStream, Sprite sprite) throws IOException {
        // Question 7a: Informations de base
        outputStream.writeInt(sprite.getId());
        outputStream.writeUTF(sprite.getKind());
        outputStream.writeUTF(sprite.getName());

        // Location
        outputStream.writeDouble(sprite.getX());
        outputStream.writeDouble(sprite.getY());

        outputStream.writeDouble(sprite.getDirection());
        outputStream.writeInt(sprite.getPlayerId());

        // Question 7b: Propriétés
        Map<String, Object> properties = sprite.getProperties();
        outputStream.writeInt(properties.size());
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            outputStream.writeUTF(entry.getKey());
            outputStream.writeUTF(entry.getValue().toString());
        }
    }
    @Override
    public void sendModelChanged(DynamicGameModel dynamicGameModel) {
        // Envoi à tous les clients connectés
        for (ClientSession session : playerToSocketMap.values()) {
            try {
                DataOutputStream out = session.dataOutputStream;

                // Question 8a: Frame
                out.writeLong(dynamicGameModel.getFrame());

                // Question 8b: Joueurs
                out.writeInt(dynamicGameModel.getPlayers().size());
                for (Player player : dynamicGameModel.getPlayers()) {
                    writePlayer(out, player);
                }

                // Question 8c: Sprites
                out.writeInt(dynamicGameModel.getSprites().size());
                for (Sprite sprite : dynamicGameModel.getSprites()) {
                    writeSprite(out, sprite);
                }

                out.flush();

            } catch (IOException e) {
            }
        }
    }




    }
