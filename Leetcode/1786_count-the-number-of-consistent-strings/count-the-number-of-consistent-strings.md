# count-the-number-of-consistent-strings

## attempt_1.java
*Style: detailed*

# Deep‑Dive Reference: `Solution.countConsistentStrings`

---  

## 1. Summary  

The method **`countConsistentStrings`** solves the classic *consistent‑string* problem (LeetCode 1684).  
Given a string `allowed` containing distinct lower‑case letters and an array `words`, the goal is to count how many words consist **exclusively** of characters from `allowed`.

**Algorithmic technique** – *set‑based membership test*:

1. **Pre‑process** `allowed` into a `HashSet<Character>` for **O(1)** average‑time containment checks.  
2. **Iterate** over each word; for each word, **scan** its characters until a character not in the set is encountered (early‑exit).  
3. **Increment** a counter only when the scan finishes without a mismatch.

The solution is a **straight‑forward greedy scan** with early termination, exploiting the fact that the decision for a word is binary (consistent / not) and does not require dynamic programming or sorting.

---  

## 2. Complexity Analysis  

| Phase | Operation | Worst‑case Cost per Element | Overall Cost | Reason |
|-------|-----------|----------------------------|--------------|--------|
| **Allowed‑set construction** | Insert each char of `allowed` into `HashSet` | O(1) amortized per insert | **O(|A|)**, where `|A| = allowed.length()` | HashSet offers constant‑time `add`. |
| **Word traversal** | For each `word` iterate its characters until a disallowed char is found | O(L) where `L = word.length()` (full scan) | **O( Σ |word_i| )** over all words | The inner loop may break early, but worst‑case when the word is consistent (or the first disallowed char is the last). |
| **Containment check** | `allowedCharacters.contains(ch)` | O(1) amortized | Embedded in the inner loop above | HashSet lookup is constant‑time on average; worst‑case O(|A|) if many hash collisions, but Java’s hash function for `Character` is essentially the code point, making collisions unlikely. |
| **Total** | – | – | **O(|A| + Σ |word_i| )** time, **O(|A|)** extra space | Space: the `HashSet` holds at most 26 elements (lower‑case alphabet). No extra per‑word storage. |

*Why these bounds hold*  

- **Time:** The algorithm touches each character of `allowed` exactly once and each character of each word at most once (early break can only reduce work). No nested iteration across words, so the cost is linear in the total input size.  
- **Space:** The `HashSet` stores a maximum of 26 `Character` objects (the English lower‑case alphabet). No auxiliary arrays, recursion stack, or per‑word buffers are allocated beyond the temporary `char[]` created by `String.toCharArray()`. The per‑word `char[]` is a *view* created by the JVM; its memory is reclaimed after each iteration.

---  

## 3. Component Deep Dive  

### 3.1. `Set<Character> allowedCharacters = new HashSet<>();`

- **Purpose:** Enables O(1) containment checks for any character.
- **Implementation details:**  
  - `HashSet` internally uses a hash table with load factor 0.75 and default initial capacity 16.  
  - When the set size exceeds `capacity * loadFactor`, the table resizes (doubling), rehashing all entries.  
  - Since `allowed` contains at most 26 distinct characters, resizing never occurs beyond the first few insertions, making the construction practically O(1) per element.

### 3.2. Populating the set

```java
for (char character : allowed.toCharArray()) {
    allowedCharacters.add(character);
}
```

- `String.toCharArray()` allocates a new `char[]` of size `|A|`.  
- The loop iterates over that array, invoking `HashSet.add`.  
- **Edge Cases:**  
  - If `allowed` contains duplicate characters, `HashSet` silently ignores subsequent adds; the algorithm still works because the set ends up containing the distinct characters only.  
  - An empty `allowed` string results in an empty set; consequently, **no word** can be consistent (unless also empty, which the problem definition typically excludes).  

### 3.3. Main word loop

