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

## optimal_stack_solution.java
*Style: detailed*

# Engineering Reference: Largest Rectangle in Histogram

## 1. Summary
The implementation utilizes a **Monotonic Increasing Stack** to solve the "Largest Rectangle in Histogram" problem in linear time. The core algorithmic technique relies on the observation that the largest rectangle with a bar of height $H$ as its shortest member extends as far left and as far right as possible until a bar with height $< H$ is encountered. 

By maintaining a stack of indices corresponding to strictly increasing heights, the algorithm defers calculating the area of a bar until it encounters a "right boundary" (a bar shorter than the current stack top). This allows the algorithm to determine the width of rectangles in $O(1)$ amortized time per bar.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Each element is pushed once and popped at most once.** 
*   Even though there is a `while` loop nested within the `for` loop, the inner loop operation `stack.pop()` is conditioned on the total number of items pushed. Across the entire execution, there are exactly $n$ pushes and $n$ pops, resulting in $2n$ operations. This yields a linear amortized time complexity.

### Space Complexity: $O(n)$
*   In the worst-case scenario (a strictly increasing sequence of heights), the stack will hold all $n$ indices. Thus, the auxiliary space requirement is $O(n)$.

---

## 3. Component Deep Dive

### The "Virtual Boundary" Strategy
The algorithm appends a virtual height of `0` at index `n`. This is a critical pattern in monotonic stack problems. It forces the stack to "flush" all remaining elements, ensuring that even if the input array is sorted in ascending order (where no right-boundary condition is met during the main loop), the remaining rectangles are calculated.

### The Width Calculation Logic
For a height $H$ at index $P$ (the popped element):
1.  **Right Boundary ($R$):** The index $i$ that triggered the pop.
2.  **Left Boundary ($L$):** The index currently at the top of the stack (after $P$ is popped). 
3.  **The width formula:** `width = i - stack.peek() - 1`.
    *   If the stack is empty after the pop, it implies that the popped bar was the minimum height encountered so far, meaning the rectangle extends from index `0` to $i-1$. Thus, `width = i`.
    *   Otherwise, the range is $(stack.peek(), i)$, excluding both boundaries, resulting in $i - (stack.peek() + 1)$.

### Edge Case Handling
*   **Empty Array:** The loop condition `i <= n` handles arrays of size 0 gracefully, returning `maxArea = 0`.
*   **Strictly Increasing/Decreasing Inputs:** 
    *   *Increasing:* The stack grows until `i == n`, then pops everything.
    *   *Decreasing:* Every new element triggers a pop, calculating local maxima continuously.
*   **Duplicate Heights:** The use of `>=` in `heights[stack.peek()] >= heights[i]` ensures that duplicate heights are processed correctly as right boundaries, preventing erroneous width calculation for identical values.

---

## 4. Key Insights & Nuances

*   **The Stack Invariant:** The stack must strictly store indices of increasing heights. The condition `heights[stack.peek()] >= heights[i]` maintains this. If you were to use `>` instead of `>=`, you might fail to correctly calculate the width of identical adjacent bars because the algorithm would treat the first occurrence of a height as "still valid" when a subsequent identical height arrives.
*   **Index-Only Storage:** Storing only indices (`ArrayDeque<Integer>`) is more memory-efficient than storing custom objects or tuples (e.g., `Pair<Integer, Integer>`). Since the input array is accessible within the scope, the `heights` values are always retrievable via `heights[index]`.
*   **Performance Optimization:** `ArrayDeque` is preferred over `Stack` in Java. `Stack` extends `Vector`, which is synchronized (thread-safe); the overhead of internal synchronization is unnecessary here and degrades performance compared to the non-synchronized `ArrayDeque`.
*   **Subtle Logic Trap:** The most common mistake in this implementation is calculating the area *before* popping or incorrectly handling the `i == n` case. Always ensure the "Right Boundary" logic is processed first; the current structure correctly handles this by using the `i == n` OR condition to trigger the `while` loop, effectively clearing the stack at the end of the input stream.

---
