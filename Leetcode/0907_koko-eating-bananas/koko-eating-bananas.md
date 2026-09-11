# koko-eating-bananas

## standard_binary_search.java
*Style: detailed*

# Engineering Deep Dive: Koko Eating Bananas (Binary Search on Answer)

## Summary
The solution employs a **Binary Search on the Answer Space** pattern. Since the total time required to consume all piles ($H$) is monotonically non-increasing relative to the eating speed ($k$), we can treat the problem as finding the smallest value $k \in [1, \max(\text{piles})]$ such that $f(k) \leq h$, where $f(k)$ is the summation of ceiling divisions of all piles by $k$. 

This shifts the problem from an optimization search (trying to "guess" a speed) to a decision problem (can we finish in time $T$?) wrapped in a logarithmic search.

---

## Complexity Analysis

### Time Complexity: $O(N \log M)$
*   **$N$**: Number of piles.
*   **$M$**: The maximum size of a pile (the upper bound of our search space).
*   **Reasoning**: We perform a binary search over the range $[1, M]$, which takes $O(\log M)$ iterations. In each iteration, we iterate through the entire input array `piles` to calculate the total hours, which is $O(N)$. Thus, the total complexity is $O(N \log M)$.

### Space Complexity: $O(1)$
*   **Reasoning**: The algorithm performs the search in-place using only a few primitive integer variables (`left`, `right`, `minimumSpeed`, `totalHours`). No auxiliary data structures proportional to the input size are allocated.

---

## Component Deep Dive

### 1. Ceiling Division Calculation
```java
totalHours += (pile + eatingSpeed - 1) / eatingSpeed;
```
*   **Mechanism**: Standard integer division in Java truncates towards zero. To perform a `ceil(a / b)` using only integer arithmetic, we use the identity $\lceil a/b \rceil = (a + b - 1) / b$. 
*   **Edge Case**: This avoids the overhead of `Math.ceil()` (which returns a `double`) and potential floating-point precision issues, while also avoiding branching logic.

### 2. Search Space Boundaries
*   **Lower Bound (`left = 1`)**: Koko must eat at least one banana per hour.
*   **Upper Bound (`right = maxPileSize`)**: Eating faster than the largest pile does not reduce the total hours consumed, as Koko can only work on one pile at a time. The problem states she finishes a pile and does nothing else for the remainder of that hour, effectively making any speed $> \max(\text{piles})$ equivalent to $\max(\text{piles})$ in terms of total time.

### 3. Loop Termination and Convergence
*   **Logic**: The `while (left <= right)` condition, coupled with `right = eatingSpeed - 1` and `left = eatingSpeed + 1`, ensures we converge on the smallest valid `minimumSpeed`. By caching the current `eatingSpeed` into `minimumSpeed` whenever `totalHours <= h`, we ensure that even if the search converges, we hold onto the smallest valid "successful" candidate.

---

## Key Insights

### Numeric Overflow
*   **Risk**: `totalHours` is declared as a `long`. In an environment where `h` is very large or `piles` contains many large values, `totalHours` could potentially exceed `Integer.MAX_VALUE` before the condition `totalHours <= h` is checked. Using `long` is a defensive best practice.

### Performance Optimization Nuance
*   The `maxPileSize` calculation is an $O(N)$ overhead. While it is necessary for the initial search space, it could technically be omitted if we knew the absolute upper bound (e.g., $10^9$ per problem constraints), but calculating the specific `max` is tighter and yields a faster search.

### Why not use `Math.ceil`?
*   `Math.ceil` operates on `double`. Converting between `int` and `double` is computationally more expensive than simple integer addition and division. Furthermore, for very large pile sizes, `double` precision (53 bits of significand) could theoretically lead to off-by-one errors if `pile` values approach $2^{53}$, though unlikely given typical competitive programming constraints. The integer formula `(a + b - 1) / b` is strictly superior in performance and safety.

### Subtle Edge Case: $h = \text{piles.length}$
*   If $h$ equals the number of piles, Koko must eat at a speed exactly equal to the largest pile. The binary search handles this correctly because the `right` bound starts at `maxPileSize`, and the loop will eventually test this value and return it as the minimum speed.

---
