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

## Dutch_national_flag_algorithm.java
*Style: detailed*

# Engineering Reference: Dutch National Flag (DNF) Algorithm

## Summary
The provided solution implements Edsger Dijkstra’s **Dutch National Flag algorithm**. It performs a single-pass, in-place sort of an array containing only three distinct values (0, 1, 2), representing colors (e.g., Red, White, Blue). Unlike counting sort, which requires two passes (one to count frequencies, one to overwrite), this approach uses a **three-pointer partition scheme** to segregate elements into three contiguous segments: `[0...l-1]` (zeros), `[l...m-1]` (ones), and `[h+1...n-1]` (twos), with the pointer `m` scanning the unknown region `[m...h]`.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm utilizes a single `while` loop that iterates through the array. Although elements are swapped, each element is visited and processed a constant number of times. Specifically, every index from `0` to `n-1` is finalized exactly once, and pointer `m` monotonically increases toward `h`, while `h` monotonically decreases toward `l`.

### Space Complexity: $O(1)$
*   **Reasoning:** The implementation is strictly in-place. It utilizes three integer pointers (`l`, `m`, `h`) regardless of the input array size. No auxiliary data structures (stacks, hash maps, or secondary arrays) are allocated.

---

## Component Deep Dive

### 1. Pointer Semantics
*   **`l` (Low):** Points to the boundary where the next `0` should be placed. Everything to the left is confirmed `0`.
*   **`m` (Mid):** The "explorer" pointer. It inspects the current value. Everything between `l` and `m-1` is confirmed `1`.
*   **`h` (High):** Points to the boundary where the next `2` should be placed. Everything to the right is confirmed `2`.

### 2. Logic Flow & State Transitions
*   **`nums[m] == 0`:** The element belongs at the front. Swapping with `l` pushes a known `1` (or `0`) to `m`. Since we know the value swapped from `l` into `m` is either `0` or `1`, both `l` and `m` can safely increment.
*   **`nums[m] == 2`:** The element belongs at the end. Swapping with `h` sends the `2` to the back. **Crucially**, we do not increment `m` after this swap. The value swapped from `h` into `m` is unknown; it must be evaluated in the next iteration to determine if it is a `0`, `1`, or `2`.
*   **`nums[m] == 1`:** The element is correctly positioned in the middle region. We simply increment `m`.

### 3. Edge-Case Handling
*   **Empty Arrays / Single Element:** The loop condition `m <= h` correctly handles cases where `n=0` (loop never enters) or `n=1` (loop executes once, no swaps needed), preventing index out-of-bounds.
*   **Already Sorted / Reverse Sorted:** The logic holds for boundary scenarios like all `0`s, all `2`s, or mixed arrays, as the partition boundaries naturally collapse to the edges.

---

## Key Insights

### Why `m` doesn't increment on a `2` swap
This is the most common pitfall in DNF implementations. When `nums[m] == 2`, the value at `nums[h]` (which is swapped into `nums[m]`) has not been examined by the algorithm yet. If we were to increment `m` blindly, we would skip the validation of the incoming value, potentially leaving a `0` or `2` in the "ones" partition.

### Stability
It is important to note that this algorithm is **not stable**. Because it performs long-distance swaps to place elements, the relative order of identical elements is not preserved. If stability were required, a stable multi-pass approach (like counting sort) would be necessary.

### Optimization Nuance
The `swap` function uses a temporary variable. While some developers might use XOR-based swapping to avoid the `temp` variable, modern JVM JIT compilers optimize the `temp` variable approach effectively. Furthermore, the `temp` approach avoids potential issues with XOR-swapping if `nums[i]` and `nums[j]` point to the same memory address (though the loop logic here inherently prevents `i == j` during the `nums[m] == 0` case).

---
