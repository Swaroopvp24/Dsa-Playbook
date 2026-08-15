# reverse-string

## two_pointer.java
*Style: detailed*

# Deep-Dive Reference: In-Place Array Reversal

## Summary
The provided solution implements a **Two-Pointer In-Place Reversal** algorithm. By maintaining two indices—one starting at the lower bound (`st`) and one at the upper bound (`en`)—the algorithm converges toward the center of the character array. At each iteration, the elements at these pointers are swapped using a temporary variable, effectively transforming the array into its reverse representation without allocating additional heap memory proportional to the input size.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm performs a single pass over the array. Since the pointers `st` and `en` converge linearly toward the center, the total number of swaps is $\lfloor N/2 \rfloor$, where $N$ is the length of the array.
*   **Efficiency:** Each operation within the `while` loop (assignment, increment/decrement) is $O(1)$. Thus, $O(N/2)$ simplifies to $O(N)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm operates strictly in-place. The space consumed by variables `st`, `en`, and `temp` remains constant regardless of the input array size $N$. 
*   **Requirement:** This satisfies the "constant space" constraint, making it highly memory-efficient for large buffers.

---

## Component Deep Dive

### 1. Pointer Logic (`st` and `en`)
*   **`st` (Start):** Initialized to index `0`. Tracks the head of the unswapped segment.
*   **`en` (End):** Initialized to `s.length - 1`. Tracks the tail of the unswapped segment.
*   **Termination Condition (`st < en`):** The loop terminates when the pointers meet or cross. 
    *   **Odd-length arrays:** When `st == en`, the pointers rest on the middle element, which does not require swapping (as it is already in its correct position).
    *   **Even-length arrays:** When `st > en`, all pairs have been swapped.

### 2. The Swap Mechanism
The swap uses a standard temporary variable (`temp`) approach:
```java
char temp = s[st];
s[st] = s[en];
s[en] = temp;
```
*   **Edge Case Handling:**
    *   **Empty array (`s.length == 0`):** The condition `0 < -1` is false; the loop is skipped, returning correctly without error.
    *   **Single-element array (`s.length == 1`):** The condition `0 < 0` is false; the loop is skipped, which is correct as a single element is its own reverse.
    *   **Null inputs:** While not handled explicitly, this would throw a `NullPointerException`. In a production environment, an `if (s == null) return;` check is standard practice.

---

## Key Insights

### Architectural Nuance: Memory Locality
Because this algorithm modifies the array in-place, it exhibits high **spatial locality of reference**. In hardware terms, the CPU cache performs well with this pattern because it accesses the beginning and end of the array repeatedly, keeping the cache lines containing `s` active in the L1/L2 cache until the middle of the array is reached.

### Potential Optimization (Micro-level)
*   **XOR Swap:** One could perform the swap using XOR bitwise operations:
    ```java
    s[st] ^= s[en];
    s[en] ^= s[st];
    s[st] ^= s[en];
    ```
    *   *Warning:* While this saves the `temp` variable, modern JVM JIT compilers often optimize the temporary variable swap into a single `xchg` CPU instruction. The XOR swap can actually be *slower* due to increased instruction count and dependency chains that hinder superscalar execution. Stick to the `temp` variable for readability and JVM performance.

### Subtle Considerations
*   **Character Encodings:** This implementation assumes the input is a standard primitive `char` array (UTF-16 in Java). It performs a "code unit" reversal. If the string contains multi-code-unit surrogate pairs (e.g., specific emojis), this algorithm will **corrupt** the characters by swapping the high and low surrogates incorrectly. For production systems handling arbitrary Unicode, consider using `String` and `StringBuilder` or handling surrogate pairs explicitly.

---
