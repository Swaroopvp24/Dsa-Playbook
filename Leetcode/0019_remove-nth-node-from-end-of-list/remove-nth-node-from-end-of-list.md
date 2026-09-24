# remove-nth-node-from-end-of-list

## linkedlist_solution.java
*Style: detailed*

## 🚀  Solution Overview  

The implementation solves **“Remove N‑th Node From End of List”** (LeetCode 19) with a **two‑pass linear scan**:

1. **First pass** – walk the list once to compute its length `L`.  
2. **Second pass** – walk again to the node *just before* the one that must be deleted (`L‑n‑th` from the front) and splice it out.

The algorithm is deterministic, uses **O(1) auxiliary space**, and works for any valid `1 ≤ n ≤ L`.  

---

## 📊  Complexity Analysis  

| Phase | Operations | Cost per operation | #iterations | Total |
|------|------------|-------------------|------------|-------|
| **Pass 1** (size count) | `currentNode = currentNode.next` | pointer move, constant work | `L` (list length) | **O(L)** |
| **Pass 2** (reach predecessor) | `previousNode = previousNode.next` | pointer move, constant work | `L‑n‑1` (or `L‑n` depending on 1‑based loop) | **O(L)** |
| **Splice** | `previousNode.next = previousNode.next.next` | constant | 1 | **O(1)** |
| **Overall** | – | – | – | **Time = O(L)** |

*Space*: Only a handful of local references (`currentNode`, `previousNode`, `listSize`, loop counter). No recursion, no auxiliary containers.

**Why** `O(L)`?  
Every node is visited at most twice; there are no nested loops or data‑structure expansions that depend on `L`.

---

## 🔎  Component Deep‑Dive  

### 1. List Traversal & Length Computation  

```java
ListNode currentNode = head;
int listSize = 0;
while (currentNode != null) {
    currentNode = currentNode.next;
    listSize++;
}
```

* **Invariant**: At loop entry, `listSize` equals the number of nodes already processed; `currentNode` points to the *next* node to be counted.  
* **Termination**: When `currentNode` becomes `null`, exactly `listSize` nodes have been counted → `listSize = L`.

### 2. Head‑Removal Shortcut  

```java
if (n == listSize) {
    return head.next;
}
```

* Handles the edge case where the node to delete is the **first** element.  
* Returns the new head (`null` if the list consisted of a single node).  

### 3. Locating the Predecessor  

```java
ListNode previousNode = head;
for (int position = 1; position < listSize - n; position++) {
    previousNode = previousNode.next;
}
```

* The loop is **1‑based**: `position` starts at `1` because `previousNode` is already at the first node (`head`).  
* Loop condition `position < listSize - n` stops when `position == listSize - n`.  
  * After the loop, `previousNode` points to the **node preceding** the target (index `L‑n‑1` from the front, 0‑based).  

**Why the off‑by‑one works**:  

| Desired target index (0‑based) | Predecessor index |
|--------------------------------|-------------------|
| `L‑n` (node to delete)         | `L‑n‑1`            |

Since we start at index 0 (`head`) and advance `listSize‑n‑1` steps, the loop counter must run `listSize‑n‑1` times → `position` runs from `1` up to `< listSize‑n`.

### 4. Splicing Out the Target  

```java
previousNode.next = previousNode.next.next;
```

* `previousNode.next` is the node to delete (guaranteed non‑null because `n < listSize`).  
* Re‑assigning the `next` pointer bypasses the target, letting the GC reclaim it.  

### 5. Return Value  

```java
return head;
```

* The head never changes in this branch (only the *head‑removal* case returns `head.next`).  

---

## 💡  Key Insights & Gotchas  

