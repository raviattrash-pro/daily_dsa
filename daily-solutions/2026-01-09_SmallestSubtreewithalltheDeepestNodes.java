class Solution {
    // Definition for a binary tree node (provided by LeetCode environment):
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // Helper class to store the result of the DFS:
    // 1. The 'node': This is the root of the smallest subtree containing all deepest nodes
    //    within the current subtree being processed.
    // 2. The 'depth': This is the maximum depth (or height) of any node in the current subtree
    //    relative to the current subtree's root. For a leaf, this would be 1. For a null node, 0.
    private class Result {
        TreeNode node;
        int depth;

        Result(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        // The final answer is the 'node' component of the Result returned by the DFS starting from the root.
        return dfs(root).node;
    }

    // This DFS function recursively finds the deepest nodes and their lowest common ancestor.
    private Result dfs(TreeNode node) {
        // Base case: If the node is null, it has no subtree, no deepest nodes, and its depth is considered 0.
        if (node == null) {
            return new Result(null, 0);
        }

        // Recursively call dfs for the left and right children to get their results.
        Result leftResult = dfs(node.left);
        Result rightResult = dfs(node.right);

        // Compare the depths returned by the left and right subtrees.
        // These depths represent the max height of the subtree rooted at node.left/node.right.
        if (leftResult.depth == rightResult.depth) {
            // If both subtrees have the same maximum depth, it means that the deepest
            // nodes are equally deep in both left and right branches (relative to the current node).
            // In this scenario, the current 'node' itself is the LCA (root of the smallest subtree)
            // for all deepest nodes found within its combined left and right subtrees.
            // The depth of this subtree (rooted at 'node') is 1 (for 'node' itself) + the depth of its children's subtrees.
            return new Result(node, 1 + leftResult.depth);
        } else if (leftResult.depth > rightResult.depth) {
            // If the left subtree is deeper, it means all the deepest nodes within the current
            // subtree (rooted at 'node') must reside entirely within the left subtree.
            // Therefore, we propagate the 'node' and updated 'depth' from the left result.
            // The depth is 1 (for 'node' itself) + the depth of the left subtree.
            return new Result(leftResult.node, 1 + leftResult.depth);
        } else { // rightResult.depth > leftResult.depth
            // If the right subtree is deeper, similar to the left case, all deepest nodes
            // must reside entirely within the right subtree.
            // We propagate the 'node' and updated 'depth' from the right result.
            // The depth is 1 (for 'node' itself) + the depth of the right subtree.
            return new Result(rightResult.node, 1 + rightResult.depth);
        }
    }
}
// Time Complexity: O(N), where N is the number of nodes in the binary tree.
// Each node is visited exactly once during the DFS traversal.
// Space Complexity: O(H), where H is the height of the binary tree.
// This space is used by the recursion stack. In the worst case (a skewed tree), H can be N,
// leading to O(N) space. In the best case (a balanced tree), H is log N, leading to O(log N) space.