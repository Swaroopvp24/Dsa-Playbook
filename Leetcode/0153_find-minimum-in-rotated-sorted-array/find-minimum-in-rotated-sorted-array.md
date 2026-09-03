# find-minimum-in-rotated-sorted-array

## optimal_binary_search.java
*Style: detailed*

# Technical Deep-Dive: Rotated Sorted Array Minimum

## Summary
The provided solution implements a modified binary search to find the minimum element in a **rotated sorted array** (an array originally sorted in ascending order and then rotated at some pivot point $k$).

The core algorithmic technique relies on the **invariant property of rotation**. In a rotated sorted array, if the middle element is greater than the element at the right boundary (`nums[middle] > nums[right]`), the "inflection point" (where the value drops from the maximum to the minimum) must exist in the right half. Conversely, if `nums[middle] <= nums[right]`, the minimum is guaranteed to be at or to the left of the `middle` index. This binary search approach effectively prunes the search space by half in each iteration, converging on the pivot point.

---

## Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** The algorithm utilizes a divide-and-conquer strategy. In each iteration of the `while` loop, the search range `[left, right]` is halved.
*   **Mathematical Context:** The number of iterations required to reduce a range of size $n$ to 1 is defined by $2^k = n$, resulting in $k = \log_2(n)$. Therefore, the time complexity is logarithmic relative to the number of elements.

### Space Complexity: $O(1)$
*   **Derivation:** The solution is strictly iterative. It utilizes a constant amount of extra space (three integer variables: `left`, `right`, and `middle`) regardless of the input array size. No auxiliary data structures or recursion stacks are employed.

---

## Component Deep Dive

### 1. The Pivot Invariant
The logic `if (nums[middle] > nums[right])` is the "heart" of the algorithm. 
*   **The Scenario:** If `nums[middle] > nums[right]`, we are currently in the "left ascending segment" of the rotated array. Since the array is rotated, a larger value appearing before a smaller value at the end of the range implies the rotation point exists between `middle + 1` and `right`.
*   **The Exclusion:** We can safely set `left = middle + 1` because we know `middle` itself cannot be the minimum.

### 2. The Convergence Logic
When `nums[middle] <= nums[right]`, we are in the "right ascending segment" or the entire segment is sorted. 
*   **The Invariant:** The minimum could be at `middle` or somewhere to the left. By setting `right = middle`, we preserve the potential minimum within our search boundary.

### 3. Edge-Case Handling
*   **Single Element:** If `nums.length == 1`, `left` and `right` start at 0, the loop condition `0 < 0` fails, and `nums[0]` is correctly returned.
*   **Already Sorted (No Rotation):** If the array is sorted (e.g., `[1, 2, 3]`), `nums[middle] <= nums[right]` will always trigger, eventually moving `right` down to index 0, correctly identifying `nums[0]` as the minimum.
*   **Two Elements:** Whether sorted `[1, 2]` or rotated `[2, 1]`, the logic correctly collapses the range to the smaller index.

---

## Key Insights

*   **Integer Overflow Prevention:** The calculation `middle = left + (right - left) / 2` is critical. Using `(left + right) / 2` is a classic bug-prone pattern in Java that can cause an integer overflow if `left + right` exceeds `Integer.MAX_VALUE`.
*   **Loop Termination (`left < right`):** Using `left < right` ensures the loop terminates when `left == right`. This is safer than `left <= right` because it avoids an infinite loop when `right` is updated to `middle`.
*   **Non-Strict Inequality:** The `else` block handles the `nums[middle] == nums[right]` case. In a standard rotated array with unique elements, this only happens when `left == right`. If the array contained duplicates, this algorithm would still work for finding *a* minimum, but would degrade to $O(n)$ in scenarios like `[3, 3, 1, 3]`, requiring an additional check to handle the ambiguity.
*   **Performance Nuance:** Because this solution is iterative, it benefits from instruction-level parallelism and CPU branch prediction. The lack of branching complexity inside the loop makes it highly performant for memory-bound workloads where the array is too large to fit in L1 cache.

---
