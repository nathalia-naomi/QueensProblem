package solvers.socket.v3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import solvers.socket.v3.utils.Soluction;

public class ServerHandler extends Thread {

    private final Socket socket;
    private final int row;
    private final int n;
    private final int clientId;
    public static ArrayList<Soluction> soluctions = new ArrayList<>();

    public ServerHandler(Socket socket, int clientId, int n, int row) {
        this.socket = socket;
        this.row = row;
        this.n = n;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            OutputStream output = this.socket.getOutputStream();

            try {
                while (true) {
                    ObjectOutputStream out = new ObjectOutputStream(output);

                    out.writeObject(this.n);
                    out.flush();

                    System.out.println("Enviando N para o cliente " + n);

                    out.writeObject(this.row);
                    out.flush();

                    System.out.println("Enviando initialRow para o cliente " + this.row);

                    ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream());

                    System.out.println("Recebendo soluções do cliente");

                    Soluction soluction = (Soluction) in.readObject();

                    soluction.clientId = this.clientId;

                    ServerHandler.soluctions.add(soluction);

                    System.out.println("Soluções recebidas do cliente");

                    System.out.println("Tempo de execução: " + (soluction.endTime - soluction.startTime) + "ms");
                }
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found: " + ex.getMessage());
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            System.out.println("Server exception EXIO: " + ex.getMessage());
        } finally {
            try {
                this.socket.close();
            } catch (IOException ex) {
                System.out.println("Server exception IO: " + ex.getMessage());
            }
        }
    }
}
