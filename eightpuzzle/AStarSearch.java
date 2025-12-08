package Eight_Puzzle;

import java.util.*;

public class AStarSearch {

    static class State {
        String puzzle; // e.g. "123405678"

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
    }

    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }

    private static int heuristic(State s, String goal) {
        String board = s.puzzle;
        int distance = 0;
        for (int i = 0; i < 9; i++) {
            char c = board.charAt(i);
            if (c != '0') {
                int goalIndex = goal.indexOf(c);
                distance += Math.abs(i / 3 - goalIndex / 3) + Math.abs(i % 3 - goalIndex % 3);
            }
        }
        return distance;
    }

    private static int getPriority(State s, Map<String, Integer> gValues, String goal) {
        return gCost(s.toString(), gValues) + heuristic(s, goal);
    }

    public static void aStarSearch(String start, String goal) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();

        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues, goal), getPriority(s2, gValues, goal));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);

        State startState = new State(start);
        pq.add(startState);
        parentMap.put(startState.toString(), null);
        gValues.put(startState.toString(), 0);


        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.puzzle.equals(goal)) {
                printSolutionPath(parentMap, currentState.toString());
                System.out.println("Total steps (cost): " + gCost(currentState.toString(), gValues));
                return;
            }
            int[][] moves = {
                {1, 3},        // 0
                {0, 2, 4},     // 1
                {1, 5},        // 2
                {0, 4, 6},     // 3
                {1, 3, 5, 7},  // 4
                {2, 4, 8},     // 5
                {3, 7},        // 6
                {4, 6, 8},     // 7
                {5, 7}         // 8
            };

            int zeroIndex = currentState.puzzle.indexOf('0');

            List<State> next = new ArrayList<>();
            for (int nextPos : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextPos);
                State nextState = new State(newPuzzle);
                next.add(nextState);
            }

            for(State nextState : next){
                int tentativeG = gCost(currentState.toString(), gValues) + 1;

                if (tentativeG < gCost(nextState.toString(), gValues)) {
                    gValues.put(nextState.toString(), tentativeG);
                    parentMap.put(nextState.toString(), currentState.toString());
                    pq.add(nextState);
                }
            }
        }

        System.out.println("No solution found!");
    }

    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }

    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }

    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String start = "123405678"; // Example start
        String goal = "123456780";  // Goal state
        aStarSearch(start, goal);
    }
}


