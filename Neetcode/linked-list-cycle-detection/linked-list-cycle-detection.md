# linked-list-cycle-detection

## linkedlist_solution(slowfast_pointers).java
*Style: detailed*

# 📚 Deep‑Dive Reference: Detecting a Cycle in a Singly‑Linked List  
*Solution class – Floyd’s Tortoise‑and‑Hare algorithm*  

---  

## 1️⃣ Summary – What the Code Does & Why It Works  

The method `hasCycle(ListNode head)` determines whether a singly‑linked list contains a cycle (i.e., a node whose `next` reference eventually points back to an earlier node).  

**Core technique** – *Floyd’s Cycle‑Finding algorithm* (also called *Tortoise and Hare*).  
- Two pointers traverse the list at different speeds:  
  - **slow** → advances `1` node per loop iteration.  
  - **fast** → advances `2` nodes per loop iteration.  
- If the list is *acyclic*, the fast pointer reaches the terminal `null` and the loop exits.  
- If the list *contains a cycle*, the fast pointer will eventually “lap” the slow pointer inside the loop, causing `slow == fast` and the method returns `true`.

The algorithm requires **only two references** and runs in **linear time** with **constant extra space**.

---  

## 2️⃣ Complexity Analysis  

| Metric | Derivation | Result |
|--------|------------|--------|
| **Time** | Let `n` be the number of distinct nodes reachable from `head`. <br> • In the *acyclic* case each iteration advances fast by 2 nodes, so at most `⌈n/2⌉` iterations before hitting `null`. <br> • In the *cyclic* case let `μ` be the number of nodes before the cycle begins (the “tail”), and `λ` be the cycle length. After `μ` steps both pointers are inside the cycle. Inside the cycle the distance between fast and slow grows by `1` each iteration (fast gains one extra step per loop). After at most `λ` additional iterations they meet. <br> • Total iterations ≤ `μ + λ ≤ n`. | **O(n)** |
| **Space** | Only two `ListNode` references (`slowPointer`, `fastPointer`) are allocated, plus the method’s stack frame. No auxiliary containers are used. | **O(1)** |

**Why `O(n)` is tight** – In the worst case (a long tail followed by a long cycle) the algorithm must traverse every node at least once to guarantee detection. Any algorithm that must *inspect* each node cannot beat `Θ(n)` time.

---  

## 3️⃣ Component Deep Dive  

