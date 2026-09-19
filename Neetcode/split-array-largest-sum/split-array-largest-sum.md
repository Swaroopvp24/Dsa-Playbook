# split-array-largest-sum

## binary_search_on_answer(nlogn).java
*Style: detailed*

# Technical Reference: Split Array Largest Sum (Minimax Approach)

## 1. Summary
The `splitArray` problem is a classic optimization challenge classified as a **Minimax Problem** (minimizing the maximum value). The solution employs **Binary Search on the Answer Space**, coupled with a **Greedy Verification Algorithm**. 

Instead of traditional dynamic programming (which would yield $O(n^2 \cdot k)$), this approach defines the range of possible solutions—bounded by the maximum element in the array (lower bound) and the total sum of the array (upper bound)—and performs a binary search to find the smallest valid sum that satisfies the $k$ subarray constraint.

## 2. Complexity Analysis

### Time Complexity: $O(N \log(S - M))$
*   **$N$**: Number of elements in `nums`.
*   **$S$**: Total sum of elements.
*   **$M$**: Maximum element in the array.
*   **Reasoning**: The binary search space spans from $M$ to $S$. The number of iterations in binary search is logarithmic, specifically $\log(\sum nums - \max(nums))$. Inside each iteration, the `countSubarrays` helper function performs a single pass over the array, taking $O(N)$ time.

### Space Complexity: $O(1)$
*   **Reasoning**: The algorithm performs all operations in-place. It only allocates a constant amount of stack/heap space for primitives (`left`, `right`, `mid`, `currentSum`, etc.), regardless of input size.

## 3. Component Deep Dive

### `countSubarrays(int[] nums, int maxAllowedSum)`
*   **Algorithmic Technique**: Greedy partitioning.
*   **Functionality**: This is a predicate function used to validate if a specific "max sum" is feasible. It iterates through the array and greedily packs elements into a subarray until the capacity is reached.
*   **Edge Case Handling**:
    *   **Implicit Validation**: If an individual element `num` is greater than `maxAllowedSum`, the greedy logic will naturally force a new subarray creation for that element alone. Note that the initial binary search lower bound (`largestElement`) ensures that `num <= maxAllowedSum` always holds, preventing illegal states.

### `splitArray(int[] nums, int k)`
*   **Search Range Initialization**:
    *   `left = max(nums)`: If the limit were smaller than the largest element, it would be impossible to include that element in any subarray.
    *   `right = sum(nums)`: The worst-case scenario where $k=1$.
*   **Binary Search Logic**:
    *   We seek the *smallest* value $X$ such that `countSubarrays(X) <= k`.
    *   When `requiredSubarrays <= k`, it means the current `mid` is a valid capacity. We store this potential answer (implicitly handled by the `left` pointer) and attempt to shrink the capacity further by setting `right = mid - 1`.
    *   When `requiredSubarrays > k`, the current `mid` is too small to accommodate the array elements within $k$ buckets. We increase the capacity by setting `left = mid + 1`.

## 4. Key Insights

### Monotonicity
The core of this solution relies on the **monotonicity of the predicate function**. As the `maxAllowedSum` increases, the `requiredSubarrays` count monotonically decreases (or stays the same). This property is strictly required for binary search to function correctly.

### Subtle Bugs & Robustness
*   **Integer Overflow**: The provided code calculates `mid = left + (right - left) / 2`. This is a professional-grade pattern to avoid `(left + right) / 2` overflow issues common in languages with fixed-width integers, though `totalSum` itself could potentially overflow if `nums[i]` are near `Integer.MAX_VALUE`. For production systems, one should consider using `long` for the sum bounds.
*   **The $k > N$ constraint**: The check `if (k > nums.length)` is essential. While the greedy function would return a count, logically, you cannot partition an array into more subarrays than there are elements (assuming non-empty subarrays).
*   **The Search Termination**: Upon completion of the `while` loop, `left` will converge to the lowest possible valid `mid`. This occurs because `left` is updated to `mid + 1` only when a value is strictly insufficient, ensuring it points to the boundary of the feasibility range.

---
