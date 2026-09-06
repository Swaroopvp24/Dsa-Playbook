# largest-rectangle-in-histogram

## standard_stack_solution.java
*Style: detailed*

# Deep-Dive: Largest Rectangle in Histogram

## Summary
The problem asks for the largest rectangular area possible in a histogram. The core algorithmic challenge is determining the "span" of each bar $i$, defined as the range $[L_i, R_i]$ where `heights[i]` is the minimum height. 

This solution employs a **Monotonic Increasing Stack** technique. By maintaining a stack of indices whose corresponding heights are in non-decreasing order, we can identify the **Nearest Smaller Element** (NSE) to the left and right of every index in linear time. This effectively reduces the problem from an $O(n^2)$ brute-force search (checking all pairs) to an $O(n)$ optimization problem by precomputing boundary constraints.

---

## Complexity Analysis

### Time Complexity: $O(n)$
- **Stack Operations:** Each index $i$ is pushed onto the stack exactly once and popped at most once during the calculation of `leftBoundary` and `rightBoundary` respectively. 
- **Array Traversals:** We perform three linear passes (left-to-right, right-to-left, and area calculation), each taking $O(n)$. 
- **Total:** $O(n) + O(n) + O(n) = O(n)$.

### Space Complexity: $O(n)$
- **Auxiliary Storage:** We utilize two arrays (`leftBoundary`, `rightBoundary`) of size $n$ to store the boundary indices.
- **Stack:** In the worst-case scenario (a strictly increasing histogram), the stack stores $n$ indices.
- **Total:** $O(n)$.

---

## Component Deep Dive

### 1. Monotonic Stack Strategy
The stack maintains indices $idx$ such that `heights[idx]` is strictly increasing. 
- **The "Pop" Logic:** When we encounter `heights[i]` that is smaller than the top of the stack, the current element $i$ acts as the "bottleneck." It is the first bar to the right (or left, depending on the pass) that terminates the potential rectangle starting or ending at the stack-top height.
- **Boundary Defaulting:** 
    - Left boundary defaults to `-1` (virtual index before the array).
    - Right boundary defaults to `n` (virtual index after the array).
    - This ensures that if no smaller element exists, the bar can extend to the very start or end of the histogram.

### 2. Edge Case Handling
- **Flat Histograms (e.g., `[2, 2, 2]`):** The condition `heights[stack.peek()] >= heights[i]` is critical. By using `>=` (inclusive), we pop bars of equal height. This ensures that the boundary points to the *actual* nearest element smaller than the height, rather than an equal one, preventing redundant calculation and potential index errors.
- **Strictly Increasing/Decreasing Inputs:** 
    - **Increasing:** The stack grows to size $n$. All `leftBoundary` values are `i-1`.
    - **Decreasing:** The stack size stays at 1; each new element pops the previous one. `rightBoundary` values are all `i+1`.
- **Empty/Single-Element Arrays:** Handled natively by the initialization logic; an empty array returns `maxArea = 0` (assuming $n=0$ logic), and a single element correctly calculates `heights[0] * (1 - (-1) - 1) = heights[0]`.

---

## Key Insights

### Performance Nuances
- **Memory Locality:** While this implementation uses two passes and three arrays, it is highly cache-friendly. However, it can be optimized into a **Single-Pass Monotonic Stack** algorithm. 
    - In a single pass, when you pop a bar from the stack, you know the element being processed is its "right boundary," and the element currently below the popped element on the stack is its "left boundary." This eliminates the need for pre-allocation and multiple traversals.

### Subtle Bugs & Gotchas
- **Inclusive vs. Exclusive:** The calculation `width = rightBoundary[i] - leftBoundary[i] - 1` relies on the fact that `leftBoundary` and `rightBoundary` are indices of *elements outside* the range. If these boundaries were inclusive (i.e., pointing to the actual smaller element within the histogram), the formula would change to `rightBoundary[i] - leftBoundary[i] + 1`. Keeping boundaries as "exclusive" handles the virtual -1 and $n$ boundaries cleanly.
- **The Equality Check:** If the condition were changed to `heights[stack.peek()] > heights[i]`, the algorithm would fail for duplicate heights. By popping equal values, we ensure that the computed area for a specific height is only captured by the *rightmost* occurrence of that height in the stack, avoiding under-counting the width.

---
