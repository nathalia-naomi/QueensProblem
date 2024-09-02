package solvers.socket.v2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class QueensServer {
    private int N;
    private int port;
    private List<int[][]> solutions = new ArrayList<>();
    private BlockingQueue<int[]> tasksQueue = new LinkedBlockingQueue<>();

    public QueensServer(int N, int port) {
        this.N = N;
        this.port = port;
        generateTasks();
    }

    private void generateTasks() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tasksQueue.add(new int[]{i, j});
            }
        }
    }

    public void startServer() {
        ExecutorService executor = Executors.newFixedThreadPool(N);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            while (!tasksQueue.isEmpty()) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);

            System.out.println("Todas as soluções foram recebidas.");
            printSolutions();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            int[] task;
            while ((task = tasksQueue.poll()) != null) {
                out.writeObject(task);
                out.flush();

                List<int[][]> clientSolutions = (List<int[][]>) in.readObject();
                if (clientSolutions != null) {
                    synchronized (solutions) {
                        solutions.addAll(clientSolutions);
                    }
                }
            }
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
        int port = 14456;
        QueensServer server = new QueensServer(N, port);
        server.startServer();
    }
}
