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
