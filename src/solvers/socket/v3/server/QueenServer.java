package solvers.socket.v3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import solvers.socket.v3.utils.Soluction;

public class QueenServer {

    public static void main(String[] args) {
        int port = 3011;
        int n = 8;
        int row = 0;
        int clientId = 1;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Connection created");

            while (true) {
                if (ServerHandler.soluctions.size() == 4) {
                    break;
                }

                Socket socket = serverSocket.accept();

                System.out.println("Client connected to server with IP: " + socket.getInetAddress().getHostAddress() + " and row: " + row);

                if (row >= n) {
                    System.out.println("Número excessivo de clientes");

                    socket.close();

                    break;
                }

                new ServerHandler(socket, clientId, n, row).start();

                row++;
                clientId++;
            }

            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }

        ArrayList<Soluction> soluctions = ServerHandler.soluctions;

        System.out.println("Soluções encontradas: " + soluctions.size());

        for (Soluction soluction : soluctions) {
            System.out.println("Cliente " + soluction.clientId);
        }

        Soluction bestSoluction = soluctions.get(0);

        long time = bestSoluction.endTime - bestSoluction.startTime;

        for (Soluction soluction : soluctions) {
            if (soluction.endTime - soluction.startTime < time) {
                bestSoluction = soluction;
                time = soluction.endTime - soluction.startTime;
            }        
        }

        System.out.println("Melhor tempo é a do cliente " + bestSoluction.clientId + " encontrou em " + (bestSoluction.endTime - bestSoluction.startTime) + "ms");

    }
}
