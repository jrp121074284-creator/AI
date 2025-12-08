package N_Queens;

import java.util.*;

public class NQueensAstar {

    static class State {
        int row;       
        int[] queens;  

        State(int row, int[] queens) {
            this.row = row;
            this.queens = queens.clone();
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOf(queens, row));
        }
    }

    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }

    private static int heuristic(State s) {
        int conflicts = 0;
        for (int i = 0; i < s.row; i++) {
            for (int j = i + 1; j < s.row; j++) {
                if (s.queens[i] == s.queens[j] ||
                    Math.abs(s.queens[i] - s.queens[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    private static int getPriority(State s, Map<String, Integer> gValues) {
        return gCost(s.toString(), gValues) + heuristic(s);
    }

    public static void aStarSearch(int n) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();

        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues), getPriority(s2, gValues));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);

        State start = new State(0, new int[n]);
        pq.add(start);
        parent.put(start.toString(), null);
        gValues.put(start.toString(), 0);
        visited.add(start.toString());
        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (current.row == n) {
                printSolutionPath(parent, current.toString(), n);
                System.out.println("Total steps (cost): " + gCost(current.toString(), gValues));
                return;
            }
            List<State> next = new ArrayList<>();
        
            for (int col = 0; col < n; col++) {
                if (isSafe(current.queens, current.row, col)) {
                    int[] newQueens = current.queens.clone();
                    newQueens[current.row] = col;
                    next.add(new State(current.row + 1, newQueens));
                }
            }

            for(State nextState : next){
                
                    int tentativeG = gCost(current.toString(), gValues) + 1;

                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parent.put(nextState.toString(), current.toString());
                        pq.add(nextState);
                    }
            }
        }

        System.out.println("No solution found.");
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

    private static void printSolutionPath(Map<String, String> parent, String end, int n) {
        List<String> path = new ArrayList<>();
        String cur = end;

        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search):");
        for (String step : path) {
            printBoard(step, n);
            System.out.println();
        }
    }

    private static void printBoard(String step, int n) {
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
        aStarSearch(n);
    }
}


