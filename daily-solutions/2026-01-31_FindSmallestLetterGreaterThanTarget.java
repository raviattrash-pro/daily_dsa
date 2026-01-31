class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        // Time Complexity: O(log N), where N is the length of the 'letters' array.
        // Space Complexity: O(1), as no extra space proportional to input size is used.

        int n = letters.length;
        int low = 0;
        int high = n - 1;

        // Perform a binary search to find the smallest character lexicographically greater than target.
        // The loop condition 'low <= high' ensures we cover all possible indices and eventually
        // 'low' will point to the index where 'target' would be inserted if it were to maintain
        // the sorted order, specifically, the index of the first element strictly greater than 'target'.
        while (low <= high) {
            int mid = low + (high - low) / 2; // Calculate mid to prevent potential overflow

            if (letters[mid] > target) {
                // If letters[mid] is greater than target, it's a potential answer.
                // We want the *smallest* such character, so we try to find an even
                // smaller one in the left half (including mid-1).
                high = mid - 1;
            } else {
                // If letters[mid] is less than or equal to target, it's not the answer we're looking for.
                // We need to look in the right half for characters greater than target.
                low = mid + 1;
            }
        }

        // After the loop terminates, 'low' will be the index of the first character
        // in 'letters' that is lexicographically greater than 'target'.
        //
        // If no such character exists (i.e., all characters in 'letters' are
        // less than or equal to 'target'), then 'low' will be equal to 'n' (letters.length).
        //
        // The problem statement says: "If such a character does not exist,
        // return the first character in letters."
        //
        // Using the modulo operator 'low % n' elegantly handles both cases:
        // 1. If 'low' is a valid index (0 to n-1), then 'low % n' is 'low'.
        // 2. If 'low' is 'n' (meaning no character greater than 'target' was found),
        //    then 'n % n' is '0', returning letters[0] as required.
        return letters[low % n];
    }
}