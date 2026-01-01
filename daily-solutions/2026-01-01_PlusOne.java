import java.util.Arrays;

class Solution {
    public int[] plusOne(int[] digits) {
        int n = digits.length;

        // Iterate from the least significant digit (rightmost) to the most significant digit (leftmost)
        for (int i = n - 1; i >= 0; i--) {
            // If the current digit is less than 9, we can simply increment it
            // and we are done, as there's no carry-over.
            if (digits[i] < 9) {
                digits[i]++;
                return digits; // Return the modified array
            }
            // If the current digit is 9, set it to 0 and continue the loop
            // to propagate the carry-over to the next digit to the left.
            digits[i] = 0;
        }

        // If the loop completes, it means all original digits were 9 (e.g., [9], [9,9], [9,9,9]).
        // In this case, we need to create a new array that is one digit longer.
        // The new most significant digit will be 1, and all subsequent digits will be 0.
        int[] newDigits = new int[n + 1];
        newDigits[0] = 1; // The carry-over becomes the new leading digit
        // The rest of the newDigits array elements will be 0 by default, which is correct
        // (e.g., for [9,9], after the loop, it becomes [0,0]. The new array is [1,0,0]).

        return newDigits;
    }
}

// Time Complexity: O(N), where N is the number of digits.
// In the worst case (e.g., [9,9,...9]), we iterate through all N digits and then create a new array of size N+1.
// In the best case (e.g., [1,2,3]), we only modify the last digit, taking O(1) time.
//
// Space Complexity: O(1) in the best/average case.
// If no carry-over beyond the original length is needed, we modify the input array in place.
// O(N) in the worst case (e.g., [9,9,...9]), where a new array of size N+1 is created.