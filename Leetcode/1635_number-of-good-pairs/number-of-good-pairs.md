# number-of-good-pairs

## attempt_1.java
*Style: detailed*

# Technical Deep Dive: Identical Pairs Counting

## Summary
The solution employs a **Single-Pass Frequency Counting** technique to identify the total number of "good pairs" $(i, j)$ such that `nums[i] == nums[j]` and $i < j$. Instead of a brute-force $O(N^2)$ comparison, the algorithm leverages the mathematical property of combinations. For any number $x$ that has appeared $k$ times previously, the $k+1$-th appearance of $x$ forms exactly $k$ new pairs with all preceding occurrences. By accumulating these values, we derive the total count in linear time.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm iterates through the input array `nums` exactly once. Inside the loop, `HashMap.getOrDefault()` and `HashMap.put()` operations execute in amortized $O(1)$ time. 
*   **Result:** $O(N \cdot 1) = O(N)$, where $N$ is the number of elements in the array.

### Space Complexity: $O(K)$
*   **Derivation:** The space requirement is dictated by the `frequencyMap`, which stores the frequency of unique integers encountered. 
*   **Result:** $O(K)$, where $K$ is the number of **unique** elements in `nums`. In the worst case (all elements unique), $K = N$; in the best case (all elements identical), $K = 1$.

---

## Component Deep Dive

### 1. The Accumulation Logic
The core mechanism is: `goodPairs += previousCount`. 
Mathematically, if a number appears $n$ times, the total number of pairs is $\binom{n}{2} = \frac{n(n-1)}{2}$. The code computes this incrementally:
*   1st occurrence: `previousCount` is 0; `goodPairs` adds 0.
*   2nd occurrence: `previousCount` is 1; `goodPairs` adds 1 (Total: 1).
*   3rd occurrence: `previousCount` is 2; `goodPairs` adds 2 (Total: 1 + 2 = 3).
*   $n$-th occurrence: `previousCount` is $(n-1)$; `goodPairs` adds $(n-1)$.
Summing $0 + 1 + 2 + \dots + (n-1)$ equals $\frac{n(n-1)}{2}$, confirming the incremental logic is equivalent to the combinatorial formula.

### 2. Data Structure Selection: `HashMap`
*   **Suitability:** `HashMap<Integer, Integer>` is used to track frequency. Given that the constraints on the *value* of the integers are typically not defined, `HashMap` is safer than a fixed-size integer array (`int[101]`), which would rely on implicit assumptions about the range of input values.
*   **Performance:** While `HashMap` has higher constant-time overhead than a primitive array, it provides O(1) average lookup and insertion, maintaining the linear complexity requirement.

### 3. Edge-Case Handling
*   **Empty Array/Single Element:** If `nums.length < 2`, the loop completes without adding to `goodPairs`, correctly returning 0.
*   **Integer Overflow:** The current logic uses an `int` for `goodPairs`. Since the maximum number of pairs for an array of size $N$ is $\frac{N(N-1)}{2}$, if $N = 10^5$, the result would be $\approx 5 \times 10^9$. **Note:** This exceeds the range of a 32-bit signed integer ($2.14 \times 10^9$). In production environments with large constraints, `goodPairs` should be promoted to a `long`.

---

## Key Insights

*   **Single-Pass vs. Post-Processing:** An alternative approach would be to calculate frequencies using a pass, then use a loop over the entry set to calculate $\frac{n(n-1)}{2}$ for each. The current single-pass approach is superior as it reduces the number of operations and avoids the need for an additional iteration.
*   **Performance Nuance:** The overhead of `HashMap` boxing (converting `int` to `Integer` objects) can be significant in high-frequency, low-latency systems. If the range of `nums[i]` is known and small (e.g., $1 \le nums[i] \le 100$), a `int[]` frequency array would significantly outperform the `HashMap` in both memory and CPU cycles by avoiding object allocation and hash collisions.
*   **Memory Pressure:** For massive inputs, the `HashMap` will cause frequent GC (Garbage Collection) pauses due to the creation of `Entry` objects. If memory overhead is a concern, sorting the array $O(N \log N)$ and counting contiguous blocks would allow for $O(1)$ auxiliary space at the cost of execution time.

---
