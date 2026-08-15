# reverse-string

## two_pointer.java
*Style: detailed*

# Deep-Dive Reference: In-Place Array Reversal

## Summary
The provided solution utilizes the **Two-Pointer Technique** to reverse a character array in-place. By maintaining two indices—one starting at the lower bound (`st`) and one at the upper bound (`en`)—the algorithm systematically swaps elements while converging toward the center of the array. This approach is optimal for memory-constrained environments, as it avoids auxiliary data structures.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm performs a single pass over the array. The pointers `st` and `en` move toward each other, meeting at the midpoint.
*   **Operations:** Total number of swaps performed is $\lfloor N/2 \rfloor$. Since each swap is an $O(1)$ operation, the overall time scales linearly with the length of the input array.

### Space Complexity: $O(1)$
*   **Derivation:** The reversal is performed strictly in-place. 
*   **Auxiliary Space:** Only a single primitive `char` variable (`temp`) is used for the swap operation. No recursive stack frames or additional data structures (like a new array or `StringBuilder`) are allocated, resulting in constant auxiliary space complexity.

---

## Component Deep Dive

### 1. Pointer Initialization
*   `st = 0`: Points to the first element (index `0`).
*   `en = s.length - 1`: Points to the last element. This correctly handles edge cases such as single-character arrays (where `st == en`, preventing unnecessary swaps) and empty arrays (where `st > en`, bypassing the loop entirely).

### 2. The Swap Logic
The implementation uses a classic temporary variable swap:
```java
char temp = s[st];
s[st] = s[en];
s[en] = temp;
```
*   **Memory Safety:** Because `char` is a primitive type in Java, the swap does not involve object references, ensuring zero overhead from the Garbage Collector.
*   **Loop Invariant:** The invariant `s[0...st-1]` and `s[en+1...N-1]` are already reversed relative to the original state, and the final state is reached when `st >= en`.

### 3. Edge Case Handling
*   **Null Inputs:** The current implementation assumes `s` is non-null. If passed a `null` reference, a `NullPointerException` will be thrown. A production-grade implementation would typically include a `if (s == null) return;` guard clause.
*   **Empty/Single-element arrays:** 
    *   `s.length == 0`: `st` (0) is not `<` `en` (-1), loop terminates.
    *   `s.length == 1`: `st` (0) is not `<` `en` (0), loop terminates.
    *   Both are handled gracefully without modification.

---

## Key Insights

### Performance Optimization & CPU Cache
Because the array is modified in-place, this solution exhibits excellent **spatial locality**. When the CPU loads segments of the array into the L1/L2 cache, the two pointers interact with the cache lines effectively. Compared to allocating a new array, this minimizes cache misses and avoids the overhead of memory allocation.

### Alternative Approaches (Why not XOR?)
A common optimization in low-level languages (like C) is the bitwise XOR swap (avoiding a `temp` variable). 
*   *Caveat:* In Java, the XOR swap approach is discouraged because it is less readable and offers **zero performance gain**. Modern JVMs (JIT compilers) identify the standard `temp` variable swap and optimize it into a single register swap instruction. XORing would involve unnecessary CPU cycles and potentially obscure the code's intent.

### Thread Safety Note
While this method is efficient, it is **not thread-safe**. If the array reference is shared across threads, callers must implement external synchronization (e.g., `synchronized` blocks or `ReadWriteLock`) to prevent race conditions during the reversal process.

---
