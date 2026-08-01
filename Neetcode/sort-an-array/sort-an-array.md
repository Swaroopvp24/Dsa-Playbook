# sort-an-array

## MergeSort.java
*Style: concise*

### Merge Sort Implementation

**Overview**
Implements a classic divide-and-conquer Merge Sort algorithm to sort an integer array in $O(n \log n)$ time complexity. It recursively partitions the array into halves and merges them in sorted order.

**Key Components**
*   `sortArray(int[] nums)`: Public entry point that initializes the recursive sorting process.
*   `mergeSort(int[] nums, int l, int h)`: Recursive function that splits the array into subarrays until base cases (single elements) are reached.
*   `merge(int[] nums, int l, int m, int h)`: Merges two adjacent sorted segments (`[l..m]` and `[m+1..h]`) into a temporary array and copies the result back to `nums`.

**Notes**
*   **Space Complexity:** Uses $O(n)$ auxiliary space due to the allocation of a new `copy` array in each `merge` call (or globally if optimized).
*   **Stability:** The use of `nums[i] <= nums[j]` preserves the relative order of equal elements, making this a stable sort.
*   **Overflow Prevention:** `m = l + (h - l) / 2` is used instead of `(l + h) / 2` to prevent potential integer overflow for very large indices.

---
