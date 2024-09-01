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

    // metodo define um pool de threads para resolução do problema
    public void solve() {
        long startTime = System.currentTimeMillis(); // marca o inicio da execução

        for (int i = 0; i < N; i++) {
            submitTask(i);
        }
        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.HOURS);  // espera até que todas as tarefas terminem
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");
    }

    // inicia uma tarefa para resolver o problema de N Rainhas a partir de uma configuração inicial
    private void submitTask(int initialRow) {
        executor.submit(() -> {
            int[][] board = new int[N][N];
            board[initialRow][0] = 1;

            if (solveQueens(board, 1)) {
                printSolution(board);
            }
        });
    }

    // metodo para resolver o problema de N Rainhas usando backtracking
    private boolean solveQueens(int board[][], int col) {
        if (col >= N) // verifica se todas as rainhas foram colocadas
            return true;

        // tentativa de colocar a rainha em cada linha da coluna atual
        for (int i = 0; i < N; i++) {
            if (isQueenSafe(board, i, col)) {
                board[i][col] = 1;

                if (solveQueens(board, col + 1)) // tentativa de resolver o subproblema para a próxima coluna
                    return true;

                board[i][col] = 0; // se a tentativa falhar, remove a rainha (backtracking)
            }
        }
        return false;
    }

    // verifica se a casa do board é segura para colocar uma rainha,
    // recebe o board e as cordenadas da casa como parametros
    private boolean isQueenSafe(int board[][], int row, int col) {
        // verifica a se a linha é segura
        for (int i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;

        // verifica a se a diagonal superior é segura
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // verifica a se a diagonal inferior é segura
        for (int i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    // imprime a solução encontrada de forma sincronizada
    private synchronized void printSolution(int board[][]) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int N = 29;  // numero de rainhas (n=29: 8286 ms)
        QueensParallelPool solver = new QueensParallelPool(N);
        solver.solve();
    }
}
