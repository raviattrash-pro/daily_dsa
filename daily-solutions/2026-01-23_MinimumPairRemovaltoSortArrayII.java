The problem asks for the minimum number of operations to make an array non-decreasing. An operation consists of selecting the adjacent pair with the minimum sum (leftmost if multiple exist) and replacing it with their sum. The number of operations is `N - L`, where `N` is the initial array length and `L` is the final array length. We need to find the earliest point (minimum operations) at which the array becomes non-decreasing, following the specific merge rules.

This problem can be solved by simulating the process using a combination of a doubly linked list and a priority queue (implemented with `TreeSet` for efficient updates).

Here's the breakdown of the approach:

1.  **Doubly Linked List for Array Elements**: The array `nums` is dynamic, with elements merging and reducing its length. A doubly linked list is suitable for representing the current state of the array because it allows for O(1) removal/merging of adjacent elements and O(1) updates to their neighbors. Each node in the list will store its `val`, references to its `prev` and `next` nodes, and its `originalLeftIdx`. The `originalLeftIdx` is crucial for tie-breaking when multiple pairs have the same minimum sum (as per problem rules, choose the leftmost).

2.  **Priority Queue for Minimum Sum Pairs**: We need to efficiently find the adjacent pair with the minimum sum. A `PriorityQueue` or `TreeSet` (which acts as an ordered set and can retrieve the smallest element) can be used. A custom `PairData` class will store the `sum` of the pair, the `originalLeftIdx` of its left element (for tie-breaking), and references to the `leftNode` and `rightNode` of the pair. The `PairData` will implement `Comparable` to sort by `sum` then `originalLeftIdx`.

3.  **Handling Dynamic Sums and Stale Entries**: The main challenge is that when a node's value changes due to a merge, any `PairData` objects in the priority queue involving that node will become stale (their stored `sum` will be incorrect). A standard `PriorityQueue` doesn't support efficient updates or deletions of arbitrary elements. A `TreeSet` is preferred because it allows `remove(Object)` in O(log K) time (where K is set size).
    To manage stale entries and updates:
    *   Each `Node` will store references to the `PairData` objects it is part of: `leftPair` (for `(prev, this)`) and `rightPair` (for `(this, next)`).
    *   When a pair `(left, right)` is chosen for merging:
        *   We first check if `currentPair` from `TreeSet` is still valid (i.e., `left.rightPair == currentPair` and `right.leftPair == currentPair`). This ensures that the `currentPair` has not been invalidated by other merges.
        *   Before merging, we remove the `PairData` objects involving `left.prev`, `left`, `right`, and `right.next` from the `TreeSet` (if they exist). The `currentPair` (which is `left.rightPair`) is already removed by `pollFirst()`.
        *   After merging `left` and `right` (updating `left.val` and adjusting linked list pointers), new `PairData` objects are created for `(left.prev, left)` and `(left, left.next)` (if these neighbors exist) and added to the `TreeSet`. The `left.leftPair` and `left.rightPair` references are updated to point to these new `PairData` objects.

4.  **Tracking Non-Decreasing State**: To determine when the array becomes non-decreasing, we maintain a `violationsCount`. This counter tracks the number of adjacent pairs `(A, B)` where `A > B`.
    *   Initialize `violationsCount` by iterating through the initial array.
    *   When `(left, right)` are merged into `left`:
        *   Decrement `violationsCount` if `(left.prev, left)`, `(left, right)`, or `(right, right.next)` were violations *before* the merge.
        *   Increment `violationsCount` if `(left.prev, left)` or `(left, left.next)` become violations *after* the merge (with `left` now having its new sum).
    *   If `violationsCount` becomes 0 at any point, the array is sorted, and we return the current `operations` count.

**Algorithm Steps:**

1.  **Initialization**:
    *   Create `N` `Node` objects, linking them into a doubly linked list. Each node `i` gets `nums[i]` and `originalLeftIdx = i`.
    *   Initialize `TreeSet<PairData> pqSet`.
    *   Initialize `violationsCount = 0`. For each `i` from `0` to `N-2`:
        *   Create `PairData` for `(nodes[i], nodes[i+1])`, storing its references in `nodes[i].rightPair` and `nodes[i+1].leftPair`. Add it to `pqSet`.
        *   If `nodes[i].val > nodes[i+1].val`, increment `violationsCount`.
    *   Initialize `operations = 0`.

