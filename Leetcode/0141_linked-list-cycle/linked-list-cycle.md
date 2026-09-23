# linked-list-cycle

## linkedlist_solution(slowfast_pointers).java
*Style: detailed*

# 📘 Deep‑Dive Reference: Detecting a Cycle in a Singly‑Linked List  
*Solution implementation – Java (Floyd’s Tortoise & Hare)*  

---  

## 1️⃣ Summary – What the code does & the algorithmic idea  

| Goal | Detect whether a `ListNode` chain contains a cycle (i.e. a node whose `next` pointer eventually points back to an earlier node). |
|------|--------------------------------------------------------------------------------------------------------------------------|
| Technique | **Floyd’s Cycle‑Finding algorithm** (aka *Tortoise & Hare*). Two pointers traverse the list at different speeds; if a cycle exists they must eventually collide. |
| Core invariant | After `k` iterations: `slow` has moved `k` steps, `fast` has moved `2k` steps. In a cyclic list the distance between them modulo the cycle length shrinks by 1 each iteration, guaranteeing a meeting point. |
| Return value | `true` if a meeting occurs → cycle present, otherwise `false`. |

---

## 2️⃣ Complexity Analysis  

| Metric | Derivation |
|--------|------------|
| **Time** | `O(n)` where `n` is the number of *distinct* nodes reachable from `head`. <br> *Why*: In the worst case (acyclic list) the loop iterates once per node until `fast` hits `null`. In a cyclic list each iteration advances the slow pointer by one node; the fast pointer makes two hops, so the number of iterations before collision is at most `μ + λ` where `μ` is the non‑cycle prefix length and `λ` the cycle length. Both are ≤ `n`. |
| **Space** | `O(1)` – only two additional `ListNode` references (`slowPointer`, `fastPointer`). No auxiliary containers are allocated. |
| **Auxiliary work** | Constant‑time pointer updates (`.next` accesses) per iteration; no recursion, no heap allocations. |

---

## 3️⃣ Component Deep Dive  

### 3.1 Data Structure – `ListNode`

```java
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; next = null; }
}
```

*Only the `next` reference matters for cycle detection.*  
The `val` field is irrelevant to the algorithm; it could be any type.

### 3.2 Core Function – `hasCycle`

```java
public boolean hasCycle(ListNode head) {
    ListNode slowPointer = head;
    ListNode fastPointer = head;

    while (fastPointer != null && fastPointer.next != null) {
        slowPointer = slowPointer.next;          // 1 step
        fastPointer = fastPointer.next.next;     // 2 steps

        if (slowPointer == fastPointer) {
            return true;                         // meeting point → cycle
        }
    }
    return false;                                // fast hit list end → acyclic
}
```

#### Step‑by‑step execution  

| Step | Condition checked | Pointer updates | Cycle detection |
|------|-------------------|----------------|-----------------|
| 1️⃣   | `fastPointer != null && fastPointer.next != null` | – | Guarantees `fastPointer.next.next` is safe. |
| 2️⃣   | `slowPointer = slowPointer.next` | Moves *tortoise* one node forward. |
| 3️⃣   | `fastPointer = fastPointer.next.next` | Moves *hare* two nodes forward. |
| 4️⃣   | `if (slowPointer == fastPointer)` | Identity comparison (reference equality). |
| 5️⃣   | `return true` | Early exit on detection. |
| 6️⃣   | Loop repeats until fast reaches list end → `return false`. |

#### Edge‑case handling  

| Edge case | Code path | Why it works |
|-----------|-----------|--------------|
| `head == null` | Loop guard fails (`fastPointer == null`). | Returns `false` – empty list cannot contain a cycle. |
| Single node, `next == null` | Guard fails on `fastPointer.next == null`. | Returns `false`. |
| Single node, `next` points to itself | Guard passes (`fastPointer != null && fastPointer.next != null`). After first iteration both pointers refer to the same node → `true`. |
| Extremely long acyclic list | Loop runs `n` times, each iteration constant work. | Time stays `O(n)`. |
| Cycle starting at head (`μ = 0`) | Fast and slow start together; after one iteration they differ, but will meet after ≤ `λ` steps. | Guarantees detection. |

