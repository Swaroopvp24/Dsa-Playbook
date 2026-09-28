# find-the-duplicate-number

## kinda_linkedlist_solution(slowfast_pointers).java
*Style: detailed*

# 🧩 Deep‑Dive Reference: Floyd’s Cycle Detection for **“Find the Duplicate Number”**  

*Solution class – `findDuplicate(int[] nums)`*  

---  

## 1️⃣ Summary – What the algorithm does & why it works  

The input array `nums` (size `n+1`, values in `[1, n]`) can be interpreted as a **functional graph** (a directed graph where every node has exactly one outgoing edge).  

| Node (index) | Edge (next) |
|--------------|-------------|
| `i`          | `nums[i]`   |

Because there are `n+1` nodes but only `n` distinct values, **pigeon‑hole principle** guarantees at least one value appears twice. In the functional graph this manifests as a **cycle**: two different indices eventually point to the same successor, and from that point onward the walk will loop forever.

The algorithm:

1. **Phase 1 – Cycle detection** – Floyd’s Tortoise‑and‑Hare (slow/fast pointers).  
   - `slow` moves `1` step: `slow = nums[slow]`.  
   - `fast` moves `2` steps: `fast = nums[nums[fast]]`.  
   - When `slow == fast` they are inside the cycle.

2. **Phase 2 – Cycle entry (the duplicate)** – Reset one pointer to the start (`0`) and move both one step at a time.  
   - The meeting point of these two pointers is the **entrance** of the cycle, which is exactly the duplicated number.

The approach yields **O(n) time, O(1) extra space**, satisfies the problem constraints (no array modification, constant extra memory, deterministic linear time).  

---  

## 2️⃣ Complexity Analysis  

| Phase | Operation | Cost per iteration | #Iterations (worst‑case) | Total |
|-------|-----------|--------------------|--------------------------|-------|
| **1** (cycle detection) | 2 array reads (`slow = nums[slow]`, `fast = nums[nums[fast]]`) | **O(1)** | ≤ `n` (the fast pointer traverses at most `2n` steps before meeting) | **O(n)** |
| **2** (find entry) | 2 array reads (`slow = nums[slow]`, `entry = nums[entry]`) | **O(1)** | ≤ `n` (both pointers travel at most the length of the tail + cycle) | **O(n)** |
| **Overall** | – | – | – | **Time = O(n)** |

*Why the bound holds*:  
- In a functional graph with a single cycle, the distance from the start (index `0`) to the cycle entry ≤ `n`.  
- The fast pointer moves twice as fast, so within at most `2·n` steps it must either meet the slow pointer or have already lapped the cycle. Hence the while‑loop terminates after ≤ `n` iterations.  
- Phase 2 walks at most the length of the tail (`μ`) plus the cycle length (`λ`), both ≤ `n`.

**Space**: Only three `int` variables (`slowPointer`, `fastPointer`, `cycleEntryPointer`) → **O(1)** auxiliary memory.  
The input array is read‑only, no extra structures (hash sets, visited flags, etc.) are allocated.

---  

## 3️⃣ Component Deep‑Dive  

### 3.1. Data‑Structure View – Functional Graph  

```
index i  ──►  nums[i] (next index)
```

- **Node** = array index `i` (`0 … n`).  
- **Edge** = deterministic mapping `f(i) = nums[i]`.  
- Because each node has out‑degree `1`, the graph consists of **one rooted tree** (possibly empty) feeding into a **single directed cycle**.

The duplicate value `d` is the **first node that is reached twice**, i.e. the **cycle entry**.

### 3.2. Phase 1 – Detecting a cycle  

```java
int slowPointer = 0;
int fastPointer = 0;

while (true) {
    slowPointer = nums[slowPointer];          // 1 step
    fastPointer = nums[nums[fastPointer]];    // 2 steps

    if (slowPointer == fastPointer) {
        break;                                 // meeting inside the cycle
    }
}
```

- **Why start at 0?**  
  `0` is *not* a valid value (`nums[i] ∈ [1,n]`), but it is a valid **node** that points into the graph. Starting from a guaranteed non‑cycle node simplifies the proof (the tail may be length `0`).  

- **Guarantee of termination**:  
  The functional graph contains at least one cycle (by pigeon‑hole). The fast pointer outruns the slow pointer by one step each iteration; in a cycle of length `λ` they will meet after at most `λ` iterations once both are inside the cycle.  

- **Safety of array accesses**:  
  All values are in `[1, n]`; thus `nums[fastPointer]` and `nums[nums[fastPointer]]` are always valid indices (`0 … n`). No IndexOutOfBoundsException can arise.

### 3.3. Phase 2 – Locating the entry  

```java
int cycleEntryPointer = 0;   // start from head again

while (true) {
    slowPointer = nums[slowPointer];
    cycleEntryPointer = nums[cycleEntryPointer];

    if (slowPointer == cycleEntryPointer) {
        return slowPointer;   // duplicate found
    }
}
```

- At the end of Phase 1 both pointers are somewhere **inside** the cycle (call that node `M`).  
- Let `μ` = distance from start (`0`) to the cycle entry, `λ` = cycle length.  
- After resetting `cycleEntryPointer` to `0`, we move both one step per iteration.  
  - After `μ` steps, `cycleEntryPointer` lands on the entry node `E`.  
  - Simultaneously, `slowPointer` (starting from `M`) moves `μ` steps forward, which lands exactly on `E` because `M` is `k` steps ahead of `E` where `k = (λ - μ % λ)`.  
- Hence the first meeting point is `E`, i.e., the duplicated number.  