2.  **Simulation Loop**: `while (!pqSet.isEmpty())`
    *   If `violationsCount == 0`, return `operations`.
    *   `PairData currentPair = pqSet.pollFirst()` (gets min sum pair).
    *   `Node left = currentPair.leftNode`, `Node right = currentPair.rightNode`.
    *   **Validity Check**: If `left.rightPair != currentPair || right.leftPair != currentPair`, this `currentPair` is stale or invalid; `continue` to next iteration.
    *   `operations++`.
    *   **Update `violationsCount` (Decrement for old pairs)**:
        *   If `left.prev != null` and `left.prev.val > left.val`, decrement.
        *   If `left.val > right.val`, decrement (this is the pair being merged).
        *   If `right.next != null` and `right.val > right.next.val`, decrement.
    *   **Remove Old Pairs from `pqSet`**:
        *   If `left.leftPair != null`, `pqSet.remove(left.leftPair)`.
        *   If `right.rightPair != null`, `pqSet.remove(right.rightPair)`.
    *   **Perform Merge**:
        *   `left.val += right.val`.
        *   Update linked list: `left.next = right.next`. If `right.next != null`, `right.next.prev = left`.
        *   Clear `right` node's pair references. Clear `left.rightPair`.
    *   **Update `violationsCount` (Increment for new pairs)**:
        *   If `left.prev != null` and `left.prev.val > left.val`, increment.
        *   If `left.next != null` and `left.val > left.next.val`, increment.
    *   **Add New Pairs to `pqSet`**:
        *   If `left.prev != null`, create a new `PairData` for `(left.prev, left)`, add to `pqSet`, and update `left.prev.rightPair` and `left.leftPair`.
        *   Else, `left.leftPair = null`.
        *   If `left.next != null`, create a new `PairData` for `(left, left.next)`, add to `pqSet`, and update `left.rightPair` and `left.next.leftPair`.
        *   Else, `left.rightPair = null`.
3.  If the loop completes (all elements merged into one), return `operations`.

**Time Complexity**: O(N log N)
*   Initialization: O(N) for creating nodes, O(N log N) for adding N-1 initial pairs to `TreeSet`.
*   The loop runs at most N-1 times (one for each merge operation).
*   Inside the loop:
    *   `pqSet.pollFirst()` (equivalent to `TreeSet.pollFirst()` which is `TreeSet.first()` + `TreeSet.remove(element)`) takes O(log N).
    *   Updates to `violationsCount` are O(1).
    *   Removing up to two old `PairData` objects from `pqSet` (for `left.leftPair` and `right.rightPair`) takes O(log N) each.
    *   Adding up to two new `PairData` objects to `pqSet` takes O(log N) each.
    *   Linked list manipulations are O(1).
*   Total time complexity: O(N log N).

**Space Complexity**: O(N)
*   `Node` array: O(N) to store `N` Node objects.
*   `TreeSet`: Stores up to N-1 `PairData` objects. Each `PairData` stores references. This is O(N).
*   Total space complexity: O(N).


import java.util.TreeSet;

class Solution {

    // Node class to represent elements in the doubly linked list
    static class Node {
        long val;
        Node prev;
        Node next;
        int originalLeftIdx; // Original index of the leftmost element contributing to this sum
                             // Used for tie-breaking and for identifying nodes

        // References to PairData objects involving this node
        // These are stored in the TreeSet.
        // leftPair: PairData for (this.prev, this)
        // rightPair: PairData for (this, this.next)
        PairData leftPair;
        PairData rightPair;

        Node(long val, int originalLeftIdx) {
            this.val = val;
            this.originalLeftIdx = originalLeftIdx;
        }
    }

    // PairData class to store information about adjacent pairs
    static class PairData implements Comparable<PairData> {
        long sum;
        int originalLeftIdx; // originalLeftIdx of the left node in the pair
        Node leftNode;
        Node rightNode;

        PairData(long sum, int originalLeftIdx, Node leftNode, Node rightNode) {
            this.sum = sum;
            this.originalLeftIdx = originalLeftIdx;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        @Override
        public int compareTo(PairData other) {
            if (this.sum != other.sum) {
                return Long.compare(this.sum, other.sum);
            }
            return Integer.compare(this.originalLeftIdx, other.originalLeftIdx);
        }
    }

