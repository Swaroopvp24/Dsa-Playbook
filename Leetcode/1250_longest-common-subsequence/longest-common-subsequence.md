# longest-common-subsequence

## attempt_1_memoizatiion.java
*Style: detailed*

### Technical Reference: Longest Common Subsequence (LCS) via Top-Down DP

#### 1. Summary
The provided solution implements the classic **Longest Common Subsequence** problem using a **Top-Down Dynamic Programming (Memoization)** approach. The core algorithmic technique relies on the principle of optimal substructure: the LCS of two strings can be recursively derived from the LCS of their prefixes.

*   **Recursive Transition:**
    *   If `s1[i] == s2[j]`: The character is part of the LCS; increment the result by 1 and move diagonally (i-1, j-1).
    *   If `s1[i] != s2[j]`: The LCS must be the maximum result obtained by either skipping the current character of `s1` or skipping the current character of `s2`.

#### 2. Complexity Analysis

*   **Time Complexity:** $O(N \times M)$
    *   Where $N$ and $M$ are lengths of `st1` and `st2` respectively.
    *   **Reasoning:** The `dp` table size is $N \times M$. Each state `(i, j)` is computed exactly once due to the memoization check (`dp[i][j] != -1`). Subsequent calls to a previously computed state return in $O(1)$. Total operations are proportional to the number of states.
*   **Space Complexity:** $O(N \times M)$
    *   **Reasoning:** The space complexity is dictated by two factors:
        1.  **DP Table:** Explicit $O(N \times M)$ memory allocation to store results.
        2.  **Recursion Stack:** The recursion depth can reach $O(N + M)$ in the worst-case scenario (e.g., strings with no common characters).
    *   Total complexity is $O(N \times M)$ due to the dominance of the DP table size.

#### 3. Component Deep Dive

*   **`sub(...)` Function:** 
    *   **Base Case:** Explicitly handles indices reaching `< 0`. This effectively models the "empty string" boundary condition where the LCS of any string and an empty string is 0.
    *   **Memoization Lookup:** Performs an $O(1)$ check against `dp[i][j]`. This prevents the exponential $O(2^{\max(N,M)})$ complexity of a naive recursive approach.
    *   **Branching Logic:** The `Math.max` branch effectively prunes the search space by choosing the optimal path between overlapping subproblems.

*   **Data Structures:**
    *   `char[]` arrays: Converting the input `String` objects to `char[]` is a critical performance optimization in Java. Accessing `String.charAt(i)` inside a recursive loop involves bounds checking; `char[]` access is generally faster at the bytecode level.
    *   `dp` matrix: Initialized with `-1` to distinguish between a "computed result of 0" and an "uncomputed state."

*   **Edge-Case Handling:**
    *   **Empty Strings:** The logic gracefully returns 0 immediately as the indices will be `< 0` upon the first call if length is 0 (though the current implementation might throw an `ArrayIndexOutOfBounds` if `st1` or `st2` length is 0 due to array initialization `new int[0][0]`). 
    *   *Correction Note:* A production-grade implementation should include `if (st1.length() == 0 || st2.length() == 0) return 0;` before initializing the `dp` table.

#### 4. Key Insights & Nuances

*   **Recursion Depth:** For very large input strings, this approach may trigger a `StackOverflowError` in Java, as the default thread stack size is often limited. For strings with lengths $> 10^4$, an **Iterative Bottom-Up (Tabulation)** approach is strictly preferred to avoid stack overhead.
*   **Space Optimization (The "Two-Row" Trick):** If only the length of the LCS is required (not the sequence itself), the space complexity can be reduced to $O(\min(N, M))$ by observing that computing row $i$ only requires row $i-1$. This is a standard optimization for memory-constrained environments.
*   **Initialization Trap:** The code uses `Arrays.fill(row, -1)`. This is correct. However, if the DP table were initialized with `0`, the logic would fail because 0 is also a valid output for the LCS subproblem, leading to redundant re-computation of states.
*   **Performance Bottleneck:** The current solution creates the `dp` matrix regardless of input size. If one string is significantly shorter than the other, the memory footprint remains tied to $N \times M$. Ensuring the matrix is `dp[min(N, M)][max(N, M)]` can improve cache locality.

---
