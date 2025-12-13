class Solution {
    /**
     * Searches for a target value in an m x n integer matrix.
     * The matrix has the following properties:
     * - Integers in each row are sorted in ascending order from left to right.
     * - The first integer of each row is greater than the last integer of the previous row.
     *
     * Time Complexity: O(log(m*n)) - Since the matrix can be treated as a single sorted array of size m*n, 
     * we can use binary search on the entire structure.
     * Space Complexity: O(1) - Constant extra space is used.
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Treat the 2D matrix as a single sorted 1D array of size rows * cols.
        int left = 0;
        int right = rows * cols - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Convert the 1D index (mid) back to 2D coordinates (row, col)
            int row = mid / cols;
            int col = mid % cols;

            int midValue = matrix[row][col];

            if (midValue == target) {
                return true;
            } else if (midValue < target) {
                // Target is in the right half (larger values)
                left = mid + 1;
            } else {
                // Target is in the left half (smaller values)
                right = mid - 1;
            }
        }

        return false;
    }
}