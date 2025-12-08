package CSP;

import java.util.*;

public class NQueensCSP {

    static int n = 8;
    static int[] queens = new int[n];

    public static void main(String[] args) {
        Arrays.fill(queens, -1);

        if (!solve(0)) {
            System.out.println("No solution exists!");
        }
    }

    private static boolean solve(int row) {
        if (row == n) {
            printSolution();
            return true;
        }

        for (int col = 0; col < n; col++) {
            if (isSafe(row, col)) {
                queens[row] = col;
                if (solve(row + 1)) return true;
                queens[row] = -1;
            }
        }
        return false;
    }

    private static boolean isSafe(int row, int col) {
        for (int r = 0; r < row; r++) {
            int c = queens[r];
            if (c == col || Math.abs(c - col) == Math.abs(r - row)) {
                return false;
            }
        }
        return true;
    }

    private static void printSolution() {
        System.out.println("Solution Found:");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (queens[r] == c) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}


/*
# Explanation — N-Queens backtracking Java program

Nice little program — it solves the classic **N-Queens** problem by backtracking and prints the first solution it finds for `n = 8`. I’ll explain what the code does line-by-line, then walk through the algorithm, data structures, complexity, and a short pseudocode summary.

---

# Problem statement (brief)

Place `n` queens on an `n × n` chessboard so that no two queens attack each other. A queen attacks along the same row, same column, and both diagonals. The program finds a configuration (here the first one it finds) where no two queens threaten each other.

---

# Line-by-line explanation

I'll quote small code fragments and explain each.

```java
package CSP;
```

* Declares the package name `CSP`. (Groups this class into that package; optional for explanation of the algorithm.)

```java
import java.util.*;
```

* Imports the Java utility package. This program uses `Arrays.fill` from `java.util.Arrays`.

```java
public class NQueensCSP {
```

* Declares a public class named `NQueensCSP`. Everything else is inside this class.

```java
    static int n = 8;
```

* A static integer `n` is the board size (number of queens). Default set to `8`. Change this value to solve other sizes.

```java
    static int[] queens = new int[n];
```

* An array `queens` of length `n`. `queens[row] = col` will store the column index of the queen placed in `row`.
* Using a single array to represent the board is common: one queen per row, so only need column positions.

```java
    public static void main(String[] args) {
        Arrays.fill(queens, -1);
```

* `main` is the program entry point.
* `Arrays.fill(queens, -1)` initializes every entry to `-1` meaning “no queen placed yet” for that row.

```java
        if (!solve(0)) {
            System.out.println("No solution exists!");
        }
    }
```

* Calls the recursive `solve` starting at row `0`.
* If `solve(0)` returns `false` (no solution found), prints `"No solution exists!"`.
* Note: `solve` returns `true` as soon as it finds a valid full arrangement, so this program prints the *first* solution it finds.

```java
    private static boolean solve(int row) {
        if (row == n) {
            printSolution();
            return true;
        }
```

* `solve(row)` attempts to place queens from `row` to `n-1`.
* Base case: if `row == n`, all rows have queens → a full valid solution. It calls `printSolution()` and returns `true` (signal success).

```java
        for (int col = 0; col < n; col++) {
            if (isSafe(row, col)) {
                queens[row] = col;
                if (solve(row + 1)) return true;
                queens[row] = -1;
            }
        }
        return false;
    }
```

* Loop over every column in the current `row`.
* For each `col`, check `isSafe(row, col)`:

  * If safe, place the queen by `queens[row] = col`.
  * Recursively attempt to solve the next row `solve(row+1)`.
  * If the recursive call returns `true` (solution found down the path), propagate `true` immediately so the program stops and prints the first solution.
  * Otherwise, backtrack: `queens[row] = -1` to remove the queen and try the next column.
* If no column leads to a solution for this `row`, return `false` to backtrack further.

```java
    private static boolean isSafe(int row, int col) {
        for (int r = 0; r < row; r++) {
            int c = queens[r];
            if (c == col || Math.abs(c - col) == Math.abs(r - row)) {
                return false;
            }
        }
        return true;
    }
```

* `isSafe` checks whether placing a queen at `(row, col)` conflicts with any previously placed queen in rows `0..row-1`.
* For each earlier row `r`, read its column `c = queens[r]`.

  * `c == col` → same column conflict.
  * `Math.abs(c - col) == Math.abs(r - row)` → same diagonal conflict (difference in columns equals difference in rows).
* If any conflict found, return `false`; otherwise `true`.

```java
    private static void printSolution() {
        System.out.println("Solution Found:");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (queens[r] == c) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
```

* `printSolution()` prints the board textually:

  * For each row `r`, prints `Q ` where a queen is located and `. ` for empty squares.
  * Produces an `n`-line ASCII board showing the solution.

---

# How the algorithm works (high level)

* The code uses **backtracking**. It places a queen row by row.
* For each row it tries each column; when a placement conflicts with previously placed queens it's rejected.
* If no column works for a row, it backtracks to the previous row and tries the next column there.
* Because `solve` returns `true` when the first complete solution is found, the program stops after printing that first solution.

---

# Data structures used

* `int[] queens` — linear array of length `n`. Index = row, value = column. Compact representation that enforces one queen per row implicitly.
* Recursion stack — used by the `solve` method to hold the current state (row depth).
* No extra hash sets or boolean arrays used here — `isSafe` checks conflicts by scanning previous rows each time.

---

# Time Complexity (analysis)

* Worst-case theoretical upper bound without pruning is (n^n) (n choices per row, depth n), but the `isSafe` constraints reduce choices.
* A commonly cited practical upper bound for backtracking with one queen per row (and forbidding columns) is about (O(n!)) because after placing a queen in a column, that column cannot be used again (approximately n choices, then n−1, etc.).
* **Precise complexity is complicated** and depends on pruning; for large `n` the search space still grows superpolynomially.
* For finding **one** solution, the algorithm may stop early — average cost much lower than worst case, depending on `n`.
* If you wanted **all** solutions, runtime would be proportional to the total number of valid placements explored; the number of solutions grows but not exponentially like (n^n).

Short summary:

* Worst-case: roughly (O(n!)) (often stated) or upper bound (O(n^n)) if you don't account for column pruning.
* Average/typical: much smaller due to pruning — but still exponential in `n`.

(When quoting complexity, note: backtracking problems like N-Queens generally have exponential time complexity.)

---

# Space Complexity

* `queens` array uses `O(n)` space.
* Recursion depth is at most `n` → recursion stack `O(n)`.
* So overall **space complexity = O(n)**.

---

# Variations / important notes

* This code **prints only the first solution** found because `solve` returns `true` immediately after `printSolution()`. To enumerate all solutions, remove the `return true` after the recursive call and instead let the recursion continue, and collect/print solutions (and adjust return logic).
* `isSafe` currently checks only previously placed rows (0..row-1). This is enough because we fill rows top-down and never place more than one queen per row.
* For large `n`, improvements include:

  * Using boolean arrays for columns and both diagonals to make `isSafe` O(1) instead of scanning previous rows (this reduces time constant and often improves speed).
  * Bitmasking techniques (very common for speed and small memory).
  * Heuristics (ordering columns, constraint propagation) to reduce branching.

---

# Short pseudocode

```
function solve(row):
    if row == n:
        print board
        return true    // stop after first solution

    for col from 0 to n-1:
        if isSafe(row, col):
            place queen at (row, col)
            if solve(row + 1) == true:
                return true
            remove queen from (row, col)
    return false

function isSafe(row, col):
    for r from 0 to row-1:
        c = queens[r]
        if c == col or abs(c - col) == abs(r - row): return false
    return true
```

---

# Example behaviour

* With `n = 8`, the program will find and print one valid 8×8 placement (there are 92 distinct solutions for n=8 in total; this program prints just one).
* Output format:

```
Solution Found:
. . Q . . . . .
. . . . Q . . .
... etc ...
```

---

# Summary (quick bullets)

* Problem: place `n` queens so none attack each other.
* Approach: backtracking, place queen row-by-row, check conflicts, backtrack on failure.
* Data structure: `int[] queens` (one entry per row).
* Time complexity: exponential (commonly stated ~`O(n!)`, upper bound `O(n^n)`); exact depends on pruning.
* Space complexity: `O(n)` (array + recursion stack).
* Code prints the **first** solution found; modify it to enumerate all.

---

If you want, I can:

* Show the program modified to **list all solutions** and count them,
* Replace `isSafe` with O(1) checks using boolean arrays or bitmasks for faster performance,
* Or run through a concrete example step-by-step for `n = 4` to demonstrate how backtracking proceeds.

Which of those would be most helpful next?

*/