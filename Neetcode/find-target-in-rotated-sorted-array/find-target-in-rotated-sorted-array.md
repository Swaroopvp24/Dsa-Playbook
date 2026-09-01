# find-target-in-rotated-sorted-array

## standard_binary_search.java
*Style: concise*

### Search in Rotated Sorted Array

This implementation performs a $O(\log n)$ search on a rotated sorted array by first identifying the rotation pivot (the minimum element) and then performing a standard binary search on the appropriate sorted sub-array.

#### Key Components
*   `search()`: Orchestrator that determines which half of the array contains the target based on the pivot point and the target value relative to `nums[0]`.
*   `findPivot()`: Uses a modified binary search to locate the index of the smallest element in $O(\log n)$.
*   `binarySearch()`: Standard implementation for a contiguous sorted range.

#### Logic Notes
*   **Pivot Identification**: The condition `nums[middle] > nums[right]` is critical; it identifies that the unsorted "drop" (the pivot) exists in the right half of the current range, forcing the search boundary to `middle + 1`.
*   **Search Partitioning**: By comparing `target` to `nums[0]`, we establish whether the target resides in the left (larger) or right (smaller) sorted segment.
*   **Efficiency**: Total time complexity is $O(\log n)$ because both pivot finding and the subsequent search are logarithmic. Space complexity is $O(1)$.

---

## optimal_binary_search.java
*Style: detailed*

# Technical Reference: Rotated Sorted Array Search

## 1. Summary
The implementation utilizes a **modified Binary Search** to achieve $O(\log n)$ time complexity on a rotated sorted array. A standard binary search relies on the monotonicity of the entire array; however, a rotated array contains at most two sorted contiguous subarrays. By identifying which half of the current search space $[left, right]$ maintains a strictly monotonic increase, we can determine if the `target` resides within that half or the other. The algorithm effectively prunes half of the remaining search space at every iteration by comparing `target` against the bounds of the sorted segment.

## 2. Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** In each iteration of the `while` loop, the search space is divided exactly in half (`middle = left + (right - left) / 2`). 
*   **Mechanism:** Even though the array is rotated, the property that at least one half is always sorted remains invariant. Because we eliminate half of the remaining elements based on the sorted segment check, the recurrence relation is $T(n) = T(n/2) + O(1)$, which resolves to logarithmic time via the Master Theorem.

### Space Complexity: $O(1)$
*   **Derivation:** The solution employs an iterative approach. It uses a fixed number of integer variables (`left`, `right`, `middle`) regardless of the input array size $n$. No auxiliary data structures or recursion stacks are utilized.

---

## 3. Component Deep Dive

### The Invariant Logic
The core logic relies on the fact that for any split at `middle`, if `nums[left] <= nums[middle]`, the range `[left, middle]` must be sorted. Otherwise, the range `[middle, right]` must be sorted.

#### Left Half Sorted Case: `nums[left] <= nums[middle]`
If the left segment is sorted, we perform a range check:
*   **Condition:** `nums[left] <= target < nums[middle]`
*   **Rationale:** If this condition holds, the target *must* be in the left half, allowing us to discard the entire right half (`right = middle - 1`). Otherwise, we pivot to the right (`left = middle + 1`).

#### Right Half Sorted Case: `nums[middle] < nums[right]` (Implicit)
If the left side is not sorted, the right side is guaranteed to be sorted:
*   **Condition:** `nums[middle] < target <= nums[right]`
*   **Rationale:** If the target falls within the bounds of the sorted right half, we move `left = middle + 1`. Otherwise, the target must be in the unsorted left partition, forcing `right = middle - 1`.

### Edge-Case Handling
*   **Single Element Arrays:** The `while (left <= right)` condition and the `middle` calculation correctly handle arrays of size 1. If `nums[0] == target`, it returns 0; otherwise, it exits and returns -1.
*   **Two Element Arrays:** The `middle` calculation favors the left index (e.g., `0 + (1-0)/2 = 0`). The logic holds as it compares the sortedness of the element at 0 against 0, then proceeds to compare against the remaining bounds.
*   **Integer Overflow:** The calculation `left + (right - left) / 2` is used instead of `(left + right) / 2` to prevent potential integer overflow for extremely large arrays, a best practice in robust binary search implementations.

---

## 4. Key Insights

*   **The Sorted Invariant:** It is mathematically impossible for *neither* half to be sorted after the pivot split. This is the fundamental premise that makes binary search applicable here.
*   **Strict Inequality vs. Inclusive:** Note the use of `<=`. This is critical for cases where the range contains only one or two elements. For example, if `nums = [3, 1]` and `target = 1`, the logic `nums[left] <= nums[middle]` (3 <= 3) evaluates to true, correctly triggering the logic that directs the search to the right.
*   **Performance Nuance:** This implementation performs optimally on arrays with distinct elements. If the array contained **duplicates** (e.g., `[1, 0, 1, 1, 1]`), the sorted property is compromised. In such scenarios, `nums[left] == nums[middle]` could occur even if the array is unsorted, forcing a worst-case $O(n)$ time complexity as you would be required to increment `left` linearly to break the ambiguity.
*   **Branch Predictor Friendly:** The code avoids complex nested logic or multiple conditional jumps by utilizing an `if-else` structure that cleanly partitions the decision space, which is efficient for CPU branch prediction.

---
