class Solution {
    // Time Complexity: O(N * budget * 2) where N is the number of employees.
    // In a tree, the sum of children operations across all nodes is proportional to N.
    // For each node, we perform a DP transition that involves iterating through its children and
    // combining their DP states. Each child's DP state is an array of size (budget + 1).
    // The combination step for two DP arrays of size K takes O(K^2) time.
    // If a node has C children, and each child's subtree has a DP array of size (budget+1),
    // combining all children's results for a node takes O(C * (budget+1)^2) in the worst case (if done sequentially with pairwise combination)
    // or more efficiently O(degree * budget) with careful merging.
    // A more precise analysis for tree DP (knapsack on tree) gives O(N * budget) or O(N * budget^2) in some variants.
    // Here, each node has two states (bought by boss or not). So it's effectively a tree DP with item choices.
    // The DP state for a subtree rooted at `u` is `dp[u][cost]`, representing the max profit for `cost` budget.
    // When combining children `v1, v2, ..., vk` into `u`:
    // For each child `v`, we get `dp[v][cost_v][bought_by_boss_v_status]`.
    // The inner loop for combining is effectively a knapsack problem for each node's children.
    // Total complexity for knapsack on tree: O(N * budget).
    // The `dfs` function processes each node once. Inside `dfs`, the combination of children's results
    // is like a tree knapsack. The total complexity for merging all children's DP states across the entire tree
    // is O(N * budget) because each budget state `k` for a node `u` is 'transferred' at most once to its parent
    // (with its cost and profit) and merged with other children results.
    // The total size of all `dp` arrays summed over all nodes is O(N * budget).
    // The combination of DP arrays: dp[u] = dp[u] + dp[v] (for each child v). This is effectively a convolution.
    // If dp[u] has size K1 and dp[v] has size K2, merging takes K1*K2.
    // But in tree knapsack, if we combine `dp_subtree_A` and `dp_subtree_B` (where `subtree_A` and `subtree_B` are disjoint subtrees),
    // the size of the combined result is `size_A + size_B`. The cost for merging is `size_A * size_B`.
    // Total cost across the tree is O(N * budget^2) in the worst case if not careful.
    // However, the standard tree knapsack DP with two states (take/not take current item) on a subtree of size `s` with budget `b` is O(s * b).
    // Here, each item (employee) has two costs/profits depending on the boss.
    // The overall complexity is O(N * budget).
    // Each node `u` has `budget+1` states for `dp[u][0]` and `dp[u][1]`.
    // Combining these results for children:
    // `temp_dp[k]` for `dp[u][0]` (u not buying) and `temp_dp[k]` for `dp[u][1]` (u buying).
    // Then iterate through children `v`. For each child, combine `dp[v][0]` (v not buying) and `dp[v][1]` (v buying with discount).
    // This part is the knapsack-like merging. If child `v`'s boss `u` doesn't buy, `v`'s cost is `present[v]`.
    // If child `v`'s boss `u` buys, `v`'s cost is `floor(present[v]/2)`.
    // This is essentially merging two knapsack solutions at each step.
    // The inner loop for combining two lists of DP states `curr_dp` (for node u so far) and `child_dp` (for child v)
    // takes `O(budget^2)`. Since this happens for each child, this looks like `O(N * budget^2)`.
    // But standard tree knapsack optimization leads to O(N * budget).
    // Let's verify the tree knapsack complexity. When merging results for a node with its children:
    // For a node `u` and its child `v`, let `dp_u` be the current DP state for `u`'s subtree (including children processed so far)
    // and `dp_v` be the DP state for `v`'s subtree.
    // To combine them, we iterate `cost_u` from 0 to `budget` and `cost_v` from 0 to `budget`.
    // `new_dp[cost_u + cost_v] = max(new_dp[cost_u + cost_v], dp_u[cost_u] + dp_v[cost_v])`.
    // This is `O(budget^2)` for one child. Summing `budget^2` over all children of a node can be large.
    // The optimization for tree knapsack relies on the fact that `size(subtree(u))` can be small.
    // Here, `dp[u][0][k]` and `dp[u][1][k]` are the max profits for budget `k` if `u` doesn't buy / `u` buys.
    // These `dp` arrays have size `budget+1`.
    // Each node `u` has two options: buy or not buy.
    // If `u` buys: cost `present[u]`, profit `future[u] - present[u]`. Children get discount.
    // If `u` doesn't buy: cost 0, profit 0. Children don't get discount.
    // Let `dp[u][0][k]` be max profit for subtree rooted at `u`, given budget `k`, if `u` DOES NOT buy.
    // Let `dp[u][1][k]` be max profit for subtree rooted at `u`, given budget `k`, if `u` DOES buy.
    //
    // Base case (leaf node `u`):
    // `dp[u][0][0] = 0`. All other `dp[u][0][k] = -infinity`.
    // If `present[u] <= k`, `dp[u][1][present[u]] = future[u] - present[u]`. All other `dp[u][1][k] = -infinity`.
    //
    // Recursive step (node `u` with children `v1, ..., vm`):
    // Initialize `temp_dp0` and `temp_dp1` to `dp[u][0]` and `dp[u][1]` respectively, considering only `u` itself.
    // `temp_dp0[0] = 0`. All others `-infinity`.
    // If `present[u] <= budget`, `temp_dp1[present[u]] = future[u] - present[u]`. All others `-infinity`.
    // For each child `v`:
    //   Recursively call `dfs(v)`. This returns `dp[v][0]` and `dp[v][1]` (max profits for `v`'s subtree).
    //   Create `next_dp0` and `next_dp1` arrays, initialized to `-infinity`.
    //   To compute `next_dp0` (if `u` doesn't buy):
    //     For `k_u` from 0 to `budget`:
    //       If `temp_dp0[k_u] == -infinity`, continue.
    //       For `k_v` from 0 to `budget - k_u`:
    //         // Option 1: `v` does not buy. No discount (boss `u` didn't buy).
    //         `next_dp0[k_u + k_v] = max(next_dp0[k_u + k_v], temp_dp0[k_u] + dp[v][0][k_v])`
    //         // Option 2: `v` buys. No discount (boss `u` didn't buy).
    //         // The discount only applies if boss buys. So we use `present[v]` for `v`.
    //         // This is wrong. `dp[v][0]` already considers `v` not buying or buying at `present[v]`.
    //         // `dp[v][1]` already considers `v` buying at `floor(present[v]/2)` when boss `v` buys.
    //         // The issue is that `dp[v][0]` and `dp[v][1]` for child `v` are computed assuming `v` is the root.
    //         // We need to re-evaluate for child `v` based on `u`'s status.
    //
    // Revised DP state:
    // `dp[u][cost]` = maximum profit for the subtree rooted at `u`, given `cost` budget, assuming `u`'s parent DOES NOT buy `u`'s stock.
    // `dp_discounted[u][cost]` = maximum profit for the subtree rooted at `u`, given `cost` budget, assuming `u`'s parent DOES buy `u`'s stock (so `u` gets discount).
    //
    // For node `u`:
    //   Calculate `cost_u_no_discount = present[u]`. `profit_u_no_discount = future[u] - present[u]`.
    //   Calculate `cost_u_discounted = present[u] / 2`. `profit_u_discounted = future[u] - (present[u] / 2)`.
    //
    //   Initialize two temporary DP arrays, `res_no_disc` and `res_disc`, both of size `budget+1`, filled with `-infinity`.
    //   `res_no_disc[0] = 0`. (u doesn't buy, no cost, no profit)
    //   If `cost_u_no_discount <= budget`, `res_no_disc[cost_u_no_discount] = max(res_no_disc[cost_u_no_discount], profit_u_no_discount)`.
    //
    //   `res_disc[0] = 0`.
    //   If `cost_u_discounted <= budget`, `res_disc[cost_u_discounted] = max(res_disc[cost_u_discounted], profit_u_discounted)`.
    //
    //   For each child `v` of `u`:
    //     Recursively call `dfs(v)`. This returns `dp[v]` and `dp_discounted[v]`.
    //
    //     Create `next_res_no_disc` and `next_res_disc` arrays, initialized to `-infinity`.
    //
    //     // Case 1: `u` does NOT buy (so children `v` do NOT get discount from `u`)
    //     For `k_u` from 0 to `budget`:
    //       If `res_no_disc[k_u] == -infinity`, continue.
    //       For `k_v` from 0 to `budget - k_u`:
    //         // Child `v`'s status is "no discount" from `u`.
    //         // So we use `dp[v][k_v]` (which is `dp_no_discount[v][k_v]`)
    //         `next_res_no_disc[k_u + k_v] = max(next_res_no_disc[k_u + k_v], res_no_disc[k_u] + dp[v][k_v])`
    //
    //     // Case 2: `u` DOES buy (so children `v` DO get discount from `u`)
    //     For `k_u` from 0 to `budget`:
    //       If `res_disc[k_u] == -infinity`, continue.
    //       For `k_v` from 0 to `budget - k_u`:
    //         // Child `v`'s status is "discounted" from `u`.
    //         // So we use `dp_discounted[v][k_v]`
    //         `next_res_disc[k_u + k_v] = max(next_res_disc[k_u + k_v], res_disc[k_u] + dp_discounted[v][k_v])`
    //
    //     Update `res_no_disc = next_res_no_disc` and `res_disc = next_res_disc`.
    //
    //   After processing all children, store `res_no_disc` into `dp[u]` and `res_disc` into `dp_discounted[u]`.
    //
    // Final answer is the maximum value in `dp[1]` (since CEO has no boss, so no discount).
    // This is `O(N * budget^2)`. Given `N=160, budget=160`, `160 * 160^2 = 160^3 = 4,096,000`. This might be acceptable.
    // Let's implement this `O(N * budget^2)` approach.

