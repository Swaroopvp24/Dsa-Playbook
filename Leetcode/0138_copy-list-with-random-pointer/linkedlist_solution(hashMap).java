/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/

class Solution {
    public Node copyRandomList(Node head) {

        // Map each original node to its corresponding copied node
        Map<Node, Node> originalToCopy = new HashMap<>();

        // Step 1: Create a copy of every node
        Node current = head;

        while (current != null) {
            Node copiedNode = new Node(current.val);

            originalToCopy.put(current, copiedNode);

            current = current.next;
        }

        // Step 2: Connect the next and random pointers
        current = head;

        while (current != null) {
            Node copiedNode = originalToCopy.get(current);

            // Connect the copied node's next pointer
            copiedNode.next = originalToCopy.get(current.next);

            // Connect the copied node's random pointer
            copiedNode.random = originalToCopy.get(current.random);

            current = current.next;
        }

        // Return the copy of the head node
        return originalToCopy.get(head);
    }
}
