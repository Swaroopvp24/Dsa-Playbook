# find-k-closest-elements

## sorting_(custom_comparator).java
*Style: detailed*

# Technical Reference: K-Closest Elements via Custom Comparator

## Summary
The provided solution employs a **sorting-based heuristic approach** to identify the $k$ closest elements to a target $x$. The algorithm treats distance as the primary sort key and the numerical value as a tie-breaker. By projecting the input array into a `List`, applying a custom `Comparator`, and performing a partial sort, the solution identifies the optimal subset. Finally, the subset is re-sorted to satisfy the problem's requirement for ascending output order.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Initial Sorting:** Sorting the entire list of size $N$ using `numbers.sort()` invokes Timsort (in Java), which is $O(N \log N)$.
*   **Sub-list creation:** Slicing the first $k$ elements is $O(k)$.
*   **Secondary Sorting:** Sorting the final $k$ elements takes $O(k \log k)$.
*   **Total:** $O(N \log N + k \log k)$, which simplifies to **$O(N \log N)$** as $k \leq N$.

### Space Complexity: $O(N)$
*   **Storage:** The algorithm allocates a new `ArrayList` to store the $N$ integers from the input array.
*   **Auxiliary Space:** Timsort's stack/merge space is $O(N)$ in the worst case.
*   **Resultant List:** The final list consumes $O(k)$ space.
*   **Total:** **$O(N)$**, primarily due to the creation of the `ArrayList` wrapper.

---

## Component Deep Dive

### 1. The Comparator Logic
The core of the selection strategy lies in the custom lambda:
```java
int firstDistance = Math.abs(first - x);
int secondDistance = Math.abs(second - x);
if (firstDistance != secondDistance) return firstDistance - secondDistance;
return first - second;
```
*   **Distance Calculation:** Using `Math.abs` is safe within integer bounds provided the input values don't overflow the difference.
*   **Tie-breaking:** The constraint "smaller number first" when distances are equal is handled by the `first - second` return statement. This ensures deterministic behavior when $|a - x| = |b - x|$.

### 2. Post-Selection Requirement
The problem requires the output to be in **ascending order**. Since the primary sort was based on proximity to $x$, the initial `sort()` operation effectively scrambles the original relative ordering of the elements. Therefore, a secondary `Collections.sort()` is mandatory to satisfy the contract.

---

## Key Insights & Optimization Nuances

### 1. Efficiency Trap: Binary Search vs. Sorting
While this $O(N \log N)$ approach is readable and correct, it is suboptimal for large datasets. Because the input array is typically sorted (or can be treated as a range), the problem can be solved in **$O(\log(N-k) + k)$** time using binary search. 
*   **The Logic:** Instead of sorting, we can find the starting index of the window of size $k$ using binary search on the array, identifying the optimal "left" boundary where `arr[mid] < x - arr[mid + k]`.

### 2. Performance Nuance: Boxing Overhead
*   The use of `List<Integer>` forces **auto-boxing** of `int` primitives into `Integer` objects. For an array of size $N$, this incurs significant memory overhead due to object headers (16-24 bytes per element) and impacts cache locality compared to an `int[]` primitive array.

### 3. Edge-Case Handling
*   **$k = N$:** The algorithm correctly returns the entire array (re-sorted).
*   **$k = 0$:** Returns an empty list.
*   **Negative Numbers:** The logic holds true for negative integers as `Math.abs` handles the range correctly, and the primary comparison is based on distance, not magnitude.

### 4. Subtle Bug Risk
*   **Integer Overflow:** If `first` or `x` are near `Integer.MIN_VALUE` or `Integer.MAX_VALUE`, `Math.abs(first - x)` could potentially overflow. In a production environment, this should be cast to `long` before calculation to ensure stability: `(long)first - x`.

---

## standard_two_pointer(linear_Search).java
*Style: detailed*

# Technical Deep Dive: K-Closest Elements

## Summary
The provided solution employs a **"Find-and-Expand" two-pointer strategy**. It identifies the index of the element closest to the target $x$ using a linear search, then performs a bidirectional expansion to collect the $k$ closest neighbors. The algorithm prioritizes elements by their absolute distance to $x$, with a tie-breaking rule that favors smaller values (the left neighbor). Finally, it reconciles the collection order by sorting the resulting subset.

## Complexity Analysis

### Time Complexity: $O(N + k \log k)$
*   **Initial Scan:** $O(N)$ to locate the starting `closestIndex`.
*   **Expansion:** $O(k)$ to traverse and select $k$ elements via the `while` loop.
*   **Sorting:** $O(k \log k)$ to sort the resulting `closestElements` list.
*   **Total:** $O(N + k \log k)$. While $O(N)$ is acceptable, the $O(k \log k)$ post-processing sort makes this less efficient than an optimal $O(\log N + k)$ binary search approach.

### Space Complexity: $O(k)$
*   The space requirement is dominated by the storage of the resulting `closestElements` list, which requires $O(k)$ space. The auxiliary space used for pointers and scalar variables is $O(1)$.

## Component Deep Dive

