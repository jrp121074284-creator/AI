package N_Queens;

import java.util.*;

public class NQueensDLS {
    static class State {
        int row;       
        int[] queens;   

        State(int row, int[] queens) {
            this.row = row;
            this.queens = queens.clone();
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOf(queens, row));
        }
    }

    static int n = 4; 
    static Set<String> visited = new HashSet<>();
    static Map<String, String> parent = new HashMap<>();
    static int depthLimit = 6;

    public static boolean dls(State cur, int depth) {
        
        if (depth > depthLimit) {
            return false;
        }

        if (cur.row == n) {
            printPath(cur.toString());
            return true;
        }

        visited.add(cur.toString());

        List<State> nextStates = new ArrayList<>();
        for (int col = 0; col < n; col++) {
            if (isSafe(cur.queens, cur.row, col)) {
                int[] newQueens = cur.queens.clone();
                newQueens[cur.row] = col;
                nextStates.add(new State(cur.row + 1, newQueens));
            }
        }

        for (State s : nextStates) {
            if (!visited.contains(s.toString())) {
                parent.put(s.toString(), cur.toString());
                if (dls(s, depth + 1)) return true;
            }
        }
        return false;
    }

    private static boolean isSafe(int[] queens, int row, int col) {
        for (int r = 0; r < row; r++) {
            int c = queens[r];
            if (c == col || Math.abs(c - col) == Math.abs(r - row)) {
                return false;
            }
        }
        return true;
    }

    private static void printPath(String end) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        System.out.println("Steps to solve (Depth Limited Search):");
        for (String step : path) {
            printStep(step, n);
            System.out.println();
        }
    }

    private static void printStep(String step, int n) {
        step = step.replaceAll("\\[|\\]|\\s", "");
        if (step.isEmpty()) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    System.out.print(". ");
                }
                System.out.println();
            }
            return;
        }
        String[] parts = step.split(",");
        int[] queens = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            queens[i] = Integer.parseInt(parts[i]);
        }

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (r < queens.length && queens[r] == c) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        State start = new State(0, new int[n]);
        parent.put(start.toString(), null);

        if (!dls(start, 0)) {
            System.out.println("No solution found within depth limit " + depthLimit);
        }
    }
}


