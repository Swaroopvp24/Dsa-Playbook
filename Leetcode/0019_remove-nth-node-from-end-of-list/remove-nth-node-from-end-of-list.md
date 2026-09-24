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
