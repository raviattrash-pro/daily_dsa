class Solution {
    // Time Complexity: O(n^2) where n is the number of rectangles.
    // We iterate through all unique pairs of rectangles.
    // Space Complexity: O(1).
    public long largestSquareArea(int[][] bottomLeft, int[][] topRight) {
        int n = bottomLeft.length;
        long maxSide = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Rectangle i: (x1, y1) to (x2, y2)
                int x1_i = bottomLeft[i][0];
                int y1_i = bottomLeft[i][1];
                int x2_i = topRight[i][0];
                int y2_i = topRight[i][1];

                // Rectangle j: (x1, y1) to (x2, y2)
                int x1_j = bottomLeft[j][0];
                int y1_j = bottomLeft[j][1];
                int x2_j = topRight[j][0];
                int y2_j = topRight[j][1];

                // Calculate the intersection rectangle
                // The bottom-left x-coordinate of the intersection is the maximum of the two bottom-left x-coordinates.
                int intersect_x1 = Math.max(x1_i, x1_j);
                // The bottom-left y-coordinate of the intersection is the maximum of the two bottom-left y-coordinates.
                int intersect_y1 = Math.max(y1_i, y1_j);
                // The top-right x-coordinate of the intersection is the minimum of the two top-right x-coordinates.
                int intersect_x2 = Math.min(x2_i, x2_j);
                // The top-right y-coordinate of the intersection is the minimum of the two top-right y-coordinates.
                int intersect_y2 = Math.min(y2_i, y2_j);

                // Check if an intersection exists
                if (intersect_x1 < intersect_x2 && intersect_y1 < intersect_y2) {
                    // If an intersection exists, calculate its width and height
                    long width = intersect_x2 - intersect_x1;
                    long height = intersect_y2 - intersect_y1;

                    // The largest square that can fit in this intersection has a side length
                    // equal to the minimum of the intersection's width and height.
                    long currentSide = Math.min(width, height);
                    
                    // Update the maximum side found so far
                    maxSide = Math.max(maxSide, currentSide);
                }
            }
        }
        
        // The result is the square of the maximum side length found.
        return maxSide * maxSide;
    }
}