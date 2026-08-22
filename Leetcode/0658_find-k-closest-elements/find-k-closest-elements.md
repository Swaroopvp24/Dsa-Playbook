# find-k-closest-elements

## attempt_1_sorting.java
*Style: concise*

### Study Notes: Find K Closest Elements

#### Overview
This solution identifies the `k` elements in a sorted array that are closest to a target value `x`. It achieves this by sorting all array elements based on their absolute difference from `x`, then returning the first `k` elements in their original relative order.

#### Key Logic
*   **Custom Comparator**: 
    *   Sorts primarily by distance: `abs(num - x)`.
    *   Sorts secondarily by value: `a - b` (tie-breaker for equal distances).
*   **Post-processing**: Uses `list.subList(0, k)` to extract the result set, then performs `Collections.sort()` to restore the ascending order required by the problem's output contract.

#### Complexity Note
*   **Time**: $O(N \log N)$ due to the full array sort. 
    *   *Optimization Note:* This can be reduced to $O(\log N + k)$ using binary search to find the closest element and a two-pointer approach to expand the window.
*   **Space**: $O(N)$ to store the list copy.

---
