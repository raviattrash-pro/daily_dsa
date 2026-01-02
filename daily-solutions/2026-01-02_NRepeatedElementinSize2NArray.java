class Solution {
    public int repeatedNElement(int[] nums) {
        // Time Complexity: O(N), where N is the length of the nums array.
        // We iterate through the array at most once. Each comparison is O(1).

        // Space Complexity: O(1).
        // We are using a fixed number of variables, regardless of the input array size.

        // Due to the problem constraints (one element repeated n times in a 2n-length array),
        // it can be mathematically proven that there must exist at least two occurrences
        // of the repeated element X that are separated by a distance of 1, 2, or 3.
        // That is, for some index `i`, one of these conditions must be true:
        // nums[i] == nums[i+1] OR
        // nums[i] == nums[i+2] OR
        // nums[i] == nums[i+3]

        // We iterate through the array to find such a pattern.
        // The bounds checks (e.g., `i + 1 < nums.length`) ensure that we don't access out-of-bounds indices.
        // The loop runs from `i = 0` up to `nums.length - 1`. For `i = nums.length - 1`,
        // all conditions `i + k < nums.length` will be false, so no comparisons will be made,
        // which is correct as no further elements exist to compare against.
        for (int i = 0; i < nums.length; i++) {
            // Check for adjacent elements
            if (i + 1 < nums.length && nums[i] == nums[i + 1]) {
                return nums[i];
            }
            // Check for elements separated by one
            if (i + 2 < nums.length && nums[i] == nums[i + 2]) {
                return nums[i];
            }
            // Check for elements separated by two
            if (i + 3 < nums.length && nums[i] == nums[i + 3]) {
                return nums[i];
            }
        }

        // According to the problem statement, a repeated element satisfying the conditions
        // will always be found within the loop. This line should theoretically not be reached.
        return -1;
    }
}