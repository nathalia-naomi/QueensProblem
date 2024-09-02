package solvers.socket.v1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Conectado ao servidor...");

            // Recebe o subproblema do servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            int initialRow = (int) in.readObject();

            // Resolve o subproblema
            List<int[][]> solutions = solve(initialRow);

            // Envia a solução de volta para o servidor
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(solutions);
            out.flush();

            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<int[][]> solve(int initialRow) {
        List<int[][]> solutions = new ArrayList<>();
        int[][] board = new int[N][N];
        board[initialRow][0] = 1;

        solveQueens(board, 1, solutions);
        return solutions;
    }

    private boolean solveQueens(int[][] board, int col, List<int[][]> solutions) {
        if (col >= N) {
            solutions.add(copyBoard(board));
            return true;
        }

        boolean foundSolution = false;
        for (int i = 0; i < N; i++) {
            if (isQueenSafe(board, i, col)) {
                board[i][col] = 1;

                foundSolution |= solveQueens(board, col + 1, solutions);

                board[i][col] = 0;
            }
        }
        return foundSolution;
    }

    private boolean isQueenSafe(int[][] board, int row, int col) {
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
