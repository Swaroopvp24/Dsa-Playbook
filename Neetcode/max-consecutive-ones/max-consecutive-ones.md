# max-consecutive-ones

## attempt_1.java
*Style: concise*

### Notes: Find Max Consecutive Ones

**Overview**
This algorithm calculates the length of the longest contiguous subarray of `1`s in a binary array by maintaining a running count that resets upon encountering a `0`.

**Key Components**
* `ct`: Tracks the current streak of consecutive `1`s.
* `maxCt`: Stores the global maximum streak encountered during the iteration.

**Logic Notes**
* **Reset Mechanism:** The running count (`ct`) is explicitly reset to `0` whenever `nums[i] == 0`, effectively breaking the sequence.
* **Efficiency:** Single-pass solution with $O(n)$ time complexity and $O(1)$ space complexity.
* **Optimization:** `maxCt` is updated eagerly within the `if (nums[i] == 1)` block to avoid unnecessary updates when encountering `0`s.

---
