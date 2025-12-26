import java.util.Arrays;

class Solution {
    public int bestClosingTime(String customers) {
        int n = customers.length();
        
        // Calculate prefix sums for 'N' (penalty if open)
        // openPenalty[i] will store the count of 'N's from hour 0 to hour i-1.
        // If shop closes at hour j, hours 0 to j-1 are open.
        int[] openPenalty = new int[n + 1];
        for (int i = 0; i < n; i++) {
            openPenalty[i + 1] = openPenalty[i] + (customers.charAt(i) == 'N' ? 1 : 0);
        }
        
        // Calculate suffix sums for 'Y' (penalty if closed)
        // closedPenalty[i] will store the count of 'Y's from hour i to hour n-1.
        // If shop closes at hour j, hours j to n-1 are closed.
        int[] closedPenalty = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            closedPenalty[i] = closedPenalty[i + 1] + (customers.charAt(i) == 'Y' ? 1 : 0);
        }
        
        int minPenalty = Integer.MAX_VALUE;
        int bestClosingHour = -1;
        
        // Iterate through all possible closing hours j from 0 to n
        for (int j = 0; j <= n; j++) {
            // Penalty if shop is open from hour 0 to j-1
            int currentOpenPenalty = openPenalty[j];
            
            // Penalty if shop is closed from hour j to n-1
            int currentClosedPenalty = closedPenalty[j];
            
            int totalPenalty = currentOpenPenalty + currentClosedPenalty;
            
            if (totalPenalty < minPenalty) {
                minPenalty = totalPenalty;
                bestClosingHour = j;
            }
        }
        
        return bestClosingHour;
    }
}

// Time Complexity: O(N) where N is the length of the customers string.
// We iterate through the string once to calculate openPenalty prefix sums,
// once to calculate closedPenalty suffix sums, and once to calculate total penalties.
// Each iteration takes O(N) time.

// Space Complexity: O(N) for storing the openPenalty and closedPenalty arrays.
// Each array has a size of N+1.