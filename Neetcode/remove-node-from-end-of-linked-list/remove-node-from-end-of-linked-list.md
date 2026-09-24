# remove-node-from-end-of-linked-list

## linkedlist_solution.java
*Style: detailed*

# Deep‑Dive Reference: `removeNthFromEnd` (Two‑Pass Length‑Based Solution)

> **Problem** – Given the head of a singly‑linked list, remove the *n*‑th node from the end and return the (possibly new) head.  
> **Solution style** – Classic **two‑pass** algorithm that first computes the total length of the list, then deletes the target node using a single forward walk.

---

## 1️⃣ Summary – High‑level Approach  

| Step | What happens | Why it works |
|------|--------------|--------------|
| **Pass 1 – length count** | Walk from `head` to `null`, incrementing `listSize`. | After this traversal `listSize` equals the exact number of nodes, so we can translate “*n*‑th from end” into an index from the **front** (`listSize - n`). |
| **Edge‑case head removal** | If `n == listSize` we return `head.next`. | When the node to delete is the first node, there is no “previous” node to patch; we simply drop the head. |
| **Pass 2 – locate predecessor** | Starting at `head`, advance `listSize - n - 1` steps to reach the node **just before** the one we need to cut out. | In a 0‑based front‑indexed list, the predecessor of index `listSize - n` is at index `listSize - n - 1`. |
| **Splice out the target** | `previousNode.next = previousNode.next.next;` | Re‑linking skips the unwanted node, allowing it to be garbage‑collected. |
| **Return** | Return the (original) `head` (or `head.next` if we removed the first node). | The list is now one element shorter, with the proper head reference. |

The algorithm runs **two linear scans** over the list but uses **O(1) extra memory**.

---

## 2️⃣ Complexity Analysis  

| Metric | Derivation |
|--------|------------|
| **Time** | <ul><li>First while‑loop traverses *all* `listSize` nodes ⇒ **O(N)**.</li><li>Second `for` loop traverses `listSize - n - 1` nodes (worst case `N‑1`) ⇒ **O(N)**.</li><li>Constant‑time operations elsewhere ⇒ total **O(N)**.</li></ul> |
| **Space** | Only a handful of primitive variables (`listSize`, `currentNode`, `previousNode`, loop counter) and a few references. No auxiliary data structures that grow with input size ⇒ **O(1)**. |
| **Auxiliary** | The recursion stack depth is zero (iterative only). |

*Why not O(N²)?* – Both loops are **sequential**, not nested. The first loop finishes before the second begins, so each node is visited at most twice, not repeatedly.

---

## 3️⃣ Component Deep Dive  

### 3.1 Data Structure: `ListNode`

```java
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val)               { this.val = val; }
    ListNode(int val, ListNode next){ this.val = val; this.next = next; }
}
```

*Only* two fields (`int val`, `ListNode next`). No sentinel/dummy node is provided, so head removal must be handled specially.

### 3.2 `removeNthFromEnd` – Line‑by‑line walkthrough  

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    // ---------- PASS 1: compute length ----------
    ListNode currentNode = head;
    int listSize = 0;

    while (currentNode != null) {
        currentNode = currentNode.next;
        listSize++;
    }
```

*Purpose*: Count nodes.  
*Edge Cases*:  
- `head == null` → loop never executes, `listSize == 0`. The later `if (n == listSize)` will compare `n` to `0`. For any positive `n` the function will eventually throw a NPE (see later).  

```java
    // ---------- HEAD removal ----------
    if (n == listSize) {
        return head.next;          // drop the first node
    }
```

*Why before the second pass?*  
- When `n` equals the total length, the node to delete **is** the head. Trying to locate a predecessor (`previousNode`) would be impossible (no node before head), so we short‑circuit.

*Potential pitfalls*:
- If `listSize == 0` and `n == 0` (illegal per problem statement), the method returns `head.next` → `null`. The contract typically assumes `n ≥ 1`.

```java
    // ---------- PASS 2: locate predecessor ----------
    ListNode previousNode = head;

    for (int position = 1; position < listSize - n; position++) {
        previousNode = previousNode.next;
    }
