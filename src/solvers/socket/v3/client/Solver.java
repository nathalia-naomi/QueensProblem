package solvers.socket.v3.client;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    public List<int[][]> solve(int N, int initialRow) {
        List<int[][]> solutions = new ArrayList<>();
        int[][] board = new int[N][N];
        board[initialRow][0] = 1;

        solveQueens(N, board, 1, solutions);
        return solutions;
    }

    private boolean solveQueens(int N, int[][] board, int col, List<int[][]> solutions) {
        if (col >= N) {
            solutions.add(copyBoard(N, board));
            return true;
        }

        boolean foundSolution = false;
        for (int i = 0; i < N; i++) {
            if (isQueenSafe(N, board, i, col)) {
                board[i][col] = 1;

                foundSolution |= solveQueens(N, board, col + 1, solutions);

                board[i][col] = 0;
            }
        }
        return foundSolution;
    }

    private boolean isQueenSafe(int N, int[][] board, int row, int col) {
        for (int i = 0; i < col; i++) {
            if (board[row][i] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        for (int i = row, j = col; j >= 0 && i < N; i++, j--) {
            if (board[i][j] == 1) {
                return false;
            }
        }

        return true;
    }

    private int[][] copyBoard(int N, int[][] board) {
        int[][] newBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, N);
        }
        return newBoard;
    }
}
