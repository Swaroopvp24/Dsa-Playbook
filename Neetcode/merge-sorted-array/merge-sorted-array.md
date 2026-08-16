# merge-sorted-array

## standard_two_pointer.java
*Style: detailed*

# Technical Deep-Dive: In-Place Sorted Array Merge

## Summary
The solution implements an **in-place reverse-pointer merge algorithm**. This approach leverages the pre-allocated buffer at the end of `nums1` to perform a linear-time merge without requiring additional $O(m+n)$ auxiliary space. By traversing backwards from the last valid element of each array, we avoid the destructive overwriting of elements that would occur with a forward-moving pointer approach, effectively transforming an $O((m+n) \log (m+n))$ or $O(m+n)$ auxiliary space problem into an $O(1)$ space operation.

---

## Complexity Analysis

### Time Complexity: $O(m + n)$
*   **Derivation:** The algorithm uses a single `while` loop that terminates when `nums2Index < 0`. In each iteration, exactly one element (either from `nums1` or `nums2`) is placed into its final sorted position at `mergedIndex`.
*   **Total Operations:** The loop executes exactly $n$ times (the number of elements in `nums2`). The elements remaining in `nums1` are already in their correct sorted positions, requiring no further movement. Thus, the work is strictly proportional to the number of elements being processed.

### Space Complexity: $O(1)$
*   **Derivation:** The merge is performed strictly in-place. We utilize only three integer pointers (`nums1Index`, `nums2Index`, `mergedIndex`) regardless of the size of the input arrays. No auxiliary arrays or recursive stack frames are utilized.

---

## Component Deep Dive

### 1. Pointer Initialization
*   `nums1Index = m - 1`: Points to the last initialized element in the source array.
*   `nums2Index = n - 1`: Points to the last element in the secondary array.
*   `mergedIndex = m + n - 1`: Points to the end of the buffer in `nums1`.

### 2. The Reverse-Traversal Logic
The core strategy relies on the fact that `nums1` has enough capacity to hold both arrays. By working backwards:
*   We compare `nums1[nums1Index]` and `nums2[nums2Index]`.
*   The larger of the two is placed at `mergedIndex`.
*   **Edge-Case - `nums1Index < 0`:** If the loop continues but `nums1` is exhausted, the condition `nums1Index >= 0` fails. The code falls into the `else` block, efficiently copying the remaining elements of `nums2` into the front of `nums1`.
*   **Edge-Case - `nums2Index < 0`:** The loop condition `while (nums2Index >= 0)` ensures that if `nums2` is exhausted, we terminate. Since `nums1` is already sorted, the remaining elements in `nums1` (from index 0 to `nums1Index`) are already in their correct place.

---

## Key Insights

### Avoiding Buffer Collision
The fundamental risk in merging arrays is overwriting elements that have not yet been compared. A standard forward merge would require $O(m)$ extra space. By moving the "insertion point" to the end of the array, we guarantee that any element we overwrite at `mergedIndex` has already been moved to a future position or is irrelevant (a placeholder), maintaining data integrity without a temporary buffer.

### Short-Circuiting Optimization
*   The logic assumes `nums1` is already sorted. If `nums2Index` reaches `-1` early, the algorithm terminates instantly. This provides a "best-case" performance when all elements in `nums1` are larger than all elements in `nums2`, performing $O(1)$ movements beyond the loop overhead.
*   Conversely, if `nums1Index` reaches `-1` first, the `else` block performs a bulk write of the remaining `nums2` elements. 

### Implementation Nuance
One might be tempted to use a ternary operator inside the loop to save lines, but the explicit `if-else` is preferred here for clarity and to handle the implicit state where `nums1Index` might be negative. The explicit check `nums1Index >= 0` is critical; without it, an `ArrayIndexOutOfBoundsException` would occur during the comparison when `nums1` elements are exhausted.

---
