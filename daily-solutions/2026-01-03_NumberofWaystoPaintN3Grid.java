class Solution {
    public int numOfWays(int n) {
        long MOD = 1_000_000_007;

        // For n=1:
        // 'abb' represents patterns where the first and third colors are the same (e.g., RGR).
        // A pattern (c1, c2, c1) must have c1 != c2.
        // Choices: 3 for c1, 2 for c2. Total = 3 * 2 = 6 such patterns.
        // Examples: RYR, RGR, YRY, YGY, GRG, GYG

        // 'abc' represents patterns where all three colors are different (e.g., RGY).
        // A pattern (c1, c2, c3) must have c1 != c2, c2 != c3, c1 != c3.
        // Choices: 3 for c1, 2 for c2, 1 for c3. Total = 3 * 2 * 1 = 6 such patterns.
        // Examples: RYG, RGY, YRG, YGR, GRY, GYR

        // dp[i][type] will represent the number of ways to paint 'i' rows such that the 'i'-th row
        // has a pattern of 'type'.
        // We can optimize space by only keeping track of the previous row's counts.
        long abb_prev = 6; // Number of ways to paint a row with 'abb' pattern
        long abc_prev = 6; // Number of ways to paint a row with 'abc' pattern

        // Dynamic programming to build up to 'n' rows
        for (int i = 2; i <= n; i++) {
            long abb_curr;
            long abc_curr;

            // Calculate abb_curr (number of ways to end current row with an 'abb' pattern):
            // A current 'abb' pattern (C1 C2 C1) can follow:
            // 1. An 'abb' pattern from the previous row (P1 P2 P1):
            //    Constraints: P1!=C1, P2!=C2.
            //    For any given (C1 C2 C1) pattern, there are 3 compatible (P1 P2 P1) patterns.
            //    Example: If current is (R Y R). Compatible (P1 P2 P1) patterns are (Y R Y), (Y G Y), (G R G).
            //    Contribution: 3 * abb_prev
            //
            // 2. An 'abc' pattern from the previous row (P1 P2 P3):
            //    Constraints: P1!=C1, P2!=C2, P3!=C1.
            //    For any given (C1 C2 C1) pattern, there are 2 compatible (P1 P2 P3) patterns.
            //    Example: If current is (R Y R). Compatible (P1 P2 P3) patterns are (Y R G), (G R Y).
            //    Contribution: 2 * abc_prev
            abb_curr = (3 * abb_prev + 2 * abc_prev) % MOD;

            // Calculate abc_curr (number of ways to end current row with an 'abc' pattern):
            // A current 'abc' pattern (C1 C2 C3) can follow:
            // 1. An 'abb' pattern from the previous row (P1 P2 P1):
            //    Constraints: P1!=C1, P2!=C2, P1!=C3.
            //    For any given (C1 C2 C3) pattern, there are 2 compatible (P1 P2 P1) patterns.
            //    Example: If current is (R Y G). Compatible (P1 P2 P1) patterns are (Y R Y), (Y G Y).
            //    Contribution: 2 * abb_prev
            //
            // 2. An 'abc' pattern from the previous row (P1 P2 P3):
            //    Constraints: P1!=C1, P2!=C2, P3!=C3.
            //    For any given (C1 C2 C3) pattern, there are 3 compatible (P1 P2 P3) patterns.
            //    Example: If current is (R Y G). Compatible (P1 P2 P3) patterns are (Y G R), (G R Y), (G Y R).
            //    Contribution: 3 * abc_prev
            abc_curr = (2 * abb_prev + 3 * abc_prev) % MOD;

            // Update previous counts for the next iteration
            abb_prev = abb_curr;
            abc_prev = abc_curr;
        }

        // The total number of ways for 'n' rows is the sum of ways ending in 'abb' and 'abc' patterns.
        return (int)((abb_prev + abc_prev) % MOD);
    }
}

/*
Time Complexity: O(N)
The solution iterates from i=2 to N once. Each iteration performs a constant number of arithmetic operations.
Given N up to 5000, this is efficient.

Space Complexity: O(1)
The solution uses a fixed number of variables (abb_prev, abc_prev, abb_curr, abc_curr, MOD)
regardless of the input 'n'. No arrays or collections scaled with 'n' are used.
*/