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

## standard_two_pointer_optimal.java
*Style: detailed*

# Engineering Deep-Dive: Rescue Boat Optimization

## Summary
The problem is a variation of the **Bin Packing Problem** with a constraint of at most two items per bin. Given that we can only pair at most two people per boat, we utilize a **Greedy Two-Pointer approach**. By sorting the weights, we pair the lightest person (`l`) with the heaviest person (`r`). If they fit within the `limit`, they share a boat. If they do not, the heaviest person must occupy a boat alone, as no one else is light enough to accompany them. This approach ensures that we minimize the total number of boats by maximizing the occupancy of each boat relative to the capacity constraint.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** The `Arrays.sort()` implementation in Java (Dual-Pivot Quicksort for primitives) dominates the execution time, resulting in $O(N \log N)$.
*   **Two-Pointer Traversal:** The `while` loop iterates through the array exactly once, visiting each element at most once. This portion is $O(N)$.
*   **Total:** $O(N \log N + N) \approx \mathbf{O(N \log N)}$.

### Space Complexity: $O(\log N)$ to $O(N)$
*   Depending on the JDK implementation of `Arrays.sort()`, the space complexity for the sorting algorithm typically ranges from $O(\log N)$ to $O(N)$ due to stack recursion or auxiliary buffers. The pointer variables themselves are $O(1)$.

## Component Deep Dive

### 1. Sorting Strategy
The efficacy of the greedy choice depends entirely on the sorted order. By sorting ascendingly, we establish a monotonic relationship where `people[r]` is the most difficult element to pair. The strategy of "heaviest + lightest" is optimal because:
*   If the heaviest person (`r`) cannot pair with the lightest person (`l`), they cannot pair with *anyone* in the array. 
*   Therefore, `r` must be decremented alone (creating a new boat).

### 2. The Two-Pointer Logic
```java
while (l <= r) {
    if (people[l] + people[r] <= limit) {
        l++; // Lightest person is "rescued" with the heaviest
    }
    r--; // Heaviest person is always "rescued" (either alone or with l)
    boats++;
}
```
*   **The `l <= r` condition:** This covers the base case where a single person remains. When `l == r`, the person occupies one final boat.
*   **Pointer Advancement:** The `r--` occurs in every iteration because the heaviest person is guaranteed to be assigned to a boat regardless of whether they are paired. The `l++` is conditional, acting as a greedy match.

### 3. Edge-Case Handling
*   **Empty Array:** If `people` is empty, the loop does not execute; `boats` remains 0. Correct.
*   **Single Person:** If `people.length == 1`, the loop runs once, `r--` executes, and `boats` becomes 1. Correct.
*   **Unpairable elements:** If all `people[i] > limit`, the logic still holds (though the problem constraints typically imply `people[i] <= limit`), as each person would simply increment the `boats` counter individually.

## Key Insights

*   **Greedy Correctness:** This is a classic greedy problem where local optimal choices (pairing the heaviest with the lightest possible) lead to a global optimum. This works specifically because the bin capacity is limited to **exactly two**. If the capacity were $k > 2$, this greedy approach would fail, and we would likely require a more complex approach (e.g., Min-Priority Queues or dynamic programming).
*   **Performance Nuance:** Using `Arrays.sort()` on an array of primitives (`int[]`) is significantly faster than sorting an `Integer[]` object array. Primitive sorting avoids overhead from object metadata and cache-unfriendly pointer dereferencing.
*   **Subtle Bug Warning:** Do not attempt to use a `for` loop that increments `l` and decrements `r` based on index logic alone. The two-pointer `while` loop is safer because it correctly handles the scenario where the pointers collide (i.e., when only one person is left in the middle of the sorted list).

---
