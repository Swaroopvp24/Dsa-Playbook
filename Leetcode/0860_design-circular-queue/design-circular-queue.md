# design-circular-queue

## linkedlist_solution(single linked list).java
*Style: detailed*

# Deep-Dive Reference: Linked-List Based Circular Queue

## Summary
The provided `MyCircularQueue` implementation utilizes a **Singly Linked List** to maintain a First-In-First-Out (FIFO) data structure with a fixed capacity. Unlike array-based circular queues that use modular arithmetic (index wrapping), this implementation achieves the "circular" logical behavior by maintaining explicit `front` and `rear` pointers. 

While the implementation does not physically form a circular loop in memory (i.e., `rear.next` does not point back to `front`), it mimics circular queue behavior by strictly enforcing a `capacity` constraint and managing node lifecycle via standard linked list pointer manipulation.

---

## Complexity Analysis

| Operation | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| `enQueue` | O(1) | O(1) |
| `deQueue` | O(1) | O(1) |
| `Front`/`Rear` | O(1) | O(1) |
| `isFull`/`isEmpty` | O(1) | O(1) |

### Rationale
*   **Time Complexity:** All operations perform a constant number of pointer assignments and integer comparisons. No loops or recursion are utilized, ensuring strict constant-time performance regardless of the state of the queue.
*   **Space Complexity:** The space complexity is O(k), where `k` is the `capacity`. This is dominated by the allocation of `ListNode` objects. No additional auxiliary data structures (like buffers) are used, making the space overhead strictly proportional to the number of stored elements.

---

## Component Deep Dive

### 1. State Management
The class relies on two primary state variables:
*   `size`: An integer tracker that serves as the "source of truth" for queue occupancy. This avoids O(n) traversals to determine fullness.
*   `capacity`: An immutable final field that defines the upper bound.

### 2. `enQueue(int value)`
*   **Mechanism:** Creates a new `ListNode`. If the queue is empty, both `front` and `rear` reference the new node. Otherwise, it updates `rear.next` to the new node and then updates the `rear` pointer.
*   **Edge Case Handling:** It explicitly checks `isFull()` before allocation, preventing memory overflow beyond the logical capacity.

### 3. `deQueue()`
*   **Mechanism:** Directly advances the `front` pointer to `front.next`. 
*   **Critical Path:** The logic includes a conditional check: `if (size == 0) { rear = null; }`. This is essential because if the last remaining element is dequeued, the `front` becomes `null`, but the `rear` would still be pointing at the orphaned node if not manually cleared, leading to potential memory leaks or state inconsistency.

### 4. `Front()` / `Rear()`
*   **Mechanism:** These are accessor methods that return the values pointed to by `front` and `rear`. 
*   **Safety:** They return `-1` if `isEmpty()` is true, adhering to common interface specifications for such structures.

---

## Key Insights & Performance Nuances

### Pointer Integrity
The implementation relies on manual garbage collection triggers. By nullifying `rear` when `size == 0`, we ensure that there are no remaining references to the dequeued `ListNode` objects, allowing the JVM to reclaim the memory promptly.

### Potential Limitations
*   **Memory Fragmentation:** Because this implementation uses `ListNode` objects, each enqueue operation triggers a new heap allocation. In a high-throughput environment, this may lead to frequent GC activity compared to an array-based circular queue, which utilizes contiguous memory and is generally more cache-friendly.
*   **Lack of "True" Circularity:** This is technically a **bounded linked queue**. A true circular queue typically uses a static array to recycle memory. This implementation creates and destroys nodes, which is less efficient if the queue experiences a high churn rate of insertions and deletions.

### Subtle Considerations
*   **Null Safety:** The code is robust against `NullPointerException` because the `isEmpty()` checks prevent dereferencing `front` or `rear` when they are null.
*   **Concurrency:** This class is **not thread-safe**. If this were to be used in a multithreaded context, the `enQueue` and `deQueue` operations would require `synchronized` blocks or an `AtomicReference` approach to protect the `size` variable and the `front`/`rear` pointers from race conditions.

---

## array+twopointer_solution.java
*Style: detailed*

# Deep‑Dive Reference for `MyCircularQueue` (Java)

## 1. Summary – What the implementation does & the algorithmic technique  

`MyCircularQueue` implements a **fixed‑capacity circular buffer** (a.k.a. ring queue) using a plain `int[]`.  
The core idea is to treat the underlying array as a circle where the **front** index points to the element that will be dequeued next and the **rear** index points to the *next free slot* for an enqueue.  

