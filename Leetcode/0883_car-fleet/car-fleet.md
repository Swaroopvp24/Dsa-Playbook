# car-fleet

## standard_stack_solution.java
*Style: detailed*

# Technical Deep Dive: Car Fleet Solution

## Summary
The "Car Fleet" problem is a classic application of **monotonic stack logic** combined with greedy scheduling. The core intuition is that a car can only form a fleet with cars *ahead* of it if it is faster and reaches the destination at or before the lead car. By sorting the cars by position and iterating from the target backward, we transform the problem into a "right-to-left" analysis of arrival times. If a trailing car has a greater (slower) travel time than the current leading fleet, it cannot catch up; therefore, it initiates a new fleet.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Sorting:** The `Arrays.sort` operation on the `cars` array (an array of $N$ pairs) dominates the complexity, requiring $O(N \log N)$.
*   **Iteration:** The single pass from `carCount - 1` down to $0$ is $O(N)$.
*   **Total:** $O(N \log N + N) \approx O(N \log N)$, where $N$ is the number of cars.

### Space Complexity: $O(N)$
*   **Auxiliary Array:** Creating the `cars[][]` matrix requires $O(N)$ space.
*   **Stack:** In the worst-case scenario (where no cars ever form a fleet, i.e., all cars have strictly decreasing arrival times), the `fleetTimes` stack will store $N$ elements, requiring $O(N)$ space.

---

## Component Deep Dive

### 1. The Preprocessing Strategy
We represent cars as `int[2]` tuples `[position, speed]`. Sorting by position is critical because it establishes a spatial hierarchy. By processing from the car closest to the target ($target - position$ is minimal) backward to the car furthest away, we ensure we always compare a "follower" against the "current leader" of the fleet ahead.

### 2. Time-to-Target Calculation
The core metric is the floating-point value: 
$$T = \frac{target - position_i}{speed_i}$$
Using `double` precision is mandatory here. Integer division would truncate necessary fractions, leading to incorrect fleet identification (e.g., treating two cars as arriving at the same time when one is slightly behind).

### 3. The Greedy Monotonic Logic
The `fleetTimes` stack functions as a **strictly increasing monotonic sequence tracker**. 
*   **The condition `timeToTarget > fleetTimes.peek()`**: If the current car (which is physically behind the car/fleet ahead) takes *longer* to arrive than the car/fleet ahead, it implies this car can never catch up to the lead car. It will reach the target on its own schedule. We "push" this new time onto the stack, establishing a new fleet leader.
*   **The Implicit Merge**: If `timeToTarget <= fleetTimes.peek()`, the current car arrives at or before the lead car. Since it is behind the lead car, it will inevitably become part of that lead car's fleet. We effectively "skip" this car by not pushing it to the stack.

---

## Key Insights & Nuances

*   **Boundary Conditions:**
    *   `position.length == 0`: The current logic handles this via `carCount` initialization, correctly returning 0.
    *   `target == position[i]`: The math $0 / speed$ results in $0.0$. The algorithm correctly treats cars already at the destination as valid fleet components.
*   **Numerical Stability:** While `double` is used, very large inputs for `target` or very small `speed` values could theoretically lead to precision loss. However, for standard competitive programming constraints, IEEE 754 double precision is sufficient.
*   **Optimization Potential:** 
    *   The `Deque` approach is idiomatic, but since we only ever access the top of the stack, a simple `double lastTime` variable would suffice, reducing space complexity from $O(N)$ to $O(1)$ auxiliary space (ignoring the $O(N)$ required for the sorted input array). 
    *   **Refactored Memory Usage:**
        ```java
        double lastFleetTime = -1.0;
        int fleetCount = 0;
        for (int i = carCount - 1; i >= 0; i--) {
            double current = (double)(target - cars[i][0]) / cars[i][1];
            if (current > lastFleetTime) {
                fleetCount++;
                lastFleetTime = current;
            }
        }
        return fleetCount;
        ```
*   **Subtle Bug Warning:** Be wary of sorting stability if positions are identical (though the problem usually guarantees distinct positions). If two cars start at the same position, the sort order would technically be arbitrary, but the problem logic remains sound: the faster car would be processed first, or they would be evaluated sequentially, correctly merging them.

---
