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

## QuickSort.java
*Style: concise*

### Notes: QuickSort Implementation

**Overview**
Implements an in-place QuickSort algorithm using a Hoare-like partitioning scheme to sort an integer array in $O(n \log n)$ average time.

**Key Components**
*   **`sortArray`**: Entry point; initializes the recursive sorting process.
*   **`quickSort`**: Divide-and-conquer logic; recursively partitions the array around a pivot until base cases (single elements) are reached.
*   **`partition`**: Rearranges elements such that all values $\le$ pivot move to the left and values $>$ pivot move to the right. Returns the final index of the pivot.
*   **`swap`**: Helper utility to exchange two elements in the array.

**Logic Notes**
*   **Pivot Selection**: Uses the first element (`nums[l]`) as the pivot. Note that this implementation is susceptible to $O(n^2)$ worst-case time complexity on already sorted arrays.
*   **Pointer Inversion**: The `partition` method uses two pointers (`i` and `j`) that converge. The final `swap(nums, l, j)` is critical—it places the pivot into its correct sorted position before returning the index.
*   **Boundary Conditions**: The `i <= h` and `j >= l` checks in the `while` loops prevent `ArrayIndexOutOfBoundsException` during partitioning.

---