The queue maintains an auxiliary `size` counter to distinguish the **full** vs **empty** states without having to keep a sentinel element. All pointer arithmetic is performed modulo `capacity`, which guarantees O(1) wrap‑around without moving data.

---

## 2. Complexity Analysis  

| Operation | Time Complexity | Reasoning |
|-----------|----------------|-----------|
| `enQueue` | **O(1)** | Single array write, a constant‑time modulo, and a few primitive updates. |
| `deQueue` | **O(1)** | Only updates `front` (modulo) and `size`. No traversal. |
| `Front` / `Rear` | **O(1)** | Direct array access after an `isEmpty` check. |
| `isEmpty` / `isFull` | **O(1)** | Simple integer comparison. |
| Construction (`MyCircularQueue(k)`) | **O(k)** | Allocates an `int[k]` array; Java zero‑fills the array, which is linear in `k`. |

### Space Complexity  

- **Static**: `int[] queue` consumes `O(k)` memory where `k` is the user‑provided capacity.  
- **Auxiliary**: Four `int` fields (`front`, `rear`, `size`, `capacity`) → `O(1)`.  

Overall **O(k)** space.

---

## 3. Component Deep Dive  

### 3.1 Fields  

| Field | Purpose | Invariant |
|-------|---------|-----------|
| `int[] queue` | Backing storage for elements. | Length == `capacity`. |
| `int front` | Index of the element that will be returned by `Front()` / removed by `deQueue()`. | `0 ≤ front < capacity`. |
| `int rear` | Index *one past* the last valid element; also the insertion point for the next `enQueue`. | `0 ≤ rear < capacity`. |
| `int size` | Number of valid elements currently stored. | `0 ≤ size ≤ capacity`. |
| `int capacity` | Maximum number of elements the queue can hold. | Fixed after construction. |

### 3.2 Constructor  

```java
public MyCircularQueue(int k) {
    capacity = k;
    queue = new int[capacity];
    front = 0;
    rear = 0;
    size = 0;
}
```

- Allocates a fresh array; Java automatically initializes it to zeroes (irrelevant for correctness because `size` guards visibility).  
- Sets both pointers to zero, meaning the buffer is empty and the first insertion will go to index `0`.

### 3.3 Enqueue (`enQueue`)  

```java
public boolean enQueue(int value) {
    if (isFull()) return false;            // Guard against overflow
    queue[rear] = value;                    // Store at current tail
    rear = (rear + 1) % capacity;           // Advance tail with wrap‑around
    size++;                                 // Keep count
    return true;
}
```

**Key points**

1. **Full‑check** uses `size == capacity`. This avoids the classic “off‑by‑one” ambiguity when `front == rear`.  
2. **Modulo arithmetic** (`(rear + 1) % capacity`) ensures constant‑time wrap‑around without branching.  
3. **`size` increment** guarantees that `isFull` and `isEmpty` are O(1) checks.

### 3.4 Dequeue (`deQueue`)  

```java
public boolean deQueue() {
    if (isEmpty()) return false;           // Guard against underflow
    front = (front + 1) % capacity;        // Advance head
    size--;                                 // Decrease count
    return true;
}
```

- No element is physically cleared; the slot is considered garbage and will be overwritten on a future `enQueue`.  
- Advancing `front` by one modulo `capacity` is the core of the circular behavior.

### 3.5 Front & Rear Accessors  

```java
public int Front() {
    if (isEmpty()) return -1;
    return queue[front];
}
```

```java
public int Rear() {
    if (isEmpty()) return -1;
    return queue[(rear - 1 + capacity) % capacity];
}
```

- `Front` simply reads the element at `front`.  
- `Rear` is subtle: `rear` points *past* the last element, so we need to step back one slot.  
  The expression `(rear - 1 + capacity) % capacity` handles the case when `rear == 0` (wrap‑to‑`capacity‑1`).

### 3.6 Empty / Full Checks  

```java
public boolean isEmpty() { return size == 0; }
public boolean isFull()  { return size == capacity; }
```

- Because `size` is maintained on every mutation, these checks are O(1) and free of the “front == rear” ambiguity.

### 3.7 Edge‑Case Handling  

