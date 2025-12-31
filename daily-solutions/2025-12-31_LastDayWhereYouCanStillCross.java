class Solution {
    // Union-Find (Disjoint Set Union) class
    private static class UnionFind {
        private int[] parent;
        private int[] size;
        
        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        // Find operation with path compression
        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }
        
        // Union operation with union by size
        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    parent[rootI] = rootJ;
                    size[rootJ] += size[rootI];
                } else {
                    parent[rootJ] = rootI;
                    size[rootI] += size[rootJ];
                }
            }
        }
    }

    public int latestDayToCross(int row, int col, int[][] cells) {
        // Total number of cells in the grid.
        int N = row * col;
        
        // Initialize Union-Find structure with N cells plus two virtual nodes.
        // The virtual nodes represent the top row (TOP_VIRTUAL) and the bottom row (BOTTOM_VIRTUAL).
        // Cell indices 0 to N-1 for grid cells.
        // N for TOP_VIRTUAL, N+1 for BOTTOM_VIRTUAL.
        UnionFind uf = new UnionFind(N + 2);
        
        int TOP_VIRTUAL = N;
        int BOTTOM_VIRTUAL = N + 1;
        
        // `isLand[r][c]` tracks whether a cell (r,c) is land (true) or water (false).
        // In this reverse simulation, initially all cells are considered water (false).
        // We progressively turn them into land.
        boolean[][] isLand = new boolean[row][col];
        
        // Directions for checking 4-connected neighbors (up, down, left, right)
        int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // The problem asks for the *last* day it's possible to cross.
        // This implies a monotonic property, suggesting binary search or processing in reverse.
        // We process `cells` in reverse order. `cells[i]` is the cell that turns to water on day `i+1`.
        // By iterating `i` from `N-1` down to `0`, we simulate the grid state
        // where cells `cells[i], cells[i+1], ..., cells[N-1]` are land,
        // and cells `cells[0], ..., cells[i-1]` are water.
        // This state corresponds to "Day `i`" in the problem's context (meaning `i` cells are water).
        for (int i = N - 1; i >= 0; i--) {
            int r = cells[i][0] - 1; // Convert 1-based row to 0-based
            int c = cells[i][1] - 1; // Convert 1-based column to 0-based
            
            isLand[r][c] = true; // This cell is now considered land in our reverse simulation
            
            // Convert 2D grid coordinates (r, c) to a 1D index for Union-Find.
            int currentCellIdx = r * col + c; 
            
            // If the current land cell is in the first row, union it with the TOP_VIRTUAL node.
            if (r == 0) {
                uf.union(currentCellIdx, TOP_VIRTUAL);
            }
            // If the current land cell is in the last row, union it with the BOTTOM_VIRTUAL node.
            if (r == row - 1) {
                uf.union(currentCellIdx, BOTTOM_VIRTUAL);
            }
            
            // Check all 4 neighbors of the current cell.
            // If a neighbor is valid (within bounds) and is already land, union the current cell with it.
            for (int[] dir : DIRS) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                
                // Check bounds and if the neighbor is already marked as land
                if (nr >= 0 && nr < row && nc >= 0 && nc < col && isLand[nr][nc]) {
                    int neighborCellIdx = nr * col + nc;
                    uf.union(currentCellIdx, neighborCellIdx);
                }
            }
            
            // After adding the current cell as land and forming all possible connections,
            // check if the TOP_VIRTUAL and BOTTOM_VIRTUAL nodes are connected.
            // If `find(TOP_VIRTUAL) == find(BOTTOM_VIRTUAL)` is true, it means there's a path
            // from the top row to the bottom row using only land cells.
            // Since we are iterating backward from the state of "all water" towards "all land",
            // the *first* time we find a path, `i` represents the latest day (number of flooded cells)
            // where a path exists.
            if (uf.find(TOP_VIRTUAL) == uf.find(BOTTOM_VIRTUAL)) {
                return i; // `i` corresponds to the number of flooded cells (and thus the day number).
            }
        }
        
        // This line should theoretically not be reached given the problem constraints (row, col >= 2),
        // as on Day 0 (when i=0 and no cells are flooded), a path must always exist.
        return 0; 
    }
}