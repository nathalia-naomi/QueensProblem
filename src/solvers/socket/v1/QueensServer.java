package solvers.socket.v1;

import solvers.utils.Result;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueensServer {
    private int N;
    private int port;
    private AtomicBoolean solutionFound = new AtomicBoolean(false);
    private long totalClientExecutionTime = 0; // Tempo total de execução dos clientes
    private long serverStartTime;

    public QueensServer(int N, int port) {
        this.N = N;
        this.port = port;
    }

    public void startServer() {
        serverStartTime = System.currentTimeMillis(); // Inicia a contagem do tempo do servidor

        ExecutorService executor = Executors.newFixedThreadPool(N);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            for (int i = 0; i < N; i++) {
                final int row = i;
                executor.submit(() -> handleClientConnection(serverSocket, row));

                if (solutionFound.get()) {
                    break;
                }
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            long serverEndTime = System.currentTimeMillis(); // Tempo total de execução do servidor
            long totalTime = serverEndTime - serverStartTime; // Tempo total de execução (server + clients)

            System.out.println("Tempo total de execução do servidor: " + (totalTime - totalClientExecutionTime) + " ms");
            System.out.println("Tempo total de execução dos clientes: " + totalClientExecutionTime + " ms");
            System.out.println("Tempo total de execução (servidor + clientes): " + totalTime + " ms");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientConnection(ServerSocket serverSocket, int initialRow) {
        try (Socket clientSocket = serverSocket.accept()) {
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.writeObject(initialRow); // Envia o subproblema para o cliente

            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Result result = (Result) in.readObject(); // Recebe a solução e tempo de execução

            totalClientExecutionTime += result.getExecutionTime(); // Soma o tempo de execução do cliente

            if (!solutionFound.get() && result.hasSolution()) {
                solutionFound.set(true);
                printSolution(result.getSolution());
            }

            in.close();
            out.close();
            clientSocket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printSolution(int[][] solution) {
        System.out.println("Solução encontrada:");
        for (int[] row : solution) {
            for (int cell : row) {
                System.out.print(cell + " ");
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
