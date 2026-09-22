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
    public ListNode reverseList(ListNode head) {
        // 'previous' keeps track of the node that should come
        // before the current node in the reversed list.
        ListNode previous = null;

        // 'current' is the node we are currently processing.
        ListNode current = head;

        while (current != null) {
            // Save the next node before changing current.next.
            // Otherwise, we would lose the rest of the list.
            ListNode nextNode = current.next;

            // Reverse the current node's pointer.
            // Instead of pointing to the next node, it now points
            // to the previous node.
            current.next = previous;

            // Move 'previous' forward to the current node.
            previous = current;

            // Move 'current' forward to the next node we saved.
            current = nextNode;
        }

        // 'previous' is now the new head of the reversed list.
        return previous;
    }
}
