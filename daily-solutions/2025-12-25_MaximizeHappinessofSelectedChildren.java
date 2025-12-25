The problem asks us to select `k` children out of `n` available children to maximize the sum of their happiness values. The tricky part is that each time a child is selected, the happiness of all *unselected* children decreases by 1, with happiness values capped at 0.

Let's analyze the process. We make `k` selections.
When we pick the first child, its happiness is its original value.
When we pick the second child, `1` turn has passed, so all unselected children (including this one, if it wasn't picked first) have their happiness reduced by 1. So, its current happiness will be `max(0, original_happiness - 1)`.
When we pick the `j`-th child (0-indexed, so `j` goes from `0` to `k-1`), `j` turns have already passed. During each of these `j` turns, the happiness of this child (if it was unselected) would have decreased by 1. Therefore, when we pick it, its happiness will be `max(0, original_happiness - j)`.

To maximize the total sum, we want to pair the largest original happiness values with the smallest decrement counts. The smallest decrement counts are `0, 1, 2, ..., k-1`.
This implies a greedy strategy:
1. Sort the `happiness` array in descending order.
2. In the first turn (decrement count `0`), pick the child with the highest original happiness. Its contribution is `happiness[0] - 0`.
3. In the second turn (decrement count `1`), pick the child with the second highest original happiness. Its contribution is `max(0, happiness[1] - 1)`.
4. Continue this for `k` turns. In the `j`-th turn (0-indexed), pick the child with the `j`-th highest original happiness. Its contribution is `max(0, happiness[j] - j)`.

**Algorithm:**
1. Sort the `happiness` array in ascending order. This allows us to easily access the largest happiness values by iterating from the end of the array.
2. Initialize a `long` variable `totalHappiness` to 0. This is because the sum can exceed the maximum value of an `int`.
3. Iterate `i` from `0` to `k-1`. In this loop, `i` represents two things:
   - The `i`-th child being picked (0-indexed).
   - The number of decrements that have occurred before picking this child.
4. In each iteration `i`:
   a. Get the `(i+1)`-th largest original happiness value. Since the array is sorted ascending, this value is at index `happiness.length - 1 - i`. Let's call this `originalHappiness`.
   b. Calculate the current happiness of this child: `currentHappiness = originalHappiness - i`. Remember to cast to `long` for the calculation to avoid potential intermediate overflow if `originalHappiness` is large.
   c. If `currentHappiness` is greater than 0, add it to `totalHappiness`.
   d. If `currentHappiness` is 0 or less, it means this child (and any subsequent children, as they will have less or equal original happiness and more decrements) will contribute 0 to the total sum. So, we can `break` out of the loop early as an optimization.
5. Return `totalHappiness`.

**Example Walkthrough:**
`happiness = [1,2,3], k = 2`

1. `Arrays.sort(happiness)` results in `[1,2,3]`. `n = 3`.
2. `totalHappiness = 0L`.
3. Loop for `i` from `0` to `k-1` (i.e., `i = 0, 1`):
   - **`i = 0`**:
     - `originalHappiness = happiness[3 - 1 - 0] = happiness[2] = 3`.
     - `currentHappiness = (long)3 - 0 = 3`.
     - `currentHappiness > 0`, so `totalHappiness += 3` (total becomes `3`).
   - **`i = 1`**:
     - `originalHappiness = happiness[3 - 1 - 1] = happiness[1] = 2`.
     - `currentHappiness = (long)2 - 1 = 1`.
     - `currentHappiness > 0`, so `totalHappiness += 1` (total becomes `4`).
4. Loop finishes.
5. Return `4`.

**Time Complexity:**
- Sorting the `happiness` array takes `O(N log N)` time, where `N` is `happiness.length`.
- The loop runs at most `k` times. Inside the loop, operations are `O(1)`. So this part is `O(k)`.
- Total time complexity: `O(N log N + k)`, which simplifies to `O(N log N)` since `k <= N`.

**Space Complexity:**
- `Arrays.sort()` for primitive arrays in Java uses an in-place dual-pivot quicksort, typically having `O(log N)` auxiliary space for stack frames, or `O(N)` in worst-case scenarios for some implementations like Timsort (which Java uses for objects and potentially large primitive arrays).
- Other variables use `O(1)` space.
- Thus, the space complexity is dominated by the sorting algorithm, which can be considered `O(log N)` or `O(N)`.


import java.util.Arrays;

class Solution {
    public long maximumHappinessSum(int[] happiness, int k) {
        // Sort the happiness array in ascending order.
        // This allows us to easily pick the k largest happiness values by iterating from the end.
        // Time Complexity: O(N log N), where N is happiness.length
        Arrays.sort(happiness);

        long totalHappiness = 0;
        
        // Iterate k times to select k children.
        // 'i' represents the number of children already picked (0-indexed).
        // It also corresponds to the decrement value applied to the current child's happiness.
        // The children are picked in decreasing order of their original happiness.
        // The largest original happiness is happiness[happiness.length - 1].
        // The second largest is happiness[happiness.length - 2], and so on.
        for (int i = 0; i < k; i++) {
            // Get the original happiness of the current child being considered.
            // This is the (i+1)-th largest happiness value.
            int originalHappiness = happiness[happiness.length - 1 - i];
            
            // Calculate the actual happiness this child contributes.
            // It's its original happiness minus 'i' decrements.
            // Happiness cannot be negative.
            long currentHappiness = (long)originalHappiness - i;

            // If the calculated happiness is positive, add it to the total.
            // If it's zero or negative, we add 0, and further children will also contribute 0.
            // This is because subsequent children have either equal or less original happiness
            // and will be subject to an even greater number of decrements (i+1, i+2, etc.).
            if (currentHappiness > 0) {
                totalHappiness += currentHappiness;
            } else {
                // Optimization: If the current child contributes 0 or less happiness,
                // all subsequent children will also contribute 0 or less.
                // So, we can stop early.
                break;
            }
        }

        return totalHappiness;
    }
}