/*
Here’s a **detailed explanation** of your `NQueensDLS` Java program, including problem statement, line-by-line explanation, data structures, time and space complexity, and pseudocode.

---

# **Problem Statement**

The **N-Queens Problem**: Place `n` queens on an `n × n` chessboard so that no two queens attack each other (no same row, column, or diagonal).

This program uses **Depth-Limited Search (DLS)**:

* It’s a **DFS variant** that explores solutions to a maximum depth (`depthLimit`).
* Each state represents a **partial placement** of queens (from row 0 to `row-1`).
* Helps avoid infinite recursion and limits search to a manageable depth.

---

# **Line-by-Line Explanation**

```java
package N_Queens;
import java.util.*;
```

* Declares package and imports Java utilities (ArrayList, HashSet, HashMap, etc.).

```java
public class NQueensDLS {
```

* Top-level class for DLS-based N-Queens solver.

---

### **State Representation**

```java
    static class State {
        int row;       
        int[] queens;   

        State(int row, int[] queens) {
            this.row = row;
            this.queens = queens.clone();
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOf(queens, row));
        }
    }
```

* `row`: the next row to place a queen.
* `queens[]`: array storing the column index of the queen in each row.
* Constructor **clones array** to prevent overwriting previous states.
* `toString()` returns a string representation of the board up to `row`.

---

### **Global Variables**

```java
    static int n = 4; 
    static Set<String> visited = new HashSet<>();
    static Map<String, String> parent = new HashMap<>();
    static int depthLimit = 6;
```

* `n`: size of the board (4×4 here).
* `visited`: stores visited states to avoid redundant exploration.
* `parent`: stores the parent of each state for path reconstruction.
* `depthLimit`: maximum depth the search will go (limits DFS recursion).

---

### **Depth-Limited Search Function**

```java
    public static boolean dls(State cur, int depth) {
        if (depth > depthLimit) return false;
```

* Stops recursion if `depth` exceeds `depthLimit`.

```java
        if (cur.row == n) {
            printPath(cur.toString());
            return true;
        }
```

* Goal test: all `n` queens placed → print solution path and return true.

```java
        visited.add(cur.toString());
```

* Mark current state as visited.

```java
        List<State> nextStates = new ArrayList<>();
        for (int col = 0; col < n; col++) {
            if (isSafe(cur.queens, cur.row, col)) {
                int[] newQueens = cur.queens.clone();
                newQueens[cur.row] = col;
                nextStates.add(new State(cur.row + 1, newQueens));
            }
        }
```

* Generate **next states**: try placing a queen in each column of current row if safe.

```java
        for (State s : nextStates) {
            if (!visited.contains(s.toString())) {
                parent.put(s.toString(), cur.toString());
                if (dls(s, depth + 1)) return true;
            }
        }
        return false;
    }
```

* For each next state:

  * Skip if already visited.
  * Set parent pointer.
  * Recur with `depth + 1`.
* Return false if no solution found.

---

### **Safety Check**

```java
    private static boolean isSafe(int[] queens, int row, int col) {
        for (int r = 0; r < row; r++) {
            int c = queens[r];
            if (c == col || Math.abs(c - col) == Math.abs(r - row)) {
                return false;
            }
        }
        return true;
    }
```

* Checks whether placing a queen in `(row,col)` conflicts with previously placed queens.
* Column conflict: `c == col`.
* Diagonal conflict: `|c - col| == |r - row|`.

---

### **Path Reconstruction & Printing**

```java
    private static void printPath(String end) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
```

* Reconstructs solution path from `parent` map.
* Reverse to show steps from start → goal.

```java
        System.out.println("Steps to solve (Depth Limited Search):");
        for (String step : path) {
            printStep(step, n);
            System.out.println();
        }
    }
```

* Prints each step as a chessboard.

---

### **Board Printing**

```java
    private static void printStep(String step, int n) {
        step = step.replaceAll("\\[|\\]|\\s", "");
        if (step.isEmpty()) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) System.out.print(". ");
                System.out.println();
            }
            return;
        }
        String[] parts = step.split(",");
        int[] queens = new int[parts.length];
        for (int i = 0; i < parts.length; i++) queens[i] = Integer.parseInt(parts[i]);

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (r < queens.length && queens[r] == c) System.out.print("Q ");
                else System.out.print(". ");
            }
            System.out.println();
        }
    }
```

* Converts string to array.
* Prints `Q` for queens, `.` for empty cells.
* Handles partially filled boards.

---

### **Main Method**

```java
    public static void main(String[] args) {
        State start = new State(0, new int[n]);
        parent.put(start.toString(), null);

        if (!dls(start, 0)) {
            System.out.println("No solution found within depth limit " + depthLimit);
        }
    }
```

* Initializes start state (row=0, empty board).
* Runs DLS from start at depth 0.
* Prints solution or failure message.

---

# **Data Structures Used**

1. `State` objects → store row number and queen positions.
2. `HashSet<String>` → stores visited states.
3. `HashMap<String, String>` → parent map for reconstructing solution path.
4. `ArrayList<State>` → stores next possible states.
5. `int[] queens` → stores column positions of queens.

---

# **Time Complexity**

* Worst-case: **O(b^d)**

  * `b = n` (branching factor: number of columns per row)
  * `d = depthLimit` (max depth).
* DLS explores up to `depthLimit` levels, pruning invalid positions with `isSafe()`.

---

# **Space Complexity**

* **O(b × d)** for recursion stack.
* **O(number of visited states)** → up to `O(n^d)` for storing visited and parent maps.

---

# **Pseudocode (Short)**

```
function DLS_NQueens(current, depth):
    if depth > depthLimit: return false
    if current.row == n: printPath(current); return true

    visited.add(current)
    for col in 0..n-1:
        if isSafe(current, row, col):
            next = placeQueen(current, row, col)
            if next not visited:
                parent[next] = current
                if DLS_NQueens(next, depth + 1) return true
    return false
```

---

✅ **Summary**

* Solves N-Queens using **Depth-Limited Search (DLS)**.
* Uses recursion, visited set, and parent map for path reconstruction.
* Prints step-by-step board configuration.
* Efficient for small `n` and depth-limited scenarios.

---

If you want, I can also **draw the recursion tree for n=4 showing depth-limited exploration**, which makes DLS logic very clear visually.

Do you want me to do that?
 */