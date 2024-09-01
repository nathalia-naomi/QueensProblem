package solvers.thread;

import java.util.concurrent.CountDownLatch;

public class QueensParallel {
    private int N;
    private CountDownLatch latch;


    public QueensParallel(int N) {
        this.N = N;
        this.latch = new CountDownLatch(N); // Inicializa o latch com N
    }

    // Verifica se uma posição é segura para colocar uma rainha
    private boolean isQueenSafe(int board[][], int row, int col) {
        for (int i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        for (int i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    // Método recursivo de backtracking para resolver o problema de N Rainhas
    private boolean solveQueens(int board[][], int col) {
        if (col >= N)
            return true;

        for (int i = 0; i < N; i++) {
            if (isQueenSafe(board, i, col)) {
                board[i][col] = 1;

                if (solveQueens(board, col + 1))
                    return true;

                board[i][col] = 0;
            }
        }
        return false;
    }

    public void solve() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < N; i++) {
            final int row = i;
            new Thread(() -> {
                int[][] board = new int[N][N];
                board[row][0] = 1;

                if (solveQueens(board, 1)) {
                    printSolution(board);
                }

                latch.countDown(); // Decrementa o latch ao terminar a thread
            }).start();
        }

        try {
            latch.await(); // Espera até que todas as threads terminem
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");
    }

    // Imprime a solução encontrada de forma sincronizada
    private void printSolution(int board[][]) {
        synchronized (this) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(board[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int N = 8;  // Exemplo com 8 rainhas
        QueensParallel solver = new QueensParallel(N);
        solver.solve();
    }
}
