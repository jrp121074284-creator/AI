package Eight_Puzzle;

import java.util.*;

public class DFS {

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

    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static String GOAL = "123456780";

    public static boolean dfs(State currentState) {
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
                if (dfs(nextState)) {
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

        System.out.println("Steps to solve (DFS Path):");
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
    }

    public static void main(String[] args) {
        String start = "123456780";
        State startState = new State(start);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState)) {
            System.out.println("No solution found!");
        }
    }
}


/*

# Explanation — DFS for the 8-Puzzle (Java)

Nice — this program performs a **depth-first search (DFS)** on the 8-puzzle state space. Below I’ll:

1. Explain the problem in one sentence.
2. Walk through the code **line-by-line / block-by-block**, explaining what each part does.
3. Describe the algorithm’s behavior, correctness limitations, and practical notes.
4. List data structures used.
5. Give time & space complexity (precise and intuitive).
6. Provide a short pseudocode summary.
7. Suggest a few simple improvements.

---

# Problem statement (brief)

Given a 3×3 sliding puzzle (tiles `1..8` and a blank represented by `'0'`), find a sequence of legal moves (sliding a neighbor into the blank) that transforms a given start configuration into the goal configuration `"123456780"`.

---

# Line-by-line / block explanation

```java
package Eight_Puzzle;

import java.util.*;
```

* Declares the package and imports all utility classes (`Set`, `Map`, `List`, `ArrayList`, `HashSet`, `HashMap`, `Collections`, etc.).

```java
public class DFS {
```

* Class `DFS` encloses everything.

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

* Inner static class `State` wraps the board as a `String` of length 9, e.g. `"123405678"`.
* `toString()` returns the string representation — convenient for sets/maps and printing.

```java
    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static String GOAL = "123456780";
```

* `visitedStates`: global `HashSet` to record states already visited (prevents infinite cycles).
* `parentMap`: maps a child state string → parent state string. Used to reconstruct the path after finding the goal.
* `GOAL`: target configuration. Static so all methods share it.

```java
    public static boolean dfs(State currentState) {
        if (currentState.puzzle.equals(GOAL)) {
            printSolutionPath(currentState.toString());
            return true;
        }
```

* `dfs` is a recursive method. Base case: if `currentState` equals `GOAL`, reconstruct & print the path and return `true` indicating success.

```java
        visitedStates.add(currentState.toString());
```

* Mark the `currentState` as visited to avoid revisiting it later in this search.

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

* Precomputed neighbor indices for the blank (`0..8`). For example, at index `4` (center) the blank can swap with indices `1,3,5,7`.

```java
        int zeroIndex = currentState.puzzle.indexOf('0');
        List<State> nextStates = new ArrayList<>();

        for (int next : moves[zeroIndex]) {
            String newPuzzle = swap(currentState.puzzle, zeroIndex, next);
            nextStates.add(new State(newPuzzle));
        }
```

* Find the blank position `zeroIndex`. For each legal neighbor index, create a new string by swapping `'0'` and that tile, wrap it into a `State`, and collect it into `nextStates`.

```java
        for (State nextState : nextStates) {
            if (!visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dfs(nextState)) {
                    return true;
                }
            }
        }

        return false;
    }
```

* For each successor: if it hasn’t been visited, store `parentMap[child] = currentState`, then recursively call `dfs(child)`.
* If any recursive call returns `true` (goal found downstream), propagate `true` to stop the search and unwind recursion.
* If all successors explored (or are visited) and none reaches the goal, return `false` to backtrack.

```java
    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
```

* Utility to swap characters at indices `i` and `j` in a `String` and return the resulting `String` (used to generate successor states).

```java
    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (DFS Path):");
        for (String step : path) {
            printPuzzle(step);
            System.out.println();
        }
    }
```

* Reconstructs the path from `endState` to the start using `parentMap` (follow parent pointers until `null`), then reverses to get start→goal and prints each board.

```java
    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
    }
```

* Nicely prints the 3×3 puzzle (with an empty line before each row).

```java
    public static void main(String[] args) {
        String start = "123456780";
        State startState = new State(start);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState)) {
            System.out.println("No solution found!");
        }
    }
}
```

* `main` sets `start = "123456780"` (the goal itself in this example), wraps it into `State`, sets its parent to `null` in `parentMap`, then calls `dfs(startState)`.
* Because the start equals the goal here, `dfs` prints the path immediately.

---

# Algorithm behavior & important notes

* This is **Depth-First Search** (recursive, backtracking). It explores one branch fully before backtracking.
* `visitedStates` prevents revisiting states already explored (turns DFS into graph DFS rather than naive tree DFS).
* The algorithm **stops at the first found solution** and returns; it does **not** guarantee a shortest list-of-moves solution (DFS typically does not).
* The example `main` uses `start = GOAL`, so the program will immediately print the trivial path containing only the start.
* The **order of neighbors** in `moves` determines which path DFS explores first; different order → different found solution.

---

# Data structures used

* `String` for state encoding (length 9).
* `State` wrapper (contains the `String`).
* `HashSet<String> visitedStates` — constant time membership checks to avoid revisits.
* `HashMap<String,String> parentMap` — stores parent pointers for path reconstruction.
* `ArrayList<State> nextStates` — temporary list for successors.
* Recursion stack (implicit) for DFS.
* `int[][] moves` — static neighbor table.

---

# Time complexity

Let `b` = branching factor (≤ 4 for 8-puzzle) and `d` = depth of the solution found.

* **Worst-case time:** `O(b^d)` expansions (exponential in solution depth). For the general n-puzzle the search space grows exponentially.
* For the 8-puzzle there is an absolute cap: number of reachable states ≤ `9!/2 = 181,440`. So worst case this program will examine at most that many states (so `O(1)` limit for 8-puzzle in absolute terms), but asymptotically DFS is exponential.
* Each successor generation does `swap` (O(1) to produce a 9-char array) and `visited` membership is O(1) on average. So per-expanded-node cost is O(1) aside from recursion/stack overhead.
* **Summary:** exponential worst-case (practical upper bound for 8-puzzle: ≤ 181,440 states).

---

# Space complexity

* `visitedStates` and `parentMap` can store up to `S` states visited; in worst case `S` may be as large as the reachable state space (≤ 181,440 for 8-puzzle). So **space = O(S)**.
* Recursion depth ≤ `d` (solution depth) so recursion stack uses **O(d)**.
* Each state string occupies small constant space (9 chars).
* **Overall:** `O(S)` dominated by visited + parent mapping; recursion adds `O(d)`.

---

# Correctness & limitations

* DFS will find a solution if one exists (and the code stops when found) because the state space is finite and cycles are prevented by `visitedStates`.
* **Not guaranteed shortest:** DFS may return a very long solution even if a shorter one exists. Use BFS or A* for shortest solutions.
* **Stack depth risk:** for extremely deep searches (not typical in 8-puzzle), recursion may overflow the stack. Could convert to iterative DFS (explicit stack) to mitigate.
* **Solvability:** Not all permutations are solvable in the 8-puzzle. This code does not check solvability before searching; it will just exhaust reachable states and print `No solution found!` if none. Checking parity/inversion count first is cheap and recommended.

---

# Short pseudocode

```
function DFS(state):
    if state == GOAL:
        print path using parentMap
        return true
    mark state visited
    for each neighbor in successors(state):   // swap blank with adjacent tiles
        if neighbor not visited:
            parentMap[neighbor] = state
            if DFS(neighbor): return true
    return false

main:
    start = "..." 
    parentMap[start] = null
    if not DFS(start): print "No solution found!"
```

---

# Practical suggestions / improvements

1. **Use a different start example** in `main` (the current example is already the goal). Try a scrambled start to see real search behavior.
2. **Solvability check:** compute inversions and skip search if unsolvable — saves work.
3. **Iterative Deepening DFS (IDDFS)**: yields shortest path with limited memory (useful when you need optimal moves but want DFS-style memory).
4. **Convert to iterative DFS** using an explicit stack to avoid recursion depth / stack overflow issues.
5. **BFS or A*** if you need shortest solution; A* with the Manhattan heuristic will give optimal solution and is standard for the 8-puzzle.
6. **Cache successors or avoid creating unnecessary objects** (e.g., reuse char arrays or store integers) if performance/memory becomes an issue.

---

If you want, I can:

* Modify this code to run iterative deepening (IDDFS) so it finds the shortest path and still uses low memory,
* Or convert it to A* (optimal) with Manhattan heuristic and cached `g` values, or
* Show a concrete step-by-step expansion trace for a nontrivial start state (so you can see the order DFS explores).

Which would you like next?
*/