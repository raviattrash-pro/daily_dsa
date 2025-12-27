import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;

class Solution {

    // Custom class to represent a busy room in the priority queue.
    // It stores the time when the room becomes free (finishTime) and its ID (roomId).
    // This allows the PriorityQueue to sort based on finishTime first,
    // and then by roomId for tie-breaking (Rule 1: lowest room number).
    static class BusyRoom implements Comparable<BusyRoom> {
        long freeTime; // The time when this room becomes available
        int roomId;    // The ID of the room

        BusyRoom(long freeTime, int roomId) {
            this.freeTime = freeTime;
            this.roomId = roomId;
        }

        @Override
        public int compareTo(BusyRoom other) {
            // Prioritize by earlier freeTime
            if (this.freeTime != other.freeTime) {
                return Long.compare(this.freeTime, other.freeTime);
            }
            // For ties in freeTime, prioritize by lower roomId
            return Integer.compare(this.roomId, other.roomId);
        }
    }

    public int mostBooked(int n, int[][] meetings) {
        // Sort meetings by their original start time (Rule 3 implies this priority)
        // Time Complexity: O(M log M) where M is meetings.length
        Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));

        // meetingsCount[i] stores the total number of meetings room i has hosted.
        // Space Complexity: O(N)
        int[] meetingsCount = new int[n];

        // availableRooms: A min-priority queue to store indices of rooms that are currently free.
        // Rooms are prioritized by their ID (lowest ID first) to satisfy Rule 1.
        // Space Complexity: O(N)
        PriorityQueue<Integer> availableRooms = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            availableRooms.offer(i);
        }

        // busyRooms: A min-priority queue to store rooms that are currently busy.
        // Each element is a BusyRoom object (freeTime, roomId).
        // Rooms are prioritized by their freeTime (earliest first),
        // and then by roomId (lowest ID first) for tie-breaking.
        // Space Complexity: O(N)
        PriorityQueue<BusyRoom> busyRooms = new PriorityQueue<>();

        // Iterate through the sorted meetings
        // Time Complexity: O(M) for the loop itself, plus PQ operations
        for (int[] meeting : meetings) {
            int currentStart = meeting[0];
            int currentEnd = meeting[1];
            long duration = currentEnd - currentStart; // Duration of the current meeting

            // Step 1: Free up rooms that finished their meetings by 'currentStart'.
            // Move these rooms from 'busyRooms' to 'availableRooms'.
            // Time Complexity: In total, over all meetings, each room is added/removed from busyRooms
            // and availableRooms at most once per meeting it hosts. Max M meetings total,
            // so total PQ operations across all meetings is O(M log N).
            while (!busyRooms.isEmpty() && busyRooms.peek().freeTime <= currentStart) {
                BusyRoom finishedRoom = busyRooms.poll();
                availableRooms.offer(finishedRoom.roomId);
            }

            // Step 2: Assign the current meeting to a room.
            if (!availableRooms.isEmpty()) {
                // Scenario A: There are rooms available right now (at or before currentStart).
                // Rule 1: Use the unused room with the lowest number.
                int roomId = availableRooms.poll(); // Get lowest ID available room
                meetingsCount[roomId]++; // Increment meeting count for this room
                long newFreeTime = currentStart + duration; // Meeting starts at currentStart
                busyRooms.offer(new BusyRoom(newFreeTime, roomId)); // Mark room as busy
            } else {
                // Scenario B: No rooms are available right now. The meeting must be delayed.
                // Rule 2: Delayed until a room becomes free. Same duration.
                // Rule 3: This implicitly handles Rule 3 because meetings are processed in
                // order of original start times. The current meeting is the "oldest" unassigned one.
                
                // Get the room that will become free earliest (and has the lowest ID for ties).
                BusyRoom earliestFreeRoom = busyRooms.poll();
                int roomId = earliestFreeRoom.roomId;
                long roomBecomesFreeAt = earliestFreeRoom.freeTime; // The delayed start time

                meetingsCount[roomId]++; // Increment meeting count
                long newFreeTime = roomBecomesFreeAt + duration; // Meeting starts when room frees up
                busyRooms.offer(new BusyRoom(newFreeTime, roomId)); // Mark room as busy again
            }
        }

        // Step 3: Find the room that held the most meetings.
        // If there are multiple rooms, return the one with the lowest number.
        // Time Complexity: O(N)
        int maxMeetings = -1;
        int resultRoomId = -1;

        for (int i = 0; i < n; i++) {
            if (meetingsCount[i] > maxMeetings) {
                maxMeetings = meetingsCount[i];
                resultRoomId = i;
            }
        }

        return resultRoomId;
    }
}

// Time Complexity: O(M log M + M log N)
//   - O(M log M) for sorting the meetings array.
//   - O(M log N) for iterating through meetings and performing PriorityQueue operations.
//     Each meeting involves a constant number of PQ offers/polls, each taking O(log N) time.
//     The 'while' loop for freeing rooms runs a total of O(M) times across all meetings,
//     as each room transition from busy to available (and back) corresponds to a meeting.
//   - O(N) for finding the room with the most meetings.
//   Overall, given M can be much larger than N, it's dominated by O(M log M).

// Space Complexity: O(N + M)
//   - O(N) for meetingsCount, availableRooms, and busyRooms.
//   - O(M) for storing the sorted copy of the meetings array (depends on Arrays.sort implementation,
//     can be O(log M) or O(M)). In the worst case, if the input array is copied for sorting.