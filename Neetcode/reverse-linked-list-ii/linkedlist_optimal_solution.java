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
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // No reversal needed for an empty list or a single-node range.
        if (head == null || left == right) {
            return head;
        }

        // Dummy node helps handle the case where left == 1.
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // Move to the node immediately before the reversal range.
        ListNode beforeReversal = dummy;

        for (int position = 0; position < left - 1; position++) {
            beforeReversal = beforeReversal.next;
        }

        // 'start' remains the first node of the reversal range.
        ListNode start = beforeReversal.next;

        // 'nodeToMove' is the next node that we will move
        // to the front of the reversal range.
        ListNode nodeToMove = start.next;

        /*
         * Move each node after 'start' to immediately after
         * 'beforeReversal'.
         *
         * Example:
         * 1 -> 2 -> 3 -> 4 -> 5
         *      ^         ^
         *    left      right
         *
         * First iteration:
         * 1 -> 3 -> 2 -> 4 -> 5
         *
         * Second iteration:
         * 1 -> 4 -> 3 -> 2 -> 5
         */
        for (int i = 0; i < right - left; i++) {

            // Remove nodeToMove from its current position.
            start.next = nodeToMove.next;

            // Insert nodeToMove immediately after beforeReversal.
            nodeToMove.next = beforeReversal.next;
            beforeReversal.next = nodeToMove;

            // Move to the next node that needs to be brought forward.
            nodeToMove = start.next;
        }

        return dummy.next;
    }
}
