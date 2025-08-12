package com.connect4.model;

import java.util.Arrays;

public class GameBoard {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    private final int[][] board;
    private int currentPlayer;
    
    public GameBoard() {
        board = new int[ROWS][COLS];
        currentPlayer = 1;
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }
    }
    
    public boolean dropPiece(int col) {
        if (col < 0 || col >= COLS || isColumnFull(col)) {
            return false;
        }
        
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = currentPlayer;
                return true;
            }
        }
        return false;
    }
    
    public boolean isColumnFull(int col) {
        return board[0][col] != 0;
    }
    
    public boolean checkWin(int player) {
        return checkHorizontalWin(player) || 
               checkVerticalWin(player) || 
               checkDiagonalWin(player);
    }
    
    private boolean checkHorizontalWin(int player) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player &&
                    board[row][col + 1] == player &&
                    board[row][col + 2] == player &&
                    board[row][col + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkVerticalWin(int player) {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row <= ROWS - 4; row++) {
                if (board[row][col] == player &&
                    board[row + 1][col] == player &&
                    board[row + 2][col] == player &&
                    board[row + 3][col] == player) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkDiagonalWin(int player) {
        // Check diagonal (bottom-left to top-right)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player &&
                    board[row - 1][col + 1] == player &&
                    board[row - 2][col + 2] == player &&
                    board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }
        
        // Check diagonal (top-left to bottom-right)
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player &&
                    board[row + 1][col + 1] == player &&
                    board[row + 2][col + 2] == player &&
                    board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (!isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }
    
    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getPiece(int row, int col) {
        return board[row][col];
    }
    
    public void reset() {
        initializeBoard();
        currentPlayer = 1;
    }
    
    public void setCurrentPlayer(int player) {
        if (player == 1 || player == 2) {
            this.currentPlayer = player;
        }
    }
}
