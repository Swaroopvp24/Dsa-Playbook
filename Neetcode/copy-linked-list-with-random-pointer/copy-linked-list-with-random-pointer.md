# copy-linked-list-with-random-pointer

## linkedlist_solution(hashMap).java
*Style: detailed*

# Technical Reference: Deep Copy of Linked List with Random Pointers

## Summary
The solution employs a **two-pass Hash-Map-based mapping strategy** to perform a deep copy of a singly linked list where nodes contain an additional "random" pointer. This technique decouples the node instantiation process from the structural linkage process, effectively resolving the circular dependency problem inherent in graph-like linked structures. By using the original node reference as the key in a `HashMap`, we achieve $O(1)$ lookup for the corresponding cloned node during the pointer assignment phase.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **First Pass:** $N$ iterations to instantiate each node and populate the `HashMap`. Each `Map.put()` operation is $O(1)$ on average.
*   **Second Pass:** $N$ iterations to establish `next` and `random` links. Each `Map.get()` operation is $O(1)$ on average.
*   **Total:** $O(N + N) = O(N)$, where $N$ is the number of nodes in the list.

### Space Complexity: $O(N)$
*   The `HashMap` stores exactly $N$ entries, where each entry consists of an object reference mapping (original `Node` to cloned `Node`).
*   Additional space for the cloned list is proportional to $N$, but in strict terms of auxiliary space, the Map is the primary consumer.

## Component Deep Dive

### 1. The Mapping Strategy (`HashMap<Node, Node>`)
The core of the algorithm is the identity-based mapping. In Java, `HashMap` uses the object's `hashCode()` and `equals()` methods. Since `Node` does not override these, the `HashMap` relies on default reference equality (the memory address). This is exactly what is required to ensure that each unique node in the original structure is mapped to one—and only one—corresponding cloned node.

### 2. Phase 1: Decoupled Instantiation
By iterating through the original list first, we ensure that every target node exists in the `HashMap` before we attempt to link them. This prevents `NullPointerException` errors during the second pass when a `random` pointer might refer to a node that hasn't been instantiated yet.

### 3. Phase 2: Pointer Reconstruction
We traverse the original list a second time. For every `current` node:
*   `copiedNode.next = originalToCopy.get(current.next)`: If `current.next` is `null`, `get()` returns `null`, correctly terminating the list.
*   `copiedNode.random = originalToCopy.get(current.random)`: Similarly, if `current.random` is `null`, the copy’s random pointer remains `null`.

## Key Insights

*   **Handling Nulls:** The `HashMap.get()` method gracefully returns `null` if the key is `null`. This is critical, as it eliminates the need for explicit conditional checks (e.g., `if (current.random != null)`) when assigning pointers.
*   **Space-Time Tradeoff:** The `HashMap` approach is highly readable and idiomatic. However, it is possible to achieve $O(1)$ auxiliary space by interleaving the cloned nodes into the original list (i.e., `A -> A' -> B -> B'`). While that approach reduces memory overhead, it complicates the logic and is non-thread-safe if the list is being accessed concurrently.
*   **Garbage Collection Considerations:** Because the `HashMap` holds strong references to all cloned nodes, they will not be garbage collected until the `HashMap` itself goes out of scope. In memory-constrained systems, ensuring the `originalToCopy` map is cleared or falls out of scope immediately after the method returns is important.
*   **Edge Case Handling:**
    *   **Empty List:** If `head` is `null`, the `while` loops are skipped, and `originalToCopy.get(null)` returns `null`, correctly returning an empty structure.
    *   **Single Node:** If a node points to itself (`random` points to `self`), the logic holds because `originalToCopy.get(current)` will retrieve the same cloned object for the random pointer.

---

## linkedlist_optimal_solution.java
*Style: detailed*

# Deep-Dive: Deep Copying Linked Lists with Random Pointers

## Summary
The solution implements a **three-pass interleaving algorithm** to perform a deep copy of a linked list where each node contains an arbitrary `random` pointer. This approach avoids the auxiliary space overhead of a `HashMap` (which would typically store mapping from `original -> copy`) by repurposing the existing structure of the list itself. By interleaving the copied nodes directly into the original list structure (`A -> A' -> B -> B'`), we gain direct O(1) access to the corresponding copied nodes, enabling the resolution of `random` pointers without additional data structures.

## Complexity Analysis

### Time Complexity: $O(N)$
The algorithm performs three sequential linear passes over the linked list:
1. **Pass 1 (Interleaving):** Visits each of the $N$ nodes once to create the copy.
2. **Pass 2 (Random Pointer Mapping):** Visits each original node ($N$) to assign `random` pointers.
3. **Pass 3 (List Decoupling):** Visits each node ($N$) to restore the original list and isolate the copy.
Since $O(N) + O(N) + O(N) = O(N)$, the algorithm scales linearly with the number of nodes.

### Space Complexity: $O(1)$
This solution achieves optimal auxiliary space complexity. While it creates $N$ new nodes, this is the inherent requirement of the problem (returning a new list). It uses no extra data structures (like HashMaps or recursion stacks) regardless of list size. All pointer assignments are done in-place.

## Component Deep Dive

### 1. The Interleaving Phase (Expansion)
The logic `copiedNode.next = current.next; current.next = copiedNode;` creates a linked structure: $A \to A' \to B \to B'$. This is the critical architectural step. It establishes the mathematical relationship where `originalNode.next` is guaranteed to be its corresponding `copiedNode`.

### 2. Random Pointer Resolution
The transformation `current.next.random = current.random.next` is the core of the algorithm.
*   `current` is the original node.
*   `current.random` is the target node in the original list.
*   `current.random.next` is the *copied* version of that target node because of the interleaving in Step 1.
*   **Edge Case:** If `current.random` is `null`, the pointer assignment is skipped, correctly maintaining `null` in the copy.

### 3. List Decoupling (Restoration)
This phase cleans up the "workspace." By setting `current.next = copiedNode.next`, we revert the original list's `next` pointers to their original state. Simultaneously, we link the copied nodes to each other via `copiedNode.next = copiedNode.next.next`. 
*   **Edge-case handling:** The condition `if (copiedNode.next != null)` prevents a `NullPointerException` when processing the final node in the list.

## Key Insights

### Avoiding Memory Overhead
Most naive implementations use a `HashMap<Node, Node>` to store the reference mapping. While readable, it requires $O(N)$ extra space. This interleaved approach is the "Senior Engineer" preference for resource-constrained environments (e.g., embedded systems or massive datasets) where heap pressure is a concern.

### Subtle Bugs & Gotchas
*   **Pointer Corruption:** A common bug in this algorithm is failing to restore the original list's `next` pointers. If you leave the interleaved nodes in the original list, the caller receives a corrupted data structure, which can cause subtle memory leaks or logic errors in subsequent operations. 
*   **Loop Termination:** In the decoupling phase, the `current` pointer must progress correctly. `current = current.next` works *after* `current.next` has been restored to point to the next original node. If the logic order is swapped, the traversal will enter the copied list instead of the original.
*   **Readability vs. Performance:** While this is highly space-efficient, it modifies the original list structure in-flight. In a multi-threaded environment, this approach would be **unsafe** without external locking, whereas a `HashMap` approach would allow for reading from the original list while constructing the copy concurrently.

---
