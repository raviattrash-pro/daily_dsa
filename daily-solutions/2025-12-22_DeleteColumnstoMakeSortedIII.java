class Solution {
    // Time Complexity: O(m^2 * n)
    //   m is the length of each string (number of columns)
    //   n is the number of strings (number of rows)
    //   The outer loop runs m times (for j).
    //   The inner loop runs up to m times (for i).
    //   The innermost loop runs n times (for k).
    //   Character comparison is O(1).
    // Space Complexity: O(m)
    //   An array `dp` of size `m` is used.
    public int minDeletionSize(String[] strs) {
        if (strs == null || strs.length == 0) {
            return 0;
        }

        int n = strs.length; // Number of rows
        int m = strs[0].length(); // Number of columns

        // As per constraints, 1 <= strs[i].length <= 100, so m will always be at least 1.
        // Thus, strs[0].length() == 0 case is not strictly necessary but harmless.
        // if (m == 0) {
        //     return 0;
        // }

        // dp[j] stores the maximum number of columns we can keep, ending with column j,
        // such that all rows are lexicographically sorted among the kept columns.
        int[] dp = new int[m];

        // Initialize dp array. Each column can at least be kept by itself, forming a sequence of length 1.
        Arrays.fill(dp, 1);

        // Iterate through each column j (current column) to calculate dp[j]
        for (int j = 0; j < m; j++) {
            // Iterate through each previous column i (potential preceding column for column j)
            for (int i = 0; i < j; i++) {
                boolean canFollow = true;
                // Check if column j can follow column i for ALL strings
                // i.e., for every string k, strs[k][i] <= strs[k][j] must hold.
                for (int k = 0; k < n; k++) {
                    if (strs[k].charAt(i) > strs[k].charAt(j)) {
                        canFollow = false;
                        break; // This row breaks the sorted property, so column i cannot precede column j.
                    }
                }

                // If column j can follow column i, update dp[j]
                // dp[j] becomes the maximum of its current value or (dp[i] + 1)
                // (where dp[i] is the length of the sequence ending at i)
                if (canFollow) {
                    dp[j] = Math.max(dp[j], dp[i] + 1);
                }
            }
        }

        // The maximum value in the dp array represents the maximum number of columns we can keep
        // while satisfying the lexicographical order constraint for all rows.
        int maxKeptColumns = 0;
        for (int count : dp) {
            maxKeptColumns = Math.max(maxKeptColumns, count);
        }

        // The minimum number of deletions is the total number of columns minus the maximum number of columns kept.
        return m - maxKeptColumns;
    }
}