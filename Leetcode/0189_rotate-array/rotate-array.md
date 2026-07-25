# rotate-array

## attempt_1.java
*Style: detailed*

## Deep-Dive Technical Reference: Array Rotation (Buffer-Based)

### Summary
The provided solution implements a **cyclic shift** of an array using an auxiliary buffer (the `temp` array). The core algorithmic technique relies on the observation that a rotation by $k$ positions transforms an array into two contiguous segments:
1. The "tail" segment of length $k$ (elements from index $n-k$ to $n-1$).
2. The "head" segment of length $n-k$ (elements from index $0$ to $n-k-1$).

By mapping the tail to the start of the `temp` buffer and the head to the remainder, the algorithm effectively performs a linear reordering. This is a **space-time tradeoff** approach where memory is traded to achieve $O(n)$ time complexity, favoring readability and simplicity over in-place space optimization.

---

### Complexity Analysis

*   **Time Complexity:** $O(n)$
    *   The algorithm performs three distinct linear passes over the data:
        1. Copying the tail $k$ elements to `temp`.
        2. Copying the head $n-k$ elements to `temp`.
        3. Copying the entire `temp` array back to `nums`.
    *   Total operations: $k + (n - k) + n = 2n$, resulting in $O(n)$.
*   **Space Complexity:** $O(n)$
    *   The solution allocates a secondary array `temp` of size $n$ to hold the intermediate rotated state. This is a non-in-place approach, making it suboptimal for extremely memory-constrained environments (e.g., embedded systems) where $O(1)$ space algorithms (like the reversal algorithm) are preferred.

---

### Component Deep Dive

#### 1. The Modulo Operator (`k = k % n`)
This is the most critical line for robustness. 
*   **Purpose:** It handles cases where $k \ge n$. Since rotating an array of size $n$ by $n$ positions results in the original array, the effective rotation is always `k % n`.
*   **Edge Case:** If $k=0$ or $k=n$, the modulo ensures $k=0$, resulting in an identity transformation (no change), which is handled correctly by the logic.

#### 2. The Pointer Logic
The algorithm maintains two pointers, `i` (index for `temp`) and `pt` (index for `nums`):
*   **Pass 1 (Tail copy):** `pt` starts at `n-k` and runs to `n-1`. These elements are correctly shifted to the front of the new array.
*   **Pass 2 (Head copy):** `pt` resets to `0` and runs to `n-k-1`. The `i` pointer continues from where it left off (at `k`), filling the remainder of the array.
*   **Synchronization:** The use of `i++` and `pt++` within the `while` loops ensures that indices are incremented monotonically, preventing index-out-of-bounds errors.

---

### Key Insights & Nuances

*   **Memory Allocation:** The allocation `new int[n]` triggers a heap allocation. In a high-throughput, latency-sensitive system, this could lead to frequent Garbage Collection (GC) pressure if the `rotate` method is called repeatedly on large arrays.
*   **The "Reversal" Alternative:** While this implementation is clean, it is not the most memory-efficient. A "Reversal Algorithm" (reversing the whole array, then the two segments) can achieve the same result in **$O(1)$ extra space**. 
    *   *Decision Criteria:* Use this buffer-based approach if the code needs to be highly readable or if the JVM's JIT compiler can optimize the array copy (e.g., using `System.arraycopy`) better than multiple manual loops.
*   **Performance Optimization:** 
    *   The final `for` loop `nums[j] = temp[j]` can be replaced with `System.arraycopy(temp, 0, nums, 0, n)`. `System.arraycopy` is a native method that performs a low-level memory block move, typically resulting in significantly higher performance for large `n` due to CPU cache optimization and intrinsic hardware support.
*   **Subtle Bug Risks:** 
    *   If `nums` is `null` or empty, the modulo operation `k % n` will throw an `ArithmeticException` (division by zero). A production-grade implementation should include:
        ```java
        if (nums == null || nums.length == 0) return;
        ``` 
    *   This check is essential for system stability.

---
