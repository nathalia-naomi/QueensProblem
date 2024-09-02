package solvers.socket.v2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class QueensClient {
    private int N;
    private String serverAddress;
    private int port;
    private ExecutorService executor;

    public QueensClient(int N, String serverAddress, int port) {
        this.N = N;
        this.serverAddress = serverAddress;
        this.port = port;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void startClient() {
        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Conectado ao servidor...");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                int[] task = (int[]) in.readObject();
                if (task == null) break;

                Future<List<int[][]>> future = executor.submit(() -> solveNQueens(task));
                List<int[][]> solutions = future.get();

                out.writeObject(solutions);
                out.flush();
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (IOException | ClassNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<int[][]> solveNQueens(int[] task) {
        List<int[][]> solutions = new ArrayList<>();
        int[][] board = new int[N][N];
        board[task[0]][0] = 1; // Set the first queen at the given position

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
        int N = 8;  // Exemplo com 8 rainhas
        String serverAddress = "localhost";
        int port = 14456;
        QueensClient client = new QueensClient(N, serverAddress, port);
        client.startClient();
    }
}
