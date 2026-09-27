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

        // If the list is empty, there is nothing to copy
        if (head == null) {
            return null;
        }

        Node current = head;

        // Step 1: Insert a copied node after every original node
        //
        // Original:  A -> B -> C
        // Becomes:   A -> A' -> B -> B' -> C -> C'
        while (current != null) {
            Node copiedNode = new Node(current.val);

            copiedNode.next = current.next;
            current.next = copiedNode;

            current = copiedNode.next;
        }

        // Step 2: Set the random pointers of copied nodes
        //
        // If originalNode.random points to X,
        // then originalNode.next.random should point to X.next
        // because X.next is the copied version of X.
        current = head;

        while (current != null) {

            if (current.random != null) {
                current.next.random = current.random.next;
            }

            // Move to the next original node
            current = current.next.next;
        }

        // Step 3: Separate the original list and copied list
        Node copiedHead = head.next;
        current = head;

        while (current != null) {
            Node copiedNode = current.next;

            // Restore the original node's next pointer
            current.next = copiedNode.next;

            // Connect the copied node to the next copied node
            if (copiedNode.next != null) {
                copiedNode.next = copiedNode.next.next;
            }

            // Move to the next original node
            current = current.next;
        }

        // Return the head of the copied list
        return copiedHead;
    }
}

