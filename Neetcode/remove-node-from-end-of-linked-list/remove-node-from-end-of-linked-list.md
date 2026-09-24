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

## linkedlist_solution(slowfast_pointers).java
*Style: detailed*

# 📘 Deep‑Dive Reference – “Remove N‑th Node From End of List”

> **Problem** – Given a singly‑linked list `head` and an integer `n`, delete the *n‑th* node from the end of the list and return the new head.

The implementation below solves the problem with the classic **two‑pointer (fast/slow) technique** and a **dummy sentinel node** to unify head‑removal logic.

```java
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

## 1️⃣ Summary – Core Idea & Algorithmic Technique

| Step | What happens | Why it works |
|------|--------------|--------------|
| **Dummy sentinel** | `dummyNode → head` | Guarantees a non‑null predecessor for *every* real node, including the original head. Removes the need for a special‑case branch when deleting the first node. |
| **Fast pointer advance** | Move `fastPointer` exactly `n` steps forward from `dummyNode`. | After this, the distance between `fastPointer` and `slowPointer` equals `n`. |
| **Synchronous walk** | Increment both pointers until `fastPointer.next == null`. | When `fastPointer` is at the **last** node, `slowPointer` is positioned *just before* the node that is `n` positions from the tail (i.e., the target for removal). |
| **Splice out** | `slowPointer.next = slowPointer.next.next`. | Directly bypasses the target node, achieving O(1) deletion in a singly‑linked list. |
| **Return** | `dummyNode.next` (the possibly new head). | The dummy node hides whether the head changed or not. |

The algorithm is a textbook **single‑pass** solution: it traverses the list only once (the for‑loop + while‑loop combined) while using O(1) extra space.

---

## 2️⃣ Complexity Analysis

| Metric | Derivation |
|--------|------------|
| **Time** | Let `L = length(head)`. <br> *Advance fast*: `n` steps → `O(n)`. <br> *Synchronous walk*: from the `(n+1)`‑th node to the tail → `O(L‑n)`. <br> Total = `O(n + (L‑n)) = O(L)`. |
| **Space** | Only a handful of local references (`dummyNode`, `fastPointer`, `slowPointer`) → **O(1)** auxiliary memory. The input list itself is not duplicated. |
| **Best‑case / Worst‑case** | Both are `Θ(L)` because regardless of `n` we must walk to the end to locate the predecessor. |

*Why not `O(L·n)`?* The two pointers share the same traversal; the first loop does **not** restart the list – it merely offsets the fast pointer.

---

## 3️⃣ Component Deep Dive

### 3.1 Dummy Sentinel (`dummyNode`)

```java
ListNode dummyNode = new ListNode(0);
dummyNode.next = head;
```

*Purpose*:  
- Guarantees that `slowPointer` always has a valid `next` to splice (`slowPointer.next` is never `null` when we reach the deletion line).  
- Eliminates the “if head needs removal” conditional.

*Implementation nuance*:  
- The dummy’s value (`0`) is irrelevant; only the `next` link matters.  
- Returning `dummyNode.next` yields the correct head even if the original head is removed.

### 3.2 Fast/Slow Pointers

```java
ListNode fastPointer = dummyNode;
ListNode slowPointer = dummyNode;
```

Both start at the dummy. The invariant maintained during the synchronous walk:

```
distance(fastPointer, slowPointer) == n
```

When `fastPointer.next == null` (i.e., fastPointer is the *last* node), the invariant ensures:

```
slowPointer points to node preceding the n‑th from end
```

### 3.3 Advancing `fastPointer` `n` Steps

```java
for (int i = 0; i < n; i++) {
    fastPointer = fastPointer.next;
}
```

*Assumptions*: `n` is guaranteed to be ≤ `L`. The loop does **not** check for `null` because LeetCode’s contract promises a valid `n`. In a production setting you’d guard against `null` to avoid `NullPointerException`.

### 3.4 Synchronous Walk

```java
while (fastPointer.next != null) {
    fastPointer = fastPointer.next;
    slowPointer = slowPointer.next;
}
```

*Key point*: The condition uses `fastPointer.next` rather than `fastPointer`. This ensures that when the loop terminates, `fastPointer` is positioned **at** the last node, not beyond it. Consequently `slowPointer` stops exactly one node *before* the deletion target.

### 3.5 Deleting the Target Node

```java
slowPointer.next = slowPointer.next.next;
```

- The statement works because `slowPointer.next` is guaranteed non‑null (the node to delete exists).  
- No explicit memory reclamation is needed; Java’s GC will clean up the orphaned node.

### 3.6 Edge‑Case Handling

| Edge case | How it’s covered |
|-----------|------------------|
| **Remove head** (`n == L`) | `fastPointer` ends up `n` steps ahead of dummy, so after the walk `slowPointer` stays at dummy → `slowPointer.next` points to the original head, which gets spliced out. |
| **Single‑node list** (`L == 1, n == 1`) | Dummy → node → `null`. After advancement, `fastPointer` points to the node. While loop never executes (since `fastPointer.next == null`). `slowPointer` stays at dummy; splice makes `dummy.next = null`. Returns `null`. |
| **`head == null`** (invalid per problem) | The `for` loop would throw `NullPointerException` on the first iteration. In a defensive version you’d early‑return `null` or raise an IllegalArgumentException. |
| **`n > L`** (invalid) | Same NPE as above; a production‑grade implementation would validate and possibly throw `IllegalArgumentException`. |

---

## 4️⃣ Key Insights & Gotchas

| Insight | Explanation |
|---------|-------------|
| **Dummy node eliminates head‑removal branching** | Without a dummy you would need `if (slowPointer == null) head = head.next;` after traversal. The sentinel makes the code uniform. |
| **Choosing `fastPointer.next != null` vs `fastPointer != null`** | Using `.next` ensures `slowPointer` ends *before* the target, not *at* the target. If we used `while (fastPointer != null)`, `slowPointer` would land on the node to delete, requiring a different splice (`slowPointer = slowPointer.next`). |
| **Single‑pass guarantee** | The two loops are logically a single linear scan; no extra traversal of the list is needed. |
| **Space‑optimal** | Only three extra references; no auxiliary data structures (arrays, stacks) are allocated. |
| **Potential NPE when `n` > length** | The for‑loop does not guard against `fastPointer` becoming `null`. In interview settings you can mention that you’d add a guard or pre‑count the list length if the contract isn’t strict. |
| **Integer overflow not a concern** | `n` and list length are bounded by `int` range in typical LeetCode constraints; the loop counters are safe. |
| **Thread‑safety** | The method mutates the input list in‑place. If the list were shared across threads, external synchronization would be required – not a concern for the isolated problem. |
| **Garbage collection** | Java automatically reclaims the removed node; no manual `delete` needed (as in C/C++). |
| **Alternative formulations** | You could also perform a *two‑pass* solution (first count nodes, then delete), which uses O(L) time but also O(1) space, but requires two scans. The presented one‑pass method is generally preferred for interview performance. |
| **Testing corner cases** | Recommended unit‑test matrix: <br>1. `[1,2,3,4,5] , n=2 → [1,2,3,5]` <br>2. `[1] , n=1 → []` <br>3. `[1,2] , n=2 → [2]` <br>4. `[1,2] , n=1 → [1]` <br>5. `null , n=1 → IllegalArgumentException` (if defensive). |

---

## 5️⃣ Quick Reference Implementation (Defensive Variant)

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    if (head == null) {
        throw new IllegalArgumentException("List cannot be null");
    }

    ListNode dummy = new ListNode(0, head);
    ListNode fast = dummy, slow = dummy;

    // Advance fast n steps; validate n.
    for (int i = 0; i < n; i++) {
        if (fast.next == null) {
            throw new IllegalArgumentException("n exceeds list length");
        }
        fast = fast.next;
    }

    // Walk until fast is at the last node.
    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }

    // Delete.
    slow.next = slow.next.next;
    return dummy.next;
}
```

*The extra checks turn the algorithm from “LeetCode‑assumed‑valid” to production‑ready.*

---

### TL;DR

- **Technique**: One‑pass two‑pointer (fast/slow) with dummy sentinel.
- **Time**: `O(L)` – single linear scan.
- **Space**: `O(1)` – only constant extra references.
- **Robustness**: Dummy node simplifies head removal; ensure `n` is validated if the contract is not guaranteed.

This should serve as a concise yet exhaustive reference for anyone reviewing, teaching, or extending the solution.

---
