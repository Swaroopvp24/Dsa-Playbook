# find-duplicate-integer

## kinda_linkedlist_solution(slowfast_pointers).java
*Style: detailed*

# 📘 Deep‑Dive Reference: Floyd’s Tortoise‑and‑Hare for *Find the Duplicate Number*  

*File:* `Solution.java`  
*Method:* `int findDuplicate(int[] nums)`

---

## 1️⃣ Summary – Algorithmic Technique

The solution treats the input array `nums` as a **functional graph** (a directed graph where every node has out‑degree 1).  
* Node = array index `i` (0‑based).  
* Edge = directed from `i → nums[i]`.  

Because the array length is `n + 1` and every value lies in `[1, n]`, the graph contains exactly **one directed cycle** (pigeonhole principle). The duplicate value is precisely the **entry node** of that cycle.

The algorithm is a classic **Floyd’s cycle detection** (Tortoise‑and‑Hare) applied to the functional graph:

1. **Phase 1 – Find a meeting point** inside the cycle using a slow pointer (1 step) and a fast pointer (2 steps).  
2. **Phase 2 – Locate the cycle entrance** by resetting one pointer to the start (index 0) and advancing both at equal speed; they meet at the duplicate value.

No extra data structures are allocated, satisfying the *O(1) extra space* constraint while running in linear time.

---

## 2️⃣ Complexity Analysis  

| Metric | Derivation |
|--------|------------|
| **Time** | **O(n)** – Each iteration moves pointers strictly forward along edges. In Phase 1 the slow pointer traverses at most `μ + λ` steps (μ = distance to cycle entry, λ = cycle length). The fast pointer moves twice as fast, so the number of loop executions ≤ `μ + λ`. Phase 2 performs at most `μ` steps until both pointers converge. Summed ≤ `2μ + λ ≤ 2n`. |
| **Space** | **O(1)** – Only a handful of `int` variables (`slowPointer`, `fastPointer`, `cycleEntryPointer`). No recursion, no auxiliary containers. |
| **Modifications** | **None** – The array is read‑only; the algorithm works in‑place without altering `nums`. |

*Why it is linear:* The functional graph contains exactly `n+1` edges, each visited at most a constant number of times (once by the slow pointer, up to twice by the fast pointer). No nested loops or back‑tracking occur.

---

## 3️⃣ Component Deep Dive  

### 3.1 Data Model – Functional Graph Interpretation  

```java
// Conceptual mapping:
int next(int i) { return nums[i]; }
```

- **Domain:** indices `0 … n` (size `n+1`).  
- **Range:** values `1 … n`.  
- **Guarantee:** At least one value repeats → graph has a *single* cycle (could be a self‑loop if duplicate appears twice consecutively, e.g., `nums = [1,1]`).

### 3.2 Phase 1 – Cycle Detection  

```java
while (true) {
    slowPointer = nums[slowPointer];          // 1 step
    fastPointer = nums[nums[fastPointer]];    // 2 steps
    if (slowPointer == fastPointer) break;
}
```

| Variable | Role |
|----------|------|
| `slowPointer` | Tortoise; walks `f(i) = nums[i]` once per iteration. |
| `fastPointer` | Hare; walks `f(f(i))` (two hops) per iteration. |

**Invariant:** After `k` iterations, `slowPointer = f^k(0)`, `fastPointer = f^{2k}(0)`.  
**Proof of convergence:** Let `μ` be distance from start (0) to cycle entry, `λ` cycle length. Once both pointers enter the cycle (after ≤ `μ` steps for the slow, ≤ `2μ` for the fast), the distance between them modulo `λ` reduces by 1 each iteration (fast gains one extra step per loop). By the pigeonhole principle they must meet after at most `λ` more iterations.

*Edge Cases*  
- **Self‑loop duplicate** (`nums = [1,1]`): `slow = fast = 1` after first iteration → immediate break.  
- **Duplicate at index 0** (`nums[0]` equals a later element): still works because the graph always starts from node 0, which points to a value in `[1,n]`.

### 3.3 Phase 2 – Cycle Entrance Identification  

```java
int cycleEntryPointer = 0;
while (true) {
    slowPointer = nums[slowPointer];
    cycleEntryPointer = nums[cycleEntryPointer];
    if (slowPointer == cycleEntryPointer) return slowPointer;
}
```

- **Reset:** `cycleEntryPointer` starts at the source node (index 0).  
- **Movement:** Both pointers now advance *single* steps (`f`).  
- **Invariant:** After `t` iterations,  
  - `slowPointer = f^{μ + t}(0)` (starting from meeting point).  
  - `cycleEntryPointer = f^{t}(0)`.  

