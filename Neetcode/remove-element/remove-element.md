# remove-element

## remove_ele_attempt_1.java
*Style: concise*

### Overview
Removes all instances of a specified `val` from an array in-place by shifting non-target elements to the front of the array. It returns the new length of the modified prefix.

### Key Components
*   `ct` (pointer): Tracks the index where the next non-`val` element should be placed.
*   `for` loop: Iterates through the entire array exactly once, maintaining $O(n)$ time complexity.

### Logic Notes
*   **Two-Pointer Strategy**: Uses a "slow" pointer (`ct`) and a "fast" pointer (`i`). The array is modified in-place, overwriting elements that match `val` without requiring extra space ($O(1)$ space complexity).
*   **Order Independence**: The relative order of the remaining elements is preserved, though this is not strictly required by the problem definition.

---
