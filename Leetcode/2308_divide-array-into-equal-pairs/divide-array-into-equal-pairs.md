# divide-array-into-equal-pairs

## attempt_1.java
*Style: detailed*

# Technical Reference: Array Partitioning by Frequency

## Summary
The solution employs a **frequency counting (bucket sort derivative)** approach to determine if an array of size $N$ can be partitioned into $N/2$ pairs of equal elements. 

The underlying algorithmic principle is the **Parity Requirement Constraint**: for any collection of items to be fully partitioned into pairs, every unique element must possess an even frequency count. This reduces the problem from an $O(N \log N)$ sorting or $O(N)$ hash-map problem to a constant-space $O(N)$ linear scan, leveraging the constrained input range of the elements ($[1, 500]$).

---

## Complexity Analysis

### Time Complexity: $O(N + K)$
*   **$N$**: The number of elements in the input array. We perform a single pass to populate the frequency array: $O(N)$.
*   **$K$**: The range of the input values (fixed at 500 in this implementation). The final validation pass iterates exactly $K$ times: $O(K)$.
*   **Total**: Given $K$ is a constant, the complexity is effectively **$O(N)$**.

### Space Complexity: $O(K)$
*   We allocate a fixed-size integer array `frequency` of size 501. 
*   Because the space used does not scale with $N$, the space complexity is **$O(1)$** (or $O(K)$ in terms of the range of input values). This is highly memory-efficient compared to using a `HashMap<Integer, Integer>`, which would incur object overhead and pointer chasing.

---

## Component Deep Dive

### 1. Frequency Distribution Array
Instead of utilizing a `HashMap` (which would introduce $O(N)$ space overhead and hashing latency), the implementation uses a primitive `int[]` array. This provides:
*   **Cache Locality**: The array resides in a contiguous memory block, minimizing cache misses during the frequency accumulation phase.
*   **Branch Prediction**: The tight loop for counting is highly predictable for modern CPU branch predictors.

### 2. Parity Validation Logic
The core logic relies on `frequency[number] % 2 != 0`. 
*   **Mechanism**: If any frequency is odd, at least one instance of that number remains unpaired after forming pairs, making a complete partition impossible. 
*   **Early Exit**: The function returns `false` as soon as the first odd frequency is encountered, preventing unnecessary computation if the condition fails early.

### 3. Edge-Case Handling
*   **Empty Arrays**: The logic implicitly returns `true` (loop doesn't execute, frequency array is all zeros). *Note: In a real-world scenario, you should verify if an empty set constitutes a valid partition per problem requirements.*
*   **Input Size Constraints**: The algorithm assumes `nums.length` is even (as implied by the problem nature of pairing). If `nums.length` is odd, the `frequency` array would theoretically still identify the odd count, but an initial check `if (nums.length % 2 != 0) return false;` could optimize the path further.

---

## Key Insights

*   **Fixed Range Exploitation**: The efficiency of this solution is entirely dependent on the constraint that $1 \leq nums[i] \leq 500$. If the range of values were significantly larger (e.g., $10^9$), this approach would trigger `OutOfMemoryError` or require a shift to a `HashMap`.
*   **Memory Overhead**: By using `new int[501]`, we allocate ~2KB of memory. While trivial here, in high-throughput systems, using a static `ThreadLocal` or re-using a buffer can reduce GC pressure if this function is called in a tight loop.
*   **Primitive vs. Object**: Avoiding `Integer` boxing/unboxing is critical. The use of primitive `int` arrays ensures that we are not performing heap allocations for map entry objects, keeping the runtime strictly bounded by CPU cycles rather than memory allocation speed.
*   **Potential Optimization**: If the input `nums` length is very small and the number of distinct elements is even smaller, sorting the array (`Arrays.sort(nums)`) might be faster due to hardware-level optimizations in `DualPivotQuicksort`, despite the $O(N \log N)$ complexity, as it would avoid the secondary loop over the 500-element array. However, for $N > 100$, the $O(N)$ counting approach is strictly superior.

---
