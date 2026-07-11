# two-sum

## attempt_1.java
*Style: concise*

### Two Sum Implementation

**Purpose**
Finds the indices of two numbers in an array that add up to a specific `target`. It achieves $O(n)$ time complexity by using a hash map to trade space for lookup speed.

**Key Components**
*   `HashMap<Integer, Integer> a`: Stores the mapping of `{value: index}`.
*   `target - nums[i]`: Calculates the required "complement" needed to reach the target sum.

**Notes**
*   **One-pass approach:** By checking for the complement in the map *before* adding the current element, the algorithm naturally handles the requirement of using two distinct elements (i.e., you cannot use the same element twice because it hasn't been added to the map yet).
*   **Edge Case:** Returns `{-1}` if no valid pair is found. 
*   **Optimization:** This approach avoids the $O(n^2)$ nested loop typical of a brute-force solution.

---
