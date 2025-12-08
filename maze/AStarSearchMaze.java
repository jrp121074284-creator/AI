package Maze;

import java.util.*;

public class AStarSearchMaze {

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

    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }

    private static int heuristic(State s, int goalRow, int goalCol) {
        return Math.abs(goalRow - s.row) + Math.abs(goalCol - s.col);
    }

    private static int getPriority(State s, Map<String, Integer> gValues,
                                   int goalRow, int goalCol) {
        return gCost(s.toString(), gValues) + heuristic(s, goalRow, goalCol);
    }

    public static void aStarSearch(int[][] maze, int goalRow, int goalCol) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();

        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues, goalRow, goalCol),
                                getPriority(s2, gValues, goalRow, goalCol));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);

        State start = new State(0, 0);
        pq.add(start);
        visited.add(start.toString());
        parentMap.put(start.toString(), null);
        gValues.put(start.toString(), 0);

        
        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (current.row == goalRow && current.col == goalCol) {
                printSolutionPath(parentMap, current.toString());
                System.out.println("Total steps (cost): " + gCost(current.toString(), gValues));
                return;
            }
            List<State> nexStates = new ArrayList<>();
            int[][] moves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
            };

            for (int[] mv : moves) {
                int newR = current.row + mv[0];
                int newC = current.col + mv[1];

                nexStates.add(new State(newR, newC));
            }
            for(State nextState : nexStates){
                
                if (isValidState(maze, nextState)){

                    int tentativeG = gCost(current.toString(), gValues) + 1;

                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parentMap.put(nextState.toString(), current.toString());
                        pq.add(nextState);
                    }
                }
            }
        }

        System.out.println("No path found.");
    }

    private static boolean isValidState(int[][] maze, State s) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (s.row < 0 || s.col < 0 || s.row >= rows || s.col >= cols)
            return false;

        return maze[s.row][s.col] == 0;
    }

    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search on Maze):");
        for (String step : path) {
            String[] p = step.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);
            System.out.println("Position -> (" + r + ", " + c + ")");
        }
    }

    public static void main(String[] args) {
        int[][] maze = {
                {0, 0, 1, 0},
                {1, 0, 1, 0},
                {0, 0, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 0, 0}
        };

        int goalRow = maze.length - 1;
        int goalCol = maze[0].length - 1;

        aStarSearch(maze, goalRow, goalCol);
    }
}




