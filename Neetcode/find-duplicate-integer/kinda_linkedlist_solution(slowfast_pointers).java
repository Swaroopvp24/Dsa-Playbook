class Solution {
    public int findDuplicate(int[] nums) {
        // Treat the array like a linked list:
        // index = current node, nums[index] = next node.
        //
        // Because there is exactly one duplicate number, two indices
        // will eventually point to the same "next" index, creating a cycle.
        //
        // Example:
        // nums = [1, 3, 4, 2, 2]
        //
        // 0 -> 1 -> 3 -> 2 -> 4 -> 2 ...
        //                 ^         |
        //                 |_________|
        //
        // The duplicate number is the entry point of this cycle.

        int slowPointer = 0;
        int fastPointer = 0;

        // Phase 1: Detect whether a cycle exists.
        //
        // The slow pointer moves one step at a time.
        // The fast pointer moves two steps at a time.
        //
        // If a cycle exists, the fast pointer will eventually
        // catch up with the slow pointer inside the cycle.
        while (true) {
            slowPointer = nums[slowPointer];
            fastPointer = nums[nums[fastPointer]];

            if (slowPointer == fastPointer) {
                break;
            }
        }

        // Phase 2: Find the entrance of the cycle.
        //
        // Reset one pointer to the beginning (index 0).
        // Keep the other pointer at the meeting point.
        //
        // Now move both pointers one step at a time.
        // They will meet exactly at the cycle's entrance,
        // which corresponds to the duplicate number.
        int cycleEntryPointer = 0;

        while (true) {
            slowPointer = nums[slowPointer];
            cycleEntryPointer = nums[cycleEntryPointer];

            if (slowPointer == cycleEntryPointer) {
                return slowPointer;
            }
        }
    }
}