| Aspect | Detail |
|--------|--------|
| **Two‑pass vs. one‑pass** | The classic “fast‑slow pointer” (move fast `n` steps, then move both until fast hits tail) reduces the number of traversals to **one** but adds a pointer and a more subtle termination condition. The two‑pass version is easier to reason about and less error‑prone for beginners. |
| **Edge‑case: single‑node list** | When `L = 1` and `n = 1`, `n == listSize` triggers the head‑removal clause, returning `null`. This is correct but must be explicitly covered; otherwise `previousNode.next` would be a null‑dereference. |
| **Invalid `n` (n > L)** | The code assumes valid input per problem constraints. If `n > L`, `listSize - n` becomes negative, the `for` loop never runs, and `previousNode.next` would be `null` → `NullPointerException`. Defensive production code should guard against this. |
| **Zero‑based vs. one‑based confusion** | The loop uses a **one‑based** counter (`position = 1`). Mixing 0‑based indexing would require `for (int i = 0; i < listSize - n - 1; i++)`. Maintaining a clear invariant eliminates off‑by‑one bugs. |
| **Memory‑leak considerations** | The removed node remains reachable only via `previousNode.next` before reassignment. After the splice, there is no external reference to it, so Java’s GC will reclaim it—no manual `null` assignment needed. |
| **Potential micro‑optimizations** | • Collapse the two traversals into a single loop that keeps a trailing pointer lagging `n` steps behind – this saves one pass of pointer dereferencing. <br>• Use `int` for `listSize`; overflow is impossible because the maximum list length fits in an `int` (LeetCode constraints). |
| **Readability tip** | Extract the length‑computation into a helper (`int size(ListNode head)`) and the predecessor‑locating logic into a method (`ListNode getPrev(ListNode head, int steps)`). This isolates concerns and makes unit testing easier. |
| **Thread‑safety** | The method mutates the input list in‑place. If the list were shared across threads, external synchronization would be required; the algorithm itself is not re‑entrant. |

---