/*

Nice — this is a neat A* solver for the 8-puzzle. I’ll go through the code top-to-bottom, explain every important line / block, point out design notes (good & subtle problems), list the data structures used, give time/space complexity and correctness remarks, and end with a short clean pseudocode you can copy.

---

# Problem statement (short)

Solve the 8-puzzle: given a start permutation of tiles `0..8` on a 3×3 board (where `0` represents the blank), find a sequence of legal moves (swap blank with an orthogonal neighbor) that transforms the start into the goal configuration in the fewest moves. This implementation uses **A*** with the **Manhattan distance** heuristic.

---

# High-level summary before line-by-line

* States are represented as 9-character strings like `"123405678"` (row-major).
* `heuristic` computes Manhattan distance of all tiles (ignoring blank).
* `gValues` stores the discovered shortest-so-far cost from start (`g(s)`).
* A `PriorityQueue` orders frontier states by `f(s) = g(s) + h(s)`.
* `parentMap` reconstructs the solution path.
* Neighbors are generated by swapping the `'0'` with allowed positions.
* On reaching `goal`, path is printed and total steps shown.

---

# Line-by-line explanation

```java
package Eight_Puzzle;

import java.util.*;
```

* Package and imports. `java.util.*` brings in `Map`, `HashMap`, `PriorityQueue`, `Comparator`, `List`, etc.

```java
public class AStarSearch {
```

* Top-level class containing solver and `main`.

### State inner class

```java
    static class State {
        String puzzle; // e.g. "123405678"

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
    }
```

* `State` wraps the 9-char puzzle string. `toString()` returns the string — used as the key for maps.
* Using a string is convenient for hashing/comparison and to produce readable output.

### gCost helper

```java
    private static int gCost(String state, Map<String, Integer> gValues) {
        return gValues.getOrDefault(state, Integer.MAX_VALUE);
    }
```

* Returns `g(s)` from `gValues` if present; otherwise a sentinel `Integer.MAX_VALUE` meaning "infinite / unknown".
* Used when computing tentative g-values and priorities.

### heuristic (Manhattan)

```java
    private static int heuristic(State s, String goal) {
        String board = s.puzzle;
        int distance = 0;
        for (int i = 0; i < 9; i++) {
            char c = board.charAt(i);
            if (c != '0') {
                int goalIndex = goal.indexOf(c);
                distance += Math.abs(i / 3 - goalIndex / 3) + Math.abs(i % 3 - goalIndex % 3);
            }
        }
        return distance;
    }
```

* Computes sum of Manhattan distances for each tile (ignoring '0').
* For index `i` on a 3×3 board: row = `i/3`, col = `i%3`.
* `goal.indexOf(c)` locates the tile's target index in the goal string.
* Manhattan is admissible & consistent for 8-puzzle with unit move cost → A* will find optimal solution **if** the frontier ordering is handled correctly.

### getPriority

```java
    private static int getPriority(State s, Map<String, Integer> gValues, String goal) {
        return gCost(s.toString(), gValues) + heuristic(s, goal);
    }
```

* `f(s) = g(s) + h(s)` — used by the comparator for the `PriorityQueue`.

### aStarSearch main algorithm

```java
    public static void aStarSearch(String start, String goal) {
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> gValues = new HashMap<>();
```

* `parentMap` stores parent pointers: `childStateString -> parentStateString`.
* `gValues` stores currently known best `g` for each visited state.

```java
        Comparator<State> comparator = (s1, s2) ->
                Integer.compare(getPriority(s1, gValues, goal), getPriority(s2, gValues, goal));

        PriorityQueue<State> pq = new PriorityQueue<>(comparator);
```

* Creates a comparator that compares `f(s)` using current `gValues`.
* **Subtle point:** comparator references `gValues` (mutable). If you update `gValues` after inserting a `State` into `pq`, `PriorityQueue`’s internal heap does **not** reorder automatically. This can lead to stale ordering and must be handled carefully. (See “implementation notes / fixes” below.)

```java
        State startState = new State(start);
        pq.add(startState);
        parentMap.put(startState.toString(), null);
        gValues.put(startState.toString(), 0);
```

* Initialize start: push into `pq`, set parent `null`, `g(start)=0`.

```java
        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.puzzle.equals(goal)) {
                printSolutionPath(parentMap, currentState.toString());
                System.out.println("Total steps (cost): " + gCost(currentState.toString(), gValues));
                return;
            }
```

* Main loop: pop the state with smallest `f` (according to comparator).
* If popped state equals goal, reconstruct path and print cost. `gCost` retrieves `g(goal)`.

```java
            int[][] moves = {
                {1, 3},        // 0
                {0, 2, 4},     // 1
                {1, 5},        // 2
                {0, 4, 6},     // 3
                {1, 3, 5, 7},  // 4
                {2, 4, 8},     // 5
                {3, 7},        // 6
                {4, 6, 8},     // 7
                {5, 7}         // 8
            };

            int zeroIndex = currentState.puzzle.indexOf('0');
```

* `moves` encodes legal swap positions for the blank at each index 0..8 (precomputed neighbors). `zeroIndex` finds blank's position in the current string.

```java
            List<State> next = new ArrayList<>();
            for (int nextPos : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextPos);
                State nextState = new State(newPuzzle);
                next.add(nextState);
            }
```

* Generate successor states by swapping '0' with each legal neighbor. `swap` returns a new string.

```java
            for(State nextState : next){
                int tentativeG = gCost(currentState.toString(), gValues) + 1;

                if (tentativeG < gCost(nextState.toString(), gValues)) {
                    gValues.put(nextState.toString(), tentativeG);
                    parentMap.put(nextState.toString(), currentState.toString());
                    pq.add(nextState);
                }
            }
        }

        System.out.println("No solution found!");
    }
```

* For each neighbor:

  * `tentativeG = g(current) + 1` (one move).
  * If this `tentativeG` improves the previously known `g` for that neighbor, update `gValues`, set parent, and add neighbor to `pq`.
* If loop finishes without finding goal, print failure.

### swap helper

```java
    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
```

* Utility to return a new puzzle string with positions `i` and `j` swapped.

### printSolutionPath + printPuzzle

```java
    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (A* Search):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }

    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
        System.out.println();
    }
```

* Reconstructs path from goal to start using `parentMap`, reverses it, and prints each 3×3 board. `printPuzzle` formats a 1-line string into a 3×3 view.

### main

```java
    public static void main(String[] args) {
        String start = "123405678"; // Example start
        String goal = "123456780";  // Goal state
        aStarSearch(start, goal);
    }
}
```

* Example run: start with blank in middle of top row index 4? (the string places '0' at index 4). Calls A*.

---

# Data structures used

* `String` to represent board state (immutable, hashable).
* `State` wrapper (small).
* `PriorityQueue<State>` — frontier ordered by comparator using `f = g+h`.
* `Map<String,Integer> gValues` — best-known `g` for each state.
* `Map<String,String> parentMap` — for path reconstruction.
* `List<State>` — temporary list of successors.
* `int[][] moves` — static neighbor index list.

---

# Correctness & heuristic properties

* **Heuristic (Manhattan)** is **admissible** (never overestimates) and **consistent** for the 8-puzzle with unit move cost, so A* with proper handling returns an optimal (shortest) solution.
* However, correctness *depends on correct PQ handling*: because the comparator reads `gValues` (mutable), PQ entries may become out-of-order after `gValues` updates. This is a classic Java `PriorityQueue` pitfall:

  * Java PQ does not update positions of objects when comparator-relevant external state changes.
  * The code mitigates this partly by inserting *new* `State` objects when `g` improves, but stale entries can remain in the PQ and might be popped earlier than a newly inserted better `f` entry. The algorithm still works if on pop you verify whether the popped state's `g` matches the current `gValues` (skip stale nodes). This code does **not** perform that check, so it risks expanding stale nodes. Often it still finds correct path, but it's brittle and may be inefficient or incorrect in edge cases.
* **Recommendation:** Use PQ entries that store immutable `g` and `f` values (a `Node` object with `state`, `g`, `f`) and on `poll()` compare popped node's `g` to `gValues.get(state)`. If they mismatch, skip the entry (it’s stale). Or implement decrease-key behavior by always pushing new entries and skipping stale pops.

---

# Time complexity

* Let `b` be branching factor (max successors) — for 8-puzzle `b ≤ 4`.
* Let `d` be solution depth (optimal number of moves).
* A* worst-case time is exponential; more formally:

  * Time ≈ `O(N log N)` where `N` is number of states expanded and each PQ operation costs `O(log N)`.
  * Worst-case number of expanded states can be on the order of the size of the reachable state space: for 8-puzzle that is at most `9!/2 = 181,440` solvable states (because half of permutations are unsolvable). So worst-case `O(181k log 181k)` for full exploration.
  * In heuristic terms, worst-case complexity is `O(b^d)` (exponential in solution depth) when heuristic provides poor guidance.
* Per-step costs include string creation for swaps and `goal.indexOf(c)` calls inside heuristic (which are `O(9)` each). These string operations add constant-factor overhead.

# Space complexity

* `gValues`, `parentMap`, and `pq` can all grow up to `O(N)` where `N` is number of visited states (≤ ~181k). So **space = O(N)**.
* Each stored state uses a `String` (9 chars) plus maps/queue overhead.

---

# Practical notes and optimizations

1. **Fix PQ stale-entry issue**

   * Option A (recommended): store entries with explicit `g`/`f` (immutable) in the PQ:

     ```java
     class Node { String state; int g, f; }
     pq.add(new Node(newState, tentativeG, tentativeG + heuristic(...)));
     ```

     On `poll()` check `if (node.g != gValues.get(node.state)) continue; // stale`
   * Option B: use a `TreeSet` + update/remove old entries (complex), or implement a custom binary heap with decrease-key.

2. **Optimize heuristic computation**

   * Replace `goal.indexOf(c)` with a precomputed map `char -> index` for O(1) lookup.
   * Avoid recomputing Manhattan for unchanged tiles; you can incrementally update `h` when swapping blank with tile.

3. **Avoid excessive String allocation**

   * Use `char[]` internally or encode state as an `int` (bit-packed) for faster swaps & smaller memory.

4. **Detect unsolvable instances early**

   * The 8-puzzle solvability depends on inversion parity. Check start/goal parity and return “no solution” if mismatched — this prevents fruitless search.

5. **Closed set**

   * Many A* implementations maintain a `closed` set of expanded states with finalized `g`. You can skip re-expanding states when popped if they are already in `closed`.

---

# Example: solvable state-space size

* The 8-puzzle has `9! = 362,880` permutations; exactly half are reachable from any given configuration. So reachable solvable states ≤ `9!/2 = 181,440`. That bounds the maximum N in practice.

---

# Short pseudocode (clean & robust)

```
function AStar(start, goal):
  g = map()                // g[state] best known cost
  parent = map()
  g[start] = 0
  parent[start] = null

  // precompute goalIndex[char] -> position (0..8) for heuristic speed
  pq = priority queue of Node(state, g, f) ordered by f
  pq.add(Node(start, g=0, f=g[ start ] + h(start)))

  closed = empty set()

  while pq not empty:
    node = pq.poll()
    state = node.state

    // skip stale entry
    if node.g != g[state]: continue

    if state == goal:
      return reconstruct_path(parent, state)

    closed.add(state)

    for each neighbor of state:
      tentative = g[state] + 1
      if tentative < g.get(neighbor, ∞):
        g[neighbor] = tentative
        parent[neighbor] = state
        if neighbor not in closed:
          pq.add(Node(neighbor, g=tentative, f = tentative + h(neighbor)))

  return failure
```

---

# Final checklist of what this code does correctly and what to change

* ✅ Uses Manhattan heuristic (admissible & consistent).
* ✅ Uses `gValues` and `parentMap` so it can reconstruct optimal path (if PQ handled properly).
* ✅ Generates legal neighbors via precomputed move table.
* 🔶 **Change/Improve**: fix the `PriorityQueue` stale-entry problem (store `g` in PQ entries and skip stale pops, or use a Node wrapper).
* 🔶 **Optional**: precompute goal indexes, check solvability parity, reduce string allocations, and optionally use a closed set.

---

If you’d like I can:

* produce a corrected, efficient Java A* implementation that uses a `Node` wrapper and solves the example (with solvability check), or
* show the exact sequence of states this program would print for the example `start="123405678"` → `goal="123456780"` (i.e., run the search mentally and list the moves).

Which would you like?

*/