```java
int consistentCount = 0;
for (String word : words) {
    boolean isConsistent = true;
    for (char character : word.toCharArray()) {
        if (!allowedCharacters.contains(character)) {
            isConsistent = false;
            break;
        }
    }
    if (isConsistent) {
        consistentCount++;
    }
}
```

- **Outer Loop:** Linear over the number of words `n = words.length`.  
- **Inner Loop:** Converts each `word` to a fresh `char[]`. This is necessary because Java strings are immutable; iterating directly over `charAt(i)` would avoid the allocation but is marginally slower due to bounds checks per call.  
- **Early‑exit (`break`):** Stops scanning as soon as a disallowed character appears, saving work for words that are *inconsistent early*.  
- **Flag variable `isConsistent`:** Keeps track of the word's status without needing an extra loop after the inner scan.  

#### Edge‑Case Handling

| Situation | Behaviour |
|-----------|------------|
| `words` is `null` | **Undefined** – the method will throw `NullPointerException`. Caller must guarantee non‑null input (as per LeetCode contract). |
| `words` contains `null` entries | Same as above – a `NullPointerException` when iterating `null.toCharArray()`. |
| Word length = 0 (empty string) | The inner loop never executes → `isConsistent` stays `true` → counts as consistent (consistent with definition: an empty word contains no disallowed characters). |
| `allowed` empty | `allowedCharacters` empty → `contains` always returns `false`. Only empty strings are counted. |
| Characters outside `'a'..'z'` | The set can hold any Unicode `char`. The algorithm does not enforce lower‑case; it will treat any character as allowed if present in `allowed`. This is robust but deviates from the problem's constraints if violated. |

### 3.4. Return value

```java
return consistentCount;
```

- Simple scalar return; no overflow concerns because `consistentCount` ≤ `words.length` ≤ `10^5` (LeetCode limits). Fits comfortably in a 32‑bit signed int.

---  

## 4. Key Insights & Gotchas  

| Insight | Explanation |
|---------|-------------|
| **HashSet vs. Boolean Array** | For the fixed alphabet size (26), a `boolean[26]` lookup (`allowedMask[char - 'a']`) would be marginally faster and avoid object allocation. However, the `HashSet` version is more general (works for any Unicode range) and still O(1). |
| **Allocation cost of `toCharArray()`** | Each word incurs a temporary `char[]`. If `words` is very large (e.g., 10⁶ entries, each length ≈ 10), the cumulative allocation can stress the garbage collector. An alternative is `for (int i = 0; i < word.length(); i++) { char c = word.charAt(i); ... }` which avoids per‑word array creation. |
| **Early‑exit impact** | The worst‑case scenario is when all words are consistent or the disallowed character appears at the *last* position of each word. In practice, many test cases have early failures, reducing runtime dramatically. |
| **HashSet load factor** | With ≤26 elements, the default capacity (16) may cause a single resize (to 32) when the 13th element is added. This resize cost is negligible, but if the method were to be called repeatedly in a tight loop, pre‑sizing the set (`new HashSet<>(allowed.length()*2)`) could shave microseconds. |
| **Potential bug – mutable `allowed`** | `allowed` is a `String`, immutable, so no concurrency issue. If the method were refactored to accept a mutable collection, concurrent modifications could corrupt the set. |
| **Thread‑safety** | The method is *stateless* aside from local variables, thus safe to call from multiple threads simultaneously. The only shared state would be the `words` array reference; callers must ensure it isn’t modified concurrently. |
| **Character encoding** | Java `char` is UTF‑16 code unit. For characters outside the BMP (surrogate pairs), `toCharArray()` yields two `char`s. The algorithm would treat each surrogate independently, which could lead to false positives/negatives if `allowed` contains such characters. Not an issue under the problem’s lower‑case ASCII assumption. |
| **Alternative bitmask solution** | Represent `allowed` as a 26‑bit integer (`int mask`). For each word, compute its mask on the fly (`mask |= 1 << (c - 'a')`) and test `(mask & ~allowedMask) == 0`. This eliminates the HashSet entirely and reduces per‑character work to a few bitwise ops. It’s the *optimal* solution in terms of constant factors. |
| **Testing corner cases** | - `allowed = ""`, `words = {"", "a"}` → result `1`. <br> - `allowed = "abc"`, `words = {null}` → throws NPE → test harness should guard against null entries. |
| **Micro‑benchmark tip** | When measuring performance, disable JIT warm‑up or use JMH. The difference between `HashSet` and a boolean array is on the order of **10–20 ns** per character, which becomes noticeable only for very large inputs. |

