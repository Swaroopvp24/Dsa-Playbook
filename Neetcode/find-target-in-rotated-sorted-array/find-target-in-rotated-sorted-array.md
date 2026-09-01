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
