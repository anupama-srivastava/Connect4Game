package com.connect4.controller;

import com.connect4.ai.MinimaxAI;
import com.connect4.model.GameBoard;
import com.connect4.model.GameState;
import com.connect4.service.GameStateService;
import com.connect4.statistics.GameStatistics;
import com.connect4.themes.EnhancedThemeManager;
import com.connect4.settings.EnhancedSettings;
import com.connect4.multiplayer.MultiplayerManager;
import com.connect4.util.EnhancedAnimationManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class EnhancedGameController {
    
    @FXML private VBox mainContainer;
    @FXML private GridPane gameGrid;
    @FXML private Label playerTurnLabel;
    @FXML private Label player1ScoreLabel;
    @FXML private Label player2ScoreLabel;
    @FXML private Label gameModeLabel;
    @FXML private Label difficultyLabel;
    @FXML private Button resetButton;
    @FXML private Button newGameButton;
    @FXML private Button settingsButton;
    @FXML private Button statisticsButton;
    @FXML private ComboBox<String> gameModeComboBox;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private ComboBox<EnhancedThemeManager.Theme> themeComboBox;
    @FXML private ProgressBar player1Progress;
    @FXML private ProgressBar player2Progress;
    @FXML private VBox statisticsPanel;
    @FXML private TitledPane multiplayerPanel;
    
    private GameBoard gameBoard;
    private Circle[][] circles;
    private MinimaxAI ai;
    private GameStateService gameStateService;
    private GameStatistics gameStatistics;
    private EnhancedSettings settings;
    private EnhancedAnimationManager animationManager;
    private MultiplayerManager multiplayerManager;
    
    private int player1Score = 0;
    private int player2Score = 0;
    private String currentGameMode = "Human vs Human";
    private int currentDifficulty = 2;
    private boolean isAiTurn = false;
    private long gameStartTime;
    private int moveCount = 0;
    private Map<Integer, Integer> columnUsage = new HashMap<>();
    
    public void initialize() {
        settings = EnhancedSettings.getInstance();
        gameStatistics = new GameStatistics();
        animationManager = new EnhancedAnimationManager();
        multiplayerManager = new MultiplayerManager();
        
        initializeGame();
        initializeUI();
        initializeControls();
        initializeStatistics();
        initializeMultiplayer();
    }
    
    private void initializeGame() {
        gameBoard = new GameBoard();
        circles = new Circle[GameBoard.ROWS][GameBoard.COLS];
        ai = new MinimaxAI(currentDifficulty);
        gameStateService = new GameStateService();
        
        // Initialize column usage tracking
        for (int i = 0; i < GameBoard.COLS; i++) {
            columnUsage.put(i, 0);
        }
    }
    
    private void initializeUI() {
        applyTheme(settings.getCurrentTheme());
        initializeBoardUI();
        initializeHoverEffects();
        initializeAnimations();
    }
    
    private void initializeBoardUI() {
        gameGrid.getChildren().clear();
        
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                Circle circle = createEnhancedCircle();
                circles[row][col] = circle;
                gameGrid.add(circle, col, row);
                
                final int column = col;
                circle.setOnMouseClicked(event -> handleColumnClick(column));
                circle.setOnMouseEntered(event -> handleHover(column));
                circle.setOnMouseExited(event -> handleHoverExit(column));
            }
        }
    }
    
    private Circle createEnhancedCircle() {
        Circle circle = new Circle(30);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.web("#2c3e50"));
        circle.setStrokeWidth(2);
        circle.setEffect(animationManager.createGlowEffect());
        return circle;
    }
    
    private void initializeHoverEffects() {
        // Hover preview functionality
        for (int col = 0; col < GameBoard.COLS; col++) {
            final int column = col;
            for (int row = 0; row < GameBoard.ROWS; row++) {
                Circle circle = circles[row][col];
                circle.setOnMouseEntered(event -> {
                    if (!gameBoard.isColumnFull(column)) {
                        circle.setFill(getHoverColor(gameBoard.getCurrentPlayer()));
                        animationManager.pulseEffect(circle);
                    }
                });
                circle.setOnMouseExited(event -> {
                    int piece = gameBoard.getPiece(row, column);
                    circle.setFill(getPieceColor(piece));
                });
            }
        }
    }
    
    private void initializeAnimations() {
        // Initialize particle effects and smooth animations
        animationManager.initializeParticleSystem(gameGrid);
    }
    
    private void initializeControls() {
        gameModeComboBox.getItems().addAll("Human vs Human", "Human vs AI", "AI vs AI", "LAN Multiplayer");
        gameModeComboBox.setValue(currentGameMode);
        gameModeComboBox.setOnAction(event -> handleGameModeChange());
        
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard", "Expert");
        difficultyComboBox.setValue("Medium");
        difficultyComboBox.setOnAction(event -> handleDifficultyChange());
        
        themeComboBox.getItems().addAll(EnhancedThemeManager.Theme.values());
        themeComboBox.setValue(settings.getCurrentTheme());
        themeComboBox.setOnAction(event -> applyTheme(themeComboBox.getValue()));
        
        resetButton.setOnAction(event -> resetGame());
        newGameButton.setOnAction(event -> newGame());
        settingsButton.setOnAction(event -> openSettings());
        statisticsButton.setOnAction(event -> showStatistics());
    }
    
    private void initializeStatistics() {
        updateStatisticsDisplay();
    }
    
    private void initializeMultiplayer() {
        multiplayerManager.setGameUpdateListener(new MultiplayerManager.GameUpdateListener() {
            @Override
            public void onPlayerJoined(MultiplayerManager.Player player) {
                updatePlayerList();
            }
            
            @Override
            public void onPlayerLeft(MultiplayerManager.Player player) {
                updatePlayerList();
            }
            
            @Override
            public void onGameStarted() {
                startMultiplayerGame();
            }
            
            @Override
            public void onMoveMade(int column, int player) {
                handleRemoteMove(column, player);
            }
            
            @Override
            public void onGameEnded(String winner) {
                handleGameEnd(winner);
            }
        });
    }
    
    private void applyTheme(EnhancedThemeManager.Theme theme) {
        settings.setCurrentTheme(theme);
        
        // Apply theme to all UI elements
        mainContainer.setStyle("-fx-background-color: " + theme.getBackgroundColor() + ";");
        
        // Update circle colors
        updateBoardUI();
        
        // Apply theme to labels and buttons
        playerTurnLabel.setStyle("-fx-text-fill: " + theme.getAccentColor() + ";");
        player1ScoreLabel.setStyle("-fx-text-fill: " + theme.getPlayer1Color() + ";");
        player2ScoreLabel.setStyle("-fx-text-fill: " + theme.getPlayer2Color() + ";");
    }
    
    private void handleColumnClick(int col) {
        if (isAiTurn || gameBoard.isColumnFull(col)) {
            return;
        }
        
        makeMove(col);
    }
    
    private void makeMove(int col) {
        if (gameBoard.dropPiece(col)) {
            moveCount++;
            columnUsage.merge(col, 1, Integer::sum);
            
            animatePieceDrop(col);
            
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
    
    private void animatePieceDrop(int col) {
        int row = gameBoard.getLastMoveRow();
        Circle circle = circles[row][col];
        
        animationManager.animatePieceDrop(circle, getPieceColor(gameBoard.getPiece(row, col)));
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
    
    private void handleWin() {
        String winner = gameBoard.getCurrentPlayer() == 1 ? "Player 1" : "Player 2";
        updateScore(gameBoard.getCurrentPlayer());
        
        // Record game statistics
        long duration = (System.currentTimeMillis() - gameStartTime) / 1000;
        gameStatistics.recordGame(currentGameMode, winner, moveCount, duration, 
                                 getDifficultyString(), columnUsage);
        
        // Animate winning move
        animateWinningMove();
        
        showGameEndDialog(winner + " wins!");
    }
    
    private void handleDraw() {
        long duration = (System.currentTimeMillis() - gameStartTime) / 1000;
        gameStatistics.recordGame(currentGameMode, "Draw", moveCount, duration, 
                                 getDifficultyString(), columnUsage);
        
        showGameEndDialog("It's a draw!");
    }
    
    private void animateWinningMove() {
        List<int[]> winningPositions = gameBoard.getWinningPositions();
        if (winningPositions != null) {
            animationManager.animateWinningPositions(circles, winningPositions);
        }
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
        return piece == 1 ? EnhancedThemeManager.getPlayerColor(1) : 
               piece == 2 ? EnhancedThemeManager.getPlayerColor(2) : Color.WHITE;
    }
    
    private Color getHoverColor(int player) {
        return player == 1 ? EnhancedThemeManager.getPlayerColor(1).deriveColor(0, 1, 1, 0.5) :
               EnhancedThemeManager.getPlayerColor(2).deriveColor(0, 1, 1, 0.5);
    }
    
    private void handleHover(int col) {
        // Visual feedback for hover
    }
    
    private void handleHoverExit(int col) {
        // Reset hover effect
    }
    
    private void handleGameModeChange() {
        currentGameMode = gameModeComboBox.getValue();
        resetGame();
    }
    
    private void handleDifficultyChange() {
        String difficulty = difficultyComboBox.getValue();
        currentDifficulty = switch (difficulty) {
            case "Easy" -> 1;
            case "Medium" -> 2;
            case "Hard" -> 3;
            case "Expert" -> 4;
            default -> 2;
        };
        ai.setDifficulty(currentDifficulty);
    }
    
    private String getDifficultyString() {
        return difficultyComboBox.getValue();
    }
    
    private void updateScore(int winner) {
        if (winner == 1) {
            player1Score++;
            player1ScoreLabel.setText("Player 1: " + player1Score);
        } else {
            player2Score++;
            player2ScoreLabel.setText("Player 2: " + player2Score);
        }
    }
    
    private void updatePlayerTurnLabel() {
        String player = gameBoard.getCurrentPlayer() == 1 ? "Player 1" : "Player 2";
        playerTurnLabel.setText(player + "'s Turn");
    }
    
    private void updateStatisticsDisplay() {
        // Update statistics panel with current game data
    }
    
    private void resetGame() {
        gameBoard.reset();
        moveCount = 0;
        columnUsage.replaceAll((k, v) -> 0);
        gameStartTime = System.currentTimeMillis();
        updateBoardUI();
        updatePlayerTurnLabel();
    }
    
    private void newGame() {
        resetGame();
        player1Score = 0;
        player2Score = 0;
        player1ScoreLabel.setText("Player 1: 0");
        player2ScoreLabel.setText("Player 2: 0");
    }
    
    private void openSettings() {
        // Open enhanced settings dialog
    }
    
    private void showStatistics() {
        // Show comprehensive statistics dialog
    }
    
    private void handleRemoteMove(int column, int player) {
        makeMove(column);
    }
    
    private void startMultiplayerGame() {
        // Initialize multiplayer game
    }
    
    private void showGameEndDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
