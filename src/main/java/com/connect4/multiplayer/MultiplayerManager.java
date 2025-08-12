package com.connect4.multiplayer;

import com.connect4.model.GameBoard;
import com.connect4.model.GameState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MultiplayerManager {
    
    public static class Player {
        private final String name;
        private final String id;
        private boolean isReady;
        private int wins;
        private int losses;
        
        public Player(String name, String id) {
            this.name = name;
            this.id = id;
            this.isReady = false;
            this.wins = 0;
            this.losses = 0;
        }
        
        public String getName() { return name; }
        public String getId() { return id; }
        public boolean isReady() { return isReady; }
        public void setReady(boolean ready) { this.isReady = ready; }
        public int getWins() { return wins; }
        public int getLosses() { return losses; }
        public void incrementWins() { wins++; }
        public void incrementLosses() { losses++; }
    }
    
    public static class GameRoom {
        private final String roomId;
        private final Player host;
        private Player guest;
        private boolean isGameActive;
        private GameBoard gameBoard;
        
        public GameRoom(String roomId, Player host) {
            this.roomId = roomId;
            this.host = host;
            this.isGameActive = false;
            this.gameBoard = new GameBoard();
        }
        
        public String getRoomId() { return roomId; }
        public Player getHost() { return host; }
        public Player getGuest() { return guest; }
        public void setGuest(Player guest) { this.guest = guest; }
        public boolean isGameActive() { return isGameActive; }
        public void setGameActive(boolean active) { this.isGameActive = active; }
        public GameBoard getGameBoard() { return gameBoard; }
        public boolean isFull() { return guest != null; }
    }
    
    public interface GameUpdateListener {
        void onPlayerJoined(Player player);
        void onPlayerLeft(Player player);
        void onGameStarted();
        void onMoveMade(int column, int player);
        void onGameEnded(String winner);
    }
    
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isHost = false;
    private boolean isConnected = false;
    private String playerName;
    private GameUpdateListener listener;
    
    private final ObservableList<Player> connectedPlayers = FXCollections.observableArrayList();
    private final Map<String, GameRoom> activeRooms = new ConcurrentHashMap<>();
    
    public MultiplayerManager() {
        // Initialize multiplayer manager
    }
    
    public void setGameUpdateListener(GameUpdateListener listener) {
        this.listener = listener;
    }
    
    public ObservableList<Player> getConnectedPlayers() {
        return connectedPlayers;
    }
    
    public boolean startHost(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isHost = true;
            isConnected = true;
            
            new Thread(() -> {
                try {
                    while (isConnected) {
                        Socket client = serverSocket.accept();
                        handleNewConnection(client);
                    }
                } catch (IOException e) {
                    System.err.println("Server error: " + e.getMessage());
                }
            }).start();
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            return false;
        }
    }
    
    public boolean connectToHost(String host, int port, String playerName) {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.playerName = playerName;
            isConnected = true;
            
            // Send player info
            out.println("JOIN:" + playerName);
            
            // Start listening for messages
            new Thread(this::listenForMessages).start();
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect: " + e.getMessage());
            return false;
        }
    }
    
    private void handleNewConnection(Socket client) {
        try {
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
            
            String message = clientIn.readLine();
            if (message != null && message.startsWith("JOIN:")) {
                String playerName = message.substring(5);
                String playerId = UUID.randomUUID().toString();
                Player newPlayer = new Player(playerName, playerId);
                
                connectedPlayers.add(newPlayer);
                
                if (listener != null) {
                    Platform.runLater(() -> listener.onPlayerJoined(newPlayer));
                }
                
                // Handle client communication
                new Thread(() -> handleClient(client, newPlayer)).start();
            }
        } catch (IOException e) {
            System.err.println("Error handling new connection: " + e.getMessage());
        }
    }
    
    private void handleClient(Socket client, Player player) {
        try {
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
            
            String message;
            while ((message = clientIn.readLine()) != null) {
                handleMessage(message, player);
            }
        } catch (IOException e) {
            System.err.println("Client disconnected: " + e.getMessage());
            connectedPlayers.remove(player);
            if (listener != null) {
                Platform.runLater(() -> listener.onPlayerLeft(player));
            }
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                handleMessage(message, null);
            }
        } catch (IOException e) {
            System.err.println("Disconnected from server: " + e.getMessage());
            isConnected = false;
        }
    }
    
    private void handleMessage(String message, Player sender) {
        String[] parts = message.split(":", 2);
        String command = parts[0];
        String data = parts.length > 1 ? parts[1] : "";
        
        switch (command) {
            case "MOVE":
                int column = Integer.parseInt(data);
                if (listener != null) {
                    Platform.runLater(() -> listener.onMoveMade(column, sender != null ? 1 : 2));
                }
                break;
            case "READY":
                if (sender != null) {
                    sender.setReady(true);
                }
                break;
            case "START":
                if (listener != null) {
                    Platform.runLater(() -> listener.onGameStarted());
                }
                break;
            case "END":
                if (listener != null) {
                    Platform.runLater(() -> listener.onGameEnded(data));
                }
                break;
        }
    }
    
    public void sendMove(int column) {
        if (isConnected && out != null) {
            out.println("MOVE:" + column);
        }
    }
    
    public void sendReady() {
        if (isConnected && out != null) {
            out.println("READY:true");
        }
    }
    
    public void sendGameEnd(String winner) {
        if (isConnected && out != null) {
            out.println("END:" + winner);
        }
    }
    
    public void disconnect() {
        isConnected = false;
        try {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            System.err.println("Error during disconnect: " + e.getMessage());
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public boolean isHost() {
        return isHost;
    }
}