/*

Nice — I’ll go through your Java A* maze program **line-by-line**, explain exactly what each part does, and then summarize the problem, data structures, complexity, correctness notes (including the PQ / stale-entry issue), and a short clean pseudocode. I’ll keep explanations tied to the code so you can follow easily.

# 1) File / class header + imports

```java
package Maze;

import java.util.*;
```

* `package Maze;` — declares this source file is in the `Maze` package. Useful for project organization; not important to algorithm logic.
* `import java.util.*;` — imports all utility classes (List, Map, HashMap, PriorityQueue, etc.) used throughout the program.

# 2) Class and inner State class

```java
public class AStarSearchMaze {

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

* `public class AStarSearchMaze` — top-level class containing the A* implementation and `main`.
* `static class State` — a nested class that stores a grid cell coordinate `(row, col)`.

  * `int row; int col;` — public fields (default access for nested class) holding the cell coordinates.
  * `State(int r, int c)` — constructor: assigns `row` and `col`.
  * `toString()` — returns a string `"row,col"` used as the key for maps (`gValues` and `parentMap`). Note: this method is used frequently and is critical because maps use string keys.

**Practical note:** `State` does **not** override `equals()` or `hashCode()`. If you later want to use `State` objects as keys in hash-based collections, you should implement them. The code uses `toString()` keys instead, so equality is by string.

# 3) gCost helper

```java
    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }
```

* `gCost(...)` returns the current known g-value (cost from start to a given state) stored in `gValues`.
* If the state has **no entry** in `gValues`, returns `Integer.MAX_VALUE` as a sentinel meaning "unknown / infinite cost".
* This function is used repeatedly to check existing costs.

# 4) heuristic (h) and getPriority (f)

```java
    private static int heuristic(State s, int goalRow, int goalCol) {
        return Math.abs(goalRow - s.row) + Math.abs(goalCol - s.col);
    }

    private static int getPriority(State s, Map<String, Integer> gValues,
                                   int goalRow, int goalCol) {
        return gCost(s.toString(), gValues) + heuristic(s, goalRow, goalCol);
    }
```

* `heuristic(...)` computes the **Manhattan distance** `|dx| + |dy|` from state `s` to the goal `(goalRow, goalCol)`. For 4-way grid movement with unit cost, Manhattan distance is an admissible and consistent heuristic.
* `getPriority(...)` returns `f(s) = g(s) + h(s)` using the `gCost` helper and `heuristic`. This `f` value is used by the `PriorityQueue` comparator to choose which node to expand next.

**Important note**: `getPriority` reads `gValues` at comparison time. Because `gValues` can change after a `State` has been inserted into the `PriorityQueue`, this leads to a *mutable comparator* problem (discussed in the "issues" section below).

# 5) aStarSearch method — signature and initial structures

```java
    public static void aStarSearch(int[][] maze, int goalRow, int goalCol) {
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();
```

* `aStarSearch` takes:

  * `int[][] maze` — 2D grid; `0` is free/walkable, `1` is blocked.
  * `goalRow, goalCol` — coordinates of target cell.
* `visited` — a `HashSet<String>` intended to track expanded nodes (but in this code it is **not** used to skip expansions; only start is added).
* `parentMap` — `Map<String, String>` mapping each state's key (`"r,c"`) to its parent state's key. Used to reconstruct the path from goal to start.
* `gValues` — `Map<String, Integer>` storing best-known `g(s)` for each discovered state.

# 6) PriorityQueue comparator and creation

```java
        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues, goalRow, goalCol),
                                getPriority(s2, gValues, goalRow, goalCol));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);
```

* `comparator` compares `State` objects by `f = g + h` using `getPriority`.
* `pq` is a `PriorityQueue<State>` that pops the `State` with smallest `f`.
* **Subtle point:** the comparator references `gValues`, which the algorithm updates later. Java's `PriorityQueue` does not automatically reorder elements when values that affect ordering change. That means previously inserted elements may have stale priority ordering. This can lead to inefficiency or incorrect expansions if not handled carefully. We’ll cover fixes later.

# 7) Start state initialization

```java
        State start = new State(0, 0);
        pq.add(start);
        visited.add(start.toString());
        parentMap.put(start.toString(), null);
        gValues.put(start.toString(), 0);
```

* `start` = `(0,0)` (top-left) — the algorithm always starts here in this implementation.
* `pq.add(start)` — push the start into the frontier.
* `visited.add(start.toString())` — marks start as visited (but later code does not check this `visited` set when expanding).
* `parentMap.put(start.toString(), null)` — start has no parent.
* `gValues.put(start.toString(), 0)` — cost from start to start is 0.

# 8) Main loop — pop & goal test

```java
        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (current.row == goalRow && current.col == goalCol) {
                printSolutionPath(parentMap, current.toString());
                System.out.println("Total steps (cost): " + gCost(current.toString(), gValues));
                return;
            }
```

* Loop until `pq` is empty (no more states to expand).
* `current = pq.poll()` — remove (and return) the state with smallest `f` according to the comparator's current view.
* Immediately check if `current` is the goal; if yes:

  * call `printSolutionPath(...)` to reconstruct and print the path using `parentMap`.
  * print the total cost using `gCost(current.toString(), gValues)`.
  * `return` — stops search.

**Missing check:** There's no guard to skip *stale* entries popped from the PQ that don't reflect the current `gValues` (see improvement section).

# 9) Generate neighbor states (nexStates)

```java
            List<State> nexStates = new ArrayList<>();
            int[][] moves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
            };

            for (int[] mv : moves) {
                int newR = current.row + mv[0];
                int newC = current.col + mv[1];

                nexStates.add(new State(newR, newC));
            }
```

* `nexStates` is a temporary list to collect potential neighboring states (up, down, left, right).
* `moves` array encodes the four directional offsets.
* For each move, compute neighbor coordinate `(newR, newC)` and add a freshly-constructed `State` to `nexStates`.

# 10) For each neighbor: validity, tentative g, relax

```java
            for(State nextState : nexStates){
                
                if (isValidState(maze, nextState)){

                    int tentativeG = gCost(current.toString(), gValues) + 1;

                    if (tentativeG < gCost(nextState.toString(), gValues)) {
                        gValues.put(nextState.toString(), tentativeG);
                        parentMap.put(nextState.toString(), current.toString());
                        pq.add(nextState);
                    }
                }
            }
```

* Iterate over `nexStates`.
* `isValidState(maze, nextState)` checks bounds and whether the cell is walkable (`maze[newR][newC] == 0`). If invalid, skip the neighbor.
* `tentativeG` = `g(current) + 1`. Each move costs `1`.
* If `tentativeG < g(nextState)` (where `g(nextState)` is `Integer.MAX_VALUE` if unseen), then we found a better path to `nextState`:

  * Update `gValues` with the better `tentativeG`.
  * Update `parentMap` to set `nextState`'s parent to `current`.
  * `pq.add(nextState)` — push neighbor onto the priority queue for future expansion.

**Important**:

* The code does not add `nextState` into `visited` here (and `visited` is unused), so repeated neighbor insertions can occur. That is OK if you handle stale entries or rely on `gValues` to avoid accepting worse paths.
* Because the comparator reads `gValues` at comparison time, and we changed `gValues` before adding the state, the new `f` is visible. But previously added `State` objects for the same coordinates (with worse `g`) may still be inside the PQ and unordered. That is why pop-time validation or immutable PQ entries are recommended.

# 11) End of loop — no path found

```java
        }

        System.out.println("No path found.");
    }
```

* If the while loop exits without returning (pq became empty), there's no path to the goal — print `"No path found."`.

# 12) isValidState helper

```java
    private static boolean isValidState(int[][] maze, State s) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (s.row < 0 || s.col < 0 || s.row >= rows || s.col >= cols)
            return false;

        return maze[s.row][s.col] == 0;
    }
```

* Returns `false` if `s` is out of bounds.
* Returns `true` only if `maze[s.row][s.col] == 0` (walkable cell).
* Simple guard that prevents illegal neighbor expansions.

# 13) printSolutionPath — reconstructs and prints the path

```java
    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search on Maze):");
        for (String step : path) {
            String[] p = step.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);
            System.out.println("Position -> (" + r + ", " + c + ")");
        }
    }
```

* Start from `endState` (a string like `"r,c"`) and follow `parentMap` back to the start, collecting keys in `path`.
* Reverse `path` so it goes from start → goal.
* Print each step by splitting the `"r,c"` string and parsing integers, then printing `Position -> (r, c)`.

**Note:** Frequent string parsing and `split()` are slower than using a dedicated object or storing `State` objects / integer pairs. But for readability and small mazes it's fine.

# 14) main method — example maze and calling aStarSearch

```java
    public static void main(String[] args) {
        int[][] maze = {
                {0, 0, 1, 0},
                {1, 0, 1, 0},
                {0, 0, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 0, 0}
        };

        int goalRow = maze.length - 1;
        int goalCol = maze[0].length - 1;

        aStarSearch(maze, goalRow, goalCol);
    }
}
```

* Defines a 5×4 grid (5 rows, 4 columns). `0` walkable, `1` blocked.
* `goalRow` is last row index (4), `goalCol` is last column index (3) — target is bottom-right cell.
* Calls `aStarSearch` with that maze and goal coordinates.

---

# 2) Problem statement (short & exact)

Given a 2D grid `maze` where `0` = free cell and `1` = blocked cell, find a shortest path from the start `(0,0)` to a given goal `(goalRow, goalCol)` moving only in four directions (up/down/left/right) with uniform step cost 1. Use A* search with Manhattan distance as heuristic (admissible for 4-way grid).

# 3) Data structures used (summary)

* `PriorityQueue<State>` — frontier, ordered by f = g + h via comparator.
* `HashMap<String, Integer>` (`gValues`) — best-known g(s) for each state keyed by `"r,c"`.
* `HashMap<String, String>` (`parentMap`) — parent pointers for path reconstruction.
* `HashSet<String>` (`visited`) — declared but not used meaningfully.
* Temporary `ArrayList<State>` (`nexStates`) — stores neighbor candidates.

# 4) Time & Space Complexity

Let `R` = rows, `C` = columns, `V = R * C` maximum number of vertices.

**Time complexity (worst-case):**

* A* worst-case behaves similar to Dijkstra: O(E log V). For grid E ≈ 4V so O(V log V).
* Each node expansion processes up to 4 neighbours; each PQ operation (insert/poll) costs O(log V).
* Using string keys adds overhead for `toString()` and `split()` (extra constant factors).

**Space complexity:**

* `gValues`, `parentMap`, `visited`, and `pq` can all grow up to O(V) in the worst case.
* So overall space = O(V).

**Practical note:** Heuristic reduces actual expansions, but worst-case bound remains.

# 5) Correctness / Implementation caveats & fixes

1. **Mutable comparator & stale PQ entries**

   * The comparator uses `gValues` which changes after elements are inserted into `PriorityQueue`. Java's `PriorityQueue` does not re-order entries when comparator-relevant external state changes, so entries in the heap can be stale. This can cause:

     * Inefficient expansions (expand nodes out of true f order).
     * Potentially incorrect behavior in some pathological cases (though typically A* still finds a path because `gValues` and parent updates still occur — but correctness/optimality is not formally guaranteed with mutable comparator).
   * **Fix (recommended):** Do **not** rely on external mutable state in comparator. Instead:

     * Create a `Node`/`Entry` wrapper that stores immutable values used for ordering (e.g., `g` and `f`) when you push it into the PQ:

       ```java
       class Node {
         int row, col, g, f;
         String key() { return row + "," + col; }
       }
       ```

       * When you update a neighbor with a better `g`, you push a new `Node` with the *new* `f` into `pq`.
       * When polling, check if the polled node's `g` matches `gValues.get(node.key())`. If not, it's stale — `continue` and poll again.
   * Alternatively, use `TreeSet` with updatable entries or a custom heap that supports decrease-key; Java's built-in `PriorityQueue` lacks decrease-key.

2. **`visited` set is unused**

   * Either remove it or use it to prevent re-expanding nodes that are already expanded at optimal cost. If you mark nodes as permanently closed once popped (and only after confirming the popped entry's `g` matches current `gValues`), then you can skip re-expansion.

3. **Use State equality instead of String keys**

   * Implement `equals()` and `hashCode()` for `State` and use `Map<State, Integer>` for `gValues` and `Map<State, State>` for `parentMap`. That avoids expensive string operations.

4. **Avoid repeated `toString()` and `split()`**

   * For performance, avoid building strings each time. Use integer pair keys (e.g., combine row and col into a single int index `idx = row * cols + col`) or use `State` as a key.

5. **Avoid Integer.MAX_VALUE arithmetic**

   * `gCost` returns `Integer.MAX_VALUE` for unknown nodes. Adding to that would overflow. The code adds only when `g(current)` is known (start is set), so it's safe here, but be cautious in other contexts.

# 6) Corrected approach (brief pseudo-code)

Short, clean pseudocode for a robust A* implementation:

```
function AStar(start, goal, maze):
    g = map()              // g-values
    parent = map()
    pq = priority queue ordered by f = g_entry + h

    g[start] = 0
    parent[start] = null
    pq.add(Node(start, g=0, f = 0 + h(start)))

    closed = set()  // states already expanded optimally

    while pq not empty:
        node = pq.poll()
        if node.state in closed: continue          // skip stale or already expanded
        if node.state == goal:
            return reconstruct_path(parent, node.state)
        closed.add(node.state)

        for each neighbor of node.state:
            if neighbor invalid: continue
            tentative = g[node.state] + 1
            if tentative < g.get(neighbor, +∞):
                g[neighbor] = tentative
                parent[neighbor] = node.state
                pq.add(Node(neighbor, g=tentative, f = tentative + h(neighbor)))
    return failure
```

Key points: store `g` and `f` in PQ entries, and use a `closed` set to track expanded nodes (skip stale entries when popped).

# 7) Short summary & recommendations

* The code is a mostly-correct A* implementation for grid mazes using Manhattan heuristic.
* It will usually work for typical inputs, but has two important improvements to make it robust and efficient:

  1. Avoid a mutable comparator that reads `gValues`. Instead, push immutable PQ entries with explicitly stored `g`/`f` and skip stale entries on pop.
  2. Replace string-key maps with either `State` keys (with `equals`/`hashCode`) or integer indexes for performance.

# 8) Short pseudocode (super compact)

```
A* (start, goal):
  g[start]=0
  parent[start]=null
  pq.push(start, priority=g[start]+h(start))
  while pq not empty:
    cur = pq.pop()
    if cur == goal: return reconstruct(parent, cur)
    for each neighbor:
      if not walkable: continue
      tentative = g[cur] + 1
      if tentative < g.get(neighbor, ∞):
        g[neighbor] = tentative
        parent[neighbor] = cur
        pq.push(neighbor, tentative + h(neighbor))
  return failure
```

---

If you want, I can now:

* produce a **corrected and efficient Java version** that fixes the comparator/stale-entry issue and uses `State` as keys (with `equals`/`hashCode`), or
* step through the specific sample maze and show the exact order of node expansions and the final path.

Which would you prefer?



*/