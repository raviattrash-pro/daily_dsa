import java.util.Arrays;

class Solution {
    public int minimumBoxes(int[] apple, int[] capacity) {
        // Calculate the total number of apples that need to be stored.
        int totalApples = 0;
        for (int pack : apple) {
            totalApples += pack;
        }

        // Sort the capacity array in ascending order.
        // This allows us to easily pick boxes with the largest capacities
        // by iterating from the end of the sorted array.
        Arrays.sort(capacity);

        // Initialize variables to keep track of the current total capacity accumulated
        // and the count of boxes selected.
        int currentCapacitySum = 0;
        int boxesCount = 0;

        // Iterate through the sorted capacity array from the largest capacity downwards.
        // We greedily pick the largest capacity boxes first to minimize the total number of boxes.
        for (int i = capacity.length - 1; i >= 0; i--) {
            currentCapacitySum += capacity[i]; // Add the current box's capacity to the sum
            boxesCount++;                      // Increment the count of boxes used

            // If the accumulated capacity is now greater than or equal to the total number of apples,
            // we have found the minimum number of boxes required.
            if (currentCapacitySum >= totalApples) {
                break; // Exit the loop as we have enough capacity
            }
        }

        // Return the total count of boxes selected.
        return boxesCount;
    }
}

/*
Time Complexity:
O(N + M log M), where N is the number of apple packs (apple.length) and M is the number of boxes (capacity.length).
1. Summing the total apples: O(N)
2. Sorting the capacity array: O(M log M)
3. Iterating through the sorted capacity array to find the minimum boxes: O(M) in the worst case.
The dominant factor is the sorting step, so the overall time complexity is O(N + M log M).

Space Complexity:
O(1) auxiliary space.
1. A few integer variables (totalApples, currentCapacitySum, boxesCount) require O(1) space.
2. Arrays.sort() for primitive arrays in Java typically uses an in-place sort algorithm (like Dual-Pivot Quicksort) or very limited auxiliary space (e.g., O(log M) for stack frames in recursion), which is considered O(1) for practical purposes in competitive programming contexts given the constraints.
*/