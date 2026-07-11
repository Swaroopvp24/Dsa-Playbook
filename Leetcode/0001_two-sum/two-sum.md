# two-sum

## attempt_1.java
*Style: concise*

### Two Sum Implementation

**Purpose**
Finds the indices of two numbers in an array that sum to a specific target value. It performs this in a single pass with $O(n)$ time complexity using a hash map for $O(1)$ lookups.

**Key Components**
*   `twoSum(int[] nums, int target)`: Main logic method; iterates through the array once.
*   `HashMap<Integer, Integer> a`: Stores the `value` as the key and its `index` as the value for rapid lookups of the "complement."

**Logic Notes**
*   **Complement Strategy:** Instead of nested loops ($O(n^2)$), calculate the `complement` (`target - current_value`). If the complement exists in the map, you have found the pair.
*   **Single Pass Optimization:** By adding the current element to the map *after* checking for the complement, the code implicitly handles the requirement of not using the same element twice (and avoids edge cases with duplicate numbers).
*   **Edge Case:** Returns `{-1}` if no valid pair is found.

---
