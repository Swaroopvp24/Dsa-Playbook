# reverse-linked-list-ii

## linkedlist_solution.java
*Style: detailed*

# 📚 Deep‑Dive Reference: `reverseBetween` (LeetCode 92)  

*Language:* Java 17 (compatible with earlier versions)  

*Goal:* In‑place reversal of a singly‑linked sub‑list bounded by the **1‑based** indices `left` and `right`.  

---  

## 1️⃣ Summary – Core Idea & Algorithmic Technique  

The solution implements the classic **in‑place sub‑list reversal** using a **dummy sentinel** and **pointer rewiring**:

1. **Sentinel (dummy) node** – Guarantees a uniform handling of the head‑segment (`left == 1`).  
2. **Locate three pivot nodes** in a single forward scan up to `right`:  
   - `leftPrev` – node *just before* the segment to reverse.  
   - `leftNode` – first node of the segment (the one that will become the tail after reversal).  
   - `rightNode` – last node of the segment (the one that will become the head after reversal).  
3. **Reverse** the segment `[leftNode … rightNode]` by iterating from `leftNode` toward `rightNode.next`, **pre‑pending** each visited node onto a new list whose initial head is `rightNode.next` (`nodeAfter`).  
4. **Reconnect** the reversed chunk: `leftPrev.next = rightNode`. The original `leftNode` is already linked to `nodeAfter` by the reversal loop, so the list is fully stitched together.  

The algorithm is a **single‑pass, O(1) extra‑space** in‑place transformation – the canonical textbook approach for this problem.

---  

## 2️⃣ Complexity Analysis  

| Metric | Reasoning |
|--------|-----------|
| **Time** | **O(N)** where `N` = `list length`. <br> • The `for` loop traverses up to `right` nodes ( ≤ N ). <br> • The `while` loop traverses the sub‑list once ( `right‑left+1` ≤ N ). <br> • No nested loops or repeated traversals ⇒ linear. |
| **Space** | **O(1)** auxiliary. <br> • Only a handful of `ListNode` references (`dummy`, `leftPrev`, `leftNode`, `rightNode`, `nodeAfter`, `previous`, `currentNode`, `nextNode`). <br> • No collections, recursion, or extra nodes are allocated. |
| **Auxiliary Stack** | None – iterative only. |

**Why not O(N log N) or O(N²)?**  
All pointer manipulations are constant‑time; each node is visited at most twice (once during locating, once during reversal). No data is copied, and the dummy node is a single extra object.

---  

## 3️⃣ Component Deep Dive  

### 3.1 Class & Method Signature  

```java
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) { … }
}
```

*`ListNode`* – standard singly‑linked node (`int val`, `ListNode next`).  
*Indices* – 1‑based per problem statement (i.e., `left == 1` refers to the real head).

### 3.2 Guard Clauses  

```java
if (head == null || left == right) return head;
```

- `head == null` → empty list, nothing to do.  
- `left == right` → segment length 1, reversal is a no‑op.  

Both return early, avoiding unnecessary dummy creation.

### 3.3 Dummy Sentinel  

```java
ListNode dummy = new ListNode(0);
dummy.next = head;
```

- Guarantees a **predecessor** for the sub‑list even when `left == 1`.  
- The final answer is `dummy.next`, which automatically adapts to head changes.

### 3.4 Locating Pivot Nodes (single forward pass)

```java
ListNode current = dummy;
ListNode leftPrev = null, leftNode = null, rightNode = null;

for (int position = 0; position <= right; position++) {
    if (position == left - 1) leftPrev = current;
    if (position == left)     leftNode = current;
    if (position == right)    rightNode = current;
    current = current.next;
}
```

| Variable | Meaning | Invariant at loop end |
|----------|---------|-----------------------|
| `leftPrev` | Node *before* the start of the reversal range (`left-1`). | Points to node at position `left-1` (or dummy when `left == 1`). |
| `leftNode` | Node at position `left`. | The **first** node to be reversed; becomes the tail after reversal. |
| `rightNode`| Node at position `right`. | The **last** node to be reversed; becomes the head after reversal. |
| `current`  | One step past `rightNode` (i.e., `rightNode.next`). | Holds `nodeAfter`. |

**Why the loop runs to `position <= right`?**  
We need to capture `rightNode` *before* we advance past it, therefore the body processes the node at index `right`, then moves `current` forward. The extra iteration (position `right+1`) is unnecessary; stopping at `right` suffices because we already have `nodeAfter = rightNode.next` later.

### 3.5 Reversal Core  

```java
ListNode nodeAfter = rightNode.next; // may be null (right == length)
ListNode previous = nodeAfter;       // anchor for the reversed sub‑list
ListNode currentNode = leftNode;

while (currentNode != nodeAfter) {
    ListNode nextNode = currentNode.next; // store forward link
    currentNode.next = previous;          // reverse link
    previous = currentNode;               // advance 'previous' pointer
    currentNode = nextNode;               // advance traversal
}
```

