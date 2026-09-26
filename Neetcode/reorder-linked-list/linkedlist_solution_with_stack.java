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
        // Store all nodes in a stack so we can access
        // the nodes from the end of the list.
        Deque<ListNode> nodeStack = new ArrayDeque<>();

        ListNode current = head;

        while (current != null) {
            nodeStack.push(current);
            current = current.next;
        }

        // Start reordering from the beginning of the list.
        ListNode currentNode = head;

        while (currentNode != null && !nodeStack.isEmpty()) {
            // Get the last remaining node.
            ListNode lastNode = nodeStack.pop();

            // If both pointers refer to the same node,
            // we have reached the middle of the list.
            if (currentNode == lastNode) {
                currentNode.next = null;
                break;
            }

            ListNode nextNode = currentNode.next;

            // If the last node is already the next node,
            // we have reached the end of the reordering.
            if (nextNode == lastNode) {
                lastNode.next = null;
                break;
            }

            // Insert the last node after the current node:
            //
            // currentNode -> nextNode
            // becomes
            // currentNode -> lastNode -> nextNode
            currentNode.next = lastNode;
            lastNode.next = nextNode;

            // Move forward to the next original node.
            currentNode = nextNode;
        }
    }
}
