package solvers.socket.v1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

class QueensServer {
    private int N;
    private int port;
    private List<int[][]> solutions = new ArrayList<>();

    public QueensServer(int N, int port) {
        this.N = N;
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            for (int i = 0; i < N; i++) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Envia o subproblema para o cliente
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(i);
                out.flush();

                // Recebe a solução do cliente
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                List<int[][]> clientSolutions = (List<int[][]>) in.readObject();
                solutions.addAll(clientSolutions);

                in.close();
                out.close();
                clientSocket.close();
            }

            System.out.println("Todas as soluções foram recebidas.");
            printSolutions();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printSolutions() {
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
        int N = 8;  // Exemplo com 8 rainhas
        int port = 12345;
        QueensServer server = new QueensServer(N, port);
        server.startServer();
    }
}
