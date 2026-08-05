# longest-consecutive-sequence

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Longest Consecutive Sequence

## 1. Summary
The solution employs a **Hash-Based Set Traversal** strategy to solve the Longest Consecutive Sequence problem in linear time. The core algorithmic insight is to identify the "sequence start" points—defined as any number $n$ where $(n-1)$ is absent from the set—and only initiate a sequence scan from those points. By ignoring numbers that are clearly internal to a sequence (i.e., those where $n-1$ exists), the algorithm ensures that each element in the array is visited a constant number of times, effectively transforming a brute-force $O(N^2)$ search into an $O(N)$ lookup-driven process.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Set Population:** Iterating through `nums` once to populate the `HashSet` takes $O(N)$ on average.
*   **Sequence Scanning:** While there is a `while` loop nested within a `for` loop, the conditional `!present.contains(n - 1)` acts as a guard. An inner `while` loop only executes if `n` is the *head* of a sequence. Consequently, each element is part of a "count" operation exactly once across the entire lifecycle of the function.
*   **Amortized Analysis:** Given that each element is visited at most twice (once for the initial set check and once as part of a sequence), the total time complexity is $O(N + N) = O(N)$.

### Space Complexity: $O(N)$
*   **Data Structure:** We allocate a `HashSet` to store all elements from the input array. In the worst case (all elements are unique), the set stores $N$ integers, requiring $O(N)$ space.

## 3. Component Deep Dive

### The "Sentinel" Guard (`!present.contains(n - 1)`)
This is the most critical logic gate in the code. Without this check, the complexity would revert to $O(N^2)$ because the algorithm would attempt to rebuild the sequence for every single element in the input. By verifying that `n - 1` is not present, we effectively designate `n` as the smallest possible starting integer of a potential sequence, preventing redundant computations.

### Hash-Based Lookup
The use of `java.util.HashSet` is essential here. The `contains()` operation provides $O(1)$ average-time complexity. If this were replaced with a sorted `TreeSet` or a `List` scan, the complexity would jump to $O(N \log N)$ or $O(N^2)$ respectively.

### Edge Case Handling
*   **Empty Arrays:** If `nums` is empty, the `maxC` variable is initialized to 0. The loops are skipped, and the method correctly returns 0.
*   **Single Element:** If `nums = [1]`, the `present.contains(1 - 1)` will be false. The `while` loop fails immediately, `maxC` becomes `Math.max(0, 0 + 1)`, returning 1. Correct.
*   **Duplicates:** The `HashSet` automatically handles duplicates. If the input is `[1, 2, 0, 1]`, the set remains `{0, 1, 2}`. The logic naturally calculates the sequence starting at 0 and ignores the second `1`.

## 4. Key Insights & Nuances

*   **The `ct + 1` logic:** Note that `ct` tracks the number of *subsequent* elements found. Thus, `ct + 1` correctly accounts for the sequence head itself.
*   **Memory Overhead:** While the algorithm is $O(N)$ in time, the `HashSet` involves significant overhead due to `Entry` objects and hashing mechanisms. In extremely memory-constrained environments, sorting the array $O(N \log N)$ and scanning for streaks might be preferred over the $O(N)$ space complexity of the set.
*   **Integer Overflow:** The current logic uses `num + 1` inside the `while` loop. While `Integer.MAX_VALUE` could potentially lead to overflow if a sequence reached the absolute limit of the integer range, the check `present.contains(num + 1)` would safely return `false` before `num` increments to a value that would cause an invalid key lookup.
*   **Subtle Bug Opportunity:** A common mistake in similar implementations is updating the `maxC` *inside* the `while` loop. The provided code correctly updates `maxC` *after* the `while` loop finishes, ensuring that the total length is calculated only after the full sequence has been traversed.

---
