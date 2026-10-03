# monotonic-array

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: Monotonic Array Validation

## 1. Summary
The `isMonotonic` implementation utilizes a **single-pass linear scan** to determine the ordering property of an array. The algorithm maintains two boolean flags, `isIncreasing` and `isDecreasing`, representing the hypothesis that the array follows a non-decreasing ($a[i] \le a[i+1]$) or non-increasing ($a[i] \ge a[i+1]$) sequence, respectively. By iteratively checking against these predicates, the algorithm identifies if the input violates one, both, or neither constraint.

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Analysis:** The function performs a single iteration through the array of size $n$, starting from index 1. Each element comparison is a constant-time $O(1)$ operation. 
*   **Why:** There are no nested loops or recursive calls. Even in the worst-case scenario (e.g., an array that is not monotonic until the final element), the algorithm must visit every index exactly once.

### Space Complexity: $O(1)$
*   **Analysis:** The memory footprint is constant. 
*   **Why:** We allocate exactly two `boolean` primitives (`isIncreasing`, `isDecreasing`) regardless of the input array size. No auxiliary data structures (like arrays, stacks, or sets) are employed, making this implementation memory-efficient.

## 3. Component Deep Dive

### Core Logic: State Narrowing
The algorithm starts with the assumption that the array could be both non-decreasing and non-increasing (which is true for an empty array, a single-element array, or an array consisting of identical elements). 

*   **`isIncreasing` state:** Transitions to `false` the moment a descent ($a[i] < a[i-1]$) is detected.
*   **`isDecreasing` state:** Transitions to `false` the moment an ascent ($a[i] > a[i-1]$) is detected.

### Edge-Case Handling
*   **Single-element Arrays:** The `for` loop condition `i < nums.length` will be false immediately (if length is 1). The method returns `true || true`, which correctly identifies a single-element array as monotonic.
*   **Empty Arrays:** Similar to single-element arrays, the loop is skipped, returning `true`.
*   **Arrays with Identical Elements:** If all elements are equal, neither the `nums[i] < nums[i - 1]` nor the `nums[i] > nums[i - 1]` condition will ever evaluate to `true`. Both flags remain `true`, correctly returning `true`.
*   **Plateaus:** Arrays like `[1, 2, 2, 3]` are handled gracefully; the non-decreasing check remains true because the condition `nums[i] < nums[i - 1]` is never triggered by `2 < 2`.

## 4. Key Insights

*   **Short-Circuiting Opportunity:** While the current implementation iterates to the end, one could theoretically optimize for "early exit." If both `isIncreasing` and `isDecreasing` become `false` mid-iteration, the function can immediately return `false`. Adding this check inside the loop:
    ```java
    if (!isIncreasing && !isDecreasing) return false;
    ```
    This provides a performance boost for non-monotonic arrays, though it does not improve the asymptotic $O(n)$ worst-case.

*   **Boolean Logic:** The expression `return isIncreasing || isDecreasing;` is mathematically sound. A monotonic array is defined as non-decreasing **or** non-increasing. By defaulting both flags to `true`, we effectively treat the array as "vacuously monotonic" until evidence suggests otherwise.

*   **Instruction Pipeline:** Because the conditions are branch-heavy, the CPU branch predictor will handle this efficiently for highly monotonic data (common in time-series data or sorted buffers). If the data is highly volatile (random), branch mispredictions may slightly degrade performance, but the $O(n)$ operation remains negligible for standard memory-resident arrays.

---
