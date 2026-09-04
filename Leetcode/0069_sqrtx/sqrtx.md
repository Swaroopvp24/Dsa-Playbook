# sqrtx

## standard_binary_search.java
*Style: detailed*

# Technical Deep-Dive: Integer Square Root Implementation

## Summary
The provided solution implements the integer square root function, $\lfloor\sqrt{x}\rfloor$, using a **Binary Search on the Answer Space**. Given that the square root function is monotonic for $x \ge 0$, we treat the range $[2, x]$ as a sorted monotonic sequence and perform a search to locate the largest integer $k$ such that $k^2 \le x$. This approach achieves logarithmic time complexity, which is significantly more efficient than linear iterative approaches.

---

## Complexity Analysis

### Time Complexity: $O(\log x)$
*   **Derivation:** The search space is defined by the interval $[2, x]$. In each iteration of the `while` loop, the search range is halved (`left = mid + 1` or `right = mid - 1`). 
*   **Convergence:** The number of iterations required to reduce the interval size to zero is defined by $\log_2(x)$. Since each iteration performs a constant-time division and comparison, the total complexity is $O(\log x)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm utilizes a fixed number of primitive integer variables (`left`, `right`, `mid`). No additional data structures or recursion stacks are employed, resulting in constant auxiliary space complexity.

---

## Component Deep Dive

### 1. Midpoint Calculation: `left + (right - left) / 2`
Standard midpoint calculation `(left + right) / 2` is prone to **integer overflow** when `left + right` exceeds `Integer.MAX_VALUE` ($2^{31}-1$). The implementation uses `left + (right - left) / 2` to algebraically maintain the midpoint while ensuring that intermediate calculations stay within the bounds of a 32-bit signed integer.

### 2. Overflow-Resilient Comparison: `mid <= x / mid`
This is the most critical logic component for stability. 
*   **The Trap:** If we calculated `mid * mid <= x`, a large `mid` could result in an integer overflow (wrapping around to a negative value), causing the condition to evaluate incorrectly.
*   **The Solution:** By transforming the inequality from $mid^2 \le x$ to $mid \le x/mid$, we eliminate the possibility of overflow. Since $x$ and $mid$ are positive integers, the division operation remains within the valid range of `int` for all $x \ge 0$.

### 3. Binary Search Invariant
The loop maintains the following invariant:
*   `right` always tracks the highest candidate value that satisfies $right^2 \le x$.
*   `left` always tracks the smallest value that potentially could be the square root.
*   When the loop terminates (`left > right`), `right` serves as the floor of the square root, as it is the "last valid" value encountered before the search space collapsed.

---

## Key Insights & Nuances

### Boundary Handling
The check `if (x < 2)` handles the trivial cases $0$ and $1$ immediately. By setting the search range starting at `left = 2`, the algorithm avoids unnecessary computation for cases where the result is trivial, effectively optimizing the constant factor of the $O(\log x)$ runtime.

### Why `right` is the Answer
In a binary search where we seek the "last valid" element, the variable `right` moves left when `mid` is too large (`right = mid - 1`), and `left` moves right when `mid` is valid (`left = mid + 1`). Because the loop runs until `left` crosses `right`, `right` will always converge to the largest value that passed the valid test (`mid <= x / mid`).

### Subtle Performance Considerations
While this algorithm is optimal for general use, modern hardware often provides intrinsic functions (like `Math.sqrt` or assembly-level `fsqrt` instructions). 
*   **Floating point vs. Integer:** `Math.sqrt()` uses floating-point arithmetic (IEEE 754), which may introduce precision errors for extremely large integers (though usually safe for `int`). The binary search approach is strictly integer-based and guarantees precision, making it the preferred architectural choice for systems where floating-point units (FPU) are either restricted or desired to be bypassed.

### Potential Pitfalls
*   **Infinite Loops:** If the midpoint logic were `mid = (left + right) / 2` without careful boundary movement, one could risk an infinite loop. Using `mid = left + (right - left) / 2` ensures the `mid` is always rounded down towards `left`, which, combined with the `left = mid + 1` update, guarantees termination.

---
