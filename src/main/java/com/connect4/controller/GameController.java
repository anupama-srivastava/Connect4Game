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
