# find-minimum-in-rotated-sorted-array

## optimal_binary_search.java
*Style: detailed*

# Engineering Deep-Dive: Rotated Sorted Array Minimum Search

## 1. Summary
The `findMin` implementation leverages a **Modified Binary Search** (variant of the "Find Pivot" algorithm) to locate the minimum element in an $O(\log n)$ timeframe. 

The core algorithmic intuition relies on the property of a rotated sorted array: if an array is rotated, it contains exactly one "inflection point" where `nums[i] > nums[i+1]`. By comparing the `middle` element against the `right` boundary, the algorithm implicitly determines whether the current search space is strictly sorted or contains the inflection point. If `nums[middle] > nums[right]`, the pivot point must reside in the right partition (excluding the middle); otherwise, the pivot is either at the middle or to the left.

## 2. Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** In each iteration of the `while` loop, the search space `[left, right]` is halved. The loop terminates when `left == right`.
*   **Mathematical Proof:** The number of iterations follows the recurrence relation $T(n) = T(n/2) + O(1)$, which resolves to $O(\log n)$ via the Master Theorem.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm is **iterative** and operates entirely in-place. It only requires a constant amount of memory for stack variables (`left`, `right`, `middle`), regardless of the input array size.

---

## 3. Component Deep Dive

### The Pivot Condition: `nums[middle] > nums[right]`
This is the heart of the logic.
*   **Case 1: `nums[middle] > nums[right]`**: This proves the rotation occurred somewhere between `middle` and `right`. Therefore, the current `middle` cannot be the minimum, allowing us to safely set `left = middle + 1`.
*   **Case 2: `nums[middle] <= nums[right]`**: This indicates the segment `[middle, right]` is sorted. While `middle` might be the minimum, it is possible the minimum exists to the left of `middle`. Thus, we constrain the search to `right = middle` (keeping `middle` in the search space).

### Edge-Case Handling
*   **Single Element Arrays:** If `nums.length == 1`, the loop condition `left < right` is false immediately. The function correctly returns `nums[0]`.
*   **Non-Rotated Arrays:** If the array is sorted normally (e.g., `[1, 2, 3]`), the condition `nums[middle] > nums[right]` is always false. The `right` pointer collapses to the left until `left == right`, correctly returning the first element.
*   **Integer Overflow:** The calculation `middle = left + (right - left) / 2` is used instead of `(left + right) / 2`. This is a critical defensive programming pattern to prevent overflow in scenarios where `left + right` exceeds `Integer.MAX_VALUE`.

---

## 4. Key Insights

### The "Inclusive" vs "Exclusive" Boundary
A common pitfall in binary search is the selection of boundaries. In this specific implementation:
*   We use `right = middle` rather than `right = middle - 1`. This is required because if `nums[middle]` is the minimum, `right = middle - 1` would incorrectly discard the answer. 
*   Conversely, because we know `nums[middle] > nums[right]`, we are certain `middle` is not the minimum, allowing `left = middle + 1`.

### Convergence Stability
The use of `middle = left + (right - left) / 2` (integer division truncating towards zero) ensures that `middle` is always biased toward `left`. In a two-element array `[A, B]` where `A > B`, `middle` will evaluate to `left`. 
1.  `nums[middle] > nums[right]` is true.
2.  `left` becomes `middle + 1` (the index of `B`).
3.  The loop terminates with `left == right`, correctly returning `B`.

### Performance Nuance
While $O(\log n)$ is optimal for this problem, note that if the array contains **duplicate values** (e.g., `[3, 3, 1, 3, 3]`), the condition `nums[middle] > nums[right]` is insufficient. In such a case, the worst-case time complexity degrades to $O(n)$ because the algorithm cannot distinguish which half contains the inflection point when `nums[middle] == nums[right]`, necessitating a linear scan. This implementation assumes a strictly unique-element rotated array.

---
