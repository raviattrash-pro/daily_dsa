class Solution {
    public long maxProfit(int[] prices, int[] strategy, int k) {
        int n = prices.length;
        
        // Time Complexity: O(N)
        // Space Complexity: O(1) (excluding input arrays)
        long initialProfit = 0;
        for (int i = 0; i < n; i++) {
            initialProfit += (long)strategy[i] * prices[i];
        }
        
        long maxOverallProfit = initialProfit;
        
        // Time Complexity: O(N)
        // Space Complexity: O(N) for val1, val2
        long[] val1 = new long[n]; // Profit change if strategy[i] becomes 0: -strategy[i] * prices[i]
        long[] val2 = new long[n]; // Profit change if strategy[i] becomes 1: (1 - strategy[i]) * prices[i]
        for (int i = 0; i < n; i++) {
            val1[i] = (long)-strategy[i] * prices[i];
            val2[i] = (long)(1 - strategy[i]) * prices[i];
        }
        
        // Time Complexity: O(N)
        // Space Complexity: O(N) for prefixSum1, prefixSum2
        long[] prefixSum1 = new long[n + 1];
        long[] prefixSum2 = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum1[i + 1] = prefixSum1[i] + val1[i];
            prefixSum2[i + 1] = prefixSum2[i] + val2[i];
        }
        
        // Time Complexity: O(N-K) which is O(N)
        // Space Complexity: O(1)
        int kHalf = k / 2;
        for (int j = 0; j <= n - k; j++) {
            long currentDeltaProfit = 0;
            
            // Sum of val1 for the first k/2 elements: strategy[j ... j + kHalf - 1] changed to 0
            currentDeltaProfit += (prefixSum1[j + kHalf] - prefixSum1[j]);
            
            // Sum of val2 for the last k/2 elements: strategy[j + kHalf ... j + k - 1] changed to 1
            currentDeltaProfit += (prefixSum2[j + k] - prefixSum2[j + kHalf]);
            
            maxOverallProfit = Math.max(maxOverallProfit, initialProfit + currentDeltaProfit);
        }
        
        return maxOverallProfit;
    }
}
// Total Time Complexity: O(N)
// Total Space Complexity: O(N)