package Maze;
import java.util.*;

public class BestFirstSearchMaze {

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

    private static int heuristic(State s, int goalRow, int goalCol) {
        return Math.abs(goalRow - s.row) + Math.abs(goalCol - s.col);
    }

    public static void bestFirstSearch(int[][] maze, int goalRow, int goalCol) {
        
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();

        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingInt(s -> heuristic(s, goalRow, goalCol))
        );

        State start = new State(0, 0);
        pq.add(start);
        visited.add(start.toString());
        parentMap.put(start.toString(), null);

        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (current.row == goalRow && current.col == goalCol) {
                printSolutionPath(parentMap, current.toString());
                return;
            }

            List<State> nextStates = new ArrayList<>();
            int[][] moves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
            };

            for (int[] mv : moves) {
                int newR = current.row + mv[0];
                int newC = current.col + mv[1];

                State nextState = new State(newR, newC);
                nextStates.add(nextState);
            }

            for (State nextState : nextStates) {
                if (isValidState(maze, nextState) && !visited.contains(nextState.toString())) {
                    visited.add(nextState.toString());
                    parentMap.put(nextState.toString(), current.toString());
                    pq.add(nextState);
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

        if (maze[s.row][s.col] == 1)
            return false;

        return true;
    }

    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (Best First Search on Maze):");
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
        bestFirstSearch(maze, goalRow, goalCol);
    }
}


