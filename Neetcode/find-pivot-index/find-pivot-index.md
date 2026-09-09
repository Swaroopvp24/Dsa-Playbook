# find-pivot-index

## attempt_1.java
*Style: detailed*

## 📘  Deep‑Dive Reference for `Solution.pivotIndex(int[])`

> **Problem (LeetCode 724 – Find Pivot Index)**  
> Given an integer array `nums`, return the **pivot index** of this array. The pivot index is the index where the sum of all the numbers strictly to the left of the index is equal to the sum of all the numbers strictly to the right of the index. If no such index exists, return `-1`. If there are multiple pivot indexes, return the left‑most one.

The implementation below solves the problem by **pre‑computing a prefix‑sum array** and then scanning for the first index where the left‑hand sum equals the right‑hand sum.

```java
public class Solution {
    public int pivotIndex(int[] nums) {
        int n = nums.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        for (int i = 0; i < n; i++) {
            int leftSum = prefixSum[i];
            int rightSum = prefixSum[n] - prefixSum[i + 1];
            if (leftSum == rightSum) {
                return i;
            }
        }
        return -1;
    }
}
```

---

## 1️⃣  Summary – High‑Level Approach

| Step | What happens | Why it works |
|------|--------------|--------------|
| **1️⃣ Build prefix sum** | `prefixSum[k]` stores the sum of the first `k` elements (`nums[0] … nums[k‑1]`). | Enables O(1) range‑sum queries: `sum(nums[l..r]) = prefixSum[r+1] - prefixSum[l]`. |
| **2️⃣ Scan for pivot** | For each index `i`, compute `leftSum = prefixSum[i]` and `rightSum = totalSum - prefixSum[i+1]`. Compare them. | `leftSum` is sum of elements left of `i`; `rightSum` is sum of elements right of `i`. Equality ⇔ pivot condition. |
| **3️⃣ Return result** | First index where equality holds → left‑most pivot. If none, return `-1`. | Guarantees the required output semantics. |

The algorithm is **deterministic**, **single‑pass after preprocessing**, and runs in linear time with linear auxiliary memory.

---

## 2️⃣  Complexity Analysis

| Metric | Derivation | Big‑O |
|--------|------------|-------|
| **Time** | 1. One loop of length `n` to fill `prefixSum` (each iteration O(1)). <br> 2. Second loop of length `n` to test each candidate index (each iteration O(1)). | **O(n)** |
| **Space** | `prefixSum` holds `n+1` integers. No other data structures of size proportional to `n`. | **O(n)** auxiliary (input array `nums` is not counted). |

### Why the Time is Linear
- Both loops iterate **exactly** `n` times, with only constant‑time arithmetic and array accesses inside.
- No nested loops, no recursion, and no data‑structure operations that are super‑linear.

### Why the Space is Linear
- The prefix‑sum array mirrors the input length (`n+1`). This is the sole extra memory.
- The algorithm could be rewritten to use **O(1) space** (see “Key Insights” → “Space‑optimised variant”), but the current code chooses clarity over minimal memory.

### Edge‑Case Complexity Considerations
| Edge case | Effect on complexity |
|-----------|----------------------|
| `nums` empty (`n = 0`) | Both loops execute 0 iterations → O(1) time, O(1) extra space (still allocate array of size 1). |
| Extremely large `n` (e.g., 10⁷) | Linear time remains feasible; memory usage becomes the limiting factor (`≈ 40 MiB` for `int[]`). |
| Very large values causing overflow | Time unaffected; however, integer overflow can corrupt the logical equality test, potentially producing a wrong pivot index. This is a *semantic* issue, not a complexity one. |

---

## 3️⃣  Component Deep Dive

### 3.1  `int n = nums.length;`
- Captures array size once to avoid repeated field accesses in the loops (micro‑optimisation).

### 3.2  Prefix‑Sum Construction  

