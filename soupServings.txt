class Solution {
    private Double[][] dp;

    public double soupServings(int n) {
        // Optimization: for large n, probability approaches 1
        if (n > 5000) return 1.0;

        int m = (int)Math.ceil(n / 25.0);
        dp = new Double[m+1][m+1];
        return findProb(m, m);
    }

    private double findProb(int a, int b) {
        if (a <= 0 && b <= 0) return 0.5;
        if (a <= 0) return 1.0;
        if (b <= 0) return 0.0;

        if (dp[a][b] != null) return dp[a][b];

        dp[a][b] = 0.25 * (
            findProb(a-4, b) +           // 100,0 → 4,0 after divide by 25
            findProb(a-3, b-1) +         // 75,25
            findProb(a-2, b-2) +         // 50,50
            findProb(a-1, b-3)           // 25,75
        );

        return dp[a][b];
    }
}