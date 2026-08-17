# boats-to-save-people

## two_pointer_greedysolution.java
*Style: detailed*

# Technical Deep-Dive: Rescue Boats Optimization

## Summary
The problem is a variation of the **Bin Packing Problem** with a constraint of at most two items per bin. Given that we can carry at most two people and they must not exceed the weight `limit`, this specific constraint allows us to deviate from the NP-hard general case and employ a **Greedy Two-Pointer strategy**.

The algorithm sorts the weights to bring the lightest and heaviest individuals to the boundaries. By attempting to pair the lightest person with the heaviest person, we greedily maximize the utility of each boat. If the lightest person is too heavy to fit with the heaviest, the heaviest person is objectively "too heavy" to be paired with anyone else who could also fit with them, necessitating a solo boat.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** The `Arrays.sort()` method in Java uses a Dual-Pivot Quicksort for primitives, resulting in $O(N \log N)$ average and worst-case time complexity.
*   **Two-Pointer Traversal:** Once sorted, the algorithm iterates through the array exactly once. The pointers `l` and `r` traverse the array linearly, performing constant time operations in each iteration. This part is $O(N)$.
*   **Dominant Factor:** Since $N \log N > N$, the total time complexity is $O(N \log N)$.

### Space Complexity: $O(1)$ to $O(N)$
*   **Auxiliary Space:** The algorithm uses a fixed number of integer pointers (`l`, `r`, `boats`), which is $O(1)$.
*   **Sorting Space:** Depending on the JVM implementation and the specific sorting algorithm used by `Arrays.sort()` on primitive `int[]` arrays, it typically requires $O(\log N)$ stack space for recursion. However, some implementations might internally require additional memory, effectively treating it as $O(N)$ in worst-case scenarios for object-based sorting or specific library implementations.

## Component Deep Dive

### 1. The Greedy Strategy (The "Why")
The core insight is that the heaviest person (`people[r]`) must be rescued. There are two scenarios for this individual:
1.  **Paired:** They fit on a boat with the lightest person (`people[l]`).
2.  **Solo:** They do not fit with anyone else (even the lightest person).

If `people[l] + people[r] <= limit`, pairing them is always optimal. Why? Because `people[l]` is the "easiest" person to pair. By using `people[l]` to help move the heaviest person, we effectively "save" a future boat slot. If we didn't pair them, `people[r]` would occupy a boat alone, and `people[l]` would still need a boat later.

### 2. Pointer Logic
*   `l` (Left Pointer): Points to the lightest remaining person.
*   `r` (Right Pointer): Points to the heaviest remaining person.
*   **The Exit Condition:** `l <= r`. When `l == r`, exactly one person remains. They must be assigned a boat, which the loop handles correctly: `r` decrements, `boats` increments, and the loop terminates.

### 3. Edge-Case Handling
*   **Single Person:** If `people.length == 1`, the loop runs once, `r` becomes `-1`, `boats` becomes `1`, and the function returns correctly.
*   **All fit individually:** If `people[l] + people[r] > limit` for all pairs, the `if` block is never entered, `l` stays at `0`, and the loop simply decrements `r` until all boats are assigned (one per person).
*   **Constraints:** The code assumes `limit` is sufficient to carry the heaviest individual. If a single person exceeds the limit, this code does not explicitly handle that error state (it would return an invalid count); typically, constraints guarantee `people[i] <= limit`.

## Key Insights & Nuances

*   **Non-Optimal Pairing:** A common mistake is attempting to pair two "medium" weight people. In this greedy approach, we purposefully ignore pairing two mid-sized people if it means failing to pair the heaviest person. The priority is reducing the number of boats by pairing the *heaviest* with the *lightest*.
*   **Input Modification:** The input array is modified via `Arrays.sort()`. In a production environment, if the order of the input array must be preserved, you must perform a defensive copy (`people.clone()`), which would increase space complexity to $O(N)$.
*   **Dual-Pivot Quicksort:** Note that Java's `Arrays.sort` on primitives is unstable. While not relevant for `int` weights, be aware if this pattern is extended to objects where stability might be required.
*   **Performance:** For extremely large datasets, the $O(N \log N)$ bottleneck is the sort. If the range of weights (the `limit`) is very small compared to $N$, a Counting Sort could reduce this to $O(N + \text{limit})$, though this is rarely necessary given the efficiency of `Arrays.sort`.

---
