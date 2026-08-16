# merge-sorted-array

## standard_two_pointer.java
*Style: detailed*

# Technical Deep-Dive: In-Place Merge of Sorted Arrays

## 1. Summary
The provided solution implements an **in-place three-pointer merge strategy** to combine two sorted arrays, `nums1` and `nums2`, into `nums1`. The algorithm leverages the fact that `nums1` has a buffer of size `n` at the end, allowing us to perform the merge from back to front (descending order). This technique avoids the $O(n+m)$ auxiliary space complexity typically required by the merge step in Merge Sort, achieving $O(1)$ space complexity by transforming the operation into a series of strategic overwrites.

## 2. Complexity Analysis

### Time Complexity: $O(m + n)$
*   **Derivation:** The algorithm utilizes a single `while` loop that terminates once all elements from `nums2` are processed (`nums2Index >= 0`). 
*   **Execution:** In each iteration, we perform exactly one comparison and one assignment. Since the maximum number of iterations is dictated by the total number of elements to be merged ($m + n$), the runtime scales linearly with the size of the combined input.

### Space Complexity: $O(1)$
*   **Derivation:** The merge is performed strictly in-place. We only utilize three integer pointers (`nums1Index`, `nums2Index`, `mergedIndex`) to track positions.
*   **Constraint:** The solution assumes `nums1` has a length of $m + n$, which is provided as a pre-condition. No additional data structures or recursion stacks are introduced.

## 3. Component Deep Dive

### The Three-Pointer Logic
1.  **`nums1Index` ($m-1$):** Tracks the last initialized element in the original `nums1`.
2.  **`nums2Index` ($n-1$):** Tracks the last element in `nums2`.
3.  **`mergedIndex` ($m+n-1$):** Tracks the insertion point at the end of the total allocated space in `nums1`.

### The Merge Loop
The loop `while (nums2Index >= 0)` is the core engine. 
*   **Backwards Traversal:** By starting at the end, we ensure that we never overwrite an element in `nums1` that hasn't been processed yet. The buffer space at the end of `nums1` serves as the destination for the largest elements found.

### Edge-Case Handling
*   **`nums2` is empty ($n=0$):** The loop condition `nums2Index >= 0` immediately fails. `nums1` remains untouched, which is the correct behavior.
*   **`nums1` is empty ($m=0$):** `nums1Index` starts at -1. The `if` condition `nums1Index >= 0` fails, correctly defaulting to filling `nums1` with the contents of `nums2`.
*   **`nums1` values are smaller than `nums2`:** Once `nums1Index` drops below zero, the `else` block executes exclusively, copying the remaining elements of `nums2` into the front of `nums1`.

## 4. Key Insights

*   **The Implicit Termination Condition:** Notice that if `nums1Index < 0` but `nums2Index >= 0`, the loop naturally continues to copy the remainder of `nums2`. However, if `nums2Index < 0` but `nums1Index >= 0`, the loop terminates. This is highly efficient because if `nums2` is fully merged and elements remain in `nums1`, they are already in their correct, final sorted positions.
*   **Avoidance of Array Shifting:** A naive approach would be to insert elements at the front of `nums1`, necessitating a shift of $O(m)$ elements for every insertion. This would result in $O(m \cdot n)$ complexity. By reversing the direction, we eliminate the need for shifting entirely.
*   **Memory Safety:** This algorithm relies on the precondition that `nums1.length == m + n`. If the allocated capacity is smaller, an `ArrayIndexOutOfBoundsException` will occur. In a production environment, an assertion or validation check on `nums1.length` should be added for robustness.

---