/*
Here’s a detailed explanation of your **`NQueensAstar`** Java program. I’ll break it down step by step, explain the data structures, time/space complexity, problem statement, and provide pseudocode.

---

# **Problem Statement**

The **N-Queens problem** asks: place `n` queens on an `n×n` chessboard such that no two queens threaten each other (i.e., no two queens share the same row, column, or diagonal).

This program uses **A* Search**:

* Each state represents a partially filled board.
* `g(n)` = number of queens placed so far.
* `h(n)` = number of conflicts between placed queens (heuristic).
* Priority queue selects states with minimal `f(n) = g(n) + h(n)`.

Goal: place all `n` queens safely on the board.

---

# **Line-by-line explanation**

```java
package N_Queens;
import java.util.*;
```

* Declares package and imports Java utilities (ArrayList, HashMap, HashSet, PriorityQueue, etc.)

```java
public class NQueensAstar {
```

* Top-level class for A* search on N-Queens.

---

### **State representation**

```java
    static class State {
        int row;       
        int[] queens;  

        State(int row, int[] queens) {
            this.row = row;
            this.queens = queens.clone();
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOf(queens, row));
        }
    }
```

* `row` = number of queens placed so far.
* `queens[]` = array where `queens[i] = column` of queen in row `i`.
* Constructor clones array to avoid shared references.
* `toString()` returns partial board configuration up to current row (used for `gValues`, `parent`, and visited states).

---

### **Cost and priority functions**

```java
    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }
```

* Returns the path cost `g(n)` of a state (number of moves/queens placed so far).

```java
    private static int heuristic(State s) {
        int conflicts = 0;
        for (int i = 0; i < s.row; i++) {
            for (int j = i + 1; j < s.row; j++) {
                if (s.queens[i] == s.queens[j] ||
                    Math.abs(s.queens[i] - s.queens[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }
```

* `heuristic(s)` counts number of conflicts among already placed queens.
* Conflicts:

  * Same column: `s.queens[i] == s.queens[j]`
  * Same diagonal: `|i-j| == |queens[i]-queens[j]|`

```java
    private static int getPriority(State s, Map<String, Integer> gValues) {
        return gCost(s.toString(), gValues) + heuristic(s);
    }
```

* A* priority: `f(n) = g(n) + h(n)`.

---

### **A* Search algorithm**

```java
    public static void aStarSearch(int n) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();
```

* `visited` = states already processed (avoids duplicates).
* `parent` = stores predecessor for path reconstruction.
* `gValues` = stores path cost `g(n)` for each state.

```java
        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues), getPriority(s2, gValues));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);
```

* Priority queue ordered by `f(n)` (A* behavior).

```java
        State start = new State(0, new int[n]);
        pq.add(start);
        parent.put(start.toString(), null);
        gValues.put(start.toString(), 0);
        visited.add(start.toString());
```

* Start state: row 0, empty board.
* `g(start) = 0`, no parent.

---

### **Main loop**

```java
        while (!pq.isEmpty()) {
            State current = pq.poll();
```

* Pop state with minimum `f(n)` from priority queue.

```java
            if (current.row == n) {
                printSolutionPath(parent, current.toString(), n);
                System.out.println("Total steps (cost): " + gCost(current.toString(), gValues));
                return;
            }
```

* Goal test: all `n` queens placed (`row == n`).
* If reached, print solution and cost.

```java
            List<State> next = new ArrayList<>();
        
            for (int col = 0; col < n; col++) {
                if (isSafe(current.queens, current.row, col)) {
                    int[] newQueens = current.queens.clone();
                    newQueens[current.row] = col;
                    next.add(new State(current.row + 1, newQueens));
                }
            }
```

* Generate successors: place a queen in **current row** in all safe columns.
* `isSafe` ensures no conflicts with previous queens.

```java
            for(State nextState : next){
                    int tentativeG = gCost(current.toString(), gValues) + 1;
                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parent.put(nextState.toString(), current.toString());
                        pq.add(nextState);
                    }
            }
```

* Update `g(n)` if this path is cheaper, record parent, and add to priority queue.

---

### **Safety check**

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

* Check whether placing a queen at `(row,col)` conflicts with any previous queen:

  * Same column
  * Same diagonal

---

### **Print solution path**

```java
    private static void printSolutionPath(Map<String, String> parent, String end, int n) {
        List<String> path = new ArrayList<>();
        String cur = end;

        while (cur != null) {
            path.add(cur);
            cur = parent.get(cur);
        }

        Collections.reverse(path);
```

* Reconstruct the path from start → goal using parent map.

```java
        System.out.println("Steps to solve (A* Search):");
        for (String step : path) {
            printBoard(step, n);
            System.out.println();
        }
    }
```

* Print each board configuration along the path.

```java
    private static void printBoard(String step, int n) {
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

* Converts `state.toString()` back to array.
* Prints board using `'Q'` for queens, `'.'` for empty.

```java
    public static void main(String[] args) {
        int n = 4;
        aStarSearch(n);
    }
}
```

* Runs the A* search for `n=4`.

---

# **Data structures used**

1. **State class** → stores board state and row info.
2. **PriorityQueue<State>** → A* frontier ordered by `f(n) = g + h`.
3. **HashSet<String> visited** → tracks visited states.
4. **HashMap<String,String> parent** → reconstruct path.
5. **HashMap<String,Integer> gValues** → stores path cost `g(n)`.
6. **ArrayList<State> next** → stores candidate successors.

---

# **Time Complexity**

* Worst-case: generate all possible placements of `n` queens row by row.
* Rough bound:
  [
  O(n^n)
  ]
* Heuristic helps prune many invalid states early, improving practical performance.

---

# **Space Complexity**

* `O(n^2)` for `State` copies and `visited` set is very small in practice for moderate `n` (~10).
* Priority queue can grow to `O(n^n)` worst case.
* Overall: **exponential in `n`**.

---

# **Pseudocode (short)**

```
AStarSearch(n):
    start = empty board, row=0
    frontier = priority queue ordered by f(n)=g+h
    parent[start] = null
    g[start] = 0

    while frontier not empty:
        current = frontier.pop()
        if current.row == n:
            print path using parent
            return

        for col in 0..n-1:
            if isSafe(current, row, col):
                next = place queen at (row, col)
                tentativeG = g[current]+1
                if tentativeG < g[next]:
                    g[next] = tentativeG
                    parent[next] = current
                    frontier.add(next)
```

---

✅ **Summary**

* Solves N-Queens using **A* Search** with `f(n) = g+h`.
* `g` = number of queens placed, `h` = number of conflicts among placed queens.
* Uses **priority queue** for frontier, **HashMap** for path reconstruction, and **HashSet** for visited states.
* Finds a solution efficiently compared to naive DFS/backtracking, especially for moderate `n`.

---

If you want, I can also draw a **visual example for `n=4` showing the A* search tree and path**. This makes it very easy to understand.

Do you want me to do that?
 */