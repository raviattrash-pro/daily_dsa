class Solution {
    public int countNegatives(int[][] grid) {
        // Time Complexity: O(m + n), where m is the number of rows and n is the number of columns.
        // The row pointer 'r' traverses each row at most once (m steps).
        // The column pointer 'col' traverses each column at most once in total across all rows (n steps).
        // In the worst case, the column pointer starts at n-1 and moves to 0 over several rows.
        // Space Complexity: O(1) as only a few extra variables are used.

        int m = grid.length;
        int n = grid[0].length;
        int count = 0;

        // Start from the top-right corner of the matrix.
        // 'col' represents the current column we are examining.
        // It starts at the last column (n-1) and only moves left.
        int col = n - 1;

        // Iterate through each row of the matrix.
        for (int r = 0; r < m; r++) {
            // In the current row 'r', we move the 'col' pointer to the left
            // as long as we are within bounds and the element is negative.
            // This finds the leftmost negative number in the current row (or goes out of bounds).
            while (col >= 0 && grid[r][col] < 0) {
                col--;
            }

            // After the while loop, 'col' points to the last non-negative element's column
            // or is -1 if all elements in the current row are negative.
            // All elements from (col + 1) to (n - 1) in the current row are negative.
            // The number of such negative elements is n - (col + 1).
            // Example: if col is 2, then indices 3, ..., n-1 are negative. Number = n - (2+1).
            // Example: if col is -1, then indices 0, ..., n-1 are negative. Number = n - (-1+1) = n.
            count += (n - (col + 1));
        }

        return count;
    }
}