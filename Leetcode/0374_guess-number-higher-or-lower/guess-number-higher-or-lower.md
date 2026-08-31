# guess-number-higher-or-lower

## standard_binary_search.java
*Style: concise*

### Notes: Binary Search for Hidden Integer

**Overview**
Implements a standard binary search algorithm to identify a target integer `n` chosen by a system-provided `guess(int)` API. It narrows the search space logarithmically by comparing the midpoint against the target.

**Key Components**
* `guessNumber(int n)`: Main entry point; initializes search boundaries `[0, n]` and executes the search loop.
* `guess(int m)`: External API; returns `0` (match), `-1` (target < guess), or `1` (target > guess).

**Logic/Implementation Details**
* **Overflow Prevention:** Uses `m = l + (r - l) / 2` instead of `(l + r) / 2` to avoid integer overflow when `l` and `r` are large.
* **Search Space:** Inclusive boundaries (`l <= r`) are used to ensure the algorithm handles the case where the target is exactly at the boundary. 
* **Complexity:** Time complexity is $O(\log n)$, space complexity is $O(1)$.

---
