package Eight_Puzzle;

import java.util.*;

public class BestFirstSearch {

    static class State {
        String puzzle; // Represented as string "123405678"

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
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
    
    public static void bestFirstSearch(String start, String GOAL) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s, GOAL)));

        State startState = new State(start);

        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);

        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.puzzle.equals(GOAL)) {
                printSolutionPath(parentMap, currentState.toString());
                return;
            }


            int[][] moves = {
                    { 1, 3 }, // index 0
                    { 0, 2, 4 }, // index 1
                    { 1, 5 }, // index 2
                    { 0, 4, 6 }, // index 3
                    { 1, 3, 5, 7 }, // index 4
                    { 2, 4, 8 }, // index 5
                    { 3, 7 }, // index 6
                    { 4, 6, 8 }, // index 7
                    { 5, 7 } // index 8
            };
            int zeroIndex = currentState.puzzle.indexOf('0');

            List<State> next = new ArrayList<>();
            for (int nextState : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextState);
                next.add(new State(newPuzzle));
            }

            for(State s : next){
                if (!visitedStates.contains(s.toString())) {
                    visitedStates.add(s.toString());
                    parentMap.put(s.toString(), currentState.toString());
                    pq.add(s);
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

        System.out.println("Steps to solve (BFS - Shortest Path):");
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
    }

    public static void main(String[] args) {
        // Example start state: 1 2 3
                            //  4 0 5
                            //  6 7 8
        String start = "123405678";
        String GOAL = "123456780";
        bestFirstSearch(start, GOAL);
    }
}


