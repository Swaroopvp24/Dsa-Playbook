# reorder-list

## linkedlist_solution_with_stack.java
*Style: detailed*

# Technical Deep-Dive: Linked List Reordering via Stack

## 1. Summary
The provided solution utilizes a **Linear Data Structure (Stack)** approach to perform an in-place-style reordering of a singly-linked list. The transformation objective is to convert a list $L = [L_0, L_1, \dots, L_{n-1}, L_n]$ into the pattern $[L_0, L_n, L_1, L_{n-1}, \dots]$.

The algorithm leverages a `Deque` (acting as a LIFO stack) to capture the memory references of all nodes in the list. By iteratively popping the tail nodes and splicing them between the current head-traversal nodes, the algorithm reconstructs the list pointers to achieve the desired interleaved order.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Traversal:** We perform one linear pass through the list ($N$ nodes) to populate the `nodeStack`.
*   **Reconstruction:** We perform a second traversal where we process approximately $N/2$ nodes. Each operation inside the loop (popping, pointer reassignment) is $O(1)$.
*   **Total:** $O(N) + O(N/2) = O(N)$.

### Space Complexity: $O(N)$
*   **Auxiliary Storage:** The algorithm maintains a `Deque` of size $N$, storing references to every `ListNode` object in the list. 
*   **Note:** This is significantly less memory-efficient than the optimal pointer-manipulation approach (which uses $O(1)$ space by finding the middle and reversing the second half of the list).

## 3. Component Deep Dive

### The Stack Pattern (`Deque<ListNode>`)
Using `ArrayDeque` is an appropriate choice here due to its lower overhead compared to `java.util.Stack` (which is legacy and synchronized). By pushing all nodes into the stack, we gain reverse-order access to the list elements without needing to perform an explicit list reversal or calculate list length prematurely.

### Termination Logic (The "Middle" Check)
The reordering logic relies on two critical conditions to prevent infinite loops or circular references:
1.  **`currentNode == lastNode`**: This handles cases with an odd number of nodes. When the pointer traversing from the front meets the pointer being pulled from the back, we are at the center. Setting `currentNode.next = null` is essential to prevent a tail-cycle.
2.  **`nextNode == lastNode`**: This handles cases with an even number of nodes. It ensures that the insertion process stops immediately when the remaining nodes are adjacent, preventing the re-insertion of an already-reordered node.

### Pointer Re-weaving
The core mutation logic:
```java
currentNode.next = lastNode;
lastNode.next = nextNode;
currentNode = nextNode;
```
This pattern effectively inserts the tail node into the gap between the current node and its original successor. It is crucial that `nextNode` is stored *before* the reassignment, or the reference to the rest of the list will be lost.

## 4. Key Insights

### Performance Nuance: Memory Overhead
While $O(N)$ is theoretically acceptable, the use of a stack consumes $O(N)$ space in the heap. In a production environment with extremely large lists, this could trigger `OutOfMemoryError` or heavy GC pressure. A more robust approach for large-scale systems would be:
1.  **Find Middle:** Use the "Tortoise and Hare" algorithm.
2.  **Reverse Second Half:** In-place reversal of the list starting from `middle.next`.
3.  **Merge:** Interleave the two halves.
This achieves $O(N)$ time with $O(1)$ space.

### Edge Case Handling
*   **Null Head:** The code handles a `null` head gracefully (the `while` loop won't execute).
*   **Single/Double Nodes:** The `if (currentNode == lastNode)` and `if (nextNode == lastNode)` checks correctly terminate the execution for lists of length 1 or 2, respectively, preventing redundant pointer assignments.

### Potential Bug Vectors
*   **Circular References:** The manual nulling of `next` pointers is the most critical part of this implementation. If the `break` conditions were missing, the list would contain a cycle (e.g., $L_n \to L_1 \to L_n$), which would cause infinite loops in any subsequent traversal.
*   **Pointer Loss:** If a developer were to refactor this code and mistakenly reassign `currentNode` before saving `nextNode`, they would sever the tail of the list and leak memory. The implementation correctly preserves the forward reference before modification.

---
