package com.connect4.ai;

import com.connect4.model.GameBoard;

public class MinimaxAI {
    private final int difficulty;
    private final int maxDepth;
    
    public MinimaxAI(int difficulty) {
        this.difficulty = difficulty;
        this.maxDepth = switch (difficulty) {
            case 1 -> 2; // Easy
            case 2 -> 4; // Medium
            case 3 -> 6; // Hard
            default -> 4;
        };
    }
    
    public int getBestMove(GameBoard board) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
        
        for (int col = 0; col < GameBoard.COLS; col++) {
            if (board.isColumnFull(col)) continue;
            
            GameBoard tempBoard = copyBoard(board);
            tempBoard.dropPiece(col);
            
            int score = minimax(tempBoard, maxDepth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = col;
            }
        }
        
        return bestMove;
    }
    
    private int minimax(GameBoard board, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || board.checkWin(1) || board.checkWin(2) || board.isBoardFull()) {
            return evaluateBoard(board);
        }
        
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < GameBoard.COLS; col++) {
                if (board.isColumnFull(col)) continue;
                
                GameBoard tempBoard = copyBoard(board);
                tempBoard.dropPiece(col);
                tempBoard.switchPlayer();
                
                int eval = minimax(tempBoard, depth - 1, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < GameBoard.COLS; col++) {
                if (board.isColumnFull(col)) continue;
                
                GameBoard tempBoard = copyBoard(board);
                tempBoard.dropPiece(col);
                tempBoard.switchPlayer();
                
                int eval = minimax(tempBoard, depth - 1, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }
    
    private int evaluateBoard(GameBoard board) {
        int score = 0;
        
        // Check for wins
        if (board.checkWin(2)) {
            return 1000;
        }
        if (board.checkWin(1)) {
            return -1000;
        }
        
        // Evaluate positions
        score += evaluatePosition(board, 2);
        score -= evaluatePosition(board, 1);
        
        return score;
    }
    
    private int evaluatePosition(GameBoard board, int player) {
        int score = 0;
        
        // Center column preference
        int[] centerArray = new int[GameBoard.ROWS];
        for (int row = 0; row < GameBoard.ROWS; row++) {
            centerArray[row] = board.getPiece(row, GameBoard.COLS / 2);
        }
        int centerCount = countConsecutive(centerArray, player);
        score += centerCount * 3;
        
        // Horizontal scoring
        for (int row = 0; row < GameBoard.ROWS; row++) {
            int[] rowArray = new int[GameBoard.COLS];
            for (int col = 0; col < GameBoard.COLS; col++) {
                rowArray[col] = board.getPiece(row, col);
            }
            score += evaluateLine(rowArray, player);
        }
        
        // Vertical scoring
        for (int col = 0; col < GameBoard.COLS; col++) {
            int[] colArray = new int[GameBoard.ROWS];
            for (int row = 0; row < GameBoard.ROWS; row++) {
                colArray[row] = board.getPiece(row, col);
            }
            score += evaluateLine(colArray, player);
        }
        
        // Diagonal scoring
        score += evaluateDiagonals(board, player);
        
        return score;
    }
    
    private int evaluateLine(int[] line, int player) {
        int score = 0;
        
        for (int i = 0; i < line.length - 3; i++) {
            int count = 0;
            int empty = 0;
            
            for (int j = 0; j < 4; j++) {
                if (line[i + j] == player) {
                    count++;
                } else if (line[i + j] == 0) {
                    empty++;
                }
            }
            
            if (count == 4) {
                score += 100;
            } else if (count == 3 && empty == 1) {
                score += 5;
            } else if (count == 2 && empty == 2) {
                score += 2;
            }
        }
        
        return score;
    }
    
    private int evaluateDiagonals(GameBoard board, int player) {
        int score = 0;
        
        // Positive slope diagonals
        for (int row = 3; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                int[] diag = new int[4];
                for (int i = 0; i < 4; i++) {
                    diag[i] = board.getPiece(row - i, col + i);
                }
                score += evaluateLine(diag, player);
            }
        }
        
        // Negative slope diagonals
        for (int row = 0; row < GameBoard.ROWS - 3; row++) {
            for (int col = 0; col < GameBoard.COLS - 3; col++) {
                int[] diag = new int[4];
                for (int i = 0; i < 4; i++) {
                    diag[i] = board.getPiece(row + i, col + i);
                }
                score += evaluateLine(diag, player);
            }
        }
        
        return score;
    }
    
    private int countConsecutive(int[] array, int player) {
        int count = 0;
        for (int value : array) {
            if (value == player) {
                count++;
            }
        }
        return count;
    }
    
    private GameBoard copyBoard(GameBoard original) {
        GameBoard copy = new GameBoard();
        int[][] originalBoard = original.getBoard();
        
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                copy.getBoard()[row][col] = originalBoard[row][col];
            }
        }
        copy.setCurrentPlayer(original.getCurrentPlayer());
        
        return copy;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
}
