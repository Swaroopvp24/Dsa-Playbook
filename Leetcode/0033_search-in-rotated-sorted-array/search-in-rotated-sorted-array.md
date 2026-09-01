# search-in-rotated-sorted-array

## standard_binary_search.java
*Style: concise*

### Search in Rotated Sorted Array

This implementation performs a $O(\log n)$ search in a rotated sorted array by first identifying the rotation pivot (the smallest element) and then applying standard binary search on the appropriate sorted half.

#### Key Components
*   **`search(int[], int)`**: Orchestrates the search by partitioning the array based on the `pivotIndex` and delegating to `binarySearch`.
*   **`findPivot(int[])`**: Uses a modified binary search to locate the index of the minimum element.
*   **`binarySearch(int[], int, int, int)`**: A standard iterative binary search restricted to a specific index range.

#### Logic Notes
*   **Pivot Identification**: The condition `nums[middle] > nums[right]` determines if the pivot is in the right half. If true, the pivot must be at `middle + 1` or further right. Otherwise, the pivot is at `middle` or to its left.
*   **Partitioning**: After finding the pivot, comparing the target against `nums[0]` effectively decides if the target exists in the "left" (0 to pivot-1) or "right" (pivot to end) segment of the array.
*   **Stability**: The pivot search logic handles the boundary by shrinking the search space until `left == right`, ensuring it converges on the absolute minimum.

---
