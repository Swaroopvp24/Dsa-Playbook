# sqrtx

## standard_binary_search.java
*Style: detailed*

# Technical Deep Dive: Integer Square Root Implementation

## 1. Summary
The implementation utilizes a **Binary Search on the Answer Space** technique to compute the floor of the square root of a non-negative integer $x$. Rather than relying on iterative approximation methods (like Newton-Raphson), this approach treats the range $[2, x]$ as a monotonic function $f(n) = n^2$. By maintaining an invariant where `right` tracks the largest integer $n$ such that $n^2 \le x$, the algorithm converges logarithmically toward the integer square root.

## 2. Complexity Analysis

### Time Complexity: $O(\log x)$
*   **Derivation:** The search space is defined by the range $[2, x]$. In each iteration of the `while` loop, the search interval is halved via `mid = left + (right - left) / 2`. 
*   **Mathematical Bound:** The number of operations required to reduce the interval $[2, x]$ to a single point is $\lceil \log_2(x-2) \rceil$. Thus, the time complexity is logarithmic with respect to the input magnitude.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm operates in-place using a constant number of primitive integer variables (`left`, `right`, `mid`). No auxiliary data structures are allocated, resulting in constant auxiliary space complexity.

---

## 3. Component Deep Dive

### Integer Overflow Mitigation
The expression `mid * mid` is a common pitfall in C++/Java implementations. If `mid` exceeds $\approx 46,340$ (the integer square root of `Integer.MAX_VALUE`), `mid * mid` will overflow into a negative value due to 32-bit two's complement wrapping.
*   **Solution:** The code uses division: `mid <= x / mid`. This is mathematically equivalent to `mid * mid <= x` but guarantees that no operation exceeds the `int` bounds, as `x / mid` will always be $\le$ `mid` (for $x \ge 0$).

### The Midpoint Calculation
*   `int mid = left + (right - left) / 2;`
*   **Nuance:** This is the standard idiomatic way to calculate a midpoint in binary search to prevent overflow in the expression `(left + right)`. While `(left + right) >>> 1` is also common, the subtraction method is robust and idiomatic across languages.

### Loop Invariant and Return Value
*   **Invariant:** At the start of each iteration, `right` is the current candidate for the "high-water mark" of values whose square is $\le x$.
*   **Termination:** The loop terminates when `left > right`. At this exact moment, `right` will point to the floor value of $\sqrt{x}$. 
*   **Edge-Case Handling:** 
    *   $x=0, 1$: Handled by the guard clause `if (x < 2)`, returning $x$ immediately.
    *   Perfect Squares: If $mid^2 = x$, the `mid <= x / mid` branch executes, moving `left` to `mid + 1`. The loop continues until `right` meets `mid`, correctly returning the root.

---

## 4. Key Insights

### Binary Search vs. Newton-Raphson
While Newton-Raphson (Heron's method) is technically faster in terms of convergence for floating-point calculations ($O(\log(\text{bits}))$), binary search is preferred in strict integer arithmetic because it avoids floating-point precision issues entirely. There is no risk of oscillating around the root or losing precision due to `double` representation limits.

### Subtle Bugs to Watch For
1.  **Search Bounds:** Initializing `right = x` is safe for $x \ge 2$. If the input could be `Integer.MAX_VALUE`, `right` remains within bounds. However, if using `right = x + 1` or other initialization strategies, one must be cautious of `Integer.MAX_VALUE` overflow.
2.  **The `left <= right` Condition:** If the loop condition were `left < right`, the logic inside would require significant adjustment (e.g., handling the `mid` vs `mid + 1` logic carefully). The current `left <= right` setup with `right = mid - 1` and `left = mid + 1` is the cleanest way to find an exact floor.
3.  **Division by Zero:** The guard clause `if (x < 2)` prevents any possibility of `x / mid` dividing by zero, as the smallest `mid` checked in the loop is derived from `left = 2`.

---
