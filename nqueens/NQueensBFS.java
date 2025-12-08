package N_Queens;

import java.util.*;

public class NQueensBFS {
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

    public static void solve(int n) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        State start = new State(0, new int[n]);
        queue.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            if (cur.row == n) {
                printPath(parent, cur.toString(), n);
                return;
            }

            List<State> nextStates = new ArrayList<>();

            for (int col = 0; col < n; col++) {
                if (isSafe(cur.queens, cur.row, col)) {
                    int[] newQueens = cur.queens.clone();
                    newQueens[cur.row] = col;
                    nextStates.add(new State(cur.row + 1, newQueens));
                }
            }

            for (State s : nextStates) {
                String key = s.toString();
                if (!visited.contains(key)) {
                    visited.add(key);
                    parent.put(key, cur.toString());
                    queue.add(s);
                }
            }
        }
        System.out.println("No solution found!");
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

    private static void printPath(Map<String, String> parent, String end, int n) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        System.out.println("Steps to solve (each step shows the board):");
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
        int n = 8;
        solve(n);
    }
}


/*
Here’s a **detailed explanation** of your `NQueensBFS` Java program, step by step, along with time/space complexity, data structures, problem statement, and pseudocode.

---

# **Problem Statement**

The **N-Queens Problem**: Place `n` queens on an `n × n` chessboard so that no two queens attack each other (no same row, column, or diagonal).

This program uses **Breadth-First Search (BFS)**:

* Explores all possible board configurations level by level.
* Each state represents a **partial placement** of queens (first `row` rows filled).
* BFS guarantees the **shortest path to solution** in terms of queen placements.

---

# **Line-by-Line Explanation**

```java
package N_Queens;
import java.util.*;
```

* Declares package and imports Java utilities (Queue, ArrayList, HashMap, HashSet, etc.).

```java
public class NQueensBFS {
```

* Top-level class for BFS-based N-Queens solver.

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

* `row`: number of rows already filled with queens.
* `queens[]`: array where `queens[i] = column` of queen in row `i`.
* Constructor **clones array** to avoid reference sharing.
* `toString()` returns a string representation of the board state up to current row (used in BFS bookkeeping).

---

### **BFS Solver**

```java
    public static void solve(int n) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
```

* `queue` = BFS frontier (FIFO).
* `visited` = keeps track of visited states to avoid revisiting duplicates.
* `parent` = keeps track of previous state for path reconstruction.

```java
        State start = new State(0, new int[n]);
        queue.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);
```

* Start state: row = 0 (no queens placed).
* Adds start state to BFS queue, marks visited, and sets parent to null.

---

### **Main BFS Loop**

```java
        while (!queue.isEmpty()) {
            State cur = queue.poll();
```

* Pop the front of the queue (FIFO order).

```java
            if (cur.row == n) {
                printPath(parent, cur.toString(), n);
                return;
            }
```

* Goal test: if `row == n`, all queens are placed → print the solution and terminate.

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

* Generate **successor states**: try placing a queen in every column of the current row.
* Use `isSafe()` to check that this placement does not conflict with previous queens.

```java
            for (State s : nextStates) {
                String key = s.toString();
                if (!visited.contains(key)) {
                    visited.add(key);
                    parent.put(key, cur.toString());
                    queue.add(s);
                }
            }
        }
```

* For each next state:

  * Skip if already visited.
  * Mark visited, set parent, and add to BFS queue.

```java
        System.out.println("No solution found!");
    }
```

* If queue empties without finding a solution, print failure message.

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

* Check for column conflict: `c == col`.
* Check for diagonal conflict: `|r-row| == |c-col|`.
* Returns `true` if placement is safe.

---

### **Path Reconstruction & Printing**

```java
    private static void printPath(Map<String, String> parent, String end, int n) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);
```

* Reconstructs the path from start → goal using parent map.
* Reverse list to print in order of steps.

```java
        System.out.println("Steps to solve (each step shows the board):");
        for (String step : path) {
            printStep(step, n);
            System.out.println();
        }
    }
```

* Prints each step (board configuration).

---

### **Board Printing**

```java
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
                if (r < queens.length && queens[r] == c) System.out.print("Q ");
                else System.out.print(". ");
            }
            System.out.println();
        }
    }
```

* Converts string representation of board → array.
* Prints `Q` for queen, `.` for empty cell.

---

### **Main Method**

```java
    public static void main(String[] args) {
        int n = 8;
        solve(n);
    }
```

* Solves 8-Queens problem using BFS.

---

# **Data Structures Used**

1. `Queue<State>` → BFS frontier.
2. `HashSet<String>` → visited states to avoid cycles.
3. `HashMap<String,String>` → parent map for path reconstruction.
4. `int[] queens` → stores queen column positions for each row.
5. `ArrayList<State>` → temporarily holds next states.

---

# **Time Complexity**

* Worst case: all possible placements of queens → O(n^n) (exponential).
* BFS explores level by level, pruning invalid placements with `isSafe()`.

---

# **Space Complexity**

* BFS queue can store up to `O(n^n)` states in worst case.
* `visited` and `parent` maps store all explored states → also O(n^n).

---

# **Pseudocode (short)**

```
function BFS_NQueens(n):
    start = empty board, row=0
    queue.enqueue(start)
    visited[start] = true
    parent[start] = null

    while queue not empty:
        current = queue.dequeue()
        if current.row == n:
            printPath(current)
            return
        for col in 0..n-1:
            if isSafe(current, row, col):
                next = placeQueen(current, row, col)
                if next not in visited:
                    visited[next] = true
                    parent[next] = current
                    queue.enqueue(next)
```

---

✅ **Summary**

* Solves N-Queens problem using **BFS**.
* Explores **all safe placements level by level**, guaranteeing minimal number of moves.
* Uses `State` objects, `Queue`, `HashSet`, and `HashMap`.
* Prints step-by-step board configuration.

---

I can also create a **visual diagram showing BFS tree traversal for n=4**, which makes the process much easier to understand.

Do you want me to do that?

*/