Here's the Java code for the `Solution` class:


import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;

class Solution {

    /**
     * Helper method to add an element to the current window's frequency map and distinct set.
     *
     * @param val The integer value to add.
     * @param valueCounts A HashMap to store the frequency of each value in the current window.
     * @param distinctValues A TreeSet to store distinct values in sorted order.
     */
    private void add(int val, HashMap<Integer, Integer> valueCounts, TreeSet<Integer> distinctValues) {
        valueCounts.put(val, valueCounts.getOrDefault(val, 0) + 1);
        distinctValues.add(val); // TreeSet automatically handles duplicates, only adds if not present
    }

    /**
     * Helper method to remove an element from the current window's frequency map and distinct set.
     *
     * @param val The integer value to remove.
     * @param valueCounts A HashMap to store the frequency of each value in the current window.
     * @param distinctValues A TreeSet to store distinct values in sorted order.
     */
    private void remove(int val, HashMap<Integer, Integer> valueCounts, TreeSet<Integer> distinctValues) {
        valueCounts.put(val, valueCounts.get(val) - 1);
        if (valueCounts.get(val) == 0) {
            distinctValues.remove(val); // Remove from TreeSet only if its count drops to 0
        }
    }

    /**
     * Helper method to compute the minimum absolute difference among distinct values in the TreeSet.
     *
     * @param distinctValues A TreeSet containing distinct values in sorted order.
     * @return The minimum absolute difference, or 0 if there are 0 or 1 distinct values.
     */
    private int getMinDiff(TreeSet<Integer> distinctValues) {
        if (distinctValues.size() <= 1) {
            return 0; // If there's 0 or 1 distinct element, the difference is 0.
        }

        int minDiff = Integer.MAX_VALUE;
        Iterator<Integer> it = distinctValues.iterator();
        int prev = it.next(); // Get the smallest element

        // Iterate through sorted distinct values to find the minimum difference between adjacent elements
        while (it.hasNext()) {
            int current = it.next();
            minDiff = Math.min(minDiff, current - prev);
            prev = current;
        }
        return minDiff;
    }

    /**
     * Computes the minimum absolute difference between any two distinct values
     * for every contiguous k x k submatrix of the given grid.
     *
     * @param grid The input m x n integer matrix.
     * @param k The size of the square submatrix (k x k).
     * @return A 2D array 'ans' of size (m - k + 1) x (n - k + 1),
     *         where ans[i][j] is the minimum absolute difference in the submatrix
     *         whose top-left corner is (i, j) in grid.
     *
     * Time Complexity: O(M * N * (K * log(V) + V)), where:
     *   M is the number of rows in the grid.
     *   N is the number of columns in the grid.
     *   K is the size of the submatrix.
     *   V is the maximum number of distinct elements in any K x K submatrix (V <= K*K).
     *   In the worst case, this simplifies to O(M * N * K*K).
     *   Given M, N, K <= 30, this is approximately 30*30 * (30*log(900) + 900) which is ~10^6 operations, well within limits.
     *
     * Space Complexity: O(M * N + K*K), where:
     *   O(M * N) for the result array 'ans'.
     *   O(K*K) for the HashMap 'valueCounts' and TreeSet 'distinctValues' to store elements
     *   of a K x K window.
     */
    public int[][] minimumAbsoluteDifference(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;

        int ansRows = m - k + 1;
        int ansCols = n - k + 1;
        int[][] ans = new int[ansRows][ansCols];

        // Iterate through each possible starting row 'i' for the k x k submatrix in the output 'ans' array
        for (int i = 0; i < ansRows; i++) {
            // Initialize/reset the frequency map and distinct set for the current k x k window.
            // This setup corresponds to the submatrix with top-left corner (i, 0).
            HashMap<Integer, Integer> valueCounts = new HashMap<>();
            TreeSet<Integer> distinctValues = new TreeSet<>();

            // Populate the initial k x k window for the current row of results (i.e., at (i, 0)).
            // This submatrix covers rows from 'i' to 'i + k - 1' and columns from '0' to 'k - 1'.
            for (int r = i; r < i + k; r++) {
                for (int c = 0; c < k; c++) {
                    add(grid[r][c], valueCounts, distinctValues);
                }
            }
            ans[i][0] = getMinDiff(distinctValues);

            // Slide the k x k window horizontally for the remaining columns of the current 'ans' row.
            // This loop calculates ans[i][j] for j from 1 to n - k.
            for (int j = 1; j < ansCols; j++) {
                // Remove the leftmost column (j-1) of the previous window.
                // The elements to remove are grid[r][j-1] for rows from 'i' to 'i + k - 1'.
                for (int r = i; r < i + k; r++) {
                    remove(grid[r][j-1], valueCounts, distinctValues);
                }

                // Add the rightmost column (j+k-1) for the current window.
                // The elements to add are grid[r][j+k-1] for rows from 'i' to 'i + k - 1'.
                for (int r = i; r < i + k; r++) {
                    add(grid[r][j+k-1], valueCounts, distinctValues);
                }
                ans[i][j] = getMinDiff(distinctValues);
            }
        }

        return ans;
    }
}