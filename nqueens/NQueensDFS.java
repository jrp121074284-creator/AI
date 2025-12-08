package N_Queens;

import java.util.*;

public class NQueensDFS {
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

    public static boolean dfs(State cur) {
        
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
                if (dfs(s)) return true; 
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
        State start = new State(0, new int[n]);
        parent.put(start.toString(), null);
        if (!dfs(start)) {
            System.out.println("No solution found!");
        }
    }
}


/*
Sure! Let’s go **step by step** to explain this **N-Queens solution using DFS** in Java.

---

# **Problem Statement**

**N-Queens Problem:**

* Place `N` queens on an `N x N` chessboard such that **no two queens attack each other**.
* A queen can attack another in the **same row, same column, or diagonals**.
* The goal is to find at least **one valid arrangement** of the queens.

This code uses **Depth-First Search (DFS)** to explore all possible placements **row by row** until a solution is found.

---

# **Code Explanation**

```java
package N_Queens;

import java.util.*;
public class NQueensDFS {
```

* Package and imports.
* Class `NQueensDFS` implements DFS-based N-Queens solution.

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
  2. `queens[]`: stores the **column index** of the queen in each filled row.
* `toString()`: used for **tracking visited states** and storing **parent information**.

---

### **Global Variables**

```java
    static int n = 4; 
    static Set<String> visited = new HashSet<>();
    static Map<String, String> parent = new HashMap<>();
```

* `n`: board size (4x4 in this example).
* `visited`: avoids revisiting the same state.
* `parent`: stores the previous state for reconstructing **the solution path**.

---

### **DFS Function**

```java
    public static boolean dfs(State cur) {
        if (cur.row == n) {
            printPath(cur.toString());
            return true;
        }
```

* **Base case:** if all `n` rows are filled, we found a solution.
* Calls `printPath()` to show the solution.

```java
        visited.add(cur.toString());

        List<State> nextStates = new ArrayList<>();
        for (int col = 0; col < n; col++) {
            if (isSafe(cur.queens, cur.row, col)) {
                int[] newQueens = cur.queens.clone();
                newQueens[cur.row] = col;
                nextStates.add(new State(cur.row + 1, newQueens));
            }
        }
```

* For the current row:

  1. Try placing a queen in each column.
  2. `isSafe()` checks **no conflicts with previously placed queens**.
  3. If safe, create a **new state** and increment the row.

```java
        for (State s : nextStates) {
            if (!visited.contains(s.toString())) {
                parent.put(s.toString(), cur.toString());
                if (dfs(s)) return true; 
            }
        }
        return false;
    }
```

* Recursively call `dfs()` for each **safe next state**.
* Record **parent mapping** to reconstruct solution.
* Returns `true` if a solution is found.

---

### **Check Safety**

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

* Ensures no **queen in the same column or diagonal**.

---

### **Print Solution Path**

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

* Reconstructs **the sequence of board states** from `parent` map.
* Reverses the list to print in **step-by-step order**.

```java
        System.out.println("Steps to solve (each step shows the board):");
        for (String step : path) {
            printStep(step, n);
            System.out.println();
        }
    }
```

* Prints each state using `printStep()`.

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
```

* Converts string representation back to **queen positions**.

```java
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

* Prints **N x N chessboard** with `Q` for queens and `.` for empty spaces.

---

### **Main Method**

```java
    public static void main(String[] args) {
        State start = new State(0, new int[n]);
        parent.put(start.toString(), null);
        if (!dfs(start)) {
            System.out.println("No solution found!");
        }
    }
```

* Starts DFS from an **empty board** (`row = 0`).
* Adds `null` parent for the starting state.
* Prints "No solution found" if DFS fails.

---

# **Data Structures Used**

| Structure                   | Purpose                                           |
| --------------------------- | ------------------------------------------------- |
| `State` class               | Represents a partial board configuration.         |
| `Set<String> visited`       | Avoid revisiting same board configuration.        |
| `Map<String,String> parent` | Stores parent state to reconstruct solution path. |
| `int[] queens`              | Stores column indices of queens in each row.      |

---

# **Time & Space Complexity**

* **Time complexity (DFS):**

  * Worst case: O(N!) (placing N queens row by row, exploring all possibilities).
* **Space complexity:**

  * O(N!) for storing `visited` states and `parent` mapping.
  * Call stack depth: O(N) (maximum recursion depth).

> DFS explores **deeply first**, so may find a solution faster than BFS for small N, but may not be optimal for all solutions.

---

# **Pseudo Code (Short)**

```
function DFS(state):
    if state.row == N:
        print solution path
        return true

    mark state as visited
    for col in 0..N-1:
        if placing queen at (row,col) is safe:
            nextState = copy state with queen at (row,col)
            parent[nextState] = state
            if DFS(nextState) return true
    return false
```

---

# ✅ **Summary**

* Solves **N-Queens using Depth-First Search**.
* Uses `visited` to avoid repeated states.
* Uses `parent` to print the solution **step by step**.
* Prints **visual chessboard** for each step.
* Time complexity: O(N!), Space complexity: O(N!) for visited + O(N) recursion stack.

---

If you want, I can make a **side-by-side comparison of DFS vs BestFS for N-Queens** showing **how search order and efficiency differ**.

Do you want me to do that?
 */