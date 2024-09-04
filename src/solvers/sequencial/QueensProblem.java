package solvers.sequencial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class QueensProblem {
    private int N;
    private int[][] solution;
    private boolean solutionFound;

    public QueensProblem(int N) {
        this.N = N;
        this.solution = new int[N][N];
        this.solutionFound = false;
    }

    // metodo define as threads para resolução do problema
    public long solve() {
        int[][] board = new int[N][N];
        long startTime = System.currentTimeMillis(); // inicia a contagem do tempo

        solveQueens(board, 0);
        long endTime = System.currentTimeMillis(); // termina a contagem do tempo

        long executionTime = endTime - startTime; // calcula o tempo de execução
        printSolution(board);

        System.out.println("Tempo total de execução: " + (executionTime) + " ms");

        return executionTime;
    }

    private boolean solveQueens(int[][] board, int row) {
        if (solutionFound) {
            return true; // se a solução foi encontrada, não continue
        }

        if (row >= N) { // verifica se todas as rainhas foram colocadas
            copyBoard(board, solution);
            solutionFound = true;
            return true;
        }

        boolean foundSolution = false;
        // tentativa de colocar a rainha em cada linha da coluna atual
        for (int col = 0; col < N; col++) {
            if (isQueenSafe(board, row, col)) {
                board[row][col] = 1;

                foundSolution = solveQueens(board, row + 1) || foundSolution;

                board[row][col] = 0;
            }
        }
        return foundSolution;
    }

    // verifica se a casa do board é segura para colocar uma rainha,
    // recebe o board e as cordenadas da casa como parametros
    private boolean isQueenSafe(int[][] board, int row, int col) {
        // verifica a se a linha é segura
        for (int i = 0; i < row; i++)
            if (board[i][col] == 1)
                return false;

        // verifica a se a diagonal superior é segura
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // verifica a se a diagonal inferior é segura
        for (int i = row, j = col; i >= 0 && j < N; i--, j++)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    // copia o tabuleiro de origem para o tabuleiro de destino
    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < N; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, N);
        }
    }

    // imprime a solução encontrada de forma sincronizada
    private void printSolution(int[][] board) {
        if (solutionFound) {
            System.out.println("Solução encontrada:");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(solution[i][j] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Nenhuma solução encontrada.");
        }
    }

    // escreve o valor de N e o tempo de execução em um arquivo csv
    public void printSolution(long executionTime) {
        String csvFile = "./sequencial.csv";
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
        int N = 8; // Exemplo com 8 rainhas
        QueensProblem queens = new QueensProblem(N);
        queens.solve();

    }
}