### 1. Initial Pivot Selection
The algorithm begins with a linear scan to find the "anchor" point.
*   **Logic:** `Math.abs(x - arr[closestIndex]) > Math.abs(x - arr[i])`
*   **Edge Case:** If the array contains duplicate values or multiple values with equal distance to $x$, the current logic finds the *first* occurrence of the minimum distance index.

### 2. Bidirectional Expansion
The `while` loop utilizes two pointers (`leftIndex`, `rightIndex`) to act as a sliding window expansion.
*   **Boundary Handling:** The logic explicitly checks if `leftIndex` or `rightIndex` have crossed array bounds. This ensures no `ArrayIndexOutOfBoundsException` occurs.
*   **Tie-Breaking:** The condition `Math.abs(x - arr[leftIndex]) <= Math.abs(x - arr[rightIndex])` is critical. By using `<=`, it ensures that if distances are equal, the `leftIndex` (the smaller value) is favored. This satisfies the problem requirement that in case of ties, smaller values are preferred.

### 3. Post-Processing
Since elements are added to the list as they are discovered (which might jump back and forth across the pivot), the resulting list is not necessarily sorted. The `Collections.sort(closestElements)` call enforces the required ascending output order.

## Key Insights & Optimization Nuances

*   **Algorithmic Efficiency:** The current solution is $O(N)$. However, because the input array is sorted (as implied by the nature of "closest elements" problems), a **Binary Search** could locate the pivot in $O(\log N)$. Furthermore, instead of expanding, one could use a **Sliding Window** over the sorted array to find the $k$-length subarray in $O(\log(N-k) + k)$ time, completely avoiding the need for a final $O(k \log k)$ sort.
*   **Tie-Breaking Nuance:** The logic `Math.abs(x - arr[leftIndex]) <= Math.abs(x - arr[rightIndex])` is the "golden rule" of this implementation. If this were strictly `<` (exclusive of equality), the algorithm would fail the requirement to favor smaller values during ties.
*   **Potential Bug/Redundancy:** 
    *   The initial scan is $O(N)$. If the array is already sorted, the linear search for the closest element is an unnecessary bottleneck compared to `Arrays.binarySearch` or a manual binary search implementation. 
    *   If $k = N$, the algorithm performs $O(N)$ work to build the list and then $O(N \log N)$ to sort it, whereas returning the original array would be $O(1)$ (or $O(N)$ for a copy).
*   **Memory Efficiency:** For very large $k$, returning a view or using a deque to append/prepend could potentially avoid the need for the final `Collections.sort` if the logic was adjusted to ensure order preservation during insertion.

---

## standard_two_pointer(sliding_window).java
*Style: detailed*

# Technical Reference: Sliding Window Shrinkage for Closest Elements

## Summary
The provided solution addresses the problem of finding the $k$ closest elements to a target $x$ in a sorted array by utilizing a **two-pointer window contraction strategy**. 

Instead of searching for the insertion point (binary search) or calculating distances for all elements, the algorithm treats the entire array as an initial window $[left, right]$ and iteratively shrinks it. By comparing the absolute differences between the target $x$ and the current boundaries (`arr[left]` and `arr[right]`), the algorithm greedily discards the element that is objectively farther from $x$. Since the array is sorted, this maintains the contiguous property of the result set, eventually leaving exactly $k$ elements that are guaranteed to be the closest.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm initializes two pointers at the ends of the array. In each iteration of the `while` loop, exactly one pointer is incremented or decremented. The loop terminates when `right - left + 1 == k`, meaning we perform exactly $N - k$ operations.
*   **Constraints:** Since $k \le N$, the complexity is effectively linear relative to the size of the input array.

### Space Complexity: $O(k)$
*   **Derivation:** The algorithm operates in-place on the input array (ignoring the space required for the output list). The result list requires $O(k)$ space to store the $k$ identified elements. No auxiliary data structures are used during the processing phase.

---

## Component Deep Dive

### 1. The Contraction Logic
```java
if (Math.abs(x - arr[left]) <= Math.abs(x - arr[right])) {
    right--;
} else {
    left++;
}
```
*   **The Tie-Breaking Rule:** The `Math.abs(x - arr[left]) <= Math.abs(x - arr[right])` condition is critical. By using `<=`, when distances are equal, the algorithm prefers to keep the left element (by decrementing `right`). This aligns with the standard problem requirement that if two numbers have the same distance, the smaller number is preferred.
*   **Mechanism:** Because the array is sorted, if `arr[left]` is closer to `x` than `arr[right]`, then `arr[right]` is guaranteed to be the least optimal element in the current range, allowing for a safe O(1) decision per step.

### 2. Termination Condition
*   The loop condition `right - left >= k` ensures that the window shrinks until exactly $k$ elements remain. Note that the length of the window is `right - left + 1`. When `right - left + 1 == k`, the condition `(k-1) >= k` evaluates to `false`, exiting the loop correctly.

---

## Key Insights & Performance Nuances

