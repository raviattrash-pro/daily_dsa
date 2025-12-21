class Solution {
    public int minDeletionSize(String[] strs) {
        // n is the number of strings
        int n = strs.length;
        // If there is only one string or no strings, it is already considered sorted.
        if (n <= 1) {
            return 0;
        }

        // m is the length of each string
        int m = strs[0].length();

        // isSorted[i] indicates whether the pair (strs[i], strs[i+1])
        // is already confirmed to be in lexicographical order (strs[i] < strs[i+1])
        // based on the columns kept so far.
        // If isSorted[i] is false, it means strs[i] and strs[i+1] are still
        // considered equal by the kept columns, or their order hasn't been determined.
        boolean[] isSorted = new boolean[n - 1];
        
        // Counter for the number of columns deleted.
        int deletedCount = 0;
        
        // Counter for the number of adjacent string pairs (strs[i], strs[i+1])
        // that are not yet strictly sorted.
        // Initially, all n-1 pairs are considered unsorted/tied.
        int unsortedPairs = n - 1;

        // Iterate through each column from left to right (index j)
        for (int j = 0; j < m; j++) {
            boolean currentColumnMustBeDeleted = false;
            
            // First, check if keeping the current column 'j' would violate
            // the lexicographical order for any currently unsorted/tied pair.
            for (int i = 0; i < n - 1; i++) {
                // We only need to check pairs that are not yet confirmed to be sorted.
                // If isSorted[i] is true, strs[i] is already strictly less than strs[i+1],
                // so this column cannot make them unsorted.
                if (!isSorted[i]) {
                    // Compare characters at column 'j' for adjacent strings.
                    if (strs[i].charAt(j) > strs[i+1].charAt(j)) {
                        // Violation found: strs[i] > strs[i+1] at this column.
                        // This column MUST be deleted to maintain order.
                        currentColumnMustBeDeleted = true;
                        deletedCount++;
                        // Once a violation is found for this column, we stop checking
                        // other pairs for this column and move to the next column.
                        break; 
                    }
                }
            }

            // If the current column does not cause any violations, we decide to keep it.
            // Then, we update the `isSorted` status for pairs that become strictly sorted
            // due to the characters in this column.
            if (!currentColumnMustBeDeleted) {
                for (int i = 0; i < n - 1; i++) {
                    // Only update if the pair was previously unsorted/tied.
                    if (!isSorted[i]) {
                        // If strs[i].charAt(j) is strictly less than strs[i+1].charAt(j),
                        // this pair is now resolved to be in lexicographical order.
                        if (strs[i].charAt(j) < strs[i+1].charAt(j)) {
                            isSorted[i] = true; // Mark this pair as sorted.
                            unsortedPairs--;    // Decrement the count of unsorted pairs.
                        }
                    }
                }
            }
            
            // Optimization: If all pairs are now strictly sorted (unsortedPairs becomes 0),
            // then there is no need to process any further columns.
            // The condition is met, and any remaining columns would only be kept (not deleted)
            // and wouldn't change the sorted status, thus not affecting `deletedCount`.
            if (unsortedPairs == 0) {
                break; 
            }
        }

        return deletedCount;
    }
}
// Time Complexity: O(M * N)
// Where M is the length of each string and N is the number of strings.
// In the worst case, we iterate through all M columns. For each column,
// we iterate through N-1 pairs of strings twice (once to check for deletions,
// once to update sorted status). This results in O(M * N) operations.
// The `unsortedPairs` optimization can reduce operations in best/average cases
// by breaking early, but the worst-case complexity remains O(M * N).

// Space Complexity: O(N)
// We use a boolean array `isSorted` of size N-1 to keep track of the sorted status
// of adjacent string pairs. This requires O(N) auxiliary space.