### 3.4. Edge‑Case Handling  

| Edge case | Reasoning / handling |
|-----------|----------------------|
| **Minimum size** (`n = 1`, array `[1,1]`) | Tail length `μ = 0`, cycle length `λ = 1`. Phase 1 meets instantly (`slow = fast = 1`). Phase 2 finds entry after one iteration. |
| **Duplicate appears at index `0`** – not possible because values start at `1`. The algorithm relies on this guarantee. |
| **Multiple duplicates** – problem spec guarantees **exactly one** duplicate, otherwise the graph could have multiple cycles and Floyd’s algorithm would still return *some* cycle entry but not necessarily a duplicate value. |
| **Large `n` (≈10⁵‑10⁶)** – algorithm uses only O(1) memory, safe for typical stack/heap limits. |
| **Potential integer overflow** – indices never exceed `n ≤ 10⁵` (or whatever limit), well below `int` max. No overflow concerns. |

---  

## 4️⃣ Key Insights & Gotchas  

| Insight | Explanation |
|---------|-------------|
| **Treating the array as a linked list** | This mental model turns a “find duplicate” problem into a classic **cycle‑detection** problem. It eliminates the need for sorting or extra hashing. |
| **Why start from index `0` (a “virtual” head)** | `0` is guaranteed not to be a duplicate value, so the walk starts outside the cycle. This aligns the algorithm with the standard proof of Floyd’s method. |
| **Meeting point is *not* the duplicate** – the first meeting (Phase 1) can be anywhere inside the cycle. Only after resetting one pointer does the second meeting give the entry. |
| **Two‑step fast pointer** – must be `nums[nums[fast]]`, not `fast + 2`. Because we walk along the *graph*, not the linear array. |
| **No need for a `do…while`** – the `while(true)` with an explicit break is a common pattern to avoid extra condition checks; however, a `do…while` would be equally correct. |
| **Potential bug: using `fast = nums[fast]`** – would cause the fast pointer to move only one step, breaking the O(n) guarantee (it could degrade to O(n²) in pathological graphs). |
| **If the array were 0‑indexed (values in `[0, n-1]`)** – you would need to start from any index (e.g., `0`) but also ensure the duplicate is not the start node; the proof still holds but you must avoid a self‑loop at the head. |
| **Memory‑only constraint** – this solution is the canonical answer for the LeetCode “Find the Duplicate Number” problem where you cannot modify the input and must use O(1) extra space. |
| **Time‑space trade‑off** – alternative solutions (sorting, hash set) use O(n log n) or O(n) time with O(n) space. Floyd’s method is optimal in both dimensions for this problem class. |

---  

## 5️⃣ Annotated Reference Implementation  

```java
/**
 * Floyd's Tortoise‑and‑Hare algorithm.
 * Time   : O(n)  (linear walk until the two phases converge)
 * Space  : O(1)  (three integer variables)
 *
 * Preconditions:
 *   - nums.length == n + 1   (n >= 1)
 *   - 1 <= nums[i] <= n for every i
 *   - Exactly one value appears twice (others appear once)
 */
class Solution {
    public int findDuplicate(int[] nums) {
        /* ---------------------------------------------------------
         * Phase 1 – Find a meeting point inside the cycle.
         * ------------------------------------------------------- */
        int slow = 0;      // moves 1 step each iteration
        int fast = 0;      // moves 2 steps each iteration

        while (true) {
            slow = nums[slow];               // 1 hop
            fast = nums[nums[fast]];         // 2 hops
            if (slow == fast) {              // they met → inside cycle
                break;
            }
        }

        /* ---------------------------------------------------------
         * Phase 2 – Locate the entrance (duplicate number).
         * ------------------------------------------------------- */
        int entry = 0;   // start a fresh pointer from the head

        while (true) {
            slow = nums[slow];   // 1 step from meeting point
            entry = nums[entry]; // 1 step from head
            if (slow == entry) { // first coincidence is the entry
                return slow;     // duplicate value
            }
        }
    }
}
```

---  

## 6️⃣ When to Use (and When Not To)  

| Situation | Recommended? | Reason |
|-----------|---------------|--------|
| **Exact problem constraints** (read‑only array, O(1) extra memory) | ✅ | Floyd’s algorithm satisfies both. |
| **Multiple duplicates** | ❌ | The graph may contain several cycles; the method returns *a* cycle entry but not necessarily a specific duplicate. |
| **Mutable input allowed** | ❌ (or ✅ if you prefer O(1) space) | Simpler approaches (in‑place marking, swapping) are easier to understand and may be faster due to fewer indirections. |
| **Very small `n` (≤ 10)** | ✅ (still works) but overhead of extra loops may dominate; a brute‑force O(n²) could be clearer. |
| **Parallel processing** | ❌ | The algorithm is inherently sequential; no obvious parallelism. |

---  

## 7️⃣ References & Further Reading  

1. **Robert W. Floyd**, *“Algorithm 202 – Tortoise and Hare”*, Communications of the ACM, 1970.  
2. LeetCode problem **287. Find the Duplicate Number** – discussion of Floyd vs. binary‑search / bit‑vector solutions.  
3. “Functional Graph” – concept used in many cycle‑detection problems (e.g., “Linked List Cycle II”).  

---  

**Bottom line**: By re‑interpreting the array as a deterministic graph and applying Floyd’s classic cycle detection, we obtain a *linear‑time, constant‑space* solution that directly yields the duplicated element as the cycle’s entry point. The implementation is succinct, robust under the given constraints, and avoids any mutation or auxiliary containers.

---
