import java.util.Stack;

class Solution {

    public int maximalRectangle(char[][] matrix) {
        // Time Complexity: O(rows * cols)
        // Space Complexity: O(cols)

        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxArea = 0;

        int[] heights = new int[cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    heights[j]++;
                } else {
                    heights[j] = 0;
                }
            }
            
            maxArea = Math.max(maxArea, largestRectangleArea(heights));
        }

        return maxArea;
    }

    private int largestRectangleArea(int[] h) {
        // Time Complexity: O(n), where n is the length of the heights array.
        // Space Complexity: O(n), where n is the length of the heights array.

        int n = h.length;
        if (n == 0) {
            return 0;
        }

        int maxArea = 0;
        Stack<Integer> stack = new Stack<>(); 

        for (int i = 0; i <= n; i++) {
            int currH = (i == n) ? 0 : h[i];

            while (!stack.isEmpty() && currH < h[stack.peek()]) {
                int height = h[stack.pop());
                
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }
}