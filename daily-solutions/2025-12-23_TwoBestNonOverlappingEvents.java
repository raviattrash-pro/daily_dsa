import java.util.Arrays;
import java.util.Comparator;

class Solution {
    /**
     * Finds the index of the first event in the sorted events array that starts at or after targetStartTime.
     * Uses binary search (lower_bound equivalent).
     *
     * @param events The 2D array of events, sorted by start time.
     * @param targetStartTime The minimum start time for the desired event.
     * @return The index of the first event that satisfies the condition, or events.length if no such event exists.
     */
    private int findNextEventIndex(int[][] events, int targetStartTime) {
        int low = 0;
        int high = events.length - 1;
        int ans = events.length; // Default to events.length if no event starts at or after targetStartTime

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (events[mid][0] >= targetStartTime) {
                ans = mid;         // This event (events[mid]) is a potential candidate, record its index
                high = mid - 1;    // Try to find an even earlier event that also satisfies the condition
            } else {
                low = mid + 1;     // Current event (events[mid]) starts too early, need to look in the right half
            }
        }
        return ans;
    }

    public int maxTwoEvents(int[][] events) {
        int n = events.length;

        // Step 1: Sort events by their start times.
        // If start times are equal, the relative order does not affect correctness
        // because findNextEventIndex only depends on start times and suffixMaxValue
        // correctly considers the maximum value irrespective of exact event order after nextEventIdx.
        Arrays.sort(events, Comparator.comparingInt(a -> a[0]));

        // Step 2: Initialize maxTotalValue with the maximum value of any single event.
        // This covers the case where we only choose one event, or if no two non-overlapping events can be found.
        int maxTotalValue = 0;
        for (int[] event : events) {
            maxTotalValue = Math.max(maxTotalValue, event[2]);
        }

        // Step 3: Precompute suffix_max_value.
        // suffixMaxValue[k] stores the maximum value among events[k], events[k+1], ..., events[n-1].
        // This allows O(1) lookup for the best second event after a given index.
        int[] suffixMaxValue = new int[n];
        suffixMaxValue[n - 1] = events[n - 1][2]; // Base case: last event's value
        for (int k = n - 2; k >= 0; k--) {
            suffixMaxValue[k] = Math.max(events[k][2], suffixMaxValue[k + 1]);
        }

        // Step 4: Iterate through each event. For each event 'i', consider it as the first event.
        // Then, find the best non-overlapping second event that starts after event 'i' ends.
        for (int i = 0; i < n; i++) {
            int currentEventValue = events[i][2];
            int currentEventEndTime = events[i][1];

            // The second event must start at or after currentEventEndTime + 1 to be non-overlapping.
            int targetStartTimeForSecondEvent = currentEventEndTime + 1;

            // Find the index of the first event that can potentially be the second non-overlapping event.
            int nextEventIdx = findNextEventIndex(events, targetStartTimeForSecondEvent);

            // If a valid second event can be found (i.e., nextEventIdx is within bounds):
            if (nextEventIdx < n) {
                // The maximum value we can get from a second event starting at or after targetStartTimeForSecondEvent
                // is stored in suffixMaxValue[nextEventIdx].
                maxTotalValue = Math.max(maxTotalValue, currentEventValue + suffixMaxValue[nextEventIdx]);
            }
        }

        return maxTotalValue;
    }
}
// Time Complexity: O(N log N)
//   - Sorting takes O(N log N).
//   - Initial scan for max single event: O(N).
//   - Precomputing suffix_max_value: O(N).
//   - Main loop: N iterations. Inside the loop, findNextEventIndex (binary search) takes O(log N). Total O(N log N).
//   Overall, the dominant factor is O(N log N).
// Space Complexity: O(N)
//   - O(N) for storing the suffixMaxValue array.
//   - O(N) for sorting in the worst case (Java's TimSort uses O(N) auxiliary space).