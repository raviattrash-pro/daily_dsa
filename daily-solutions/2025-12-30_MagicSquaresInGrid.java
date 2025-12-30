class Solution {
    // Time Complexity: O(R * C) where R is the number of rows and C is the number of columns in the grid.
    // The outer loops iterate (R-2) * (C-2) times.
    // The `isMagicSquare` helper function performs a fixed number of operations (checking 9 elements for range/distinctness and 8 sums of 3 elements each),
    // which is considered O(1) because the size of the 3x3 subgrid is constant.
    // Thus, the total time complexity is proportional to the number of possible 3x3 subgrids, which is O(R*C).
    // Given R, C <= 10, this is very efficient.
    // Space Complexity: O(1) for the `seen` boolean array of fixed size 10 used within the `isMagicSquare` helper function.
    public int numMagicSquaresInside(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // A 3x3 magic square cannot be formed if the grid is smaller than 3x3
        if (rows < 3 || cols < 3) {
            return 0;
        }

        int magicSquareCount = 0;

        // Iterate through all possible top-left corners of 3x3 subgrids
        // A 3x3 subgrid starting at (r, c) means it uses rows r, r+1, r+2 and columns c, c+1, c+2.
        // So, r can go up to rows - 3 and c can go up to cols - 3.
        for (int r = 0; r <= rows - 3; r++) {
            for (int c = 0; c <= cols - 3; c++) {
                if (isMagicSquare(r, c, grid)) {
                    magicSquareCount++;
                }
            }
        }

        return magicSquareCount;
    }

    /**
     * Helper function to check if a 3x3 subgrid starting at (r, c) is a magic square.
     * A 3x3 magic square must contain distinct numbers from 1 to 9, and all row,
     * column, and main diagonal sums must be equal (which is 15 for numbers 1-9).
     *
     * @param r The starting row index of the 3x3 subgrid.
     * @param c The starting column index of the 3x3 subgrid.
     * @param grid The original grid.
     * @return true if the subgrid is a magic square, false otherwise.
     */
    private boolean isMagicSquare(int r, int c, int[][] grid) {
        // Optimization: The center element of any 3x3 magic square with numbers 1-9 must be 5.
        // This is a known property: (sum of all numbers) / 9 = 45 / 9 = 5.
        // The center element is involved in 4 sums (middle row, middle col, both diagonals).
        // Since all sums are 15, and other elements are symmetrical, the center must be 5.
        if (grid[r + 1][c + 1] != 5) {
            return false;
        }

        // Use a boolean array to check for distinct numbers from 1 to 9.
        // Index 0 is unused, indices 1-9 correspond to the numbers.
        boolean[] seen = new boolean[10]; 
        
        // Check if all numbers in the 3x3 subgrid are distinct and within the range [1, 9]
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int val = grid[r + i][c + j];
                // If value is out of range [1, 9] or has already been seen, it's not a magic square
                if (val < 1 || val > 9 || seen[val]) {
                    return false;
                }
                seen[val] = true; // Mark number as seen
            }
        }

        // For a 3x3 magic square with numbers 1-9, the sum of each row, column, and diagonal must be 15.
        // Check row sums
        if (grid[r][c] + grid[r][c + 1] + grid[r][c + 2] != 15) return false;
        if (grid[r + 1][c] + grid[r + 1][c + 1] + grid[r + 1][c + 2] != 15) return false;
        if (grid[r + 2][c] + grid[r + 2][c + 1] + grid[r + 2][c + 2] != 15) return false;

        // Check column sums
        if (grid[r][c] + grid[r + 1][c] + grid[r + 2][c] != 15) return false;
        if (grid[r][c + 1] + grid[r + 1][c + 1] + grid[r + 2][c + 1] != 15) return false;
        if (grid[r][c + 2] + grid[r + 1][c + 2] + grid[r + 2][c + 2] != 15) return false;

        // Check main diagonal sum
        if (grid[r][c] + grid[r + 1][c + 1] + grid[r + 2][c + 2] != 15) return false; 
        // Check anti-diagonal sum
        if (grid[r][c + 2] + grid[r + 1][c + 1] + grid[r + 2][c] != 15) return false; 

        // If all checks pass, it's a valid magic square
        return true;
    }
}