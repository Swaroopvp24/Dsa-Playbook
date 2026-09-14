# find-in-mountain-array

## standard_binary_search.java
*Style: detailed*

# Deep-Dive Reference: Mountain Array Search

## Summary
The solution employs a **three-phase divide-and-conquer strategy** utilizing binary search to find a `target` within a bitonic array (a mountain array). Given the array's unimodal structure (strictly increasing until a peak, then strictly decreasing), the problem is partitioned into:
1. **Peak Identification:** Locating the global maximum.
2. **Ascending Search:** Standard binary search on $[0, \text{peakIndex}]$.
3. **Descending Search:** Inverted binary search on $[\text{peakIndex} + 1, \text{length} - 1]$.

This approach avoids linear scans, leveraging the property that the array can be treated as two independent sorted subarrays.

---

## Complexity Analysis

### Time Complexity: $O(\log N)$
*   **Peak Finding:** A binary search over the range $[0, N-1]$ performs $O(\log N)$ comparisons.
*   **Subarray Searches:** We perform two binary searches, each $O(\log N)$.
*   **Total:** $O(\log N) + O(\log N) + O(\log N) = O(\log N)$. Since the `MountainArray.get()` interface is effectively an $O(1)$ abstraction, the total complexity remains logarithmic relative to the array length $N$.

### Space Complexity: $O(1)$
*   The algorithm operates using a fixed number of pointer variables (`left`, `right`, `middle`, `peakIndex`). No auxiliary data structures or recursion stacks are used, ensuring constant space requirements.

---

## Component Deep Dive

### 1. `findPeakIndex` (Peak Identification)
This function utilizes the property that if `arr[mid] < arr[mid + 1]`, the slope is positive, meaning the peak *must* exist at `mid + 1` or further right. 
*   **Edge Case Handling:** By using `right = middle` (instead of `middle - 1`), the algorithm safely preserves the potential peak index during narrowing. The termination condition `left < right` guarantees convergence to a single index where the invariant `arr[left] >= arr[left + 1]` is eventually violated or met.

### 2. `binarySearch` (Polymorphic Logic)
This is a unified implementation for both sorted halves.
*   **Logic Branching:** The `boolean ascending` flag toggles the comparison operator behavior.
    *   **Ascending:** If `midVal < target`, the search window shifts right (`left = mid + 1`).
    *   **Descending:** If `midVal < target`, the search window shifts left (`right = mid - 1`), because a value smaller than the target in a descending sequence implies the target is "higher up" toward the peak (leftward).
*   **Return Priority:** The main function calls the ascending search first. This explicitly handles the requirement to return the *smallest* index if the `target` appears twice (once on each slope).

---

## Key Insights & Nuances

### 1. Minimizing API Calls
The interface `mountainArray.get()` is the primary bottleneck. In a production system, these are often hidden network or disk I/O calls. 
*   **Optimization:** A standard implementation of `binarySearch` that calls `get()` twice per iteration (once for comparison, once for range adjustment) would be inefficient. Note that this implementation caches `middleValue` once per iteration, keeping constant factors low.

### 2. Termination Safety
The `findPeakIndex` loop logic is inherently safe against `ArrayIndexOutOfBoundsException`. By checking `mid < mid + 1` and limiting the range to `arrayLength - 1`, we ensure `middle + 1` is always valid.

### 3. Potential Pitfalls
*   **Range Splitting:** The split point between the two binary searches must be precise: `[0, peakIndex]` and `[peakIndex + 1, N - 1]`. If the peak is the last element, the second call to `binarySearch` will receive `(N, N-1)` as bounds. The `while (left <= right)` condition in the search correctly handles this by skipping execution immediately.
*   **Integer Overflow:** The calculation `int middle = left + (right - left) / 2` is used instead of `(left + right) / 2`. This is a critical defensive coding practice to prevent overflow when dealing with extremely large index values.

---
