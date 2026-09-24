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
        // Dummy node makes it easier to handle removing the head.
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;

        ListNode fastPointer = dummyNode;
        ListNode slowPointer = dummyNode;

        // Move fastPointer n nodes ahead.
        for (int i = 0; i < n; i++) {
            fastPointer = fastPointer.next;
        }

        // Move both pointers until fastPointer reaches the last node.
        // slowPointer will then be immediately before the node to remove.
        while (fastPointer.next != null) {
            fastPointer = fastPointer.next;
            slowPointer = slowPointer.next;
        }

        // Remove the nth node from the end.
        slowPointer.next = slowPointer.next.next;

        return dummyNode.next;
    }
}