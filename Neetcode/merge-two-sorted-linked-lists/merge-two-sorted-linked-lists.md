# merge-two-sorted-linked-lists

## linkedlist_solution.java
*Style: detailed*

# Technical Deep Dive: Two-Pointer Iterative Linked List Merge

## Summary
The provided solution implements a classic **in-place merging algorithm** for two sorted singly-linked lists. It leverages a **dummy node (sentinel)** pattern to eliminate conditional logic when initializing the head of the resultant list. The algorithm employs a two-pointer approach, greedily selecting the smaller head element between the two input lists and performing pointer mutation to construct the merged sequence. By manipulating existing `next` references rather than creating new `ListNode` objects, the algorithm achieves optimal space efficiency.

---

## Complexity Analysis

### Time Complexity: $O(N + M)$
*   **Reasoning:** The algorithm performs a single traversal of both linked lists. Let $N$ be the number of nodes in `firstList` and $M$ be the number of nodes in `secondList`. In the worst case (e.g., interleaved values), the `while` loop executes exactly $N + M$ times. Each iteration performs a constant-time comparison and pointer reassignment, resulting in linear complexity relative to the total number of nodes.

### Space Complexity: $O(1)$
*   **Reasoning:** The solution operates in **constant auxiliary space**. It only allocates a single `dummyHead` reference and a `current` pointer. No new `ListNode` objects are created; the existing nodes are rearranged in-place by updating their `next` pointers. The stack space used is also constant, as the approach is iterative rather than recursive.

---

## Component Deep Dive

### 1. The Dummy Head Pattern
The `ListNode dummyHead = new ListNode(0)` acts as an anchor. Without this, the logic would require a conditional check inside the loop to initialize the `head` of the merged list, followed by an `if` statement to track the tail. By using a sentinel, we treat the first node identically to all subsequent nodes, reducing branch misprediction and simplifying the code path.

### 2. Pointer Mutation Logic
```java
current.next = firstList;
firstList = firstList.next;
```
This is the core of the algorithm. We are not copying data; we are re-linking existing nodes. The `current` pointer tracks the tail of the newly constructed list. When `current.next` is updated to point to `firstList`, it effectively stitches the node into the resulting chain. Advancing `firstList` ensures we continue traversing the source list.

### 3. Residual List Handling (The "Cleanup" Phase)
```java
if (firstList != null) {
    current.next = firstList;
} else {
    current.next = secondList;
}
```
After the main loop terminates, one of the two lists is guaranteed to be empty (null), while the other may still contain remaining nodes. Since the source lists are already sorted, we do not need to traverse the remainder of the non-empty list. We simply point `current.next` to the head of the remaining sub-list, which is an $O(1)$ operation that effectively appends the rest of the chain.

---

## Key Insights

*   **Pointer Stability:** A common pitfall in linked list manipulation is losing the reference to the "next" node. Note that the algorithm carefully advances the pointer (`firstList = firstList.next`) *after* linking, ensuring the traversal remains intact.
*   **Memory Efficiency:** Because this is an in-place mutation, the original head references provided to the function are modified. In a production system, this could lead to side effects if the input lists are expected to remain immutable. If input preservation were a requirement, space complexity would increase to $O(N + M)$ due to the necessity of cloning nodes.
*   **Edge Case Resilience:**
    *   **Empty Inputs:** If both inputs are `null`, the code returns `dummyHead.next` (which is `null`), correctly handling the empty-list case.
    *   **Single Input Empty:** If one list is empty, the `while` loop is bypassed entirely, and the second `if/else` block correctly attaches the non-empty list to the dummy, providing a robust path for unbalanced lists.
*   **Stability:** The condition `firstList.val <= secondList.val` preserves the **relative order** of equal elements, ensuring the merge is stable. This is a critical property if the linked list nodes carry complex data payloads.

---
