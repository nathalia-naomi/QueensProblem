package solvers.sequencial;

import java.util.ArrayList;
import java.util.List;

public class QueensProblem {
    private int N;
    private List<int[][]> solutions;

    public QueensProblem(int N) {
        this.N = N;
        this.solutions = new ArrayList<>();
    }

    public void solve() {
        int[][] board = new int[N][N];
        solveQueens(board, 0);
    }

    private boolean solveQueens(int[][] board, int row) {
        if (row >= N) {
            solutions.add(copyBoard(board));
            return true;
        }

        boolean foundSolution = false;
        for (int col = 0; col < N; col++) {
            if (isQueenSafe(board, row, col)) {
                board[row][col] = 1; // Place queen

                foundSolution |= solveQueens(board, row + 1);

                board[row][col] = 0; // Remove queen (backtrack)
            }
        }
        return foundSolution;
    }

    private boolean isQueenSafe(int[][] board, int row, int col) {
        // Check column
        for (int i = 0; i < row; i++)
            if (board[i][col] == 1)
                return false;

        // Check upper left diagonal
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // Check upper right diagonal
        for (int i = row, j = col; i >= 0 && j < N; i--, j++)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, N);
        }
        return newBoard;
    }

    public void printSolutions() {
        for (int[][] solution : solutions) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(solution[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int N = 15; // Exemplo com 8 rainhas

        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo

        QueensProblem Queens = new QueensProblem(N);
        Queens.solve();

        long endTime = System.currentTimeMillis(); // Termina a contagem do tempo

        long executionTime = endTime - startTime; // Calcula o tempo de execução

//        Queens.printSolutions();
        System.out.println("Tempo de execução: " + executionTime + " ms");
    }
}

