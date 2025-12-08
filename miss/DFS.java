package MissionariesAndCannibals;

import java.util.*;

public class DFS {

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        boolean boatOnLeft; 

        State(int missionaries, int cannibals, boolean boat) {
            this.missionariesLeft = missionaries;
            this.cannibalsLeft = cannibals;
            this.boatOnLeft = boat;
        }

        @Override
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }

    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int totalMissionaries = 3, totalCannibals = 3;

    public static boolean dfs(State currentState) {
        if (currentState.missionariesLeft == 0 &&
            currentState.cannibalsLeft == 0 &&
            !currentState.boatOnLeft) {

            printSolutionPath(currentState.toString());
            return true;
        }

        visitedStates.add(currentState.toString());

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

            nextStates.add(new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft));
        }

        for (State nextState : nextStates) {
            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dfs(nextState)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isValidState(State state) {
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

    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);

        System.out.println("Steps to solve:");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                "Boat: " + (boatOnLeft ? "Left" : "Right")
            );
        }
    }

    public static void main(String[] args) {
        State startState = new State(3, 3, true);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState)) {
            System.out.println("No solution found!");
        }
    }
}


/*
# Problem statement (short)

Move `M` missionaries and `C` cannibals from the left bank to the right bank using a boat that carries 1 or 2 people. Never leave more cannibals than missionaries on either bank (unless missionaries on that bank are zero). Find any valid sequence of crossings. This program solves the classic `3` missionaries / `3` cannibals instance using **Depth-First Search (DFS)**.

---

# High-level idea

A **state** is `(missionariesLeft, cannibalsLeft, boatOnLeft)`; the algorithm does a recursive DFS from the start state `(3,3,true)` generating all possible legal boat moves `{(1,0),(2,0),(0,1),(0,2),(1,1)}`. It tracks visited states to avoid loops, stores parent pointers to reconstruct the path, and prints the first solution it finds.

---

# Line-by-line explanation

```java
package MissionariesAndCannibals;

import java.util.*;
```

* Declares package and imports Java collections (`HashSet`, `HashMap`, `ArrayList`, etc.).

```java
public class DFS {
```

* Top-level class containing the solver and `main`.

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

        @Override
        public String toString() {
            return missionariesLeft + "," + cannibalsLeft + "," + (boatOnLeft ? "L" : "R");
        }
    }
```

* `State` is a plain data holder for the configuration.
* `toString()` produces a compact key like `"3,2,L"` used by `visitedStates` and `parentMap`.

```java
    static Set<String> visitedStates = new HashSet<>();
    static Map<String, String> parentMap = new HashMap<>();
    static int totalMissionaries = 3, totalCannibals = 3;
```

* `visitedStates`: global `HashSet` of string keys to avoid revisiting states (cycle prevention).
* `parentMap`: `childKey -> parentKey` used to reconstruct the path once goal found.
* `totalMissionaries`, `totalCannibals`: problem parameters (here 3 and 3).

```java
    public static boolean dfs(State currentState) {
        if (currentState.missionariesLeft == 0 &&
            currentState.cannibalsLeft == 0 &&
            !currentState.boatOnLeft) {

            printSolutionPath(currentState.toString());
            return true;
        }
```

* `dfs` is the recursive routine.
* First action: **goal test** — if no one remains on left and boat on right, print path and return `true`.

```java
        visitedStates.add(currentState.toString());
```

* Mark the current state as visited to avoid re-exploring it later.

```java
        int[][] possibleMoves = {
                { 1, 0 }, { 2, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }
        };
```

* All legal boat payloads: 1 missionary, 2 missionaries, 1 cannibal, 2 cannibals, or 1 of each.

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

            nextStates.add(new State(newMissionariesLeft, newCannibalsLeft, newBoatOnLeft));
        }
```

* For each payload, compute the successor state:

  * Flip the boat side `newBoatOnLeft`.
  * If boat was on left, subtract moved people from left counts; otherwise add them back to left (they came from right).
* Collect candidate successors in `nextStates` (they may be invalid — filtered next).

```java
        for (State nextState : nextStates) {
            if (isValidState(nextState) && !visitedStates.contains(nextState.toString())) {
                parentMap.put(nextState.toString(), currentState.toString());
                if (dfs(nextState)) {
                    return true;
                }
            }
        }

        return false;
    }
```

* For each candidate:

  * `isValidState(nextState)` checks bounds and safety (see below).
  * If valid and not visited, record parent and recurse.
  * If recursion returns `true` (goal found deeper), propagate `true` up immediately (stop other branches).
* If no neighbor leads to goal, return `false` (backtrack).

```java
    private static boolean isValidState(State state) {
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

* `isValidState` enforces:

  * left counts within `[0, total]`.
  * safety on **left**: if missionariesLeft > 0 then `missionariesLeft >= cannibalsLeft`.
  * safety on **right**: analogous using `missionariesRight` and `cannibalsRight`.
* Returns `true` only if state is legal.

```java
    private static void printSolutionPath(String endState) {
        List<String> path = new ArrayList<>();
        String current = endState;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
```

* Reconstruct path by following `parentMap` from goal back to start, then reverse to get start→goal order.

```java
        System.out.println("Steps to solve:");
        for (String step : path) {
            String[] parts = step.split(",");
            int missionariesLeft = Integer.parseInt(parts[0]);
            int cannibalsLeft = Integer.parseInt(parts[1]);
            boolean boatOnLeft = parts[2].equals("L");

            System.out.println(
                "Left[Missionaries=" + missionariesLeft + ", Cannibals=" + cannibalsLeft + "] | " +
                "Right[Missionaries=" + (totalMissionaries - missionariesLeft) +
                ", Cannibals=" + (totalCannibals - cannibalsLeft) + "] | " +
                "Boat: " + (boatOnLeft ? "Left" : "Right")
            );
        }
    }
```

* For each state string, parse the counts and print a readable line showing left and right bank composition and boat position.

```java
    public static void main(String[] args) {
        State startState = new State(3, 3, true);
        parentMap.put(startState.toString(), null);

        if (!dfs(startState)) {
            System.out.println("No solution found!");
        }
    }
}
```

* `main` constructs the start `(3,3,boatOnLeft=true)`, sets its parent `null`, calls `dfs`. If `dfs` returns `false`, prints failure.

---

# Data structures used

* `State` objects (small structs).
* `HashSet<String> visitedStates` — O(1) average membership checks to avoid re-exploration.
* `HashMap<String,String> parentMap` — parent pointers for path reconstruction.
* `ArrayList<State>` to hold generated candidate successors.
* Implicit recursion stack for DFS.

---

# Correctness, guarantees & caveats

**Correctness**

* The code generates only valid successor states (filtering with `isValidState`), avoids revisiting states (via `visitedStates`), and prints the first complete solution reached.

**Completeness**

* The search will find *a* solution if one exists and reachable within the finite state graph because the state space is finite and visited prevents infinite loops. However, the order is depth-first — it may find a deep solution before a shorter one.

**Optimality**

* DFS is **not** guaranteed to find the shortest sequence of crossings (it returns the first solution found in DFS order). If you need the minimum number of boat trips, use BFS (or A* with an admissible cost heuristic).

**Visited-set behavior**

* This implementation marks states as visited globally on first visit. For this problem that pruning is correct and desirable (the state graph is small and cycles must be avoided). Global visited can, in some algorithms, prevent finding certain solutions if you rely on different path costs, but here costs are uniform and we only need *a* path.

**Edge cases**

* The code assumes `totalMissionaries` and `totalCannibals` are small integers; state space grows as `(M+1)*(C+1)*2`. For large inputs the naive DFS may become intractable.

---

# Time complexity

Let `M = totalMissionaries`, `C = totalCannibals`, and `S = (M+1)*(C+1)*2` be the size of the state space.

* **Worst-case time:** the algorithm may visit every valid state once, generating up to 5 successors per state, doing O(1) work each:
  [
  O(S)
  ]
  in terms of number of states (with constant factors from move generation and set/map ops).
* In the tree-search model with branching factor `b ≤ 5` and depth `d`, time ~ `O(b^d)` (exponential in depth). For the canonical 3×3 instance `S` is tiny (≤ 32 valid configurations), so runtime is trivial.

---

# Space complexity

* `visitedStates` and `parentMap` can hold up to `O(S)` strings → `O(S)` memory.
* Recursion depth ≤ number of distinct states in worst case → `O(S)` stack space (but practically depth is much smaller).
* So overall **space = O(S)**.

---

# Short pseudocode

```
DFS(start):
  visited = empty set
  parent = empty map
  parent[start] = null
  return dfs_rec(start)

function dfs_rec(state):
  if state is goal:
    print path using parent; return true

  visited.add(state)

  for each move in [(1,0),(2,0),(0,1),(0,2),(1,1)]:
    next = applyMove(state, move)   // subtract or add depending on boat side
    if isValid(next) and next not in visited:
      parent[next] = state
      if dfs_rec(next) return true

  return false
```

---

# Practical suggestions / possible improvements

* **Find shortest solution:** use `BFS` (guaranteed shortest when each move cost = 1).
* **Find optimal-cost w.r.t. different cost measures:** use `A*` with `g` (moves so far) and an admissible `h` (e.g., `ceil((peopleLeft)/2)` as a lower bound on remaining trips).
* **State encoding:** `toString()` is convenient but an integer bit-encoding or small `int[]` is faster and uses less memory if you scale up.
* **Heuristics / pruning:** domain knowledge can prune impossible sequences earlier (but for 3×3 problem not necessary).

---

If you’d like, I can:

* provide a **BFS** version (guaranteed minimum boat crossings) and explain differences, or
* produce an **A*** version with an admissible heuristic that finds optimal moves and show it on this instance.

Which would you prefer?
 */