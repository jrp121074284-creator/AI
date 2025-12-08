package N_Queens;

import java.util.*;

public class NQueensBestFS {
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
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s)));

        State start = new State(0, new int[n]);
        pq.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);

        while (!pq.isEmpty()) {
            State cur = pq.poll();

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
                    pq.add(s);
                }
            }
        }
        System.out.println("No solution found!");
    }

    static int heuristic(State s) {
        int conflicts = 0;
        for (int i = 0; i < s.row; i++) {
            for (int j = i + 1; j < s.row; j++) {
                if (s.queens[i] == s.queens[j] || Math.abs(s.queens[i] - s.queens[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
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

        System.out.println("Steps to solve (BestFS order, with boards):");
        
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
        int n = 4;
        solve(n);
    }
}

/*
Sure! Let’s go **step by step** to explain this **Best-First Search (BestFS) solution for the N-Queens problem** in detail.

---

# **Problem Statement**

**N-Queens Problem:**

* Place `N` queens on an `N x N` chessboard such that **no two queens attack each other**.
* A queen can attack any other queen in the **same row, column, or diagonal**.
* The goal is to find one or more valid placements of the queens.

This implementation uses **Best-First Search (heuristic-based search)** to guide the search toward promising states with fewer conflicts.

---

# **Code Explanation**

```java
package N_Queens;
import java.util.*;
public class NQueensBestFS {
```

* Package and imports.
* Class `NQueensBestFS` implements the **Best-First Search** solution.

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

* `State` represents a **partial solution**:

  1. `row`: how many rows have been filled with queens.
  2. `queens[]`: column indices of queens placed in previous rows.
* `toString()`: returns a string for **visited checking** and `parent` mapping.

---

### **Solve Function (BestFS)**

```java
    public static void solve(int n) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
```

* `visited`: prevents revisiting the same state.
* `parent`: stores parent states for **reconstructing the solution path**.

```java
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s)));
```

* Priority queue to always select the state with **minimum heuristic value** (fewer conflicts).

```java
        State start = new State(0, new int[n]);
        pq.add(start);
        visited.add(start.toString());
        parent.put(start.toString(), null);
```

* Start with an **empty board** (`row=0`).

---

### **BestFS Loop**

```java
        while (!pq.isEmpty()) {
            State cur = pq.poll();
            if (cur.row == n) { 
                printPath(parent, cur.toString(), n);
                return;
            }
```

* Pop the **state with lowest heuristic value**.
* If `row == n`, all rows are filled → solution found.

---

### **Generate Next States**

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

* For current row, try placing a queen in every column:

  * `isSafe()` checks **no conflicts** with previously placed queens.
  * If safe, create a **new state** with the queen placed.

---

### **Add Next States to Priority Queue**

```java
            for (State s : nextStates) {
                String key = s.toString();
                if (!visited.contains(key)) {
                    visited.add(key);
                    parent.put(key, cur.toString());
                    pq.add(s);
                }
            }
        }
        System.out.println("No solution found!");
    }
```

* For each new state:

  * Check **visited** to avoid loops.
  * Record **parent** for path reconstruction.
  * Add to **priority queue** to process based on heuristic.

---

### **Heuristic Function**

```java
    static int heuristic(State s) {
        int conflicts = 0;
        for (int i = 0; i < s.row; i++) {
            for (int j = i + 1; j < s.row; j++) {
                if (s.queens[i] == s.queens[j] || Math.abs(s.queens[i] - s.queens[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }
```

* Counts **pairwise conflicts** among queens in the partially filled board.
* Used by **BestFS** to prioritize states with **fewer conflicts**.

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

* Ensures no **two queens attack each other** in the current partial placement.

---

### **Print Solution Path**

```java
    private static void printPath(Map<String, String> parent, String end, int n) {
        List<String> path = new ArrayList<>();
        String cur = end;
        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }
        Collections.reverse(path);

        System.out.println("Steps to solve (BestFS order, with boards):");
        for (String step : path) {
            printStep(step, n);
            System.out.println();
        }
    }
```

* Reconstructs solution from `parent` map.
* Calls `printStep()` to display **board visually**.

---

### **Print Board**

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
                if (r < queens.length && queens[r] == c) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
```

* Converts `step` string to **queen positions**.
* Prints the **N x N chessboard** showing queens (`Q`) and empty squares (`.`).

---

### **Main Function**

```java
    public static void main(String[] args) {
        int n = 4;
        solve(n);
    }
```

* Sets `n = 4` and calls `solve()` to find **one solution** for 4-Queens.

---

# **Data Structures Used**

| Structure                   | Purpose                                               |
| --------------------------- | ----------------------------------------------------- |
| `State` class               | Represents a board configuration (partial solution).  |
| `PriorityQueue<State>`      | Select next state with **lowest heuristic** (BestFS). |
| `Set<String> visited`       | Avoid revisiting same state.                          |
| `Map<String,String> parent` | Stores parent for reconstructing the path.            |
| `int[] queens`              | Holds column positions of queens per row.             |

---

# **Time & Space Complexity**

* **State space:** `N!` (number of ways to place N queens row by row).
* **BestFS time complexity:** O(N!) in worst case (depends on heuristic efficiency).
* **Space complexity:**

  * `PriorityQueue` stores partial states → O(N!);
  * `visited` and `parent` maps → O(N!).

> Using the heuristic reduces the **effective search space**, making BestFS faster than pure DFS.

---

# **Pseudo Code (Short)**

```
function BestFS(n):
    start = empty board
    pq = priority queue ordered by heuristic
    visited = {}
    parent = {}
    add start to pq

    while pq not empty:
        cur = pq.poll()
        if cur.row == n:
            print solution path
            return

        for col in 0..n-1:
            if placing queen at (cur.row, col) is safe:
                nextState = copy cur + queen at (row,col)
                if nextState not visited:
                    mark visited
                    parent[nextState] = cur
                    add nextState to pq
```

---

# ✅ **Summary**

* Solves **N-Queens using Best-First Search**.
* Uses **heuristic** to prioritize states with fewer conflicts.
* Maintains visited states and parent mapping to reconstruct solution.
* Efficient compared to naive DFS for larger `N` due to heuristic guidance.

---

If you want, I can also **draw a small example of BestFS search tree for N=4** to show exactly how the heuristic guides the search.

Do you want me to do that?
 */