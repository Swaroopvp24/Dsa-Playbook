# permutation-in-string

## standard_sliding_window.java
*Style: detailed*

# Technical Reference: Sliding Window Permutation Check

## Summary
The solution employs a **Fixed-Size Sliding Window** technique combined with **Frequency State Tracking** to determine if any permutation of string `s1` exists as a contiguous substring within `s2`. 

Rather than re-counting characters for every substring (which would be $O(N \cdot K)$), the algorithm maintains a frequency delta state. It treats the condition "the frequency of all 26 lowercase English characters in the window matches those in `s1`" as a boolean count (`matches`). By incrementally updating this count as the window slides, the algorithm reduces the per-step overhead to $O(1)$.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Initialization:** Populating `s1f` and the initial window of `s2` takes $O(K)$, where $K = |s1|$.
*   **Sliding:** The loop iterates $N - K$ times ($N = |s2|$). Inside the loop, every operation (array access, integer increment/decrement, comparison) is $O(1)$.
*   **Total:** $O(K + (N-K)) = O(N)$. The algorithm traverses `s2` exactly once.

### Space Complexity: $O(1)$
*   The algorithm utilizes two integer arrays (`s1f` and `freq`) of fixed size 26.
*   Because the alphabet size ($\Sigma$) is constant (26), the space requirements do not scale with the input string lengths, resulting in constant space.

---

## Component Deep Dive

### 1. The `matches` Counter Strategy
Instead of comparing two arrays of size 26 at every step ($O(26)$), the algorithm maintains a running integer `matches`.
*   A "match" is defined as `s1f[i] == freq[i]` for a specific character index `i`.
*   As the window slides, the code updates the `freq` array. Before and after these updates, it checks if the state of `matches` transitioned:
    *   **Incrementing/Decrementing:** If an update causes `freq[i]` to equal `s1f[i]`, we increment `matches`. If an update causes `freq[i]` to deviate from `s1f[i]` (from a state of equality), we decrement `matches`.

### 2. Edge Case Handling
*   **$|s1| > |s2|$:** The initial guard clause `if (k > s2.length())` correctly identifies that a permutation cannot exist if the pattern is larger than the target.
*   **End-of-Loop Boundary:** The final check `return matches == 26` is critical. Because the loop logic checks for a match *before* processing the final window position, the final return ensures the last window is validated.
*   **Single Character Strings:** The logic naturally handles single-character strings as the loop structure for `r` will simply skip if `k == s2.length()`, relying on the final check.

---

## Key Insights

### The "State Destruction" Logic
The most subtle part of the code is the update logic:
```java
else if (s1f[index] + 1 == freq[index]) {
    matches--;
}
```
This is a clever way to handle state transitions. When `freq[index]` exceeds `s1f[index]` by exactly 1, we know the window was previously in a "match" state for this specific character. Incrementing the count forces a mismatch. 

### Why not just `Arrays.equals()`?
While `Arrays.equals(s1f, freq)` is $O(26)$ and technically $O(1)$ constant time, it performs 26 comparisons per slide. The `matches` counter approach reduces this to roughly 4-6 operations per slide. While the asymptotic complexity remains the same, this is a significant optimization in hot-path scenarios or high-throughput systems.

### Potential Pitfalls
*   **Alphabet assumptions:** The code assumes `s2.charAt(r) - 'a'` will always map to an index `0-25`. If the input contained non-lowercase characters, an `ArrayIndexOutOfBoundsException` would occur.
*   **Implicit Logic Order:** The order of `matches--` vs `matches++` is rigid. You must ensure you update the `freq` array *before* checking the `s1f` equality, otherwise, you are checking the state *before* the character was added/removed, which leads to logical off-by-one errors.

---