| Scenario | How the code handles it |
|----------|------------------------|
| **Enqueue into a full queue** | `isFull()` returns true → `enQueue` returns `false` without touching the array. |
| **Dequeue from an empty queue** | `isEmpty()` returns true → `deQueue` returns `false`. |
| **Front/Rear on empty queue** | Both return `-1` sentinel per LeetCode spec. |
| **Wrap‑around** | All pointer updates use `% capacity`, guaranteeing that indices stay in `[0, capacity‑1]`. |
| **Zero capacity (`k = 0`)** | Constructor creates a zero‑length array; `isFull()` is true from start (`size == capacity == 0`). All operations return `false`/`-1` as appropriate—no out‑of‑bounds access because modulo `0` is never executed (guarded by `isFull`/`isEmpty`). |

---

## 4. Key Insights, Optimizations & Pitfalls  

### 4.1 Why a `size` field?  
- **Avoids sentinel slot**: Many ring‑buffer implementations reserve one slot to differentiate full vs empty (`front == rear`), which reduces usable capacity by one.  
- Maintaining `size` preserves the full `k` capacity and makes `isFull`/`isEmpty` O(1) without extra conditionals.

### 4.2 Modulo vs Conditional Branching  
- `% capacity` compiles to a single remainder instruction on modern JVMs. For powers of two, a bitmask (`& (capacity‑1)`) would be marginally faster, but Java does not guarantee that `%` is slower, and readability wins.  
- If the queue were to be used in a *tight* real‑time loop with a known power‑of‑two capacity, replacing `%` with `& (capacity - 1)` could shave a few nanoseconds per operation.

### 4.3 Memory Visibility & Thread Safety  
- The implementation is **not** thread‑safe. `size`, `front`, `rear`, and `queue` are mutable without synchronization.  
- Adding `volatile` to the indices or wrapping operations in `synchronized` blocks would guarantee visibility for multi‑threaded producers/consumers, but would also increase contention.

### 4.4 Potential Subtle Bug – Modulo with Negative Numbers  
- The expression `(rear - 1 + capacity) % capacity` is deliberately written to avoid a negative intermediate value.  
- If we wrote simply `(rear - 1) % capacity`, Java’s `%` yields a negative remainder when `rear == 0`, leading to an `ArrayIndexOutOfBoundsException`.  

### 4.5 Zero‑Length Queue Edge Case  
- The constructor accepts any non‑negative integer. If `k == 0`, the array length is zero. All operations short‑circuit via `isFull`/`isEmpty` before touching the array, so no `ArrayIndexOutOfBoundsException` occurs.  
- However, calling `new MyCircularQueue(-1)` would allocate a negative‑size array and throw `NegativeArraySizeException`. Input validation could be added if defensive programming is required.

### 4.6 Alternative Designs  
| Design | Trade‑offs |
|--------|------------|
| **Two‑pointer + size** (current) | Full capacity usable, O(1) ops, extra integer field. |
| **Front & rear only, reserve slot** | No extra size field, but loses one element of capacity. |
| **Linked‑list nodes** | Unlimited growth, O(1) ops, higher per‑node overhead, more GC pressure. |
| **Atomic ring buffer (Disruptor pattern)** | Lock‑free multi‑producer/consumer, complex memory‑ordering guarantees. |

### 4.7 Performance Profiling Tips  

| Metric | How to measure |
|--------|----------------|
| **Throughput** (ops/s) | Use JMH benchmarks with tight loops of mixed `enQueue`/`deQueue`. |
| **Cache behavior** | Since the buffer is a contiguous array, it enjoys good spatial locality; however, random access pattern (alternating front/rear) can cause cache line thrashing if `capacity` exceeds L1. |
| **Branch prediction** | The early `if (isFull())` / `if (isEmpty())` branches are highly predictable under steady load. If the workload frequently hits the limit, branch misprediction cost rises. |

---

## 5. Quick Reference Cheat‑Sheet  

```java
// Construction
MyCircularQueue q = new MyCircularQueue(k); // O(k) allocation

// Enqueue
boolean ok = q.enQueue(val); // O(1)

// Dequeue
boolean ok = q.deQueue();    // O(1)

// Peek
int front = q.Front();       // O(1), -1 if empty
int rear  = q.Rear();        // O(1), -1 if empty

// State
boolean empty = q.isEmpty(); // O(1)
boolean full  = q.isFull();  // O(1)
```

- `front` points at the *current* head, `rear` points at the *next* free slot.
- `size` == number of valid entries, never exceeds `capacity`.
- All index updates: `idx = (idx + 1) % capacity`.

---

### Bottom Line  

The provided code is a **canonical, production‑ready ring buffer** for integer values with deterministic O(1) operations, full utilization of the allocated capacity, and clear handling of all edge conditions. The only non‑functional limitations are lack of concurrency control and absence of input validation for negative capacities.

---