---  

## 5. Suggested Optimizations (Optional)

1. **Bitmask Implementation** (constant‑time per character, no object allocation):
   ```java
   int allowedMask = 0;
   for (char c : allowed.toCharArray()) {
       allowedMask |= 1 << (c - 'a');
   }
   int count = 0;
   for (String w : words) {
       int wordMask = 0;
       boolean ok = true;
       for (int i = 0; i < w.length(); i++) {
           int bit = 1 << (w.charAt(i) - 'a');
           if ((allowedMask & bit) == 0) { ok = false; break; }
           wordMask |= bit; // optional, only if you need the mask later
       }
       if (ok) count++;
   }
   return count;
   ```
2. **Avoid `toCharArray()`** – use `charAt(i)` loop to cut per‑word allocation.
3. **Pre‑size `HashSet`** – `new HashSet<>(allowed.length() * 2)` to prevent the single resize.
4. **Parallel Stream** – for extremely large `words` arrays, a parallel reduction can be employed (though overhead often outweighs benefits for ≤10⁵ words).

---  

### Bottom Line  

The provided implementation is **correct, readable, and sufficiently efficient** for the constraints typical of the problem (≤10⁴–10⁵ total characters). Its core strength is the early‑exit scan combined with a constant‑time lookup structure. For performance‑critical contexts, a bitmask or boolean‑array lookup can shave constant factors, but the algorithmic complexity remains linear in the input size.

---

## attempt_2.java
*Style: detailed*

## 📘  Deep‑Dive Reference – `countConsistentStrings`

> **Problem (LeetCode 1684)** – Given a string `allowed` consisting of distinct lowercase English letters and an array `words`, count how many words are *consistent*; i.e., every character of the word appears in `allowed`.

The implementation below solves the problem with a **static‑size boolean lookup table** and a **two‑level scan** (allowed → words → characters).

```java
class Solution {
    public int countConsistentStrings(String allowed, String[] words) {
        boolean[] allowedCharacters = new boolean[26];

        // 1️⃣  Populate the whitelist.
        for (char character : allowed.toCharArray()) {
            allowedCharacters[character - 'a'] = true;
        }

        int consistentCount = 0;

        // 2️⃣  Validate each word.
        for (String word : words) {
            boolean isConsistent = true;

            for (char character : word.toCharArray()) {
                if (!allowedCharacters[character - 'a']) {
                    isConsistent = false;
                    break;          // early exit on first violation
                }
            }

            if (isConsistent) {
                consistentCount++;
            }
        }

        return consistentCount;
    }
}
```

---

## 1️⃣  Summary – Core Idea & Algorithmic Technique

| Step | What happens | Data structure used |
|------|--------------|---------------------|
| **Whitelist build** | Convert `allowed` into a *constant‑time membership* structure. | `boolean[26]` – direct‑address table for `'a'..'z'`. |
| **Word validation** | Iterate over each word and its characters, checking the table. | Same `boolean[26]`. Early `break` stops scanning a word as soon as a forbidden character appears. |
| **Counting** | Increment a counter only when a word passes the full scan. | Primitive `int`. |

The technique is essentially **direct addressing** (also known as a *bitset* when compressed). Because the alphabet size is fixed (`26`), the lookup cost is *O(1)* per character and the auxiliary space is *O(1)*.

---

