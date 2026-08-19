# trapping-rain-water

## standard_two_pointer.java
*Style: detailed*

# Engineering Deep Dive: Trapping Rain Water (Two-Pointer Optimization)

## Summary
The provided solution addresses the "Trapping Rain Water" problem using the **Two-Pointer Technique**. The core algorithmic insight is that the amount of water trapped at any index `i` is determined by `min(max_left[i], max_right[i]) - height[i]`. 

Instead of pre-calculating prefix and suffix maximum arrays (which would require $O(N)$ extra space), this solution maintains two pointers (`l` and `r`) and two running maximums (`lmax` and `rmax`). By comparing `height[l]` and `height[r]`, we effectively process the limiting boundary of the water container from the outside in, ensuring we always have sufficient information to calculate the water capacity at the current pointer without needing full global knowledge of the array.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm employs a single pass over the array. Each element is visited exactly once by either the `l` or `r` pointer. Inside the `while` loop, all operations (comparisons, additions, assignments) are $O(1)$. Since we terminate when `l > r`, the total operations are proportional to the number of elements in the array.

### Space Complexity: $O(1)$
*   **Reasoning:** The algorithm utilizes a constant amount of auxiliary space. We only allocate four primitive integer variables (`l`, `r`, `lmax`, `rmax`, and `total`) regardless of the input array size. This is a significant optimization over the standard dynamic programming approach which requires $O(N)$ space for auxiliary storage.

---

## Component Deep Dive

### 1. The Pointer Logic
The logic hinges on the conditional: `if (height[l] < height[r])`.
*   When `height[l] < height[r]`, we know that the water level at the `l` pointer is strictly constrained by `lmax` (because even if there is a higher bar further right, the current `height[r]` or some bar between `l` and `r` guarantees that `lmax` is the bottleneck). 
*   Because we move the pointer pointing to the smaller height, we effectively guarantee that we are always calculating water based on the "known" shortest boundary.

### 2. Variable Initialization
*   `lmax` and `rmax` are initialized to `Integer.MIN_VALUE`. While this is technically correct, it assumes that `height[i]` will be non-negative. If the input allows negative integers, the code remains robust, though typically heights are $\geq 0$.
*   The `while(l <= r)` loop ensures the middle element (if the array length is odd) is processed, preventing off-by-one errors common in two-pointer implementations.

### 3. Edge Case Handling
*   **Empty/Single/Double Element Arrays:** If `height.length < 3`, the loop will execute, but `total` will remain `0` because `Math.min(lmax, rmax) - height[i]` will effectively cancel out or be zero, correctly returning 0.
*   **Monotonic Arrays:** If the array is strictly increasing or decreasing, `total` remains 0, as water cannot be trapped without both left and right boundaries.

---

## Key Insights

*   **The "Bottleneck" Principle:** The fundamental insight here is that we only need to know the *minimum* of the two walls. By moving the pointer associated with the smaller wall, we eliminate the need to know the exact `rmax` when `lmax` is clearly the limiting factor (and vice versa).
*   **Potential Integer Overflow:** While not an issue with the provided constraints in LeetCode (where sum of heights fits in an `int`), for extremely large datasets or very high bar heights, `total` should ideally be a `long` to prevent overflow.
*   **Subtle Bug Prevention:** Note the use of `lmax = Math.max(lmax, height[l])`. It is crucial that this update happens *before* the water calculation. If updated after, the logic would attempt to calculate water for the current index based on previous boundaries, leading to incorrect calculations for the index being evaluated.
*   **Performance Nuance:** The branch predictor in modern CPUs handles the `if (height[l] < height[r])` statement well for random distributions, but for highly patterned data, this solution is incredibly efficient due to minimal memory cache misses (linear traversal).

---

## standard_two_pointer_modified.java
*Style: detailed*

# Technical Reference: Two-Pointer Trapped Rainwater Solution

## Summary
The "Trapping Rain Water" problem is solved here using an **optimal two-pointer approach**. Instead of pre-calculating prefix and suffix maximums (which requires $O(N)$ extra space), this algorithm calculates the trapped water on-the-fly by maintaining dynamic boundaries.

The core algorithmic intuition is that the amount of water trapped at any index `i` is determined by `min(max_left, max_right) - height[i]`. By maintaining two pointers (`left` and `right`) and tracking the `max` height encountered from both directions, we can greedily process the side that is currently "limiting" the water column, ensuring we always have a guaranteed boundary for the current calculation.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   The algorithm utilizes a single `while` loop that traverses the array exactly once.
*   Each element is visited exactly once by either the `left` or `right` pointer.
*   All operations within the loop (comparisons, additions, assignments) are $O(1)$.

### Space Complexity: $O(1)$
*   The solution is strictly in-place.
*   Only a constant amount of memory is allocated for pointer indices (`left`, `right`) and accumulation variables (`leftMaxHeight`, `rightMaxHeight`, `trappedWater`), regardless of the input array size `N`.

---

## Component Deep Dive

### 1. The Pointers (`left`, `right`)
The pointers act as the boundaries of the search window. Initialized at indices `0` and `length - 1`, they converge toward the global maximum of the array.

### 2. State Tracking (`leftMaxHeight`, `rightMaxHeight`)
These variables store the historical peak height observed from each respective direction. Crucially, because we always process the smaller side (the `if (height[left] <= height[right])` block), we implicitly guarantee that `leftMaxHeight` is the true `min(leftMax, rightMax)` for the `left` pointer, or vice versa for the `right`.

### 3. The Water Calculation Logic
```java
trappedWater += leftMaxHeight - height[left];
```
This line is the heart of the algorithm. By the time the logic reaches index `i`, we have confirmed through the conditional branch that the side we are moving from is lower than (or equal to) the other side. Thus, `leftMaxHeight` effectively represents the **limiting boundary** of the water container. Since we are subtracting the current height from the peak, the result is guaranteed to be $\ge 0$.

### 4. Edge-Case Handling
*   **Empty Array / Single Element:** If `height.length` is 0 or 1, the `while` loop behaves correctly; the conditions are met immediately, and the function returns 0.
*   **Flat Terrain:** If all heights are equal, `trappedWater` remains 0.
*   **Descending/Ascending Slopes:** The algorithm naturally handles these cases as the `max` variables will be updated accordingly, and the subtraction result will remain zero.

---

## Key Insights

### Why the Conditional Logic Works
The "trick" of this implementation is the comparison:
`if (height[left] <= height[right])`
We only care about the *smaller* side. If `height[left]` is smaller than `height[right]`, we know that the water at `left` is trapped by `leftMaxHeight` because there is a taller barrier somewhere to the right (specifically, at least as tall as `height[right]`). This eliminates the need for a separate pass to pre-calculate `max_right`.

### Subtle Nuances
*   **In-Place Mutation:** The algorithm does not modify the input array, making it thread-safe and memory-efficient.
*   **Convergence:** The loop condition `left <= right` is technically robust, though the final iteration where `left == right` results in `max - height` which is correctly `0` (since `height[left] == maxHeight`), meaning it doesn't affect the running sum.
*   **Potential Optimization:** For very large datasets in primitive-heavy systems, this approach is already cache-friendly as it accesses the array linearly, maximizing CPU cache line utilization.

---
