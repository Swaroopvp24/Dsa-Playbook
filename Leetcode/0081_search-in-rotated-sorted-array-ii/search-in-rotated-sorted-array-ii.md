# search-in-rotated-sorted-array-ii

## optimal_binary_search.java
*Style: detailed*

# Deep-Dive: Rotated Sorted Array Search (with Duplicates)

## 1. Summary
This algorithm implements a modified **Binary Search** to locate an element in a rotated sorted array that may contain duplicates. In a standard sorted array, binary search relies on the monotonicity of the range to discard half the search space. In a rotated sorted array, the array is effectively two sorted subarrays joined at a pivot. 

The approach maintains the invariant that at least one half of the array (either `[left, middle]` or `[middle, right]`) remains sorted. By checking which side is sorted and comparing the `target` against the boundaries of that side, we can reduce the search space logarithmically. The inclusion of duplicates necessitates a linear-time fallback (the `nums[left] == nums[middle]` check), which breaks the standard $O(\log n)$ guarantee in worst-case scenarios.

---

## 2. Complexity Analysis

### Time Complexity
*   **Average Case: $O(\log n)$**
    In most scenarios, the search space is halved every iteration, following the standard binary search recurrence $T(n) = T(n/2) + O(1)$.
*   **Worst Case: $O(n)$**
    When the array contains many duplicates (e.g., `[1, 1, 1, 0, 1]`), the condition `nums[left] == nums[middle]` may be met frequently. If the pivot and boundaries are identical, the algorithm can only increment `left` by 1, effectively performing a linear scan. This occurs when the distribution of values prevents the identification of a sorted subarray.

### Space Complexity
*   **$O(1)$**
    The algorithm utilizes an iterative approach with a constant number of pointer variables (`left`, `right`, `middle`). No auxiliary data structures or recursive stack space are required.

---

## 3. Component Deep Dive

### The Ambiguity of Duplicates
The core challenge in the presence of duplicates is that `nums[left] == nums[middle]` makes it impossible to determine if the rotation pivot lies within the left or right segment. 
*   **Example:** `[1, 0, 1, 1, 1]` vs `[1, 1, 1, 0, 1]`.
*   **Handling:** The code performs an explicit check `if (nums[left] == nums[middle]) { left++; }`. This is a defensive move that shrinks the problem size by one, effectively "peeling" away the ambiguous boundary until the sorted property becomes discriminable.

### Partitioning Logic
The logic hinges on identifying the "clean" side:
1.  **Left Side Sorted (`nums[left] <= nums[middle]`):** If this holds, we verify if `target` resides within `[nums[left], nums[middle])`. If it does, we discard the right; otherwise, we must look in the right.
2.  **Right Side Sorted (Implicit `else`):** If the left side isn't sorted, the pivot *must* be in the left, implying the right segment is strictly sorted. We then verify if `target` resides within `(nums[middle], nums[right]]`.

### Edge-Case Handling
*   **Single Element:** The `while (left <= right)` condition ensures the loop terminates correctly for arrays of size 1.
*   **Target Not Present:** The loop gracefully terminates when `left > right`, returning `false`.
*   **Fully Sorted/Non-Rotated:** The logic defaults to standard binary search behavior, correctly handling the case where the pivot is at index 0.

---

## 4. Key Insights

*   **The "Sorted" Property:** A crucial nuance is understanding that in a rotated sorted array, at least one half of the current search interval is guaranteed to be sorted. This is the mathematical invariant that makes logarithmic search possible.
*   **Why `left++` is sufficient:** In the `nums[left] == nums[middle]` scenario, we aren't necessarily losing the target if we increment `left`. We are simply narrowing the search range. Since `nums[middle]` is also equal to the target value (if we consider the possibility of target being at `left`), but we haven't found it yet, moving `left` by one is a safe way to resolve the indeterminacy. 
*   **Performance Nuance:** Avoid using `(left + right) / 2` to calculate the middle index. Using `left + (right - left) / 2` is a standard "Senior Staff" practice to prevent **integer overflow** when `left` and `right` are large, which would occur if the array size approaches `Integer.MAX_VALUE`.
*   **Subtle Bug Trap:** Do not swap the order of checks. Checking for `nums[left] == nums[middle]` must precede the range-based sorting checks. If not, the algorithm may incorrectly categorize an ambiguous range as sorted, leading to wrong-half truncation and failure to find the target.

---