## 2️⃣  Complexity Analysis

Let  

- `A = |allowed|`  (≤ 26, because the problem guarantees distinct lowercase letters)  
- `W = words.length`  
- `L_i = words[i].length` – length of the *i‑th* word.  
- `N = Σ_{i=0}^{W-1} L_i` – total number of characters across all words.

### Time Complexity

| Phase | Operation | Cost |
|-------|-----------|------|
| **Whitelist construction** | `for (char c : allowed)` | `Θ(A)` |
| **Word loop** | Outer loop iterates `W` times. | `Θ(W)` (overhead) |
| **Inner char scan** | For each word `i`, scan up to `L_i` characters, possibly breaking early. In the worst case (every word is consistent) we scan **all** characters. | `Θ(N)` |
| **Total** | `Θ(A + N)` → Since `A ≤ 26` is a constant, the dominant term is `Θ(N)`. |

> **Why**: The boolean table gives *O(1)* per character check, so the total time scales linearly with the total number of characters processed.

### Space Complexity

| Component | Size | Reason |
|-----------|------|--------|
| `allowedCharacters` | `26` booleans → 26 bytes (or 26 bits if packed) | Fixed size, independent of input length. |
| Counters & loop variables | O(1) | Primitive types. |
| **Overall** | **O(1)** (constant) | No allocation proportional to `words` or `allowed`. |

> **Note**: If we replace the `boolean[]` with an `int` bitmask, the space drops to a single 32‑bit word, but asymptotically it stays `O(1)`.

---

## 3️⃣  Component Deep Dive

### 3.1  Whitelist (`boolean[] allowedCharacters`)

- **Indexing scheme**: `character - 'a'` maps `'a'..'z'` to `0..25`. This relies on the **Unicode code point** ordering of ASCII letters.  
- **Initialization**: Java zero‑fills the array, guaranteeing a `false` default for every entry.  
- **Population loop**:

  ```java
  for (char character : allowed.toCharArray()) {
      allowedCharacters[character - 'a'] = true;
  }
  ```

  - `toCharArray()` creates a fresh `char[]` copy; negligible cost because `allowed` ≤ 26.  
  - The loop is *idempotent* – if `allowed` contains duplicates (contrary to spec), later assignments simply overwrite `true` again, no harm.

### 3.2  Word Validation Loop

```java
for (String word : words) {
    boolean isConsistent = true;
    for (char character : word.toCharArray()) {
        if (!allowedCharacters[character - 'a']) {
            isConsistent = false;
            break;
        }
    }
    if (isConsistent) {
        consistentCount++;
    }
}
```

- **Early exit (`break`)**: Stops scanning a word the moment a disallowed character appears.  
  - **Best‑case** for a word with a disallowed character at position `k` → `O(k)`.  
  - **Worst‑case** for a consistent word → full `L_i` scans.  
- **`boolean isConsistent` flag**: Avoids nested `if` statements; clearer intent.  

#### Edge‑case handling inside the inner loop

| Situation | Current behaviour | Potential failure mode |
|-----------|-------------------|------------------------|
| **Empty word** (`word = ""`) | Inner loop never executes → `isConsistent` stays `true` → counted as consistent (as per problem definition). | None. |
| **Non‑lowercase characters** (e.g., `'A'`, `'1'`) | `character - 'a'` yields a negative or ≥ 26 index → **ArrayIndexOutOfBoundsException**. | The method assumes well‑formed input (lowercase a–z). Defensive code could guard with `if (character < 'a' || character > 'z') …`. |
| **`null` word** (i.e., `words` contains `null`) | `for (char character : word.toCharArray())` throws `NullPointerException`. | Not required by LeetCode; could be handled by a null‑check. |
| **`allowed` empty** | `allowedCharacters` remains all `false`; every non‑empty word will be rejected, empty word passes. | Correct per spec. |
| **`words` empty** | Outer loop never runs → `consistentCount` remains `0`. | Correct. |

