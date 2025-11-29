package net.thevpc.gaming.atom.examples.kombla.main.client.dal;

import net.thevpc.gaming.atom.examples.kombla.main.shared.dal.ProtocolConstants;
import net.thevpc.gaming.atom.examples.kombla.main.shared.engine.AppConfig;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.DynamicGameModel;
import net.thevpc.gaming.atom.examples.kombla.main.shared.model.StartGameInfo;
import net.thevpc.gaming.atom.model.DefaultPlayer;
import net.thevpc.gaming.atom.model.DefaultSprite;
import net.thevpc.gaming.atom.model.Player;
import net.thevpc.gaming.atom.model.Sprite;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPMainClientDAO implements MainClientDAO {

    private MainClientDAOListener listener;
    private AppConfig config;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private volatile boolean running;

    // Question 2: Implémentation de la méthode start
    @Override
    public void start(MainClientDAOListener listener, AppConfig properties) {
        this.listener = listener;
        this.config = properties;
        this.running = true;

        // IMPORTANT: La méthode start() est non-bloquante
        // On ne crée pas les sockets ici, seulement dans connect()
    }

    // Question 4: Méthode connect (bloquante)
    @Override
    public StartGameInfo connect() {
        try {
            // Question 4c: Création de la socket TCP
            socket = new Socket(config.getServerAddress(), config.getServerPort());
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            // Envoi de la commande CONNECT
            outputStream.writeInt(ProtocolConstants.CONNECT);
            String playerName = config.getPlayerName();
            outputStream.writeUTF(playerName == null ? "" : playerName);
            outputStream.flush();

            // Lecture de la réponse
            int response = inputStream.readInt();
            if (response != ProtocolConstants.OK) {
                throw new IOException("Connection refused by server");
            }

            // Question 4d et 4e: Lecture des informations de jeu
            StartGameInfo startGameInfo = readStartGameInfo();

            // IMPORTANT: Démarrage de la boucle de réception APRÈS la connexion
            startModelChangedLoop();

            return startGameInfo;

        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server", e);
        }
    }

    // Question 4d: Méthode readStartGameInfo
    private StartGameInfo readStartGameInfo() throws IOException {
        int playerId = inputStream.readInt();
        int rows = inputStream.readInt();
        int cols = inputStream.readInt();
        int[][] maze = new int[rows][cols];

        // Lecture du labyrinthe
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = inputStream.readInt();
            }
        }

        return new StartGameInfo(playerId, maze);
    }

    // Question 5: Méthode readPlayer
    private Player readPlayer() throws IOException {
        DefaultPlayer player = new DefaultPlayer();

        // Question 5a: id, name
        player.setId(inputStream.readInt());
        player.setName(inputStream.readUTF());

        // Question 5b: properties (Map)
        int propertyCount = inputStream.readInt();
        for (int i = 0; i < propertyCount; i++) {
            String key = inputStream.readUTF();
            String value = inputStream.readUTF();
            player.setProperty(key, value);
        }

        return player;
    }

    // Question 6: Méthode readSprite
    private Sprite readSprite() throws IOException {
        DefaultSprite sprite = new DefaultSprite();

        // Question 6a: Informations de base
        sprite.setId(inputStream.readInt());
        sprite.setKind(inputStream.readUTF());
        sprite.setName(inputStream.readUTF());

        // Location
        double x = inputStream.readDouble();
        double y = inputStream.readDouble();
        sprite.setLocation(x, y);

        sprite.setDirection(inputStream.readDouble());
        sprite.setPlayerId(inputStream.readInt());

        // Question 6b: properties (Map)
        int propertyCount = inputStream.readInt();
        for (int i = 0; i < propertyCount; i++) {
            String key = inputStream.readUTF();
            String value = inputStream.readUTF();
            sprite.setProperty(key, value);
        }

        return sprite;
    }

    // Question 3 et 7: Méthode pour lancer la boucle de réception
    // NOTE: Cette méthode n'est pas dans l'interface, donc pas de @Override
    private void startModelChangedLoop() {
        new Thread(() -> {
            while (running && socket != null && !socket.isClosed()) {
                try {
                    // Vérifier si des données sont disponibles
                    if (inputStream.available() > 0) {
                        // Question 7: Lecture du modèle dynamique
                        DynamicGameModel model = readDynamicGameModel();
                        if (listener != null) {
                            listener.onModelChanged(model);
                        }
                    }
                    // Petit délai pour éviter la surcharge CPU
                    Thread.sleep(16); // ~60 FPS
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Network error: " + e.getMessage());
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Model change loop stopped");
        }).start();
    }

    // Question 7: Lecture du DynamicGameModel
    private DynamicGameModel readDynamicGameModel() throws IOException {
        DynamicGameModel model = new DynamicGameModel();

        // Question 7a: frame (temps du jeu)
        model.setFrame(inputStream.readLong());

        // Question 7b: joueurs (Player)
        int playerCount = inputStream.readInt();
        for (int i = 0; i < playerCount; i++) {
            model.getPlayers().add(readPlayer());
        }

        // Question 7c: sprites (Bomber, Bombe, Explosion)
        int spriteCount = inputStream.readInt();
        for (int i = 0; i < spriteCount; i++) {
            model.getSprites().add(readSprite());
        }

        return model;
    }

    // Question 8: Méthode sendMoveLeft
    @Override
    public void sendMoveLeft() {
        try {
            if (outputStream != null) {
                outputStream.writeInt(ProtocolConstants.LEFT);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send move left", e);
        }
    }

    @Override
    public void sendMoveRight() {
        try {
            if (outputStream != null) {
                outputStream.writeInt(ProtocolConstants.RIGHT);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send move right", e);
        }
    }

    @Override
    public void sendMoveUp() {
        try {
            if (outputStream != null) {
                outputStream.writeInt(ProtocolConstants.UP);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send move up", e);
        }
    }

    @Override
    public void sendMoveDown() {
        try {
            if (outputStream != null) {
                outputStream.writeInt(ProtocolConstants.DOWN);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send move down", e);
        }
    }

    @Override
    public void sendFire() {
        try {
            if (outputStream != null) {
                outputStream.writeInt(ProtocolConstants.FIRE);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to send fire", e);
        }
    }

    // Méthode utilitaire pour fermer la connexion (non dans l'interface)
    public void disconnect() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    // Méthode utilitaire pour vérifier la connexion
    public boolean isConnected() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }

}