/*
Nice — this is a clean Java implementation of **Greedy Best-First Search** for the 8-puzzle (3×3 sliding puzzle). I’ll explain the code line-by-line, then describe the problem, data structures, complexities, correctness/limits, and end with a short pseudocode summary and suggestions for improvement.

---

# Problem statement (brief)

You have a 3×3 board containing tiles numbered `1..8` and one blank (here represented by `'0'`). A legal move slides a neighboring tile into the blank. Given a start configuration (e.g. `"123405678"`) you must reach the goal configuration (`"123456780"`) by sliding tiles. This code uses a **best-first (greedy)** search guided by the **Manhattan distance** heuristic to find a path from start to goal.

---

# Line-by-line explanation

```java
package Eight_Puzzle;

import java.util.*;
```

* Declares the package and imports the Java utility classes used (collections, priority queue, etc).

```java
public class BestFirstSearch {
```

* Declares the outer class.

```java
    static class State {
        String puzzle; // Represented as string "123405678"

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
    }
```

* Inner static class `State` wraps a single board configuration as a `String` of length 9.
* `toString()` returns that string (useful for storing/printing).

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

* Computes the **Manhattan distance** heuristic for state `s` relative to `goal`.

  * For each tile `c` (ignoring the blank `'0'`), find the tile’s current index `i` and its goal index `goalIndex`.
  * Add row difference `abs(i/3 - goalIndex/3)` + column difference `abs(i%3 - goalIndex%3)`.
* The sum of those distances is returned. (This is admissible for A*, but here we use it in greedy best-first.)

```java
    public static void bestFirstSearch(String start, String GOAL) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> heuristic(s, GOAL)));
```

* `visitedStates` — `HashSet` of strings to avoid re-visiting configurations.
* `parentMap` — `HashMap` to store the parent (predecessor) of each visited state for path reconstruction.
* `pq` — `PriorityQueue` that orders `State` objects by `heuristic(state, GOAL)` (lowest first). This implements **greedy best-first** behavior.

```java
        State startState = new State(start);

        pq.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
```

* Create and initialize the start state: push into the priority queue, mark visited, set its parent `null`.

```java
        while (!pq.isEmpty()) {
            State currentState = pq.poll();

            if (currentState.puzzle.equals(GOAL)) {
                printSolutionPath(parentMap, currentState.toString());
                return;
            }
```

* Loop: pop the state with the smallest heuristic.
* If it equals the goal, reconstruct and print the path and **return** (stop at first found path).

```java
            int[][] moves = {
                    { 1, 3 }, // index 0
                    { 0, 2, 4 }, // index 1
                    { 1, 5 }, // index 2
                    { 0, 4, 6 }, // index 3
                    { 1, 3, 5, 7 }, // index 4
                    { 2, 4, 8 }, // index 5
                    { 3, 7 }, // index 6
                    { 4, 6, 8 }, // index 7
                    { 5, 7 } // index 8
            };
            int zeroIndex = currentState.puzzle.indexOf('0');

            List<State> next = new ArrayList<>();
            for (int nextState : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextState);
                next.add(new State(newPuzzle));
            }
```

* `moves` is a hardcoded neighbor list: for each blank index `0..8` the possible swap indices (up/down/left/right).
* `zeroIndex` finds blank location in current state.
* For each legal neighbor index, create the new puzzle string by swapping blank with the neighbor, wrap into `State`, and add to `next`.

```java
            for(State s : next){
                if (!visitedStates.contains(s.toString())) {
                    visitedStates.add(s.toString());
                    parentMap.put(s.toString(), currentState.toString());
                    pq.add(s);
                }
            }
        }
        System.out.println("No solution found!");
    }
```

* For every successor `s`, if it hasn’t been visited:

  * Mark it visited
  * Record its parent as the `currentState`
  * Enqueue it in the priority queue ordered by heuristic
* If loop ends without finding goal: print `"No solution found!"`.

```java
    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
```

* Helper to swap characters at indices `i` and `j` in a `String` and return the new `String` (used to generate successors).

```java
    private static void printSolutionPath(Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (BFS - Shortest Path):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }
```

* Reconstruct the path by following `parentMap` from `endState` back to `null`, reverse the list to get start→goal order, and print each board.
* Note: the printed header says `"BFS - Shortest Path"` — that’s misleading: this program implements **Greedy Best-First Search**, not BFS, and greedy best-first does **not guarantee** the shortest solution.

```java
    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
    }
```

* Nicely prints the 3×3 layout. (It prints a newline before the first row as well.)

```java
    public static void main(String[] args) {
        String start = "123405678";
        String GOAL = "123456780";
        bestFirstSearch(start, GOAL);
    }
}
```

* Example `main` that runs `bestFirstSearch` on a sample start and goal.

---

# What algorithm is this?

* **Greedy Best-First Search**: states are expanded in increasing order of `h(s)` = Manhattan distance to the goal.
* It uses a `PriorityQueue` ordered by `h(s)` only (no `g` cost included), so it is *not* A* and will not necessarily find the shortest (least moves) path.
* It uses a `visitedStates` set to avoid re-visiting states that have already been enqueued/discovered.

---

# Data structures used

* `String` to encode board state (length 9).
* `State` wrapper class for clarity.
* `PriorityQueue<State>` (min-heap) ordered by heuristic.
* `HashSet<String>` `visitedStates` to track visited/enqueued states.
* `HashMap<String, String>` `parentMap` to reconstruct the path.
* `ArrayList` for temporary successor collection.

---

# Correctness notes & behavioral details

* Because the PQ uses only `h`, the search is greedy: it always expands the node that *looks* closest to the goal.
* Greedy best-first is **fast in practice** for many instances but **not optimal** (may not return the shortest path).
* The `visitedStates` is updated when successors are generated (on enqueue), which prevents multiple enqueues of the same configuration (good for efficiency).
* For the 8-puzzle the total reachable state space is at most `9!/2 = 181,440` configurations (only half of permutations are reachable). Thus the algorithm is bounded by that finite set in worst case.
* The printed message `"BFS - Shortest Path"` is incorrect — this algorithm is not BFS and may not produce a shortest path.

---

# Time & Space Complexity

**For the 8-puzzle (and n-puzzle generally):**

* Let `S` be the number of states visited/expanded by the algorithm.
* Each state expansion generates up to `b` successors (for 8-puzzle, `b ≤ 4`).
* Each `pq.add()` or `pq.poll()` is `O(log M)` where `M` is current queue size (≤ `S`).
* Heuristic calculation in this implementation recomputes Manhattan distance each time the comparator is invoked (which internally calls `heuristic`). That can add extra cost — you could cache the heuristic per state to avoid recomputation.

**Practical bounds:**

* Worst-case time: `O(S * log S)` where `S` ≤ number of reachable states (`≤ 9!/2 ≈ 181,440` for 8-puzzle). For general n-puzzle, worst-case exponential in depth: `O(b^d)`.
* Worst-case space: `O(S)` for `visitedStates`, `parentMap`, and priority queue.

**Big picture:** greedy best-first search typically runs in **exponential time** in the depth of the solution in the worst case and consumes exponential space.

---

# Example of a run

* Start: `"123405678"` (blank in middle of second row).
* Goal: `"123456780"`.
* The algorithm will expand states ordered by the Manhattan distance heuristic, stop when it pops the goal from the PQ and then print the reconstructed path.

---

# Short pseudocode

```
function bestFirstSearch(start, goal):
    visited = empty set
    parent = empty map
    pq = priority queue ordered by heuristic(state, goal)
    pq.add(start)
    visited.add(start)
    parent[start] = null

    while pq not empty:
        current = pq.poll()
        if current == goal:
            reconstruct_and_print_path(parent, current)
            return
        for neighbor in successors(current):
            if neighbor not in visited:
                visited.add(neighbor)
                parent[neighbor] = current
                pq.add(neighbor)
    print "No solution found"
```

`heuristic(state, goal)` = sum of Manhattan distances of all tiles (ignore blank).

---

# Suggestions / improvements

1. **If you need an optimal (shortest) solution, use A*:** use `f(s) = g(s) + h(s)` where `g` is path cost (number of moves so far). Maintain `g` values (map from state → g). Greedy (`h` only) is not guaranteed optimal.
2. **Cache heuristic values** instead of recomputing `heuristic(s, GOAL)` repeatedly in the comparator. Store `h` when creating a `State` to avoid repeated `goal.indexOf()` calls.
3. **Avoid storing `String` copies excessively** — you could use a `char[]` or an `int[]` inside `State` and compute a compact key for the visited set.
4. **Tie-breaking:** when heuristics are equal, prefer nodes with larger `g` or smaller `g` depending on desired behavior — can improve quality.
5. **Memory / performance:** for 8-puzzle this implementation is fine; for larger puzzles use bitmasks/packed integers + specialized open/closed structures.
6. **Fix labels / messages:** `printSolutionPath` should not say `"BFS - Shortest Path"` — change to `"Greedy Best-First Path"` or adjust if implementing A*.

---

If you’d like, I can:

* Provide a modified version that implements **A*** (guarantees shortest path) using `f = g + h` and caching heuristics;
* Optimize this implementation by caching heuristics and storing `h` inside `State`;
* Or show a concrete step-by-step run for the sample start state so you can see which nodes are expanded and in what order.

Which would you like next?

*/