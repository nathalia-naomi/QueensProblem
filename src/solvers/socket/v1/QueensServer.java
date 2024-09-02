package solvers.socket.v1;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueensServer {
    private int N;
    private int port;
    private AtomicBoolean solutionFound = new AtomicBoolean(false);
    private long startTime;

    public QueensServer(int N, int port) {
        this.N = N;
        this.port = port;
    }

    public void startServer() {
        startTime = System.currentTimeMillis(); // Inicia a contagem do tempo
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

                if (!solutionFound.get() && !clientSolutions.isEmpty()) {
                    solutionFound.set(true);
                    printSolution(clientSolutions.get(0));
                    break; // Interrompe a aceitação de novos clientes
                }

                in.close();
                out.close();
                clientSocket.close();
            }

            // Calcula e exibe o tempo total de execução
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printSolution(int[][] solution) {
        System.out.println("Solução encontrada:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(solution[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int N = 8;  // Número de rainhas
        int port = 12345;
        QueensServer server = new QueensServer(N, port);
        server.startServer();
    }
}
