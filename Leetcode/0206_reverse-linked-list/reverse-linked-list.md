# reverse-linked-list

## linkedlist_solution.java
*Style: detailed*

# Reverse a Singly‑Linked List – Deep‑Dive Reference  

*File*: `Solution.java`  
*Method*: `public ListNode reverseList(ListNode head)`  

---  

## 1. Summary  

The solution implements the classic **in‑place iterative reversal** of a singly‑linked list.  
It walks the list once, rewiring each node’s `next` pointer to point at the node that precedes it in the original order.  
Two sliding pointers—`previous` (the head of the partially‑reversed prefix) and `current` (the node currently being processed)—maintain the invariant that **all nodes before `current` are already reversed**, while the remainder of the list is untouched.

The algorithm is *deterministic* and runs in **O(N)** time with **O(1)** auxiliary space, where *N* is the number of nodes.

---

## 2. Complexity Analysis  

| Aspect | Reasoning | Big‑O |
|--------|-----------|-------|
| **Time** | Each iteration executes a constant amount of work: read `current.next`, assign `current.next`, and advance two pointers. The loop executes exactly once per node (including the `null` termination check). | **O(N)** |
| **Space (auxiliary)** | Only three local references (`previous`, `current`, `nextNode`) are allocated on the stack. No data structures proportional to *N* are created. | **O(1)** |
| **Space (output)** | The reversed list re‑uses the original nodes; no new `ListNode` objects are allocated. | **O(1)** (in‑place) |

*Why the invariant guarantees linear time:*  
The loop condition `while (current != null)` ensures each node becomes `current` exactly once; after the iteration the node moves to the reversed prefix (`previous`). No node is revisited, so the total number of pointer assignments is `3 * N` (read, write, advance) → linear.

*Why the space is constant:*  
All bookkeeping variables are scalar references. The recursion stack depth is zero because the algorithm is iterative.

---

## 3. Component Deep Dive  

### 3.1. Data Structure – `ListNode`

```java
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
```

*Key properties*  

* `val` – payload (irrelevant for reversal).  
* `next` – mutable reference to the successor node.  

The reversal never touches `val`; only `next` pointers are mutated.

---

### 3.2. Core Variables  

| Variable | Meaning (at loop start) | Updated To |
|----------|------------------------|------------|
| `previous` | Head of the already‑reversed prefix (null initially). | `previous = current` (the node just processed). |
| `current`  | First node of the *unprocessed* suffix (starts at original `head`). | `current = nextNode` (the saved original successor). |
| `nextNode` | Temporary storage for `current.next` before it is overwritten. | Assigned each iteration: `nextNode = current.next`. |

**Invariant** (maintained at the top of each loop iteration):  

```
previous → reversed list (head = previous, tail = original head)
current  → first node of the remaining original list (may be null)
```

When `current` becomes `null`, `previous` points to the new head of the fully reversed list.

---

### 3.3. Step‑by‑Step Walkthrough  

Assume an original list: `A → B → C → D → null`.

| Iteration | `previous` (reversed prefix) | `current` (node being processed) | `nextNode` (saved) | Action (`current.next = previous`) | Resulting Links |
|-----------|------------------------------|-----------------------------------|-------------------|------------------------------------|-----------------|
| 0 (init) | `null` | `A` | – | – | – |
| 1 | `null` | `A` | `B` | `A.next = null` | `A → null` |
|   | `A` | `B` | `C` | `B.next = A` | `B → A → null` |
| 2 | `B → A` | `C` | `D` | `C.next = B` | `C → B → A → null` |
| 3 | `C → B → A` | `D` | `null` | `D.next = C` | `D → C → B → A → null` |
| 4 | `D → C → B → A` | `null` | – | loop exits | `previous` is new head (`D`). |

---

### 3.4. Edge‑Case Handling  

| Edge case | How the code behaves | Why it is safe |
|-----------|----------------------|----------------|
| `head == null` (empty list) | `current` is `null` initially → loop never runs → returns `previous` (`null`). | Returns an empty list as required. |
| Single‑node list (`head.next == null`) | Loop runs once: `nextNode = null`, `current.next = null` (already), `previous = head`, `current = null`. Returns original node. | No pointer is lost; reversal of one element is a no‑op. |
| Very long list (e.g., 10⁶ nodes) | O(N) time, O(1) extra memory; no stack overflow. | Iterative approach avoids recursion depth limits. |
| List with cycles (invalid input) | The loop would never encounter `null` and would spin forever → **undefined behavior**. | The problem statement guarantees an acyclic list; defensive programming could add a cycle detection guard (Floyd’s algorithm) if needed. |
| `ListNode` objects reused elsewhere (shared tails) | The algorithm mutates `next` pointers, so any other references to the same nodes will now see the reversed order. | This is inherent to in‑place mutation; if sharing is required, a copy‑based algorithm must be used. |

---

### 3.5. Correctness Proof Sketch  

*Lemma*: After *k* iterations (`k ≥ 0`), the following hold:
1. `previous` points to the head of a list consisting of the first *k* original nodes, now reversed.
2. `current` points to the (k+1)‑th original node (or `null` if *k = N*).

