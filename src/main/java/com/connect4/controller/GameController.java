package com.connect4.controller;

import com.connect4.ai.MinimaxAI;
import com.connect4.model.GameBoard;
import com.connect4.model.GameState;
import com.connect4.service.GameStateService;
import com.connect4.util.AnimationManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Optional;

public class GameController {
    
    @FXML private GridPane gameGrid;
    @FXML private Label playerTurnLabel;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    @FXML private Button resetButton;
    @FXML private Button newGameButton;
    @FXML private ComboBox<String> gameModeComboBox;
    @FXML private ComboBox<Integer> difficultyComboBox;
    @FXML private MenuItem saveGameMenuItem;
    @FXML private MenuItem loadGameMenuItem;
    @FXML private MenuItem undoMoveMenuItem;
    
    private GameBoard gameBoard;
    private Circle[][] circles;
    private MinimaxAI ai;
    private GameStateService gameStateService;
    private int player1Score = 0;
    private int player2Score = 0;
    private String currentGameMode = "Human vs Human";
    private int currentDifficulty = 2;
    private boolean isAiTurn = false;
    
    public void initialize() {
        gameBoard = new GameBoard();
        circles = new Circle[GameBoard.ROWS][GameBoard.COLS];
        ai = new MinimaxAI(currentDifficulty);
        gameStateService = new GameStateService();
        
        initializeBoardUI();
        initializeGameControls();
        updatePlayerTurnLabel();
    }
    
    private void initializeBoardUI() {
        gameGrid.getChildren().clear();
        
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                Circle circle = createCircle();
                circles[row][col] = circle;
                gameGrid.add(circle, col, row);
                
                final int column = col;
                circle.setOnMouseClicked(event -> handleColumnClick(column));
            }
        }
    }
    
    private void initializeGameControls() {
        gameModeComboBox.getItems().addAll("Human vs Human", "Human vs AI", "AI vs AI");
        gameModeComboBox.setValue(currentGameMode);
        gameModeComboBox.setOnAction(event -> handleGameModeChange());
        
        difficultyComboBox.getItems().addAll(1, 2, 3);
        difficultyComboBox.setValue(currentDifficulty);
        difficultyComboBox.setOnAction(event -> handleDifficultyChange());
    }
    
    private Circle createCircle() {
        Circle circle = new Circle(30);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.web("#2c3e50"));
        circle.setStrokeWidth(2);
        return circle;
    }
    
    private void handleColumnClick(int col) {
        if (isAiTurn || gameBoard.isColumnFull(col)) {
            return;
        }
        
        makeMove(col);
    }
    
    private void makeMove(int col) {
        if (gameBoard.dropPiece(col)) {
            updateBoardUI();
            
            if (gameBoard.checkWin(gameBoard.getCurrentPlayer())) {
                handleWin();
            } else if (gameBoard.isBoardFull()) {
                handleDraw();
            } else {
                gameBoard.switchPlayer();
                updatePlayerTurnLabel();
                
                if (currentGameMode.equals("Human vs AI") && gameBoard.getCurrentPlayer() == 2) {
                    handleAIMove();
                } else if (currentGameMode.equals("AI vs AI")) {
                    handleAIMove();
                }
            }
        }
    }
    
    private void handleAIMove() {
        isAiTurn = true;
        
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
        pause.setOnFinished(event -> {
            int aiMove = ai.getBestMove(gameBoard);
            makeMove(aiMove);
            isAiTurn = false;
        });
        pause.play();
    }
    
    private void updateBoardUI() {
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                int piece = gameBoard.getPiece(row, col);
                Color color = getPieceColor(piece);
                circles[row][col].setFill(color);
            }
        }
    }
    
    private Color getPieceColor(int piece) {
        return switch (piece) {
            case 1 -> Color.web("#e74c3c"); // Red for Player 1
            case 2 -> Color.web("#f39c12"); // Yellow for Player 2
            default -> Color.WHITE; // Empty
        };
    }
    
    private void updatePlayerTurnLabel() {
        String player = gameBoard.getCurrentPlayer() == 1 ? "Player 1" : "Player 2";
        playerTurnLabel.setText(player + "'s Turn");
    }
    
    private void handleWin() {
        String winner = gameBoard.getCurrentPlayer() == 1 ? "Player 1" : "Player 2";
        
        if (gameBoard.getCurrentPlayer() == 1) {
            player1Score++;
        } else {
            player2Score++;
        }
        
        updateScoreLabels();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(winner + " wins!");
        alert.showAndWait();
        
        resetGame();
    }
    
    private void handleDraw() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("It's a draw!");
        alert.showAndWait();
        
        resetGame();
    }
    
    private void updateScoreLabels() {
        player1ScoreLabel.setText("Player 1: " + player1Score);
        player2ScoreLabel.setText("Player 2: " + player2Score);
    }
    
    @FXML
    private void resetGame() {
        gameBoard.reset();
        updateBoardUI();
        updatePlayerTurnLabel();
        isAiTurn = false;
    }
    
    @FXML
    private void newGame() {
        resetGame();
        player1Score = 0;
        player2Score = 0;
        updateScoreLabels();
    }
    
    private void handleGameModeChange() {
        currentGameMode = gameModeComboBox.getValue();
        resetGame();
    }
    
    private void handleDifficultyChange() {
        currentDifficulty = difficultyComboBox.getValue();
        ai = new MinimaxAI(currentDifficulty);
    }
    
    @FXML
    private void saveGame() {
        try {
            TextInputDialog dialog = new TextInputDialog("game_save");
            dialog.setTitle("Save Game");
            dialog.setHeaderText("Save Current Game");
            dialog.setContentText("Enter save name:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(filename -> {
                gameStateService.saveGame(gameBoard, player1Score, player2Score, 
                                        currentGameMode, currentDifficulty, filename);
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to save game");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void loadGame() {
        try {
            List<String> savedGames = gameStateService.getSavedGames();
            if (savedGames.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Saves");
                alert.setHeaderText(null);
                alert.setContentText("No saved games found.");
                alert.showAndWait();
                return;
            }
            
            ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Load Saved Game");
            dialog.setContentText("Choose a saved game:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(filename -> {
                try {
                    GameState gameState = gameStateService.loadGame(filename);
                    gameBoard.setCurrentPlayer(gameState.getCurrentPlayer());
                    
                    // Restore board state
                    int[][] board = gameState.getBoard();
                    for (int row = 0; row < GameBoard.ROWS; row++) {
                        for (int col = 0; col < GameBoard.COLS; col++) {
                            gameBoard.getBoard()[row][col] = board[row][col];
                        }
                    }
                    
                    player1Score = gameState.getPlayer1Score();
                    player2Score = gameState.getPlayer2Score();
                    currentGameMode = gameState.getGameMode();
                    currentDifficulty = gameState.getDifficulty();
                    
                    updateBoardUI();
                    updateScoreLabels();
                    updatePlayerTurnLabel();
                    
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load game");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load saved games");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void undoMove() {
        // This would require implementing move history
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Undo Move");
        alert.setHeaderText(null);
        alert.setContentText("Undo functionality not yet implemented.");
        alert.showAndWait();
    }
