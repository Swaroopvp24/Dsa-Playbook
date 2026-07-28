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

## attempt_2_tabulation.java
*Style: detailed*

# Technical Deep-Dive: Longest Common Subsequence (LCS)

## Summary
This implementation solves the **Longest Common Subsequence** problem using a bottom-up Dynamic Programming (DP) approach. The algorithm constructs an optimal solution by decomposing the problem into overlapping sub-problems: finding the LCS of prefixes of strings `s1` and `s2`. 

While the provided code contains vestigial recursive logic (memoized top-down approach), the functional core relies on an $O(N \times M)$ iterative table-filling strategy. The state transition is based on the decision: if characters at current indices match, the LCS length increments; otherwise, the value is the maximum LCS length obtainable by excluding one character from either string.

---

## Complexity Analysis

### Time Complexity: $O(N \times M)$
*   **Derivation**: The solution utilizes a nested loop structure where `i` iterates from `1` to `n1` and `j` iterates from `1` to `n2`. Each cell in the `dp` table is computed in $O(1)$ constant time (comparison + addition or comparison + max).
*   **Constraint Impact**: With strings of length $N$ and $M$, the work performed is strictly proportional to the number of cells in the matrix, making this optimal for standard DP approaches to LCS.

### Space Complexity: $O(N \times M)$
*   **Derivation**: We allocate a 2D integer array of size $(N+1) \times (M+1)$. 
*   **Optimization Note**: The space complexity is technically $O(N \times M)$ due to the explicit table. However, since the state calculation for `dp[i][j]` only depends on `dp[i-1][j]`, `dp[i][j-1]`, and `dp[i-1][j-1]`, this can be space-optimized to $O(\min(N, M))$ using two rows (current and previous) or a single rolling array.

---

## Component Deep Dive

### 1. The DP State Transition
The core logic resides in:
```java
if (s1[i-1] == s2[j-1]) {
    dp[i][j] = 1 + dp[i - 1][j - 1];
} else {
    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
}
```
*   **Matching Case**: If `s1[i-1] == s2[j-1]`, these characters are guaranteed to be part of the LCS ending at these indices. We extend the previous optimal length found at `(i-1, j-1)`.
*   **Mismatch Case**: If they differ, the LCS must be either the LCS of `(s1[0...i-2], s2[0...j-1])` or `(s1[0...i-1], s2[0...j-2])`. We take the maximum of these two paths to ensure optimality.

### 2. Base Case Initialization
*   The `dp` table is sized `(n1+1) x (n2+1)` to account for the empty string case.
*   By initializing row 0 and column 0 to `0`, the algorithm implicitly handles the case where one string is empty (LCS length is 0), removing the need for boundary checks inside the primary loops.

### 3. Vestigial Recursive Logic (`sub` method)
The provided code includes a `sub` function, which represents the **Top-Down Memoized** version of the problem. 
*   **Pros**: It computes only necessary states; beneficial if the state space is sparse.
*   **Cons**: Subject to `StackOverflowError` on very large strings due to recursion depth ($O(N+M)$ call stack). The bottom-up iterative approach provided is generally preferred in production for better cache locality and avoiding stack limits.

---

## Key Insights

*   **Cache Locality**: Iterating in row-major order (`i` then `j`) is cache-friendly for Java's row-based 2D array representation. If memory usage becomes critical, transposing the matrix to ensure the inner loop iterates over the contiguous dimension of the 2D array can further improve performance.
*   **Initialization Trap**: If the DP array was allocated with size `N x M` rather than `(N+1) x (M+1)`, one would need to implement `if` checks for indices `i=0` or `j=0` inside the loop, significantly cluttering the core transition logic. The current approach of padding the grid acts as a "sentinel value" barrier.
*   **Subtle Bug Warning**: Note the off-by-one indexing. `s1` and `s2` are 0-indexed, but the `dp` table is 1-indexed (to handle the base case). Accessing `s1[i-1]` is crucial; attempting to access `s1[i]` inside the loops will result in an `ArrayIndexOutOfBoundsException` at the end of the iterations.

---
