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

## attempt_1_Boyer-Moore majority vote idea generalized to n/3.java
*Style: detailed*

# Engineering Reference: Boyer-Moore Majority Vote (Generalization)

## Summary
The provided solution implements a specialized variation of the **Boyer-Moore Voting Algorithm** designed to identify all elements in an array that appear more than $\lfloor n/3 \rfloor$ times. 

While the standard Boyer-Moore algorithm tracks a single candidate for a majority element (> 50%), this generalization tracks two candidates. Mathematically, there can be at most two elements that appear more than $1/3$ of the time in any given sequence. The algorithm functions as a stream-processing technique that performs a "three-way cancellation": if we encounter three distinct elements, we decrement the counts of the two current candidates. This effectively "evicts" elements that cannot be part of the majority set.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Pass 1 (Candidate Selection):** The algorithm performs a single linear scan through the input array $O(N)$. During each iteration, constant time arithmetic and conditional logic are applied.
*   **Pass 2 (Verification):** A second linear scan is required to count the actual occurrences of the candidates identified in the first pass, totaling $O(N)$.
*   **Total:** $2 \times O(N) = O(N)$.

### Space Complexity: $O(1)$
*   The algorithm utilizes a fixed number of integer primitives (`ct1`, `ct2`, `cand1`, `cand2`) regardless of the input size $N$. 
*   The space allocated for the result list is at most 2 elements, which is constant relative to the input array size.

## Component Deep Dive

### 1. The Multi-Candidate Selection Logic
The loop logic is prioritized to ensure correctness:
1.  **Direct Match:** If the incoming number matches `cand1` or `cand2`, increment the respective counter. This reinforces existing candidates.
2.  **Allocation:** If a counter is zero, we "adopt" a new candidate. This is the mechanism by which we replace stale, exhausted candidates with new contenders.
3.  **Cancellation:** If the number matches neither candidate and both counters are positive, we decrement both. This is the core "voting" step: effectively saying, "these three distinct elements (the two candidates and the current number) cancel each other out."

### 2. The Verification Phase
The algorithm is a heuristic that identifies *potential* candidates. Because the cancellation logic can leave artifacts in the counters, the candidates are not guaranteed to be majority elements (e.g., in `[1, 2, 3]`, candidates might end up as 2 and 3 with counts of 1, but they are not > 1/3 of the length). The second pass is mandatory to confirm the condition $\text{count} > N/3$.

### 3. Edge-Case Handling
*   **Empty Arrays:** The loop simply does not run; `res` returns empty, which is correct.
*   **Single Element:** `cand1` is set to `nums[0]`, `ct1` becomes 1. The second pass verifies $1 > 1/3$, adding it to the list.
*   **Duplicates:** The logic handles duplicates via the `n == cand` check at the start of the loop, ensuring the count increases rather than hitting the cancellation logic.

## Key Insights

### The "Stale Candidate" Risk
The most common mistake when implementing this is failing to reset the candidates before the second pass. The candidates identified in the first pass are merely placeholders; the `ct1` and `ct2` values are discarded, and the frequencies are recalculated from scratch against the entire array.

### Why exactly two candidates?
This is based on the **Pigeonhole Principle**. If you divide an array into $k$ buckets, there can be at most $k-1$ elements that appear more than $1/k$ times. For $1/3$, $k=3$, so there are at most $3-1 = 2$ such elements. If the requirement were to find elements occurring $> 1/4$ of the time, the algorithm would need to track 3 candidates and perform 4-way cancellation.

### Performance Nuance
Note the order of the `if-else` chain. It is critical to check for matching candidates (`n == cand1`) *before* checking if a counter is zero. If you checked `ct1 == 0` first, a valid candidate that temporarily hits a count of 0 (due to being part of a cancellation triplet) might be erroneously swapped out for a different number.

---
