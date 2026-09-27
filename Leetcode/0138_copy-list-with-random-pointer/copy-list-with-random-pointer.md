# copy-list-with-random-pointer

## linkedlist_solution(hashMap).java
*Style: detailed*

# Deep-Dive: Deep Copying Linked Lists with Random Pointers

## Summary
The provided solution addresses the problem of cloning a linked list where each node contains an additional `random` pointer that can point to any node in the list or `null`. This is a graph serialization problem where the challenge is maintaining object identity across a disconnected pointer structure.

The implementation utilizes an **Adjacency Mapping approach**. By using a `HashMap<Node, Node>`, the algorithm establishes a one-to-one correspondence between original nodes and their clones. This decouples the allocation of memory from the pointer assignment phase, effectively treating the linked list as a directed graph and performing a two-pass traversal to perform a deep copy.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Phase 1 (Creation):** A single linear scan is performed over the $N$ nodes to instantiate the clones and populate the `HashMap`. Each `Map.put` operation is $O(1)$ on average.
*   **Phase 2 (Linking):** A second linear scan iterates through the $N$ nodes. Each `Map.get` operation is $O(1)$ on average.
*   **Total:** $O(N) + O(N) = O(N)$, where $N$ is the number of nodes in the list.

### Space Complexity: $O(N)$
*   The `HashMap` stores exactly $N$ entries, each mapping an original `Node` reference to a new `Node` reference.
*   In terms of auxiliary heap space, the complexity is $O(N)$ because the cloned list itself consumes $O(N)$ space, and the map structure introduces a proportional overhead.

---

## Component Deep Dive

### 1. The Mapping Strategy (`HashMap<Node, Node>`)
The choice of `HashMap` is critical because it relies on the default `Object.hashCode()` and `Object.equals()` implementations. Since `Node` does not override these methods, the map performs identity-based lookups. This ensures that even if two nodes had the same `val`, they are treated as distinct entities, preventing pointer collisions.

### 2. Two-Pass Pointer Assignment
*   **Pass 1:** Focuses exclusively on node allocation. By populating the map completely before attempting to link pointers, we ensure that `originalToCopy.get(target)` will never return `null` for a non-null target, regardless of the order of the `random` pointers.
*   **Pass 2:** Focuses on pointer reconstruction. The expression `copiedNode.next = originalToCopy.get(current.next)` handles the trailing `null` case elegantly: if `current.next` is `null`, the map returns `null`, correctly terminating the cloned list.

---

## Key Insights

### Performance Optimization: The $O(1)$ Space Alternative
While this implementation is $O(N)$ in space, this problem can be solved in **$O(1)$ auxiliary space** (excluding the output list) by weaving the clone nodes directly into the original list structure:
1.  **Interweaving:** For each node `A`, create `A'` and set `A.next = A'`, and `A'.next = originalNext`.
2.  **Random Linking:** The random pointer for `A'` can be derived via `A'.random = A.random.next`.
3.  **Unweaving:** Restore the original list and extract the cloned list.

*Trade-off:* While the interweaving approach is more memory-efficient, it modifies the original list structure mid-process. The provided `HashMap` approach is **thread-safe for read-only access to the original list** and is generally considered more idiomatic for production code due to higher readability and the absence of side effects on the input object.

### Subtle Edge Cases
*   **Empty List (`head == null`):** The logic handles this implicitly. If `head` is `null`, the `while` loops are skipped, and `originalToCopy.get(null)` returns `null`, correctly returning an empty result.
*   **Cycles/Self-Referencing Random Pointers:** Because the `HashMap` approach resolves lookups based on object identity, self-referential `random` pointers (where `node.random == node`) are handled naturally without requiring special logic or recursion depth management.
*   **Garbage Collection:** Note that this solution holds a reference to every node in the original list within the `HashMap` until the function exits. In extremely large lists, this ensures the original nodes remain reachable in the heap, effectively doubling the memory pressure during execution.

---

## linkedlist_optimal_solution.java
*Style: detailed*

