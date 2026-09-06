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

## standard_stack_solution(One_pass).java
*Style: detailed*

# Engineering Deep-Dive: Monotonic Stack Solution for Largest Rectangle in Histogram

## Summary
The solution employs a **Monotonic Increasing Stack** to solve the problem in linear time. The core algorithmic insight is that a rectangle's area is constrained by its shortest bar (the "bottleneck"). By maintaining a stack of bars in non-decreasing order of height, we defer the area calculation until we encounter a bar shorter than the current stack top. When a bar is popped, we confirm that the popped height is the minimum for the interval spanning from its `startIndex` to the current index `i`, allowing for a single-pass calculation of potential maximal areas.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** Although there is a `while` loop nested within a `for` loop, each element is pushed onto the stack exactly once and popped at most once. The final cleanup loop iterates through the stack elements, which is also bounded by $O(N)$. Thus, the total operations scale linearly with the number of bars.

### Space Complexity: $O(N)$
*   **Reasoning:** In the worst-case scenario (a histogram with strictly increasing bar heights), every element will be pushed onto the `ArrayDeque`. The space requirement for the stack is therefore proportional to $N$.

---

## Component Deep Dive

### 1. The Stack Structure (`Deque<int[]>`)
The stack acts as a history of "potential" start indices. Each element `[startIndex, height]` represents a bar that could potentially extend further to the right. 

### 2. The `startIndex` Propagation Logic
This is the most nuanced part of the algorithm:
*   When a taller bar is popped, its `startIndex` is captured. 
*   The current, shorter bar (`heights[i]`) inherits this `startIndex` because the shorter bar can extend back to the point where the taller, now-popped bar began. 
*   This ensures that even when a tall bar is removed, its "influence" on the left-boundary is preserved for subsequent shorter bars.

### 3. The Cleanup Loop
Post-iteration, the stack may contain bars that never encountered a "shorter neighbor" to the right. These bars are implicitly limited by the boundary of the histogram (`heights.length`). The second loop calculates the area for these remaining bars by treating their `startIndex` as the left boundary and the array's end as the right boundary.

---

## Key Insights & Nuances

### 1. The "Monotonic" Invariant
The stack is maintained strictly as an **increasing** sequence of heights. If we encounter a height $H_{curr} < H_{top}$, we have found the right boundary for $H_{top}$. The width of the rectangle for $H_{top}$ is determined by `(current_index - start_index)`.

### 2. Why `startIndex` matters
A common mistake is using `i` as the index in the stack. By storing the `startIndex` when a bar is pushed (or inherited during a pop), we handle the left-expansion logic elegantly. If the current bar is the absolute minimum so far, its `startIndex` is `0`, enabling it to span the entire histogram to its left.

### 3. Edge Case Handling
*   **Empty Histogram:** The loops will not execute, returning `0`, which is correct.
*   **Strictly Increasing Input:** The `while` loop never triggers; the cleanup loop handles all rectangles.
*   **Strictly Decreasing Input:** Every bar is immediately popped, correctly calculating single-bar rectangles and extending the minimum height to the full width.
*   **Duplicate Heights:** The current logic handles duplicates safely. If `heights[i] == stack.peek()[1]`, the `while` loop condition `> heights[i]` remains `false`, avoiding redundant pops and maintaining the property that we only process unique height boundaries.

### 4. Subtle Performance Consideration
Using `ArrayDeque` is preferable to `Stack` in Java as it avoids the synchronized overhead of the legacy `java.util.Stack` class, providing better performance in high-throughput scenarios. The use of `int[]` for the pair is memory-efficient; however, in extremely memory-constrained environments, one could pack two integers into a single `long` to avoid the object overhead of the array allocation.

---