/*

# Line-by-line explanation (detailed)

I'll walk through your **BestFirstSearchMaze** Java program from top to bottom and explain each line / block and what it does.

```java
package Maze;
import java.util.*;
```

* `package Maze;` — declares this file belongs to the `Maze` package (organizes code).
* `import java.util.*;` — imports utility classes used later (`List`, `Map`, `HashMap`, `HashSet`, `PriorityQueue`, `Comparator`, etc.).

---

```java
public class BestFirstSearchMaze {
```

* Defines the public class `BestFirstSearchMaze` which contains the entire program (state class, algorithm, helpers, `main`).

---

```java
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

* `static class State` — a nested class representing a grid cell coordinate `(row, col)`.
* `int row; int col;` — integer fields for the cell index.
* `State(int r, int c)` — constructor that assigns the row and column.
* `toString()` — returns `"row,col"`; the main program uses this string as the key in maps/sets (`visited`, `parentMap`).
  **Notes:**

  * `State` does not override `equals()`/`hashCode()`. The program avoids using `State` objects as keys by using `toString()` instead. Using `State` directly would require `equals` and `hashCode` for correct `HashMap`/`HashSet` behavior.

---

```java
    private static int heuristic(State s, int goalRow, int goalCol) {
        return Math.abs(goalRow - s.row) + Math.abs(goalCol - s.col);
    }
```

* `heuristic(...)` computes the **Manhattan distance** between state `s` and the goal `(goalRow, goalCol)`: `|dx| + |dy|`.
* This heuristic is appropriate for 4-direction grid movement with unit costs. It is **admissible** (never overestimates true shortest remaining cost).

---

```java
    public static void bestFirstSearch(int[][] maze, int goalRow, int goalCol) {
        
        Set<String> visited = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
```

* `bestFirstSearch(...)` — method implementing the greedy best-first search algorithm.
* `int[][] maze` — 2D grid: `0` = free/walkable, `1` = blocked.
* `goalRow`, `goalCol` — goal coordinates.
* `visited` — `HashSet<String>` stores keys (`"r,c"`) of states already added/expanded to avoid revisiting.
* `parentMap` — `HashMap` mapping each state's string to its parent's string, used to reconstruct the path.

---

```java
        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingInt(s -> heuristic(s, goalRow, goalCol))
        );
```

* Creates a `PriorityQueue<State>` named `pq`.
* The `Comparator` orders `State` objects by their heuristic value `h(s)` only (i.e., greedy best-first). States with lower `h` (closer, by Manhattan) are prioritized.
* Important: The queue does **not** consider `g` (cost-so-far); it is greedy.

---

```java
        State start = new State(0, 0);
        pq.add(start);
        visited.add(start.toString());
        parentMap.put(start.toString(), null);
```

* `start` = `(0,0)` — algorithm always starts from top-left here.
* `pq.add(start)` — push start into the frontier.
* `visited.add(start.toString())` — mark start as visited so it won't be enqueued again.
* `parentMap.put(start.toString(), null)` — start has no parent.

---

```java
        while (!pq.isEmpty()) {
            State current = pq.poll();
```

* Main loop: repeat while frontier `pq` not empty.
* `pq.poll()` removes and returns the state with smallest `h(s)` (closest to goal by heuristic).

---

```java
            if (current.row == goalRow && current.col == goalCol) {
                printSolutionPath(parentMap, current.toString());
                return;
            }
```

* If the popped `current` is the goal:

  * `printSolutionPath(...)` reconstructs the path using `parentMap` and prints it (see helper below).
  * `return` — exit, search succeeded. Note: no cost is printed because best-first doesn't track `g` values here.

---

```java
            List<State> nextStates = new ArrayList<>();
            int[][] moves = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
            };

            for (int[] mv : moves) {
                int newR = current.row + mv[0];
                int newC = current.col + mv[1];

                State nextState = new State(newR, newC);
                nextStates.add(nextState);
            }
```

* Build neighbor (successor) candidates in `nextStates` for the 4 cardinal moves: up, down, left, right.
* For each move offset `mv`, compute new coordinates and create a new `State(newR, newC)`, add to list.

---

```java
            for (State nextState : nextStates) {
                if (isValidState(maze, nextState) && !visited.contains(nextState.toString())) {
                    visited.add(nextState.toString());
                    parentMap.put(nextState.toString(), current.toString());
                    pq.add(nextState);
                }
            }
```

* For each neighbor:

  * `isValidState(maze, nextState)` checks bounds and that the cell is walkable (`0`).
  * `!visited.contains(nextState.toString())` ensures we only process previously unseen states (prevents re-enqueuing).
  * If valid and unseen:

    * `visited.add(...)` — mark it visited immediately.
    * `parentMap.put(...)` — set neighbor's parent to `current`.
    * `pq.add(nextState)` — push neighbor into the queue (priority = `heuristic(nextState)`).

**Important behavioral detail:** Marking `visited` **on enqueue** (not on dequeue) prevents the algorithm from later discovering a different path to the same state that might be better. For greedy best-first (which doesn’t track path cost), this is typical but means:

* you **never** reconsider a state once seen,
* you may miss a path that reaches the same cell with better future potential,
* best-first is *not* guaranteed to find the shortest path (it can find a path quickly or miss one entirely).

---

```java
        }

        System.out.println("No path found.");
    }
```

* Loop finished with empty queue and no goal reached => print `"No path found."`.

---

```java
    private static boolean isValidState(int[][] maze, State s) {
        int rows = maze.length;
        int cols = maze[0].length;

        if (s.row < 0 || s.col < 0 || s.row >= rows || s.col >= cols)
            return false;

        if (maze[s.row][s.col] == 1)
            return false;

        return true;
    }
```

* `isValidState` checks:

  * bounds (row and col inside grid),
  * cell is walkable (`maze[r][c] == 0`).
* Returns `true` only if both conditions satisfied.

---

```java
    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (Best First Search on Maze):");
        for (String step : path) {
            String[] p = step.split(",");
            int r = Integer.parseInt(p[0]);
            int c = Integer.parseInt(p[1]);

            System.out.println("Position -> (" + r + ", " + c + ")");
        }
    }
```

* Reconstructs the path from `endState` back to the start using `parentMap`.
* Builds `path` list of `"r,c"` strings, reverses to start→goal order, then prints each as `Position -> (r, c)`.
* Uses `split(",")` and `Integer.parseInt(...)` to convert string keys back to coordinates.

---

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
        bestFirstSearch(maze, goalRow, goalCol);
    }
}
```

* `main` sets up a sample 5×4 maze (same sample you used in A*).
* `goalRow` / `goalCol` are set to bottom-right cell indices.
* Calls `bestFirstSearch` to run and print the result.

---

# Problem statement (concise)

Given a grid `maze` where `0` = free cell and `1` = blocked, find **a** path from start `(0,0)` to a goal `(goalRow, goalCol)` using **Best-First Search** (greedy): at every step expand the node that appears closest to the goal according to a heuristic (Manhattan distance). Movement allowed: up, down, left, right (4-neighbors). Best-first is greedy and optimizes only the heuristic (not the cost so far).

---

# What this program guarantees (and what it doesn't)

* **Guarantees:** If it finds a path, it will reconstruct and print that path.
* **Does NOT guarantee optimality:** Best-first is *greedy*: it does not track or minimize path cost (`g`). Therefore the found path may not be the shortest in number of steps.
* **Completeness depends on terrain:** With visited-marking on enqueue, it will terminate and either return one path or report no path. It may fail to find a path if the heuristic leads it down dead ends and `visited` prevents re-exploring alternative routes (but since all neighbors are considered, it will eventually explore other nodes if queue exhausts).
* **Efficiency:** Often faster than uninformed search because it focuses toward the goal; but in worst cases it may still explore most of the grid.

---

# Data structures used

* `PriorityQueue<State>` — frontier (ordered by `h(s)`).
* `HashSet<String>` (`visited`) — tracks seen nodes; prevents re-enqueue.
* `HashMap<String,String>` (`parentMap`) — parent pointers for path reconstruction.
* `ArrayList<State>` — used temporarily to collect next states.
* `String` keys `"r,c"` — used instead of `State` keys in the maps/sets.

---

# Time complexity

Let `R`×`C` be grid dimensions and `V = R*C` be number of cells.

* **Each cell is enqueued at most once** because `visited` prevents re-enqueueing.
* Each enqueue/dequeue from the priority queue costs `O(log N)` where `N` is size of the queue (≤ V).
* Generating/checking neighbors is `O(1)` per neighbor (4 neighbors).

Therefore:

* **Worst-case time:** `O(V log V)` (each of up to V nodes enqueued/dequeued once).
* If the heuristic is very effective, fewer nodes are explored; if it's misleading, near-worst-case behavior occurs.

**Note:** Using string keys (toString + split/parse) adds extra constant overhead per operation.

---

# Space complexity

* `visited`, `parentMap`, and `pq` can each store up to `O(V)` entries in the worst case.
* So **space** = `O(V)`.

---

# Correctness properties and pitfalls

* **Heuristic:** Manhattan distance is admissible but best-first ignores `g`, so admissibility isn't used for optimality here.
* **Visited-on-enqueue:** Prevents duplicate enqueues but can prematurely block better routes that reach a node after a different path. For greedy search this is common but amplifies suboptimality risk.
* **No g-values:** Because the implementation doesn’t track `g` (cost-so-far), you cannot report path cost and you cannot detect whether a discovered path is optimal.
* **String-based keys:** Works but is less efficient and error-prone; prefer `State` with `equals`/`hashCode` or an integer index.

---

# Improvements / alternatives

* **If you need shortest path:** Use A* (maintain `g` and use `f = g + h`). A* with Manhattan distance will give optimal path length for uniform-cost grids.
* **If you want to allow revisiting when a better path found:** Don’t mark visited on enqueue; instead, use cost tracking and mark closed on dequeue when the best `g` for the node is known.
* **Make `State` usable as a key:** implement `equals()` and `hashCode()` and use `Map<State, Integer>` for `g` if you add costs.
* **Avoid string parsing:** store integer pair or index `idx = r * cols + c` as the key.

---

# Short pseudo-code (compact)

```
function BestFirst(start, goal, maze):
  visited = empty set
  parent = empty map
  pq = priority queue ordered by heuristic(s, goal)

  visited.add(start)
  parent[start] = null
  pq.push(start)

  while pq not empty:
    current = pq.pop()
    if current == goal:
      return reconstruct_path(parent, current)
    for neighbor in neighbors(current):   // up,down,left,right
      if valid(neighbor) and neighbor not in visited:
        visited.add(neighbor)
        parent[neighbor] = current
        pq.push(neighbor)
  return failure
```

---

# Quick summary (one paragraph)

This program implements **Greedy Best-First Search** on a 2D maze using Manhattan distance as the heuristic. It uses a `PriorityQueue` keyed by `h(s)` to always expand the node closest to the goal (according to `h`). It marks nodes visited on enqueue and uses a `parentMap` of string keys to reconstruct the found path. Complexity is `O(V log V)` time and `O(V)` space in the worst case. Best-first is fast and focused but **not guaranteed** to return the shortest path; if you need optimal shortest paths, prefer A* (track `g` and use `f = g + h`).

---

If you want, I can:

* convert this to a **correct A*** Java implementation (with `g` tracking and robust PQ handling), or
* refactor this code to use `State` as map keys (implement `equals`/`hashCode`) and show the exact path Best-First produces on your sample maze step-by-step.

Which would you like next?


*/