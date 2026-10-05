# number-of-good-pairs

## attempt_1.java
*Style: detailed*

# Deep-Dive Technical Reference: Identical Pairs Counting

## Summary
The solution implements a **Single-Pass Frequency Counting** approach to identify "good pairs" (indices $i < j$ such that $nums[i] == nums[j]$). Instead of the brute-force $O(n^2)$ approach of checking every combination, this algorithm leverages the mathematical property of combinations: if a number has appeared $k$ times previously, the current instance can form $k$ new pairs with those existing occurrences. By maintaining a running frequency map, we compute the total count in linear time.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm iterates through the input array `nums` exactly once. 
*   **Operations:** Inside the loop, `HashMap.getOrDefault()` and `HashMap.put()` are $O(1)$ on average. Therefore, for an array of size $n$, the total time complexity is $O(n)$.
*   **Worst Case:** In scenarios with high hash collisions (rare with `Integer` keys in Java), the complexity could theoretically degrade to $O(n \cdot m)$ where $m$ is the chain length, but for standard inputs, $O(n)$ holds.

### Space Complexity: $O(k)$
*   **Derivation:** We utilize a `HashMap` to store counts.
*   **Constraint:** The space complexity is $O(k)$, where $k$ is the number of **unique** elements in the input array. 
*   **Limit:** In the absolute worst case (all elements unique), $k = n$, resulting in $O(n)$ space. In cases where the range of numbers is small and fixed (e.g., $1 \le nums[i] \le 100$), this effectively becomes $O(1)$ auxiliary space if replaced by a fixed-size integer array.

---

## Component Deep Dive

### 1. Mathematical Induction
The core logic relies on the accumulation of valid pairs:
*   When we encounter a number for the $n$-th time, it creates exactly $n-1$ new pairs.
*   By adding `previousCount` to `goodPairs` *before* incrementing the map, we avoid double-counting or needing to calculate combinations via $\frac{n(n-1)}{2}$ at the end. This is a "streaming" approach to combinatorics.

### 2. Hash Map Mechanism
*   **`getOrDefault(number, 0)`**: This is critical for handling the first occurrence of any integer. It avoids null-pointer exceptions and explicit `contains` checks, keeping the loop body clean and performant.
*   **State Management**: By updating the map *after* reading the `previousCount`, the algorithm correctly maintains the invariant that the map always represents the state of the array indices $0$ to $i-1$.

### 3. Edge Case Handling
*   **Empty Array**: The loop does not execute; `goodPairs` remains $0$, which is the correct mathematical result.
*   **Single Element**: The loop executes once, `previousCount` is $0$, `goodPairs` remains $0$. Correct.
*   **Duplicates**: The logic holds for any frequency $n > 1$. For example, if a number appears three times:
    1. First encounter: `previousCount` = 0, `goodPairs` = 0, map = {x: 1}
    2. Second encounter: `previousCount` = 1, `goodPairs` = 1, map = {x: 2}
    3. Third encounter: `previousCount` = 2, `goodPairs` = 1 + 2 = 3. 
    Matches the combination formula $3C2 = 3$.

---

## Key Insights

*   **Optimization Opportunity (Range Constraint)**: If the problem constraints specify that $nums[i]$ falls within a small, known range (e.g., $1 \le nums[i] \le 100$), replace the `HashMap<Integer, Integer>` with a primitive `int[]` array of size 101. This removes the hashing overhead, improves cache locality, and reduces object allocation pressure, significantly speeding up execution.
*   **Integer Overflow**: In extreme cases where $n$ is very large (e.g., $n = 10^5$ and all elements are identical), the number of pairs could reach $\approx 5 \times 10^9$. The current implementation uses an `int` for `goodPairs`, which would overflow (max $2.14 \times 10^9$). **Recommendation:** Use `long` for the `goodPairs` accumulator if the input size exceeds $65,535$ identical elements.
*   **Memory Locality**: The `HashMap` stores `Integer` objects (boxed), causing heap fragmentation. Using a primitive-based collection library (like fastutil or Trove) would be more performant for memory-constrained, high-throughput systems.

---
