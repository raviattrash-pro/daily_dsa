class Solution {
    public int sumFourDivisors(int[] nums) {
        int totalSum = 0; // This will store the sum of divisors for all qualifying numbers.

        // Iterate through each number in the input array.
        for (int num : nums) {
            int count = 0; // Counts the number of divisors for the current 'num'.
            int currentSumOfDivisors = 0; // Stores the sum of divisors for the current 'num'.

            // Iterate from 1 up to the square root of 'num' to find its divisors.
            // Using i * i <= num avoids repeated calls to Math.sqrt() and potential floating point issues.
            for (int i = 1; i * i <= num; i++) {
                if (num % i == 0) { // If 'i' is a divisor of 'num'.
                    // 'i' is one divisor.
                    count++;
                    currentSumOfDivisors += i;

                    // 'num / i' is another divisor.
                    // We need to check if 'i' and 'num / i' are distinct.
                    // They are distinct if i * i != num (e.g., for num = 6, i = 2, num/i = 3).
                    // They are the same if i * i == num (e.g., for num = 4, i = 2, num/i = 2).
                    if (i * i != num) {
                        count++;
                        currentSumOfDivisors += num / i;
                    }
                }

                // Optimization: If we've already found more than 4 divisors,
                // then this number cannot have exactly four divisors.
                // We can stop checking further for this 'num' and move to the next.
                // This saves computation for numbers with many divisors.
                if (count > 4) {
                    break; 
                }
            }

            // After checking all divisors up to sqrt(num), if 'num' has exactly 4 divisors,
            // add the sum of its divisors to the total sum.
            if (count == 4) {
                totalSum += currentSumOfDivisors;
            }
        }

        return totalSum; // Return the accumulated total sum.
    }
}

// Time Complexity: O(N * sqrt(M)), where N is the length of the nums array and M is the maximum value in nums.
// In the worst case, N = 10^4 and M = 10^5. So, 10^4 * sqrt(10^5) approx 10^4 * 316 = 3.16 * 10^6 operations.
// This is efficient enough for the given constraints.

// Space Complexity: O(1), as only a few extra variables are used regardless of input size.