```java
int[] prefixSum = new int[n + 1];
for (int i = 0; i < n; i++) {
    prefixSum[i + 1] = prefixSum[i] + nums[i];
}
```

#### Data Structure: `int[] prefixSum`
- **Indexing semantics**:  
  - `prefixSum[0] = 0` (sum of zero elements).  
  - `prefixSum[k]` = Σ_{j=0}^{k‑1} nums[j] for `k ∈ [1, n]`.  
- **Why `n+1`?** Allows direct retrieval of the sum *before* the first element (`0`) and *after* the last element (`totalSum`).

#### In‑place vs Separate Array
- The algorithm **does not modify** `nums`. This guarantees that callers see the original array untouched, a useful contract in library code.

#### Edge‑Case Handling
- **Overflow**: `prefixSum[i] + nums[i]` may overflow `int`. The Java language silently wraps around (two’s‑complement). For inputs where total sum exceeds `Integer.MAX_VALUE` or drops below `Integer.MIN_VALUE`, the pivot condition may be evaluated incorrectly. A production‑grade solution could:
  - Use `long` for `prefixSum` (and `leftSum/rightSum`) to avoid overflow for typical problem constraints (`|nums[i]| ≤ 10⁵, n ≤ 10⁴` is safe with `int`, but larger constraints demand `long`).  
  - Or check for overflow before addition using `Math.addExact` and catch `ArithmeticException`.

### 3.3  Pivot Search Loop  

```java
for (int i = 0; i < n; i++) {
    int leftSum  = prefixSum[i];
    int rightSum = prefixSum[n] - prefixSum[i + 1];
    if (leftSum == rightSum) {
        return i;
    }
}
return -1;
```

#### Derivation of `leftSum` and `rightSum`
- `leftSum` = sum of elements left of `i` → `prefixSum[i]`.
- `rightSum` = total sum (`prefixSum[n]`) minus sum of elements up to and including `i` (`prefixSum[i+1]`).

#### Why `i + 1` for the right side?
- `prefixSum[i+1]` includes `nums[i]`. Subtracting it removes the pivot element itself, leaving only the right‑hand side.

#### Early Return
- The method returns at the **first** matching index, satisfying “leftmost pivot” requirement. No need to continue scanning after a match.

#### Edge Cases Covered
| Condition | Result |
|-----------|--------|
| `i = 0` (first element) | `leftSum = prefixSum[0] = 0`. `rightSum = total - prefixSum[1]`. Works for “pivot at start”. |
| `i = n‑1` (last element) | `rightSum = total - prefixSum[n] = 0`. `leftSum = prefixSum[n‑1]`. Works for “pivot at end”. |
| No pivot | Loop finishes, returns `-1`. |
| Single‑element array (`n = 1`) | `leftSum = 0`, `rightSum = 0` → returns `0`. Correct per problem statement. |

### 3.4  Return Value Semantics
- `-1` is the sentinel for “no pivot”. The method never returns a negative index other than `-1`, guaranteeing callers can safely test `>= 0` for success.

---

## 4️⃣  Key Insights – Nuances, Optimisations, and Pitfalls

### 4.1  Space‑Optimised Variant (O(1) extra space)

The prefix array can be eliminated by maintaining a running left sum while iterating once:

```java
public int pivotIndex(int[] nums) {
    int total = 0;
    for (int v : nums) total += v;          // one pass: compute total sum
    int left = 0;
    for (int i = 0; i < nums.length; i++) {
        int right = total - left - nums[i]; // total - left - pivot = right
        if (left == right) return i;
        left += nums[i];
    }
    return -1;
}
```

- **Pros**: O(1) auxiliary memory, same O(n) time.
- **Cons**: Two passes (still linear) and a slightly more complex expression for `right`. The original version is arguably clearer for teaching or debugging.

### 4.2  Integer Overflow Guardrails

