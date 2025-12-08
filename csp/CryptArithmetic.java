package CSP;

import java.util.*;

public class CryptArithmetic {

    static String[] words = {"SEND", "MORE"};
    static String result = "MONEY";

    static Map<Character, Integer> charToDigit = new HashMap<>();
    static boolean[] usedDigits = new boolean[10]; 

    public static void main(String[] args) {
        Set<Character> uniqueChars = new HashSet<>();
        for (String w : words) for (char c : w.toCharArray()) uniqueChars.add(c);
        for (char c : result.toCharArray()) uniqueChars.add(c);

        char[] letters = new char[uniqueChars.size()];
        int idx = 0;
        for (char c : uniqueChars) letters[idx++] = c;

        if (!solve(letters, 0)) {
            System.out.println("No solution exists!");
        }
    }

    private static boolean solve(char[] letters, int index) {
        if (index == letters.length) {
            if (isValidSolution()) {
                printSolution();
                return true;
            }
            return false;
        }

        char current = letters[index];

        for (int digit = 0; digit <= 9; digit++) {
            if (!usedDigits[digit]) {
                if (digit == 0 && startsWith(current)) continue;

                charToDigit.put(current, digit);
                usedDigits[digit] = true;

                if (solve(letters, index + 1)) return true;

                charToDigit.remove(current);
                usedDigits[digit] = false;
            }
        }
        return false;
    }

    private static boolean isValidSolution() {
        int sum = 0;
        for (String w : words) {
            sum += wordToNumber(w);
        }
        return sum == wordToNumber(result);
    }

    private static int wordToNumber(String word) {
        int value = 0;
        for (char c : word.toCharArray()) {
            value = value * 10 + charToDigit.get(c);
        }
        return value;
    }

    private static boolean startsWith(char c) {
        for (String w : words) if (w.charAt(0) == c) return true;
        return result.charAt(0) == c;
    }