When `t = μ`, `cycleEntryPointer` reaches the entry node, and `slowPointer` has traversed exactly `μ` steps inside the cycle, landing on the same entry node. Thus they meet at the duplicate value.

*Why we return `slowPointer` (or `cycleEntryPointer`)*: Both hold the **index** that equals the duplicate **value**, because in the functional graph the node identity *is* the array value we need.

### 3.4 Correctness Sketch  

1. **Existence of a Cycle:**  
   - `|V| = n+1`, `|range(nums)| ≤ n`. By the pigeonhole principle, at least two distinct indices map to the same value, creating a directed cycle.
2. **Uniqueness of Cycle Entry:**  
   - The functional graph of a map `f: V → V` with out‑degree 1 has at most one cycle reachable from any start node. Starting from 0, we inevitably reach *the* cycle that contains the duplicate.
3. **Meeting Point Guarantees Cycle Membership:**  
   - Floyd’s algorithm guarantees the first meeting point lies inside the cycle.
4. **Entrance Detection Guarantees Duplicate:**  
   - The second phase returns the unique node where the two pointers first coincide, which is the entry of the only cycle → the duplicated number.

Thus the algorithm always returns the required duplicate.

---

## 4️⃣ Key Insights & Gotchas  

| Insight | Explanation / Pitfall |
|---------|------------------------|
| **Why index 0 is safe as start** | Even though `nums[0]` can be any value in `[1,n]`, the graph definition guarantees a path from 0 into the unique cycle. No need to start from a value‑containing index. |
| **Cycle length does *not* affect correctness** | The algorithm does not need `λ`; Phase 1 converges in ≤ `μ + λ` steps, Phase 2 in exactly `μ`. Both are bounded by `n`. |
| **Self‑loop handling** | If the duplicate forms a self‑loop (`i → i`), the fast pointer may “skip over” the loop in one iteration, but because we compute `fast = nums[nums[fast]]`, the double dereference still lands on the same node, causing a meeting. |
| **Potential `ArrayIndexOutOfBoundsException`** | The code never accesses `nums[-1]` because all pointers start at 0 and `nums[i]` is guaranteed to be ≥ 1. The only risk would be a malformed input violating problem constraints (e.g., a value of `0`); the algorithm assumes valid input. |
| **Space vs. Time trade‑off** | Alternative solutions (hash set, sorting) use O(n) extra space or O(n log n) time. Floyd’s method achieves the optimal O(1) extra space with linear time, but its constant factor is larger due to two pointer traversals. |
| **Thread‑safety** | The method is pure (read‑only) and can be called concurrently on distinct arrays without synchronization. |
| **Integer overflow** | No arithmetic beyond array indexing; overflow cannot happen. |
| **Proof of O(1) extra space** | Only three primitive `int` variables are allocated on the stack; no heap allocation. |
| **Extensibility** | The same pattern works for any problem reducible to “find the start of a cycle in a functional graph,” e.g., *Linked List Cycle II* (LeetCode 142). |

---

## 5️⃣ Annotated Source (for quick reference)

```java
class Solution {
    /**
     * Returns the single duplicated number in {@code nums}.
     * Preconditions:
     *   - nums.length == n + 1,  n >= 1
     *   - each nums[i] is in [1, n]
     *   - exactly one value appears at least twice
     */
    public int findDuplicate(int[] nums) {
        // ---------- Phase 1: find meeting point ----------
        int slow = 0;               // tortoise
        int fast = 0;               // hare
        while (true) {
            slow = nums[slow];              // 1 step
            fast = nums[nums[fast]];        // 2 steps
            if (slow == fast) break;        // inside the cycle
        }

        // ---------- Phase 2: locate cycle entry ----------
        int entry = 0;                       // starts from the head
        while (true) {
            slow = nums[slow];              // move both 1 step
            entry = nums[entry];
            if (slow == entry) return slow; // duplicate found
        }
    }
}
```

---

## 6️⃣ Quick Checklist for Implementers  

- [ ] Validate input size (`nums.length >= 2`).  
- [ ] Ensure values are within `[1, n]` (otherwise algorithm may access out of bounds).  
- [ ] Do **not** modify `nums`; the method must remain pure.  
- [ ] Remember to return `slow` (or `entry`) after Phase 2 – both hold the duplicate.  
- [ ] No need for additional termination condition; the `while(true)` loops are guaranteed to break by the mathematical properties described above.

---

**Bottom line:**  
The solution is a textbook application of Floyd’s Tortoise‑and‑Hare cycle detection on a functional graph derived from the input array. It delivers **O(n) time** and **O(1 extra space**), while elegantly sidestepping any need for auxiliary data structures or array mutation.

---
