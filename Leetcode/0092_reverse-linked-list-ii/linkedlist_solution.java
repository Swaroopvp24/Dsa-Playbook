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

        // Dummy node makes it easier to handle cases where left == 1.
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode current = dummy;

        ListNode leftPrev = null;  // Node immediately before the reversal range
        ListNode leftNode = null;  // First node of the reversal range
        ListNode rightNode = null; // Last node of the reversal range

        /*
         * Find the three important nodes:
         *
         * leftPrev -> leftNode -> ... -> rightNode -> nodeAfter
         */
        for (int position = 0; position <= right; position++) {
            if (position == left - 1) {
                leftPrev = current;
            }

            if (position == left) {
                leftNode = current;
            }

            if (position == right) {
                rightNode = current;
            }

            current = current.next;
        }

        // Node immediately after the portion that needs to be reversed.
        ListNode nodeAfter = rightNode.next;

        /*
         * Reverse the sublist from leftNode to rightNode.
         *
         * We start prev at nodeAfter so that, after reversal,
         * rightNode points to nodeAfter automatically.
         */
        ListNode previous = nodeAfter;
        ListNode currentNode = leftNode;

        while (currentNode != nodeAfter) {
            ListNode nextNode = currentNode.next;

            currentNode.next = previous;
            previous = currentNode;
            currentNode = nextNode;
        }

        // Connect the node before the reversed portion to its new first node.
        leftPrev.next = rightNode;

        return dummy.next;
    }
}
