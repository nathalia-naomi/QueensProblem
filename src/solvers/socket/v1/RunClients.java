package solvers.socket.v1;

public class RunClients {
    private int N;
    private String serverAddress;
    private int port;
    private int numberOfClients;

    public RunClients(int N, String serverAddress, int port, int numberOfClients) {
        this.N = N;
        this.serverAddress = serverAddress;
        this.port = port;
        this.numberOfClients = numberOfClients;
    }

    public void executeClients() {
        Thread[] clientThreads = new Thread[numberOfClients];

        for (int i = 0; i < numberOfClients; i++) {
            clientThreads[i] = new Thread(() -> {
                QueensClient client = new QueensClient(N, serverAddress, port);
                client.startClient();
            });
            clientThreads[i].start();
        }

        // Aguarda todas as threads de clientes terminarem
        for (int i = 0; i < numberOfClients; i++) {
            try {
                clientThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos os clientes concluíram a execução.");
    }
    public static void main(String[] args) {
        int N = 8;  // Número de rainhas
        String serverAddress = "localhost";
        int port = 12345;
        int numberOfClients = 4;  // Número de clientes que serão executados em paralelo

        RunClients executor = new RunClients(N, serverAddress, port, numberOfClients);
        executor.executeClients();
    }
}