    public int minimumOperations(int[] nums) {
        int n = nums.length;

        if (n <= 1) {
            return 0;
        }

        // 1. Initialize the doubly linked list of Node objects
        Node[] nodesArr = new Node[n];
        for (int i = 0; i < n; ++i) {
            nodesArr[i] = new Node(nums[i], i);
        }
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                nodesArr[i].prev = nodesArr[i - 1];
            }
            if (i < n - 1) {
                nodesArr[i].next = nodesArr[i + 1];
            }
        }

        // 2. Initialize TreeSet as a custom priority queue for PairData objects
        // TreeSet allows efficient retrieval of min element (first()) and removal (remove())
        TreeSet<PairData> pqSet = new TreeSet<>();

        // 3. Initialize violationsCount and populate pqSet
        int violationsCount = 0;
        for (int i = 0; i < n - 1; ++i) {
            Node left = nodesArr[i];
            Node right = nodesArr[i + 1];

            // Create PairData for (left, right)
            PairData pd = new PairData(left.val + right.val, left.originalLeftIdx, left, right);
            pqSet.add(pd);

            // Update node references to this PairData
            left.rightPair = pd;
            right.leftPair = pd;

            // Check for initial violations
            if (left.val > right.val) {
                violationsCount++;
            }
        }

        int operations = 0;

        // 4. Main loop: perform merges until the array is non-decreasing
        while (!pqSet.isEmpty()) {
            if (violationsCount == 0) {
                return operations; // Array is sorted, we are done.
            }

            PairData currentPair = pqSet.pollFirst(); // Get the pair with the minimum sum

            Node left = currentPair.leftNode;
            Node right = currentPair.rightNode;

            // Validity check: ensures the pair is still valid (not stale)
            // A pair is stale if `left.rightPair` or `right.leftPair` no longer point to `currentPair`
            // This happens if `left` or `right` were involved in a more recent merge operation,
            // resulting in a new PairData object replacing this one in the TreeSet.
            if (left.rightPair != currentPair || right.leftPair != currentPair) {
                 continue; // This pair is stale or invalid, skip.
            }

            operations++;

            // Update violationsCount for the removal of (left, right) and its neighbors
            // Decrement for potentially removed violations
            if (left.prev != null && left.prev.val > left.val) {
                violationsCount--;
            }
            if (left.val > right.val) { // This pair (left, right) itself
                violationsCount--;
            }
            if (right.next != null && right.val > right.next.val) {
                violationsCount--;
            }

            // Remove relevant PairData entries from pqSet
            // currentPair (left.rightPair) is already removed via pollFirst()
            if (left.leftPair != null) {
                pqSet.remove(left.leftPair);
            }
            if (right.rightPair != null) {
                pqSet.remove(right.rightPair);
            }
            
            // Perform merge: Update left node's value and link structure
            left.val += right.val;
            
            // Update linked list connections
            left.next = right.next; // left now points to what right used to point to
            if (right.next != null) {
                right.next.prev = left; // The new right neighbor points back to left
            }

            // Invalidate the removed node's pair references
            right.leftPair = null;
            right.rightPair = null;

            // Update violationsCount for the new (left.prev, left) and (left, left.next)
            // Increment for potentially new violations
            if (left.prev != null && left.prev.val > left.val) {
                violationsCount++;
            }
            if (left.next != null && left.val > left.next.val) {
                violationsCount++;
            }

            // Add new or updated PairData entries to pqSet
            // New pair involving (left.prev, left)
            if (left.prev != null) {
                Node p = left.prev;
                // A new PairData object for (p, left)
                PairData newPd = new PairData(p.val + left.val, p.originalLeftIdx, p, left);
                pqSet.add(newPd);
                p.rightPair = newPd;
                left.leftPair = newPd;
            } else {
                left.leftPair = null; // No left neighbor, so no leftPair
            }

            // New pair involving (left, left.next)
            if (left.next != null) {
                Node nxt = left.next;
                // A new PairData object for (left, nxt)
                PairData newPd = new PairData(left.val + nxt.val, left.originalLeftIdx, left, nxt);
                pqSet.add(newPd);
                left.rightPair = newPd;
                nxt.leftPair = newPd;
            } else {
                left.rightPair = null; // No right neighbor, so no rightPair
            }
        }
        
        // If the loop finishes, it means all elements have been merged into one, which is non-decreasing.
        return operations;
    }
}
// Time Complexity: O(N log N)
// Space Complexity: O(N)