# merge-two-sorted-lists

## linkedlist_solution.java
*Style: detailed*

# Technical Reference: Iterative Merge of Two Sorted Linked Lists

## Summary
The solution implements a **two-pointer iterative merging algorithm** to combine two monotonically increasing singly-linked lists into a single sorted list. The approach relies on an auxiliary **dummy head node** to abstract the boundary condition of the result list's head, effectively turning the insertion of the first node into a generic tail-append operation.

## Complexity Analysis

### Time Complexity: $O(N + M)$
*   **Derivation:** The algorithm performs a single linear pass through both input lists. Each iteration of the `while` loop processes exactly one node from either `firstList` or `secondList`. 
*   **Termination:** The loop terminates when either list is exhausted ($min(N, M)$ iterations). The final `if` block executes exactly one pointer assignment, which is $O(1)$. Thus, the total work is proportional to the sum of the lengths of the two lists.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm performs an **in-place merge**. It rearranges the existing `next` pointers of the provided `ListNode` objects rather than allocating new nodes. 
*   **Auxiliary Space:** Aside from the `dummyHead` pointer and a few reference variables (`current`), no extra data structures are created. Note: The `dummyHead` itself consumes constant $O(1)$ space, satisfying the requirements for constant space complexity.

---

## Component Deep Dive

### 1. The Dummy Node Pattern
The `dummyHead` (`ListNode(0)`) acts as a sentinel. This is a critical design pattern in linked list manipulation because it eliminates the need for conditional logic (e.g., `if (head == null)`) inside the loop to determine whether to set the head of the merged list or append to an existing tail. 
*   **Result:** Clean code that treats all nodes uniformly, reducing the likelihood of `NullPointerException` during the initial attachment.

### 2. Pointer Manipulation (The `current` Pointer)
The `current` reference acts as a trailing pointer that tracks the tail of the newly formed list. 
*   **Operation:** In each iteration, `current.next` is re-assigned to the smaller of the two head nodes. This is an $O(1)$ operation that effectively "steals" the node from its original chain and links it into the new structure.
*   **State Update:** The pointer `firstList` or `secondList` is incremented *before* the `current` pointer is moved forward to ensure the continuity of the original list isn't lost until it is safely referenced by `current.next`.

### 3. Exhaustion Handling
Once one list is exhausted, the loop terminates. Because the input lists are guaranteed to be sorted, the remaining portion of the non-empty list is already sorted and satisfies the condition to be greater than (or equal to) the last node appended to the result. We perform a single assignment `current.next = remaining` instead of iterating, which is an optimization that keeps the post-loop logic $O(1)$.

---

## Key Insights

*   **Stability:** This algorithm is **stable**. By using `<=` in the condition `if (firstList.val <= secondList.val)`, the algorithm preserves the relative order of nodes with identical values from the `firstList`. If the requirement were to swap based on arrival, simply changing to `<` would alter the stability.
*   **Memory Efficiency:** Since this is an in-place merge, it is highly memory-efficient. However, it assumes the caller is prepared to have the original input lists modified. If the original lists must remain intact, a deep copy approach (increasing space to $O(N+M)$) would be required.
*   **Edge Case Resilience:**
    *   **Empty Lists:** If either list is `null` at the start, the `while` loop is skipped entirely, and the `if` block correctly attaches the non-null list (or `null` if both are empty).
    *   **Single-node Lists:** The logic handles these naturally, as the `while` loop completes after one iteration, and the remaining list is attached correctly.
*   **Garbage Collection Note:** Because this solution creates only one auxiliary `ListNode` (`dummyHead`) and discards it after returning, the memory pressure on the JVM heap is negligible, making this suitable for high-frequency, low-latency execution contexts.

---
