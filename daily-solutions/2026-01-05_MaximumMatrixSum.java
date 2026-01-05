import java.util.PriorityQueue;

class Solution {
    // Time Complexity: O(N^2)
    // Space Complexity: O(1)
    public long maxMatrixSum(int[][] matrix) {
        long totalSum = 0;
        int negativeCount = 0;
        int minAbs = Integer.MAX_VALUE;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] < 0) {
                    negativeCount++;
                }
                totalSum += Math.abs(matrix[i][j]);
                minAbs = Math.min(minAbs, Math.abs(matrix[i][j]));
            }
        }

        if (negativeCount % 2 == 0) {
            return totalSum;
        } else {
            return totalSum - 2L * minAbs;
        }
    }
}