# add-two-numbers

## linkedlist_solution.java
*Style: detailed*

# Engineering Deep-Dive: Linked List Addition

## Summary
The solution implements a robust, single-pass simulation of schoolbook addition, adapted for singly-linked lists representing non-negative integers in reverse order (least significant digit first). The algorithm iterates through both lists simultaneously, maintaining a `carry` variable to propagate overflows across decimal positions. By utilizing a "dummy head" sentinel node, the implementation eliminates conditional branching for initializing the head of the result list, resulting in cleaner and more performant pointer manipulation.

---

## Complexity Analysis

### Time Complexity: $O(\max(N, M))$
*   **Derivation:** $N$ and $M$ are the lengths of the two input lists. The loop executes exactly once for every node in the longer list, plus potentially one additional iteration if a final `carry` persists after both lists are exhausted. Each iteration performs $O(1)$ constant-time arithmetic and pointer assignment operations. 

### Space Complexity: $O(\max(N, M))$
*   **Derivation:** The space is determined by the output list. We generate a new linked list with a length equal to the number of digits in the sum, which is at most $\max(N, M) + 1$. The algorithm operates in-place relative to the input lists (no auxiliary data structures like stacks or recursion are used), satisfying the requirement for $O(1)$ auxiliary space excluding the returned output.

---

## Component Deep Dive

### 1. The Dummy Sentinel Pattern
```java
ListNode dummyHead = new ListNode(0);
ListNode currentNode = dummyHead;
```
By initializing `currentNode` to `dummyHead`, we treat the result list as a non-empty sequence from the start. This pattern avoids the "check-if-head-is-null" logic inside the loop, ensuring the head pointer never needs re-assignment during iteration.

### 2. Synchronization Logic
The loop condition `firstList != null || secondList != null || carry != 0` is the engine of the algorithm.
*   **Asynchronous Lengths:** The ternary operators correctly normalize missing nodes to `0`, effectively padding the shorter list with virtual zeros without modifying the underlying data structures.
*   **Terminal Carry:** The `carry != 0` check handles cases where the sum of the most significant digits exceeds 9 (e.g., $500 + 500 = 1000$), ensuring the new list can grow beyond the length of the longest input list.

### 3. Pointer Progression
The advancement logic is explicitly guarded:
```java
if (firstList != null) firstList = firstList.next;
```
This is critical for stability. It prevents `NullPointerException` when one list is shorter, ensuring the pointer stays anchored at `null` for the remainder of the calculation.

---

## Key Insights & Performance Nuances

### Arithmetic Precision
*   **Modular Arithmetic:** The use of `sum % 10` and `sum / 10` is the standard, efficient way to separate the current digit from the carry. 
*   **Overflow:** Because `firstDigit` and `secondDigit` are at most 9, and `carry` is at most 1, `sum` will never exceed 19. This fits safely within standard integer types without requiring `Long` or `BigInteger`.

### Subtle Edge Cases
*   **Unequal Lists:** The implementation gracefully handles inputs like `[9, 9]` and `[1]`. The shorter list is treated as having leading zeros.
*   **Single-Digit Sums:** If inputs are `[0]` and `[0]`, the loop runs once, produces `[0]`, and correctly returns the dummy-next node.
*   **Final Carry:** The most common source of error in this problem is failing to include `carry != 0` in the loop condition, which would result in losing the final most-significant digit (e.g., $9+1 = 0$ instead of $10$).

### Potential Optimization
While this implementation is optimal in terms of Big-O, in scenarios where memory allocation is expensive, one could consider **reusing nodes** from the first list if it is equal to or longer than the second. This would reduce the garbage collector pressure to $O(\min(N, M))$ at the cost of significantly more complex code and side effects on the input structures (which is generally discouraged in production APIs).

---