```

*Loop invariant*: After `k` iterations (`position == k+1`), `previousNode` points to the node at front index `k`.  
- The loop stops when `position == listSize - n`. Consequently `previousNode` ends up at index `listSize - n - 1` – exactly the predecessor of the target.

*Off‑by‑one sanity check* (example `listSize = 5`, `n = 2`):
- Target index from front = `5 - 2 = 3` (0‑based).  
- Loop runs while `position < 3` → `position = 1,2`. After two iterations, `previousNode` is at index `1` (`listSize - n - 1 = 2`).  
- `previousNode.next` is node index `2` (the node to delete). Works.

```java
    // ---------- Splice out ----------
    previousNode.next = previousNode.next.next;
    return head;
}
```

*Safety*: `previousNode.next` is guaranteed non‑null because:
- The head case (`n == listSize`) was already handled.
- `n` is assumed ≤ `listSize` (problem guarantee). If violated, a `NullPointerException` will be thrown here.

---

### 3.3 Edge‑Case Handling (Explicit & Implicit)

| Situation | How it is handled | Remarks |
|-----------|-------------------|---------|
| `n == listSize` (delete head) | `return head.next;` | Early exit. |
| `listSize == 1 && n == 1` | Same as head case → returns `null`. | Correctly yields empty list. |
| `n > listSize` | No guard → `previousNode.next` will be `null` → NPE. | The original LeetCode spec guarantees `1 ≤ n ≤ length`. In production code you’d add validation. |
| `head == null` | First loop never runs, `listSize = 0`. If `n == 0` returns `head.next` (NPE). Otherwise later NPE. | Again, spec guarantees non‑empty list. |
| `n == 0` (invalid) | Falls through to loop, `listSize - n == listSize`. Loop runs `listSize‑1` times, `previousNode` ends at last node, then `previousNode.next = previousNode.next.next` → NPE because `previousNode.next` is `null`. | Input validation needed for robust API. |

---

## 4️⃣ Key Insights & Pitfalls  

### 4.1 Why a **dummy** node simplifies the code  

Adding a sentinel before the real head (`ListNode dummy = new ListNode(0, head)`) eliminates the special‑case `if (n == listSize)`. The algorithm could then uniformly locate the predecessor (including when it’s the dummy) and always return `dummy.next`. This reduces branching and the risk of forgetting an edge case.

### 4.2 One‑Pass Alternative (Two‑Pointer / Fast‑Slow)

```java
ListNode dummy = new ListNode(0, head);
ListNode fast = dummy, slow = dummy;
for (int i = 0; i <= n; ++i) fast = fast.next;   // move n+1 steps
while (fast != null) { fast = fast.next; slow = slow.next; }
slow.next = slow.next.next;
return dummy.next;
```

- Same **O(N)** time, **O(1)** space.
- Only **one traversal**, which can be ~2× faster on very large lists due to reduced cache pressure.
- Avoids the need for an explicit length count.

**When to prefer two‑pass?**  
- Simplicity of reasoning for beginners.  
- When list length is already known (e.g., pre‑computed elsewhere).  

### 4.3 Integer overflow on `listSize`  

`listSize` is an `int`. The maximum length of a Java linked list is limited by available heap, but theoretically could exceed `Integer.MAX_VALUE`. In that pathological scenario `listSize++` would wrap to negative, breaking the algorithm. Using `long` would make it robust, but for any realistic interview problem `int` suffices.

### 4.4 Garbage collection considerations  

After `previousNode.next = previousNode.next.next;` the removed node loses all inbound references and becomes eligible for GC. No explicit `null` assignment to the removed node’s `next` field is required, but doing so (`removed.next = null`) can speed up reclamation for very large lists with many pending deletions (helps the GC identify isolated sub‑graphs earlier).

### 4.5 Subtle off‑by‑one bug source  

The loop condition `position < listSize - n` is easy to mis‑write as `<=`. Using `<=` would stop **one step early**, causing the predecessor to be the node *before* the real predecessor, leading to deleting the wrong node (or a NPE when deleting the last node). The chosen `position` start at `1` (instead of `0`) aligns the arithmetic nicely; any change must be mirrored in both the start value and the bound.

### 4.6 Defensive programming checklist  

- **Validate inputs**: `if (head == null || n <= 0) return head;`
- **Guard against `n > listSize`**: after counting, if `n > listSize` throw `IllegalArgumentException`.
- **Use a dummy node** to collapse the head‑removal branch.
- **Optional**: store `previousNode.next` in a temporary variable before re‑linking, then `temp.next = null;` for explicit dereference.

---

## 5️⃣ TL;DR – What to Remember  

| Aspect | Takeaway |
|--------|----------|
| **Algorithm** | Two‑pass length‑based deletion (first count nodes, second splice). |
| **Complexity** | `O(N)` time, `O(1)` auxiliary space. |
| **Critical line** | `previousNode.next = previousNode.next.next;` – requires a non‑null predecessor. |
| **Common bug** | Forgetting the head‑removal case or mis‑computing the loop bound (`position < listSize - n`). |
| **Optimization** | One‑pass fast‑slow pointers, or dummy sentinel to drop the early `if`. |
| **Robustness** | Add validation for `null` head, `n <= 0`, or `n > length`. |
| **Memory** | No extra containers; the removed node is automatically GC‑eligible. |

--- 

### Final Code (with defensive dummy node – production‑ready)

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // Defensive checks (optional, depending on contract)
        if (head == null || n <= 0) return head;

        // Dummy sentinel eliminates special‑case for head removal
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy, slow = dummy;

        // Move fast n+1 steps ahead (covers removal of head)
        for (int i = 0; i <= n; i++) {
            if (fast == null)               // n > length
                throw new IllegalArgumentException("n exceeds list length");
            fast = fast.next;
        }

        // Advance both pointers until fast reaches the end
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // Splice out the target node
        ListNode toDelete = slow.next;
        slow.next = toDelete.next;
        // Optional explicit deref for GC friendliness
        toDelete.next = null;

        return dummy.next;
    }
}
```

*The above version retains the O(N) / O(1) guarantees while being safer and easier to read.*

---
