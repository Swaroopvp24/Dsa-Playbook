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

## optimaal_binary_search.java
*Style: concise*

### Study Notes: Rotated Sorted Array Search

**Overview**
This code implements an $O(\log n)$ search algorithm for a target value within a sorted array that has been rotated at an unknown pivot point. It uses a modified binary search to identify which half of the array remains monotonically sorted at each step.

**Key Components**
*   `search(int[] nums, int target)`: Entry point; initializes two-pointer binary search and returns the index of the `target` or `-1` if not found.
*   `middle` calculation: Calculated as `left + (right - left) / 2` to prevent potential integer overflow.

**Non-Obvious Logic**
*   **The Sorted Half Check:** In a rotated sorted array, at least one half (left or right of `middle`) must always be sorted. 
*   **Binary Search Logic:** 
    1. Determine which half is sorted by comparing `nums[left]` and `nums[middle]`.
    2. Check if the `target` resides within the range of the *sorted* half.
    3. If the target is in the sorted range, prune the search to that half; otherwise, discard it and search the unsorted side.
*   **Boundary Conditions:** Using `left <= right` and `middle +/- 1` correctly handles single-element arrays and ensures the termination of the loop.

---
