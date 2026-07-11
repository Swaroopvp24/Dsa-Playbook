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

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Two Sum Solution
## Summary
The provided Java solution utilizes a hash-based approach to solve the classic "Two Sum" problem. The algorithm iterates through the input array, maintaining a `HashMap` to store elements and their indices. For each element, it checks if its complement (i.e., the value needed to reach the target sum) is present in the map. If found, it returns the indices of the complement and the current element. This approach leverages the constant-time lookup capabilities of hash tables to achieve an efficient solution.

## Complexity Analysis
### Time Complexity
The time complexity of this solution is **O(n)**, where **n** is the length of the input array `nums`. This is because the algorithm performs a single pass through the array, and the `HashMap` operations (`containsKey`, `get`, and `put`) have an average time complexity of **O(1)**. Although there is a loop iterating over the array, the constant-time hash table operations dominate the overall time complexity.

### Space Complexity
The space complexity is also **O(n)**, as in the worst-case scenario, all elements from the input array might be stored in the `HashMap`. The size of the map grows linearly with the input size, hence the linear space complexity.

## Component Deep Dive
### Critical Functions
* `containsKey(compl)`: This method checks if the complement of the current element is present in the `HashMap`. It leverages the hash table's constant-time lookup capability, allowing the algorithm to efficiently identify potential solutions.
* `a.put(nums[i], i)`: This line stores the current element and its index in the `HashMap`. It ensures that each element is associated with its correct index, enabling the algorithm to retrieve the indices of the two sum elements once the complement is found.

### Data Structures
* `HashMap<Integer, Integer> a`: This hash table is the core data structure in the solution. It maps each element from the input array to its index, facilitating constant-time lookups and efficient storage of intermediate results.

### Edge-Case Handling
* The solution handles the case where no two sum solution exists by returning an array containing `-1`. This ensures that the algorithm provides a clear indication when no valid solution is found.
* The use of a `HashMap` implies that duplicate elements in the input array will overwrite previous entries. However, this is not a concern in the context of the two sum problem, as the solution only requires finding one pair of elements that sum to the target.

## Key Insights
* **Hash Collisions**: Although the `HashMap` provides an average time complexity of **O(1)**, hash collisions can occur, potentially leading to slower performance. However, this is mitigated by the use of a good hash function and the fact that the input array is not excessively large.
* **Early Termination**: The algorithm does not explicitly check for early termination conditions, such as when the input array has less than two elements. While this does not affect the correctness of the solution, it may lead to unnecessary computations in edge cases.
* **Input Validation**: The solution assumes that the input array is non-null and contains at least one element. In a real-world scenario, additional input validation and error handling mechanisms should be implemented to ensure the algorithm's robustness and reliability.

---
