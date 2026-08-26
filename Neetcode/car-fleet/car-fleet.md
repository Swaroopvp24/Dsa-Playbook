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

## space_optimized_solution.java
*Style: detailed*

# Engineering Deep Dive: Car Fleet Solution

## Summary
The "Car Fleet" problem is a classic greedy interval-scheduling derivative. The core algorithmic insight is that a car $i$ can only form a fleet with cars ahead of it (closer to the target). If a car $i$ takes less time to reach the destination than a car $j$ (where $j > i$ in position), car $i$ will eventually catch up and be constrained by $j$'s speed.

The solution employs a **reverse-order greedy sweep**. By sorting cars by position and iterating from the target backwards (from the closest car to the furthest), we can maintain a "bottleneck time" (`lastTime`). If a car's time to the target is greater than the current bottleneck, it cannot catch up to the fleet ahead, necessitating the creation of a new, slower fleet.

---

## Complexity Analysis

### Time Complexity: $O(N \log N)$
1.  **Preprocessing ($O(N)$):** Creating the `cars` 2D array.
2.  **Sorting ($O(N \log N)$):** The bottleneck of the algorithm. Sorting the cars by starting position is required to establish the spatial dependency of fleet formations.
3.  **Iteration ($O(N)$):** A single linear pass through the sorted array. 
*   **Total:** $O(N \log N + N) \approx O(N \log N)$.

### Space Complexity: $O(N)$
1.  **Data Structure ($O(N)$):** We store the `cars` array of size $N \times 2$.
2.  **Auxiliary Space ($O(\log N)$ or $O(N)$):** Depending on the implementation of `Arrays.sort()` (Dual-Pivot Quicksort for primitives, Timsort/MergeSort for objects), the sort consumes stack space.
*   **Total:** $O(N)$.

---

## Component Deep Dive

### 1. Sorting Strategy
The primary requirement is processing cars based on their distance to the target. Sorting by `position` ensures that when we iterate backwards (from `n-1` to `0`), we are effectively looking at the car closest to the target first.

### 2. The Greedy Conditional: `if (time > lastTime)`
*   **`time`**: The time required for the current car to reach the `target` given its independent velocity.
*   **`lastTime`**: The time taken by the fleet immediately ahead.
*   **The Logic**: If the current car's `time` is $\le$ `lastTime`, it means this car will collide with or arrive simultaneously with the fleet ahead. Because it is physically blocked, it effectively merges into that fleet. We ignore it. If `time > lastTime`, the current car is slower than the fleet ahead; it forms a new, independent fleet, and we update `lastTime` to this car's `time`.

### 3. Edge Case Handling
*   **Single Car (`n=1`)**: The loop runs once, `time > -1` is true, returns 1. Correct.
*   **Zero/Negative Speed**: The problem constraints usually define speed $> 0$. If `speed` could be $0$, division by zero would occur; an implicit constraint check is required for robust production code.
*   **Identical Positions**: While standard inputs usually assume distinct positions, the `Arrays.sort` handles ties stably based on the input index (though position ties are physically impossible for unique cars).

---

## Key Insights & Optimization Nuances

### Floating Point Precision
Using `double` for time calculations is standard here, but beware of precision issues in strict equality comparisons. The logic uses `time > lastTime` instead of `>=`. This is intentional: if `time == lastTime`, the cars arrive at the exact same moment at the target and thus belong to the same fleet. Floating point epsilon comparison (`time > lastTime + 1e-9`) is generally safer in high-precision scenarios, though standard `double` usually suffices for typical coordinate ranges.

### Space Optimization
The `cars` array is $O(N)$ space. Can we do better?
*   We can avoid the $O(N)$ array creation if we have a way to sort the indices of `position` based on `position` values without creating the `int[][]`.
*   Example: Create an array of indices `Integer[] idx = {0, 1, ..., n-1}`, sort `idx` using a custom comparator `(a, b) -> Integer.compare(position[a], position[b])`. This is functionally equivalent but slightly more memory-efficient if the objects were larger.

### The "Stack" Concept
The commented-out `Deque` in the provided snippet highlights the standard "Stack-based" approach. By iterating in reverse, we essentially simulate the stack's behavior without the memory overhead of the `Deque`. The variable `lastTime` acts as the `stack.peek()`. This is an **elegant space reduction** from $O(N)$ stack depth to $O(1)$ auxiliary space during the iteration phase.

---
