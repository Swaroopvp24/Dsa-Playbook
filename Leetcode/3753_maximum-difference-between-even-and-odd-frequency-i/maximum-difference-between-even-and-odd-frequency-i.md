# maximum-difference-between-even-and-odd-frequency-i

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: Frequency-Based Parity Optimization

## 1. Summary
The `maxDifference` algorithm computes the parity-constrained delta between character frequency distributions in a string. The core objective is to identify the range (difference) between the maximum odd-frequency character and the minimum even-frequency character within a fixed-size character set ($|\Sigma| = 26$). 

The approach utilizes a **Linear Frequency Counting** pattern (or Bucket Counting). By reducing the input string to a static array of size 26, the algorithm decouples the time complexity from the string length $N$ after the initial pass, moving it to constant space $O(|\Sigma|)$ and linear time $O(N)$.

---

## 2. Complexity Analysis

### Time Complexity: $O(N + |\Sigma|)$
*   **$O(N)$**: The initial pass through the string of length $N$ to compute the frequency map. Every character is accessed exactly once.
*   **$O(|\Sigma|)$**: The secondary pass iterates through the frequency array. Since the alphabet size $|\Sigma|$ is fixed at 26, this is technically $O(1)$ constant time, but conceptually scales with the size of the character set.
*   **Total**: $O(N)$, where $N$ is the number of characters in the string.

### Space Complexity: $O(|\Sigma|)$
*   The frequency array `int[26]` consumes a constant amount of memory regardless of the input string length.
*   **Space**: $O(1)$ (effectively constant, assuming a fixed alphabet size).

---

## 3. Component Deep Dive

### Frequency Mapping (`int[] frequency`)
Instead of a `HashMap<Character, Integer>`, which incurs hashing overhead and object boxing, the code uses a primitive array. By using `c - 'a'`, it maps lowercase English characters to indices $[0, 25]$, providing $O(1)$ access and minimizing cache misses.

### Parity Logic
The logic hinges on two state variables:
1.  **`minEvenFrequency`**: Initialized to `Integer.MAX_VALUE`. This is a crucial design choice; it handles the case where no even frequencies exist by ensuring the final calculation would be mathematically invalid (or needs to be guarded).
2.  **`maxOddFrequency`**: Tracks the global maximum among odd counts.

### Edge-Case Handling
*   **Missing Even/Odd Frequencies**: 
    *   If no character has an even frequency, `minEvenFrequency` remains `Integer.MAX_VALUE`. The result will become negative, which is logically consistent with the current implementation but potentially an area for requirements clarification (i.e., whether the problem guarantees at least one even and one odd frequency).
*   **Single Character String**: The algorithm correctly computes frequency 1, identifies it as odd, and returns the result (potentially resulting in negative values if no even counts exist).
*   **Input with No Characters**: The frequency array remains zeroed; the loop logic handles this gracefully, though `minEvenFrequency` would result in a very large negative output.

---

## 4. Key Insights

*   **Fixed-Alphabet Performance**: The primary performance bottleneck is the initial string traversal. Memory locality is excellent here because the `int[]` fits entirely within L1 cache, preventing cache thrashing.
*   **Branch Prediction**: The `if (count > 0 && count % 2 == 0)` logic creates branches during the second pass. In a highly optimized environment with massive alphabets, one might use bitwise ops: `(count & 1) == 0` for even and `(count & 1) == 1` for odd. While compilers usually optimize `% 2` to bitwise shifts, explicit bitwise arithmetic is more readable for performance-critical systems.
*   **Subtle Bug/Risk**: The solution assumes the input string contains only lowercase 'a'-'z'. If the input contains uppercase characters or special symbols, the `c - 'a'` index will throw an `ArrayIndexOutOfBoundsException`.
    *   *Recommendation*: If the input domain is uncertain, replace `c - 'a'` with `Character.toLowerCase(c) - 'a'` or use a larger buffer size (e.g., `128` for ASCII).
*   **Integer Overflow**: Since the frequency count is limited by the length of the string, and the return type is `int`, there is no risk of overflow provided $N \leq 2^{31}-1$.

---
