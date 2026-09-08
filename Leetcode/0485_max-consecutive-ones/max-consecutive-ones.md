# max-consecutive-ones

## attempt_1.java
*Style: concise*

### Study Notes: Find Max Consecutive Ones

**Overview**
Calculates the length of the longest contiguous subarray of `1`s in a binary array using a single-pass linear scan.

**Logic**
*   `ct`: Tracks the current streak of consecutive `1`s.
*   `maxCt`: Stores the global maximum streak encountered so far.

**Key Logic**
*   **Reset Pattern**: When `nums[i] == 0`, the current streak `ct` is immediately reset to `0`. 
*   **Update Timing**: The `maxCt` is updated only when a `1` is encountered, ensuring the maximum is captured even if the array ends on a streak.
*   **Complexity**: $O(n)$ time complexity; $O(1)$ space complexity.

---
