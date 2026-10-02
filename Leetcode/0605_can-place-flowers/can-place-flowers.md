# can-place-flowers

## attempt_1.java
*Style: detailed*

# Technical Reference: Flowerbed Planting Algorithm

## Summary
The solution implements a **greedy local-decision algorithm** to solve the constrained placement problem. By iterating through the array once and evaluating placement eligibility based on local state (current index and immediate neighbors), the algorithm transforms the global requirement ($n$ flowers) into a series of optimal local choices. It leverages a "modify-in-place" strategy to update the state of the garden dynamically, which informs subsequent placement decisions without requiring extra look-ahead buffers or auxiliary data structures.

---

## Complexity Analysis

### Time Complexity: $O(M)$
*   **$M$**: The length of the `flowerbed` array.
*   The algorithm performs a single linear scan of the array. Each index is visited exactly once, and neighbor checks are $O(1)$ constant-time operations. The early exit condition (`flowersPlanted >= n`) provides a best-case scenario of $O(1)$ if $n$ is satisfied early, but the worst-case remains linear.

### Space Complexity: $O(1)$
*   The algorithm operates in-place. No additional data structures (such as sets or boolean arrays) are allocated. The space usage is restricted to primitive integer pointers (`i`, `flowersPlanted`, `left`, `right`), satisfying the requirements for constant auxiliary space.

---

## Component Deep Dive

### 1. Greedy State Mutation
The critical design choice is `flowerbed[i] = 1`. By mutating the array in-place, the algorithm implicitly handles the "no adjacent flowers" constraint for the next iteration. If we place a flower at `i`, the condition at `i+1` will automatically evaluate `left` as `1`, correctly blocking a flower from being placed at `i+1`.

### 2. Boundary Condition Handling
The use of ternary operators for `left` and `right` effectively treats the "out-of-bounds" areas as virtual empty slots:
*   `int left = (i == 0) ? 0 : flowerbed[i - 1];`
*   `int right = (i == flowerbed.length - 1) ? 0 : flowerbed[i + 1];`
This abstracts away complex `if-else` branching logic, ensuring that flowers can be placed at the extreme edges of the array if those edges are empty and the adjacent interior spot is also empty.

### 3. Early Exit Strategy
The implementation checks `if (flowersPlanted >= n)` immediately after a successful placement. This is a crucial performance optimization for large datasets where $n$ is significantly smaller than the available slots, preventing unnecessary iterations through the tail end of the `flowerbed`.

---

## Key Insights

### The "Look-Ahead" Effect
By mutating the array, we eliminate the need for a separate tracking mechanism for "already blocked" slots. This creates a chain reaction: placing a flower at `i` effectively "disables" `i+1`. This is a classic example of **greedy optimization** where the local local optimum (placing a flower whenever possible) leads to a globally valid solution.

### Subtle Edge Cases
*   **Single-element arrays:** If `flowerbed = [0]` and `n = 1`, the `left` and `right` logic correctly evaluates to `0` and `0`, resulting in `flowerbed[0] = 1`, correctly returning `true`.
*   **Strict Adjacency:** The logic `left == 0 && right == 0` is robust. It only permits placement if the current index is empty AND both neighbors are empty. If a flower were previously placed at `i-1`, the check at `i` would fail because `left` would be `1`.

### Potential Improvements/Refinements
While the current approach is optimal, the reliance on array mutation can be a "code smell" in functional programming contexts where input immutability is required. 
*   **Avoidance of Mutation:** To achieve the same logic without mutating the input, one would need to maintain an `int lastPlacedIndex` variable and check `if (i - lastPlacedIndex >= 2)`. This would remove the need to write to the `flowerbed` array entirely. 
*   **Performance Tweak:** In the current code, `flowersPlanted` is compared to `n` inside the loop. Note that if `n == 0`, the function should technically return `true` immediately; the current logic handles this via the final `return flowersPlanted >= n` statement.

---
