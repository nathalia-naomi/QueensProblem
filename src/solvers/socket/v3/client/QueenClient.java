package solvers.socket.v3.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import solvers.socket.v3.utils.Soluction;

public class QueenClient {

    public static void main(String[] args) {

        int threadsQuantity = 4;

        ArrayList<Thread> clients = new ArrayList<>();

        for (int i = 0; i < threadsQuantity; i++) {
            Thread client = new Thread(() -> {
                QueenClient queenClient = new QueenClient();
                queenClient.start();
            });
            clients.add(client);
            client.start();
        }

        for (Thread client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                System.out.println("Error joining threads: " + e.getMessage());
            }
        }
    }

    public void start() {
        String hostname = "localhost";
        int port = 3011;

        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Conectado com o servidor");

            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                System.out.println("Recebendo N do servidor");

                int N = (int) in.readObject();

                System.out.println("Recebendo initialRow do servidor: " + N);

                int initialRow = (int) in.readObject();

                System.out.println("Recebendo initialRow do servidor: " + initialRow);

                Solver solver = new Solver();
                long startTime = System.currentTimeMillis();

                System.out.println("Resolvendo o problema");

                ArrayList<int[][]> solutions = (ArrayList<int[][]>) solver.solve(N, initialRow);

                System.out.println("Problema resolvido");

                long endTime = System.currentTimeMillis();

                Soluction soluction = new Soluction();

                soluction.soluctions = solutions;
                soluction.startTime = startTime;
                soluction.endTime = endTime;

                System.out.println("Enviando solução para o servidor");

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeObject(soluction);

                out.flush();

                System.out.println("Solução enviada para o servidor");

                out.close();

                break;
            }

        } catch (IOException ex) {
            System.out.println("Client exception: " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        } finally {
            System.out.println("Client finished");
        }
    }
}
