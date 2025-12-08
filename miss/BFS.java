package MissionariesAndCannibals;

import java.util.*;

public class BFS {

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft;

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

      
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }

    public static void bfs(int totalMissionaries, int totalCannibals) {

        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Queue<State> queue = new LinkedList<>();

        State startState = new State(3, 3, true);

        queue.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.missionariesLeft == 0 &&
                    currentState.cannibalsLeft == 0 &&
                    !currentState.boatOnLeft) {

                printSolutionPath(totalMissionaries, totalCannibals, parentMap, currentState.toString());
                return;
            }

            int[][] possibleMoves = {
                    { 1, 0 }, { 2, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }
            };

            List<State> nextStates = new ArrayList<>();
        
            for (int[] move : possibleMoves) {
                int moveMissionaries = move[0];
                int moveCannibals = move[1];

                int newMissionariesLeft = currentState.missionariesLeft;
                int newCannibalsLeft = currentState.cannibalsLeft;
                boolean newBoatOnLeft = !currentState.boatOnLeft;

                if (currentState.boatOnLeft) {
                
                    newMissionariesLeft -= moveMissionaries;
                    newCannibalsLeft -= moveCannibals;
                } else {
                    newMissionariesLeft += moveMissionaries;
                    newCannibalsLeft += moveCannibals;
                }

                State nextState = new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft);
                nextStates.add(nextState);

            }
            for (State nextState : nextStates) {
                if (isValidState(totalMissionaries, totalCannibals, nextState)
                        && !visitedStates.contains(nextState.toString())) {
                    visitedStates.add(nextState.toString());
                    parentMap.put(nextState.toString(), currentState.toString());
                    queue.add(nextState);
                }
            }
        }

    }

    private static boolean isValidState(int totalMissionaries, int totalCannibals, State state) {
        int missionariesRight = totalMissionaries - state.missionariesLeft;
        int cannibalsRight = totalCannibals - state.cannibalsLeft;

        if (state.missionariesLeft < 0 || state.cannibalsLeft < 0 ||
                state.missionariesLeft > totalMissionaries || state.cannibalsLeft > totalCannibals)
            return false;

        if ((state.missionariesLeft > 0 && state.missionariesLeft < state.cannibalsLeft) ||
                (missionariesRight > 0 && missionariesRight < cannibalsRight))
            return false;

        return true;
    }

    private static void printSolutionPath(int totalMissionaries, int totalCannibals, Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (BFS - Shortest Path):");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                    "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                            "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                            ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                            "Boat: " + (boatOnLeft ? "Left" : "Right"));
        }
    }

    public static void main(String[] args) {

        int totalMissionaries = 3, totalCannibals = 3;
        bfs(totalMissionaries, totalCannibals);
    }
}


