Time Complexity: O(N1 * N2)
We iterate through each element of nums1 (N1) and for each, iterate through each element of nums2 (N2).
Each cell calculation takes constant time.

Space Complexity: O(N2)
We use two arrays, prevDp and currDp, each of size N2 + 1.
N1 is nums1.length, N2 is nums2.length.

class Solution {
    public int maxDotProduct(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;

        // prevDp stores the maximum dot products for the previous row (i-1).
        // currDp stores the maximum dot products for the current row (i).
        // Both arrays are of size n2 + 1 to handle the base case where one subsequence is empty (represented by index 0).
        long[] prevDp = new long[n2 + 1];
        long[] currDp = new long[n2 + 1];

        // Initialize all DP values to a very small number (negative infinity).
        // This is crucial because dot products can be negative, and we need to
        // differentiate between an invalid state (no valid non-empty subsequence formed yet)
        // and a valid negative dot product. Using Long.MIN_VALUE ensures that any
        // valid product (even a negative one) will be greater than the initial state.
        Arrays.fill(prevDp, Long.MIN_VALUE);
        Arrays.fill(currDp, Long.MIN_VALUE);

        // Iterate through nums1 elements
        for (int i = 1; i <= n1; i++) {
            // For the start of each new row (currDp), currDp[0] must be Long.MIN_VALUE,
            // as it corresponds to an empty subsequence from nums2.
            // This is handled by the `Arrays.fill(currDp, Long.MIN_VALUE)` at the end of the previous iteration.

            // prev_diagonal_value_for_current_j stores dp[i-1][j-1] from the full DP table.
            // For j=1, it starts as prevDp[0] (which is Long.MIN_VALUE).
            long prev_diagonal_value_for_current_j = prevDp[0]; 

            // Iterate through nums2 elements
            for (int j = 1; j <= n2; j++) {
                // Calculate the product of the current elements from nums1 and nums2.
                long product = (long) nums1[i - 1] * nums2[j - 1];

                // Option 1: Form a new subsequence by taking nums1[i-1] and nums2[j-1] as the last elements.
                // If dp[i-1][j-1] (represented by prev_diagonal_value_for_current_j) was negative,
                // it's better to start a fresh subsequence with just the current product.
                // Hence, we take Math.max(0L, prev_diagonal_value_for_current_j).
                long val_taking_both = product + Math.max(0L, prev_diagonal_value_for_current_j);

                // Option 2: Exclude nums1[i-1]. The max dot product comes from nums1[0...i-2] and nums2[0...j-1].
                // This value is stored in prevDp[j].
                // Option 3: Exclude nums2[j-1]. The max dot product comes from nums1[0...i-1] and nums2[0...j-2].
                // This value is stored in currDp[j-1].
                currDp[j] = Math.max(prevDp[j], currDp[j-1]);
                
                // Compare with the option of taking both current elements.
                currDp[j] = Math.max(currDp[j], val_taking_both);

                // Update prev_diagonal_value_for_current_j for the next iteration (j+1).
                // For j+1, the new diagonal value needed is dp[i-1][j], which is prevDp[j] from the current state.
                prev_diagonal_value_for_current_j = prevDp[j];
            }
            
            // After filling currDp for the current row 'i', it becomes the 'previous' row for the next iteration 'i+1'.
            // Copy elements from currDp to prevDp.
            System.arraycopy(currDp, 0, prevDp, 0, n2 + 1);
            
            // Reset currDp for the next outer loop iteration.
            // Crucially, this sets currDp[0] to Long.MIN_VALUE, preparing it for the next row's calculations.
            Arrays.fill(currDp, Long.MIN_VALUE); 
        }

        // The final result is the maximum dot product for subsequences considering all elements,
        // which is stored in the last cell of the last processed row (prevDp[n2]).
        // The problem expects an int return type, and the maximum possible value (5 * 10^8) fits in int.
        return (int) prevDp[n2];
    }
}