### Algorithmic Trade-offs
*   **Binary Search Alternative:** While this solution is $O(N)$, a binary search approach (finding the insertion point and expanding outwards) would yield $O(\log N + k)$. 
    *   **When to use $O(N)$:** This two-pointer approach is significantly more robust and easier to implement correctly. It avoids the complexities of handling edge cases associated with `Arrays.binarySearch` (e.g., target $x$ being outside the bounds of the array).
    *   **When to use $O(\log N + k)$:** If $N$ is massive (millions of elements) and $k$ is very small, the binary search approach will outperform this linear solution.

### Subtle Considerations
*   **Non-Strict Inequality:** The use of `<=` is not arbitrary. In scenarios where `x` is equidistant from two elements, the problem usually defines the smaller value as "closer." By removing the right (larger) value when distances are equal, we preserve the smaller value, satisfying standard competitive programming constraints.
*   **Memory Efficiency:** The approach is highly memory-efficient as it avoids creating intermediate lists or heaps (unlike a PriorityQueue-based solution, which would incur $O(k \log k)$ or $O(N \log k)$ time and $O(k)$ extra space).
*   **Preconditions:** The logic relies entirely on the array being sorted. If the input is unsorted, this algorithm fails; a full sort ($O(N \log N)$) or partial selection algorithm would be required.

---

## binary_search(moveslidingwindow).java
*Style: detailed*

# Technical Reference: Sliding Window Binary Search for Closest Elements

## Summary
The problem asks for $k$ elements in a sorted array that are closest to a target value $x$. The optimal approach treats the desired result as a **sliding window of size $k$** within the array. Since the array is sorted, the closeness of the window to $x$ is monotonic; we can use **binary search on the starting index** of this window $[0, \text{arr.length} - k]$ rather than the elements themselves. By comparing the element immediately to the left of a potential window (`arr[mid]`) with the element immediately to the right (`arr[mid + k]`), we determine if shifting the window right improves proximity to $x$.

## Complexity Analysis

### Time Complexity: $O(\log(N-K) + K)$
*   **Binary Search:** $O(\log(N-K))$, where $N$ is the length of the array and $K$ is the target count. We perform a binary search over the range of possible starting indices, effectively halving the search space in each iteration.
*   **Result Construction:** $O(K)$. After identifying the starting index `left`, we iterate $K$ times to populate the result list.
*   **Total:** $O(\log(N-K) + K)$. This is significantly more efficient than a linear scan ($O(N)$) or a heap-based approach ($O(N \log K)$).

### Space Complexity: $O(1)$ (excluding output)
*   The algorithm operates in-place using only primitive pointers (`left`, `right`, `mid`). The extra space is only utilized for the final list construction, which is required by the problem return type.

## Component Deep Dive

### Binary Search Logic
The condition `x - arr[mid] > arr[mid + k] - x` is the heart of the algorithm.
*   **The Window:** If our window is $[mid, mid + k]$, the element being "pushed out" by a rightward shift is `arr[mid]`, and the element being "pulled in" is `arr[mid + k]`.
*   **The Decision:** 
    *   If `x - arr[mid] > arr[mid + k] - x`, it implies `arr[mid + k]` is closer to $x$ than `arr[mid]` is. Therefore, the current window is suboptimal, and we must shift the starting position to the right (`left = mid + 1`).
    *   If the condition is false, `arr[mid]` is closer to or equidistant to $x$ compared to `arr[mid + k]`. Because the array is sorted, we prefer the smaller element (or the one closer to the left), so we keep `mid` as a potential candidate (`right = mid`).

### Edge Case Handling
*   **$k = N$:** The range `right = arr.length - k` becomes $0$. The loop `while (left < right)` terminates immediately, correctly returning the entire array.
*   **$x$ outside array bounds:**
    *   If $x$ is smaller than all elements, the condition `x - arr[mid] > arr[mid + k] - x` will almost always be false, forcing `right` to collapse to $0$, returning the first $k$ elements.
    *   If $x$ is larger than all elements, the condition will be true, pushing `left` to the end of the search space, returning the last $k$ elements.
*   **Equidistant elements:** The problem specifies that if two numbers have the same difference, the smaller number is preferred. The `<` comparison effectively handles ties by favoring the left side of the window.

## Key Insights

### 1. Monotonicity of Proximity
The genius of this approach is transforming the problem from "find $k$ elements" to "find the optimal starting point." Because the array is sorted, the function $f(i) = |x - arr[i+k]| - |x - arr[i]|$ is monotonically increasing. Binary search is applicable because we are effectively searching for the point where this function crosses zero.

### 2. Avoid Midpoint Overflow
In production environments with very large arrays (approaching `Integer.MAX_VALUE`), `(left + right) / 2` can overflow. A more robust implementation would use:
```java
int mid = left + (right - left) / 2;
```

### 3. Comparison Logic Nuance
Note that `x - arr[mid]` and `arr[mid + k] - x` are essentially comparing absolute differences. By structuring the inequality as `diff_left > diff_right`, we avoid the overhead of `Math.abs()`, which is a micro-optimization that keeps the hot loop branch-prediction friendly.

---
