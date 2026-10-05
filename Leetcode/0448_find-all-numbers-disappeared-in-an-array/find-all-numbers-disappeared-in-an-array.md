# find-all-numbers-disappeared-in-an-array

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: In-Place Cyclic Marking Algorithm

## 1. Summary
The `findDisappearedNumbers` solution addresses the problem of identifying missing integers in an array of size $N$ where elements are in the range $[1, N]$. Instead of utilizing auxiliary data structures (like HashSets) which would incur $O(N)$ space complexity, this approach leverages the input array itself as a **bit-set/frequency map** via sign-bit manipulation. By treating the absolute value of each element as an index, the algorithm marks indices as "visited" by negating the values found at those locations.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **First Pass:** The algorithm iterates through the array exactly once to perform negations. Each lookup and negation is an $O(1)$ operation.
*   **Second Pass:** The algorithm iterates through the array once more to verify which indices remain positive.
*   Total: $O(N) + O(N) = O(N)$, where $N$ is the number of elements in `nums`.

### Space Complexity: $O(1)$ (Auxiliary)
*   The algorithm operates **in-place**. It modifies the input array directly and does not allocate any additional data structures proportional to the input size (excluding the output `List` required by the method signature).
*   *Note:* In strict systems programming contexts, if modifying the input array is forbidden, the space complexity would revert to $O(N)$ to create a copy.

## 3. Component Deep Dive

### Sign-Bit Manipulation Logic
The core mechanism relies on the mathematical properties of indices $[0, N-1]$ mapping to values $[1, N]$. 
*   **Target Index:** `Math.abs(currentNumber) - 1`
*   **Negation:** `nums[index] = -Math.abs(nums[index])`
    *   Using `Math.abs()` on the right-hand side is critical. If we simply performed `nums[index] *= -1`, an element that was previously marked (already negative) would become positive again, effectively "unmarking" it and resulting in false negatives. By forcing the value to be negative regardless of its current state, we maintain the "visited" status idempotently.

### The Two-Pass Strategy
1.  **Phase 1 (Marking):** We treat the array as a map. A negative sign at `nums[i]` acts as a boolean flag `true` (meaning `i + 1` exists in the input).
2.  **Phase 2 (Collection):** We perform a linear scan. Any index `i` where `nums[i] > 0` implies that no element in the original array was mapped to index `i`. Therefore, the value `i + 1` is absent.

### Edge-Case Handling
*   **Empty Array:** The loops will not execute; returns an empty `ArrayList` (Correct behavior).
*   **Duplicates:** The logic handles duplicates gracefully. Multiple instances of the same number will redundantly mark the same index as negative, which does not interfere with the final verification.
*   **Fully Populated:** If all numbers $1 \dots N$ are present, all indices will be negated; the second pass will find no positive numbers.

## 4. Key Insights

*   **Idempotency Constraint:** The use of `Math.abs(nums[index])` is the most subtle and important part of this implementation. Without it, the algorithm would flip signs back and forth if an index is visited multiple times, leading to non-deterministic results.
*   **Data Integrity:** This solution is **destructive**. It leaves the input array in a mutated state. In a multi-threaded or long-running service, if the original order/values of the array are required later in the execution flow, you must either clone the array beforehand or perform a secondary pass to revert the signs (re-negating the elements), which adds $O(N)$ cycles.
*   **Cache Locality:** Since this approach performs linear passes and accesses memory in a predictable way (the `index` values are within the range $[0, N-1]$), it exhibits excellent spatial locality, making it highly efficient on modern CPU architectures compared to pointer-heavy data structures like HashMaps or Linked Lists.
*   **Constraint Dependency:** This algorithm is strictly dependent on the constraint $1 \le nums[i] \le N$. If the range were arbitrary or the array contained zeros/negatives, the index-mapping strategy would require a transformation layer (e.g., coordinate compression) or would fail entirely.

---
