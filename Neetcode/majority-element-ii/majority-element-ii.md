# majority-element-ii

## attempt_1.java
*Style: detailed*

# Engineering Reference: Majority Element II Implementation

## 1. Summary
The provided implementation solves the "Majority Element II" problem using a **Frequency Counting Map** pattern. The objective is to identify all elements in an array that appear more than $\lfloor n/3 \rfloor$ times. The algorithm utilizes a `HashMap` to perform a linear scan of the input, aggregating occurrences, followed by a filter pass to evaluate the frequency threshold.

This approach prioritizes implementation simplicity and readability over space efficiency. While effective for general use cases, it deviates from the constant-space (Boyer-Moore Voting Algorithm) variant often required in high-performance or memory-constrained system environments.

---

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Counting Pass:** The algorithm performs a single traversal of the input array `nums`. Each insertion into the `HashMap` provides amortized $O(1)$ time complexity.
*   **Evaluation Pass:** The algorithm iterates over the `entrySet` of the `HashMap`. Since the number of keys is at most $N$ (or more accurately, the number of distinct elements), this pass is $O(N)$.
*   **Total:** $O(N) + O(N) = O(N)$, where $N$ is the number of elements in the array.

### Space Complexity: $O(N)$
*   **Storage:** The `HashMap` stores up to $N$ distinct keys in the worst-case scenario (e.g., all elements are unique). 
*   **Total:** $O(N)$.

---

## 3. Component Deep Dive

### HashMap Frequency Aggregator
The core logic relies on `Map.put()` coupled with `Map.getOrDefault()`.
*   **Performance Nuance:** The `HashMap` in Java involves object wrapping for primitive integers (`int` -> `Integer`). This leads to autoboxing overhead, which can be significant in tight memory profiles or extremely large datasets. 
*   **Capacity Considerations:** As the map grows, internal re-hashing occurs to maintain a load factor (default 0.75). For large $N$, pre-sizing the map using `new HashMap<>(initialCapacity)` can reduce re-hash latency.

### Threshold Evaluation
The use of `Math.floor(nums.length / 3)` is mathematically equivalent to integer division `nums.length / 3` in Java.
*   **Edge Case Handling:**
    *   **Empty Arrays:** If `nums` is empty, the loop terminates immediately, returning an empty `ArrayList` (Correct).
    *   **Single Elements:** If `nums = [1]`, the count is 1, `1 > (1/3)` is true, returning `[1]` (Correct).
    *   **Multiple Majority Elements:** By definition of the Pigeonhole Principle, there can be at most two elements appearing more than $N/3$ times. This implementation handles zero, one, or two elements correctly.

---

## 4. Key Insights & Engineering Recommendations

### Optimization: Boyer-Moore Voting Algorithm
While the provided `HashMap` solution is functionally correct and readable, it is **not optimal for memory**. A Senior Staff Engineer should recognize that this can be solved in **$O(1)$ Space** using the Boyer-Moore Voting Algorithm variant:
1.  Maintain two counters and two candidates.
2.  Perform a two-pass approach:
    *   **Pass 1:** Identify potential candidates (the only candidates who could possibly appear $> N/3$ times).
    *   **Pass 2:** Verify the actual counts of these two candidates.
*   *Why use this?* It eliminates the $O(N)$ space overhead and avoids the performance penalties of `HashMap` autoboxing and re-hashing.

### Subtle Bugs / Edge Cases to Watch
*   **Floating Point vs. Integer:** While `Math.floor()` is explicitly used here, be cautious of floating-point precision issues in more complex logic. In Java, `int` division is truncated towards zero, which is functionally equivalent to `floor` for positive integers.
*   **Concurrent Access:** The `HashMap` is not thread-safe. If this service is exposed to concurrent modifications, a `ConcurrentHashMap` or a synchronized wrapper would be necessary, though it would incur a performance penalty.
*   **Input Constraints:** If the input array `nums` is extremely large (e.g., streaming data), the `HashMap` approach may trigger `OutOfMemoryError`. In such cases, the space-optimized streaming variant (Boyer-Moore) is strictly required. 

### Final Verdict
The current code is excellent for **readability and maintainability**. If this is part of a non-latency-critical API, keep this implementation. If this is part of a core processing engine where $N > 10^7$, refactor to the Boyer-Moore Voting algorithm to save significant heap space and reduce GC pressure.

---
