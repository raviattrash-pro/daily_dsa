import java.util.LinkedList;
import java.util.Queue;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int maxLevelSum(TreeNode root) {
        // Constraints state that the number of nodes is in the range [1, 10^4],
        // so root will never be null.

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // Initialize maxSum with the smallest possible integer value.
        // Node.val is up to 10^5, number of nodes up to 10^4.
        // A level sum can be up to 10^4 * 10^5 = 10^9, which fits in an int.
        int maxSum = Integer.MIN_VALUE;
        int maxLevel = 1; // The level corresponding to maxSum
        int currentLevel = 1; // Current level being processed

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // Number of nodes at the current level
            int currentLevelSum = 0;    // Sum of node values at the current level

            // Process all nodes at the current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevelSum += node.val;

                // Add children to the queue for the next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            // After processing all nodes for the current level,
            // check if its sum is greater than the maximum sum found so far.
            if (currentLevelSum > maxSum) {
                maxSum = currentLevelSum;
                maxLevel = currentLevel;
            }
            
            // Move to the next level
            currentLevel++;
        }

        return maxLevel;
    }
}

/*
Time Complexity:
Each node in the binary tree is visited and processed exactly once. For each node, we perform constant time operations such as dequeuing, summing its value, and enqueuing its children.
If N is the total number of nodes in the tree, the time complexity for this Breadth-First Search (BFS) traversal is O(N).

Space Complexity:
The space complexity is determined by the maximum number of nodes stored in the queue at any point in time. In the worst-case scenario, for a complete binary tree, the queue might hold all nodes at the widest level. The maximum width of a binary tree can be approximately N/2 nodes (at the last level).
Therefore, the space complexity is O(W), where W is the maximum width of the tree, which can be up to O(N) in the worst case (e.g., a complete binary tree).
*/