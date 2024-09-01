package solvers.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueensParallel {
    private int N;
    private CountDownLatch latch; // ferramenta para calcular o tempo de execução
    private int[][] solution;
    private AtomicBoolean solutionFound;

    public QueensParallel(int N) {
        this.N = N;
        this.latch = new CountDownLatch(N); // inicializa o latch com tamanho N
        this.solution = new int[N][N];
        this.solutionFound = new AtomicBoolean(false);
    }

    // metodo define as threads para resolução do problema
    public void solve() {
        long startTime = System.currentTimeMillis(); // marca o inicio da execução

        // para cada linha será iniciada uma thread
        for (int i = 0; i < N; i++) {
            final int row = i;
            new Thread(() -> {
                if (solutionFound.get()) {
                    latch.countDown();
                    return;
                }

                int[][] board = new int[N][N];
                board[row][0] = 1;

                if (solveQueens(board, 1)) {
                    if (solutionFound.compareAndSet(false, true)) {
                        copyBoard(board, solution);
                        printSolution(solution);
                    }
                }
                latch.countDown(); // decrementa o latch ao terminar a thread
            }).start();
        }

        try {
            latch.await(1, TimeUnit.MINUTES); // espera até que todas as threads terminem
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tempo total de execução: " + (endTime - startTime) + " ms");
    }

    // metodo para resolver o problema de N Rainhas usando backtracking
    private boolean solveQueens(int[][] board, int col) {
        if (solutionFound.get()) {
            return true; // Se a solução foi encontrada, não continue
        }

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

    // copia o tabuleiro de origem para o tabuleiro de destino
    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < N; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, N);
        }
    }

    // imprime a solução encontrada de forma sincronizada
    private void printSolution(int[][] board) {
        synchronized (this) {
            System.out.println("Solução encontrada:");
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
        int N = 8;
        QueensParallel solver = new QueensParallel(N);
        solver.solve();
    }
}
