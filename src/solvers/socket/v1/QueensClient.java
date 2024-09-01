package solvers.socket.v1;

import solvers.utils.Result;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class QueensClient {
    private int N;
    private String serverAddress;
    private int port;

    public QueensClient(int N, String serverAddress, int port) {
        this.N = N;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void startClient() {
        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo do cliente

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Conectado ao servidor...");

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            int initialRow = (int) in.readObject(); // Recebe o subproblema do servidor

            List<int[][]> solutions = solveNQueens(initialRow); // Resolve o subproblema

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime; // Tempo de execução do cliente

            System.out.println("Tempo de execução do cliente: " + executionTime + " ms"); // Imprime o tempo de execução do cliente

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Result(solutions, executionTime)); // Envia a solução e o tempo de execução

            in.close();
            out.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<int[][]> solveNQueens(int initialRow) {
        List<int[][]> solutions = new ArrayList<>();
        int[][] board = new int[N][N];
        board[initialRow][0] = 1;

        solveNQueensUtil(board, 1, solutions);
        return solutions;
    }

    private boolean solveNQueensUtil(int[][] board, int col, List<int[][]> solutions) {
        if (col >= N) {
            solutions.add(copyBoard(board));
            return true;
        }

        boolean foundSolution = false;
        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {
                board[i][col] = 1;

                foundSolution |= solveNQueensUtil(board, col + 1, solutions);

                board[i][col] = 0;
            }
        }
        return foundSolution;
    }

    private boolean isSafe(int[][] board, int row, int col) {
        for (int i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        for (int i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] newBoard = new int[N][N];
        for (int i = 0; i < N; i++)
            System.arraycopy(board[i], 0, newBoard[i], 0, N);
        return newBoard;
    }

    public static void main(String[] args) {
        int N = 8;  // Número de rainhas
        String serverAddress = "localhost";
        int port = 12345;

        QueensClient client = new QueensClient(N, serverAddress, port);
        client.startClient();
    }
}
