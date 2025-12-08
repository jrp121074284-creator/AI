package Eight_Puzzle;

import java.util.*;

public class BFS {

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

    
    public static void bfs(String start, String GOAL) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Queue<State> queue = new LinkedList<>();

        State startState = new State(start);

        queue.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

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
            int zeroIndex = currentState.puzzle.indexOf('0'); // blank position

            List<State> next = new ArrayList<>();
            for (int nextState : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextState);
                next.add(new State(newPuzzle));
            }

            for(State s : next){
                if (!visitedStates.contains(s.toString())) {
                    visitedStates.add(s.toString());
                    parentMap.put(s.toString(), currentState.toString());
                    queue.add(s);
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
        bfs(start, GOAL);
    }
}


/*
# Short problem statement

Given a start configuration of the **8-puzzle** (3×3 board with tiles `1..8` and a blank `0`), find the shortest sequence of legal moves (slide blank up/down/left/right) that transforms the start into a given goal configuration. This program uses **breadth-first search (BFS)**, so it finds a solution with the minimum number of moves (assuming all moves cost 1).

---

# Quick summary of what the program uses

* State representation: `String` of length 9 (row-major), e.g. `"123405678"`.
* Frontier: `Queue<State>` (FIFO) implemented with `LinkedList`.
* Explored set: `Set<String> visitedStates` (`HashSet`) to avoid revisiting states.
* Parent map: `Map<String,String> parentMap` (`HashMap`) to reconstruct the solution path.
* Neighbor generation: precomputed `moves` table for each blank index (0..8).
* Output: prints each board on the shortest path from start → goal.

---

# Walkthrough — line by line (important lines explained)

```java
package Eight_Puzzle;

import java.util.*;
```

* Package and imports (utilities used throughout the code).

```java
public class BFS {
```

* Class definition containing BFS solver and `main`.

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

* `State` is a tiny wrapper containing a 9-character `String` representing the board. `toString()` returns that string and is used as the key for `visitedStates` and `parentMap`.

```java
    public static void bfs(String start, String GOAL) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Queue<State> queue = new LinkedList<>();
```

* `bfs` sets up three core structures:

  * `visitedStates` — tracks states already discovered (prevent repeats).
  * `parentMap` — stores `childStateString -> parentStateString` for path reconstruction.
  * `queue` — FIFO frontier storing `State` objects to expand in BFS order.

```java
        State startState = new State(start);

        queue.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
```

* Initialize with `start`: add to queue, mark visited, and set parent to `null` so we can stop when reconstructing.

```java
        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.puzzle.equals(GOAL)) {
                printSolutionPath(parentMap, currentState.toString());
                return;
            }
```

* Main BFS loop:

  * Pop a state from the queue.
  * If it equals the `GOAL` string, reconstruct and print the path via `printSolutionPath(...)` and return (terminate). BFS guarantees this is the shortest-path in number of moves.

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
            int zeroIndex = currentState.puzzle.indexOf('0'); // blank position
```

* `moves` is a precomputed neighbor list for each blank position (0..8). This avoids computing board coordinates repeatedly.
* `zeroIndex` finds the blank position in the current state's string.

```java
            List<State> next = new ArrayList<>();
            for (int nextState : moves[zeroIndex]) {
                String newPuzzle = swap(currentState.puzzle, zeroIndex, nextState);
                next.add(new State(newPuzzle));
            }
```

* For each legal neighbor index for the blank, create a successor `String` by swapping `0` with the tile at `nextState` (using `swap` helper) and wrap it in `State`. Collected into `next` list.

```java
            for(State s : next){
                if (!visitedStates.contains(s.toString())) {
                    visitedStates.add(s.toString());
                    parentMap.put(s.toString(), currentState.toString());
                    queue.add(s);
                }
            }
        }
        System.out.println("No solution found!");
    }
```

* For each generated neighbor `s`:

  * If it hasn't been seen (`visitedStates`), mark it visited, set its parent to the current state, and enqueue it.
* If queue empties without hitting the goal, print `"No solution found!"`.

```java
    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return new String(arr);
    }
```

* Helper: produces a new `String` where characters at indices `i` and `j` are swapped. (Strings are immutable, so we build a `char[]` and new `String`.)

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

* Reconstructs path by following `parentMap` from `endState` back to `null`, reverses the list so it goes start→goal, then prints each board.

```java
    private static void printPuzzle(String state) {
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println();
            System.out.print(state.charAt(i) + " ");
        }
    }
```

* Nicely prints a 3×3 grid for a given state string.

```java
    public static void main(String[] args) {
        String start = "123405678";
        String GOAL = "123456780";
        bfs(start, GOAL);
    }
}
```

* Example `main` that launches BFS on a sample start and goal.

---

# Why BFS is appropriate here

* BFS explores states in increasing order of depth (number of moves from the start). Since all moves have equal cost (1), BFS is guaranteed to find a shortest solution in number of moves.
* The `visitedStates` set prevents cycles and re-enqueueing the same state multiple times.

---

# Time complexity

Let:

* `b` = branching factor (≤ 4 for the 8-puzzle),

* `d` = depth (number of moves) of the shortest solution,

* `N` = number of reachable states explored (≤ `9!/2 = 181,440` for the full solvable 8-puzzle state space).

* **Worst-case time:** BFS expands nodes level by level. Worst-case number of expanded nodes is `O(b^d)`. In practice for the 8-puzzle the search is bounded by the reachable state space `N`, so worst-case time is `O(N)`.

* Each expansion does constant-time neighbor generation (small fixed list) plus string `swap` and hash/set operations. Converting strings and hashing adds constant factors.

So overall: **O(N)** or **O(b^d)** in the theoretical tree bound; constant factors include string ops and hash costs.

---

# Space complexity

* `visitedStates` can store up to `O(N)` state strings.
* `parentMap` can store up to `O(N)` entries (each a pair of strings).
* `queue` can hold up to `O(b^d)` states at the current frontier; in the worst case this is `O(N)`.
* Each state is a 9-character string (small), but the overall memory is dominated by the number of states stored: **O(N)**.

---

# Correctness, guarantees & caveats

* **Correctness:** BFS is complete (will find a solution if one exists) and optimal (returns the minimum number of moves) for uniform-cost moves. The program correctly enqueues neighbors and marks visited on discovery, so each state is enqueued at most once.
* **Caveat — solvability:** Not every permutation of 9 tiles is solvable — the 8-puzzle has two parity classes. The program does not check solvability up front; it will simply exhaust the reachable states and print `"No solution found!"` if start and goal are in different parity classes. A cheap improvement is to test inversion parity and return early if unsolvable.
* **Performance caveat:** Using `String` for states is simple and correct, but `swap()` creates new strings frequently. This is fine for small searches but less efficient for deep or large searches. Alternatives: encode state in an `int` or use `char[]` and pooling.

---

# Practical suggestions / optimizations

1. **Solvability check**: before search compute parity (inversions) of start and goal; if parity differs, skip BFS and report unsolvable. This avoids wasted work.
2. **Use a compact state encoding** (e.g., an `int` or `long` bit-packed) to reduce object allocation and speed hashing.
3. **Precompute neighbor indices** (already done) and consider incremental swap on a `char[]` if you want fewer allocations.
4. **Bidirectional BFS** (meet-in-the-middle) can reduce time & memory by searching from start and goal simultaneously until the frontiers meet — often dramatically faster for puzzles with large branching and shallow depth.
5. **Avoid building the `next` list** if you can generate and process neighbors on the fly (micro-optimization).

---

# Short, clean pseudocode

```
BFS(start, goal):
  visited = empty set
  parent = empty map
  queue = empty FIFO queue

  enqueue start; visited.add(start); parent[start] = null

  while queue not empty:
    current = queue.dequeue()
    if current == goal:
      return reconstruct_path(parent, current)
    for each neighbor of current (swap blank with allowed positions):
      if neighbor not in visited:
        visited.add(neighbor)
        parent[neighbor] = current
        queue.enqueue(neighbor)

  return "no solution"
```

`reconstruct_path` follows `parent` pointers from goal back to start and reverses the list to get the solution sequence.

---

If you want I can:

* add a parity (solvability) check and show how it integrates, or
* convert the implementation to use a compact integer encoding (faster), or
* implement bidirectional BFS to make this solver much faster in practice.

Which improvement would you like me to show?

*/