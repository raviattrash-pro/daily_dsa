class Solution {
    public int minDeletionSize(String[] strs) {
        // Time Complexity: O(m * n)
        // Space Complexity: O(1)

        // n is the number of rows (number of strings)
        // m is the number of columns (length of each string)
        int n = strs.length;
        int m = strs[0].length(); 

        int deletedColumnCount = 0;

        // Iterate through each column
        for (int j = 0; j < m; j++) {
            // For each column, check if it's sorted lexicographically
            // We compare adjacent characters from top to bottom
            for (int i = 0; i < n - 1; i++) {
                // If the character in the current row is greater than the character
                // in the next row, then this column is not sorted.
                if (strs[i].charAt(j) > strs[i+1].charAt(j)) {
                    deletedColumnCount++;
                    break; // This column is unsorted, no need to check further rows for this column.
                           // Move to the next column.
                }
            }
        }

        return deletedColumnCount;
    }
}