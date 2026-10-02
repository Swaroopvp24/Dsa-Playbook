# can-place-flowers

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Flowerbed Placement Algorithm

## Summary
The solution implements a **Greedy Strategy** to solve the constrained placement problem. By iterating linearly through the `flowerbed` array, the algorithm evaluates each index to determine if it is a valid candidate for planting based on local constraints (neighboring spots must be empty). It makes the locally optimal choice to plant a flower whenever possible, which, due to the nature of the non-overlapping constraints, yields a globally optimal count.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm performs a single pass through the `flowerbed` array of size $N$. Inside the loop, all operations (indexing, conditional checks, and assignments) are $O(1)$. 
*   **Early Exit:** The function terminates early as soon as `flowersPlanted >= n`, though in the worst-case scenario (where $n$ is large or unreachable), it visits each element exactly once.

### Space Complexity: $O(1)$
*   **Reasoning:** The algorithm operates **in-place** on the input array. It requires only a constant amount of auxiliary space for integer variables (`flowersPlanted`, `left`, `right`, and the loop iterator `i`), regardless of the input size.

---

## Component Deep Dive

### 1. Greedy Local Constraints
The core logic resides in the conditional check for neighbors:
```java
int left = (i == 0) ? 0 : flowerbed[i - 1];
int right = (i == flowerbed.length - 1) ? 0 : flowerbed[i + 1];
```
This handles **Boundary Conditions** implicitly. By treating the space outside the array boundaries as "empty" (represented by $0$), we allow the logic to treat the first and last elements symmetrically without requiring padding or complex conditional chaining.

### 2. State Mutation
The code modifies the `flowerbed` array during iteration: `flowerbed[i] = 1;`.
*   **Why this is effective:** By updating the array, subsequent iterations automatically "see" the newly planted flower. This prevents the algorithm from attempting to plant at index $i+1$, as index $i$ is now marked as occupied. This effectively implements a "look-ahead" mechanism without additional memory.

### 3. Edge-Case Handling
*   **Array Length 1:** If `flowerbed` is `[0]` and `n` is 1, `left` and `right` resolve to $0$, the condition `0 == 0 && 0 == 0` evaluates to `true`, and the flower is successfully planted.
*   **Already Occupied:** If `flowerbed[i]` is already $1$, the initial `if (flowerbed[i] == 0)` gate blocks it, ensuring we don't overwrite existing flora.
*   **Large $n$:** If $n=0$, the loop may complete or return early depending on the state, but the final `return flowersPlanted >= n` correctly handles the case where no planting is required (always returns `true`).

---

## Key Insights

### 1. Performance Nuance: The "Skip" Optimization
While the provided code is $O(N)$, it can be slightly optimized by incrementing `i` after a successful plant. 
*   **Optimization:** When `flowerbed[i] = 1` is executed, we know $i+1$ cannot be a valid spot. We could effectively perform `i++` inside the block to skip the next index, reducing the number of comparisons. However, for large $N$, the current approach is sufficient.

### 2. Side Effects
The function modifies the input parameter `int[] flowerbed`. In production systems, this is a **destructive operation**. If the original state of the array must be preserved for logging, telemetry, or other concurrent processes, the input should be cloned before processing, incurring $O(N)$ space complexity.

### 3. Subtle Logic Trap
A common pitfall in this problem is attempting to use a sliding window of 3 elements. While valid, it often leads to "index out of bounds" errors if not handled as cleanly as this solution. The ternary approach here is the idiomatic way to handle boundary-dependent logic in Java, prioritizing readability and safety over potentially faster but more verbose bitwise or buffer-based logic.

---
