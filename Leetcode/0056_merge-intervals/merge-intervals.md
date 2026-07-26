# merge-intervals

## attempt_1.java
*Style: detailed*

# Technical Reference: Interval Merging Algorithm

## Summary
The solution implements a **Sort-and-Sweep** strategy to solve the classic interval merging problem. The fundamental objective is to consolidate overlapping intervals into their maximal contiguous union. The algorithm relies on the property that sorting intervals by their start times allows us to process the set in a single linear scan, where local decisions (merging or closing) are sufficient to guarantee a globally optimal result.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** The bottleneck is the `Arrays.sort()` invocation, which uses a Dual-Pivot Quicksort (for primitives) or Timsort (for objects/arrays). This requires $O(N \log N)$ comparisons.
*   **Linear Scan:** The subsequent iteration through the array is $O(N)$.
*   **Total:** $O(N \log N) + O(N) = O(N \log N)$.

### Space Complexity: $O(N)$
*   **Sorting Space:** Depending on the JVM implementation, `Arrays.sort` on object arrays may require $O(N)$ stack/auxiliary space.
*   **Auxiliary Data Structure:** The `ArrayList<int[]>` stores up to $N$ intervals in the worst-case scenario (where no intervals overlap), resulting in $O(N)$ space.
*   **Result:** The transformation from `ArrayList` to the primitive `int[][]` array is $O(N)$.

---

## Component Deep Dive

### 1. Pre-processing (Sorting)
The algorithm enforces a canonical order: `inter[i][0] <= inter[i+1][0]`. This invariant ensures that for any interval $i$, we only need to compare it against the `end` boundary of the current "active" merged interval.

### 2. The Sweep Line
The logic maintains two tracking variables: `start` and `end`.
*   **Overlap Condition (`inter[i][0] <= end`):** We have a partial overlap or containment. The critical update is `end = Math.max(end, inter[i][1])`. This is vital because the current interval `i` might be fully contained within the existing `end`, or it might extend the boundary.
*   **Gap Condition (`else`):** When the current interval's start exceeds the existing `end`, the active interval is "sealed." It is pushed to the collector, and the trackers are reset to the current interval.

### 3. Edge Case Handling
*   **Single Interval:** The loop processes the interval correctly, the `else` block is bypassed, and the final `matrix.add` correctly adds the single interval.
*   **Nested Intervals:** `[1, 10], [2, 5]`. Because we take `Math.max(end, inter[i][1])`, the `end` remains 10, correctly subsuming the inner interval.
*   **Consecutive Intervals:** `[1, 2], [2, 3]`. The condition `inter[i][0] <= end` (i.e., `2 <= 2`) triggers the merge, correctly resulting in `[1, 3]`.
*   **Empty Input:** The current code would throw an `ArrayIndexOutOfBoundsException` on `inter[0][0]` if `inter.length == 0`. *Recommendation: Add a guard clause `if (n == 0) return new int[0][0];`*

---

## Key Insights

### 1. Optimization: Primitive vs. Object Sorting
Java’s `Arrays.sort(T[], Comparator)` involves boxing/unboxing overhead if not careful. For extremely large datasets, using a primitive array `int[]` and sorting using a custom sort logic (or flattening the structure) can reduce GC pressure and memory footprint significantly.

### 2. The "Sealing" Mechanism
The `matrix.add(new int[]{start, end});` after the loop is a classic "dangling state" pattern. Beginners often try to merge inside the loop exclusively, but the final interval will always be trapped in the local `start`/`end` variables. The post-loop addition is mandatory.

### 3. Potential Bug: Integer Overflow/Bounds
While this implementation is robust for standard `int` range, if the intervals represent timestamps or coordinates that can approach `Integer.MAX_VALUE`, `end = Math.max(...)` is safe, but be wary of scenarios where input logic might involve interval subtraction or length calculations, which could trigger overflow.

### 4. Logic Nuance
The implementation assumes intervals are well-formed (`start <= end`). If the input contains "inverted" intervals (e.g., `[5, 2]`), the current logic will treat `2` as the `end` and effectively corrupt the merging logic. A pre-validation step or sanitization would be required for untrusted inputs.

---
