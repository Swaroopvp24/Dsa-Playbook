# reorder-linked-list

## linkedlist_solution_with_stack.java
*Style: detailed*

# Engineering Deep-Dive: Linked List Reordering via Stack-Based Backtracking

## Summary
The provided solution addresses the "Reorder List" problem (rearranging $L_0 \to L_1 \to \dots \to L_{n-1} \to L_n$ into $L_0 \to L_n \to L_1 \to L_{n-1} \dots$) by utilizing a **LIFO (Last-In-First-Out) auxiliary data structure**. 

Instead of an in-place pointer manipulation strategy (which typically involves finding the midpoint, reversing the second half, and merging), this implementation uses an `ArrayDeque` to mirror the list structure, allowing O(1) access to the tail elements. The algorithm iteratively stitches the "last" node from the stack between the "current" node and its successor until the pointers meet in the center.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Traversal 1:** Iterating through the linked list to populate the `nodeStack` takes $O(N)$.
*   **Traversal 2:** The reordering loop processes each node exactly once. Popping from the stack and performing pointer reassignment are $O(1)$ operations.
*   **Total:** $O(N) + O(N) = O(N)$, where $N$ is the number of nodes in the list.

### Space Complexity: $O(N)$
*   The `ArrayDeque` stores a reference to every `ListNode` in the provided sequence.
*   Unlike the optimal $O(1)$ space in-place reversal algorithm, this approach requires linear memory proportional to the input list size.

---

## Component Deep Dive

### 1. `Deque<ListNode> nodeStack`
By pushing all nodes into a `Deque`, the implementation effectively creates an indexable map of the list from back-to-front. `ArrayDeque` is preferred over `Stack` because it is not synchronized and provides better performance in single-threaded environments.

### 2. The Reordering Logic
The loop maintains two logical pointers:
*   `currentNode`: Progresses forward from the head.
*   `lastNode`: Retrieved from the stack, representing the tail-ward progression.

**The "Convergence" Guards:**
*   **`currentNode == lastNode`**: Handles lists with an **odd number of nodes**. When the front pointer meets the back pointer, the center node becomes the new tail of the list. Setting `currentNode.next = null` is critical to prevent a cycle.
*   **`nextNode == lastNode`**: Handles lists with an **even number of nodes**. When the front pointer is immediately adjacent to the back pointer, the tail is already correctly placed.

### 3. Pointer Re-linking (The "Stitching" Phase)
```java
currentNode.next = lastNode;
lastNode.next = nextNode;
currentNode = nextNode;
```
This is a standard insertion maneuver. By holding `nextNode` in a temporary variable, the implementation avoids losing the forward reference while injecting the `lastNode` into the chain.

---

## Key Insights & Performance Nuances

### Cycle Prevention
The most common failure mode in linked list manipulation is the creation of a cycle. This implementation avoids this by explicitly setting the `.next` pointer to `null` in both termination conditions. Without `lastNode.next = null`, the new tail might point back to a node earlier in the list, creating an infinite loop for subsequent traversals.

### Comparison to Optimal $O(1)$ Space
While this implementation is highly readable and logically robust, it is **sub-optimal for memory-constrained environments**. 
*   **Trade-off:** The stack approach uses $O(N)$ space to simplify the logic of finding the tail. 
*   **Alternative:** The standard "Staff Engineer" approach for this problem is:
    1.  Find middle using slow/fast pointers ($O(N)$).
    2.  Reverse the second half in-place ($O(N)$).
    3.  Merge the two lists ($O(N)$).
    *   This alternative maintains $O(N)$ time but reduces space to $O(1)$. 

### Subtle Bug Watch
*   **Null Safety:** The code assumes `head` is not null (which is usually guaranteed by problem constraints), but if the input list is `null`, the stack will remain empty and the reordering loop will be bypassed, resulting in a correct `null` output.
*   **Memory Overhead:** In a high-throughput system, if $N$ is very large (e.g., $10^6$ nodes), the overhead of the `ArrayDeque` (holding $10^6$ references) may trigger GC pressure. If the platform has tight memory limits, consider the in-place pointer reversal technique.

---

## linkedlist_optimal_solution.java
*Style: detailed*

# Engineering Design Document: In-Place Linked List Reordering

## Summary
The `reorderList` solution implements an $O(N)$ space-optimized in-place algorithm to transform a singly linked list $L: 0 \to 1 \to \dots \to n-1 \to n$ into $L: 0 \to n \to 1 \to n-1 \to 2 \to n-2 \to \dots$.

The strategy leverages a three-phase "divide-and-conquer" approach:
1. **Topology Analysis:** Locating the midpoint via the Tortoise and Hare (Floyd’s cycle-finding variant) algorithm.
2. **Structural Reversal:** In-place iterative pointer manipulation of the second sub-list.
3. **Interleaving:** A synchronized merge phase that stitches the two sub-lists by reassigning `next` pointers.

---

## Complexity Analysis

### Time Complexity: $O(N)$
* **Midpoint Search:** Traversing the list with $O(N/2)$ steps.
* **Reversal:** Reversing the second half involves $O(N/2)$ operations.
* **Merge:** Traversing the interleaved result takes $O(N/2)$ operations.
Total time complexity is $T(N) = O(N/2 + N/2 + N/2) \approx O(N)$.

### Space Complexity: $O(1)$
The implementation is strictly in-place. It utilizes a constant number of `ListNode` references (pointers) regardless of input size $N$. No recursion stack or auxiliary data structures (like arrays or deques) are used, making it highly efficient for memory-constrained environments.

---

## Component Deep Dive

### 1. The Tortoise and Hare Midpoint Logic
By setting `fast = head` and `slow = head`, the `fast` pointer hits the tail at the same time the `slow` pointer reaches the midpoint. 
* **Edge Case:** When the list length is odd, `slow` lands on the exact middle element. By setting `slow.next = null`, we effectively truncate the first half and detach the second half for reversal, ensuring the algorithm cleanly handles both odd and even parity lengths.

### 2. Iterative Pointer Reversal
The reversal phase employs a standard three-pointer shift:
* `previous` (trailing pointer), `current` (the node being processed), and `nextNode` (buffer).
* This maintains structural integrity without auxiliary list instantiation. The termination condition `current != null` ensures the entire second segment is flipped, making the tail of the original list the new head of the second segment.

### 3. Interleaving Merge
This phase is the most critical for memory safety. By buffering `firstHalfNext` and `secondHalfNext` *before* rewriting the `next` pointers of `firstHalfCurrent` and `secondHalfCurrent`, the algorithm prevents "dangling node syndrome," where references to the remainder of the list are lost.

---

## Key Insights

* **The "Null" Termination Constraint:** A common bug in linked list manipulation is creating a cycle. Specifically, in the merge step, if the second half is shorter than the first (odd length), the `secondHalfCurrent` loop naturally terminates, leaving the tail of the first half correctly pointing to the final node.
* **Pointer Buffering:** Failure to store `firstHalfNext` and `secondHalfNext` is the most common failure point. Always cache downstream references before overwriting pointer `A.next = B`.
* **Stability:** This algorithm is stable regarding node identity. It does not create new objects; it purely reassigns existing references. This is crucial for garbage collection pressure, as this function can be called on massive lists without increasing heap occupancy.
* **Refinement Opportunities:**
    * The implementation assumes the list is at least 1 node long. For production-grade code, an explicit `if (head == null || head.next == null) return;` guard clause should be added to handle empty or single-node inputs efficiently.

---
