package com.connect4.persistence;

import com.connect4.model.GameState;
import com.connect4.statistics.GameStatistics;
import com.connect4.settings.EnhancedSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CloudSaveManager {
    
    private static final String CLOUD_API_URL = "https://api.connect4game.com/v1";
    private static final String LOCAL_SAVE_DIR = "saves";
    private static final String SETTINGS_FILE = "settings.json";
    private static final String GAME_HISTORY_FILE = "game_history.json";
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EnhancedSettings settings = EnhancedSettings.getInstance();
    private String userId;
    
    public CloudSaveManager() {
        initializeDirectories();
    }
    
    private void initializeDirectories() {
        File saveDir = new File(LOCAL_SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }
    
    public CompletableFuture<Boolean> saveGameState(GameState gameState) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String filename = generateSaveFilename();
                String json = objectMapper.writeValueAsString(gameState);
                
                // Save locally
                saveToLocalFile(filename, json);
                
                // Save to cloud if enabled
                if (settings.isCloudSaveEnabled()) {
                    return uploadToCloud(filename, json);
                }
                
                return true;
            } catch (Exception e) {
                System.err.println("Error saving game state: " + e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<GameState> loadGameState(String saveId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Try cloud first
                if (settings.isCloudSaveEnabled()) {
                    String cloudData = downloadFromCloud(saveId);
                    if (cloudData != null) {
                        return objectMapper.readValue(cloudData, GameState.class);
                    }
                }
                
                // Fallback to local
                String localData = loadFromLocalFile(saveId);
                if (localData != null) {
                    return objectMapper.readValue(localData, GameState.class);
                }
                
                return null;
            } catch (Exception e) {
                System.err.println("Error loading game state: " + e.getMessage());
                return null;
            }
        });
    }
    
    public CompletableFuture<List<GameSave>> getSaveGames() {
        return CompletableFuture.supplyAsync(() -> {
            List<GameSave> saves = new ArrayList<>();
            
            // Load local saves
            File saveDir = new File(LOCAL_SAVE_DIR);
            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".json"));
            
            if (files != null) {
                for (File file : files) {
                    try {
                        String content = loadFromLocalFile(file.getName());
                        GameSave save = objectMapper.readValue(content, GameSave.class);
                        save.setId(file.getName().replace(".json", ""));
                        save.setLocation("Local");
                        saves.add(save);
                    } catch (Exception e) {
                        System.err.println("Error loading save: " + e.getMessage());
                    }
                }
            }
            
            // Load cloud saves
            if (settings.isCloudSaveEnabled()) {
                saves.addAll(getCloudSaves());
            }
            
            // Sort by last modified
            saves.sort(Comparator.comparing(GameSave::getLastModified).reversed());
            
            return saves;
        });
    }
    
    public CompletableFuture<Boolean> syncSettings() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String settingsJson = objectMapper.writeValueAsString(settings);
                saveToLocalFile(SETTINGS_FILE, settingsJson);
                
                if (settings.isCloudSaveEnabled()) {
                    return uploadToCloud(SETTINGS_FILE, settingsJson);
                }
                
                return true;
            } catch (Exception e) {
                System.err.println("Error syncing settings: " + e.getMessage());
                return false;
            }
        });
    }
    
    public CompletableFuture<Boolean> syncGameHistory(GameStatistics statistics) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String historyJson = objectMapper.writeValueAsString(statistics);
                saveToLocalFile(GAME_HISTORY_FILE, historyJson);
                
                if (settings.isCloudSaveEnabled()) {
                    return uploadToCloud(GAME_HISTORY_FILE, historyJson);
                }
                
                return true;
            } catch (Exception e) {
                System.err.println("Error syncing game history: " + e.getMessage());
                return false;
            }
        });
    }
    
    private void saveToLocalFile(String filename, String content) throws IOException {
        File file = new File(LOCAL_SAVE_DIR, filename);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(content);
        }
    }
    
    private String loadFromLocalFile(String filename) throws IOException {
        File file = new File(LOCAL_SAVE_DIR, filename);
        if (!file.exists()) return null;
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
    
    private boolean uploadToCloud(String filename, String data) {
        try {
            URL url = new URL(CLOUD_API_URL + "/saves/" + filename);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + settings.getCloudToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
                os.flush();
            }
            
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            System.err.println("Cloud upload failed: " + e.getMessage());
            return false;
        }
    }
    
    private String downloadFromCloud(String saveId) {
        try {
            URL url = new URL(CLOUD_API_URL + "/saves/" + saveId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + settings.getCloudToken());
            
            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    return response.toString();
                }
            }
        } catch (Exception e) {
            System.err.println("Cloud download failed: " + e.getMessage());
        }
        return null;
    }
    
    private List<GameSave> getCloudSaves() {
        List<GameSave> saves = new ArrayList<>();
        try {
            URL url = new URL(CLOUD_API_URL + "/saves");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + settings.getCloudToken());
            
            if (conn.getResponseCode() == 200) {
                // Parse cloud saves
                // Implementation depends on cloud API response format
            }
        } catch (Exception e) {
            System.err.println("Failed to get cloud saves: " + e.getMessage());
        }
        return saves;
    }
    
    private String generateSaveFilename() {
        return "save_" + System.currentTimeMillis() + ".json";
    }
    
    public static class GameSave {
        private String id;
        private String name;
        private Date lastModified;
        private String location;
        private GameState gameState;
        
        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Date getLastModified() { return lastModified; }
        public void setLastModified(Date lastModified) { this.lastModified = lastModified; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public GameState getGameState() { return gameState; }
        public void setGameState(GameState gameState) { this.gameState = gameState; }
    }
}
