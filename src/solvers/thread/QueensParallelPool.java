package solvers.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class QueensParallelPool {
    private int N;
    private ExecutorService executor;

    public QueensParallelPool(int N) {
        this.N = N;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    // Verifica se uma posição é segura para colocar uma rainha
    private boolean isSafe(int board[][], int row, int col) {
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
    private boolean solveNQueensUtil(int board[][], int col) {
        if (col >= N)
            return true;

        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {
                board[i][col] = 1;

                if (solveNQueensUtil(board, col + 1))
                    return true;

                board[i][col] = 0;
            }
        }
        return false;
    }

    // Imprime a solução encontrada de forma sincronizada
    private synchronized void printSolution(int board[][]) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Submete uma tarefa para resolver o problema de N Rainhas a partir de uma configuração inicial
    private void submitTask(int initialRow) {
        executor.submit(() -> {
            int[][] board = new int[N][N];
            board[initialRow][0] = 1;

            if (solveNQueensUtil(board, 1)) {
                printSolution(board);
            }
        });
    }

    // Inicia a resolução do problema utilizando um pool de threads
    public void solve() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < N; i++) {
            submitTask(i);
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);  // Espera até que todas as tarefas terminem
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");
    }

    public static void main(String[] args) {
        int N = 8;  // Exemplo com 8 rainhas
        QueensParallelPool solver = new QueensParallelPool(N);
        solver.solve();
    }
}
