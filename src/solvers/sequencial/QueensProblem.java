package solvers.sequencial;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class QueensProblem {
    private int N;
    private int[][] solution;
    private boolean solutionFound;

    public QueensProblem(int N) {
        this.N = N;
        this.solution = new int[N][N];
        this.solutionFound = false;
    }

    public long solve() {
        int[][] board = new int[N][N];
        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo

        solveQueens(board, 0);
        long endTime = System.currentTimeMillis(); // Termina a contagem do tempo

        long executionTime = endTime - startTime; // Calcula o tempo de execução
        printSolution(executionTime);


        return executionTime;
    }

    private boolean solveQueens(int[][] board, int row) {
        if (solutionFound) {
            return true; // Se a solução foi encontrada, não continue
        }

        if (row >= N) {
            copyBoard(board, solution);
            solutionFound = true;
            return true;
        }

        boolean foundSolution = false;
        for (int col = 0; col < N; col++) {
            if (isQueenSafe(board, row, col)) {
                board[row][col] = 1; // Place queen

                foundSolution = solveQueens(board, row + 1) || foundSolution;

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

    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < N; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, N);
        }
    }

    public void printSolution(long executionTime) {
//        if (solutionFound) {
//            System.out.println("Solução encontrada:");
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++) {
//                    System.out.print(solution[i][j] + " ");
//                }
//                System.out.println();
//            }
//        } else {
//            System.out.println("Nenhuma solução encontrada.");
//        }
        String csvFile = "./sequencial.csv";
        String line;
        String delimiter = ",";

        String[][] DATA = {
                {"Name", "Age", "City"},
                {"Alice", "30", "New York"},
                {"Bob", "25", "Los Angeles"},
                {"Charlie", "35", "Chicago"}
        };

        String[] row = {"sequencial", String.valueOf(N), String.valueOf(executionTime)};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
                writer.write(String.join(",", row));
                writer.newLine();
            System.out.println("CSV file created successfully!");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
        System.out.println("Tempo de execução: " + executionTime + " ms");
        System.out.println("N:" + N);
    }

    public static void main(String[] args) {
        int N = 9; // Exemplo com 8 rainhas
        for (int i = 8; i < 30; i++) {
            QueensProblem queens = new QueensProblem(i);
            queens.solve();
        }
    }
}
