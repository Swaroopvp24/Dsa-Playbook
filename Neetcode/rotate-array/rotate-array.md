# rotate-array

## using_math(reverse).java
*Style: detailed*

# Engineering Deep-Dive: Array Rotation via Triple Reversal

## Summary
The solution implements an **in-place array rotation algorithm** using a reversal-based strategy. Rather than using an auxiliary buffer (which would require $O(n)$ space) or performing $k$ individual rotations (which would result in $O(n \cdot k)$ time), this approach decomposes the rotation into three distinct reflection operations. By leveraging the algebraic property that reversing a sequence twice restores its original order, we manipulate indices to shift elements to their target positions within the existing memory footprint.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm performs three distinct reversal passes. Let $n$ be the number of elements:
    1.  The first call to `rev` visits $n$ elements.
    2.  The second call visits $k$ elements.
    3.  The third call visits $n - k$ elements.
*   The total number of swaps is exactly $n/2$ (each element is touched once). Thus, the time complexity is $O(n + k + (n-k)) = O(n)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm modifies the input array in-place. We utilize a constant amount of stack space for variables (`temp`, `left`, `right`, `k`) regardless of the input size $n$. There is no heap allocation dependent on $n$, meeting the requirements for strictly $O(1)$ auxiliary space.

## Component Deep Dive

### 1. Pre-processing (`k = k % nums.length`)
*   **Necessity:** Array rotation is cyclic. If $k = n$, the array remains unchanged. If $k > n$, performing the rotation is equivalent to rotating by $k \pmod n$. 
*   **Edge Case:** The check `if (nums.length == 0) return;` is vital to prevent an `ArithmeticException` (division by zero) when calculating the modulo.

### 2. The Reversal Logic (`rev` function)
*   **Mechanism:** This is a classic two-pointer swap. Using `left` and `right` indices moving inward ensures that we only traverse half the distance of the subarray. 
*   **Invariants:** At each step, `arr[left]` and `arr[right]` are swapped, effectively reflecting the subarray. This operation is self-inverse, meaning calling `rev` twice on the same range restores the original sequence.

### 3. The Triple Reversal Transformation
To rotate the array to the right by $k$:
1.  **Full Reversal:** `rev(nums, 0, n - 1)` reverses the entire array. This places the last $k$ elements at the front of the array, but in reversed order.
2.  **Partial Reversal A:** `rev(nums, 0, k - 1)` restores the order of the first $k$ elements.
3.  **Partial Reversal B:** `rev(nums, k, n - 1)` restores the order of the remaining $n-k$ elements.

## Key Insights

*   **Memory Locality:** Because this algorithm operates in a strictly linear fashion on contiguous memory indices, it is highly cache-friendly. It minimizes cache misses compared to a cycle-shifting approach, which jumps indices based on $i = (i+k) \% n$.
*   **Integer Overflow:** The current implementation is safe from overflow because it does not calculate indices using additions like `left + right` (which could overflow in some contexts); it only uses individual pointer increments/decrements.
*   **Subtle Bug Prevention:** A common pitfall in similar implementations is using $k$ directly without the modulo operation. If $k > n$, the subsequent `rev` calls would throw an `ArrayIndexOutOfBoundsException`. The `k %= n` operation is the most critical line for robustness.
*   **Alternative Considerations:** While the reversal method is optimal for space, a "Cyclic Replacements" approach could be considered if the array were extremely large and resided on a storage medium where writes were significantly costlier than reads, though the implementation complexity for cycle detection is significantly higher.

---
