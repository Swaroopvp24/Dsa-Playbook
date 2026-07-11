# check-if-array-is-sorted-and-rotated

## attempt_1.java
*Style: concise*

### Logic Notes: Array Sorted & Rotated

**Purpose**
Determines if an array was originally sorted in non-decreasing order and then rotated some number of positions. It returns `true` if there is at most one "drop" (where `nums[i] > nums[i+1]`) in the circular sequence.

**Key Components**
*   `check(int[] nums)`: Iterates through the array using modulo arithmetic to compare the last element with the first. Tracks the number of descending steps (`count`).

**Non-Obvious Logic**
*   **Circular Property:** The `(i + 1) % n` index effectively treats the array as a ring. A sorted-then-rotated array will have exactly one break point where the sequence drops. 
*   **Edge Case Handling:**
    *   `count == 0`: Handles fully identical arrays (e.g., `[1, 1, 1]`).
    *   `count == 1`: Handles standard rotated arrays (e.g., `[3, 4, 5, 1, 2]`).
    *   `count > 1`: Correctly invalidates arrays with multiple unsorted segments.

---
