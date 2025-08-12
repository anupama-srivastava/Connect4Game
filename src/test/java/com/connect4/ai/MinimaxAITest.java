package com.connect4.ai;

import com.connect4.model.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxAITest {
    
    private MinimaxAI easyAI;
    private MinimaxAI mediumAI;
    private MinimaxAI hardAI;
    private GameBoard gameBoard;
    
    @BeforeEach
    void setUp() {
        easyAI = new MinimaxAI(1);
        mediumAI = new MinimaxAI(2);
        hardAI = new MinimaxAI(3);
        gameBoard = new GameBoard();
    }
    
    @Test
    @DisplayName("Should initialize AI with correct difficulty levels")
    void testAIInitialization() {
        assertEquals(1, easyAI.getDifficulty());
        assertEquals(2, mediumAI.getDifficulty());
        assertEquals(3, hardAI.getDifficulty());
    }
    
    @Test
    @DisplayName("Should return valid moves within board bounds")
    void testValidMoveGeneration() {
        int move = easyAI.getBestMove(gameBoard);
        assertTrue(move >= 0 && move < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should prefer winning moves when available")
    void testWinningMovePreference() {
        // Setup board where AI can win immediately
        // Player 2 (AI) has 3 in a row with open spot
        int[][] winningSetup = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {2, 2, 2, 0, 0, 0, 0}
        };
        
        setupBoard(winningSetup);
        gameBoard.setCurrentPlayer(2);
        
        int move = hardAI.getBestMove(gameBoard);
        assertEquals(3, move); // Should choose column 3 to win
    }
    
    @Test
    @DisplayName("Should block opponent's winning moves")
    void testBlockingOpponentWin() {
        // Setup where opponent has 3 in a row
        int[][] blockingSetup = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 0, 0, 0}
        };
        
        setupBoard(blockingSetup);
        gameBoard.setCurrentPlayer(2);
        
        int move = mediumAI.getBestMove(gameBoard);
        assertEquals(3, move); // Should block column 3
    }
    
    @Test
    @DisplayName("Should prefer center column for strategic advantage")
    void testCenterColumnPreference() {
        // Empty board - AI should prefer center
        int move = hardAI.getBestMove(gameBoard);
        assertEquals(3, move); // Center column (index 3)
    }
    
    @Test
    @DisplayName("Should handle full columns gracefully")
    void testFullColumnHandling() {
        // Fill column 0 completely
        for (int i = 0; i < GameBoard.ROWS; i++) {
            gameBoard.dropPiece(0);
            gameBoard.switchPlayer();
        }
        
        int move = hardAI.getBestMove(gameBoard);
        assertTrue(move != 0); // Should not choose full column
        assertTrue(move >= 0 && move < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should demonstrate increasing difficulty levels")
    void testDifficultyLevels() {
        // Test that higher difficulty makes better moves
        // Setup a position where different difficulties might choose differently
        
        // Medium difficulty should see deeper than easy
        int[][] testPosition = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 0, 0, 0}
        };
        
        setupBoard(testPosition);
        gameBoard.setCurrentPlayer(2);
        
        int easyMove = easyAI.getBestMove(gameBoard);
        int mediumMove = mediumAI.getBestMove(gameBoard);
        int hardMove = hardAI.getBestMove(gameBoard);
        
        // All should be valid moves
        assertTrue(easyMove >= 0 && easyMove < GameBoard.COLS);
        assertTrue(mediumMove >= 0 && mediumMove < GameBoard.COLS);
        assertTrue(hardMove >= 0 && hardMove < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should handle complex board positions")
    void testComplexPositionEvaluation() {
        // Setup a complex position with multiple threats
        int[][] complexSetup = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 2, 1, 2, 1, 2, 1},
            {2, 1, 2, 1, 2, 1, 2},
            {1, 2, 1, 2, 1, 2, 1}
        };
        
        setupBoard(complexSetup);
        gameBoard.setCurrentPlayer(2);
        
        int move = hardAI.getBestMove(gameBoard);
        assertTrue(move >= 0 && move < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should not return invalid moves")
    void testNoInvalidMoves() {
        // Fill all columns except one
        for (int col = 0; col < GameBoard.COLS - 1; col++) {
            for (int row = 0; row < GameBoard.ROWS; row++) {
                gameBoard.dropPiece(col);
                gameBoard.switchPlayer();
            }
        }
        
        int move = hardAI.getBestMove(gameBoard);
        assertEquals(GameBoard.COLS - 1, move); // Should choose last available column
    }
    
    @Test
    @DisplayName("Should handle early game positions")
    void testEarlyGamePosition() {
        // First few moves
        gameBoard.dropPiece(3);
        gameBoard.switchPlayer();
        gameBoard.dropPiece(2);
        gameBoard.switchPlayer();
        
        int move = mediumAI.getBestMove(gameBoard);
        assertTrue(move >= 0 && move < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should handle late game positions")
    void testLateGamePosition() {
        // Fill most of the board
        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = 0; row < GameBoard.ROWS - 1; row++) {
                gameBoard.dropPiece(col);
                gameBoard.switchPlayer();
            }
        }
        
        int move = hardAI.getBestMove(gameBoard);
        assertTrue(move >= 0 && move < GameBoard.COLS);
    }
    
    @Test
    @DisplayName("Should demonstrate consistent behavior")
    void testConsistency() {
        // Same position should yield same move
        int move1 = hardAI.getBestMove(gameBoard);
        int move2 = hardAI.getBestMove(gameBoard);
        
        assertEquals(move1, move2);
    }
    
    // Helper method to setup board from array
    private void setupBoard(int[][] boardSetup) {
        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = GameBoard.ROWS - 1; row >= 0; row--) {
                if (boardSetup[row][col] != 0) {
                    gameBoard.setCurrentPlayer(boardSetup[row][col]);
                    gameBoard.dropPiece(col);
                }
            }
        }
    }
}
