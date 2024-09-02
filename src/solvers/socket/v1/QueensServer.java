package solvers.socket.v1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
        // startTime = System.currentTimeMillis(); // Inicia a contagem do tempo
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado e aguardando conexões...");

            Soluction clientSolutions = null;

            int i = this.N;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

                // Envia o subproblema para o cliente
                try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    System.out.println("Cliente conectado: " + clientSocket.getInetAddress() + "\nEnviando subproblema: N = " + N + ", linha inicial = " + i);

                    out.writeObject(this.N); // Envia o valor de N
                    out.flush();
                    out.writeObject(i); // Envia a linha inicial
                    out.flush();

                    // Criação do ObjectInputStream após envio dos dados
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                    // Recebe a solução do cliente
                    clientSolutions = (Soluction) in.readObject();

                    if (!solutionFound.get() && !clientSolutions.soluctions.isEmpty()) {
                        solutionFound.set(true);
                        printSolution(clientSolutions.soluctions.get(0));
                        break; // Interrompe a aceitação de novas soluções
                    }

                    // Se uma solução já foi encontrada, sair do loop
                    if (solutionFound.get()) {
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();
                }

                --i;

                // Se uma solução foi encontrada, não aceitar mais clientes
                if (solutionFound.get()) {
                    break;
                }
            }

            if (clientSolutions == null || clientSolutions.soluctions.isEmpty()) {
                System.out.println("Nenhuma solução encontrada.");
            } else {
                // Calcula e exibe o tempo total de execução
                System.out.println("Tempo total de execução: " + (clientSolutions.endTime - clientSolutions.startTime) + "ms");
            }

        } catch (IOException e) {
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
        int port = 14456;
        QueensServer server = new QueensServer(N, port);
        server.startServer();
    }
}