### 3.1 Class & Node Definition  

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
```

- The node is a *plain data holder*; `val` is irrelevant for cycle detection – we only compare *references* (`==`), not values.
- The `next` pointer may be `null` (list termination) or point to any node (including itself).

### 3.2 Method Signature  

```java
public boolean hasCycle(ListNode head)
```

- Returns `true` if a cycle exists, else `false`.
- Accepts `null` (empty list) gracefully – the while‑condition short‑circuits.

### 3.3 Pointer Initialization  

```java
ListNode slowPointer = head;
ListNode fastPointer = head;
```

- Both start at the same node.  
- **Important nuance:** The algorithm does *not* check `slowPointer == fastPointer` before the first move; otherwise a list of length 0 or 1 (with a self‑loop) would incorrectly report a cycle. The first iteration moves them forward before any comparison, guaranteeing that a meeting can only happen *after* at least one step.

### 3.4 Loop Guard  

```java
while (fastPointer != null && fastPointer.next != null) {
    // body
}
```

- **Safety:** Guarantees that `fastPointer.next.next` is a valid dereference.  
- **Why both checks?**  
  - `fastPointer != null` protects against the case where the list ends exactly at the current node.  
  - `fastPointer.next != null` protects against the case where the list ends after one more node (i.e., an odd‑length tail).  
- This guard ensures **no NullPointerException** regardless of input shape.

### 3.5 Pointer Advancement  

```java
slowPointer = slowPointer.next;          // 1 step
fastPointer = fastPointer.next.next;    // 2 steps
```

- `slowPointer` may become `null` *only* when the list has no cycle; the loop guard prevents dereferencing it after that point because the guard already failed.
- `fastPointer` leaps two nodes, which is the source of the “lap” effect inside a cycle.

### 3.6 Cycle Detection  

```java
if (slowPointer == fastPointer) {
    return true;
}
```

- **Reference equality** (`==`) is essential: we care if both pointers *refer to the exact same node object*, not whether they hold equal values.
- If they meet, we have proven the existence of a cycle, because in a finite acyclic list two pointers moving at different speeds can never occupy the same node simultaneously (they would need to converge at the unique `null` terminus, which the guard prevents).

### 3.7 Loop Exit & Return  

```java
return false;   // fastPointer reached null → no cycle
```

- Reaching this line means the fast pointer walked off the end of the list, so the structure is a simple chain.

### 3.8 Edge‑Case Coverage  

| Edge case | How the code handles it |
|-----------|--------------------------|
| `head == null` (empty list) | Guard fails immediately (`fastPointer == null`) → returns `false`. |
| Single node, `next == null` | Guard fails (`fastPointer.next == null`) → returns `false`. |
| Single node, `next` points to itself (self‑loop) | First iteration moves both pointers to the same node (`slow = head.next` → `head`; `fast = head.next.next` → `head`). The `if` condition succeeds → returns `true`. |
| Two‑node list, no cycle | Fast pointer becomes `null` after first iteration → returns `false`. |
| Two‑node list, second node points back to first (2‑node cycle) | After first iteration `slow` at node2, `fast` at node2 → `slow == fast` → `true`. |
| Long tail + cycle | Fast pointer traverses tail then enters cycle; after at most `λ` more steps it meets slow pointer inside the cycle. |

---  

## 4️⃣ Key Insights & Gotchas  

### 4.1 Why Not Use a HashSet?  
A naïve approach stores visited node references in a `HashSet` and checks membership on each step.  
- **Time:** Still O(n).  
- **Space:** O(n) extra memory, which is unnecessary because Floyd’s algorithm achieves the same time with O(1) space.  
- **Cache friendliness:** Two-pointer traversal touches each node sequentially, leading to better locality.

### 4.2 Proof Sketch of Correctness  

1. **Acyclic case** – The list is a finite chain ending at `null`.  
   - Since `fastPointer` moves strictly faster, it will reach `null` before `slowPointer`.  
   - The loop guard stops before any dereference of `null`, guaranteeing `false`.

2. **Cyclic case** – Let the cycle length be `λ`. Once both pointers are inside the cycle, define the distance (in number of edges) from `slow` to `fast` as `d`.  
   - Each iteration increments `d` by `1` (fast gains an extra step).  
   - `d` evolves as `d, d+1, d+2, …` modulo `λ`.  
   - Since `λ` and `1` are coprime, the sequence will eventually hit `0` (i.e., `d ≡ 0 (mod λ)`), meaning `slow == fast`.  

Thus the algorithm is both **sound** (never reports a cycle when none exists) and **complete** (always reports a cycle when one exists).

### 4.3 Subtle Bug‑Avoidance  

| Pitfall | Symptom | Fix (as in code) |
|---------|---------|-------------------|
| Checking `slow == fast` before moving pointers | A list with `head == null` would incorrectly return `true` (both pointers start `null`). | Perform the equality test **after** the first advancement. |
| Using `fastPointer != null && fastPointer.next != null` incorrectly (e.g., `while (fastPointer != null)`) | Potential `NullPointerException` when `fastPointer.next` is `null`. | Include both conditions as shown. |
| Comparing `slowPointer.val == fastPointer.val` | False positives on distinct nodes that happen to hold equal values. | Compare references (`==`), not values. |
| Modifying the list while traversing (e.g., `slowPointer = slowPointer.next.next`) | May skip nodes, break detection guarantees. | Keep the step sizes exactly 1 and 2. |
| Forgetting to reset pointers for multiple calls on the same `Solution` instance | Stale state may leak between calls. | Pointers are local variables; no shared mutable state. |

### 4.4 Extending the Algorithm  

1. **Finding the entry node of the cycle**  
   - After detection, reset `slowPointer` to `head`.  
   - Move both pointers one step at a time; they meet at the cycle start.  
2. **Computing cycle length**  
   - Keep `fastPointer` stationary after detection and advance `slowPointer` until it meets `fastPointer` again, counting steps.  

Both extensions still run in O(n) time and O(1) space.

### 4.5 Performance Micro‑Optimizations  

- **Inlining**: Modern JIT compilers inline the simple loop; no manual unrolling needed.  
- **Branch prediction**: The equality check is highly predictable (false most of the time), which aids CPU pipelines.  
- **Avoiding extra null checks**: The guard already ensures safety; no need for an additional `slowPointer != null` check inside the loop.  

---  

## 5️⃣ TL;DR Cheat Sheet  

| Concept | Implementation Detail |
|---------|------------------------|
| **Algorithm** | Floyd’s Tortoise‑and‑Hare (two‑pointer) |
| **Pointers** | `slow` steps `+1`, `fast` steps `+2` |
| **Loop Guard** | `while (fast != null && fast.next != null)` |
| **Cycle detection** | `if (slow == fast) return true;` |
| **Time** | O(n) – each node visited ≤ 2× |
| **Space** | O(1) – only two extra references |
| **Edge Cases** | Handles empty list, single‑node self‑loop, long tail + cycle |
| **Common Bugs** | Premature equality check, missing `fast.next` null guard, comparing values instead of references |

---  

### Bottom Line  

The provided implementation is a textbook, production‑ready example of cycle detection in a singly‑linked list. It leverages the mathematical guarantee that two pointers moving at different speeds must eventually coincide inside a finite loop, while guaranteeing **linear time** and **constant auxiliary space**. Proper guard conditions and reference‑based equality ensure robustness across all edge cases.

---
