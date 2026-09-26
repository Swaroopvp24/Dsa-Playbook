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

    public void reorderList(ListNode head) {

        // Step 1: Find the middle of the linked list.
        // 'slow' moves one step at a time,
        // while 'fast' moves two steps at a time.
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Split the list into two halves.
        // 'slow' is at the end of the first half.
        ListNode secondHalfHead = slow.next;
        slow.next = null;

        // Step 2: Reverse the second half of the list.
        ListNode previous = null;
        ListNode current = secondHalfHead;

        while (current != null) {
            // Save the next node before changing the pointer.
            ListNode nextNode = current.next;

            // Reverse the current node's pointer.
            current.next = previous;

            // Move both pointers forward.
            previous = current;
            current = nextNode;
        }

        // 'previous' is now the head of the reversed second half.
        ListNode firstHalfCurrent = head;
        ListNode secondHalfCurrent = previous;

        // Step 3: Merge the two halves alternately.
        //
        // First half:  1 -> 2 -> 3
        // Second half: 6 -> 5 -> 4
        //
        // Result:       1 -> 6 -> 2 -> 5 -> 3 -> 4
        while (secondHalfCurrent != null) {

            // Save the next nodes before modifying any links.
            ListNode firstHalfNext = firstHalfCurrent.next;
            ListNode secondHalfNext = secondHalfCurrent.next;

            // Insert a node from the second half
            // between two nodes of the first half.
            firstHalfCurrent.next = secondHalfCurrent;
            secondHalfCurrent.next = firstHalfNext;

            // Move to the next nodes in both halves.
            firstHalfCurrent = firstHalfNext;
            secondHalfCurrent = secondHalfNext;
        }
    }
}