    // Space Complexity: O(N * budget). We store two DP arrays of size `budget+1` for each of `N` nodes.
    // The recursion stack depth is `N` in the worst case (skewed tree). So `O(N)` for recursion stack.
    // Overall `O(N * budget)`.

    List<Integer>[] adj;
    int[] presentPrices;
    int[] futurePrices;
    int maxBudget;

    // dp[u][cost] stores max profit for subtree rooted at u, given 'cost' budget, if u's boss DOES NOT buy.
    int[][] dp; 
    // dp_discounted[u][cost] stores max profit for subtree rooted at u, given 'cost' budget, if u's boss DOES buy.
    int[][] dp_discounted; 

    public int maximumProfit(int n, int[] present, int[] future, int[][] hierarchy, int budget) {
        this.presentPrices = present; // 0-indexed internally
        this.futurePrices = future;   // 0-indexed internally
        this.maxBudget = budget;

        adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        // Build adjacency list (tree structure)
        for (int[] edge : hierarchy) {
            // Adjust to 0-indexed: u_i-1 is boss of v_i-1
            adj[edge[0] - 1].add(edge[1] - 1); 
        }

        // Initialize DP arrays with a value indicating "not possible" or negative infinity.
        // We use -1 to represent negative infinity, as profit can be 0 or positive.
        // Costs are always positive.
        dp = new int[n][budget + 1];
        dp_discounted = new int[n][budget + 1];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
            Arrays.fill(dp_discounted[i], -1);
        }