    private static void printSolution() {
        System.out.println("Solution Found:");
        for (Map.Entry<Character, Integer> e : charToDigit.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
        System.out.println();

        for (String w : words) System.out.println(wordToNumber(w));
        System.out.println("+");
        System.out.println("-----");
        System.out.println(wordToNumber(result));
    }
}


/*
Nice puzzle — this is a standard **cryptarithmetic** solver implemented by simple backtracking. I’ll explain the code **line-by-line**, then summarize the problem, the data structures, complexity, correctness/limitations, useful improvements, and finish with a short pseudocode you can reuse.

# What the program solves (short)

It solves the cryptarithm **SEND + MORE = MONEY**. Every letter stands for a distinct digit (0–9). Leading letters of words cannot be 0. The program finds an assignment of digits to letters that makes the arithmetic equation true, printing the first solution it finds.

---

# Line-by-line explanation

```java
package CSP;

import java.util.*;
```

* Declares package `CSP`.
* Imports all useful Java utility classes (`Set`, `Map`, `HashSet`, `HashMap`, etc.).

```java
public class CryptArithmetic {
```

* Program class.

```java
    static String[] words = {"SEND", "MORE"};
    static String result = "MONEY";
```

* `words` are the addends (left side).
* `result` is the sum (right side). Here the equation is `SEND + MORE = MONEY`.

```java
    static Map<Character, Integer> charToDigit = new HashMap<>();
    static boolean[] usedDigits = new boolean[10];
```

* `charToDigit` maps each letter (Character) to its assigned digit (Integer).
* `usedDigits[d]` is `true` if digit `d` has already been assigned to some letter (ensures all letters map to distinct digits).

```java
    public static void main(String[] args) {
        Set<Character> uniqueChars = new HashSet<>();
        for (String w : words) for (char c : w.toCharArray()) uniqueChars.add(c);
        for (char c : result.toCharArray()) uniqueChars.add(c);
```

* Builds a set `uniqueChars` containing every distinct letter that appears in `words` and `result`. `HashSet` removes duplicates.
* For `SEND, MORE, MONEY` the letters are: S, E, N, D, M, O, R, Y (8 unique letters).

```java
        char[] letters = new char[uniqueChars.size()];
        int idx = 0;
        for (char c : uniqueChars) letters[idx++] = c;
```

* Converts the `Set` into a `char[] letters` for indexed access during recursion.
* Note: iteration order of a `HashSet` is not guaranteed—`letters` order is effectively arbitrary.

```java
        if (!solve(letters, 0)) {
            System.out.println("No solution exists!");
        }
    }
```

* Calls recursive `solve` starting at index 0 (first letter). If `solve` returns `false` (no solution), prints message.

---

```java
    private static boolean solve(char[] letters, int index) {
        if (index == letters.length) {
            if (isValidSolution()) {
                printSolution();
                return true;
            }
            return false;
        }
```

* `solve` is a standard backtracking routine.
* Base case: if every letter has been assigned (`index == letters.length`), check if the full assignment is a valid arithmetic solution by calling `isValidSolution()`. If valid, print and return `true`. Otherwise backtrack (`false`).

```java
        char current = letters[index];

        for (int digit = 0; digit <= 9; digit++) {
            if (!usedDigits[digit]) {
                if (digit == 0 && startsWith(current)) continue;

                charToDigit.put(current, digit);
                usedDigits[digit] = true;

                if (solve(letters, index + 1)) return true;

                charToDigit.remove(current);
                usedDigits[digit] = false;
            }
        }
        return false;
    }
```

* For the current letter, try every digit 0..9 that is not used yet.
* `if (digit == 0 && startsWith(current)) continue;` enforces that a letter that appears as the first character of any word or of the result cannot be 0 (prevents leading zeros).
* Assign digit: put into `charToDigit` and mark `usedDigits[digit]=true`.
* Recurse to next letter. If recursive call finds a solution, immediately return `true` (this program stops at first solution).
* If recursion fails, undo assignment (backtrack): remove the mapping and unmark the digit.
* If no digit works, return `false` to backtrack further.

```java
    private static boolean isValidSolution() {
        int sum = 0;
        for (String w : words) {
            sum += wordToNumber(w);
        }
        return sum == wordToNumber(result);
    }
```

* Converts each word to its numeric value under the current `charToDigit` mapping, sums the addends, and compares to numeric value of `result`. Returns `true` if equation holds.

```java
    private static int wordToNumber(String word) {
        int value = 0;
        for (char c : word.toCharArray()) {
            value = value * 10 + charToDigit.get(c);
        }
        return value;
    }
```

* Converts a word like `"SEND"` to `S*1000 + E*100 + N*10 + D` by repeated multiplication and addition using current mapping. Assumes every letter has an assigned digit (safe because it's called only after full assignment).

```java
    private static boolean startsWith(char c) {
        for (String w : words) if (w.charAt(0) == c) return true;
        return result.charAt(0) == c;
    }
```

* Returns `true` if `c` is the first letter of any addend or the result (used to block assignment of 0 to leading letters).

```java
    private static void printSolution() {
        System.out.println("Solution Found:");
        for (Map.Entry<Character, Integer> e : charToDigit.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
        System.out.println();

        for (String w : words) System.out.println(wordToNumber(w));
        System.out.println("+");
        System.out.println("-----");
        System.out.println(wordToNumber(result));
    }
}
```

* `printSolution` prints the mapping of letters to digits (in arbitrary `Map` entry order) and then prints numeric representations of the addends and the result in a simple vertical format.

---

# Data structures used (summary)

* `HashSet<Character> uniqueChars` — collects distinct letters.
* `char[] letters` — array of letters to assign (indexed for recursion).
* `HashMap<Character,Integer> charToDigit` — current partial/complete assignment.
* `boolean[] usedDigits` — tracks used digits 0..9.
* recursion stack (implicit call stack) to depth = number of unique letters.

---

# Correctness & constraints

* Enforces distinct digits by `usedDigits`.
* Enforces no leading zeros with `startsWith`.
* Checks final arithmetic correctness only when all letters are assigned.
* Stops at the first valid solution found (does not enumerate all solutions).

---

# Time complexity (detailed)

Let `n` be the number of distinct letters (here `n=8` for SEND+MORE=MONEY).

* The solver tries permutations of up to 10 digits taken `n` at a time. Number of assignments (worst-case) = permutations `P = 10P n = 10! / (10-n)!`.
* For each complete assignment the program computes numeric values (cost proportional to total letters length `L`, e.g. length of words and result). So total worst-case time is roughly `O(P * L)`.
* If you want a simpler bound: exponential in `n`, up to about `O(10^n)` in the naive sense, but the precise count is `10P n` permutations.

Concrete example: for `n = 8` (SEND+MORE=MONEY), permutations = `10*9*8*7*6*5*4*3 = 1,814,400`. Each complete assignment requires converting ~ (4+4+5)=13 letter positions → small constant cost. So total work is on the order of a few million small operations — practicable.

# Space complexity

* `charToDigit` size `O(n)` (map entries).
* `usedDigits` is `O(1)` (size 10).
* recursion depth = `n`, so call stack `O(n)`.
* Overall space = `O(n)` (plus small constants).

---

# Practical limitations & notes

* `letters` order is arbitrary because it's built from a `HashSet`. The search order affects speed: some orders are much faster (assigning the most constrained letters first prunes earlier).
* The program only checks arithmetic **after** all letters are assigned — it does not do partial arithmetic checks or column-wise carry propagation to prune the search earlier. That means it tries many full assignments unnecessarily.
* Numeric types: code uses `int`. For very large words this could overflow; for typical cryptarithms with ≤10 letters, `int` is fine. If you plan larger instances, use `long` or `BigInteger`.
* `printSolution()` prints mapping in arbitrary map order. You may want to sort keys for readable output.

---

# Improvements & heuristics to speed this up

If you want a faster, more scalable solver, consider:

1. **Order letters by constraint** — assign letters that appear in highest place-value positions (left-most) or have the most occurrences first. This often reduces branches drastically.
2. **Forward checking / partial evaluation** — after assigning some letters, test if the partial column sums (right to left) are consistent with possible carries. That prunes huge parts of the search. (Classic cryptarithm optimization.)
3. **Use carries and column-wise constraint propagation** — propagate carries from lower digits to higher digits early and prune contradictory partial assignments.
4. **Use permutations generator** (rather than trying digits 0..9 every time) or use combinatorial iterator for `10Pn`.
5. **Stop exploring digits that violate leading-zero or immediate modular constraints** (e.g., last digit: (D + E) % 10 must equal Y etc.).
6. **Use `State` data structures / arrays instead of map lookups** if you want micro-optimizations.

---

# Short pseudocode (concise)

```
letters = all unique letters in words and result
charToDigit = empty map
usedDigits = array[0..9] = false

function solve(index):
  if index == letters.length:
    if numericValue(words) sum equals numericValue(result):
      print solution; return true
    else return false

  letter = letters[index]
  for digit in 0..9:
    if !usedDigits[digit] and not (digit==0 and letter is leading):
      assign letter->digit; mark usedDigits[digit]=true
      if solve(index+1) return true
      unassign letter; mark usedDigits[digit]=false
  return false

call solve(0)
```

---

# Short final summary

* This is a straightforward backtracking solver for `SEND + MORE = MONEY`.
* It enumerates digit assignments (distinct digits, no leading zeros) and checks the sum only when all assignments are made.
* Complexity is factorial/permutation-based in the number of distinct letters (`10P n`), space is linear.
* Adding constraint propagation (column checks, carry handling, better letter ordering) will make it dramatically faster for larger puzzles.

If you want, I can:

* produce an optimized solver that does column-wise propagation and finds the solution much faster, or
* show the exact solution this program prints (I can run a logically equivalent solver here and display the mapping and summed values).

Which would you like next?


*/