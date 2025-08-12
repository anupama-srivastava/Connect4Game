package com.connect4.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class GameState {
    private final int[][] board;
    private final int currentPlayer;
    private final int player1Score;
    private final int player2Score;
    private final LocalDateTime timestamp;
    private final String gameMode;
    private final int difficulty;
    
    @JsonCreator
    public GameState(
            @JsonProperty("board") int[][] board,
            @JsonProperty("currentPlayer") int currentPlayer,
            @JsonProperty("player1Score") int player1Score,
            @JsonProperty("player2Score") int player2Score,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("gameMode") String gameMode,
            @JsonProperty("difficulty") int difficulty) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.timestamp = timestamp;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
    }
    
    public GameState(GameBoard gameBoard, int p1Score, int p2Score, String gameMode, int difficulty) {
        this.board = gameBoard.getBoard();
        this.currentPlayer = gameBoard.getCurrentPlayer();
        this.player1Score = p1Score;
        this.player2Score = p2Score;
        this.timestamp = LocalDateTime.now();
        this.gameMode = gameMode;
        this.difficulty = difficulty;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public int getPlayer1Score() {
        return player1Score;
    }
    
    public int getPlayer2Score() {
        return player2Score;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getGameMode() {
        return gameMode;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
}