        // CEO is employee 1 (index 0). Start DFS from CEO.
        dfs(0); 

        int maxProfit = 0;
        // CEO (node 0) has no boss, so only consider dp[0][k] (no discount for CEO)
        for (int k = 0; k <= budget; k++) {
            maxProfit = Math.max(maxProfit, dp[0][k]);
        }
        return maxProfit;
    }

    // DFS function to compute DP states for subtree rooted at 'u'
    private void dfs(int u) {
        // Costs and profits for employee 'u'
        int cost_u_no_discount = presentPrices[u];
        int profit_u_no_discount = futurePrices[u] - presentPrices[u];

        int cost_u_discounted = presentPrices[u] / 2;
        int profit_u_discounted = futurePrices[u] - (presentPrices[u] / 2);

        // Initialize temporary DP arrays for 'u' considering only 'u' itself.
        // These will be merged with children's results.
        int[] current_res_no_disc = new int[maxBudget + 1];
        Arrays.fill(current_res_no_disc, -1); // -1 means impossible to achieve this budget
        current_res_no_disc[0] = 0; // It's always possible to buy nothing for 0 cost and get 0 profit.

        int[] current_res_disc = new int[maxBudget + 1];
        Arrays.fill(current_res_disc, -1);
        current_res_disc[0] = 0;

        // Option 1: u buys without discount
        if (cost_u_no_discount <= maxBudget) {
            current_res_no_disc[cost_u_no_discount] = Math.max(current_res_no_disc[cost_u_no_discount], profit_u_no_discount);
        }

        // Option 2: u buys with discount (this is for `dp_discounted[u]`, meaning u's boss bought)
        if (cost_u_discounted <= maxBudget) {
            current_res_disc[cost_u_discounted] = Math.max(current_res_disc[cost_u_discounted], profit_u_discounted);
        }

        // Merge children's results
        for (int v : adj[u]) {
            dfs(v); // Recursively compute DP states for child 'v'

            int[] next_res_no_disc = new int[maxBudget + 1];
            Arrays.fill(next_res_no_disc, -1);
            int[] next_res_disc = new int[maxBudget + 1];
            Arrays.fill(next_res_disc, -1);

            // Calculate next_res_no_disc (if u does NOT buy)
            // In this case, child 'v' also does NOT get a discount from 'u'.
            // So we use dp[v] (which is dp_no_discount[v]).
            for (int k_u = 0; k_u <= maxBudget; k_u++) {
                if (current_res_no_disc[k_u] == -1) continue; // Skip if this state is unreachable

                for (int k_v = 0; k_v <= maxBudget - k_u; k_v++) {
                    if (dp[v][k_v] == -1) continue; // Skip if this child state is unreachable

                    next_res_no_disc[k_u + k_v] = Math.max(next_res_no_disc[k_u + k_v], current_res_no_disc[k_u] + dp[v][k_v]);
                }
            }

            // Calculate next_res_disc (if u DOES buy)
            // In this case, child 'v' DOES get a discount from 'u'.
            // So we use dp_discounted[v].
            for (int k_u = 0; k_u <= maxBudget; k_u++) {
                if (current_res_disc[k_u] == -1) continue; // Skip if this state is unreachable

                for (int k_v = 0; k_v <= maxBudget - k_u; k_v++) {
                    if (dp_discounted[v][k_v] == -1) continue; // Skip if this child state is unreachable

                    next_res_disc[k_u + k_v] = Math.max(next_res_disc[k_u + k_v], current_res_disc[k_u] + dp_discounted[v][k_v]);
                }
            }
            
            // Update current results with combined results
            current_res_no_disc = next_res_no_disc;
            current_res_disc = next_res_disc;
        }

        // After processing all children, store the final results for node 'u'
        System.arraycopy(current_res_no_disc, 0, dp[u], 0, maxBudget + 1);
        System.arraycopy(current_res_disc, 0, dp_discounted[u], 0, maxBudget + 1);
    }
}