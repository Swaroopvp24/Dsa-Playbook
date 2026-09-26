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

## linkedlist_optimal_solution.java
*Style: detailed*

# Engineering Deep-Dive: In-Place Linked List Reordering

## Summary
The solution employs a **three-phase pointer manipulation strategy** to reorder a singly-linked list into the interleaved pattern $L_0 \rightarrow L_n \rightarrow L_1 \rightarrow L_{n-1} \dots$ in $O(1)$ auxiliary space. By decomposing the list into two halves and reversing the second half, the problem is transformed into a standard two-pointer list merging operation. This approach avoids the $O(n)$ space overhead associated with storing node references in an array or stack.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Middle Finding:** $O(n/2)$ traversal using the slow/fast pointer technique.
*   **Reversal:** $O(n/2)$ traversal to reverse the second half of the list.
*   **Merging:** $O(n/2)$ traversal to interleave the two halves.
*   **Total:** $O(n) + O(n) + O(n) \approx O(n)$. We touch each node a constant number of times.

### Space Complexity: $O(1)$
*   The algorithm operates **in-place**. It uses a constant number of pointer variables (`slow`, `fast`, `previous`, `current`, etc.) regardless of input size $n$. No recursive call stack or auxiliary data structures are utilized.

---

## Component Deep Dive

### 1. The Tortoise and Hare (Middle Finding)
The use of `fast.next != null && fast.next.next != null` as the loop condition ensures that `slow` stops exactly at the end of the first half. 
*   **Even list (e.g., 1-2-3-4):** `slow` lands at index 1 (value 2), `secondHalfHead` starts at index 2 (value 3).
*   **Odd list (e.g., 1-2-3-4-5):** `slow` lands at index 2 (value 3), `secondHalfHead` starts at index 3 (value 4). 
*   **Critical step:** `slow.next = null` is mandatory to decouple the halves; failure to do this results in a cyclic linked list.

### 2. In-Place Reversal
The reversal logic uses three pointers (`previous`, `current`, `nextNode`) to perform a standard iterative inversion. This is a destructive operation that rewrites the `next` pointers of the second sub-list. The sub-list head becomes the tail, and the previous tail becomes the new head, which is crucial for the sequential merging in Step 3.

### 3. Alternating Merge
This stage exploits the fact that the second half is now reversed. By caching the next pointers (`firstHalfNext`, `secondHalfNext`) before mutation, we maintain the integrity of the remaining list structure while weaving the nodes together. 

*   **Handling Parity:** Because we split the list, the second half will be equal to or shorter than the first half by exactly one node (in odd-length lists). The merge condition `secondHalfCurrent != null` handles this naturally, leaving the tail of the first half correctly linked.

---

## Key Insights

### Tricky Edge Cases
*   **Single-Node/Empty List:** If `head` is null or `head.next` is null, the middle-finding logic and the loop conditions gracefully skip processing. No modifications are made, satisfying the constraint.
*   **Two-Node List:** The middle finding logic results in `slow` at node 1, `secondHalfHead` at node 2. Reversal and merge proceed correctly, resulting in no change (which is correct for $n \le 2$).

### Performance Nuance: Pointer Stability
The primary source of bugs in this pattern is **dangling pointers**. 
*   **Optimization:** In the merge step, notice that we only need to store `firstHalfNext` and `secondHalfNext` *inside* the loop. If you store these before entering the loop, you risk logic errors when accessing null pointers.
*   **Memory Visibility:** Because we are modifying the `next` references, ensure that the intermediate pointer state does not violate the memory model if this were a multithreaded environment (though here it is confined to a single thread).

### Subtle Bug Traps
*   **The "Slow" Pointer Position:** If you accidentally move `slow` one step too far, the merge logic will experience an off-by-one error, potentially causing an infinite cycle. Always verify that `slow.next = null` cleanly terminates the first half.
*   **Destructive Read:** This algorithm is destructive. If the input list nodes are needed in their original order elsewhere in the application, a deep copy must be performed, increasing space complexity to $O(n)$.

---
