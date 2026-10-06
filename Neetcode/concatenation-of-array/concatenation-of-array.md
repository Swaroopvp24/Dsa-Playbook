# concatenation-of-array

## attempt_1.java
*Style: concise*

### Study Notes: Array Concatenation

**Overview**
Creates a new array that is the concatenation of the input array `nums` with itself (i.e., `nums + nums`). The solution runs in $O(n)$ time complexity.

**Key Components**
*   `ans[]`: Pre-allocated result array of size `2 * n`.
*   Single-loop assignment: Populates both the first and second halves of the result array simultaneously using the index offset `i + n`.

**Non-Obvious Logic**
*   **Loop Optimization:** By using `ans[i]` and `ans[i + n]` within the same iteration, we avoid traversing the array twice, effectively filling the new buffer in one pass.
*   **Memory Allocation:** Initializing `ans` with `n * 2` is more performant than dynamically resizing or using `System.arraycopy`, as it avoids intermediate object creation.

---

## attempt_1.java
*Style: concise*

### Concatenation of Array (`getConcatenation`)

**Purpose**
Creates a new array that is the concatenation of the input array `nums` with itself (i.e., `[nums, nums]`), resulting in an array of size `2n`.

**Key Components**
*   `getConcatenation(int[] nums)`: Initializes a result array of size `2 * nums.length` and performs a single-pass copy to populate the values.

**Implementation Notes**
*   **Efficiency:** The algorithm runs in $O(n)$ time complexity and $O(n)$ space complexity.
*   **Indexing Logic:** By using a single loop, you can map the index `i` from the original array to both `ans[i]` and `ans[i + n]` simultaneously, effectively filling both halves of the result array in one iteration.

---

## array_join_attempt_1.java
*Style: concise*

### Notes: Array Concatenation

#### Overview
This code creates a new array that is the concatenation of the input array `nums` with itself. It returns a result of length `2n` where the original elements are duplicated into the first and second halves.

#### Key Components
*   **`getConcatenation(int[] nums)`**: The primary logic function. It allocates a result array of size `2 * nums.length` and performs a single-pass copy to populate both halves of the result.

#### Key Logic
*   **Single-Pass Efficiency**: Rather than using two separate loops or `System.arraycopy`, the code performs both assignments (`ans[i]` and `ans[i + n]`) within a single `O(n)` iteration, minimizing overhead.
*   **Indexing**: The `ans[i + n]` index ensures that the second half of the output array is filled synchronously with the first half as the loop traverses the input.

---

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Array Concatenation Implementation

## 1. Summary
The implementation solves the array concatenation problem by allocating a target array of size $2n$ and performing a single-pass linear copy. It leverages the property that the concatenated array $ans$ consists of two identical segments: $ans[0 \dots n-1] = nums$ and $ans[n \dots 2n-1] = nums$. By utilizing a single loop to map indices from the source to both target segments simultaneously, it minimizes branch overhead and maximizes cache locality during memory writes.

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm iterates exactly $n$ times, where $n$ is the length of the input array `nums`.
*   **Operations:** Within each iteration, two constant-time ($O(1)$) assignment operations are performed. 
*   **Total:** $n \times (O(1) + O(1)) = O(n)$. Given that we must populate $2n$ slots in the output array, $O(n)$ is the theoretical lower bound for this operation.

### Space Complexity: $O(n)$
*   **Derivation:** The algorithm allocates a new integer array `ans` of size $2n$.
*   **Classification:** This is categorized as $O(n)$ auxiliary space because the output space grows linearly with the size of the input. The algorithm does not utilize any additional data structures that scale with input size (excluding the mandatory return array).

## 3. Component Deep Dive

### Index Mapping Strategy
The core logic resides in the loop:
```java
ans[i] = nums[i];
ans[i + n] = nums[i];
```
This approach avoids the need for two separate loops or `System.arraycopy`. By using the offset index `i + n`, the algorithm effectively performs a dual-write pattern.

### Memory Allocation
*   **`int ans[] = new int[n * 2];`**: In Java, array allocation initializes the heap memory with default values ($0$ for `int`).
*   **Edge Cases:**
    *   **Empty Array ($n=0$):** The code initializes an array of size $0$, the loop condition `i < 0` fails immediately, and it returns an empty array. This is memory-safe and functionally correct.
    *   **Single Element ($n=1$):** `ans` is size 2. `ans[0]` and `ans[1]` are assigned `nums[0]`. Correct behavior.
    *   **Max Integer Array:** If `n` is near `Integer.MAX_VALUE / 2`, `n * 2` will overflow, resulting in a negative array size and triggering a `NegativeArraySizeException`.

## 4. Key Insights & Performance Nuances

### Hardware-Level Optimization (Cache Locality)
*   **Write Pattern:** The algorithm exhibits excellent spatial locality. `ans[i]` and `ans[i+n]` are accessed sequentially. As `i` increments, the writes to the first half of the array are contiguous, and writes to the second half are also contiguous. This maximizes CPU cache line utilization.
*   **Branch Prediction:** The loop is extremely simple, making it a prime candidate for loop unrolling by the Just-In-Time (JIT) compiler.

### Alternative Approaches (System.arraycopy)
For production-grade systems, `System.arraycopy` is often preferred over manual loops:
```java
System.arraycopy(nums, 0, ans, 0, n);
System.arraycopy(nums, 0, ans, n, n);
```
**Why the manual loop might be better here:**
*   `System.arraycopy` involves JNI (Java Native Interface) overhead for small arrays. For very small `n`, the manual loop is often faster because it avoids the overhead of a native method call. 
*   However, for large `n`, `System.arraycopy` leverages underlying `memmove`/`memcpy` intrinsics which are highly optimized for bulk memory transfers.

### Subtle Considerations
*   **Redundancy:** The `System.out.println(ans.length)` statement is a side-effect that will degrade performance in high-throughput environments due to I/O blocking. In a performance-critical context, this should be removed.
*   **Memory Pressure:** Since this algorithm allocates $2n$ space and maintains the original array, the peak heap consumption is $3n$. If `n` is large, consider memory constraints when scaling this solution.

---
