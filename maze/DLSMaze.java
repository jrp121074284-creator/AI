package Maze;

import java.util.*;

public class DLSMaze {

    static class State {
        int row;
        int col;

        State(int r, int c) {
            this.row = r;
            this.col = c;
        }

        @Override
        public String toString() {
            return row + "," + col;
        }
    }

    static int[][] maze = new int[][] {
        {0, 0, 1, 0},
        {1, 0, 1, 0},
        {0, 0, 0, 0},
        {1, 1, 0, 1},
        {0, 0, 0, 0}
    };

    static int goalRow = maze.length - 1;
    static int goalCol = maze[0].length - 1;


    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int depthLimit = 50;  


    public static boolean dls(State currentState, int depth) {

        if (depth > depthLimit) {
            return false;
        }

        if (currentState.row == goalRow && currentState.col == goalCol) {
            printSolutionPath(currentState.toString());
            return true;
        }

        visitedStates.add(currentState.toString());

        int[][] moves = {
            {-1, 0},   // up
            {1, 0},    // down
            {0, -1},   // left
            {0, 1}     // right
        };

        for (int[] mv : moves) {

            int newR = currentState.row + mv[0];
            int newC = currentState.col + mv[1];

            State nextState = new State(newR, newC);

            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());

                if (dls(nextState, depth + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isValidState(State s) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (s.row < 0 || s.col < 0 || s.row >= rows || s.col >= cols)
            return false;

        if (maze[s.row][s.col] == 1)
            return false;

        return true;
    }

    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (Depth Limited Search on Maze):");
        for (String step : path) {
            String[] parts = step.split(",");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);

            System.out.println("Position -> (" + r + ", " + c + ")");
        }
    }

    public static void main(String[] args) {

        State start = new State(0, 0);
        parentMap.put(start.toString(), null);

        if (!dls(start, 0)) {
            System.out.println("No path found within depth limit.");
        }
    }
}