## 📐  Alternate One‑Pass Implementation (for reference)

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0, head);   // sentinel simplifies head removal
    ListNode fast = dummy, slow = dummy;

    // Advance fast n+1 steps so that slow ends up on predecessor
    for (int i = 0; i <= n; i++) fast = fast.next;

    while (fast != null) {
        fast = fast.next;
        slow = slow.next;
    }
    // splice
    slow.next = slow.next.next;
    return dummy.next;
}
```

* Same `O(L)` time, `O(1)` space, but only **one** traversal and no explicit length variable.

---

### TL;DR  

*Two‑pass linear scan → compute length → locate predecessor → splice.*  
Time **O(L)**, space **O(1)**.  
Works for all valid inputs; head‑removal handled specially; careful index arithmetic avoids off‑by‑one bugs. For production‑grade code, consider a one‑pass fast‑slow pointer version and defensive checks for `n > L`.

---

## linkedlist_solution(slowfast_pointers).java
*Style: detailed*

## 📘 Deep‑Dive Reference – “Remove N‑th Node From End of a Singly‑Linked List”

> **Problem** – Given the head of a singly‑linked list, delete the *n‑th* node from the end of the list and return the (possibly new) head.

The solution below is the classic **two‑pointer (fast/slow) “one‑pass” algorithm** that works in **O(L)** time and **O(1)** auxiliary space, where *L* is the length of the list.

```java
/**
 * Definition for singly‑linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // Dummy node makes it easier to handle removing the head.
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;

        ListNode fastPointer = dummyNode;
        ListNode slowPointer = dummyNode;

        // Move fastPointer n nodes ahead.
        for (int i = 0; i < n; i++) {
            fastPointer = fastPointer.next;
        }

        // Move both pointers until fastPointer reaches the last node.
        // slowPointer will then be immediately before the node to remove.
        while (fastPointer.next != null) {
            fastPointer = fastPointer.next;
            slowPointer = slowPointer.next;
        }

        // Remove the nth node from the end.
        slowPointer.next = slowPointer.next.next;

        return dummyNode.next;
    }
}
```

---

## 1️⃣ Summary – High‑Level Overview  

| Aspect | Description |
|--------|-------------|
| **Algorithmic technique** | *Two‑pointer sliding window* (also called “fast‑slow” or “runner” technique). |
| **Core idea** | Advance a *fast* pointer `n` steps ahead of a *slow* pointer. Then walk both pointers together until `fast` reaches the list’s tail. At that moment `slow` sits **just before** the node that must be removed. |
| **Why it works** | The distance between `fast` and `slow` is kept constant (`n`). When `fast` is at the last node (index `L‑1`), `slow` is at index `L‑n‑1`, i.e. the predecessor of the target node (`L‑n`). Deleting `slow.next` removes the desired node. |
| **Special‑case handling** | A *dummy* sentinel node is inserted before the real head. This guarantees that even when the head itself must be removed (`n == L`) we still have a non‑null predecessor (`dummy`). The method returns `dummy.next`, which automatically yields the new head. |

---

## 2️⃣ Complexity Analysis  

### Time Complexity  

| Step | Operation | Cost |
|------|-----------|------|
| **Dummy creation** | Constant‑time object allocation | `O(1)` |
| **Advance `fast` `n` steps** | Loop runs exactly `n` times | `O(n)` |
| **Traverse the remaining list** | Both pointers move together until `fast.next == null`. This visits each *remaining* node once, i.e. `L - n` iterations. | `O(L - n)` |
| **Deletion (pointer rewrite)** | Constant work | `O(1)` |
| **Total** | `O(n + (L‑n)) = O(L)` | **Linear in list length** |

> **Why it’s linear:** The algorithm never revisits a node. Each node is touched at most twice (once by `fast` while advancing the initial window, once by the joint walk). No nested loops or recursion are involved.

### Space Complexity  

| Component | Memory usage |
|-----------|--------------|
| **Input list** | Provided by caller; not counted. |
| **Auxiliary variables** (`dummyNode`, `fastPointer`, `slowPointer`, loop counters) | Fixed number of `ListNode` references and primitive ints. |
| **Recursion stack** | None (iterative). |
| **Total** | **O(1)** – constant extra space independent of `L`. |

---

## 3️⃣ Component Deep Dive  

### 3.1 `ListNode dummyNode = new ListNode(0);`  

* **Purpose:** Guarantees a *predecessor* for every possible deletion, including the head.  
* **Implementation nuance:** The dummy’s `val` is irrelevant; only `next` matters.  
* **Effect on return value:** `return dummyNode.next;` automatically yields the new head after removal, handling the case where `head` itself is removed.

### 3.2 Fast / Slow Pointer Initialization  

```java
ListNode fastPointer = dummyNode;
ListNode slowPointer = dummyNode;
```

* Both start at the dummy to keep the distance `n` **between** them *after* the first loop.
* Using the same node for both pointers simplifies the logic; no need for separate “prev” handling later.

### 3.3 Advancing `fastPointer` `n` Steps  

```java
for (int i = 0; i < n; i++) {
    fastPointer = fastPointer.next;
}
```

* **Assumption:** `n` is valid (`1 ≤ n ≤ L`). The loop does **not** check for `fastPointer == null`.  
* **Potential failure mode:** If `n` > `L`, a `NullPointerException` occurs. In production code you’d guard against it (e.g., compute length first or validate `n`).  
* **Why not `fastPointer = fastPointer.next.next;`?** The loop preserves O(1) per iteration and works for any `n`, even `n == L`.

### 3.4 Joint Traversal  

```java
while (fastPointer.next != null) {
    fastPointer = fastPointer.next;
    slowPointer = slowPointer.next;
}
```

* **Loop invariant:** The distance `fastPointer` – `slowPointer` equals `n`.  
* **Termination condition:** `fastPointer.next == null` ⇨ `fastPointer` points to the *last* node. Consequently `slowPointer` points to the node *just before* the one to delete.  
* **Edge case:** If `n == L`, after the first loop `fastPointer` points to the **real head**, and the while‑loop does **zero** iterations, leaving `slowPointer` still at `dummy`. Deleting `dummy.next` removes the original head correctly.

### 3.5 Deleting the Target Node  

```java
slowPointer.next = slowPointer.next.next;
```

* **Atomic operation**: Re‑links the predecessor to the successor, dropping the target node from the chain.  
* **Garbage collection**: The orphaned node becomes eligible for GC (no extra manual `null` needed).  
* **Safety**: Because `slowPointer` is guaranteed to have a non‑null `next` (the node to delete) and that node’s `next` can be `null` (if deleting the tail), the assignment works for all positions.

### 3.6 Return Value  

```java
return dummyNode.next;
```

* If the original head was removed, `dummyNode.next` now points to the *second* node, correctly representing the new head.  
* If no removal (should never happen with correct inputs), the original head is returned unchanged.

---

## 4️⃣ Key Insights & Gotchas  

| Insight | Detail |
|---------|--------|
| **Dummy node eliminates head‑removal special case** | Without a dummy you’d need an `if (head == nodeToDelete) …` branch. The dummy unifies the logic. |
| **Fast pointer is advanced *exactly* `n` steps, not `n‑1`** | Off‑by‑one errors are common here. Advancing `n` positions ensures the distance between `fast` and `slow` is `n`, so when `fast` is at the *last* node, `slow` sits *before* the target. |
| **Loop condition uses `fastPointer.next != null`** | This stops **one node before** `null`. If you used `fastPointer != null`, `slowPointer` would end up *at* the node to delete rather than its predecessor, requiring a different deletion strategy. |
| **Potential N > length bug** | The provided code **assumes** valid input. In a defensive implementation you could: <br>1. Compute length first and validate `n`. <br>2. Add a guard inside the for‑loop: `if (fastPointer == null) throw new IllegalArgumentException("n exceeds list length");`. |
| **Handling a single‑node list** | When `head` has one node and `n == 1`: <br>1. `dummy -> node`. <br>2. Fast moves to `node`. <br>3. While loop does **zero** iterations (fast.next == null). <br>4. `slow` is still `dummy`; `slow.next = slow.next.next` sets `dummy.next = null`. <br>5. Return `dummy.next` → `null`, correctly representing an empty list. |
| **Memory locality** | The algorithm touches each node exactly once, leading to **optimal cache behavior** for linked lists (no random jumps). |
| **Thread safety** | The method mutates the list in‑place. If the list is shared across threads, external synchronization is required. |
| **Alternative “two‑pass” solution** | One could first compute length `L`, then delete the `(L‑n+1)`‑th node. That uses the same O(1) space but requires two traversals → `O(2L) = O(L)` time with a larger constant factor. The one‑pass method is therefore preferable in latency‑critical code. |
| **Extensibility** | The same pattern works for many “k‑th from end” problems (e.g., return kth node, reverse sub‑list from kth to end, etc.) by adjusting where you stop the joint walk. |
| **Testing checklist** | 1. `n == 1` (remove tail). <br>2. `n == length` (remove head). <br>3. `n == length/2` (mid‑list). <br>4. List length = 1, `n == 1`. <br>5. Very long list (stress for performance). <br>6. Invalid `n` (greater than length) – expect graceful error. |
| **Potential micro‑optimizations** | • Unroll the first `for` loop if `n` is known to be small (e.g., constant bound). <br>• Use `fastPointer = fastPointer.next;` inline with null‑check for safety. However, these rarely affect overall runtime for typical input sizes. |

---

## 5️⃣ Full Annotated Code (for reference)

```java
/**
 * Removes the n‑th node from the end of a singly‑linked list.
 *
 * Time   : O(L)  – one linear scan.
 * Space  : O(1)  – only a few pointer variables.
 *
 * Preconditions:
 *   1 ≤ n ≤ length(head)
 *
 * @param head the head of the list (may be null)
 * @param n    1‑based index from the end
 * @return the head of the list after removal
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // -----------------------------------------------------------------
        // 1️⃣  Dummy sentinel – guarantees a predecessor for every node.
        // -----------------------------------------------------------------
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;

        // Both pointers start at the dummy.
        ListNode fastPointer = dummyNode;
        ListNode slowPointer = dummyNode;

        // -----------------------------------------------------------------
        // 2️⃣  Advance fastPointer n steps ahead.
        // -----------------------------------------------------------------
        for (int i = 0; i < n; i++) {
            // Assumes n is valid; otherwise fastPointer may become null.
            fastPointer = fastPointer.next;
        }

        // -----------------------------------------------------------------
        // 3️⃣  Move both pointers until fastPointer reaches the last node.
        // -----------------------------------------------------------------
        while (fastPointer.next != null) {
            fastPointer = fastPointer.next;
            slowPointer = slowPointer.next;
        }

        // -----------------------------------------------------------------
        // 4️⃣  Delete the target node (slowPointer.next).
        // -----------------------------------------------------------------
        slowPointer.next = slowPointer.next.next; // safe: slowPointer.next != null

        // -----------------------------------------------------------------
        // 5️⃣  Return the (potentially new) head.
        // -----------------------------------------------------------------
        return dummyNode.next;
    }
}
```

---

### TL;DR  

* **One‑pass two‑pointer technique** eliminates the need for a length pass.  
* **Dummy node** simplifies head‑removal and yields a clean `O(1)` space solution.  
* Correctness hinges on maintaining a **fixed distance `n`** between `fast` and `slow`.  
* Edge cases (single node, removing head/tail) are handled automatically; only **invalid `n`** needs extra guarding.  

Feel free to copy‑paste the annotated version into your codebase, add the defensive checks you need, and extend the pattern for related “k‑th from end” linked‑list problems. Happy coding!

---