### 3.3  Alternative Implementations (for performance or readability)

| Variant | Code Sketch | Trade‑offs |
|--------|-------------|------------|
| **Bitmask (`int mask`)** | ```int mask = 0; for (char c : allowed.toCharArray()) mask |= 1 << (c - 'a'); // check: ((mask >> (c - 'a')) & 1) == 1 ``` | Reduces memory to a single int, possibly faster due to bitwise ops, but slightly less readable for newcomers. |
| **`HashSet<Character>`** | ```Set<Character> set = new HashSet<>(); for (char c : allowed.toCharArray()) set.add(c); // check: set.contains(c) ``` | O(1) average lookup, but higher constant factors (boxing, hashing) and O(|allowed|) extra space. |
| **`boolean[]` with `Arrays.fill`** | Not needed; zero‑fill is implicit. | None – current approach already optimal. |

---

## 4️⃣  Key Insights & Gotchas

1. **Constant‑size direct addressing beats generic containers**  
   - For a *fixed* alphabet, a `boolean[26]` (or bitmask) is *the* optimal representation: O(1) lookup, no hashing overhead, and *no* dynamic memory allocation beyond the static array.

2. **Early termination is the primary micro‑optimisation**  
   - The `break` prevents scanning the rest of a word once a violation is found, which can dramatically reduce work when the majority of words are inconsistent and the offending character appears early.

3. **Potential integer overflow is impossible**  
   - The counter `consistentCount` increments at most `words.length` times; `words.length` fits in a 32‑bit `int` (LeetCode constraints ≤ 10⁴). No overflow checks required.

4. **Character‑range safety**  
   - The code assumes **strictly lowercase English letters**. If the input source ever changes (e.g., Unicode letters), the direct addressing will throw `ArrayIndexOutOfBoundsException`. Guarding the index calculation is a cheap defensive addition:
     ```java
     int idx = character - 'a';
     if (idx < 0 || idx >= 26) { isConsistent = false; break; }
     ```

5. **Memory layout**  
   - The boolean array is stored as 1‑byte per entry in the JVM (not a packed bitset). For a truly minimal footprint, a 32‑bit `int` mask (`mask |= 1 << idx`) would halve the memory, but the impact is negligible for a constant‑size table.

6. **`toCharArray()` vs. `charAt(i)`**  
   - Using `toCharArray()` incurs a temporary array allocation per word (`O(L_i)` memory, reclaimed by GC). An alternative:
     ```java
     for (int i = 0; i < word.length(); ++i) {
         char c = word.charAt(i);
         …
     }
     ```
     This eliminates the per‑word allocation and can shave off GC pressure for large inputs. The asymptotic complexity stays the same; the trade‑off is marginally better runtime in tight loops.

7. **Thread‑safety**  
   - The method is **stateless** (no static fields) and thus safe for concurrent calls. If the whitelist were cached across calls (e.g., in a static field) we’d need to ensure immutable storage or proper synchronization.

8. **Testing edge cases**  
   - Verify: empty `allowed`, empty `words`, words containing every possible letter, and maximal input size (≈10⁴ words, each length ≈10⁴). The algorithm’s linearity guarantees it will stay within typical time limits (≈0.1 s–0.5 s on LeetCode hardware).

---

## 📎  TL;DR Cheat Sheet

| Symbol | Meaning |
|--------|---------|
| `A` | `allowed.length` (≤ 26) |
| `W` | `words.length` |
| `N = Σ L_i` | total characters across all words |
| **Time** | **Θ(N)** (plus negligible Θ(A)) |
| **Space** | **Θ(1)** (26 booleans) |
| **Core trick** | Direct‑address boolean table → O(1) membership test per character |
| **Optimization** | Early `break` + possible replacement of `boolean[]` with bitmask or `charAt` to avoid temporary arrays. |

--- 

*Prepared by the Senior Staff Engineer – reference for code‑review, performance tuning, and future extensions.*

---
