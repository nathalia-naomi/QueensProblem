package solvers.sequencial;

public class QueensProblem {
    private int N;
    private int[][] solution;
    private boolean solutionFound;

    public QueensProblem(int N) {
        this.N = N;
        this.solution = new int[N][N];
        this.solutionFound = false;
    }

    public long solve() {
        int[][] board = new int[N][N];
        long startTime = System.currentTimeMillis(); // Inicia a contagem do tempo

        solveQueens(board, 0);
        printSolution();
        long endTime = System.currentTimeMillis(); // Termina a contagem do tempo

        long executionTime = endTime - startTime; // Calcula o tempo de execução

        System.out.println("Tempo de execução: " + executionTime + " ms");
        return executionTime;
    }

    private boolean solveQueens(int[][] board, int row) {
        if (solutionFound) {
            return true; // Se a solução foi encontrada, não continue
        }

        if (row >= N) {
            copyBoard(board, solution);
            solutionFound = true;
            return true;
        }

        boolean foundSolution = false;
        for (int col = 0; col < N; col++) {
            if (isQueenSafe(board, row, col)) {
                board[row][col] = 1; // Place queen

                foundSolution = solveQueens(board, row + 1) || foundSolution;

                board[row][col] = 0; // Remove queen (backtrack)
            }
        }
        return foundSolution;
    }

    private boolean isQueenSafe(int[][] board, int row, int col) {
        // Check column
        for (int i = 0; i < row; i++)
            if (board[i][col] == 1)
                return false;

        // Check upper left diagonal
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // Check upper right diagonal
        for (int i = row, j = col; i >= 0 && j < N; i--, j++)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    private void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < N; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, N);
        }
    }

    public void printSolution() {
        if (solutionFound) {
            System.out.println("Solução encontrada:");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(solution[i][j] + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Nenhuma solução encontrada.");
        }
    }

    public static void main(String[] args) {
        int N = 8; // Exemplo com 8 rainhas

        QueensProblem queens = new QueensProblem(N);
        queens.solve();
    }
}
