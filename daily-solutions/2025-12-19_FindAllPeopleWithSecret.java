class Solution {

    // DSU class for managing disjoint sets with path compression and union by rank
    private static class DSU {
        int[] parent;
        int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootJ] < rank[rootI]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
                return true;
            }
            return false;
        }

        // Resets a person's DSU state to be a standalone set
        public void reset(int i) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public List<Integer> findAllPeopleWithSecret(int n, int[][] meetings, int firstPerson) {
        // Time Complexity: O(M log M + (N + M) * alpha(N))
        // M is meetings.length, N is n. alpha(N) is the inverse Ackermann function, practically constant.
        // Sorting meetings: O(M log M)
        // Grouping meetings into map: O(M log U) where U is number of unique times, or O(M) if using HashMap and then sorting keys (or directly iterating sorted meetings).
        // DSU operations: Each find/union is O(alpha(N)). There are O(M) union operations and O(N+M) find operations. Total: O((N + M) * alpha(N)).
        // Iterating through time groups and participants: The total number of participants across all time groups, and total resets, is O(N+M).
        // Overall: dominated by sorting and DSU ops, roughly O(M log M + N + M)

        // Space Complexity: O(N + M)
        // DSU arrays: O(N)
        // hasSecret array: O(N)
        // meetingsByTime map: O(M) in worst case (all meetings at distinct times).
        // Temporary sets/lists for participants, components, and people to reset: O(N) in worst case for one time group.
        // Total: O(N + M)

        // Sort meetings by time
        Arrays.sort(meetings, (a, b) -> Integer.compare(a[2], b[2]));

        // Group meetings by time using a TreeMap to maintain chronological order of time keys
        // Alternatively, we could just iterate the sorted meetings array and identify contiguous blocks of same-time meetings.
        Map<Integer, List<int[]>> meetingsByTime = new TreeMap<>();
        for (int[] meeting : meetings) {
            meetingsByTime.computeIfAbsent(meeting[2], k -> new ArrayList<>()).add(meeting);
        }

        // Initialize DSU and secret status
        DSU dsu = new DSU(n);
        boolean[] hasSecret = new boolean[n];
        
        // Person 0 and firstPerson initially have the secret
        hasSecret[0] = true;
        hasSecret[firstPerson] = true;

        // Iterate through meetings chronologically by time group
        for (Map.Entry<Integer, List<int[]>> entry : meetingsByTime.entrySet()) {
            List<int[]> currentMeetings = entry.getValue();

            Set<Integer> participantsInThisTimeGroup = new HashSet<>();
            
            // Perform unions for all meetings at the current time
            for (int[] meeting : currentMeetings) {
                int p1 = meeting[0];
                int p2 = meeting[1];
                participantsInThisTimeGroup.add(p1);
                participantsInThisTimeGroup.add(p2);
                dsu.union(p1, p2);
            }

            // Determine which DSU components now contain a person who has the secret
            // Any person in such a component will also learn the secret.
            Set<Integer> componentsToUpdate = new HashSet<>(); // Stores roots of components that will get the secret
            for (int p : participantsInThisTimeGroup) {
                if (hasSecret[p]) {
                    componentsToUpdate.add(dsu.find(p));
                }
            }

            // Update hasSecret status for participants and identify people whose DSU state needs reset
            List<Integer> peopleToReset = new ArrayList<>(); // People who participated but didn't get secret
            for (int p : participantsInThisTimeGroup) {
                if (componentsToUpdate.contains(dsu.find(p))) {
                    hasSecret[p] = true; // Person 'p' now gets the secret
                } else {
                    // If a person participated but their component didn't get the secret,
                    // it means they did not learn the secret during this batch of meetings.
                    // Their DSU connections formed during this specific time group should be undone.
                    // This prevents them from "carrying forward" connections that didn't lead to a secret
                    // and correctly reflects that they don't have the secret (yet).
                    peopleToReset.add(p);
                }
            }

            // Reset DSU state for people who didn't get the secret in this time group.
            // This effectively breaks their connections made at the current time 'T'.
            for (int p : peopleToReset) {
                dsu.reset(p);
            }
        }

        // Collect all people who have the secret
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (hasSecret[i]) {
                result.add(i);
            }
        }

        return result;
    }
}