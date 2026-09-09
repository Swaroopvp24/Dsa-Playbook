# find-pivot-index

## attempt_1.java
*Style: detailed*

# Deep-Dive Reference: Pivot Index Calculation

## 1. Summary
The `pivotIndex` solution employs a **Prefix Sum (Cumulative Sum)** technique to transform an $O(N^2)$ brute-force comparison (calculating subarray sums for every index) into an $O(N)$ linear-time lookup. 

By pre-calculating the cumulative sum of the array, we represent the sum of any subarray $[0, i-1]$ as `prefixSum[i]`. This allows for $O(1)$ constant-time retrieval of left and right sums at any arbitrary pivot point $i$, enabling a single-pass verification.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Prefix Calculation:** A single loop of length $N$ iterates through the array to populate the `prefixSum` array.
*   **Pivot Verification:** A second loop of length $N$ performs constant-time arithmetic operations (`leftSum` and `rightSum` calculation via indexing).
*   **Total:** $2N \rightarrow O(N)$.

### Space Complexity: $O(N)$
*   **Auxiliary Space:** The algorithm allocates an array of size $N+1$ to store prefix sums.
*   **Optimization Note:** While this implementation is $O(N)$, it is possible to achieve $O(1)$ auxiliary space by calculating the `totalSum` first and then maintaining a running `leftSum` variable during the second pass. The current approach prioritizes readability and cache-friendly sequential access over memory minimization.

## 3. Component Deep Dive

### Data Structure: `prefixSum`
The array is sized $N+1$ to handle index boundaries gracefully.
*   `prefixSum[0]` is initialized to `0` (default Java array behavior).
*   `prefixSum[i]` stores the sum of `nums[0...i-1]`.
*   This structure eliminates the need for conditional checks at the array boundaries ($i=0$ or $i=n-1$), as `leftSum` and `rightSum` calculations are derived purely from array offsets.

### Logic Flow
1.  **Normalization:** The `prefixSum` array acts as a cache. `prefixSum[n]` represents the total sum of the array.
2.  **Boundary Evaluation:**
    *   For any index $i$:
        *   **Left Sum:** `prefixSum[i]` captures all elements from index $0$ to $i-1$.
        *   **Right Sum:** `prefixSum[n] - prefixSum[i+1]` captures all elements from index $i+1$ to $n-1$.
3.  **Short-circuit:** The loop returns the first index $i$ that satisfies the equilibrium condition, adhering to the problem requirement for the leftmost index.

### Edge Case Handling
*   **Empty Array:** The code initializes `n = nums.length`. If `n=0`, the loops are skipped, returning `-1` correctly.
*   **Single Element:** If `n=1`, `prefixSum` is `[0, nums[0]]`. The loop runs for $i=0$: `leftSum` is `prefixSum[0]` ($0$), `rightSum` is `prefixSum[1] - prefixSum[1]` ($0$). Returns $0$, which is correct (sum of elements to the left/right of index 0 is 0).
*   **Negative Integers:** The prefix sum approach remains mathematically sound even with negative values, as the cumulative addition preserves the linear relationship.

## 4. Key Insights

*   **Integer Overflow:** A potential risk exists if the sum of elements in `nums` exceeds `Integer.MAX_VALUE`. In a production environment with large constraints, `prefixSum` should be declared as `long[]` to prevent overflow errors.
*   **Readability vs. Memory:** This implementation is highly readable but consumes $O(N)$ extra space. If memory pressure is a concern, calculate the `totalSum` using a single variable pass, then subtract current elements from the `totalSum` as you traverse to find the pivot.
*   **Cache Locality:** By using an array for prefix sums, we ensure high spatial locality during the second pass. This is generally more cache-friendly than re-calculating sums via nested loops, which would jump across memory addresses or perform redundant redundant additions.
*   **Index Alignment:** The "off-by-one" mapping (where `prefixSum[i+1]` corresponds to `nums[i]`) is a classic pattern in prefix sum problems; ensuring this alignment is correct is critical to avoid off-by-one errors in `rightSum` calculation.

---