### 3.3 Why the loop condition is **crucial**  

```java
while (fastPointer != null && fastPointer.next != null)
```

* Guarantees `fastPointer.next.next` is a valid dereference.  
* Prevents a `NullPointerException` on the *last* node of an acyclic list (where `fastPointer.next` is `null`).  
* If we used `while (fastPointer != null)` alone, the subsequent `fastPointer.next.next` would throw when `fastPointer.next` is `null`.

### 3.4 Reference vs. value comparison  

`slowPointer == fastPointer` checks **object identity**, not `val`.  
Only identity can prove two traversals have landed on the *same node* in memory – essential for cycle detection.

---

## 4️⃣ Key Insights & Gotchas  

| Insight | Explanation |
|---------|-------------|
| **Invariant shrinkage** – In a cyclic list, the distance `d` (modulo cycle length `λ`) between fast and slow reduces by 1 each iteration (`d_{k+1} = (d_k - 1) mod λ`). Hence after at most `λ` steps they collide. |
| **Constant‑space guarantee** – No need for a hash set of visited nodes; the algorithm leverages the structure itself. |
| **Potential subtle bug** – Omitting the `fastPointer.next != null` part leads to a **runtime NPE** on the iteration where `fastPointer` is the last node of a non‑cyclic list. |
| **Equality vs. `.equals`** – Using `.equals` would be wrong unless `ListNode` overrides it to compare identities; default `Object.equals` is the same as `==`, but explicit `==` is clearer and avoids accidental deep equality checks. |
| **Performance tip** – The loop body contains *two* pointer dereferences per iteration. In a hot path (e.g., millions of nodes) a micro‑benchmark may show a slight edge to a hand‑rolled `while (true)` with manual break conditions, but the readability/maintainability trade‑off heavily favors the current form. |
| **Detect‑and‑remove variant** – Once a meeting point is found, resetting one pointer to `head` and moving both at speed 1 yields the cycle entry node in `O(n)` time, `O(1)` space. The current method stops at detection only. |
| **Thread safety** – The algorithm assumes a *static* list (no concurrent modifications). Concurrent insertion/removal could produce transient states where the invariant breaks, potentially causing infinite loops or false negatives. |
| **Memory model** – In a Java VM, object references are *volatile* enough for this single‑threaded algorithm; no additional synchronization is required. |
| **Alternative O(n) space solution** – Using a `HashSet<ListNode>` to store visited nodes also works (`O(n)` time, `O(n)` space). Floyd’s method is strictly superior for space‑constrained environments. |
| **Testing edge cases** – Unit tests should cover: empty list, single node (self‑loop vs. no loop), two‑node cycle, long acyclic list, and a list where the cycle begins after a non‑cyclic prefix (`μ > 0`). |

---

## 5️⃣ Annotated Reference Implementation (for documentation)

```java
/**
 * Detects a cycle in a singly‑linked list using Floyd's Tortoise‑and‑Hare algorithm.
 *
 * @param head the first node of the list (may be {@code null})
 * @return {@code true} if a cycle exists, {@code false} otherwise
 */
public boolean hasCycle(ListNode head) {
    // Both pointers start at the same node.
    ListNode slow = head;
    ListNode fast = head;

    // Continue while fast can advance two steps safely.
    while (fast != null && fast.next != null) {
        slow = slow.next;               // one step
        fast = fast.next.next;          // two steps

        // Identity check – same memory address means a meeting point.
        if (slow == fast) {
            return true;                // Cycle detected.
        }
    }

    // fast reached the list terminus → no cycle.
    return false;
}
```

---  

### TL;DR  

*Floyd’s Tortoise & Hare* runs in linear time, constant extra space, and reliably detects a loop by advancing two pointers at different speeds and checking for identity. The only subtlety is the loop guard (`fast != null && fast.next != null`) which prevents a null‑pointer dereference. The method is robust for all edge cases (empty list, single‑node self‑loop, long acyclic list) and forms the baseline for any further cycle‑related operations (finding entry point, removing the loop, etc.).

---
