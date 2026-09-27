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