/*

# Overview — what this program does (short)

This Java program implements **Depth-Limited Search (DLS)** on a 2-D grid maze.
Start = `(0,0)`. Goal = bottom-right cell. `0` means walkable, `1` means blocked. The search explores neighbors (up, down, left, right) recursively, but stops recursing when a global `depthLimit` is exceeded. If it finds the goal it reconstructs the path using a `parentMap` and prints it.

---

# Line-by-line explanation (detailed)

I'll explain each logical block and every important line so you can map it directly to the code you posted.

### File header / imports

```java
package Maze;
import java.util.*;
```

* Declares the class package `Maze`.
* Imports `java.util.*` (List, Map, HashMap, Set, HashSet, ArrayList, Collections, etc.) used below.

---

### Class and nested State

```java
public class DLSMaze {

    static class State {
        int row;
        int col;

        State(int r, int c) {
            this.row = r;
            this.col = c;
        }

        @Override
        public String toString() {
            return row + "," + col;
        }
    }
```

* `public class DLSMaze` — top-level class for the program.
* `static class State` — holds a coordinate pair `(row, col)`.

  * `State(int r, int c)` — constructor storing row and column.
  * `toString()` — returns `"row,col"`. That string is used as keys in `visitedStates` and `parentMap`. (Note: `State` does **not** override `equals`/`hashCode`, so the code uses strings as keys.)

---

### Maze definition and goal coordinates

```java
    static int[][] maze = new int[][] {
        {0, 0, 1, 0},
        {1, 0, 1, 0},
        {0, 0, 0, 0},
        {1, 1, 0, 1},
        {0, 0, 0, 0}
    };

    static int goalRow = maze.length - 1;
    static int goalCol = maze[0].length - 1;
```

* `maze` is a 5×4 2D int array where `0` = free and `1` = blocked.
* `goalRow` and `goalCol` are set to the indices of the bottom-right cell (`maze.length - 1`, `maze[0].length - 1`).

---

### Global search state

```java
    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int depthLimit = 50;  
```

* `visitedStates` — a global `HashSet` storing string keys (`"r,c"`) of states that have been visited. It prevents re-visiting nodes. (Important behavior notes below.)
* `parentMap` — maps a state's string key to its parent's string key; used for reconstructing the path after a goal is found.
* `depthLimit` — maximum recursion depth allowed; here set to 50 (changeable).

---

### The DLS recursive function — signature and depth guard

```java
    public static boolean dls(State currentState, int depth) {

        if (depth > depthLimit) {
            return false;
        }
```

* `dls(State currentState, int depth)` — recursive depth-limited search function.
* First check: if the current `depth` is greater than `depthLimit`, the function returns `false` (cutoff at this branch). Note: this implementation treats a cutoff as a plain failure (doesn't distinguish cutoff vs true failure).

---

### Goal test

```java
        if (currentState.row == goalRow && currentState.col == goalCol) {
            printSolutionPath(currentState.toString());
            return true;
        }
```

* If `currentState` equals goal coordinates, reconstruct & print the path using `printSolutionPath(...)` and return `true`. This signals success and unwinds recursion.

---

### Mark visited

```java
        visitedStates.add(currentState.toString());
```

* Add the current state's key `"r,c"` to the global `visitedStates`. This prevents future recursive calls from entering the same cell again.

**Important behavior:** `visitedStates` is global and permanent for the whole run — entries are **not removed** when recursion returns. This makes the algorithm behave as a *graph-search* with pruning instead of a pure tree-based DFS. That prevents infinite loops in cyclic graphs but has consequences (see "pitfalls" below).

---

### Neighbors (moves) definition

```java
        int[][] moves = {
            {-1, 0},   // up
            {1, 0},    // down
            {0, -1},   // left
            {0, 1}     // right
        };
```

* Offsets for the four cardinal neighbor moves. Each neighbor is current + offset.

---

### For each move, compute neighbor and recurse

```java
        for (int[] mv : moves) {

            int newR = currentState.row + mv[0];
            int newC = currentState.col + mv[1];

            State nextState = new State(newR, newC);

            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());

                if (dls(nextState, depth + 1)) {
                    return true;
                }
            }
        }
```

* Loop over each directional move: compute `newR`, `newC`.
* Construct a `State nextState = new State(newR, newC)`.
* `if (isValidState(nextState) && !visitedStates.contains(nextState.toString()))`:

  * `isValidState` checks bounds and whether the cell is walkable (`0` not `1`).
  * `!visitedStates.contains(...)` prevents revisiting previously visited cells.
* If neighbor passes the checks:

  * `parentMap.put(nextState.toString(), currentState.toString())` — record parent pointer for path reconstruction.
  * `if (dls(nextState, depth + 1)) return true;` — recursively call `dls` with incremented depth. If recursive call returns `true` (goal found deeper), propagate `true` up immediately (stop exploring other neighbors).

If none of the neighbor recursive calls returned `true`, the loop completes and:

```java
        return false;
    }
```

* Return `false` because the current branch did not find the goal (within depth limit).

---

### isValidState — checks bounds & blocked cells

```java
    private static boolean isValidState(State s) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (s.row < 0 || s.col < 0 || s.row >= rows || s.col >= cols)
            return false;

        if (maze[s.row][s.col] == 1)
            return false;

        return true;
    }
```

* Ensures state `(row,col)` is inside array bounds and the cell is not a wall (`1`). Returns `true` only for valid, walkable cells.

---

### Reconstruct & print path

```java
    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (Depth Limited Search on Maze):");
        for (String step : path) {
            String[] parts = step.split(",");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);

            System.out.println("Position -> (" + r + ", " + c + ")");
        }
    }
```

* Walks back from `endState` string using `parentMap` until `null` (the start had `null` parent).
* Reverses the collected path to get start→goal order and prints each position after parsing the `"r,c"` string.

---

### main — start the search

```java
    public static void main(String[] args) {

        State start = new State(0, 0);
        parentMap.put(start.toString(), null);

        if (!dls(start, 0)) {
            System.out.println("No path found within depth limit.");
        }
    }
}
```

* Create start `State(0,0)`.
* Set `parentMap.put(start.toString(), null)` so `printSolutionPath` can stop at start.
* Call `dls(start, 0)`. If it returns `false`, print that no path was found within the depth limit.

---

# Problem statement (concise)

Given a 2D grid `maze` (`0` = free, `1` = blocked), find a path from start `(0,0)` to goal `(goalRow, goalCol)` using **Depth-Limited Search** (meaning DFS limited to a maximum recursion depth). Movement allowed: up, down, left, right. Stop and print a path if the goal is reached within the depth limit; otherwise report failure.

---

# Data structures used

* `int[][] maze` — the environment (grid).
* `State` objects — hold `(row, col)` coordinates.
* `Set<String> visitedStates` — `HashSet` of `"r,c"` strings to prevent revisiting (global across whole search).
* `Map<String, String> parentMap` — `HashMap` mapping each state key to its parent's key for path reconstruction.
* Local recursion stack (implicit Java call stack).
* `List<String>` in `printSolutionPath` for temporary path storage.

---

# Time complexity

Use standard search notation: branching factor `b` = max number of successors per node (here ≤ 4), `l` = depth limit.

* **Worst-case time** (number of node expansions): `O(b^l)` (exponential in depth limit).

  * If the graph has `N` nodes and `l` large enough to explore all, worst-case `O(N)`.
  * But DLS complexity is dominated by the number of nodes generated up to depth `l`, which is roughly `1 + b + b^2 + ... + b^l = O(b^l)`.

Because the implementation uses `visitedStates` to prevent revisiting nodes globally, the number of expansions is at most the number of reachable nodes `N` — so worst-case `O(N)`. However, when treating the maze as a tree without revisiting, the exponential formula applies.

---

# Space complexity

* **Space (call stack)**: `O(l)` due to recursion depth (you store at most `l` frames on the call stack).
* **Auxiliary storage**: `visitedStates` and `parentMap` can grow up to `O(N)` in the worst case (where `N` is number of reachable cells).
* So total **space** = `O(l + N)`; if `l` ~ `N` then `O(N)`.

If you removed the global `visitedStates` and used only recursion-stack membership for cycle detection, space would drop to `O(l)` (plus parent map if you still track parents).

---

# Correctness, guarantees & pitfalls (important!)

1. **Termination** — DLS will terminate because recursion depth is bounded by `depthLimit`. (If there were cycles and no visited set, it could loop; the global `visitedStates` prevents cycles.)

2. **Completeness** — DLS is *not* complete unless the depth limit ≥ actual solution depth. If the real solution depth `d` is greater than `depthLimit`, DLS will not find it. A standard remedy is **Iterative Deepening Depth-First Search (IDDFS)** which repeatedly calls DLS with increasing limits until the solution depth is reached.

3. **Optimality** — DLS does not guarantee the shortest path (in terms of number of steps); plain DFS is not optimal.

4. **Global `visitedStates` leads to possible incompleteness / missed paths** — IMPORTANT: because `visitedStates` is a **global** set and entries are never removed when recursion unwinds, a state visited earlier by one path will be blocked from exploration from a different path later. This is correct for graph-search when you know nodes are visited at minimal depth (like BFS), but for depth-limited DFS it can **prevent** discovering a valid path that arrives at a node via a different (maybe longer) route that would allow reaching the goal within the depth limit. Example consequence:

   * Suppose you visited cell A early in a shallow dead-end branch; later there might exist a different route that reaches A while still leaving enough depth to reach the goal, but the code will ignore that because A is in `visitedStates`. So the global visited set can make the search incomplete even when a path exists within `depthLimit`.

   **Better alternative:** use a *path-level* (recursion-stack) set that marks nodes on the current DFS path only (remove upon return). That detects cycles but allows revisiting nodes via different paths. Or implement DLS as **tree-search** without a visited set (but with a cutoff to break cycles), or manage visited states carefully.

5. **Distinguishing cutoff vs failure** — In classical DLS you often want to know whether the search failed because of reaching the depth limit (a *cutoff*) or because the node is a true dead-end. This implementation treats both the same (returns `false`), making it impossible to tell whether you need a larger depth limit (cutoff occurred) or the graph truly has no solution. IDDFS requires knowing when a cutoff happened to decide whether to increase the limit.

---

# Suggestions & improvements

* **Use path-only visited set** (i.e., add `current` to a `pathSet` before recursing and remove it after recursion) to detect cycles while allowing alternative paths. That is the standard DFS approach to avoid the premature-global-pruning issue.
* **Return richer status** from `dls`: return an enum or int (e.g., `CUTOFF`, `FAILURE`, `SUCCESS`) so callers can tell whether to increase `depthLimit` (as used in IDDFS).
* **Implement IDDFS**: call DLS iteratively with increasing depth limits `0,1,2,...` until you either find a solution or reach some maximum depth.
* **Avoid global `visitedStates`** if you want to explore different paths to the same node (unless you also know the first visit is at optimal depth).
* **Use `State` as key** with `equals`/`hashCode` instead of string conversion for small performance improvements.
* **Return also path/cost** instead of printing directly — makes the algorithm more reusable.

---

# Pseudocode — short and clean

Simple DLS (tree-search style, using path-set for cycle detection):

```
function DLS(state, depth, limit, pathSet):
    if depth > limit:
        return CUTOFF
    if state is goal:
        return SUCCESS
    add state to pathSet

    anyCutoff = false
    for each neighbor of state:
        if neighbor is not valid or neighbor in pathSet: continue
        result = DLS(neighbor, depth+1, limit, pathSet)
        if result == SUCCESS:
            parent[neighbor] = state
            return SUCCESS
        if result == CUTOFF:
            anyCutoff = true
    remove state from pathSet

    if anyCutoff:
        return CUTOFF
    else:
        return FAILURE
```

IDDFS that uses DLS:

```
for limit = 0 to MAX_LIMIT:
    clear parent map
    result = DLS(start, 0, limit, emptySet)
    if result == SUCCESS:
        reconstruct path and return it
    if result == FAILURE (no cutoff occurred anywhere):
        return "no solution"
# otherwise continue increasing limit
```

---

# Complexity recap

* **Time (worst)**: `O(b^l)` where `b` ≤ 4 and `l` = depth limit. If global visited prevents repeats and you traverse all nodes, time ≤ `O(N)` where `N` = number of reachable cells.
* **Space**: recursion stack = `O(l)`; plus `parentMap` and global `visitedStates` up to `O(N)`. So total `O(l + N)` (often `O(N)`).

---

# Final short summary

* This code performs Depth-Limited Search on a grid maze from `(0,0)` to bottom-right using recursive DFS limited by `depthLimit`. It uses a global `visitedStates` set to prevent revisiting, `parentMap` to reconstruct the path, and `isValidState` to avoid walls and out-of-bounds coordinates. The algorithm may miss valid paths because `visitedStates` is global and permanent; a safer pattern is to use path-level cycle detection or adopt IDDFS. Time is exponential in the depth limit in general (or linear in number of nodes if global visited prunes fully); space is linear in depth for recursion plus any global maps.

If you want, I can:

* provide a corrected DLS + IDDFS Java implementation (with `CUTOFF`/`FAILURE`/`SUCCESS` statuses and path-only cycle detection), **or**
* convert this exact implementation to use `State` as keys (implement `equals`/`hashCode`), or
* step through the sample maze and show the exact order of nodes visited for the current code and for an improved version.

Which would you like next?


*/