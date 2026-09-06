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

## standard_stack_solution(One_pass).java
*Style: detailed*

# Technical Deep-Dive: Largest Rectangle in Histogram

## Summary
The solution employs a **Monotonic Increasing Stack** algorithm to solve the Largest Rectangle in Histogram problem in linear time. The core strategy is to maintain a non-decreasing sequence of heights. When a "decreasing" element is encountered (i.e., `heights[i] < stack.peek()`), we know that the bars taller than `heights[i]` cannot extend any further to the right. We pop these bars and calculate their potential area, effectively "compressing" the range of the taller bars by pushing the current `heights[i]` back into the stack at the earliest possible starting position (`startIndex`).

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Each element is pushed exactly once** onto the stack.
*   **Each element is popped at most once.** 
*   Even though there is a `while` loop nested inside the `for` loop, the aggregate number of `pop()` operations across the entire execution is limited by the total number of `push()` operations ($n$). Thus, the amortized cost per element is constant.
*   The final cleanup loop iterates through the stack, which contains at most $n$ elements, adding another $O(n)$ operation.

### Space Complexity: $O(n)$
*   In the worst-case scenario (a histogram with monotonically increasing bars), the stack will hold all $n$ elements. 
*   Each stack element stores a 2-integer array `[index, height]`, resulting in $O(n)$ space requirement.

## Component Deep Dive

### 1. The Monotonic Stack Strategy
The stack stores the pair `{startIndex, height}`. The `startIndex` is the critical optimization: it represents the furthest left index that the current `height` could have originated from. By updating `startIndex` during pops, we effectively allow shorter bars to "inherit" the width of the bars that were just popped, as the shorter bar could have started at those indices as well.

### 2. The "Pop" Logic
```java
while (!stack.isEmpty() && stack.peek()[1] > heights[i]) {
    int[] current = stack.pop();
    maxArea = Math.max(maxArea, current[1] * (i - current[0]));
    startIndex = current[0]; // Propagate the starting index
}
```
When `heights[i]` is smaller than the stack top, the current bar acts as a right-boundary constraint. The width is calculated as `(current_index - start_index)`. The `startIndex` is updated to the `start` of the popped bar because the current bar could have theoretically started there as well.

### 3. Cleanup Loop
After the primary loop, the stack might contain bars that never encountered a "shorter" bar to their right. These bars are, by definition, the tallest possible bars that can extend to the very end of the histogram. The calculation `heights.length - startIndex` correctly computes the area for these remaining bars.

## Key Insights & Nuances

*   **Implicit Right Boundary:** The algorithm avoids explicitly adding a `0` height to the end of the `heights` array (a common technique to trigger stack pops). Instead, it uses an explicit post-processing loop. Both approaches are functionally identical, but this implementation separates the logic clearly.
*   **Index Propagation:** The `startIndex = start` assignment is the most subtle part of the code. If you have a sequence like `[5, 4]`, when `4` is processed, `5` is popped. The `4` is pushed with `startIndex = 0`. This correctly captures that the rectangle of height `4` spans indices `0` to `1`.
*   **Performance Nuance:** Using `ArrayDeque` is preferable to `Stack` in Java because `Stack` is synchronized (carrying unnecessary overhead) and extends `Vector`. `ArrayDeque` is optimized for stack-based operations with significantly lower memory footprint per push.
*   **Edge Cases:**
    *   **Empty array:** The code returns `0` correctly.
    *   **Monotonically increasing:** The stack grows linearly, and the area is calculated in the final loop.
    *   **Monotonically decreasing:** Each new element forces the entire stack to pop, keeping the stack size minimal and computing area at each step.

---

## optimal_stack_solution.java
*Style: detailed*

# Engineering Deep Dive: Largest Rectangle in Histogram

## 1. Summary
This implementation utilizes a **Monotonic Increasing Stack** to solve the "Largest Rectangle in Histogram" problem in linear time. The core algorithmic technique relies on identifying the "nearest smaller element" to the left and right of every bar in the array. 

By maintaining a stack of indices representing heights in non-decreasing order, we ensure that when we encounter a bar smaller than the current stack top, we have defined the **Right Boundary** for the popped element. The **Left Boundary** is implicitly defined by the element residing beneath the popped element in the stack. This single-pass calculation avoids the $O(n^2)$ overhead of a brute-force approach, effectively treating each bar as the "shortest" height of a potential rectangle.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Analysis:** Although there is a nested `while` loop within a `for` loop, each index from the input `heights` array is pushed onto the stack exactly once and popped from the stack exactly once. 
*   **Total Operations:** Since each element undergoes a constant number of operations (push/pop), the amortized time complexity is $O(n)$.

### Space Complexity: $O(n)$
*   **Analysis:** In the worst-case scenario—an array of strictly increasing heights—all $n$ indices will be pushed onto the stack before the terminal iteration (the virtual index `n`). 
*   **Memory Footprint:** The `ArrayDeque` stores at most $n$ integers, leading to a space complexity of $O(n)$.

---

## 3. Component Deep Dive

### The Virtual Sentinel (i == n)
The logic `i == n` serves as a "flushing" mechanism. By treating the end of the array as a bar with a height of `0`, we force the monotonic stack to pop all remaining elements. This ensures that any rectangle that could have extended to the very end of the array is properly evaluated. Without this, the final sequence of increasing bars would remain in the stack and never calculate their potential area.

### Boundary Logic (The Width Calculation)
The calculation `width = i - stack.peek() - 1` is the crux of the algorithm:
1.  **Right Boundary:** `i` is the first index where `heights[i] < heights[popped_index]`.
2.  **Left Boundary:** After popping, `stack.peek()` is the index of the nearest element to the left that is smaller than (or equal to) the popped height.
3.  **Width:** The rectangle spans from `left_boundary + 1` to `right_boundary - 1`. The arithmetic `i - (left_idx) - 1` correctly captures this range.
4.  **Empty Stack Edge Case:** If the stack becomes empty after popping, it implies the popped height was the minimum encountered so far. Therefore, the rectangle spans the entire width from `0` to `i-1`. Using `i` as the width in this case is the mathematically correct identity.

### Stack Invariant
The stack maintains `heights[stack.peek()] <= heights[i]`. If this invariant is violated, the current height is "smaller," meaning the bars on top of the stack can no longer expand rightward. This triggers the area calculation for the bars that have been constrained.

---

## 4. Key Insights

*   **Handling Equal Heights:** Note the condition `heights[stack.peek()] >= heights[i]`. Using `>=` (instead of strict `>`) is a subtle optimization that simplifies the logic. If duplicate heights exist, we calculate the area for the leftmost duplicate redundantly or effectively ignore it until the subsequent pop. This is safe and prevents unnecessary stack growth.
*   **ArrayDeque vs. Stack:** The choice of `ArrayDeque` over the legacy `Stack` class is standard practice in Java. `Stack` is synchronized (introducing unnecessary overhead) and extends `Vector`, making `ArrayDeque` the faster, preferred stack implementation for single-threaded algorithms.
*   **Index Management:** The code avoids pushing `n` onto the stack (`if (i < n) { stack.push(i); }`). This is critical; pushing `n` would cause an `ArrayIndexOutOfBoundsException` on subsequent `heights[stack.peek()]` calls during the logic flow.
*   **Failure Modes:** A common pitfall in similar implementations is failing to correctly handle the "empty stack" case after a pop. Ensure you differentiate between "stack was empty before pop" (not possible here due to `while` condition) and "stack became empty after pop" (handled by the `stack.isEmpty()` ternary).

---
