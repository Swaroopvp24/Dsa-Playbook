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

## sliding_window-myversion.java
*Style: detailed*

# Technical Reference: Sliding Window Permutation Check

## 1. Summary
The `checkInclusion` algorithm determines if `s2` contains a substring that is a permutation of `s1`. The core approach utilizes a **Fixed-Size Sliding Window** combined with **Frequency Array Comparison**. 

By maintaining a frequency distribution (histogram) of the current window of length `k` (where `k = s1.length()`) in `s2`, the algorithm compares this against the target frequency map of `s1`. If the maps are identical, a permutation exists.

## 2. Complexity Analysis

*   **Time Complexity: $O(L_1 + (L_2 \cdot \Sigma))$**
    *   $L_1$ is the length of `s1`, $L_2$ is the length of `s2`, and $\Sigma$ is the alphabet size (constant 26).
    *   Initializing the `s1f` array takes $O(L_1)$.
    *   The sliding window iterates through `s2` once ($L_2$ operations). Inside each iteration, `Arrays.equals(s1f, freq)` performs a linear scan of the 26-element arrays, leading to a complexity of $O(26 \cdot L_2)$. 
    *   Since $\Sigma$ is constant, this is effectively $O(L_1 + L_2)$.

*   **Space Complexity: $O(\Sigma)$**
    *   We allocate two integer arrays of size 26, regardless of input string length. This constitutes $O(1)$ auxiliary space relative to input size, specifically $O(\Sigma)$.

## 3. Component Deep Dive

### Frequency Tracking
*   `s1f`: A static baseline reference map generated once.
*   `freq`: A mutable sliding window map updated dynamically.
*   The logic relies on the fact that if two strings are permutations of each other, their character counts must be identical.

### Window Management
*   **Expansion:** The loop `for (int r = 0; r < s2.length(); r++)` acts as the right boundary incrementer. 
*   **Contraction:** The `while (r - l >= k)` block ensures the window size never exceeds `s1.length()`. By decrementing `l` and moving the pointer, we effectively "slide" the window, removing the contribution of the leftmost character and adding the newest character at `r`.

### Edge-Case Handling
*   **$L_1 > L_2$:** The logic naturally handles this. If `s1` is longer than `s2`, `r - l >= k` will never allow `freq` to match `s1f` effectively, or the window will never reach size $k$ if $L_1 > L_2$, returning `false` (correct).
*   **Empty Strings:** If `s1` is empty, the logic may behave unexpectedly depending on constraints; standard implementations assume $L_1 \ge 1$.
*   **Alphabet Bounds:** The code assumes lowercase English letters (`c - 'a'`). If the input contains Unicode or mixed casing, the array indices would go out of bounds, requiring a larger hash map or a different offset strategy.

## 4. Key Insights

### Performance Optimization: Array Comparison
The current `Arrays.equals` call is $O(26)$. While constant, it can be expensive if called $L_2$ times.
*   **Optimization:** A more performant approach would be to track a `matches` count variable.
    *   Maintain an integer `count` representing how many characters have matching frequencies between the window and `s1`.
    *   Update `count` only when a character's frequency in the window changes to match its frequency in `s1`.
    *   This reduces the comparison complexity from $O(\Sigma)$ to $O(1)$ per step.

### Subtlety of the Window Slide
The `while` loop structure is slightly unconventional. Usually, sliding windows increment `r` and `l` independently. Here, the `while` loop clears space only when the window exceeds size `k`. 
*   **Potential Bug:** If `s1.length()` is 0, the code might return `true` erroneously because `Arrays.equals` of two empty arrays is `true`. Always validate input constraints for $k > 0$.

### Memory Locality
Because the alphabet is limited to 26, using `int[26]` is cache-friendly and vastly superior to a `HashMap<Character, Integer>`. The compiler and CPU can easily predict these memory accesses, minimizing cache misses during the loop execution.

---
