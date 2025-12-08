package Eight_Puzzle;

import java.util.*;

public class DLS {

    static class State {
        String puzzle;

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
    }

    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static String GOAL = "123456780";
    static int MAX_DEPTH = 10;

    public static boolean dls(State currentState, int depth) {
    
        if (depth > MAX_DEPTH) return false;

    
        if (currentState.puzzle.equals(GOAL)) {
            printSolutionPath(currentState.toString());
            return true;
        }

        visitedStates.add(currentState.toString());

        int[][] moves = {
                { 1, 3 },       // index 0
                { 0, 2, 4 },    // index 1
                { 1, 5 },       // index 2
                { 0, 4, 6 },    // index 3
                { 1, 3, 5, 7 }, // index 4
                { 2, 4, 8 },    // index 5
                { 3, 7 },       // index 6
                { 4, 6, 8 },    // index 7
                { 5, 7 }        // index 8
        };

        int zeroIndex = currentState.puzzle.indexOf('0');
        List<State> nextStates = new ArrayList<>();

        for (int next : moves[zeroIndex]) {
            String newPuzzle = swap(currentState.puzzle, zeroIndex, next);
            nextStates.add(new State(newPuzzle));
        }

        for (State nextState : nextStates) {
            if (!visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dls(nextState, depth + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }

    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (DFS with depth limit " + MAX_DEPTH + "):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }

    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        String start = "123405678"; // Example start state
        State startState = new State(start);
        parentMap.put(startState.toString(), null);

        if (!dls(startState, 0)) {
            System.out.println("No solution found within depth " + MAX_DEPTH);
        }
    }
}


/*

# Short problem statement

This program implements **Depth-Limited Search (DLS)** for the **8-puzzle**.
A state is a 3×3 board encoded as a 9-char string (e.g. `"123405678"`, where `'0'` is the blank). The goal is `"123456780"`. DLS is a depth-first search that stops recursing past a fixed depth `MAX_DEPTH` and returns success immediately if the goal is found.

---

# Line-by-line / block-by-block explanation

I’ll walk through each logical block and explain what the code does and why.

```java
package Eight_Puzzle;

import java.util.*;
```

* Package and imports. The program uses Java collections (`Set`, `Map`, `List`, etc.).

```java
public class DLS {
```

* Top-level class containing the algorithm and `main`.

```java
    static class State {
        String puzzle;

        State(String puzzle) {
            this.puzzle = puzzle;
        }

        @Override
        public String toString() {
            return puzzle;
        }
    }
```

* `State` is a tiny wrapper around the 9-char `String` representation of the board.
* `toString()` returns the underlying puzzle string; that string is used as keys in maps/sets elsewhere.

```java
    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static String GOAL = "123456780";
    static int MAX_DEPTH = 10;
```

* `visitedStates`: global `HashSet` of state strings already visited (prevents cycles / repeated exploration).
* `parentMap`: maps each state string → its parent state string for path reconstruction.
* `GOAL`: the goal state string.
* `MAX_DEPTH`: depth limit for the search (change this to allow deeper search).

```java
    public static boolean dls(State currentState, int depth) {
    
        if (depth > MAX_DEPTH) return false;
```

* `dls` is the recursive depth-limited DFS function.
* First line: cutoff test — if current recursion `depth` exceeds `MAX_DEPTH` return `false` (treat as failure/cutoff).

```java
        if (currentState.puzzle.equals(GOAL)) {
            printSolutionPath(currentState.toString());
            return true;
        }
```

* Goal test: if `currentState` equals `GOAL`, reconstruct & print path using `parentMap`, return `true` to propagate success upward.

```java
        visitedStates.add(currentState.toString());
```

* Mark current state as visited (global). This prevents visiting the same state later from any branch.

```java
        int[][] moves = {
                { 1, 3 },       // index 0
                { 0, 2, 4 },    // index 1
                { 1, 5 },       // index 2
                { 0, 4, 6 },    // index 3
                { 1, 3, 5, 7 }, // index 4
                { 2, 4, 8 },    // index 5
                { 3, 7 },       // index 6
                { 4, 6, 8 },    // index 7
                { 5, 7 }        // index 8
        };
```

* Precomputed neighbor indices for each blank position (0..8). This encodes which tile indexes the blank can swap with.

```java
        int zeroIndex = currentState.puzzle.indexOf('0');
        List<State> nextStates = new ArrayList<>();

        for (int next : moves[zeroIndex]) {
            String newPuzzle = swap(currentState.puzzle, zeroIndex, next);
            nextStates.add(new State(newPuzzle));
        }
```

* Find blank (`'0'`) index in the string.
* Generate successor `String`s by swapping `'0'` with each legal neighbor; wrap them as `State` and collect in `nextStates`.

```java
        for (State nextState : nextStates) {
            if (!visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dls(nextState, depth + 1)) {
                    return true;
                }
            }
        }

        return false;
    }
```

* For each neighbor:

  * If it has not been globally visited, set its parent to current and recursively call `dls(nextState, depth+1)`.
  * If recursive call returns `true` (goal found), immediately return `true` (stop searching other neighbors).
* If no neighbor leads to success, return `false`.

```java
    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
```

* Helper to swap two positions in the 9-char string and return the new string.

```java
    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (DFS with depth limit " + MAX_DEPTH + "):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }
```

* Reconstruct path by following `parentMap` from `endState` back to `null`, reverse it to go start→goal, and print each board.

```java
    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
        System.out.println();
    }
```

* Nicely prints a 3×3 board for a state string.

```java
    public static void main(String[] args) {
        String start = "123405678"; // Example start state
        State startState = new State(start);
        parentMap.put(startState.toString(), null);

        if (!dls(startState, 0)) {
            System.out.println("No solution found within depth " + MAX_DEPTH);
        }
    }
}
```

* `main` builds the start, sets its parent `null`, calls `dls(start, 0)`. If `false` printed, no solution found within `MAX_DEPTH`.

---

# What this implementation guarantees — and important caveats

* **Termination:** DLS always terminates because recursion depth is bounded by `MAX_DEPTH`.
* **If goal found within `MAX_DEPTH` and not excluded by visited pruning:** program prints a path and returns `true`.
* **It is not guaranteed to find a solution within `MAX_DEPTH`** — if the shortest solution depth > `MAX_DEPTH` you get "No solution".
* **Global `visitedStates` caveat:** marking states *globally* (and never removing them) prevents cycles but can make the search **incomplete** in some cases:

  * Example: one branch visits state `A` at shallow depth and it is added to `visitedStates`. Another branch might reach `A` through a different path that would allow reaching the goal within the depth limit, but because `A` is already marked visited the second branch is pruned — you can miss valid solutions that would otherwise exist within the depth bound.
  * Safer patterns:

    * Use *path-level* visited (add before recursion, remove after returning) to detect cycles only on the current DFS path (preserves completeness within depth-limit).
    * Or do **IDDFS** (iterative deepening DFS) without global visited or with careful handling to preserve both completeness and cycle avoidance.
* **String allocation overhead:** `swap` creates many short `String` objects. For small puzzles this is fine; for heavy search, use compact encodings to reduce allocation overhead.

---

# Data structures used

* `State` objects wrapping a 9-char `String`.
* `HashSet<String> visitedStates` — membership test O(1) average.
* `HashMap<String,String> parentMap` — stores parent pointers for path reconstruction.
* `ArrayList<State>` — temporary successor list.
* Implicit call stack for recursion.

---

# Time complexity

Let:

* `b` = branching factor (≤ 4 for 8-puzzle),
* `d` = depth limit (`MAX_DEPTH`),
* worst-case nodes generated ~ `O(b^d)` (tree model).

So:

* **Time (worst)** = `O(b^d)` (exponential in `d`). With global visited the number of expanded nodes ≤ number of reachable states `N` (≤ 9!/2 = 181,440) so `O(N)` worst for full exploration.
* Each node expansion does a small constant amount of work: swap, set/map operations and recursive calls. Using strings increases constant factors.

---

# Space complexity

* **Call stack depth:** `O(d)` (recursion depth).
* **visitedStates + parentMap:** can grow up to `O(N)` (number of visited states). If you remove global visited and only use path-level visited, extra space aside from recursion stack would be `O(d)`.
* So overall: `O(d + N)`; in practice dominated by `visitedStates` if left global, otherwise `O(d)`.

---

# Correctness, completeness & practical improvements

1. **If you want completeness for unknown depth** — use **Iterative Deepening DFS (IDDFS)**:

   * Repeatedly call DLS with depth limits `0,1,2,...` until you find the solution or a maximum depth.
   * IDDFS combines DFS memory efficiency with BFS-like completeness and optimality in terms of depth.

2. **Change visited handling:**

   * Use a *path set*:

     ```java
     pathSet.add(current);
     for neighbor:
       if not in pathSet:
         recurse
     pathSet.remove(current);
     ```

     That prevents cycles but allows revisiting states from different branches — avoids incompleteness introduced by global `visitedStates`.

3. **Distinguish CUT_OFF vs FAILURE:**

   * Standard DLS returns three outcomes: `SUCCESS`, `FAILURE`, or `CUTOFF` (hit depth limit). IDDFS must know if a cutoff occurred so it can continue increasing limit.

4. **Prune using solvability test:**

   * The 8-puzzle has a parity (inversion) test — if start and goal have different parities the puzzle is unsolvable. Testing that first saves wasted search.

5. **Use compact state encoding** or in-place char arrays to avoid heavy `String` allocation on each swap.

---

# Short pseudocode

Tree-style DLS with path set (avoids the global visited problem):

```
function DLS(node, depth, limit, pathSet):
  if depth > limit: return CUTOFF
  if node == GOAL: return SUCCESS
  pathSet.add(node)

  anyCutoff = false
  for each neighbor of node:
    if neighbor in pathSet: continue
    result = DLS(neighbor, depth+1, limit, pathSet)
    if result == SUCCESS: parent[neighbor]=node; return SUCCESS
    if result == CUTOFF: anyCutoff = true

  pathSet.remove(node)
  if anyCutoff: return CUTOFF
  else: return FAILURE

// IDDFS:
for limit = 0..MAX_ALLOWED:
  clear parent map
  result = DLS(start, 0, limit, emptySet)
  if result == SUCCESS: reconstruct and return path
  if result == FAILURE: return "no solution"
```

---

# Final summary (quick)

* This code is a correct *implementation of DLS with global visited state*, but be aware that global visited can make the search incomplete (miss solutions reachable from alternate paths within the depth limit).
* Complexity: exponential in depth limit (`O(b^d)` time), `O(d)` recursion stack if you remove global visited; with global visited memory may grow to `O(N)`.
* Recommended changes: use path-level visited or IDDFS, add solvability check, and optionally use a more compact state representation for performance.

If you want, I can:

* provide a corrected Java implementation of **DLS + IDDFS** that returns `CUTOFF/FAILURE/SUCCESS` correctly and reconstructs the path, or
* convert this solver to use a compact integer state and show a runtime example on your start state.

Which would you like?
*/