# unique-email-addresses

## attempt_1.java
*Style: detailed*

# Technical Deep Dive: Email Normalization & De-duplication

## 1. Summary
The provided solution addresses the "Unique Email Address" problem by implementing a canonicalization pipeline. It transforms potentially heterogeneous email inputs into a normalized state by enforcing domain-specific formatting rules (ignoring characters post-`+` and removing dots) before performing a set-based deduplication. This is a classic **string canonicalization** pattern, ensuring that multiple variations of the same mailbox resolve to a singular identifier.

## 2. Complexity Analysis

### Time Complexity: $O(N \cdot M)$
*   **$N$**: Total number of email strings in the input array.
*   **$M$**: Average length of an email string.
*   **Reasoning**: For each email string, we iterate through its characters to perform `split("@")`, `split("\\+")`, `replace(".", "")`, and eventually perform string concatenation. Since each of these operations is linear relative to the length of the string, and we perform these operations $N$ times, the aggregate complexity is linear relative to the total character count of the input. HashSet `add` operations operate in $O(M)$ average time (string hashing).

### Space Complexity: $O(N \cdot M)$
*   **Reasoning**: In the worst-case scenario where every email provided is unique after normalization, the `HashSet` will store all $N$ emails, each of length up to $M$. We also allocate temporary strings during the split and replace operations, though these are reclaimed by the Garbage Collector, the persistent memory footprint is dictated by the contents of the `HashSet`.

## 3. Component Deep Dive

### Normalization Pipeline
1.  **Splitting (`split("@")`)**: This is the primary partitioning step. Note that `split` uses regex internally, which carries a minor overhead. If this were a performance-critical hot path with millions of emails, using `email.indexOf('@')` and `substring()` would be more efficient by avoiding regex compilation/matching.
2.  **Local Name Truncation (`split("\\+")`)**: The logic correctly identifies that the `+` character acts as a terminator for the mailbox alias. By splitting and taking index `0`, we effectively discard all alias information.
3.  **Dot Removal (`replace(".", "")`)**: This is the most computationally expensive string transformation here. `replace()` scans the entire `localName` string. Since dots only matter in the local part, restricting this operation to `localName` instead of the full email is a critical optimization for memory and CPU cycles.

### Data Structure Selection
*   **`java.util.HashSet`**: Chosen for its $O(1)$ average-case insertion and lookup time. It relies on the `String.hashCode()` implementation. Because we normalize the strings *before* insertion, we ensure that the hash bucket distribution remains efficient and collisions are minimized, as we are reducing the input space to a canonical form.

### Edge Case Handling
*   **Empty Strings/Missing Parts**: The current implementation assumes the input follows valid `local@domain` syntax. If an input contains no `@`, `emailParts[1]` will throw an `ArrayIndexOutOfBoundsException`.
*   **No Dots/No Pluses**: The code handles these gracefully; `replace` and `split` return the original string if the target character is not found, ensuring the pipeline remains robust.

## 4. Key Insights & Performance Nuances

*   **Regex Overhead**: The `split("\\+")` is a regex-based call. While convenient, regex in Java involves pattern compilation or matching logic. For high-throughput systems, `localName.indexOf('+')` followed by a `substring` operation is significantly faster as it avoids regex engine overhead entirely.
*   **String Allocation**: Every call to `replace` and `split` creates new `String` objects in the heap. In a memory-constrained environment, implementing an in-place character manipulation (using a `StringBuilder` or a `char[]` buffer) would drastically reduce GC pressure by avoiding the allocation of intermediate strings during the normalization phase.
*   **Scalability Consideration**: If this were a distributed system or involved datasets larger than memory, you would move from a `HashSet` to a Bloom Filter (if a small false-positive rate is acceptable) or a distributed cache (e.g., Redis) to track unique identifiers across multiple instances.
*   **Subtle Bug Risk**: The current implementation does not validate the domain part. If the problem definition implies that the domain is case-insensitive, a `domain.toLowerCase()` call would be required; as written, the code treats `User@Domain.com` and `User@domain.com` as different entries, which might be incorrect depending on the specific mail server requirements.

---
