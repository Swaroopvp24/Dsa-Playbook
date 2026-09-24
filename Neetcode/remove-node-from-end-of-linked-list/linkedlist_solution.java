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
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // First, find the total number of nodes in the linked list.
        ListNode currentNode = head;
        int listSize = 0;

        while (currentNode != null) {
            currentNode = currentNode.next;
            listSize++;
        }

        // If n equals the list size, we need to remove the head node.
        if (n == listSize) {
            return head.next;
        }

        // Move to the node immediately before the node we want to remove.
        ListNode previousNode = head;

        for (int position = 1; position < listSize - n; position++) {
            previousNode = previousNode.next;
        }

        // Skip the target node to remove it from the linked list.
        previousNode.next = previousNode.next.next;

        return head;
    }
}
