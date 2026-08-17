# max-water-container

## standard_two_pointer.java
*Style: detailed*

# Engineering Reference: Container With Most Water

## 1. Summary
The `maxArea` solution implements a **Two-Pointer Greedy Strategy** to solve the "Container With Most Water" problem. The objective is to find two indices $i$ and $j$ that maximize the area $(j - i) \times \min(height[i], height[j])$.

Instead of a brute-force $O(n^2)$ traversal, this approach reduces the search space by observing that the area is constrained by the shorter of the two lines. By starting with the widest possible container and iteratively moving the pointer pointing to the shorter line inward, we explore the only candidates capable of producing a larger area, effectively pruning sub-optimal search paths.

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Reasoning:** The algorithm employs a two-pointer approach where each pointer traverses the array exactly once. The pointers `l` and `r` start at opposite ends and move toward each other until they converge. Each iteration involves a constant-time $O(1)$ calculation, resulting in linear time complexity relative to the input array size $n$.

### Space Complexity: $O(1)$
*   **Reasoning:** The solution operates in-place using a fixed number of integer variables (`l`, `r`, `maax`, `cur`). No auxiliary data structures are allocated that scale with the input size, achieving constant space complexity.

## 3. Component Deep Dive

### The Greedy Choice Property
The algorithm relies on the fact that if $height[l] < height[r]$, moving the right pointer $r$ inward will never increase the area. Why? Because the width $(r - l)$ decreases, and the height can, at best, stay the same (if $height[r-1] \ge height[l]$) or decrease. The only potential for an area increase lies in finding a taller line on the left side to replace $height[l]$.

### Edge-Case Handling
*   **$n < 2$:** While not explicitly guarded, the `while (l < r)` condition handles arrays with 0 or 1 elements gracefully by returning the initialized `maax` of 0.
*   **Equal Heights:** When `height[l] == height[r]`, the logic executes `r--`. This is mathematically safe; if both sides are equal, moving either pointer is valid because neither the new left nor new right could possibly form a larger area with the current opposite side than the current configuration did.
*   **Maximum Integer Values:** The current implementation uses `int` for calculations. For extremely large input arrays or heights (e.g., in a language where width $\times$ height overflows 32-bit integers), one would need to promote `cur` and `maax` to `long`.

## 4. Key Insights

*   **Pointer Invariance:** The core invariant is that the current container $[l, r]$ is the maximum possible area for the given width. Any other container with the same or smaller width starting at $l$ or $r$ is discarded because we already know their heights are inferior to the ones currently being evaluated.
*   **Optimization vs. Brute Force:** Brute force explores all $\frac{n(n-1)}{2}$ combinations. The two-pointer approach reduces this to $n-1$ iterations.
*   **Subtle Bug Warning:** Avoid the temptation to move both pointers when `height[l] == height[r]`. While it might seem like you are "skipping" pairs, you could inadvertently skip the true maximum if the next elements at both $l+1$ and $r-1$ are significantly taller. The logic `else r--` is sufficient; the `l` will naturally be evaluated in the next iteration.
*   **Memory Efficiency:** Because this is an $O(1)$ space algorithm, it is highly cache-friendly. The input array is accessed sequentially from both ends, which is optimal for modern CPU pre-fetchers.

---
