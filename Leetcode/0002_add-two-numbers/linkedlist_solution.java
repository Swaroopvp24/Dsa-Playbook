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
    public ListNode addTwoNumbers(ListNode firstList, ListNode secondList) {

        // Dummy node simplifies creating and returning the result list.
        ListNode dummyHead = new ListNode(0);
        ListNode currentNode = dummyHead;

        // Stores the carry from adding two digits.
        int carry = 0;

        // Continue while either list has nodes or there is a remaining carry.
        while (firstList != null || secondList != null || carry != 0) {

            // Get the current digit from each list.
            // Use 0 when one list has already reached its end.
            int firstDigit = (firstList != null) ? firstList.val : 0;
            int secondDigit = (secondList != null) ? secondList.val : 0;

            // Add both digits along with the carry from the previous position.
            int sum = firstDigit + secondDigit + carry;

            // The current digit is the remainder after dividing by 10.
            int resultDigit = sum % 10;

            // The quotient becomes the carry for the next position.
            carry = sum / 10;

            // Add the calculated digit to the result list.
            currentNode.next = new ListNode(resultDigit);
            currentNode = currentNode.next;

            // Move to the next node if the first list still has elements.
            if (firstList != null) {
                firstList = firstList.next;
            }

            // Move to the next node if the second list still has elements.
            if (secondList != null) {
                secondList = secondList.next;
            }
        }

        // Skip the dummy node and return the actual result.
        return dummyHead.next;
    }
}
