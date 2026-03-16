import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Collections;

class Solution {
    public List<Integer> getBiggestThree(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // Using a TreeSet to store distinct sums in ascending order.
        // It automatically handles distinct values and keeps them sorted.
        TreeSet<Integer> rhombusSums = new TreeSet<>();

        // Iterate over all possible cells (r, c) as the top-most corner of a rhombus.
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                // Case 1: Rhombus of size s=0 (a single cell)
                // The sum is just the value of the cell itself.
                rhombusSums.add(grid[r][c]);
                // To efficiently keep only the top 3 largest distinct sums,
                // if the set size exceeds 3, remove the smallest element.
                if (rhombusSums.size() > 3) {
                    rhombusSums.pollFirst(); // Removes and returns the smallest element
                }

                // Case 2: Rhombus with side length s > 0
                // 's' represents the "radius" or "half-diagonal length" from the implicit center to a corner.
                // For a top-most corner (r, c) and side length 's', the four corners are:
                // Top: (r, c)
                // Right: (r + s, c + s)
                // Bottom: (r + 2s, c)
                // Left: (r + s, c - s)
                //
                // Constraints for 's' to ensure all corners are within grid boundaries:
                // 1. Bottom corner (r + 2s, c) must be within bounds: r + 2s < m  =>  s <= (m - 1 - r) / 2
                // 2. Left corner (r + s, c - s) must be within bounds: c - s >= 0  =>  s <= c
                // 3. Right corner (r + s, c + s) must be within bounds: c + s < n   =>  s <= n - 1 - c
                int maxS = Math.min((m - 1 - r) / 2, Math.min(c, n - 1 - c));

                for (int s = 1; s <= maxS; s++) {
                    long currentSum = 0; // Use long for sum to prevent potential intermediate overflow, although final sum fits int.

                    // Calculate the sum of the border elements.
                    // The loops are carefully designed to add each unique cell on the border exactly once.

                    // 1. Top-right segment: from (r, c) to (r + s, c + s)
                    // Includes both (r, c) and (r + s, c + s).
                    for (int k = 0; k <= s; k++) {
                        currentSum += grid[r + k][c + k];
                    }

                    // 2. Right-bottom segment: from (r + s, c + s) to (r + 2s, c)
                    // Starts from k=1 to exclude (r + s, c + s), which was added in the previous segment.
                    for (int k = 1; k <= s; k++) {
                        currentSum += grid[r + s + k][c + s - k];
                    }

                    // 3. Bottom-left segment: from (r + 2s, c) to (r + s, c - s)
                    // Starts from k=1 to exclude (r + 2s, c), which was added in the previous segment.
                    for (int k = 1; k <= s; k++) {
                        currentSum += grid[r + 2s - k][c - k];
                    }

                    // 4. Left-top segment: from (r + s, c - s) to (r, c)
                    // Starts from k=1 to exclude (r + s, c - s).
                    // Ends at s-1 to exclude (r, c), which was added in the first segment.
                    for (int k = 1; k <= s - 1; k++) {
                        currentSum += grid[r + s - k][c - s + k];
                    }
                    
                    rhombusSums.add((int) currentSum); // Add the calculated sum
                    if (rhombusSums.size() > 3) {
                        rhombusSums.pollFirst(); // Remove the smallest element if size exceeds 3
                    }
                }
            }
        }

        // Convert the TreeSet (which holds the biggest 1, 2, or 3 distinct sums in ascending order)
        // to a List in descending order as required by the problem.
        List<Integer> result = new ArrayList<>(rhombusSums.descendingSet());
        return result;
    }
}

/*
Time Complexity:
- The outermost loops iterate over each cell (r, c) of the grid: O(m * n).
- The loop for 's' (rhombus side length) goes from 1 up to approximately min(m, n) / 2. So, O(min(m, n)).
- Inside the 's' loop, there are four loops to sum the border elements. Each of these loops runs O(s) times, which is at most O(min(m, n)).
- Adding elements to and removing elements from the TreeSet takes O(log K) time, where K is the maximum size of the set (at most 3 in this case). So, TreeSet operations are effectively O(1).

Combining these, the total time complexity is O(m * n * min(m, n) * min(m, n)) = O(m * n * (min(m, n))^2).
Given m, n <= 50, the worst-case number of operations is approximately 50 * 50 * (50/2)^2 = 2500 * 25^2 = 2500 * 625 = 1,562,500. This is well within typical time limits for competitive programming (usually ~10^8 operations per second).

Space Complexity:
- The TreeSet 'rhombusSums' stores at most 3 distinct integer values. This is constant space, O(1).
- The 'result' ArrayList also stores at most 3 integer values. This is also O(1).

Total space complexity is O(1).
*/