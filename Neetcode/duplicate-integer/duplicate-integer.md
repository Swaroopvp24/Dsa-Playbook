# duplicate-integer

## attempt_1.java
*Style: concise*

### Study Notes: Duplicate Detection (Sorting Approach)

**Summary**
Identifies duplicates in an integer array by sorting the elements first, then performing a linear scan to check for adjacent identical values.

**Key Components**
*   `Arrays.sort(int[] nums)`: Reorders the array in $O(n \log n)$ time to bring duplicates together.
*   `for` loop: Iterates from index 1, comparing the current element to its immediate predecessor.

**Logic Notes**
*   **Time Complexity:** $O(n \log n)$ due to the sort.
*   **Space Complexity:** $O(1)$ or $O(\log n)$ depending on the `Arrays.sort` implementation (Dual-Pivot Quicksort for primitives), making it more space-efficient than a `HashSet` ($O(n)$ space) if memory is constrained.
*   **Constraint:** Destructive operation (modifies the input array order).

---

## attempt_1.java
*Style: concise*

### Duplicate Detection (Sorting Approach)

**Overview**
This solution identifies duplicates in an integer array by sorting the elements first, then performing a linear scan to check for adjacent identical values.

**Key Components**
*   `hasDuplicate(int[] nums)`: The primary function that returns `true` if any value appears at least twice; otherwise, `false`.

**Logic Notes**
*   **Time Complexity:** $O(n \log n)$ due to the sorting step.
*   **Space Complexity:** $O(1)$ or $O(\log n)$ depending on the `Arrays.sort()` implementation (in-place sorting).
*   **Optimization Note:** This approach is space-efficient compared to using a `HashSet` ($O(n)$ space), but it is slower and modifies the input array. Only use this if memory constraints are strict and array mutation is permitted.

---

## attempt_1.java
*Style: concise*

### Study Notes: Duplicate Detection via Sorting

**Overview**
Determines if an integer array contains any duplicate elements by sorting the array first. This approach optimizes for space at the cost of time complexity compared to a HashSet-based approach.

**Key Components**
*   `Arrays.sort(int[] nums)`: Reorders elements in ascending order, bringing potential duplicates adjacent to each other.
*   `for` loop: Iterates from index 1, comparing each element with its immediate predecessor to identify matches.

**Logic Notes**
*   **Time Complexity:** $O(n \log n)$ due to the sorting overhead.
*   **Space Complexity:** $O(1)$ (or $O(\log n)$ depending on `Arrays.sort` implementation), which is more memory-efficient than $O(n)$ space required by a `HashSet`.
*   **Early Exit:** Returns `true` immediately upon finding the first pair, making it efficient for arrays with early-positioned duplicates.

---
