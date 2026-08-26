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

## space_optimized_solution.java
*Style: detailed*

# Engineering Deep-Dive: Car Fleet Problem

## 1. Summary
The "Car Fleet" problem is a variation of the interval merging problem. Since cars can only form a fleet if they reach the target at the same time or if a trailing car is faster and "catches" the car in front, the problem is essentially about identifying **monotonic non-increasing arrival times**.

The solution employs a **greedy, sweep-line approach**. By sorting cars by position in ascending order and processing them in reverse (from the car closest to the target to the car furthest away), we can determine fleet membership based on whether a car's arrival time at the `target` is greater than the arrival time of the "fleet leader" (the car immediately in front of it).

## 2. Complexity Analysis

*   **Time Complexity: $O(N \log N)$**
    *   **Sorting:** The dominant operation is sorting the `cars` array based on position, which requires $O(N \log N)$ time.
    *   **Iteration:** After sorting, we perform a single linear pass over the array of length $N$, which is $O(N)$.
    *   **Total:** $O(N \log N + N) \approx O(N \log N)$.
*   **Space Complexity: $O(N)$**
    *   We allocate a new 2D array `cars` of size $N \times 2$ to pair positions and speeds for sorting. 
    *   The sorting algorithm itself (Dual-Pivot Quicksort in Java for primitives, though `Arrays.sort` on objects/arrays uses Timsort) may incur $O(N)$ or $O(\log N)$ auxiliary space depending on the implementation.

## 3. Component Deep Dive

### Data Representation
*   **The 2D Array (`cars[N][2]`):** This is essential because `position` and `speed` are provided as separate arrays. Storing them as pairs ensures that after the sort, we maintain the association between a specific car's position and its unique velocity.

### Greedy Logic: Reverse Iteration
*   **The Core Logic:** By iterating backwards from `carCount - 1` to `0`, we are processing cars starting from the one closest to the `target`.
*   **Time Calculation:** `double timeToTarget = (double) (target - position) / speed`. We cast to `double` to prevent integer truncation, as precise fractional time values are required for comparison.
*   **Fleet Formation Condition:**
    *   If `current_car.timeToTarget > lastFleetTime`: This car cannot catch up to the current fleet leader (it is too slow). It establishes a new, independent fleet, and we update `lastFleetTime` to this new car's time.
    *   If `current_car.timeToTarget <= lastFleetTime`: This car reaches the target in less than or equal time to the car ahead of it. Because the car ahead is slower (or already occupied by a fleet), this car will "catch" the fleet and effectively merge into it. We do *not* increment the fleet count.

## 4. Key Insights & Nuances

*   **Why Reverse Iteration?** Sorting by position allows us to know exactly which car is "ahead." By moving backward, we maintain the "fleet state" of the car closest to the target. If a car's arrival time is less than or equal to the car ahead, it essentially becomes part of that car's fleet. If it is greater, it signifies the formation of a separate group, as it is impossible for it to catch the leader.
*   **Floating Point Precision:** Using `double` is critical here. While `(target - pos1) / speed1 > (target - pos2) / speed2` can be rewritten as `(target - pos1) * speed2 > (target - pos2) * speed1` to avoid floating-point errors, the current implementation is acceptable given standard constraint ranges. However, in high-precision scenarios, cross-multiplication is preferred to avoid precision drift.
*   **Edge Cases:**
    *   **Single Car:** The loop executes once, `lastFleetTime` updates, `fleetCount` becomes 1. Correct.
    *   **All cars same speed:** The cars closer to the target will always have smaller arrival times, leading to `timeToTarget > lastFleetTime` being true for every car. Result: `N` fleets. Correct.
    *   **Descending speeds:** If a fast car is behind a slow car, it will have a smaller `timeToTarget`, causing the `if` condition to fail, merging it into the leader's fleet. Correct.
*   **Potential Optimization:** If memory is a strict constraint, one could avoid the 2D array by using a single `TreeMap<Integer, Double>` where the key is the position and the value is the calculated time, iterating through the `descendingMap()`. This removes the need for the manual 2D array allocation, though it carries higher constant overhead due to `TreeMap` node objects.

---
