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
    public ListNode mergeTwoLists(ListNode firstList, ListNode secondList) {
        // Dummy node simplifies handling the head of the merged list
        ListNode dummyHead = new ListNode(0);
        ListNode current = dummyHead;

        // Compare nodes from both lists and attach the smaller one
        while (firstList != null && secondList != null) {
            if (firstList.val <= secondList.val) {
                current.next = firstList;
                firstList = firstList.next;
            } else {
                current.next = secondList;
                secondList = secondList.next;
            }

            // Move to the newly added node
            current = current.next;
        }

        // Attach the remaining nodes from whichever list is not empty
        if (firstList != null) {
            current.next = firstList;
        } else {
            current.next = secondList;
        }

        // Skip the dummy node and return the actual merged list
        return dummyHead.next;
    }
}
