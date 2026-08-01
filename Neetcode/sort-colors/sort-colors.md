# sort-colors

## attempt_1.java
*Style: detailed*

# Deep-Dive Technical Reference: Three-Way Counting Sort (Dutch National Flag Variant)

## 1. Summary
The provided solution implements a **Two-Pass Counting Sort** algorithm to solve the "Sort Colors" problem. Given that the input set is constrained to a small, fixed range of integers $\{0, 1, 2\}$, the algorithm avoids general-purpose comparison-based sorting (e.g., Quicksort, Mergesort) which would operate at $O(n \log n)$. 

Instead, it treats the problem as a distribution problem:
1. **Pass 1:** Frequency distribution analysis to compute the cardinality of each unique key.
2. **Pass 2:** Reconstructive overwrite based on the computed histogram.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** The algorithm performs exactly two linear passes over the input array of size $N$. 
    *   Pass 1: $N$ iterations to populate the `count` array.
    *   Pass 2: $N$ total operations across the nested loops (the `while` loop condition effectively decrements the count until the total increments reach $N$).
*   Since the number of colors ($K=3$) is constant, the overhead is $O(N + K) \approx O(N)$.

### Space Complexity: $O(1)$
*   **Analysis:** The auxiliary space used is a fixed-size integer array of length 3 (`int[3]`). Because this space requirement is independent of the input size $N$ (i.e., constant), it satisfies the $O(1)$ auxiliary space constraint, even if the input array size grows toward infinity.

## 3. Component Deep Dive

### `int[] count` (Histogram Buffer)
This array acts as a frequency map. By using an index-to-value mapping (index 0 for color 0, index 1 for color 1, etc.), we bypass the need for more complex data structures like `HashMap`, which would introduce overhead (hashing, collision handling, and memory allocation).

### The Reconstruction Logic
```java
int index = 0;
for (int i = 0; i < 3; i++) {
    while (count[i]-- > 0) {
        nums[index++] = index; // Logic: Sequential write
    }
}
```
*   **Mechanism:** The nested loop reconstructs the array in-place. By maintaining a monotonically increasing `index` pointer, we ensure that the array is populated in sorted order without needing a secondary array or a temporary buffer.
*   **Edge Case - Empty Array:** If `nums.length == 0`, the first loop is skipped, `index` remains 0, and the second loop's bounds `i < 3` execute without modifying `nums`, safely returning an empty array.
*   **Edge Case - Uniform Colors:** If all elements are identical (e.g., `[1, 1, 1]`), `count[1]` will be 3, and the inner `while` loop will trigger three times correctly, ensuring `nums` remains unchanged.

## 4. Key Insights

### Performance Nuance: Cache Locality
Because this approach iterates linearly over the `nums` array twice, it is extremely **cache-friendly**. The CPU prefetcher handles the sequential memory access pattern efficiently, minimizing cache misses compared to pointer-heavy algorithms like Quicksort.

### Algorithmic Trade-off: Two-Pass vs. One-Pass
While this solution is $O(N)$, it is a "Two-Pass" algorithm. 
*   **One-Pass Alternative:** The Dutch National Flag problem can be solved in a single pass using a **three-pointer approach** (low, mid, high), swapping elements in place. 
*   **Comparison:** 
    *   *Counting Sort (This solution):* Simpler to reason about and implement, performs fewer total writes if there are many duplicates.
    *   *Three-Pointer:* Performs swaps which can be more expensive than direct assignment if the values are scattered. 
    *   For production systems, the Counting Sort approach is often preferred when the domain of elements is small and known, as it is less prone to implementation errors (like "off-by-one" index swaps in the three-pointer variant).

### Subtle Risks
*   **Non-Stability:** While this solution is technically stable for these specific values (since all 0s, 1s, and 2s are indistinguishable), it is worth noting that if the objects being sorted carried secondary data (e.g., `Color object {id, value}`), this specific implementation would **not** preserve the original order of objects with the same color, as it overwrites values based on the count rather than shifting existing elements.

---
