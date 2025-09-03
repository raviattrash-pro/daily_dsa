class Solution {
    public int numberOfPairs(int[][] points) {
        // Sort by x ascending, then y descending
        Arrays.sort(points, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[0] != b[0]) return a[0] - b[0]; // sort by x
                return b[1] - a[1]; // if x same, y descending
            }
        });

        int n = points.length, ans = 0;
        for (int i = 0; i < n; i++) {
            int top = points[i][1], bottom = Integer.MIN_VALUE;
            for (int j = i + 1; j < n; j++) {
                if (points[j][1] <= top && points[j][1] > bottom) {
                    bottom = points[j][1];
                    ans++;
                }
                if (bottom == top)
                    break;
            }
        }
        return ans;
    }
}