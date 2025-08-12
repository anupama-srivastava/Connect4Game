package com.connect4.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {
    
    private GameBoard gameBoard;
    
    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard();
    }
    
    @Test
    @DisplayName("Should initialize board with correct dimensions")
    void testBoardInitialization() {
        assertEquals(GameBoard.ROWS, 6);
        assertEquals(GameBoard.COLS, 7);
        
        int[][] board = gameBoard.getBoard();
        assertEquals(board.length, GameBoard.ROWS);
        assertEquals(board[0].length, GameBoard.COLS);
        
        // Verify all cells are initially empty (0)
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                assertEquals(0, gameBoard.getPiece(row, col));
            }
        }
    }
    
    @Test
    @DisplayName("Should correctly identify valid and invalid column drops")
    void testDropPieceValidation() {
        // Valid drop
        assertTrue(gameBoard.dropPiece(0));
        assertEquals(1, gameBoard.getPiece(5, 0)); // Bottom row
        
        // Invalid column (out of bounds)
        assertFalse(gameBoard.dropPiece(-1));
        assertFalse(gameBoard.dropPiece(7));
        
        // Fill column completely
        for (int i = 0; i < GameBoard.ROWS; i++) {
            gameBoard.dropPiece(0);
            gameBoard.switchPlayer();
        }
        
        // Column should now be full
        assertFalse(gameBoard.dropPiece(0));
    }
    
    @Test
    @DisplayName("Should stack pieces correctly in columns")
    void testPieceStacking() {
        // Drop 3 pieces in column 0
        gameBoard.dropPiece(0); // Player 1
        gameBoard.switchPlayer();
        gameBoard.dropPiece(0); // Player 2
        gameBoard.switchPlayer();
        gameBoard.dropPiece(0); // Player 1
        
        assertEquals(1, gameBoard.getPiece(5, 0)); // Bottom
        assertEquals(2, gameBoard.getPiece(4, 0)); // Middle
        assertEquals(1, gameBoard.getPiece(3, 0)); // Top
    }
    
    @Test
    @DisplayName("Should detect horizontal wins correctly")
    void testHorizontalWin() {
        // Create horizontal win for player 1
        for (int col = 0; col < 4; col++) {
            gameBoard.dropPiece(col);
            gameBoard.switchPlayer();
        }
        
        assertTrue(gameBoard.checkWin(1));
        assertFalse(gameBoard.checkWin(2));
    }
    
    @Test
    @DisplayName("Should detect vertical wins correctly")
    void testVerticalWin() {
        // Create vertical win for player 1
        for (int i = 0; i < 4; i++) {
            gameBoard.dropPiece(0);
            gameBoard.switchPlayer();
        }
        
        assertTrue(gameBoard.checkWin(1));
        assertFalse(gameBoard.checkWin(2));
    }
    
    @Test
    @DisplayName("Should detect diagonal wins (positive slope)")
    void testDiagonalWinPositiveSlope() {
        // Create diagonal win (bottom-left to top-right)
        int[][] moves = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}
        };
        
        for (int[] move : moves) {
            for (int i = 0; i < move[0]; i++) {
                gameBoard.dropPiece(move[1]);
                gameBoard.switchPlayer();
            }
            gameBoard.dropPiece(move[1]);
            gameBoard.switchPlayer();
        }
        
        // Final move to create diagonal
        gameBoard.dropPiece(3);
        
        assertTrue(gameBoard.checkWin(1));
    }
    
    @Test
    @DisplayName("Should detect diagonal wins (negative slope)")
    void testDiagonalWinNegativeSlope() {
        // Create diagonal win (top-left to bottom-right)
        for (int col = 0; col < 4; col++) {
            for (int i = 0; i < col; i++) {
                gameBoard.dropPiece(col);
                gameBoard.switchPlayer();
            }
            gameBoard.dropPiece(col);
            gameBoard.switchPlayer();
        }
        
        // Final move to create diagonal
        gameBoard.dropPiece(3);
        
        assertTrue(gameBoard.checkWin(1));
    }
    
    @Test
    @DisplayName("Should detect board full condition")
    void testBoardFull() {
        // Fill entire board without any wins
        for (int col = 0; col < GameBoard.COLS; col++) {
            for (int row = 0; row < GameBoard.ROWS; row++) {
                gameBoard.dropPiece(col);
                gameBoard.switchPlayer();
            }
        }
        
        assertTrue(gameBoard.isBoardFull());
    }
    
    @Test
    @DisplayName("Should correctly identify column full status")
    void testIsColumnFull() {
        assertFalse(gameBoard.isColumnFull(0));
        
        // Fill column 0
        for (int i = 0; i < GameBoard.ROWS; i++) {
            gameBoard.dropPiece(0);
            gameBoard.switchPlayer();
        }
        
        assertTrue(gameBoard.isColumnFull(0));
        assertFalse(gameBoard.isColumnFull(1));
    }
    
    @Test
    @DisplayName("Should switch players correctly")
    void testPlayerSwitching() {
        assertEquals(1, gameBoard.getCurrentPlayer());
        
        gameBoard.switchPlayer();
        assertEquals(2, gameBoard.getCurrentPlayer());
        
        gameBoard.switchPlayer();
        assertEquals(1, gameBoard.getCurrentPlayer());
    }
    
    @Test
    @DisplayName("Should reset board to initial state")
    void testReset() {
        // Make some moves
        gameBoard.dropPiece(0);
        gameBoard.dropPiece(1);
        gameBoard.switchPlayer();
        
        // Reset
        gameBoard.reset();
        
        // Verify reset
        assertEquals(1, gameBoard.getCurrentPlayer());
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                assertEquals(0, gameBoard.getPiece(row, col));
            }
        }
    }
    
    @Test
    @DisplayName("Should handle edge cases in win detection")
    void testEdgeCaseWins() {
        // Test win at board edges
        // Horizontal win at bottom row
        for (int col = 0; col < 4; col++) {
            gameBoard.dropPiece(col);
            gameBoard.switchPlayer();
        }
        assertTrue(gameBoard.checkWin(1));
        
        gameBoard.reset();
        
        // Vertical win at edge column
        for (int i = 0; i < 4; i++) {
            gameBoard.dropPiece(0);
            gameBoard.switchPlayer();
        }
        assertTrue(gameBoard.checkWin(1));
    }
    
    @Test
    @DisplayName("Should not detect false wins")
    void testNoFalseWins() {
        // Create non-winning pattern
        gameBoard.dropPiece(0);
        gameBoard.switchPlayer();
        gameBoard.dropPiece(1);
        gameBoard.switchPlayer();
        gameBoard.dropPiece(2);
        gameBoard.switchPlayer();
        
        assertFalse(gameBoard.checkWin(1));
        assertFalse(gameBoard.checkWin(2));
    }
}
