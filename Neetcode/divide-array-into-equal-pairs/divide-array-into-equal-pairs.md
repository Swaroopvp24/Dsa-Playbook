# divide-array-into-equal-pairs

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Array Partitioning via Frequency Parity

## 1. Summary
The provided solution addresses the problem of partitioning an array of length $N$ into pairs of equal elements. The core algorithmic insight is that a valid partition exists **if and only if** every distinct element in the array appears an even number of times.

The implementation utilizes a **Frequency Array (Counting Sort style)** to track occurrences. By leveraging the constrained input space (elements $1 \le nums[i] \le 500$), the algorithm avoids the overhead of hash-based lookups, achieving optimal $O(N)$ time complexity with a minimal, fixed-size auxiliary space.

---

## 2. Complexity Analysis

### Time Complexity: $O(N + K)$
*   **$N$**: The number of elements in the input array. We perform a single linear scan to populate the `frequency` array.
*   **$K$**: The range of possible values (fixed at 500). The second pass iterates through the frequency array. Since $K$ is a constant, this is effectively $O(N)$.
*   **Why**: The operations inside both loops (incrementing a counter and the modulo operator) are $O(1)$. Total operations are roughly $N + 500$.

### Space Complexity: $O(K)$
*   **$K$**: We allocate an integer array of size 501. 
*   **Why**: Regardless of the input size $N$, the auxiliary memory footprint remains constant at approximately 2KB ($501 \times 4$ bytes), classifying this as $O(1)$ relative to the input array size if $K$ is treated as a constant, or $O(K)$ if considering the constraints.

---

## 3. Component Deep Dive

### Frequency Mapping
The `int[] frequency = new int[501]` is a direct address table. This is significantly more performant than a `HashMap<Integer, Integer>` because:
1.  **Cache Locality**: The array is a contiguous block of memory, avoiding pointer chasing and potential cache misses associated with hash collisions or entry object overhead.
2.  **No Hashing Overhead**: It avoids the computational cost of calculating `hash()` functions and resizing buckets.

### Parity Validation
The condition `frequency[number] % 2 != 0` is the mathematical equivalent of checking if a set of objects can be perfectly partitioned into pairs. If the count of any integer is odd, at least one instance will remain unpaired, violating the requirements.

### Edge Case Handling
*   **Array Size**: The solution implicitly handles odd-length input arrays. Since the sum of an odd number of odd integers is odd, an array with an odd length will mathematically fail the parity check (the sum of frequencies equals the array length $N$).
*   **Range Constraints**: The implementation relies on the constraint $1 \le nums[i] \le 500$. If the input contained values outside this range, the code would throw an `ArrayIndexOutOfBoundsException`. In a production environment, this should be guarded by `Math.min`/`Math.max` checks or by using a `HashMap` if the input range is unknown/large.

---

## 4. Key Insights

*   **The "Even Count" Invariant**: This problem is essentially a variation of the "Majority Element" or "Pairing" problems. The insight is that we do not need to physically group the elements or simulate the pairing process; we only need to verify the global property of parity.
*   **Optimization Potential**: 
    *   **Bit Manipulation**: The modulo operator (`% 2`) can be replaced with the bitwise AND operator (`frequency[number] & 1 != 0`) for a marginal performance gain, though the JIT compiler likely optimizes this automatically.
    *   **Early Exit**: One could potentially optimize by tracking a counter of "unpaired" elements while iterating through the first loop. If the count of unpaired elements exceeds the remaining elements in the array, the function could return `false` early. However, given the constraints ($K=500$), this added complexity is usually unnecessary.
*   **Robustness Concerns**: The current implementation is susceptible to input mutation if the constraints on `nums[i]` are violated. If this were a library function, adding an explicit range check before indexing the `frequency` array would prevent runtime crashes and improve API safety.

---
