/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

class Solution {
    public boolean hasCycle(ListNode head) {
        // Two pointers: slow moves one step, fast moves two steps.
        ListNode slowPointer = head;
        ListNode fastPointer = head;

        // Continue while the fast pointer can move forward.
        while (fastPointer != null && fastPointer.next != null) {
            slowPointer = slowPointer.next;
            fastPointer = fastPointer.next.next;

            // If both pointers meet, a cycle exists.
            if (slowPointer == fastPointer) {
                return true;
            }
        }

        // Fast pointer reached the end, so there is no cycle.
        return false;
    }
}
