# subarray-sum-equals-k

## attempt_1_bruteforce.java
*Style: concise*

### Notes: Subarray Sum Equals K (Brute Force)

#### Purpose
Calculates the total number of continuous subarrays that sum up to exactly `k` using a nested loop approach.

#### Key Logic
*   **Outer loop (`i`)**: Defines the starting index of the subarray.
*   **Inner loop (`j`)**: Extends the subarray end index and accumulates the running sum.
*   **Conditional**: Increments `res` whenever the cumulative `sum` matches `k`.

#### Observations
*   **Complexity**: $O(n^2)$ time and $O(1)$ space.
*   **Limitation**: Inefficient for large arrays; suboptimal compared to the $O(n)$ hash map approach (Prefix Sum technique).
*   **Edge Cases**: Correctly handles negative numbers (unlike sliding window approaches which require positive-only arrays).

---

## prefixSum_hashMap_approach.java
*Style: detailed*

# Technical Deep-Dive: Subarray Sum Equals K

## 1. Summary
The problem asks for the total number of continuous subarrays that sum to a target `k`. A brute-force $O(n^2)$ approach would check every possible subarray $[i, j]$. This solution utilizes the **Prefix Sum + Hash Map (Complement Tracking)** technique to achieve $O(n)$ time complexity.

The algorithm relies on the algebraic property: 
If the sum of a subarray from index $i$ to $j$ is $k$, then:
$$Sum(0, j) - Sum(0, i-1) = k$$
By rearranging to $Sum(0, i-1) = Sum(0, j) - k$, we see that for any current prefix sum $P_j$, we need to count how many previous prefix sums equal $P_j - k$.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Iteration:** The algorithm performs a single linear pass over the `nums` array.
*   **Hash Map Operations:** `containsKey`, `get`, and `put` operations on a `HashMap` average to $O(1)$ time complexity.
*   Total operations: $n \times O(1) = O(n)$.

### Space Complexity: $O(n)$
*   **Hash Map:** In the worst-case scenario (where every prefix sum is unique), the `count` map stores $n$ entries.
*   **Prefix Array:** While the code explicitly allocates an `int[] prefix` array ($O(n)$), this is technically redundant. The prefix sum can be tracked using a single `int currentSum` variable, reducing the auxiliary space complexity to $O(n)$ solely due to the map.

---

## 3. Component Deep Dive

### The `count` Map
*   **Function:** Stores the frequency of prefix sums encountered so far.
*   **Initialization:** `count.put(0, 1)` is critical. It accounts for the edge case where a prefix sum exactly equals $k$ (i.e., the subarray starts from index 0). Without this, subarrays starting at index 0 would be missed because $prefix[i] - k = 0$.

### The Loop Logic
*   **Prefix Calculation:** The code maintains a `prefix` array. Note that `prefix[0]` is initialized separately before the loop, which simplifies the conditional logic inside the loop but introduces a slight divergence from the standard "sliding" prefix sum variable approach.
*   **Complement Look-up:** `int val = prefix[i] - k`. By checking if `val` exists in the map, we are essentially looking for any index `x < i` where the sum between `x` and `i` is exactly `k`.

### Edge Cases
*   **Negative Numbers:** Unlike sliding window approaches (which require non-negative integers to maintain monotonicity), this Hash Map approach handles negative numbers perfectly because it relies on the associative property of addition rather than a monotonic window.
*   **Multiple Subarrays:** By storing the *frequency* of a prefix sum rather than just its existence, the algorithm correctly counts multiple valid subarrays that may share the same prefix sum value.

---

## 4. Key Insights

*   **Memory Optimization:** The `prefix[]` array is an unnecessary allocation. We can replace `prefix[i]` and `prefix[i-1]` with a single running `int currentSum`. This would reduce space overhead and cache misses, making the code more performant.
*   **Hash Map Performance:** `HashMap` can suffer from collisions in extreme scenarios. In performance-critical systems (or competitive programming), if memory allows, a primitive-based hash map (like `fastutil` or `TIntIntHashMap`) would reduce the boxing/unboxing overhead of `Integer` objects.
*   **Subtle Bug Warning:** The logic `prefix[i] = prefix[i - 1] + nums[i]` inside the loop combined with `prefix[0] = nums[0]` is slightly redundant. In the first iteration (`i=0`), the code skips the `if(i!=0)` block but still executes the logic following it. The implementation works, but the initialization logic is slightly fragile—if the array were empty, `nums[0]` would throw an `ArrayIndexOutOfBoundsException`.
    *   *Recommendation:* Always include an early return for `nums == null || nums.length == 0`.

### Refined Logic Suggestion
```java
public int subarraySum(int[] nums, int k) {
    int count = 0, sum = 0;
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);
    for (int num : nums) {
        sum += num;
        count += map.getOrDefault(sum - k, 0);
        map.put(sum, map.getOrDefault(sum, 0) + 1);
    }
    return count;
}
```
*This refactoring eliminates the redundant array and improves readability while maintaining the same complexity.*

---