- **Problem**: `int` overflow can produce false positives/negatives.  
- **Solution**: Use `long` for the cumulative totals:
  ```java
  long[] prefix = new long[n + 1];
  for (int i = 0; i < n; ++i) prefix[i + 1] = prefix[i] + (long) nums[i];
  ```
- **Impact**: Memory increases from 4 bytes per element to 8 bytes, still O(n). For the O(1) space version, simply make `total` and `left` `long`.

### 4.3  Handling Extremely Large Arrays

- **Cache Locality**: The prefix array is accessed sequentially during construction and then *read* sequentially during the second loop. This pattern is cache‑friendly, achieving near‑optimal bandwidth on modern CPUs.
- **Potential Parallelisation**: The prefix sum could be parallelised (e.g., using Java’s Fork/Join). However, the overhead outweighs benefits for typical constraints (`n ≤ 10⁵`). Only for `n` in the tens of millions would a parallel scan be worth it.

### 4.4  Subtle Bug Sources

| Symptom | Typical cause | Fix |
|---------|---------------|-----|
| **Incorrect pivot for large values** | Overflow of `int` when total sum > 2³¹‑1 or < -2³¹. | Switch to `long`. |
| **`ArrayIndexOutOfBoundsException`** | Using `prefixSum[i]` when `i` = `n` in the second loop (off‑by‑one). | Loop bounds are correctly `i < n`. Keep as‑is. |
| **Returning a later pivot instead of the leftmost** | Breaking out of the loop after *any* match is fine, but a mistakenly placed `continue` after updating `leftSum` could skip earlier matches. | Ensure immediate `return` on first equality. |
| **Modifying the input array** | Accidentally using `nums[i] = ...` inside loops. | The current code never mutates `nums`. |

### 4.5  Algorithmic Rationale

- The **prefix‑sum technique** converts a **range‑sum equality problem** (which would naïvely be O(n²) if recomputed each time) into O(1) queries per candidate index.  
- By pre‑computing once, we pay a *single* linear pass and then get constant‑time lookups, achieving the optimal linear time bound for this problem class.

### 4.6  Alternative Formulations

| Approach | Memory | Time | Comments |
|----------|--------|------|----------|
| **Two‑pointer from ends** (maintaining left/right sums) | O(1) | O(n) | Works because we can move the pointer with the smaller sum; however, it does not guarantee the *leftmost* pivot without extra logic. |
| **Segment tree / BIT** | O(n) | O(log n) per query (total O(n log n)) | Overkill; useful only if many pivot queries need to be answered on a mutable array. |
| **Sorting + prefix sums** | O(n) extra | O(n log n) | Sorting destroys original order, invalidating the definition of “pivot index”. Not applicable. |

---

## 5️⃣  Checklist for Maintaining This Code

| ✅ Item | Explanation |
|--------|-------------|
| **Overflow safety** | Verify whether input constraints guarantee sums fit in `int`. If not, switch to `long`. |
| **Null/empty guard** | The method assumes a non‑null array (LeetCode guarantees it). In production, add `Objects.requireNonNull(nums)`. |
| **Thread‑safety** | No shared mutable state → inherently thread‑safe. |
| **Performance regression** | Benchmark with large random arrays; ensure runtime stays ~`2 × n` elementary operations. |
| **Documentation** | Add Javadoc describing the algorithm, its O(n) time & O(n) space, and overflow considerations. |
| **Testing** | Include unit tests for: <br> • Empty array <br> • Single element <br> • Multiple pivots (ensure leftmost returned) <br> • No pivot <br> • Large values causing overflow (expect correct handling if using `long`). |

---

## 6️⃣  TL;DR – One‑Pager Summary

| Aspect | Detail |
|-------|--------|
| **Core idea** | Use a prefix‑sum array to get left and right sums in O(1) per index. |
| **Time** | O(n) – two linear scans. |
| **Space** | O(n) extra for `prefixSum` (can be reduced to

---