# Deep Dive: O(1) Space Complexity Deep Copy of Linked List with Random Pointers

## Summary
The problem requires duplicating a linked list where each node contains an additional `random` pointer that can point to any node in the list or `null`. The standard approach uses a hash map to maintain a mapping from original nodes to their copies, resulting in $O(N)$ space. 

This implementation utilizes an **Interweaving/Pointer Manipulation** technique. By interleaving copied nodes directly into the original list structure, we eliminate the need for auxiliary data structures (hash maps) to track node identity. This reduces the space complexity to $O(1)$ (excluding the output list) by temporarily leveraging the `next` pointers to establish the mapping.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Step 1 (Interleaving):** We traverse the list once, creating $N$ nodes. $O(N)$.
*   **Step 2 (Random Mapping):** We traverse the list once to wire the `random` pointers. $O(N)$.
*   **Step 3 (Separation):** We traverse the list once to restore original `next` pointers and extract the copy. $O(N)$.
*   Total operations scale linearly with the number of nodes $N$, resulting in $O(3N) \approx O(N)$.

### Space Complexity: $O(1)$
*   The algorithm operates **in-place** regarding auxiliary data structures. 
*   We use only a constant amount of pointer storage (`current`, `copiedNode`, `copiedHead`).
*   *Note:* The space used by the *new* list is required by the problem statement and is not considered auxiliary space in this context.

---

## Component Deep Dive

### 1. Interleaving (The "Shadow" List)
The core logic resides in inserting each `copiedNode` immediately after its corresponding `originalNode`. 
*   **Mechanism:** `A -> B` becomes `A -> A' -> B -> B'`.
*   **Effect:** This creates a deterministic, fixed-offset relationship. For any `originalNode`, its `copy` is always `originalNode.next`.

### 2. Random Pointer Resolution
Once the shadow list is established, resolving `random` pointers becomes a local operation:
*   Given `originalNode.random = Target`, the copy's random pointer must be the copy of `Target`.
*   Since `Target` is an original node, its copy is simply `Target.next`.
*   **Logic:** `current.next.random = current.random.next`.
*   **Edge Case:** The implementation correctly checks `current.random != null` to avoid `NullPointerException` when a node's random pointer points to `null`.

### 3. Separation (Pointer Restoration)
This step is critical for data integrity. We are "unzipping" the two lists:
*   `current.next = current.next.next` repairs the original list.
*   `copiedNode.next = copiedNode.next.next` links the copies together.
*   **Nuance:** The final `current` in the loop relies on the restoration of `current.next` to reach the next valid original node. Failure to restore the list pointers exactly would leave the input list in a corrupted state, which is unacceptable for production-grade library code.

---

## Key Insights

### Performance Optimization: The "Pointer Stitching" Pattern
This is a classic example of **in-place mutation as a space optimization**. While using a `HashMap<Node, Node>` is more intuitive and arguably safer (as it avoids mutating the input), it incurs a heap overhead for $N$ map entries. In highly constrained environments (embedded systems, large-scale processing), this pattern is superior.

### Subtle Bugs & Gotchas
1.  **Tail Node Separation:** In Step 3, the final node's `next` pointer in the copied list must be set to `null` to avoid pointing back into the original list structure. The provided code handles this: if `copiedNode.next` is null, the `if` block is skipped, leaving the copy's `next` as the value it took from the original list's trailing null.
2.  **Input Integrity:** This solution is **destructive**. It temporarily modifies the `next` pointers of the input list. If the function were to fail or throw an exception midway, the caller's input list would be left in a corrupted, interleaved state. In a production environment, you would wrap this in a `try-finally` block to ensure that if an error occurs, the original list structure is restored or the operation is aborted.
3.  **Thread Safety:** This algorithm is inherently **not thread-safe**. Since it relies on mutating the nodes of the original list to store auxiliary state, concurrent access to the linked list while `copyRandomList` is running will result in race conditions and memory corruption. If the input list must remain immutable to other threads, a `HashMap` approach is mandatory despite the $O(N)$ space cost.

---
