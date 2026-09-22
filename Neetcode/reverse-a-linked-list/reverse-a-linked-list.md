# reverse-a-linked-list

## linkedlist_solution.java
*Style: detailed*

# Technical Deep Dive: Iterative Singly Linked List Reversal

This document outlines the mechanics, performance characteristics, and implementation nuances of the iterative approach to reversing a singly linked list.

---

### 1. Summary
The provided solution employs an **iterative pointer-manipulation technique**. By utilizing three pointers (`previous`, `current`, and `nextNode`) to manage the state of the list during traversal, the algorithm reverses the direction of the `next` reference for each node in a single pass. This avoids the $O(n)$ stack depth associated with recursive approaches, maintaining a constant memory footprint regardless of list size.

---

### 2. Complexity Analysis

*   **Time Complexity:** $O(n)$
    *   The algorithm performs exactly one pass over the list of $n$ nodes. Each node is visited once, and only constant-time operations (pointer reassignment, variable initialization) are performed per node.
*   **Space Complexity:** $O(1)$
    *   The algorithm maintains only three `ListNode` references (`previous`, `current`, `nextNode`). As the allocation of these references does not scale with the size of the input list, the auxiliary space is constant.

---

### 3. Component Deep Dive

#### Pointer Logic
The algorithm relies on a "sliding window" of references to maintain list integrity:
1.  **`nextNode` (The Safety Buffer):** Before decoupling the `current` node from its successor, `nextNode` stores the pointer to the remainder of the list. This prevents a memory leak or loss of access to subsequent nodes.
2.  **`current.next = previous` (The Mutation):** This is the core operation. It flips the link direction. By setting the reference to `previous`, we effectively append the current node to the already-reversed segment.
3.  **Pointer Propagation:** `previous` is promoted to `current`, and `current` is promoted to `nextNode`. This shifts the entire operational window forward by one node.

#### Edge-Case Handling
*   **Empty List (`head == null`):** The `current` reference initializes to `null`. The `while` loop condition `current != null` is immediately false, returning `previous` (which is `null`). This correctly handles empty lists.
*   **Single Node List:** The loop executes once. `nextNode` becomes `null`, the single node points to `null` (its new `previous`), and the loop terminates. The function returns the single node as the new head, which is correct.

---

### 4. Key Insights & Engineering Nuances

*   **Memory Safety:** The critical point of failure in this algorithm is the loss of the "rest of the list" reference. If `current.next` is overwritten before saving the reference to the next node in the original sequence, the list is effectively truncated. The explicit use of `nextNode` as a temporary stack variable is the standard mitigation pattern.
*   **Pointer Stability:** Unlike recursive reversal, which builds a call stack and performs the reversal during the *unwinding* phase, this iterative approach mutates the structure *in-place* as it traverses. This is strictly superior in terms of memory overhead as it avoids the $O(n)$ stack frame allocation of a recursive solution.
*   **Performance Bottleneck:** The primary constraint in this solution is memory latency. Because linked list nodes are often scattered in the heap (unlike arrays), the traversal pattern incurs high cache-miss frequency. While the time complexity is $O(n)$, the *constant factor* associated with pointer chasing and memory access is significantly higher than that of an array-based iteration.
*   **Garbage Collection Note:** Because we are merely reassigning references and not creating new nodes, this algorithm is extremely garbage-collector friendly, producing zero transient objects during execution.

---
