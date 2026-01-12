class Solution {
    public int minTimeToVisitAllPoints(int[][] points) {
        int totalTime = 0;

        // Iterate through consecutive pairs of points.
        // We start from the first point (index 0) and go up to the second-to-last point (index points.length - 2).
        // For each i, we calculate the time to travel from points[i] to points[i+1].
        for (int i = 0; i < points.length - 1; i++) {
            // Current point coordinates
            int x1 = points[i][0];
            int y1 = points[i][1];

            // Next point coordinates
            int x2 = points[i+1][0];
            int y2 = points[i+1][1];

            // Calculate the absolute difference in x-coordinates
            int dx = Math.abs(x2 - x1);
            // Calculate the absolute difference in y-coordinates
            int dy = Math.abs(y2 - y1);

            // The minimum time to travel between two points (x1, y1) and (x2, y2)
            // is determined by the maximum of the absolute differences in their x and y coordinates.
            // This is because in 1 second, we can move one unit horizontally, one unit vertically,
            // or one unit diagonally (one unit horizontal AND one unit vertical).
            // Diagonal moves are the most efficient when both x and y coordinates need to change.
            // We can make min(dx, dy) diagonal moves. After these moves, one of the differences
            // (dx or dy) will be 0. The remaining difference (which is max(dx, dy) - min(dx, dy))
            // must be covered by straight (horizontal or vertical) moves.
            // Total time = min(dx, dy) + (max(dx, dy) - min(dx, dy)) = max(dx, dy).
            totalTime += Math.max(dx, dy);
        }

        return totalTime;
    }
}
// Time Complexity: O(N), where N is the number of points.
// We iterate through the points array once, performing constant time operations for each pair of consecutive points.
// Space Complexity: O(1).
// We only use a few integer variables for calculations, requiring constant extra space.