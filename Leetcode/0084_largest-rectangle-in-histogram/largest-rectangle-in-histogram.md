# largest-rectangle-in-histogram

## standard_stack_solution.java
*Style: detailed*

# Engineering Deep Dive: Largest Rectangle in Histogram

## Summary
The solution employs a **Monotonic Stack** pattern to solve the "Nearest Smaller Values" problem. The objective is to identify the span $[L, R]$ for each bar $i$ where $height[i]$ is the minimum height within that range. By finding the first index to the left ($L$) and right ($R$) where the height is strictly less than $height[i]$, we define the maximum horizontal expansion for a rectangle constrained by $height[i]$. The area for any index $i$ is defined as $height[i] \times (rightBoundary[i] - leftBoundary[i] - 1)$.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Two-Pass Monotonic Stack:** Each element is pushed onto the stack exactly once and popped at most once per pass. Since we perform two passes (left-to-right and right-to-left), the operations remain linear.
*   **Boundary Calculation/Area pass:** A final $O(n)$ iteration computes the area, maintaining a total time complexity of $O(n)$.

### Space Complexity: $O(n)$
*   **Auxiliary Arrays:** We allocate `leftBoundary` and `rightBoundary` arrays, requiring $2n$ space.
*   **Stack:** In the worst-case scenario (a strictly increasing or decreasing histogram), the stack stores up to $n$ elements.
*   Total space complexity is $O(n)$.

## Component Deep Dive

### 1. The Monotonic Stack Mechanism
The stack maintains indices such that the values at these indices are always in non-decreasing order. 
*   **The Invariant:** When processing index $i$, any element in the stack that is $\ge heights[i]$ cannot be the "nearest smaller" neighbor for any subsequent elements. Popping these elements is the critical step that ensures $O(n)$ performance.
*   **Boundary Defaulting:** 
    *   `leftBoundary` is initialized to `-1` (representing virtual index before the array start).
    *   `rightBoundary` is initialized to `n` (representing virtual index after the array end).
    *   This handles edge cases where a bar is the minimum of the entire range.

### 2. Boundary Logic
The width calculation `rightBoundary[i] - leftBoundary[i] - 1` is derived from the open-interval definition. If the nearest smaller element on the left is at index $L$ and on the right is at index $R$, the range of bars with height $\ge heights[i]$ is $[L+1, R-1]$. The number of elements is $(R-1) - (L+1) + 1$, which simplifies to $R - L - 1$.

## Key Insights & Performance Nuances

*   **Handling Plateaus (Equal Heights):** The use of `heights[stack.peek()] >= heights[i]` inside the `while` loop is intentional. If we used strictly `>`, the algorithm would fail to correctly identify the nearest smaller index when duplicate heights are present, potentially leading to incorrect widths. By popping equal values, we effectively "collapse" them into the nearest smaller boundary, which is mathematically safe because the area will be re-calculated correctly when the "last" instance of that height in the sequence is processed.
*   **Alternative Implementation (Single Pass):** This implementation uses two passes for clarity. It is possible to optimize this to a single pass by calculating areas as elements are popped from the stack. When an element $j$ is popped, the current index $i$ becomes its `rightBoundary`, and the new `stack.peek()` becomes its `leftBoundary`. 
*   **Optimization Opportunity:** To reduce memory footprint to $O(n)$ (one array instead of two), one can utilize a single-pass approach that calculates the area on-the-fly. This removes the need for `leftBoundary` and `rightBoundary` arrays entirely, though it requires a sentinel value (e.g., adding a `0` to the end of the `heights` array) to force the stack to flush at the end of the iteration.
*   **Stack Data Structure:** The choice of `ArrayDeque` is optimal in Java as it avoids the synchronization overhead of `Stack`, providing a more efficient interface for LIFO operations.

---