- **Invariant** (maintained at loop top):  
  - `previous` points to the **head of the partially reversed** sub‑list.  
  - All nodes *before* `currentNode` (i.e., those already processed) now point **forward** to `previous`.  
  - `currentNode` is the next node to reverse, whose original `next` is saved in `nextNode`.  

- **Termination**: When `currentNode == nodeAfter`, the sub‑list `[leftNode … rightNode]` is fully reversed, and `previous` equals `rightNode` (new head of the segment).  

- **Handling `nodeAfter == null`**: `previous` is initialized to `null`. The loop still works; the final tail (`leftNode`) ends with `next = null`, correctly truncating the list.

### 3.6 Stitching Back  

```java
leftPrev.next = rightNode; // rightNode == previous
return dummy.next;
```

- `rightNode` is now the head of the reversed chunk (thanks to the reversal loop).  
- `leftPrev.next` bypasses the original `leftNode` and connects to the new head.  
- The original `leftNode` already points to `nodeAfter` (set during reversal), completing the join.  

**No extra assignment needed for `leftNode.next`** – the loop already set it.

### 3.7 Edge‑Case Handling  

| Scenario | How the code behaves |
|----------|----------------------|
| **`left == 1`** | `leftPrev` becomes `dummy`; after stitching, `dummy.next` (new head) points to `rightNode`. |
| **`right == length`** | `nodeAfter` = `null`; reversal loop terminates when `currentNode` becomes `null`. Tail (`leftNode`) ends with `next = null`. |
| **`left == right`** | Guard clause returns early – O(1) work, no dummy needed. |
| **Empty list (`head == null`)** | Guard clause returns early. |
| **Invalid indices** (e.g., `left > right`, or out of bounds) | **Not checked** – contract assumes valid inputs (as per LeetCode). If required, additional validation can be added with O(N) overhead. |

---  

## 4️⃣ Key Insights & Gotchas  

### 4.1 Reversal “anchor” trick  
Initializing `previous` to `nodeAfter` (the node *after* the segment) is the elegant part: after the loop, the reversed segment is already linked to the rest of the list, eliminating a separate `rightNode.next = nodeAfter` step.

### 4.2 Dummy node eliminates head‑special‑case logic  
Without the dummy, you would need an `if (left == 1)` branch to reassign the external head pointer. The sentinel abstracts that away, making the final `return dummy.next` universal.

### 4.3 Single‑pass node discovery  
All three pivot nodes are obtained in **one** forward traversal (`O(right)`). A naïve two‑pass approach (first find `leftPrev`, then find `rightNode`) would double the traversal overhead for large `right`.

### 4.4 Sub‑list length vs. total length  
The algorithm’s runtime is **linear in `N`**, not in `right‑left`. Even if `right` is near the end of a huge list, we still walk the entire prefix once. This is optimal because any algorithm must at least read up to `right` to locate the segment.

### 4.5 Subtle bug that *could* appear  

| Potential bug | Why it matters | Fix (if it existed) |
|---------------|----------------|--------------------|
| **Using `leftNode = current.next` instead of `leftNode = current`** | Would set `leftNode` to the node *after* the intended start, shifting the reversal window and corrupting the list. | Keep `leftNode = current` at the moment `position == left`. |
| **Iterating `for (int i = 1; i < left; i++)` without dummy** | When `left == 1`, the loop would never set `leftPrev`, leading to a `NullPointerException` when reconnecting. | Either add dummy or treat `left == 1` as a special case. |
| **Reversal loop condition `while (currentNode != null)`** | Would overshoot beyond `rightNode` and reverse the entire suffix, because `nodeAfter` may be `null`. | Use `while (currentNode != nodeAfter)`. |
| **Forgot to advance `current` inside the locating loop** | Infinite loop, or `null` dereference. | Ensure `current = current.next` each iteration (as in the code). |

### 4.6 Space‑efficiency nuance  
The algorithm does *not* allocate any new `ListNode` objects. The only heap allocation is the dummy sentinel (`new ListNode(0)`), which is constant‑size regardless of `N`. This satisfies the strict O(1) auxiliary space requirement.

### 4.7 Possible micro‑optimizations  

| Optimization | Effect | Trade‑off |
|--------------|--------|-----------|
| **Reuse `dummy` as `leftPrev` when `left == 1`** | Eliminates one assignment (`leftPrev = dummy`). | Negligible; readability is higher with explicit variable. |
| **Combine locating loop with early break when `position == right`** | Stops scanning after `right`, already done. | Already O(right). |
| **Avoid storing `rightNode.next` in `nodeAfter` and use `rightNode.next` directly** | One less local variable. | Slightly reduces readability; `nodeAfter` clarifies intent. |

---  

## 5️⃣ Reference Implementation (Annotated)

```java
/**
 * Reverses the

---
