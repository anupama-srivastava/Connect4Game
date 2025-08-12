package com.connect4.statistics;

import com.connect4.model.GameBoard;
import com.connect4.model.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GameStatistics {
    
    public static class GameRecord {
        private final LocalDateTime timestamp;
        private final String gameMode;
        private final String winner;
        private final int moves;
        private final long duration;
        private final String difficulty;
        private final Map<Integer, Integer> columnUsage;
        
        public GameRecord(LocalDateTime timestamp, String gameMode, String winner, 
                         int moves, long duration, String difficulty, Map<Integer, Integer> columnUsage) {
            this.timestamp = timestamp;
            this.gameMode = gameMode;
            this.winner = winner;
            this.moves = moves;
            this.duration = duration;
            this.difficulty = difficulty;
            this.columnUsage = new HashMap<>(columnUsage);
        }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getGameMode() { return gameMode; }
        public String getWinner() { return winner; }
        public int getMoves() { return moves; }
        public long getDuration() { return duration; }
        public String getDifficulty() { return difficulty; }
        public Map<Integer, Integer> getColumnUsage() { return columnUsage; }
    }
    
    public static class PlayerStats {
        private int totalGames = 0;
        private int wins = 0;
        private int losses = 0;
        private int draws = 0;
        private int currentStreak = 0;
        private int longestStreak = 0;
        private double averageMoves = 0;
        private long totalPlayTime = 0;
        
        public double getWinRate() {
            return totalGames > 0 ? (double) wins / totalGames * 100 : 0;
        }
        
        public String getWinLossRatio() {
            return losses > 0 ? String.format("%.2f", (double) wins / losses) : "N/A";
        }
        
        public void addGame(boolean won, int moves, long duration) {
            totalGames++;
            if (won) {
                wins++;
                currentStreak++;
                longestStreak = Math.max(longestStreak, currentStreak);
            } else {
                losses++;
                currentStreak = 0;
            }
            
            averageMoves = ((averageMoves * (totalGames - 1)) + moves) / totalGames;
            totalPlayTime += duration;
        }
        
        public void addDraw(int moves, long duration) {
            totalGames++;
            draws++;
            currentStreak = 0;
            averageMoves = ((averageMoves * (totalGames - 1)) + moves) / totalGames;
            totalPlayTime += duration;
        }
        
        // Getters
        public int getTotalGames() { return totalGames; }
        public int getWins() { return wins; }
        public int getLosses() { return losses; }
        public int getDraws() { return draws; }
        public int getCurrentStreak() { return currentStreak; }
        public int getLongestStreak() { return longestStreak; }
        public double getAverageMoves() { return averageMoves; }
        public long getTotalPlayTime() { return totalPlayTime; }
    }
    
    private final List<GameRecord> gameHistory = new ArrayList<>();
    private final Map<String, PlayerStats> playerStats = new HashMap<>();
    private final ObservableList<GameRecord> observableGameHistory = FXCollections.observableArrayList();
    
    private static final String STATS_FILE = "game_statistics.json";
    
    public void recordGame(String gameMode, String winner, int moves, long duration, 
                          String difficulty, Map<Integer, Integer> columnUsage) {
        GameRecord record = new GameRecord(
            LocalDateTime.now(),
            gameMode,
            winner,
            moves,
            duration,
            difficulty,
            columnUsage
        );
        
        gameHistory.add(record);
        observableGameHistory.add(record);
        
        // Update player stats
        if (!winner.equals("Draw")) {
            playerStats.computeIfAbsent(winner, k -> new PlayerStats()).addGame(true, moves, duration);
            String loser = winner.equals("Player 1") ? "Player 2" : "Player 1";
            playerStats.computeIfAbsent(loser, k -> new PlayerStats()).addGame(false, moves, duration);
        } else {
            playerStats.computeIfAbsent("Player 1", k -> new PlayerStats()).addDraw(moves, duration);
            playerStats.computeIfAbsent("Player 2", k -> new PlayerStats()).addDraw(moves, duration);
        }
        
        saveStatistics();
    }
    
    public ObservableList<GameRecord> getObservableGameHistory() {
        return observableGameHistory;
    }
    
    public PlayerStats getPlayerStats(String player) {
        return playerStats.getOrDefault(player, new PlayerStats());
    }
    
    public Map<String, PlayerStats> getAllPlayerStats() {
        return new HashMap<>(playerStats);
    }
    
    public List<GameRecord> getRecentGames(int count) {
        return gameHistory.stream()
                .sorted(Comparator.comparing(GameRecord::getTimestamp).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
    
    public Map<Integer, Double> getColumnUsageHeatmap() {
        Map<Integer, Integer> totalUsage = new HashMap<>();
        Map<Integer, Integer> gameCount = new HashMap<>();
        
        for (GameRecord record : gameHistory) {
            record.getColumnUsage().forEach((col, count) -> {
                totalUsage.merge(col, count, Integer::sum);
                gameCount.merge(col, 1, Integer::sum);
            });
        }
        
        Map<Integer, Double> heatmap = new HashMap<>();
        totalUsage.forEach((col, total) -> {
            double avg = (double) total / gameCount.get(col);
            heatmap.put(col, avg);
        });
        
        return heatmap;
    }
    
    public void saveStatistics() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STATS_FILE))) {
            writer.println("Game Statistics Report");
            writer.println("Generated: " + LocalDateTime.now());
            writer.println("=".repeat(50));
            
            for (GameRecord record : gameHistory) {
                writer.printf("Time: %s, Mode: %s, Winner: %s, Moves: %d, Duration: %ds%n",
                    record.getTimestamp(), record.getGameMode(), record.getWinner(),
                    record.getMoves(), record.getDuration());
            }
            
            writer.println("\nPlayer Statistics:");
            writer.println("-".repeat(30));
            playerStats.forEach((player, stats) -> {
                writer.printf("%s: Games: %d, Wins: %d, Losses: %d, Draws: %d, Win Rate: %.1f%%%n",
                    player, stats.getTotalGames(), stats.getWins(), stats.getLosses(),
                    stats.getDraws(), stats.getWinRate());
            });
            
        } catch (IOException e) {
            System.err.println("Error saving statistics: " + e.getMessage());
        }
    }
    
    public void exportToCSV(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Timestamp,GameMode,Winner,Moves,Duration,Difficulty");
            for (GameRecord record : gameHistory) {
                writer.printf("%s,%s,%s,%d,%d,%s%n",
                    record.getTimestamp(), record.getGameMode(), record.getWinner(),
                    record.getMoves(), record.getDuration(), record.getDifficulty());
            }
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }
    
    public void loadStatistics() {
        // Implementation for loading from JSON would go here
        // For now, we'll keep it simple with in-memory storage
    }
}
