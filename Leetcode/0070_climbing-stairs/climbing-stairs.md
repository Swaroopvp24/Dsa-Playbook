# climbing-stairs

## attempt_1.java
*Style: concise*

### Stair Climbing (Dynamic Programming)

**Summary**
Calculates the number of distinct ways to reach the $n^{th}$ stair given the ability to climb 1 or 2 steps at a time. This is a classic implementation of the Fibonacci sequence where $f(n) = f(n-1) + f(n-2)$.

**Key Components**
* `climbStairs(int n)`: The primary entry point. Uses an iterative bottom-up approach to store results in a DP table (`int[] t`).

**Logic Notes**
* **Base Cases:** For $n=1$ and $n=2$, the answers are trivial (1 and 2, respectively). These must be handled before initializing the array to avoid `ArrayIndexOutOfBounds` or redundant calculations.
* **Space Complexity:** The current implementation uses $O(n)$ space. This can be optimized to $O(1)$ by using two variables to track the previous two steps instead of an entire array, as only the last two values are needed for the next iteration.

---
