# subarray-sum-equals-k

## prefix_hashmap_approach.java
*Style: detailed*

# Engineering Deep Dive: Subarray Sum Equals K

## Summary
The solution employs a **Prefix Sum with Hash Map lookup** technique to solve the Subarray Sum problem in linear time. 

The core algorithmic insight is based on the mathematical property of subarray sums: If the difference between the prefix sum at index $i$ ($P_i$) and a target $k$ exists as a prior prefix sum ($P_j$ where $j < i$), then the subarray between $j+1$ and $i$ must sum exactly to $k$. By maintaining a frequency map of all prefix sums encountered, we transform an $O(n^2)$ exhaustive search into an $O(n)$ lookup problem.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Traversal:** We iterate through the input array `nums` exactly once.
*   **Operations:** Inside the loop, `HashMap` operations (`containsKey`, `get`, `put`) operate in $O(1)$ amortized time.
*   **Result:** The aggregate time complexity is linear relative to the size of the input array.

### Space Complexity: $O(n)$
*   **Storage:** The `count` HashMap stores at most $n+1$ distinct prefix sums in the worst case (where all prefix sums are unique).
*   **Auxiliary:** The `prefix` array (though technically redundant, as we could use a single running variable) consumes $O(n)$ space. If memory were constrained, the `prefix` array could be replaced with a `runningSum` integer variable to achieve $O(1)$ auxiliary space, keeping the map as the only $O(n)$ component.

---

## Component Deep Dive

### 1. The Prefix Sum Invariant
The logic `prefix[i] - k = target` is the mathematical engine of this solution. 
*   Let $S_i$ be the sum from `nums[0...i]`.
*   A subarray `nums[j...i]` has a sum of $S_i - S_{j-1}$.
*   To find $S_i - S_{j-1} = k$, we rearrange to $S_{j-1} = S_i - k$.
*   The code looks for how many times the value $(S_i - k)$ has appeared previously.

### 2. The Identity Initialization
`count.put(0, 1);`
This is a critical edge case handler. It represents the scenario where a subarray starting at index `0` itself sums to `k`. Without this entry, the algorithm would fail to account for subarrays that begin at the very start of the array, as $P_i - k = 0$ would not find a corresponding entry in the map.

### 3. Redundancy Analysis
The `int[] prefix` array is strictly redundant. You can perform the exact same logic using a single integer variable `sum`:
```java
int sum = 0;
// inside loop:
sum += nums[i];
int val = sum - k;
// ... logic continues using 'sum' instead of 'prefix[i]'
```
The provided code uses the array, which slightly increases space overhead without providing functional utility.

---

## Key Insights & Performance Nuances

### Hash Map Selection
Using `java.util.HashMap` is standard, but in performance-critical environments with tight latency budgets (e.g., high-frequency trading or embedded systems), the boxing of `Integer` keys and values can introduce GC pressure. For extreme performance, a primitive collection library (like fastutil's `Int2IntOpenHashMap`) would eliminate autoboxing overhead.

### Potential Overflow
If the input array `nums` contains values that aggregate to exceed `Integer.MAX_VALUE` or fall below `Integer.MIN_VALUE`, an integer overflow will occur.
*   **Risk:** If `sum` overflows, the logic breaks because the `val` lookup will map to incorrect keys.
*   **Mitigation:** If the problem constraints allow sums outside 32-bit integer range, `prefix` calculations must use `long`.

### Subtle Bug: The `i != 0` branching
The logic inside the loop:
```java
if (i != 0) { prefix[i] = prefix[i - 1] + nums[i]; }
```
This is slightly suboptimal. A cleaner approach that avoids branching inside the hot path involves initializing `prefix[0]` outside the loop or using a running variable:
```java
int currentSum = 0;
for (int num : nums) {
    currentSum += num;
    // logic...
}
```
This eliminates the conditional branch per iteration, which is friendlier to CPU branch prediction.

---
