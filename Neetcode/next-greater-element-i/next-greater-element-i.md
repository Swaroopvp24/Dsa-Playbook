# next-greater-element-i

## standard_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Next Greater Element (Monotonic Stack)

## Summary
The solution employs a **Monotonic Decreasing Stack** combined with a **Hash Map** to solve the "Next Greater Element" problem in linear time. By traversing the reference array (`nums2`) from right to left, we maintain a stack of elements that are potential "next greater" candidates for preceding elements. The stack remains strictly decreasing from bottom to top; as we iterate, we prune values that are smaller than the current element, effectively identifying the first value to the right that is strictly greater. The result is cached in a map for $O(1)$ retrieval during the final pass over `nums1`.

---

## Complexity Analysis

### Time Complexity: $O(N + M)$
*   **$M$ (nums2 processing):** We traverse `nums2` exactly once. Each element is pushed onto the `decreasingStack` exactly once and popped at most once. Therefore, the amortized cost per element is $O(1)$, resulting in $O(M)$ time.
*   **$N$ (nums1 lookup):** We iterate through `nums1` once, performing an $O(1)$ average-time lookup in the `HashMap` for each element. This results in $O(N)$ time.
*   **Total:** $O(N + M)$, where $N$ and $M$ are lengths of `nums1` and `nums2` respectively.

### Space Complexity: $O(M)$
*   **Hash Map:** Stores the next greater element for all $M$ elements in `nums2`, requiring $O(M)$ space.
*   **Stack:** In the worst-case scenario (a strictly decreasing array), the stack will store all $M$ elements, requiring $O(M)$ space.
*   **Total:** $O(M)$, as the space is dominated by the storage required to process `nums2`.

---

## Component Deep Dive

### 1. Monotonic Stack Logic
The stack stores candidates that haven't found their "next greater" yet. By maintaining a **decreasing** order (top to bottom), the stack ensures that when we process an element `x`, any value currently on the stack that is $\le x$ is useless for all elements to the left of `x`. Why? Because `x` is closer and larger—it will always be the preferred "next greater" candidate compared to those smaller, discarded values.

### 2. Backward Traversal
Traversing `nums2` from right to left is the crucial optimization. It allows us to process the array in one pass while maintaining a running state of future elements. If we moved left-to-right, we would be searching for the "next greater," which would require looking ahead; moving right-to-left allows us to look at the "history" of elements we have already encountered.

### 3. Edge-Case Handling
*   **Last Element:** Handled by pre-seeding the `decreasingStack` and `nextGreaterMap` with the final element of `nums2`. Since there is nothing to its right, it is assigned `-1`.
*   **Empty `nums1` or `nums2`:** The logic gracefully handles empty arrays (though constraints usually imply $N, M \ge 1$), resulting in an empty result array.
*   **Duplicate Values:** The logic handles duplicates naturally. Because we store the mapping in a `HashMap`, we only ever care about the *value* of the next greater element, not its original index position.

---

## Key Insights

*   **Amortized Analysis:** It is tempting to look at the `while` loop nested inside the `for` loop and assume $O(M^2)$. However, since each element enters and leaves the stack exactly once, the operations are bounded by $2M$, confirming linear performance.
*   **Stack Pruning:** The line `decreasingStack.peek() <= currentElement` is the "monotonic" constraint. If the incoming element is larger, the smaller elements below it are rendered obsolete. This pruning is what maintains the efficiency of the algorithm.
*   **HashMap Overhead:** While a `HashMap` provides $O(1)$ lookup, it comes with higher constant-time memory overhead than an array. If the constraints on the values within `nums2` were small and contiguous (e.g., $0 \le nums2[i] \le 10^5$), an integer array could replace the `HashMap` to improve memory locality and reduce constant-time overhead.
*   **Subtle Bug Warning:** Be careful with the stack `pop()` order. Always ensure the `nextGreaterMap.put` occurs *after* the `while` loop clears the stack, but *before* the current element is pushed. Failing to do this would result in an element effectively being its own "next greater."

---
