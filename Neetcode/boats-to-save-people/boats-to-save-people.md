# boats-to-save-people

## standard_two_pointer.java
*Style: detailed*

# Technical Deep Dive: Optimal Rescue Boat Allocation

## Summary
The problem is a variation of the **Bin Packing Problem** with a constraint of at most two items per bin. Given that each boat has a weight capacity `limit` and can carry at most two people, the optimal strategy is a **Greedy Two-Pointer approach**.

The algorithm sorts the population by weight and attempts to pair the lightest person (`people[l]`) with the heaviest person (`people[r]`). If their combined weight is within the `limit`, they share a boat; otherwise, the heaviest person must occupy a boat alone.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** `Arrays.sort(people)` utilizes a Dual-Pivot Quicksort, which has a time complexity of $O(N \log N)$ on average.
*   **Two-Pointer Traversal:** The `while` loop iterates over the array exactly once, resulting in $O(N)$.
*   **Post-processing:** The final `for` loop iterates over the array once, resulting in $O(N)$.
*   **Dominant Term:** $O(N \log N) + O(N) = O(N \log N)$.

### Space Complexity: $O(1)$ or $O(\log N)$
*   Depending on the Java version and the implementation of `Arrays.sort()` (primitive `int[]` sorts typically use Dual-Pivot Quicksort), the auxiliary space complexity for the sort stack is $O(\log N)$.
*   The logic itself uses only a fixed number of pointer/counter variables, requiring $O(1)$ extra space beyond the sort.

## Component Deep Dive

### 1. The Greedy Pairing Strategy
The core logic relies on the fact that the heaviest person (`people[r]`) must be accounted for. To minimize boat count, we prioritize pairing the heaviest person with the lightest available person. If `people[l] + people[r] <= limit`, we maximize boat utility. If not, the heaviest person is "too heavy" to be paired with even the lightest remaining person, forcing them into their own boat.

### 2. State Tracking (`people[i] = -1`)
The solution uses the original `people` array as a bitmask/state tracker by setting paired elements to `-1`. This avoids the space overhead of an auxiliary `boolean[] visited` array. 
*   **Edge Case:** The final loop identifies "orphans"—individuals who were not paired during the two-pointer pass—and increments the count for each.

### 3. Loop Termination and Bounds
*   **The `while (l < r)` condition:** This ensures we only attempt to pair distinct individuals.
*   **The Post-Processing `for` loop:** This is critical. In the provided logic, if the loop terminates early or some individuals are left stranded (because they were the "heaviest" and couldn't fit with anyone else), they must still be counted.

## Key Insights & Performance Nuances

### 1. Subtle Optimization (The "One-Pass" Improvement)
The current implementation can be optimized to be cleaner and faster by eliminating the final `for` loop. Instead of marking elements as `-1`, you can track how many people are successfully paired and subtract that from the total population size:
```java
// Optimized approach:
int boats = 0;
while (l <= r) {
    if (people[l] + people[r] <= limit) l++;
    r--;
    boats++;
}
return boats;
```
*Why this is superior:* The original code performs an $O(N)$ scan after the $O(N)$ two-pointer pass. The optimized version counts the boat as soon as the pointers move, collapsing the logic into a single $O(N)$ pass after sorting.

### 2. Edge Case: Single Person
If `people.length == 1`, the `while` loop condition `l < r` fails immediately. The post-processing loop then counts the single person correctly (`1 <= limit`). The logic is robust for minimal input sets.

### 3. Memory/Mutation Trade-off
Mutating the input array (`people[l] = -1`) is efficient for space but violates functional programming principles. In a production environment with multi-threaded access to the input array, this side effect could cause data races. If the array must be preserved, an $O(N)$ copy is required, increasing space complexity to $O(N)$.

---
