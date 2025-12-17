The problem "Best Time to Buy and Sell Stock V" extends the classic stock trading problems by allowing both normal transactions (buy low, sell high) and short selling transactions (sell high, buy low). The goal is to maximize total profit with at most `k` transactions. A key insight from the problem description and examples is that the profit from any single transaction, whether normal or short selling, is effectively `|prices[j] - prices[i]|`, where `i` is the opening day and `j` is the closing day. For a normal transaction, profit is `prices[j] - prices[i]`. For a short selling transaction, profit is `prices[i] - prices[j]`. Both are positive values if the transaction is profitable. The constraint is that transactions cannot overlap.

This problem can be solved using dynamic programming. We need to keep track of three states for each number of transactions `j` (from `0` to `k`):
1.  `no_hold[j]`: The maximum profit after completing `j` transactions, and currently not holding any stock (neither long nor short).
2.  `long_hold[j]`: The maximum profit after completing `j-1` transactions and initiating the `j`-th transaction as a *long* position (i.e., bought a stock and waiting to sell it).
3.  `short_hold[j]`: The maximum profit after completing `j-1` transactions and initiating the `j`-th transaction as a *short* position (i.e., short-sold a stock and waiting to buy it back).

Let `n` be the number of days (`prices.length`).

**Initialization:**
-   `no_hold[0...k]` are all initialized to `0`. This means with zero transactions, we have zero profit.
-   `long_hold[0...k]` and `short_hold[0...k]` are initialized to a very small negative number (e.g., `Integer.MIN_VALUE / 2`). This represents an impossible or highly unprofitable state, ensuring that any real profit (even negative if we just started a transaction) is preferred. `Integer.MIN_VALUE / 2` is used to prevent potential integer overflow if we add a positive price to `Integer.MIN_VALUE`.

**Iteration:**
We iterate through each `price` in the `prices` array (representing the current day's price).
For each `price`, we then iterate `j` (representing the number of transactions) from `k` down to `1`. Iterating downwards is crucial to ensure that `no_hold[j-1]` in the current day's calculations refers to the state before the `j`-th transaction is initiated (either from the previous day's state for `j-1` or from the current day's update for `j-1` which was correctly derived from a `j-2` state).

For each `j` (from `k` down to `1`):
-   **Update `no_hold[j]` (not holding any stock):**
    The maximum profit for `j` transactions, not holding stock, can come from three possibilities:
    1.  We were already not holding any stock on the previous day (`no_hold[j]`).
    2.  We closed a *long* position today: `long_hold[j]` (from previous calculations) + `current_price` (selling the stock). This completes the `j`-th transaction.
    3.  We closed a *short* position today: `short_hold[j]` (from previous calculations) - `current_price` (buying back the stock). This also completes the `j`-th transaction.
    `no_hold[j] = Math.max(no_hold[j], Math.max(long_hold[j] + price, short_hold[j] - price))`

-   **Update `long_hold[j]` (holding a long position):**
    The maximum profit for `j` transactions, currently holding a long position, can come from two possibilities:
    1.  We were already holding a long position on the previous day (`long_hold[j]`).
    2.  We initiated a *long* position today: `no_hold[j-1]` (profit from `j-1` completed transactions) - `current_price` (buying the stock). This effectively starts the `j`-th transaction.
    `long_hold[j] = Math.max(long_hold[j], no_hold[j-1] - price)`

-   **Update `short_hold[j]` (holding a short position):**
    The maximum profit for `j` transactions, currently holding a short position, can come from two possibilities:
    1.  We were already holding a short position on the previous day (`short_hold[j]`).
    2.  We initiated a *short* position today: `no_hold[j-1]` (profit from `j-1` completed transactions) + `current_price` (short-selling the stock). This effectively starts the `j`-th transaction.
    `short_hold[j] = Math.max(short_hold[j], no_hold[j-1] + price)`

**Result:**
After iterating through all prices, the maximum total profit will be the maximum value among `no_hold[0]` through `no_hold[k]`, because we want to finish with no open positions and at most `k` transactions.

**Time Complexity:**
O(N * K), where `N` is the number of days (length of `prices`) and `K` is the maximum number of allowed transactions. This is because we have an outer loop for `N` days and an inner loop for `K` transactions.

**Space Complexity:**
O(K), as we use three arrays of size `K+1` to store the DP states.

public class Solution {
    public int maxProfit(int[] prices, int k) {
        int n = prices.length;

        // If k is 0 or there are no prices, no profit can be made.
        if (n == 0 || k == 0) {
            return 0;
        }

        // Initialize DP arrays
        // no_hold[j]: max profit with j transactions, currently not holding any stock
        int[] no_hold = new int[k + 1];
        // long_hold[j]: max profit with j-1 completed transactions, and currently holding a long position for the j-th transaction
        int[] long_hold = new int[k + 1];
        // short_hold[j]: max profit with j-1 completed transactions, and currently holding a short position for the j-th transaction
        int[] short_hold = new int[k + 1];

        // Initialize holding states to a very small negative number
        // This makes sure any valid transaction (even if it leads to an overall loss, like buying and then selling at a loss)
        // is preferred over an impossible state. Using MIN_VALUE / 2 to avoid overflow with additions/subtractions.
        final int NEG_INF = Integer.MIN_VALUE / 2;
        for (int i = 0; i <= k; i++) {
            long_hold[i] = NEG_INF;
            short_hold[i] = NEG_INF;
        }
        // no_hold is initialized to 0 by default, which is correct (0 profit with 0 transactions).

        // Iterate through each day's price
        for (int price : prices) {
            // Iterate k downwards to ensure that `no_hold[j-1]` refers to the state before the j-th transaction
            // is initiated on the current day.
            for (int j = k; j >= 1; j--) {
                // To be in no_hold[j] state:
                // 1. Maintain no_hold[j] from the previous day.
                // 2. Close a long position: sell the stock. Profit increases by `price`. (long_hold[j] + price)
                // 3. Close a short position: buy back the stock. Profit increases by `price`. (short_hold[j] - price)
                no_hold[j] = Math.max(no_hold[j], Math.max(long_hold[j] + price, short_hold[j] - price));

                // To be in long_hold[j] state:
                // 1. Maintain long_hold[j] from the previous day.
                // 2. Open a long position: buy the stock. Profit decreases by `price`. (no_hold[j-1] - price)
                long_hold[j] = Math.max(long_hold[j], no_hold[j-1] - price);

                // To be in short_hold[j] state:
                // 1. Maintain short_hold[j] from the previous day.
                // 2. Open a short position: short-sell the stock. Profit increases by `price`. (no_hold[j-1] + price)
                short_hold[j] = Math.max(short_hold[j], no_hold[j-1] + price);
            }
        }

        // The maximum profit at the end will be the maximum value in any of the `no_hold` states,
        // as we want to end with no open positions and we can make at most `k` transactions.
        int maxProfit = 0;
        for (int j = 0; j <= k; j++) {
            maxProfit = Math.max(maxProfit, no_hold[j]);
        }

        return maxProfit;
    }
}