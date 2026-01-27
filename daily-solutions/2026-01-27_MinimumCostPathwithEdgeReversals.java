import java.util.*;

class Solution {
    // Time Complexity: O(E + N log N) where N is the number of nodes and E is the number of edges.
    // In the state graph, there are 2N vertices and at most 3E edges.
    // Dijkstra's algorithm complexity is O(E_prime + V_prime log V_prime),
    // where V_prime = 2N and E_prime = O(E).
    // Thus, the complexity is O(E + N log N).
    // Space Complexity: O(N + E) for adjacency lists, dist array, and priority queue.
    public int minimumCost(int n, int[][] edges) {
        // Adjacency lists for original outgoing edges (u -> v with weight w)
        // adj[u] stores list of {v, w}
        List<int[]>[] adj = new ArrayList[n];
        // Adjacency lists for original incoming edges (x -> u with weight wx)
        // rev_adj[u] stores list of {x, wx}
        // This is used to find edges that can be reversed when at node u.
        List<int[]>[] rev_adj = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
            rev_adj[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            adj[u].add(new int[]{v, w});
            rev_adj[v].add(new int[]{u, w}); // Store original source u and weight w for incoming edge v <- u
        }

        // dist[i][0] = minimum cost to reach node i, with its switch available (unused by this path segment)
        // dist[i][1] = minimum cost to reach node i, with its switch already used (by a preceding path segment)
        int[][] dist = new int[n][2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        // PriorityQueue stores {cost, node, switch_state}
        // switch_state: 0 if switch at 'node' is available, 1 if switch at 'node' is used.
        // The PQ prioritizes entries with lower cost.
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // Start at node 0, with its switch available, and a cost of 0.
        dist[0][0] = 0;
        pq.offer(new int[]{0, 0, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int cost = current[0];
            int u = current[1];
            int switchState = current[2];

            // If we have already found a shorter path to this state (node u, switchState), skip.
            if (cost > dist[u][switchState]) {
                continue;
            }

            // Case 1: Switch at node u is available (switchState == 0)
            if (switchState == 0) {
                // Option A: Traverse a regular outgoing edge (u -> v)
                // We don't use u's switch for this. So, u's switch state effectively remains available
                // for other potential actions. We arrive at v, and v's switch is considered available.
                for (int[] edge : adj[u]) {
                    int v = edge[0];
                    int w = edge[1];
                    if (cost + w < dist[v][0]) {
                        dist[v][0] = cost + w;
                        pq.offer(new int[]{dist[v][0], v, 0});
                    }
                }

                // Option B: Use u's switch to reverse an incoming edge (x -> u) into (u -> x)
                // The reversal costs 2 * original_weight. This action consumes u's switch.
                // We arrive at x, and x's switch is considered available.
                for (int[] incomingEdge : rev_adj[u]) {
                    int x = incomingEdge[0]; // Original source node of the incoming edge (x -> u)
                    int wx = incomingEdge[1]; // Original weight of the incoming edge (x -> u)
                    
                    // Cost to traverse u -> x via reversal is 2 * wx
                    if (cost + 2 * wx < dist[x][0]) {
                        dist[x][0] = cost + 2 * wx;
                        pq.offer(new int[]{dist[x][0], x, 0});
                    }
                }

                // Internal transition: After arriving at 'u' with its switch available,
                // if we decide to use its switch (e.g., via Option B), then 'u''s switch is now consumed.
                // This means we can conceptually also reach the state (u, 1) - node u with its switch used -
                // at the current cost. This allows subsequent paths that might return to 'u' to find its switch used.
                if (cost < dist[u][1]) {
                    dist[u][1] = cost;
                    pq.offer(new int[]{dist[u][1], u, 1});
                }
            }
            // Case 2: Switch at node u is already used (switchState == 1)
            else { // switchState == 1
                // Option A: Traverse a regular outgoing edge (u -> v)
                // u's switch remains used. We arrive at v, and v's switch is considered available.
                for (int[] edge : adj[u]) {
                    int v = edge[0];
                    int w = edge[1];
                    if (cost + w < dist[v][0]) {
                        dist[v][0] = cost + w;
                        pq.offer(new int[]{dist[v][0], v, 0});
                    }
                }
                // Option B: Cannot use u's switch for reversal, as it's already been used.
            }
        }

        // The minimum cost to reach node n-1 is the minimum of reaching it with its switch
        // available or with its switch used.
        int minCost = Math.min(dist[n - 1][0], dist[n - 1][1]);
        
        // If minCost is still Integer.MAX_VALUE, it means n-1 is unreachable.
        return minCost == Integer.MAX_VALUE ? -1 : minCost;
    }
}