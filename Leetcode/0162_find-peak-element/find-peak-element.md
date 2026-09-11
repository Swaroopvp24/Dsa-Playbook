# find-peak-element

## attempt_1.java
*Style: detailed*

# Deep-Dive Reference: Peak Element Discovery

## 1. Summary
The `findPeakElement` implementation utilizes a **Modified Binary Search** technique to identify a local maximum in an unsorted array. The core observation is that if an element is smaller than its right neighbor, a peak must exist somewhere to the right (due to the boundary conditions where $nums[-1] = -\infty$ and $nums[n] = -\infty$). By iteratively discarding the half that cannot contain a peak, we achieve logarithmic search time without requiring the array to be sorted.

## 2. Complexity Analysis

### Time Complexity: $O(\log n)$
*   **Derivation:** At each iteration of the `while` loop, the search space $N$ is halved. 
*   **Justification:** The algorithm performs a constant number of comparisons and arithmetic operations per iteration. The recurrence relation is $T(n) = T(n/2) + O(1)$, which resolves to $O(\log n)$ via the Master Theorem.

### Space Complexity: $O(1)$
*   **Derivation:** The solution is strictly iterative, utilizing only a fixed set of integer pointers (`l`, `r`, `m`).
*   **Justification:** No auxiliary data structures or recursive call stacks are employed, maintaining constant space overhead regardless of input size.

## 3. Component Deep Dive

### Boundary Handling
The code explicitly manages index boundaries to prevent `ArrayIndexOutOfBoundsException`:
*   **Index 0:** Checks if `nums[0] > nums[1]`. If false, it implies a slope moving upward, so the search shifts right (`l = m + 1`).
*   **Index $n-1$:** Checks if `nums[n-1] > nums[n-2]`. If false, the peak must be to the left, so the search shifts left (`r = m - 1`).

### The Peak Decision Logic
The central branching logic relies on the slope of the neighborhood:
1.  **Peak Verification:** `nums[m] > nums[m+1] && nums[m] > nums[m-1]` confirms $m$ is a peak.
2.  **Ascending Slope:** If `nums[m+1] > nums[m]`, the sequence is increasing. An increasing sequence must eventually hit a peak or reach the end of the array, both of which are guaranteed to be "peaks" under the provided problem definition.
3.  **Descending Slope:** If the sequence is decreasing at $m$, a peak is guaranteed to exist at $m$ or to the left of $m$.

### Edge Case Handling
*   **Single Element Arrays:** Handled explicitly at the start of the loop (`nums.length == 1`). 
*   **Strictly Increasing/Decreasing Arrays:** The binary search naturally converges to the end indices ($0$ or $n-1$) which are treated as valid peaks per the problem's implicit boundary conditions ($nums[-1] = -\infty, nums[n] = -\infty$).

## 4. Key Insights

*   **The "Infinite" Boundary Insight:** This is the most crucial takeaway. Many engineers mistakenly believe the array must be sorted for binary search. Here, we exploit the property that if `nums[m+1] > nums[m]`, the "slope" is positive, guaranteeing a peak exists to the right. We do not need the *global* maximum; we only need a *local* one.
*   **Optimization Nuance:** The `if (nums.length == 1)` check is redundant inside the `while` loop. Moving this check outside the loop would improve efficiency by removing a branch prediction check from every iteration.
*   **Potential Bug/Refinement:** The current implementation uses `if (m == 0)` and `if (m == nums.length - 1)` blocks inside the loop. While correct, this creates unnecessary branching. A more "Senior Staff" approach would be to calculate neighbors as:
    ```java
    long left = (m == 0) ? Long.MIN_VALUE : nums[m - 1];
    long right = (m == nums.length - 1) ? Long.MIN_VALUE : nums[m + 1];
    ```
    This eliminates edge-case branching and simplifies the core logic into a single `if/else` block, improving instruction pipelining and readability.

---
