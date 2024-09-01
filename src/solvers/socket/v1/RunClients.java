package solvers.socket.v1;

public class RunClients {
    private int numberOfClients;


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
}