/*
# BFS for Missionaries & Cannibals — explained

Nice — this is a clean **Breadth-First Search (BFS)** implementation that finds the shortest sequence of crossings that moves all missionaries and cannibals from the left bank to the right bank (for the 3-missionary / 3-cannibal classic). I’ll explain the code block-by-block (calling out important lines), describe the problem, list data structures, show complexity, call out small bugs/edge cases, give a short pseudocode summary, and suggest improvements.

---

# Problem statement (short)

You have `M` missionaries and `C` cannibals on the left bank and a boat that carries up to 2 people. You may move 1 or 2 people per crossing. At no time may cannibals outnumber missionaries on either bank (unless there are zero missionaries on that bank). Start with everyone on the left bank; find the shortest sequence of crossings that moves everyone safely to the right bank.

---

# High-level algorithm

The program does a BFS over the state graph where each state is `(missionariesLeft, cannibalsLeft, boatOnLeft)`. BFS explores states level by level, so the first time the goal is reached the path found is guaranteed to be the shortest in number of crossings.

---

# Code walkthrough (block-by-block, important lines)

```java
package MissionariesAndCannibals;
import java.util.*;
public class BFS {
```

* Package and imports; class declaration.

```java
    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft;

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }
```

* `State` is a small data class holding:

  * `missionariesLeft` — number of missionaries on the left bank,
  * `cannibalsLeft` — number of cannibals on left,
  * `boatOnLeft` — `true` if boat is on left bank.
* `toString()` serializes a state like `"3,3,L"` and is used as the key for sets/maps.

```java
    public static void bfs(int totalMissionaries, int totalCannibals) {
        Set<String> visitedStates = new HashSet<>();
        Map<String, String> parentMap = new HashMap<>();
        Queue<State> queue = new LinkedList<>();

        State startState = new State(3, 3, true);
```

* `visitedStates`: `HashSet` to avoid revisiting a state (prevents cycles).
* `parentMap`: `HashMap` mapping a child state string → parent state string for path reconstruction.
* `queue`: BFS frontier implemented with `LinkedList`.
* **Note / small bug:** `startState` is hard-coded to `(3,3,true)`. It should use the `totalMissionaries` and `totalCannibals` parameters (e.g. `new State(totalMissionaries, totalCannibals, true)`) to generalize the method.

```java
        queue.add(startState);
        visitedStates.add(startState.toString());
        parentMap.put(startState.toString(), null);
```

* Initialize BFS: enqueue start, mark visited, set parent of start = `null`.

```java
        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.missionariesLeft == 0 &&
                    currentState.cannibalsLeft == 0 &&
                    !currentState.boatOnLeft) {

                printSolutionPath(totalMissionaries, totalCannibals, parentMap, currentState.toString());
                return;
            }
```

* Main loop: dequeue `currentState`.
* If it’s the goal `(0,0, boat on right)`, reconstruct and print the path via `parentMap` and return. Because BFS, this will be the shortest path in number of crossings.

```java
            int[][] possibleMoves = {
                    { 1, 0 }, { 2, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }
            };
```

* The allowed boat loads: 1 missionary, 2 missionaries, 1 cannibal, 2 cannibals, or 1 missionary + 1 cannibal.

```java
            List<State> nextStates = new ArrayList<>();
        
            for (int[] move : possibleMoves) {
                int moveMissionaries = move[0];
                int moveCannibals = move[1];

                int newMissionariesLeft = currentState.missionariesLeft;
                int newCannibalsLeft = currentState.cannibalsLeft;
                boolean newBoatOnLeft = !currentState.boatOnLeft;

                if (currentState.boatOnLeft) {
                    newMissionariesLeft -= moveMissionaries;
                    newCannibalsLeft -= moveCannibals;
                } else {
                    newMissionariesLeft += moveMissionaries;
                    newCannibalsLeft += moveCannibals;
                }

                State nextState = new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft);
                nextStates.add(nextState);
            }
```

* For each possible move, compute the successor `nextState`:

  * If the boat was on left, subtract moved people from left; if on right, add them back to left (boat crosses back).
  * Toggle `boatOnLeft`.
  * Collect successors in `nextStates`. (Generating them first makes the logic easy to read.)

```java
            for (State nextState : nextStates) {
                if (isValidState(totalMissionaries, totalCannibals, nextState)
                        && !visitedStates.contains(nextState.toString())) {
                    visitedStates.add(nextState.toString());
                    parentMap.put(nextState.toString(), currentState.toString());
                    queue.add(nextState);
                }
            }
        }
    }
```

* For every generated successor:

  * `isValidState(...)` checks bounds and the cannibal-outnumber rule on both banks.
  * If the successor is valid and not visited, mark it visited, record its parent, and enqueue it.
* BFS continues until queue empties or goal is found.

```java
    private static boolean isValidState(int totalMissionaries, int totalCannibals, State state) {
        int missionariesRight = totalMissionaries - state.missionariesLeft;
        int cannibalsRight = totalCannibals - state.cannibalsLeft;

        if (state.missionariesLeft < 0 || state.cannibalsLeft < 0 ||
                state.missionariesLeft > totalMissionaries || state.cannibalsLeft > totalCannibals)
            return false;

        if ((state.missionariesLeft > 0 && state.missionariesLeft < state.cannibalsLeft) ||
                (missionariesRight > 0 && missionariesRight < cannibalsRight))
            return false;

        return true;
    }
```

* `isValidState` rules:

  1. Left counts are within `[0, total]`.
  2. On **left bank**: if there is at least one missionary and missionaries < cannibals → invalid.
  3. On **right bank**: same check with `missionariesRight` and `cannibalsRight`.
* If both checks pass, the state is valid.

```java
    private static void printSolutionPath(int totalMissionaries, int totalCannibals, Map<String, String> parentMap, String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve (BFS - Shortest Path):");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                    "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                            "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                            ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                            "Boat: " + (boatOnLeft ? "Left" : "Right"));
        }
    }
```

* Reconstructs the path by following `parentMap` from goal back to start, reverses it, and prints each step in a human-readable left/right format.

```java
    public static void main(String[] args) {
        int totalMissionaries = 3, totalCannibals = 3;
        bfs(totalMissionaries, totalCannibals);
    }
}
```

* `main` calls `bfs(3,3)`. Because `bfs` currently hardcodes the start as `(3,3,true)`, this matches the parameters — but change the hardcode if you want other sizes.

---

# Data structures used

* `State` objects (small data holder).
* `Queue<State> queue = new LinkedList<>()` — BFS frontier (FIFO).
* `Set<String> visitedStates = new HashSet<>()` — O(1) average membership checks to avoid revisits.
* `Map<String,String> parentMap = new HashMap<>()` — to reconstruct the path.
* `List<State> nextStates = new ArrayList<>()` — temporary list for generated successors.

---

# Correctness notes & edge cases

* BFS guarantees the **shortest path in number of boat crossings**, because it explores states by increasing depth.
* `isValidState` correctly enforces safety constraints.
* **Hardcoded start bug:** `new State(3,3,true)` prevents calling `bfs` with other initial counts. Fix: `new State(totalMissionaries, totalCannibals, true)`.
* The code does *not* check global solvability before searching. Some parameterizations may be unsolvable (e.g., improper totals/constraints); BFS will exhaust reachable states and simply return without printing a solution (you might want an explicit "No solution" message after the loop).
* For `M=3,C=3`, state space is tiny; BFS is fast.

---

# Time & space complexity

Let `M = totalMissionaries`, `C = totalCannibals`.

* Number of possible unique states `N ≤ (M+1) * (C+1) * 2`
  (missionariesLeft ranges 0..M, cannibalsLeft 0..C, boat 2 positions).

  * Example: M=C=3 → `N ≤ 4*4*2 = 32` states.

* **Time complexity:** BFS visits each reachable state at most once and generates up to constant `b` successors per state (`b ≤ 5` here). Operations per state are O(1) on average (hash lookups, queue ops). So time ≈ **O(N)** (more precisely `O(N * b)` but `b` is constant). In general for such puzzle graphs BFS is linear in the number of reachable states.

* **Space complexity:** `visitedStates`, `parentMap`, and `queue` can together store up to `O(N)` states → **O(N)** space.

* For the classic 3/3 problem these bounds are tiny (N ≤ 32). For larger M/C growth is polynomial in M and C (specifically `(M+1)*(C+1)*2`), not exponential.

---

# Short pseudocode

```
function BFS(M, C):
    visited = empty set
    parent = empty map
    queue = empty queue

    start = (M, C, boatOnLeft=true)    // fix hardcode
    queue.enqueue(start)
    visited.add(str(start))
    parent[str(start)] = null

    while queue not empty:
        current = queue.dequeue()
        if current == goal (0,0,boatOnLeft=false):
            print_path(parent, current)
            return

        for each move in [(1,0),(2,0),(0,1),(0,2),(1,1)]:
            successor = apply_move(current, move)
            if isValid(successor, M, C) and str(successor) not in visited:
                visited.add(str(successor))
                parent[str(successor)] = str(current)
                queue.enqueue(successor)

    print "No solution found"
```

---

# Suggestions / small improvements

1. **Fix the hardcoded start**: use `new State(totalMissionaries, totalCannibals, true)`.
2. **Return an explicit message** if BFS finishes with no solution (currently it silently ends).
3. **Add solvability check** (optional): a simple parity/inversion check or basic constraints to skip pointless search for impossible inputs.
4. **Make `State` immutable and override `equals`/`hashCode`** so you can store `State` objects directly in `visitedStates` and `parentMap` (currently strings are fine and small).
5. **Allow variable boat capacity** or parameterize moves so algorithm is reusable for variants.
6. **Print the number of steps / cost** (like g-cost) along with the path for extra clarity.

---

If you want, I can:

* Provide the one-line fix (replace the hardcoded start) and return the corrected full source, or
* Modify the program to print `"No solution found"` when BFS ends without finding a goal, or
* Convert the representation to use `State` keys with overridden `hashCode/equals` instead of strings.

Which of those would you like me to show?
 */