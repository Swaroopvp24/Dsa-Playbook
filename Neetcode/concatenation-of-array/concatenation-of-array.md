# concatenation-of-array

## attempt_1.java
*Style: concise*

### Study Notes: Array Concatenation

**Overview**
Creates a new array that is the concatenation of the input array `nums` with itself (i.e., `nums + nums`). The solution runs in $O(n)$ time complexity.

**Key Components**
*   `ans[]`: Pre-allocated result array of size `2 * n`.
*   Single-loop assignment: Populates both the first and second halves of the result array simultaneously using the index offset `i + n`.

**Non-Obvious Logic**
*   **Loop Optimization:** By using `ans[i]` and `ans[i + n]` within the same iteration, we avoid traversing the array twice, effectively filling the new buffer in one pass.
*   **Memory Allocation:** Initializing `ans` with `n * 2` is more performant than dynamically resizing or using `System.arraycopy`, as it avoids intermediate object creation.

---
