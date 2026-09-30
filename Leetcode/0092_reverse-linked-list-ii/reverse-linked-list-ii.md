# reverse-linked-list-ii

## linkedlist_solution.java
*Style: detailed*

# Engineering Deep-Dive: In-Place Sublist Reversal

## Summary
The `reverseBetween` implementation employs an **in-place pointer manipulation** technique to reverse a specific segment $[left, right]$ of a singly-linked list. By utilizing a "dummy head" pattern, the algorithm eliminates conditional branching for edge cases involving the list head (where $left = 1$). The process is decomposed into two phases: **Locate Phase** (identifying the segment boundaries) and **Reversal Phase** (iterative link redirection).

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Locate Phase:** We perform a single traversal up to index `right`. In the worst case, $right = N$, resulting in $O(N)$ operations.
*   **Reversal Phase:** We iterate over the segment of length $K = (right - left + 1)$. This is at most $O(N)$.
*   **Total:** $O(N) + O(K) \approx O(N)$, where $N$ is the total number of nodes in the list.

### Space Complexity: $O(1)$
*   The algorithm operates strictly **in-place**. We only maintain a constant number of pointers (`dummy`, `leftPrev`, `leftNode`, `rightNode`, `previous`, `currentNode`, `nextNode`), regardless of the input size $N$. No additional data structures (like stacks or recursion buffers) are utilized.

---

## Component Deep Dive

### 1. The Sentinel (Dummy) Node
The use of `dummy` is critical. It acts as a stable reference point (`dummy.next`) that persists even if the reversal begins at the head of the list ($left=1$). This avoids the "special case" logic typically required to handle head modifications, ensuring `leftPrev` is always a valid node object.

### 2. The Locate Phase
*   **Logic:** The algorithm iterates up to `right` to capture the three critical nodes:
    *   `leftPrev`: The anchor that connects to the new head of the sublist.
    *   `leftNode`: The node that will become the *tail* of the reversed sublist.
    *   `rightNode`: The node that will become the *head* of the reversed sublist.
*   **Edge Case:** If $left=1$, `leftPrev` correctly defaults to the `dummy` node.

### 3. The Reversal Phase (Pointer Reversal)
*   **Technique:** The implementation uses a modified iterative reversal. 
*   **Initialization Trick:** By initializing `previous` to `nodeAfter` (the node at `right + 1`) rather than `null`, the algorithm performs a "pre-wiring." As the loop proceeds, each node in the segment is redirected to the `previous` node. When the loop finishes, `rightNode.next` is already pointing to `nodeAfter`, satisfying the sublist-to-tail connection requirement automatically.

---

## Key Insights & Nuances

### Subtle Efficiency Gain
The decision to initialize `previous = nodeAfter` is a performance and logic optimization. In a standard reversal, you have to manually relink the head of the reversed segment to the remainder of the list. Here, the initialization essentially completes the tail-linkage concurrently with the reversal, reducing the number of post-reversal operations.

### Failure Modes & Safety
*   **Bounds Violation:** The provided code assumes $1 \le left \le right \le N$. If $right$ exceeds the list length, `rightNode.next` would be `null` and `rightNode` would become `null`, causing a `NullPointerException`. In a production environment, an additional check for `current == null` during the locate phase is advisable.
*   **Single-Pass Optimization:** While the current implementation traverses the list to find the nodes and then traverses the sublist again, this is strictly $O(N)$. For very large lists, one could optimize by merging the Locate and Reversal phases into a single pass using a "sliding window" pointer approach, though this significantly increases the cognitive complexity and potential for off-by-one errors. 

### Implementation Trap
Avoid modifying `leftNode.next` before the entire reversal is finished; otherwise, you lose the reference to the rest of the sublist. The current implementation correctly handles this by tracking `nextNode` in each iteration of the `while` loop, preserving the path forward.

---
