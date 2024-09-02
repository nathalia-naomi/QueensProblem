package solvers.socket.v1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class QueensClient {

    private String serverAddress;
    private int port;

    public QueensClient(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void startClient() {
        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Conectado ao servidor...");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());     

            out.flush();

            out.writeObject(8); // Envia o valor de N

            // Recebe o valor de N e a linha inicial do subproblema do servidor
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            int N = (int) in.readObject();
            int initialRow = (int) in.readObject();

            System.out.println("Recebido subproblema: N = " + N + ", linha inicial = " + initialRow);

            Soluction soluction = new Soluction();

 
            // Inicia a contagem do tempo
            soluction.startTime = System.currentTimeMillis();

            // Resolve o subproblema
            List<int[][]> solutions = solve(N, initialRow);

            // Finaliza a contagem do tempo
            soluction.endTime = System.currentTimeMillis();

            soluction.soluctions = solutions;

            // Envia a solução de volta para o servidor
            out.writeObject(soluction);
            out.flush();

            // Fecha streams e socket
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<int[][]> solve(int N, int initialRow) {
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

    public static void main(String[] args) {
        int N = 8;  // Número de rainhas
        String serverAddress = "localhost";
        int port = 14456;
        QueensClient client = new QueensClient(serverAddress, port);
        client.startClient();
    }
}
