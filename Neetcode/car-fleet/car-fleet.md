# car-fleet

## standard_stack_solution.java
*Style: detailed*

# Technical Deep-Dive: Car Fleet Problem

## 1. Summary
The solution employs a **Greedy Sweep-Line algorithm** combined with **Time-to-Target projection**. 

The core observation is that a "fleet" is defined by the lead car's arrival time at the `target`. If a car behind a lead car has a higher velocity, it will eventually catch up, effectively merging into the lead car's fleet. Conversely, if a car behind has a slower velocity (resulting in a higher time-to-target), it cannot catch the lead car and must initiate a new, independent fleet. By processing cars in reverse order of proximity to the target (farthest to nearest), we can determine fleet formation in a single pass.

## 2. Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** The `Arrays.sort` operation on the `cars` array (an array of $N$ pairs) dominates the complexity, requiring $O(N \log N)$ time.
*   **Linear Scan:** We perform a single pass from $N-1$ to $0$ ($O(N)$ operations).
*   **Total:** $O(N \log N + N) \approx O(N \log N)$.

### Space Complexity: $O(N)$
*   **Auxiliary Array:** We create an $N \times 2$ matrix to pair positions with speeds, consuming $O(N)$ space.
*   **Stack:** The `ArrayDeque` (used as a stack) holds at most $N$ elements in the worst case (e.g., no cars ever catch up to each other), resulting in $O(N)$ space.

## 3. Component Deep Dive

### Data Preprocessing
The algorithm maps positions and speeds into a 2D array. This is critical because `position` and `speed` are provided as independent arrays. Sorting by `position[i]` allows us to establish a strictly increasing spatial ordering, enabling the stack-based evaluation of collisions.

### Reverse Temporal Sweep
The logic iterates backwards from the car closest to the target to the car furthest away. 
*   **The Logic:** If we process from the target backwards, we identify the "lead" car of a potential fleet first. 
*   **Stack Mechanism:** 
    *   `timeToTarget = (target - position) / speed`.
    *   If current car's `timeToTarget` is greater than the stack's top, it means the current car is slower than the car ahead. It will never collide, thus it forms a new fleet and is pushed onto the stack.
    *   If `timeToTarget` is less than or equal to the stack's top, it means the current car is fast enough to reach the target at or before the lead car, effectively merging into the fleet represented by the top of the stack.

### Edge Case Handling
*   **No cars ($N=0$):** `carCount` is 0; the loop is skipped, returns `0`.
*   **Single car ($N=1$):** Loop runs once, pushes to stack, returns `1`.
*   **Identical speeds/positions:** While the problem constraints typically imply distinct starting positions, the sorting logic and `timeToTarget` comparison handle identical positions by the order of operations, and the `>` comparison effectively merges identical arrival times.

## 4. Key Insights

*   **Monotonicity of Fleets:** The stack represents a monotonic sequence of arrival times. By only pushing when `timeToTarget > fleetTimes.peek()`, we maintain a strictly decreasing sequence of arrival times in the stack as we move away from the target.
*   **Division Precision:** Casting to `(double)` before division is mandatory. Integer division would truncate the fractional component, leading to incorrect fleet merging for cars that arrive at the same destination with only minor temporal differences.
*   **Space Optimization:** While `ArrayDeque` is used, the stack's size effectively represents the number of fleets. One could technically replace the stack with a simple `double lastArrivalTime` variable to store the "lead" car's time to reduce auxiliary space to $O(1)$ beyond the sorted array. 
*   **Floating Point Comparison:** In highly precise scenarios, comparing doubles with `>` can be susceptible to IEEE 754 floating-point errors. For standard competitive programming constraints, this implementation is sufficient, but in production systems with extreme precision requirements, an epsilon (`1e-9`) should be used for comparisons.

---
