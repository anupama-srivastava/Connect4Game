package com.connect4.service;

import com.connect4.model.GameBoard;
import com.connect4.model.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameStateService {
    private static final Logger logger = LoggerFactory.getLogger(GameStateService.class);
    private static final String SAVE_DIR = "saved_games";
    private static final String SAVE_FILE_EXTENSION = ".json";
    
    private final ObjectMapper objectMapper;
    private final Path saveDirectory;
    
    public GameStateService() {
        this.objectMapper = new ObjectMapper();
        this.saveDirectory = Paths.get(SAVE_DIR);
        createSaveDirectory();
    }
    
    private void createSaveDirectory() {
        try {
            if (!Files.exists(saveDirectory)) {
                Files.createDirectories(saveDirectory);
                logger.info("Created save directory: {}", saveDirectory);
            }
        } catch (IOException e) {
            logger.error("Failed to create save directory", e);
        }
    }
    
    public void saveGame(GameBoard gameBoard, int player1Score, int player2Score, 
                        String gameMode, int difficulty, String filename) {
        try {
            GameState gameState = new GameState(gameBoard, player1Score, player2Score, 
                                              gameMode, difficulty);
            
            String saveFileName = filename + SAVE_FILE_EXTENSION;
            File saveFile = saveDirectory.resolve(saveFileName).toFile();
            
            objectMapper.writeValue(saveFile, gameState);
            logger.info("Game saved successfully: {}", saveFileName);
            
        } catch (IOException e) {
            logger.error("Failed to save game", e);
            throw new RuntimeException("Failed to save game", e);
        }
    }
    
    public GameState loadGame(String filename) {
        try {
            File saveFile = saveDirectory.resolve(filename + SAVE_FILE_EXTENSION).toFile();
            
            if (!saveFile.exists()) {
                throw new RuntimeException("Save file not found: " + filename);
            }
            
            GameState gameState = objectMapper.readValue(saveFile, GameState.class);
            logger.info("Game loaded successfully: {}", filename);
            
            return gameState;
            
        } catch (IOException e) {
            logger.error("Failed to load game", e);
            throw new RuntimeException("Failed to load game", e);
        }
    }
    
    public List<String> getSavedGames() {
        try {
            return Files.list(saveDirectory)
                    .filter(path -> path.toString().endsWith(SAVE_FILE_EXTENSION))
                    .map(path -> path.getFileName().toString())
                    .map(filename -> filename.replace(SAVE_FILE_EXTENSION, ""))
                    .sorted()
                    .collect(Collectors.toList());
                    
        } catch (IOException e) {
            logger.error("Failed to list saved games", e);
            return new ArrayList<>();
        }
    }
    
    public void deleteSave(String filename) {
        try {
            File saveFile = saveDirectory.resolve(filename + SAVE_FILE_EXTENSION).toFile();
            
            if (saveFile.exists()) {
                Files.delete(saveFile.toPath());
                logger.info("Save file deleted: {}", filename);
            }
            
        } catch (IOException e) {
            logger.error("Failed to delete save file", e);
            throw new RuntimeException("Failed to delete save file", e);
        }
    }
    
    public void autoSave(GameBoard gameBoard, int player1Score, int player2Score,
                        String gameMode, int difficulty) {
        String autoSaveName = "autosave_" + System.currentTimeMillis();
        saveGame(gameBoard, player1Score, player2Score, gameMode, difficulty, autoSaveName);
    }
    
    public void cleanupOldAutoSaves(int maxAutoSaves) {
        try {
            List<File> autoSaves = Files.list(saveDirectory)
                    .filter(path -> path.getFileName().toString().startsWith("autosave_"))
                    .map(Path::toFile)
                    .sorted(Comparator.comparingLong(File::lastModified).reversed())
                    .collect(Collectors.toList());
            
            if (autoSaves.size() > maxAutoSaves) {
                autoSaves.subList(maxAutoSaves, autoSaves.size())
                        .forEach(file -> {
                            try {
                                Files.delete(file.toPath());
                            } catch (IOException e) {
                                logger.error("Failed to delete old auto-save", e);
                            }
                        });
            }
            
        } catch (IOException e) {
            logger.error("Failed to cleanup old auto-saves", e);
        }
    }
}
