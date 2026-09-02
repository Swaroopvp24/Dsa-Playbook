# search-in-rotated-sorted-array-ii

## optimal_binary_search.java
*Style: detailed*

# Engineering Deep-Dive: Search in Rotated Sorted Array II

## Summary
The solution implements a modified **Binary Search** algorithm to find a target value in a rotated sorted array that contains duplicates. In a standard rotated sorted array (no duplicates), one half is always guaranteed to be sorted. The presence of duplicates breaks this guarantee, as `nums[left] == nums[middle]` creates ambiguity regarding which side of the array is monotonically increasing. The algorithm resolves this by linearly shrinking the search space when ambiguity occurs, then performing a standard binary partition strategy on the identified sorted half.

---

## Complexity Analysis

### Time Complexity
*   **Best/Average Case: $O(\log n)$**
    In cases where there are few or no duplicates, the algorithm performs standard binary search, effectively halving the search space in each iteration.
*   **Worst Case: $O(n)$**
    This occurs when the array contains many duplicates, such as `[1, 1, 1, 1, 2, 1, 1]`. When `nums[left] == nums[middle]`, the algorithm simply increments `left`, reducing the search space by only one element. In the worst case, the algorithm degrades to a linear scan.

### Space Complexity
*   **$O(1)$**
    The algorithm operates in-place using only a constant amount of extra space for pointer variables (`left`, `right`, `middle`). No recursive stack or additional data structures are required.

---

## Component Deep Dive

### 1. The Ambiguity Guard (`nums[left] == nums[middle]`)
This is the most critical block. Unlike the standard "Search in Rotated Sorted Array" problem, duplicates force the algorithm to handle the case where the sorted half cannot be identified. 
*   **Logic:** By incrementing `left`, we remove one duplicate. We do not risk missing the target because `nums[left]` was already compared against `target` implicitly via the earlier logic or the initial `middle` check.
*   **Edge Case:** If the array is `[1, 1, 1, 2, 1]`, the `left++` maneuver allows the algorithm to eventually identify the sorted segment `[2, 1]` or `[1, 2]` once the duplicate noise is stripped away.

### 2. Identifying the Sorted Half
The algorithm relies on the observation that in a rotated sorted array, at least one half (left or right of the midpoint) must be sorted.
*   **Left Half Sorted:** Determined by `nums[left] <= nums[middle]`. If true, we check if the target falls within the inclusive bounds `[nums[left], nums[middle])`. 
*   **Right Half Sorted:** If the left half is not sorted, the right half *must* be. We then check if the target falls within `(nums[middle], nums[right]]`.

### 3. Pointer Calculation
`int middle = left + (right - left) / 2;`
Using this instead of `(left + right) / 2` is a defensive programming best practice to prevent integer overflow when dealing with extremely large arrays (where `left + right` might exceed `Integer.MAX_VALUE`).

---

## Key Insights

### Performance Nuances
*   **Branching:** The logic follows a `if-else if-else` structure. Because we prioritize the equality check (`nums[left] == nums[middle]`), we effectively handle the worst-case duplicate scenario before attempting the binary search optimization.
*   **Redundancy:** Note that `nums[middle] == target` is checked at the start of every loop. This ensures that even if we are "peeling" away duplicates, we catch the target as soon as the `middle` pointer lands on it.

### Subtle Bugs & Traps
*   **Off-by-one errors:** The conditions `nums[left] <= target < nums[middle]` and `nums[middle] < target <= nums[right]` are strictly bounded to ensure that the target is *strictly within* the sorted range. If the target is outside this range, the search space must shift to the potentially non-sorted, rotated half.
*   **Infinite Loops:** The condition `left <= right` is correct. If the condition were `left < right`, the algorithm would fail for single-element arrays or cases where the target is the only element remaining in the search space.
*   **Data Distribution:** It is important to realize that `left++` is a **linear** fallback. While this technically degrades the complexity to $O(n)$, it is the mathematically required operation to maintain correctness when the rotation pivot is hidden by duplicated values. There is no faster way to resolve this ambiguity without further information.

---