*Proof by induction*:  

*Base (k = 0)*: `previous = null` (empty reversed prefix), `current = head` (first original node). Both statements true.  

*Inductive step*: Assume statements true for *k*. During iteration *k+1*:
- `nextNode` stores the original successor of `current` (the (k+2)-th node).  
- `current.next = previous` rewires `current` to point to the reversed prefix, thus extending the reversed list by one node at the front.  
- `previous = current` updates the reversed‑list head to the newly processed node.  
- `current = nextNode` moves to the (k+2)-th node.  

Thus after iteration *k+1* the invariants hold for *k+1*.  

When `current == null` (k = N), `previous` points to the head of the fully reversed list. ∎

---

## 4. Key Insights & Gotchas  

### 4.1. Why a Temporary Variable (`nextNode`) Is Mandatory  

If you wrote `current.next = previous` **before** saving `current.next`, you lose the reference to the remainder of the list. The list would become truncated and the algorithm would either enter a null‑dereference or a cycle, depending on the order of assignments.

### 4.2. In‑Place vs. Copy‑Based Reversal  

*In‑place* (this solution) is optimal for memory, but it **mutates** the original structure.  
If callers need the original order preserved, you must first **clone** the list (O(N) time + O(N) space) and then reverse the clone.

### 4.3. Iterative vs. Recursive  

A recursive version:

```java
ListNode reverse(ListNode node, ListNode prev) {
    if (node == null) return prev;
    ListNode nxt = node.next;
    node.next = prev;
    return reverse(nxt, node);
}
```

*Pros*: concise, expresses the same invariant.  
*Cons*: consumes O(N) stack frames → risk of `StackOverflowError` for large N (≈10⁵+ in typical JVM settings). The iterative version avoids this.

### 4.4. Potential Micro‑Optimizations  

1. **Avoid redundant null checks** – the `while (current != null)` guard already guarantees `nextNode` is non‑null only when needed.  
2. **Final return** – some compilers can inline the final `return previous;` after loop exit; no extra work required.  
3. **JVM escape analysis** – because `nextNode` does not escape the loop, the JIT can allocate it on a register, eliminating heap pressure entirely.

### 4.5. Subtle Bug Patterns When Modifying  

| Symptom | Common mistake | Fix |
|---------|----------------|-----|
| Lost nodes after reversal | `current = current.next;` *after* reassigning `current.next` (i.e., not using a temporary) | Store `nextNode` **before** mutating `current.next`. |
| Infinite loop | Using `while (head != null)` and reassigning `head = head.next` **after** reversal, forgetting to move a separate pointer. | Keep `current` as the traversal pointer; never reuse `head` inside the loop. |
| NullPointerException on return | Returning `head` instead of `previous` when list is non‑empty. | After loop, `previous` holds the new head; `head` now points to the tail (original head) whose `next` is `null`. |

### 4.6. Extending to Doubly‑Linked Lists  

For a `ListNode` with both `next` and `prev`, reversal reduces to swapping the two pointers for each node:

```java
while (cur != null) {
    ListNode tmp = cur.next;
    cur.next = cur.prev;
    cur.prev = tmp;
    cur = cur.prev; // because cur.next now points to the original prev
}
```

The same O(N)/O(1) profile holds.

### 4.7. Thread‑Safety Considerations  

The method **mutates** the list in place. If the same list instance is accessed concurrently (e.g., by other threads), you must synchronize externally or work on a private copy. No internal synchronization is present.

---

## 5. Reference Implementation (Annotated)

```java
/**
 * Reverses a singly‑linked list in‑place.
 *
 * @param head the head of the original list (may be null)
 * @return the new head of the reversed list (null if the input was null)
 */
public ListNode reverseList(ListNode head) {
    // 'previous' will become the new head once the loop finishes.
    ListNode previous = null;

    // Walk through the list node by node.
    ListNode current = head;

    while (current != null) {
        // Preserve the rest of the list before we overwrite current.next.
        ListNode nextNode = current.next;   // O(1) read

        // Reverse the link: current now points backwards.
        current.next = previous;            // O(1) write

        // Advance the two pointers forward in the original order.
        previous = current;                 // previous now references the node we just processed
        current = nextNode;                 // move to the saved successor
    }

    // At termination, 'previous' points to the new head.
    return previous;
}
```

---

## 6. Quick Checklist for Reviewers  

- [x] **Invariant maintained**: all nodes before `current` are reversed.  
- [x] **No loss of reference**: `nextNode` saved before pointer overwrite.  
- [x] **Handles empty & single‑node lists** gracefully.  
- [x] **Space‑optimal**: only three local references.  
- [x] **Time‑optimal**: linear scan, constant per‑node work.  
- [ ] **Cycle detection** – not required by problem spec, but could be added if input validation is desired.  

---

### Bottom Line  

The implementation showcases a textbook in‑place iterative reversal, achieving the theoretical lower bounds for this problem (O(N) time, O(1) extra space) while keeping the code simple, safe, and highly performant for production‑scale linked list manipulations.

---
