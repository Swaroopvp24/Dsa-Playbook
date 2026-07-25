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

## threereverse.java
*Style: detailed*

# Deep-Dive Reference: Array Rotation via Reversal Algorithm

## Summary
The solution implements an **in-place array rotation** with $O(1)$ auxiliary space complexity. The core algorithmic technique relies on the properties of block transposition. Given an array divided into two parts $[A][B]$ (where $B$ is the suffix of length $k$), the goal is to transform it into $[B][A]$.

The transformation sequence follows the algebraic logic:
1. **Reverse Entire Array:** $(AB)^R = B^R A^R$
2. **Reverse Sub-segments:** $(B^R)^R (A^R)^R = BA$

This achieves the rotation without the $O(n)$ space overhead required by auxiliary buffers.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm performs three distinct reversal passes.
    *   Reversing the full array: $n/2$ swaps.
    *   Reversing the first $k$ elements: $k/2$ swaps.
    *   Reversing the remaining $n-k$ elements: $(n-k)/2$ swaps.
*   **Total Operations:** $(n/2) + (k/2) + (n-k)/2 = n$ total swaps. Since each swap is $O(1)$, the total time complexity is strictly linear, $O(n)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm operates exclusively via in-place mutation of the input array. No additional data structures (like queues, stacks, or auxiliary arrays) are initialized. The memory footprint remains constant regardless of input size $N$.

---

## Component Deep Dive

### 1. `rev(int[] arr, int st, int en)`
This utility function implements a bidirectional two-pointer approach to reverse a slice of the array.
*   **In-place Swapping:** The implementation uses the **XOR Swap Algorithm** (`a ^= b; b ^= a; a ^= b;`).
    *   *Technical Note:* While mathematically elegant, this is primarily an educational exercise. In high-performance JVM environments, a temporary variable (`int temp = arr[st]`) is generally preferred. The JIT compiler optimizes temporary variables into CPU registers, whereas XOR swapping forces three sequential read-modify-write operations on the same memory locations, preventing instruction-level parallelism.

### 2. `rotate(int[] nums, int k)`
*   **Normalization:** `k = k % nums.length` is critical. If $k > n$, rotation effectively cycles. Modulo arithmetic ensures $k$ maps to the effective rotation index, preventing `ArrayIndexOutOfBoundsException` and redundant full-array cycles.
*   **Boundary Conditions:**
    *   **$k=0$:** If $k$ is 0, the logic still holds (reverses total array, then reverses $0$ to $-1$ which is a no-op, then reverses $0$ to $n-1$, returning to identity).
    *   **$n=1$:** The logic handles single-element arrays gracefully as $k$ becomes $0$ and no swaps occur.

---

## Key Insights

### The XOR Swap Trap
The use of `a ^= b` creates a hidden dependency chain. In modern pipelined processors, `arr[st]` and `arr[en]` must be accessed sequentially. Using a temporary variable allows the CPU to potentially schedule the reads and writes more efficiently. Furthermore, if `st == en` (the middle element of an odd-length array), the XOR swap will **zero out** the value at that index.
*   *Correction:* While your `while (st < en)` guard prevents this, it is a fragile design pattern. If the boundary condition were ever changed to `st <= en`, the algorithm would corrupt the data.

### Numerical Stability & Constraints
*   **Integer Overflow:** The current implementation is safe as it does not perform arithmetic on values, only indices.
*   **Large Inputs:** For extremely large arrays, the constant factor overhead of three passes is significantly lower than $O(n)$ space allocations, making this the preferred approach for memory-constrained systems (e.g., embedded devices or high-frequency trading buffer rotations).

### Potential Edge Case Failure
If `nums` is `null` or empty, `k % nums.length` will throw an `ArithmeticException`. A robust production implementation must include:
```java
if (nums == null || nums.length <= 1) return;
```
to ensure the method handles degenerate input cases safely.

---
